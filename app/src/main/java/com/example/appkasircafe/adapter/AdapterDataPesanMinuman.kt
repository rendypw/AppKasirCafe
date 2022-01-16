package com.example.appkasircafe.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appkasircafe.Pesan
import com.example.appkasircafe.R
import com.example.appkasircafe.dataclassadd.DataClassViewMenu
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.ArrayList

class AdapterDataPesanMinuman(private val listDataku: ArrayList<DataClassViewMenu>): RecyclerView.Adapter<AdapterDataPesanMinuman.FirebaseViewHolder>() {
    inner class FirebaseViewHolder(myView: View): RecyclerView.ViewHolder(myView) {
        var varIdMnmn: TextView = myView.findViewById(R.id.tv_idMinuman)
        var varNamMnmn: TextView = myView.findViewById(R.id.tv_namaMinuman)
        var varHrgMnmn: TextView = myView.findViewById(R.id.tv_hargaMinuman)
        var varGmbrMnmn: ImageView = myView.findViewById(R.id.tv_gambarMinuman)
        var tvJmlhPesananMnmn: TextView = myView.findViewById(R.id.jmlhPesananMinuman)
        var btnTambahMinuman: Button = myView.findViewById(R.id.btnTambahMinuman)
        var btnKurangMinuman: Button = myView.findViewById(R.id.btnKurangMinuman)
        lateinit var ref: DatabaseReference
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirebaseViewHolder {
        val viewku: View = LayoutInflater.from(parent.context).inflate(R.layout.view_pesan_data_minuman, parent, false)
        return FirebaseViewHolder(viewku)
    }
    override fun onBindViewHolder(holder: FirebaseViewHolder, position: Int) {
        val dataku = listDataku[position]
        holder.tvJmlhPesananMnmn.text = dataku.SelectedQ.toString()
        holder.varIdMnmn.text = dataku.ID
        holder.varNamMnmn.text = dataku.Nama
        holder.varHrgMnmn.text = dataku.Harga
        Glide.with(holder.itemView)
                .load(dataku.GambarSrc)
                .centerCrop()
                .into( holder.varGmbrMnmn)
        val x: Int = dataku.SelectedQ
        holder.btnTambahMinuman.setOnClickListener {

            var y : Int = 1+x
            holder.ref = FirebaseDatabase.getInstance().getReference("Menu").child(dataku.uid)
            holder.ref.child("selectedQ").setValue(y)
        }
        holder.btnKurangMinuman.setOnClickListener {
            var intent = Intent(holder.itemView.context, Pesan::class.java)
            var y : Int = x-1
            holder.ref = FirebaseDatabase.getInstance().getReference("Menu").child(dataku.uid)
            holder.ref.child("selectedQ").setValue(y)
        }
    }

    override fun getItemCount(): Int {
        return listDataku.size
    }
}