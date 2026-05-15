package com.example.mybarberstore.ui.Booking

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybarberstore.R
import com.example.mybarberstore.data.api.RetroFitClient
import com.example.mybarberstore.ui.adapter.BarberAdapter
import com.example.mybarberstore.viewModel.BookingViewModel
import kotlinx.coroutines.launch

class BookingStep2Fragment : Fragment(R.layout.fragment_booking_step2) {

    private lateinit var rvBarbers: RecyclerView
    private val bookingViewModel: BookingViewModel by viewModels({ requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvBarbers = view.findViewById(R.id.rvBarbers)
        rvBarbers.layoutManager = LinearLayoutManager(requireContext())

        loadBarbers()
    }

    private fun loadBarbers() {
        lifecycleScope.launch {
            try {
                // Ya no hace falta pasar el token, el Interceptor lo hace solo
                val response = RetroFitClient.instance.getBarbers()

                if (response.isSuccessful && response.body() != null) {
                    val adapter = BarberAdapter(response.body()!!) { barber ->
                        //  Guardar elección
                        bookingViewModel.selectedBarber.value = barber

                        //  Pasar al Paso 3 (Fecha)
                        (parentFragment as? BookingFragment)?.showStep(3)
                    }
                    rvBarbers.adapter = adapter
                }
            } catch (e: Exception) {
                Log.e("STEP2_ERROR", e.message ?: "Error desconocido")
            }
        }
    }
}