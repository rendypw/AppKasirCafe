package com.example.appkasircafe

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.appkasircafe.dataclassadd.DataClassViewCartProduct
import com.example.appkasircafe.dataclassadd.DataClassViewNota
import com.example.appkasircafe.dataclassadd.DataClassViewUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Home : AppCompatActivity() {
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseUser: FirebaseUser
    lateinit var fireDbUser: DatabaseReference
    lateinit var refNota: DatabaseReference
    private var ambilDatabase: ArrayList<DataClassViewUser> = arrayListOf()
    lateinit var varbtnMnmn: Button
    lateinit var varBtnRwyt: Button
    lateinit var varBtnSetting: Button
    lateinit var varBtnProfil: Button
    lateinit var varBtnLogout: Button
    lateinit var varBtnPesan: Button
    lateinit var varTvNamauser : TextView
    lateinit var waktuIn: String
    lateinit var varTvPengaturan : TextView
    lateinit var varTvPenghasilan : TextView
    lateinit var varNamaUser : String
    lateinit var btnMknn: Button
    private var backPressedTime:Long = 0
    lateinit var backToast:Toast
    private var DbNota: ArrayList<DataClassViewNota> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        firebaseAuth = FirebaseAuth.getInstance()
        fireDbUser = FirebaseDatabase.getInstance().getReference("User")
        varbtnMnmn = findViewById(R.id.btnMinuman)
        varBtnRwyt = findViewById(R.id.btnRwyt)
        varBtnPesan = findViewById(R.id.btnorder)
        varBtnProfil = findViewById(R.id.btnProfil)
        varBtnSetting = findViewById(R.id.btnSttng)
        varTvPengaturan = findViewById(R.id.tvPengaturan)
        varBtnLogout = findViewById(R.id.btnlogout)
        varTvNamauser = findViewById(R.id.tvnamauser)
        varTvPenghasilan = findViewById(R.id.tvpenghasilan)
        btnMknn = findViewById(R.id.btnmakanan)
        btnMknn.setOnClickListener {
            val intent = Intent(this, DataMakanan::class.java)
            val bundle = Bundle()
            bundle.putString("bp_namauser", varNamaUser)
            intent.putExtras(bundle)
            startActivity(intent)
        }
        varBtnProfil.setOnClickListener {
            val intent = Intent(this, Profil::class.java)
            startActivity(intent)
        }
        varBtnSetting.setOnClickListener {
            val intent = Intent(this, Pengaturan::class.java)
            val bundle = Bundle()
            bundle.putString("bp_namauser", varNamaUser)
            intent.putExtras(bundle)
            startActivity(intent)
        }
        varbtnMnmn.setOnClickListener {
            val intent = Intent(this, DataMiuman::class.java)
            val bundle = Bundle()
            bundle.putString("bp_namauser", varNamaUser)
            intent.putExtras(bundle)
            startActivity(intent)
        }
        varBtnRwyt.setOnClickListener {
            val intent = Intent(this, RiwayatPesanan::class.java)
            startActivity(intent)
        }
        varBtnPesan.setOnClickListener {
            val intent = Intent(this, Pesan::class.java)
            startActivity(intent)
        }
        varBtnLogout.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("Logout")
            alertDialog.setMessage("kamu yakin mau Logout ?")
            alertDialog.setNegativeButton("Gajadi", DialogInterface.OnClickListener { dialogInterface, i ->
            })
            alertDialog.setPositiveButton("Iya", DialogInterface.OnClickListener { dialogInterface, i ->
                firebaseAuth.signOut()
                startActivity(Intent(this, Login::class.java))
                finishAffinity()
            })
            alertDialog.show()
        }
    }

    override fun onStart() {
        super.onStart()
        cekNamaUser()
    }
    fun cekPendapatan(){
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val now = Date()
        waktuIn=simpleDateFormat.format(now)
        refNota = FirebaseDatabase.getInstance().getReference("Nota")
        refNota.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot!!.exists()) {
                    DbNota.clear()
                    for (x in snapshot.children) {
                        val mymy = x.getValue(DataClassViewNota::class.java)
                        mymy!!.uid = x.key.toString()
                        if (waktuIn==mymy!!.waktu){
                            DbNota.add(mymy!!)
                        }
                    }
                }
                var total = 0
                for (i in 0 until DbNota.size){
                    total += DbNota[i].totalBayar
                }
                varTvPenghasilan.setText(total.toString())
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    override fun onBackPressed() {
        backToast = Toast.makeText(this, "Tekan kembali untuk keluar aplikasi", Toast.LENGTH_LONG)
        if (backPressedTime + 1500 > System.currentTimeMillis()) {
            backToast.cancel()
            super.onBackPressed()
            return
        } else {
            backToast.show()
        }
        backPressedTime = System.currentTimeMillis()
    }
    fun cekNamaUser(){
        firebaseUser = firebaseAuth.getCurrentUser()!!
        var varEmAuth = firebaseUser.getEmail()
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Memuat ...")
        progressDialog.setCancelable(false)
        progressDialog.show()
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
                                varNamaUser=mymy!!.NamaUser
                                varTvNamauser.setText(varNamaUser)
                                varBtnSetting.setVisibility(View.INVISIBLE)
                                varTvPengaturan.setVisibility(View.INVISIBLE)
                                cekPendapatan()
                                progressDialog.dismiss()
                            }
                            else if (mymy.KategoriUser=="Admin"){
                                ambilDatabase.add(mymy!!)
                                varNamaUser=mymy!!.NamaUser
                                varTvNamauser.setText(varNamaUser)
                                cekPendapatan()
                                progressDialog.dismiss()
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
}