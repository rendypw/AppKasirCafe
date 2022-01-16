package com.example.appkasircafe.adapter

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appkasircafe.R
import com.example.appkasircafe.UpdateMakanan
import com.example.appkasircafe.dataclassadd.DataClassViewCartProduct
import com.example.appkasircafe.dataclassadd.DataClassViewMenu
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.ArrayList

class AdapterDataPembayaran(private val listDataku: ArrayList<DataClassViewCartProduct>): RecyclerView.Adapter<AdapterDataPembayaran.FirebaseViewHolder>() {
    inner class FirebaseViewHolder(myView: View):RecyclerView.ViewHolder(myView) {
        var varNamItem: TextView = myView.findViewById(R.id.tv_namaItem)
        var varHrgItem: TextView = myView.findViewById(R.id.tv_HargaItem)
        var varJmlhItem: TextView = myView.findViewById(R.id.tv_jmlhItem)
        var varTotalHarga: TextView = myView.findViewById(R.id.tv_totalHarga)
        lateinit var ref: DatabaseReference
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirebaseViewHolder {
        val viewku: View = LayoutInflater.from(parent.context).inflate(R.layout.view_data_pembayaran, parent, false)
        return FirebaseViewHolder(viewku)
    }
    override fun onBindViewHolder(holder: FirebaseViewHolder, position: Int) {
        val dataku = listDataku[position]
        holder.varNamItem.text = dataku.nama
        holder.varHrgItem.text = dataku.harga.toString()
        holder.varJmlhItem.text = dataku.selectedQuantity.toString()
        holder.varTotalHarga.text = dataku.jumlah.toString()
    }

    override fun getItemCount(): Int {
        return listDataku.size
    }
}
