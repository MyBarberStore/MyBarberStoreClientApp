package com.example.mybarberstore.ui.Booking

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mybarberstore.R
import com.example.mybarberstore.data.api.RetroFitClient
import com.example.mybarberstore.data.model.AppointmentRequest
import com.example.mybarberstore.ui.HomeFragment
import com.example.mybarberstore.utils.SessionManager
import com.example.mybarberstore.viewModel.BookingViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
class BookingStep4Fragment : Fragment(R.layout.fragment_booking_step4) {

    private val viewModel: BookingViewModel by viewModels({ requireParentFragment() })
    private lateinit var sessionManager: SessionManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

        // 1. Vincular las vistas del XML
        val tvService = view.findViewById<TextView>(R.id.tvSummaryService)
        val tvBarber = view.findViewById<TextView>(R.id.tvSummaryBarber)
        val tvDate = view.findViewById<TextView>(R.id.tvSummaryDate)
        val tvTime = view.findViewById<TextView>(R.id.tvSummaryTime)
        val btnConfirm = view.findViewById<Button>(R.id.btnConfirmBooking)

        // 2. Volcar los datos del ViewModel a la UI
        tvService.text = viewModel.selectedService.value?.name
        tvBarber.text = viewModel.selectedBarber.value?.name
        tvTime.text = viewModel.selectedTime.value

        // Formatear la fecha para que sea bonita (ej: "viernes, 15 de mayo")
        viewModel.selectedDate.value?.let { rawDate ->
            try {
                val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(rawDate)
                val formatter = SimpleDateFormat("EEEE, d 'de' MMMM", Locale("es", "ES"))
                tvDate.text = formatter.format(date!!).replaceFirstChar { it.uppercase() }
            } catch (e: Exception) {
                tvDate.text = rawDate
            }
        }

        // 3. Acción del botón confirmar
        btnConfirm.setOnClickListener {
            enviarReserva()
        }
    }

    private fun enviarReserva() {
        // Recuperamos el ID del cliente que guardamos al iniciar sesión
        // Asegúrate de que en tu SessionManager tengas un método fetchClientId()
        val clientId = sessionManager.fetchClientId()

        val request = AppointmentRequest(
            serviceId = viewModel.selectedService.value?.id ?: 0L,
            employeeId = viewModel.selectedBarber.value?.id ?: 0L,
            clientId = clientId,
            status = "CONFIRMED", // Estado inicial por defecto
            date = viewModel.selectedDate.value ?: "",
            startTime = viewModel.selectedTime.value ?: ""
        )

        lifecycleScope.launch {
            try {
                val response = RetroFitClient.instance.createAppointment(request)

                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "¡Cita confirmada con éxito!", Toast.LENGTH_LONG).show()

                    // Limpiamos los datos de la reserva actual
                    viewModel.resetBooking()

                    // Volvemos a la pantalla de Inicio (HomeFragment)
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.mainContainer, HomeFragment())
                        .commit()
                } else {
                    Toast.makeText(requireContext(), "Error al reservar: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        }
    }
}