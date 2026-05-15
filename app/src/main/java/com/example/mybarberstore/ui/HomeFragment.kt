package com.example.mybarberstore.ui

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mybarberstore.R
import com.example.mybarberstore.ui.auth.LoginActivity
import com.example.mybarberstore.utils.SessionManager
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.io.File

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var sessionManager: SessionManager
    private var map: MapView? = null // Guardamos la referencia para onResume/onPause

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Contexto: En Fragmentos usamos requireContext() en lugar de 'this'
        sessionManager = SessionManager(requireContext())

        // 2. Vistas: Debemos usar 'view.findViewById'
        val tvWelcomeUser = view.findViewById<TextView>(R.id.tvWelcomeUser)
        val btnLogout = view.findViewById<ImageView>(R.id.btnLogout)


        // Lógica de Logout
        btnLogout.setOnClickListener {
            sessionManager.saveAuthToken("")
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            activity?.finish() // Finalizamos la Activity que contiene al Fragment
        }

        // 3. Configuración del MAPA (OSM)
        val ctx = requireContext()
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))

        val basePath = File(ctx.filesDir, "osmdroid")
        Configuration.getInstance().osmdroidBasePath = basePath
        Configuration.getInstance().osmdroidTileCache = File(basePath, "tiles")
        Configuration.getInstance().userAgentValue = ctx.packageName

        map = view.findViewById(R.id.mapViewHome)
        map?.let {
            it.setMultiTouchControls(true)
            val startPoint = GeoPoint(37.8882, -4.7794)
            val mapController = it.controller
            mapController.setZoom(17.5)
            mapController.setCenter(startPoint)

            val startMarker = Marker(it)
            startMarker.position = startPoint
            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            startMarker.title = "BarberPro Central"
            it.overlays.add(startMarker)
        }
    }

    // Usamos la propiedad 'map' que guardamos arriba
    override fun onResume() {
        super.onResume()
        map?.onResume()
    }

    override fun onPause() {
        super.onPause()
        map?.onPause()
    }
}