package com.example.appkasircafe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateProfil : AppCompatActivity() {
    lateinit var varEtIdUser : EditText
    lateinit var varEtNamaUser : EditText
    lateinit var varEtEmailUser : EditText
    lateinit var varEtNoHpUser : EditText
    lateinit var BpIdUser : String
    lateinit var BpNamaUser : String
    lateinit var BpEmailUser : String
    lateinit var BpNoHpUser : String
    lateinit var BpUid : String
    lateinit var btnSimpanProfil : Button
    lateinit var ref: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profil)
        getMyData()
        declaration()
        myfunction()
    }
    fun declaration(){
        varEtIdUser = findViewById(R.id.et_UpidProfil)
        varEtNamaUser = findViewById(R.id.et_UpnamaProfil)
        varEtEmailUser = findViewById(R.id.et_UpEmailProfil)
        varEtNoHpUser = findViewById(R.id.et_UpNohpProfil)
        btnSimpanProfil = findViewById(R.id.btnSmpnProfil)
        varEtIdUser.setText(BpIdUser)
        varEtNamaUser.setText(BpNamaUser)
        varEtEmailUser.setText(BpEmailUser)
        varEtNoHpUser.setText(BpNoHpUser)
        ref = FirebaseDatabase.getInstance().getReference("User").child(BpUid)

    }fun getMyData(){
        val myValue = intent.extras
        if (myValue!=null){
            BpIdUser = myValue.getString("bp_idProfil").toString()
            BpNamaUser = myValue.getString("bp_namaProfil").toString()
            BpEmailUser = myValue.getString("bp_emailProfil").toString()
            BpNoHpUser = myValue.getString("bp_noHpProfil").toString()
            BpUid = myValue.getString("bp_uid").toString()
        }
    }
    fun myfunction(){

        btnSimpanProfil.setOnClickListener {
            ref.child("namaUser").setValue(varEtNamaUser.text.toString())
            ref.child("noHpUser").setValue(varEtNoHpUser.text.toString())
            Toast.makeText(
                this, "Berhasil Disimpan",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}