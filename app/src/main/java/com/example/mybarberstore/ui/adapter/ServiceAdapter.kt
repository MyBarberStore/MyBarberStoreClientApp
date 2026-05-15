package com.example.mybarberstore.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mybarberstore.R
import com.example.mybarberstore.data.model.Service

class ServiceAdapter(
    private val services: List<Service>,
    private val onServiceSelected: (Service) -> Unit
) : RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    class ServiceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvServiceName)
        val details: TextView = view.findViewById(R.id.tvServiceDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_service, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = services[position]
        holder.name.text = service.name
        holder.details.text = "${service.durationMinutes} min • ${service.price}€"

        holder.itemView.setOnClickListener { onServiceSelected(service) }
    }

    override fun getItemCount() = services.size
}