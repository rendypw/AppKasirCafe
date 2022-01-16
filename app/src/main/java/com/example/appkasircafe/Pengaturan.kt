package com.example.appkasircafe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class Pengaturan : AppCompatActivity() {
    lateinit var varAddusr : Button
    lateinit var varBpNamauser : String
    lateinit var varBtnDataUser : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengaturan)
        varAddusr = findViewById(R.id.btnAddUser)
        varBtnDataUser = findViewById(R.id.btnDataUser)
        varBtnDataUser.setOnClickListener {
            var intent = Intent(this, DataUser::class.java)
            startActivity(intent)
        }
        varAddusr.setOnClickListener {
            var intent = Intent(this, TambahUser::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        cekBundle()
    }
    fun cekBundle(){
        val myValue = intent.extras
        if (myValue!=null){
            varBpNamauser = myValue.getString("bp_namauser").toString()
        }
    }
}