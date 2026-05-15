package com.example.mybarberstore.ui.Booking

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.mybarberstore.R

class BookingFragment : Fragment(R.layout.fragment_booking) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Cargar el paso 1 por defecto
        if (savedInstanceState == null) {
            showStep(1)
        }

    }

    fun showStep(step: Int) {
        val fragment = when (step) {
            1 -> BookingStep1Fragment()
            2 -> BookingStep2Fragment()
            3 -> BookingStep3Fragment()
            4 -> BookingStep4Fragment()
            else -> BookingStep1Fragment()
        }

        // IMPORTANTE: Usamos childFragmentManager porque es un fragmento dentro de otro
        childFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            .replace(R.id.bookingStepContainer, fragment)
            .commit()

        updateUI(step)
    }

    private fun updateUI(step: Int) {
        // 1. Obtenemos las referencias a los círculos
        val tvStep1 = view?.findViewById<TextView>(R.id.tvStep1)
        val tvStep2 = view?.findViewById<TextView>(R.id.tvStep2)
        val tvStep3 = view?.findViewById<TextView>(R.id.tvStep3)
        val tvStep4 = view?.findViewById<TextView>(R.id.tvStep4)

        // 2. Metemos los círculos en una lista para manejarlos fácilmente
        val steps = listOf(tvStep1, tvStep2, tvStep3, tvStep4)

        // 3. Recorremos la lista y aplicamos el estilo
        steps.forEachIndexed { index, textView ->
            val currentCircleStep = index + 1

            if (currentCircleStep == step) {
                // ESTADO ACTIVO: El círculo del paso en el que estamos
                textView?.setBackgroundResource(R.drawable.bg_circle_step_active)
                textView?.setTextColor(android.graphics.Color.WHITE)
            } else if (currentCircleStep < step) {
                // OPCIONAL - ESTADO COMPLETADO: Círculos de pasos que ya pasamos
                // Si quieres que los anteriores sigan resaltados, usa el mismo 'active'
                textView?.setBackgroundResource(R.drawable.bg_circle_step_active)
                textView?.setTextColor(android.graphics.Color.WHITE)
            } else {
                // ESTADO INACTIVO: Círculos de pasos que aún no han llegado
                textView?.setBackgroundResource(R.drawable.bg_circle_step_inactive)
                textView?.setTextColor(android.graphics.Color.parseColor("#7F8C8D"))
            }
        }
    }
}