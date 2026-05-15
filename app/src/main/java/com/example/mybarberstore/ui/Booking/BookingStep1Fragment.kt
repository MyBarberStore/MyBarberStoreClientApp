package com.example.mybarberstore.ui.Booking

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybarberstore.R
import com.example.mybarberstore.data.api.ApiService
import com.example.mybarberstore.data.api.RetroFitClient
import com.example.mybarberstore.ui.adapter.ServiceAdapter
import com.example.mybarberstore.viewModel.BookingViewModel
import kotlinx.coroutines.launch

class BookingStep1Fragment : Fragment(R.layout.fragment_booking_step1) {


    private lateinit var rvServices: RecyclerView
    private lateinit var apiService: ApiService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvServices = view.findViewById(R.id.rvServices)
        rvServices.layoutManager = LinearLayoutManager(requireContext())

        // Inicializar Retrofit
        apiService = RetroFitClient.instance

        loadServices()
    }

    private fun loadServices() {
        val bookingViewModel: BookingViewModel by viewModels({ requireParentFragment() })

        lifecycleScope.launch {
            try {
                // Llamada a la API a través de Retrofit
                val response = apiService.getServices()

                if (response.isSuccessful && response.body() != null) {
                    val servicesList = response.body()!!

                    // Configuramos el adaptador con la lista recibida
                    val adapter = ServiceAdapter(servicesList) { service ->

                        //  Guardar servicio seleccionado en el ViewModel
                        // Esto es vital para que al final del proceso sepamos qué se reservó
                        bookingViewModel.selectedService.value = service

                        //  Saltar al Paso 2 en el Fragmento Padre
                        (parentFragment as? BookingFragment)?.showStep(2)
                    }

                    rvServices.adapter = adapter

                } else {
                    // Si el servidor responde pero con un error (ej. 500)
                    Toast.makeText(requireContext(), "Error al obtener servicios: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // Si no hay internet, la IP es mala o el servidor está apagado
                Log.e("API_ERROR", "Fallo al conectar: ${e.message}")
                Toast.makeText(requireContext(), "Error de red. ¿Está el servidor encendido?", Toast.LENGTH_LONG).show()
            }
        }
    }
}