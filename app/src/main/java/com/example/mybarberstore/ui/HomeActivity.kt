package com.example.mybarberstore.ui
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mybarberstore.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import com.example.mybarberstore.data.api.RetroFitClient
import com.example.mybarberstore.ui.Booking.BookingFragment
import com.example.mybarberstore.ui.Booking.BookingStep1Fragment
import com.example.mybarberstore.ui.History.HistoryFragment
import com.example.mybarberstore.utils.SessionManager


class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Inicializamos y pasamos el token globalmente
        val sessionManager = SessionManager(this)
        RetroFitClient.authToken = sessionManager.fetchAuthToken()

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        // 1. Cargar el fragmento de Inicio por defecto al abrir la App
        // Esto evita que el hueco se quede vacío al empezar
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }

        // 2. Escuchar los clics del menú inferior
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.nav_book -> {
                   replaceFragment(BookingFragment())
                    true
                }
                R.id.nav_history -> {
                    replaceFragment(HistoryFragment())
                    true
                }

                else -> false
            }
        }
    }

    /**
     * Función auxiliar para intercambiar los fragmentos de forma fluida
     */
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        // Añadimos una animación sencilla de fundido para que el cambio no sea brusco
        fragmentTransaction.setCustomAnimations(
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )

        // Reemplazamos el contenido del contenedor por el nuevo fragmento
        fragmentTransaction.replace(R.id.mainContainer, fragment)
        fragmentTransaction.commit()
    }
}