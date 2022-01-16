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
import com.example.appkasircafe.UpdateMinuman
import com.example.appkasircafe.dataclassadd.DataClassViewMenu
import com.example.appkasircafe.dataclassadd.DataClassViewUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.ArrayList

class AdapterDataMinuman(private val listDataku: ArrayList<DataClassViewMenu>): RecyclerView.Adapter<AdapterDataMinuman.FirebaseViewHolder>() {
    inner class FirebaseViewHolder(myView: View): RecyclerView.ViewHolder(myView) {
        var varIdMnmn: TextView = myView.findViewById(R.id.tv_idMinuman)
        var varNamMnmn: TextView = myView.findViewById(R.id.tv_namaMinuman)
        var varHrgMnmn: TextView = myView.findViewById(R.id.tv_hargaMinuman)
        var varGmbrMnmn: ImageView = myView.findViewById(R.id.tv_gambarMinuman)
        var btnEditMkanan: Button = myView.findViewById(R.id.btnUpdtMinuman)
        var btnDelMkanan: Button = myView.findViewById(R.id.btnDelMinuman)
        lateinit var ref: DatabaseReference
        lateinit var refGambar: StorageReference
        var ambilDatabase: ArrayList<DataClassViewUser> = arrayListOf()
        lateinit var fireDbUser: DatabaseReference
        lateinit var firebaseAuth: FirebaseAuth
        lateinit var firebaseUser: FirebaseUser
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirebaseViewHolder {
        val viewku: View = LayoutInflater.from(parent.context).inflate(R.layout.view_data_minuman, parent, false)
        return FirebaseViewHolder(viewku)
    }
    override fun onBindViewHolder(holder: FirebaseViewHolder, position: Int) {
        holder.firebaseAuth = FirebaseAuth.getInstance()
        holder.fireDbUser = FirebaseDatabase.getInstance().getReference("User")
        holder.firebaseUser = holder.firebaseAuth.getCurrentUser()!!
        var varEmAuth = holder.firebaseUser.getEmail()
        holder.fireDbUser.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot!!.exists()){
                    holder.ambilDatabase.clear()
                    for (x in snapshot.children){
                        val mymy = x.getValue(DataClassViewUser::class.java)
                        mymy!!.uid = x.key.toString()
                        if (mymy!!.EmailUser==varEmAuth) {
                            if (mymy.KategoriUser=="Kasir"){
                                holder.ambilDatabase.add(mymy!!)
                                holder.btnDelMkanan.setVisibility(View.INVISIBLE)
                                holder.btnEditMkanan.setVisibility(View.INVISIBLE)
                            }
                        }
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        val dataku = listDataku[position]
        holder.varIdMnmn.text = dataku.ID
        holder.varNamMnmn.text = dataku.Nama
        holder.varHrgMnmn.text = dataku.Harga
        Glide.with(holder.itemView)
            .load(dataku.GambarSrc)
            .centerCrop()
            .into( holder.varGmbrMnmn)
        val title  = holder.varIdMnmn.text.toString()
        holder.ref = FirebaseDatabase.getInstance().getReference("Menu").child(dataku.uid)
        holder.refGambar = FirebaseStorage.getInstance().getReference("Minuman/$title")
        holder.btnEditMkanan.setOnClickListener{
            val intent = Intent(holder.itemView.context, UpdateMinuman::class.java)
            val bundle = Bundle()
            bundle.putString("bp_uid", dataku.uid)
            bundle.putString("bp_idMinuman", dataku.ID)
            bundle.putString("bp_namaMinuman", dataku.Nama)
            bundle.putString("bp_HargaMinuman", dataku.Harga)
            intent.putExtras(bundle)
            holder.itemView.context.startActivity(intent)
        }
        holder.btnDelMkanan.setOnClickListener{
            val alertDialog = AlertDialog.Builder(holder.itemView.context)
            alertDialog.setTitle("Hapus Data")
            alertDialog.setMessage("yakin mau hapus "+ holder.varNamMnmn.text+" ?")
            alertDialog.setNegativeButton("Gajadi", DialogInterface.OnClickListener { dialogInterface, i ->
            })
            alertDialog.setPositiveButton("Iya", DialogInterface.OnClickListener { dialogInterface, i ->
                holder.ref.removeValue()
                holder.refGambar.delete()
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