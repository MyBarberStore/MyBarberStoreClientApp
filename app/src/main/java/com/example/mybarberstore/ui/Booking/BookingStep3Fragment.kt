package com.example.mybarberstore.ui.Booking

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybarberstore.R
import com.example.mybarberstore.data.api.RetroFitClient
import com.example.mybarberstore.ui.adapter.HourAdapter
import com.example.mybarberstore.viewModel.BookingViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale

class BookingStep3Fragment : Fragment(R.layout.fragment_booking_step3) {

    private val bookingViewModel: BookingViewModel by viewModels({ requireParentFragment() })
    private lateinit var rvHours: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnSelectDate = view.findViewById<Button>(R.id.btnSelectDate)
        val tvSelectedDate = view.findViewById<TextView>(R.id.tvSelectedDate)

        rvHours = view.findViewById(R.id.rvHours)
        // Configuramos 3 columnas
        rvHours.layoutManager = GridLayoutManager(requireContext(), 3)

        btnSelectDate.setOnClickListener {
            showDatePicker(tvSelectedDate)
        }
    }

    private fun showDatePicker(tvDate: TextView) {
        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Selecciona el día de tu cita")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        picker.addOnPositiveButtonClickListener { selection ->
            // Convertimos los milisegundos a formato legible (yyyy-MM-dd)
            val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                Date(
                    selection
                )
            )
            val dateDisplay = SimpleDateFormat("dd 'de' MMMM", Locale("es", "ES")).format(Date(selection))

            tvDate.text = dateDisplay
            bookingViewModel.selectedDate.value = dateString

            // Una vez elegida la fecha, pedimos las horas a la API
            loadAvailableHours(dateString)
        }

        picker.show(childFragmentManager, "DATE_PICKER")
    }

    private fun loadAvailableHours(date: String) {
        val barberId = bookingViewModel.selectedBarber.value?.id ?: return

        lifecycleScope.launch {
            try {
                val response = RetroFitClient.instance.getAvailableHours(barberId, date)
                if (response.isSuccessful && response.body() != null) {
                    val adapter = HourAdapter(response.body()!!) { hour ->
                        // Guardamos la hora y... ¡estamos listos para el Paso 4 (Resumen)!
                        bookingViewModel.selectedTime.value = hour

                        // Añadimos un pequeño botón de "Siguiente" o pasamos automáticamente
                        (parentFragment as? BookingFragment)?.showStep(4)
                    }
                    rvHours.adapter = adapter
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al cargar horas", Toast.LENGTH_SHORT).show()
            }
        }
    }
}