package com.example.mybarberstore.viewModel



import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mybarberstore.data.model.Barber
import com.example.mybarberstore.data.model.Service


class BookingViewModel : ViewModel() {

    // Estas variables guardarán las elecciones del usuario en cada paso
    val selectedService = MutableLiveData<Service?>()
    val selectedBarber = MutableLiveData<Barber?>()
    val selectedDate = MutableLiveData<String?>() // Ejemplo: "2026-05-14"
    val selectedTime = MutableLiveData<String?>() // Ejemplo: "10:30"


    // Función opcional para limpiar los datos si el usuario cancela la reserva
    fun resetBooking() {
        selectedService.value = null
        selectedBarber.value = null
        selectedDate.value = null
        selectedTime.value = null
    }
}