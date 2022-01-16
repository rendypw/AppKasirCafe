package com.example.appkasircafe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.appkasircafe.dataclassadd.DataClassViewUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class Profil : AppCompatActivity() {
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseUser: FirebaseUser
    lateinit var fireDbUser: DatabaseReference
    private var ambilDatabase: ArrayList<DataClassViewUser> = arrayListOf()
    lateinit var varTvIdProf : TextView
    lateinit var varTvNamaProf : TextView
    lateinit var varTvEmailProf : TextView
    lateinit var varTvNoHpProf : TextView
    lateinit var varUid: String
    lateinit var varIdUser: String
    lateinit var varNamaUser: String
    lateinit var varEmailUser: String
    lateinit var varNoHpUser: String
    lateinit var varBtnUpdtProfil : Button
    lateinit var varBtnUpdtPassProfil : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)
        varBtnUpdtProfil = findViewById(R.id.btnUpdtProfil)
        varBtnUpdtPassProfil = findViewById(R.id.btnUpdPassProfil)
        varBtnUpdtProfil.setOnClickListener {
            val intent = Intent(this, UpdateProfil::class.java)
            val bundle = Bundle()
            bundle.putString("bp_uid", varUid)
            bundle.putString("bp_idProfil", varIdUser)
            bundle.putString("bp_namaProfil", varNamaUser)
            bundle.putString("bp_emailProfil", varEmailUser)
            bundle.putString("bp_noHpProfil", varNoHpUser)
            intent.putExtras(bundle)
            startActivity(intent)
        }
        varBtnUpdtPassProfil.setOnClickListener {
            val intent = Intent(this, UpdatePassword::class.java)
            startActivity(intent)
        }
    }
    override fun onStart(){
        super.onStart()
        initial()
        cekDbUser()
    }
    fun initial(){
        varTvIdProf = findViewById(R.id.tvProfilId)
        varTvNamaProf = findViewById(R.id.tvProfilNama)
        varTvEmailProf = findViewById(R.id.tvProfilEmail)
        varTvNoHpProf = findViewById(R.id.tvProfilNohp)
        firebaseAuth = FirebaseAuth.getInstance()
        fireDbUser = FirebaseDatabase.getInstance().getReference("User")
    }
    fun mydata(){
        varTvIdProf.setText(varIdUser)
        varTvNamaProf.setText(varNamaUser)
        varTvEmailProf.setText(varEmailUser)
        varTvNoHpProf.setText(varNoHpUser)
    }
    fun cekDbUser(){
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
                            varUid = mymy!!.uid
                            varIdUser=mymy!!.IdUser
                            varNamaUser=mymy!!.NamaUser
                            varEmailUser=mymy!!.EmailUser
                            varNoHpUser=mymy!!.NoHpUser
                            mydata()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}