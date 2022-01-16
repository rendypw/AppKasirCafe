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
import com.example.appkasircafe.dataclassadd.DataClassAddCartProduct
import com.example.appkasircafe.dataclassadd.DataClassViewMenu
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class AdapterDataPesanMakanan(private val listDataku: ArrayList<DataClassViewMenu>): RecyclerView.Adapter<AdapterDataPesanMakanan.FirebaseViewHolder>() {
    inner class FirebaseViewHolder(myView: View):RecyclerView.ViewHolder(myView) {
        var varIdMknn: TextView = myView.findViewById(R.id.tv_idMakanan)
        var varNamMknn: TextView = myView.findViewById(R.id.tv_namaMakanan)
        var varHrgMknn: TextView = myView.findViewById(R.id.tv_hargaMakanan)
        var varGmbrMknn: ImageView = myView.findViewById(R.id.tv_gambarMakanan)
        var tvJmlhPesanan: TextView = myView.findViewById(R.id.jmlhPesananMakanan)
        var btnTambahMakanan: Button = myView.findViewById(R.id.btnTambahMakanan)
        var btnKurangMakanan: Button = myView.findViewById(R.id.btnKurangMakanan)
        lateinit var ref: DatabaseReference

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirebaseViewHolder {
        val viewku: View = LayoutInflater.from(parent.context).inflate(R.layout.view_pesan_data_makanan, parent, false)
        return FirebaseViewHolder(viewku)
    }
    override fun onBindViewHolder(holder: FirebaseViewHolder, position: Int) {
        val dataku = listDataku[position]
        var uid:String = dataku.uid
        holder.tvJmlhPesanan.text = dataku.SelectedQ.toString()
        holder.varIdMknn.text = dataku.ID
        holder.varNamMknn.text = dataku.Nama
        holder.varHrgMknn.text = dataku.Harga
        Glide.with(holder.itemView)
                .load(dataku.GambarSrc)
                .centerCrop()
                .into( holder.varGmbrMknn)
        val x: Int = dataku.SelectedQ
        holder.ref = FirebaseDatabase.getInstance().getReference("Menu").child(uid)
        holder.btnTambahMakanan.setOnClickListener {
            val y : Int = 1+x
            holder.ref.child("selectedQ").setValue(y)
        }
        holder.btnKurangMakanan.setOnClickListener {
            val y : Int = x-1
            holder.ref.child("selectedQ").setValue(y)
        }
    }

    override fun getItemCount(): Int {
        return listDataku.size
    }
}
