package com.example.mybarberstore.ui.auth

import android.content.Intent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import com.example.mybarberstore.R
import com.example.mybarberstore.ui.HomeActivity
import com.example.mybarberstore.utils.SessionManager
import com.example.mybarberstore.viewModel.LoginViewModel
import com.google.android.material.textfield.TextInputEditText


class LoginActivity : AppCompatActivity() {

    // Inicializamos el ViewModel
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sessionManager = SessionManager(this)

        //  Referencias del XML
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvCreateAccount = findViewById<TextView>(R.id.tvCreateAccount)

        //  Click del Botón Login
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val pass = etPassword.text.toString().trim()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                viewModel.doLogin(email, pass)


            } else {
                Toast.makeText(this, getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show()
            }
        }

        //  Click para ir a Registro
        tvCreateAccount.setOnClickListener {
             val intent = Intent(this, RegisterActivity::class.java)
             startActivity(intent)
        }

        // 4. Observar el resultado del ViewModel
        viewModel.loginResponse.observe(this) { response ->
            if (response == null) {
                // Error de conexión (Exception en el repositorio)
                Toast.makeText(this, "Error de conexión con el servidor", Toast.LENGTH_SHORT).show()
            } else if (response.isSuccessful) {
                // ¡ÉXITO!
                val loginData = response.body()
                loginData?.let {
                    sessionManager.saveAuthToken(it.token)
                    sessionManager.saveUserName(it.user.name)
                    sessionManager.saveClientId(it.user.id)

                    Toast.makeText(this, getString(R.string.success_login, it.user.name), Toast.LENGTH_SHORT).show()

                    // Navegar a la Home
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish() // Cerramos el login
                }
            } else {
                // Error de credenciales
                Toast.makeText(this, getString(R.string.error_login), Toast.LENGTH_SHORT).show()
            }
        }
    }
}