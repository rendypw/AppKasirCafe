package com.example.appkasircafe.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appkasircafe.R
import com.example.appkasircafe.dataclassadd.DataClassViewMenu
import com.example.appkasircafe.dataclassadd.DataClassViewNota
import com.example.appkasircafe.dataclassadd.DataClassViewTransaksi
import java.util.ArrayList

class AdapterDataNota(private val listDataku: ArrayList<DataClassViewNota>): RecyclerView.Adapter<AdapterDataNota.FirebaseViewHolder>() {
    inner class FirebaseViewHolder(myView: View) : RecyclerView.ViewHolder(myView) {
        var idNota: TextView = myView.findViewById(R.id.tv_idNota)
        var atasNama: TextView = myView.findViewById(R.id.tv_namaCust)
        var noMeja: TextView = myView.findViewById(R.id.tv_noMeja)
        var waktu: TextView = myView.findViewById(R.id.tv_waktu)
        var totalBayar: TextView = myView.findViewById(R.id.tv_totalBayar)

    }


    override fun onBindViewHolder(holder: FirebaseViewHolder, position: Int) {
        val dataku = listDataku[position]

        holder.idNota.text = dataku.id
        holder.atasNama.text = dataku.atNama
        holder.noMeja.text = dataku.noMeja
        holder.waktu.text = dataku.waktu
        holder.totalBayar.text = dataku.totalBayar.toString()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirebaseViewHolder {
        val viewku: View = LayoutInflater.from(parent.context).inflate(R.layout.view_data_nota, parent, false)
        return FirebaseViewHolder(viewku)
    }

    override fun getItemCount(): Int {
        return listDataku.size
    }
}