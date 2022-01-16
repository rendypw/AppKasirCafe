package com.example.appkasircafe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appkasircafe.adapter.AdapterDataMinuman
import com.example.appkasircafe.dataclassadd.DataClassViewMenu
import com.example.appkasircafe.dataclassadd.DataClassViewUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class DataMiuman : AppCompatActivity() {
    private var ambilDatabase: ArrayList<DataClassViewUser> = arrayListOf()
    lateinit var fireDbUser: DatabaseReference
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseUser: FirebaseUser
    lateinit var fireTbMinuman: DatabaseReference
    lateinit var varBpNamauser : String
    private lateinit var layoutku: RecyclerView
    private var DbMinuman: ArrayList<DataClassViewMenu> = arrayListOf()
    lateinit var varbtnTmbhMnmn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_miuman)
        varbtnTmbhMnmn = findViewById(R.id.btn_addMinuman)
        layoutku = findViewById(R.id.rv_dataMinuman)
        fireTbMinuman = FirebaseDatabase.getInstance().getReference("Menu")
        varbtnTmbhMnmn.setOnClickListener {
            val intent = Intent(this, TambahMinuman::class.java)
            val bundle = Bundle()
            bundle.putString("bp_namauser", varBpNamauser)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    override fun onStart(){
        super.onStart()
        loadMeFirst()
        cekNamaUser()
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
                                varbtnTmbhMnmn.setVisibility(View.INVISIBLE)
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
        fireTbMinuman.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot!!.exists()) {
                    DbMinuman.clear()
                    for (x in snapshot.children) {
                        val mymy = x.getValue(DataClassViewMenu::class.java)
                        if (mymy!!.Kategori.equals("Minuman")){
                            mymy!!.uid = x.key.toString()
                            DbMinuman.add(mymy!!)
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
        val dbAbsPng = AdapterDataMinuman(DbMinuman)
        layoutku.adapter = dbAbsPng
    }
}