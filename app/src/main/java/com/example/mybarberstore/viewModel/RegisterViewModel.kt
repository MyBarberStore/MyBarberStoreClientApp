package com.example.mybarberstore.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybarberstore.data.model.RegisterRequest
import com.example.mybarberstore.data.repository.AuthRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class RegisterViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _registerResponse = MutableLiveData<Response<Void>?>()
    val registerResponse: LiveData<Response<Void>?> = _registerResponse

    fun doRegister(name: String, email: String, phone: String, pass: String) {
        viewModelScope.launch {
            val request = RegisterRequest(name, email, pass, phone)
            val result = repository.register(request)
            _registerResponse.value = result
        }
    }
}