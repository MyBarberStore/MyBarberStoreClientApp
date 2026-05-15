package com.example.mybarberstore.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mybarberstore.R
import com.google.android.material.card.MaterialCardView

class HourAdapter(
    private val hours: List<String>,
    private val onHourSelected: (String) -> Unit
) : RecyclerView.Adapter<HourAdapter.HourViewHolder>() {

    private var selectedPosition = -1 // Para saber cuál está marcada

    class HourViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvHour: TextView = view.findViewById(R.id.tvHour)
        val card: MaterialCardView = view.findViewById(R.id.hourCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hour, parent, false)
        return HourViewHolder(view)
    }

    override fun onBindViewHolder(holder: HourViewHolder, position: Int) {
        val hour = hours[position]
        holder.tvHour.text = hour

        // Cambiar color si está seleccionada
        if (selectedPosition == position) {
            holder.card.setCardBackgroundColor(Color.parseColor("#FFF4E5")) // Naranja clarito
            holder.card.strokeColor = Color.parseColor("#FF9800") // Naranja fuerte
        } else {
            holder.card.setCardBackgroundColor(Color.WHITE)
            holder.card.strokeColor = Color.parseColor("#F0F0F0")
        }

        holder.itemView.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
            onHourSelected(hour)
        }
    }

    override fun getItemCount() = hours.size
}