package com.example.appkasircafe.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appkasircafe.R
import com.example.appkasircafe.dataclassadd.DataClassViewMenu
import com.example.appkasircafe.dataclassadd.DataClassViewTransaksi
import java.util.ArrayList

class AdapterDataTransaksi(private val listDataku: ArrayList<DataClassViewTransaksi>): RecyclerView.Adapter<AdapterDataTransaksi.FirebaseViewHolder>() {
    inner class FirebaseViewHolder(myView: View) : RecyclerView.ViewHolder(myView) {
        var idTrans: TextView = myView.findViewById(R.id.tv_idTrans)
        var namItemTrans: TextView = myView.findViewById(R.id.tv_namaItemTrans)
        var hargaItemTrans: TextView = myView.findViewById(R.id.tv_ehargaItemTrans)
        var jummlahItemTrans: TextView = myView.findViewById(R.id.tv_jumlahItemTrans)
        var totalBayarTrans: TextView = myView.findViewById(R.id.tv_totalHargaTrans)
        var waktuTrans: TextView = myView.findViewById(R.id.tv_tglTrans)

    }


    override fun onBindViewHolder(holder: FirebaseViewHolder, position: Int) {
        val dataku = listDataku[position]

        holder.idTrans.text = dataku.id
        holder.namItemTrans.text = dataku.nama
        holder.hargaItemTrans.text = dataku.harga.toString()
        holder.jummlahItemTrans.text = dataku.selectedQuantity.toString()
        holder.totalBayarTrans.text = dataku.jumlah.toString()
        holder.waktuTrans.text = dataku.waktu
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirebaseViewHolder {
        val viewku: View = LayoutInflater.from(parent.context).inflate(R.layout.view_data_transaksi, parent, false)
        return FirebaseViewHolder(viewku)
    }

    override fun getItemCount(): Int {
        return listDataku.size
    }
}