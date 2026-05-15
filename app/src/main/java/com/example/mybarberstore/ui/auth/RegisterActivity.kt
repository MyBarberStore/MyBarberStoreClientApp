package com.example.mybarberstore.ui.auth

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mybarberstore.R
import com.example.mybarberstore.viewModel.RegisterViewModel
import com.google.android.material.textfield.TextInputEditText

class RegisterActivity : AppCompatActivity() {

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etName = findViewById<TextInputEditText>(R.id.etName)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val etPhone = findViewById<TextInputEditText>(R.id.etPhone)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvLogin = findViewById<TextView>(R.id.tvLogin)

        btnRegister.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val pass = etPassword.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty() && pass.isNotEmpty()) {
                viewModel.doRegister(name, email, phone, pass)
            } else {
                Toast.makeText(this, getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show()
            }
        }

        tvLogin.setOnClickListener {
            finish() // Vuelve atrás (al Login)
        }

        viewModel.registerResponse.observe(this) { response ->
            if (response == null) {
                Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show()
            } else if (response.isSuccessful) {
                Toast.makeText(this, getString(R.string.success_register), Toast.LENGTH_LONG).show()
                finish() // Cierra registro y vuelve al login para entrar
            } else {
                Toast.makeText(this, "Error al registrar: ¿El email ya existe?", Toast.LENGTH_SHORT).show()
            }
        }
    }
}