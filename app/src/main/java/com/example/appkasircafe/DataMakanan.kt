package com.example.appkasircafe

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appkasircafe.adapter.AdapterDataMakanan
import com.example.appkasircafe.dataclassadd.DataClassViewMenu
import com.example.appkasircafe.dataclassadd.DataClassViewUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class DataMakanan : AppCompatActivity() {
    private var ambilDatabase: ArrayList<DataClassViewUser> = arrayListOf()
    lateinit var fireDbUser: DatabaseReference
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseUser: FirebaseUser
    lateinit var varBpNamauser : String
    lateinit var fireTbMakanan: DatabaseReference
    private lateinit var layoutku: RecyclerView
    private var DbMakanan: ArrayList<DataClassViewMenu> = arrayListOf()
    lateinit var btnAddMknn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_makanan)
        layoutku = findViewById(R.id.rv_dataMakanan)
        fireTbMakanan = FirebaseDatabase.getInstance().getReference("Menu")
        btnAddMknn = findViewById(R.id.btn_addMakanan)
        btnAddMknn.setOnClickListener {
            val intent = Intent(this, TambahMakanan::class.java)
            val bundle = Bundle()
            bundle.putString("bp_namauser", varBpNamauser)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }
    override fun onStart(){
        super.onStart()
        cekNamaUser()
        loadMeFirst()
    }
    fun cekNamaUser(){
        firebaseAuth = FirebaseAuth.getInstance()
        fireDbUser = FirebaseDatabase.getInstance().getReference("User")
        firebaseUser = firebaseAuth.getCurrentUser()!!
        var varEmAuth = firebaseUser.getEmail()
        fireDbUser.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot!!.exists()){
                    ambilDatabase.clear()
                    for (x in snapshot.children){
                        val mymy = x.getValue(DataClassViewUser::class.java)
                        mymy!!.uid = x.key.toString()
                        if (mymy!!.EmailUser==varEmAuth) {
                            if (mymy.KategoriUser=="Kasir"){
                                ambilDatabase.add(mymy!!)
                                btnAddMknn.setVisibility(View.INVISIBLE)
                            }
                        }
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun loadMeFirst(){
        val myValue = intent.extras
        if (myValue!=null){
            varBpNamauser = myValue.getString("bp_namauser").toString()
        }
        fireTbMakanan.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot!!.exists()) {
                    DbMakanan.clear()
                    for (x in snapshot.children) {
                        val mymy = x.getValue(DataClassViewMenu::class.java)
                        if (mymy!!.Kategori.equals("Makanan")){
                            mymy!!.uid = x.key.toString()
                            DbMakanan.add(mymy!!)
                        }
                    }
                }
                loadMeNow()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    private fun loadMeNow(){
        layoutku.layoutManager = LinearLayoutManager(this)
        val dbAbsPng = AdapterDataMakanan(DbMakanan)
        layoutku.adapter = dbAbsPng
    }

}