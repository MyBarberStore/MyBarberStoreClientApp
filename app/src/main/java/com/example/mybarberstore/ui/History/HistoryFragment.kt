package com.example.mybarberstore.ui.History

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybarberstore.R
import com.example.mybarberstore.data.api.RetroFitClient
import com.example.mybarberstore.data.model.AppointmentResponse
import com.example.mybarberstore.ui.adapter.HistoryAdapter
import com.example.mybarberstore.utils.SessionManager
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream

class HistoryFragment : Fragment(R.layout.fragment_history) {

    private lateinit var sessionManager: SessionManager
    private lateinit var rvNext: RecyclerView
    private lateinit var rvPast: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())

        rvNext = view.findViewById<RecyclerView>(R.id.rvNextAppointments)
        rvPast = view.findViewById<RecyclerView>(R.id.rvPastAppointments)

        rvNext.layoutManager = LinearLayoutManager(requireContext())
        rvPast.layoutManager = LinearLayoutManager(requireContext())

        loadHistory()
    }

    private fun loadHistory() {
        val clientId = sessionManager.fetchClientId()

        lifecycleScope.launch {
            try {
                val response = RetroFitClient.instance.getClientHistory(clientId)
                if (response.isSuccessful && response.body() != null) {
                    val allApps = response.body()!!

                    val nextApps = allApps.filter { it.status == "CONFIRMED" }
                    val pastApps = allApps.filter { it.status == "CANCELLED" || it.status == "COMPLETED" }

                    rvNext.adapter = HistoryAdapter(
                        nextApps,
                        onCancelClick = { app -> mostrarDialogoConfirmacion(app) },
                        onInvoiceClick = { /* No hay factura */ }
                    )

                    rvPast.adapter = HistoryAdapter(
                        pastApps,
                        onCancelClick = { /* No se cancela */ },
                        onInvoiceClick = { app -> descargarFactura(app) }
                    )
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error: ${e.message}")
                context?.let {
                    val errorMsg = getString(R.string.error_technical, e.localizedMessage)
                    Toast.makeText(it, errorMsg, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun mostrarDialogoConfirmacion(app: AppointmentResponse) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_cancel_title))
            .setMessage(getString(R.string.dialog_cancel_message, app.serviceName))
            .setPositiveButton(getString(R.string.btn_yes_cancel)) { _, _ ->
                cancelarCita(app.id)
            }
            .setNegativeButton(getString(R.string.btn_no), null)
            .show()
    }

    private fun cancelarCita(appointmentId: Long) {
        lifecycleScope.launch {
            try {
                val response = RetroFitClient.instance.cancelAppointment(appointmentId)
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), getString(R.string.toast_appointment_cancelled), Toast.LENGTH_SHORT).show()
                    loadHistory()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), getString(R.string.toast_error_cancel), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun descargarFactura(app: AppointmentResponse) {
        lifecycleScope.launch {
            try {
                val response = RetroFitClient.instance.downloadInvoice(app.id)
                if (response.isSuccessful && response.body() != null) {
                    val file = saveFileToDisk(response.body()!!, "Factura_${app.id}.pdf")
                    if (file != null) {
                        openPdf(file)
                    }
                }
            } catch (e: Exception) {
                Log.e("PDF_ERROR", "Error: ${e.message}")
                Toast.makeText(requireContext(), getString(R.string.toast_error_connection), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveFileToDisk(body: ResponseBody, fileName: String): File? {
        return try {
            val destinationFile = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)
            body.byteStream().use { inputStream ->
                FileOutputStream(destinationFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            destinationFile
        } catch (e: Exception) {
            null
        }
    }

    private fun openPdf(file: File) {
        val authority = "${requireContext().packageName}.fileprovider"
        val uri = FileProvider.getUriForFile(requireContext(), authority, file)

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        }

        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), getString(R.string.toast_no_pdf_app), Toast.LENGTH_SHORT).show()
        }
    }
}