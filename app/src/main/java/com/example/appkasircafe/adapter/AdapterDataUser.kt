package com.example.appkasircafe.adapter

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.appkasircafe.R
import com.example.appkasircafe.dataclassadd.DataClassViewUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.ArrayList

class AdapterDataUser(private val listDataku: ArrayList<DataClassViewUser>): RecyclerView.Adapter<AdapterDataUser.FirebaseViewHolder>() {
    inner class FirebaseViewHolder(myView: View): RecyclerView.ViewHolder(myView) {
        var varIdUser: TextView = myView.findViewById(R.id.tv_idUser)
        var varNamaUser: TextView = myView.findViewById(R.id.tv_namaUser)
        var varEmailUser: TextView = myView.findViewById(R.id.tv_emailUser)
        var varNoHpUser: TextView = myView.findViewById(R.id.tv_NoHpUser)
        var varAlamatUser: TextView = myView.findViewById(R.id.tv_alamatUser)
        var varKategoriUser: TextView = myView.findViewById(R.id.tv_katUser)
        var btnDelUser: Button = myView.findViewById(R.id.btnDelUser)
        lateinit var ref: DatabaseReference
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirebaseViewHolder {
        val viewku: View = LayoutInflater.from(parent.context).inflate(R.layout.view_data_user, parent, false)
        return FirebaseViewHolder(viewku)
    }
    override fun onBindViewHolder(holder: FirebaseViewHolder, position: Int) {
        val dataku = listDataku[position]
        holder.varIdUser.text = dataku.IdUser
        holder.varNamaUser.text = dataku.NamaUser
        holder.varEmailUser.text = dataku.EmailUser
        holder.varNoHpUser.text = dataku.NoHpUser
        holder.varAlamatUser.text = dataku.AlamatUser
        holder.varKategoriUser.text = dataku.KategoriUser
        holder.ref = FirebaseDatabase.getInstance().getReference("User").child(dataku.uid)
        holder.btnDelUser.setOnClickListener{
            val alertDialog = AlertDialog.Builder(holder.itemView.context)
            alertDialog.setTitle("Hapus Data")
            alertDialog.setMessage("yakin mau hapus "+ holder.varNamaUser.text+" ?")
            alertDialog.setNegativeButton("Gajadi", DialogInterface.OnClickListener { dialogInterface, i ->
            })
            alertDialog.setPositiveButton("Iya", DialogInterface.OnClickListener { dialogInterface, i ->
                holder.ref.removeValue()
                Toast.makeText(
                    holder.itemView.context, "Berhasil dihapus",
                    Toast.LENGTH_SHORT
                ).show()
            })
            alertDialog.show()
        }
    }

    override fun getItemCount(): Int {
        return listDataku.size
    }
}