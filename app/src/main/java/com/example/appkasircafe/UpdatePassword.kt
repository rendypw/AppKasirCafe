package com.example.appkasircafe

import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class UpdatePassword : AppCompatActivity() {
    lateinit var auth : FirebaseAuth
    lateinit var varEtPassword : EditText
    lateinit var varupEtPassword : EditText
    lateinit var varupKonfrEtPassword : EditText
    lateinit var varBtnSmpanPass : Button
    private val TAG = "CreateAccountActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_password)
        auth = FirebaseAuth.getInstance()
        varEtPassword = findViewById(R.id.et_passProfil)
        varupEtPassword= findViewById(R.id.et_UpPass)
        varupKonfrEtPassword= findViewById(R.id.et_UpKonfirmPass)
        varBtnSmpanPass= findViewById(R.id.btnSmpnPassword)
        varBtnSmpanPass.setOnClickListener {
            if (varEtPassword.text.isNotEmpty() && varupEtPassword.text.isNotEmpty() && varupKonfrEtPassword.text.isNotEmpty()){
                if (varupEtPassword.text.toString().length >=8){
                    if (varupEtPassword.text.toString().equals(varupKonfrEtPassword.text.toString())){
                        val user = auth.currentUser
                        if (user!= null && user.email != null){
                            val credential = EmailAuthProvider.getCredential(user.email!!, varEtPassword.text.toString())
                            user?.reauthenticate(credential)?.addOnCompleteListener{
                                if(it.isSuccessful){
                                    user?.updatePassword(varupKonfrEtPassword.text.toString())?.addOnCompleteListener{task ->
                                        if (task.isSuccessful){
                                            Log.d(TAG, "User Password Update")
                                            Toast.makeText(this, "Password Berhasil diganti", Toast.LENGTH_SHORT).show()
                                            Toast.makeText(this, "Silahkan Login Kembali", Toast.LENGTH_SHORT).show()
                                            val intent = Intent(this, Login::class.java)
                                            auth.signOut()
                                            startActivity(intent)
                                            finishAffinity()
                                        }
                                    }
                                }else{
                                    Toast.makeText(this, "Password Salah", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                    }else{
                        Toast.makeText(this, "Password Baru tidak Sama!", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this, "Password Harus 8 karakter!", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this, "Kolom tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}