package com.TI2.famacologiccalc.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.TI2.famacologiccalc.R
import com.TI2.famacologiccalc.database.models.Pacientes
import com.TI2.famacologiccalc.database.session.ActualPatient

class PacienteAdapter(
    private val pacientes: List<Pacientes>, // Cambiar Flow por List
    private val onItemClick: (Pacientes) -> Unit
) : RecyclerView.Adapter<PacienteAdapter.PacienteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PacienteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_paciente, parent, false)
        return PacienteViewHolder(view)
    }

    override fun onBindViewHolder(holder: PacienteViewHolder, position: Int) {
        val paciente = pacientes[position] // Acceder a los pacientes directamente desde la lista
        holder.nombrePaciente.text = paciente.nombre
        holder.edadPaciente.text = "Edad: ${paciente.edad}"
        holder.pesoPaciente.text = "Peso: ${paciente.peso} kg"
        holder.alturaPaciente.text = "Altura: ${paciente.altura} cm"
        holder.fechaRegistro.text = "Registrado el: ${paciente.fechaRegistro}"
        holder.estatusPaciente.text = "Estatus: ${paciente.estatus}"

        // Cambiar el fondo si es el paciente seleccionado
        if (ActualPatient.pacienteSeleccionado == paciente) {
            holder.itemView.setBackgroundColor(Color.parseColor("#D3D3D3")) // Fondo para item seleccionado
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT) // Fondo por defecto
        }

        // Asignar click al item
        holder.itemView.setOnClickListener {
            onItemClick(paciente)
            ActualPatient.pacienteSeleccionado = paciente // Actualiza el paciente seleccionado
            notifyDataSetChanged() // Refresca la vista para aplicar cambios
        }
    }

    override fun getItemCount(): Int = pacientes.size

    class PacienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombrePaciente: TextView = itemView.findViewById(R.id.tv_patient_name)
        val edadPaciente: TextView = itemView.findViewById(R.id.tv_patient_age)
        val pesoPaciente: TextView = itemView.findViewById(R.id.tv_patient_weight)
        val alturaPaciente: TextView = itemView.findViewById(R.id.tv_patient_height)
        val fechaRegistro: TextView = itemView.findViewById(R.id.tv_patient_registration_date)
        val estatusPaciente: TextView = itemView.findViewById(R.id.tv_patient_status)
        val imageView: ImageView = itemView.findViewById(R.id.iv_patient_image)
    }
}
