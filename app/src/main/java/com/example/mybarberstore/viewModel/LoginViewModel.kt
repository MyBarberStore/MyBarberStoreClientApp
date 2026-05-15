package com.example.mybarberstore.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybarberstore.data.model.LoginRequest
import com.example.mybarberstore.data.model.LoginResponse
import com.example.mybarberstore.data.repository.AuthRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel : ViewModel() {
    private val repository = AuthRepository()

    // LiveData que la Activity observará (puede ser nulo si falla la conexión)
    private val _loginResponse = MutableLiveData<Response<LoginResponse>?>()
    val loginResponse: LiveData<Response<LoginResponse>?> = _loginResponse

    fun doLogin(email: String, pass: String) {
        // Lanzamos una corrutina (hilo secundario)
        viewModelScope.launch {
            val result = repository.login(email, pass)
            _loginResponse.value = result

        }
    }
}