package com.example.appkasircafe

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Login : AppCompatActivity() {
    lateinit var btnLog : Button
    lateinit var btnLupPswd : View
    lateinit var varEtEmail : EditText
    lateinit var varEtPasswd : EditText
    lateinit var varProgressBar : View
    private val TAG = "CreateAccountActivity"
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseUser: FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        varEtEmail = findViewById(R.id.editext_email)
        varProgressBar = findViewById(R.id.pbLogin)
        varEtPasswd = findViewById(R.id.editext_pass)
        firebaseAuth = FirebaseAuth.getInstance()
        btnLupPswd = findViewById(R.id.textlp)
        btnLog = findViewById(R.id.btnLogin)
        cekdulu()
        btnLupPswd.setOnClickListener {
            val intent = Intent(this, LupaPassword::class.java)
            startActivity(intent)
        }
        btnLog.setOnClickListener {
            if (varEtEmail.text.toString()==""){
                Toast.makeText(
                    this, "Mohon Mengisi Email",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (varEtPasswd.text.toString()==""){
                Toast.makeText(
                    this, "Mohon Mengisi Password",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                //editSavedLogin.commit()
                varProgressBar.setVisibility(View.VISIBLE)
                Handler().postDelayed({
                    loginme(varEtEmail.text.toString(), varEtPasswd.text.toString())
                },1500)
            }
        }
    }
    fun loginme(username: String, password: String ){
        firebaseAuth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val intent = Intent(this, Home::class.java)
                    startActivity(intent)
                    finishAffinity()
                    // Sign in success, update UI with signed-in user's information
                } else {
                    // If sign in fails, display a message to the user.
                    Log.e(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        this, "Login Gagal, Email atau Password Salah!",
                        Toast.LENGTH_SHORT
                    ).show()
                    varProgressBar.setVisibility(View.INVISIBLE)
                }
            }
    }
    fun cekdulu(){
        val currentUser = firebaseAuth!!.currentUser
        if (currentUser != null){
            val intent = Intent(this, Home::class.java)
            varProgressBar.setVisibility(View.VISIBLE)
            Handler().postDelayed({
                startActivity(intent)
                finishAffinity()
            },1500)
        }
    }
}