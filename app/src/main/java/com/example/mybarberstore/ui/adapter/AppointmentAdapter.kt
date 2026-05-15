package com.example.mybarberstore.ui.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mybarberstore.R
import com.example.mybarberstore.data.model.AppointmentResponse

class HistoryAdapter(
    private val appointments: List<AppointmentResponse>,
    private val onCancelClick: (AppointmentResponse) -> Unit, // Función para cancelar
    private val onInvoiceClick: (AppointmentResponse) -> Unit): // Función para factura)
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvAppName)
        val tvBarber: TextView = view.findViewById(R.id.tvAppBarber)
        val tvDate: TextView = view.findViewById(R.id.tvAppDate)
        val tvTime: TextView = view.findViewById(R.id.tvAppTime)
        val tvPrice: TextView = view.findViewById(R.id.tvAppPrice)
        val tvStatus: TextView = view.findViewById(R.id.tvAppStatus)
        val tvActionText: TextView = view.findViewById(R.id.tvActionText)
        val ivActionIcon: ImageView = view.findViewById(R.id.ivActionIcon)

        val btnAppAction: View = view.findViewById(R.id.btnAppAction)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_appointment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val app = appointments[position]
        val context = holder.itemView.context // Guardamos el contexto para usar getString

        holder.tvName.text = app.serviceName
        holder.tvBarber.text = app.employeeName
        holder.tvDate.text = app.date
        holder.tvTime.text = app.startTime.take(5) // Quita los segundos
        holder.tvPrice.text = "${app.price}€"

        // Lógica de estados
        when (app.status) {
            "COMPLETED" -> {
                // ESTADO COMPLETADA (Gris)
                holder.tvStatus.text = context.getString(R.string.status_completed)
                holder.tvStatus.setBackgroundResource(R.drawable.bg_status_completed)
                holder.tvStatus.setTextColor(Color.parseColor("#7F8C8D")) // Gris oscuro

                // Acción: Factura (Gris)
                configurarAccion(holder, R.string.action_invoice, "#7F8C8D", R.drawable.ic_invoice)
            }
            "CANCELLED" -> {
                // ESTADO CANCELADA (Rojo)
                holder.tvStatus.text = context.getString(R.string.status_cancelled)
                holder.tvStatus.setBackgroundResource(R.drawable.bg_status_cancelled)
                holder.tvStatus.setTextColor(Color.parseColor("#E74C3C")) // Rojo fuerte

                // Por simplicidad, ocultamos la zona de acción si está cancelada
                holder.btnAppAction.visibility = View.GONE
            }
            else -> {
                // ESTADO PENDIENTE / CONFIRMADA (Naranja)
                holder.tvStatus.text = context.getString(R.string.status_pending)
                holder.tvStatus.setBackgroundResource(R.drawable.bg_status_pending)
                holder.tvStatus.setTextColor(Color.parseColor("#E67E22")) // Naranja fuerte

                // Acción: Cancelar (Rojo)
                holder.btnAppAction.visibility = View.VISIBLE
                configurarAccion(holder, R.string.action_cancel, "#E74C3C", R.drawable.ic_cancel)
            }
        }
        holder.btnAppAction.setOnClickListener {
            if (app.status == "COMPLETED") {
                onInvoiceClick(app)
            } else if (app.status == "CONFIRMED") {
                onCancelClick(app)
            }
        }
    }

    private fun configurarAccion(
        holder: ViewHolder,
        textoResId: Int,
        colorHex: String,
        iconRes: Int
    ) {
        val context = holder.itemView.context
        val color = Color.parseColor(colorHex)

        holder.tvActionText.text = context.getString(textoResId)
        holder.tvActionText.setTextColor(color)
        holder.ivActionIcon.setImageResource(iconRes)
        holder.ivActionIcon.imageTintList = ColorStateList.valueOf(color)
    }

    override fun getItemCount() = appointments.size
}