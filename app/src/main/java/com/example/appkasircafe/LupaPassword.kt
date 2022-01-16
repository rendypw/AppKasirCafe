package com.example.appkasircafe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LupaPassword : AppCompatActivity() {
    private var resetPassAuth: FirebaseAuth? = null
    lateinit var varEmailLP : EditText
    lateinit var varBckLog : View
    lateinit var varBtnKrm : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lupa_password)
        resetPassAuth = FirebaseAuth.getInstance()
        varEmailLP = findViewById(R.id.editext_emailLP)
        varBckLog = findViewById(R.id.textBckLog)
        varBtnKrm = findViewById(R.id.btnKrmEmail)
        varBckLog.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finishAffinity()
        }
        varBtnKrm.setOnClickListener {
            resetPassAuth!!.sendPasswordResetEmail(varEmailLP.text.toString()) .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this, "Berhasil Mengirim",
                        Toast.LENGTH_SHORT
                    ).show()
                    varEmailLP.setText("")
                }else {
                    Toast.makeText(
                        this, "Gagal Mengirim, ulangi email",
                        Toast.LENGTH_SHORT
                    ).show() }
            }
        }
    }
}