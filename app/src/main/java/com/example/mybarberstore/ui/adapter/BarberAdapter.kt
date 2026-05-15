package com.example.mybarberstore.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mybarberstore.R
import com.example.mybarberstore.data.model.Barber


class BarberAdapter(
    private val barbers: List<Barber>,
    private val onBarberSelected: (Barber) -> Unit
) : RecyclerView.Adapter<BarberAdapter.BarberViewHolder>() {

    // El ViewHolder es el "contenedor" de las vistas de cada fila
    class BarberViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvBarberName)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarberViewHolder {
        // Inflamos el diseño del item que clonamos del de servicios
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_barber, parent, false)
        return BarberViewHolder(view)
    }

    override fun onBindViewHolder(holder: BarberViewHolder, position: Int) {
        val barber = barbers[position]

        // Asignamos los datos del modelo a los textos
        holder.tvName.text = barber.name

        // Configuramos el clic para seleccionar al barbero
        holder.itemView.setOnClickListener {
            onBarberSelected(barber)
        }
    }

    override fun getItemCount(): Int = barbers.size
}