package com.example.appkasircafe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.appkasircafe.dataclassadd.DataClassAddUser
import com.example.appkasircafe.dataclassadd.DataClassViewUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class TambahUser : AppCompatActivity() {
    private val TAG = "CreateAccountActivity"
    var radioGroup: RadioGroup? = null
    lateinit var varJumlhUser : String
    lateinit var varTvJumlhUser : TextView
    lateinit var radioButton: RadioButton
    lateinit var varEtIdUser : EditText
    lateinit var varEtNamaUser : EditText
    lateinit var varEtEmailUser : EditText
    lateinit var varEtNoHpUser : EditText
    lateinit var varEtPassUser : EditText
    lateinit var varEtAlamatUser : EditText
    lateinit var varEtKonPassUser : EditText
    lateinit var varBtnTmbhUser: Button
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var ref: DatabaseReference
    private var DbUser: ArrayList<DataClassViewUser> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_user)
        radioGroup = findViewById(R.id.rgPng)
        firebaseAuth= FirebaseAuth.getInstance()
        varTvJumlhUser = findViewById(R.id.tvJumlahUser)
        varEtIdUser = findViewById(R.id.et_idUser)
        varEtNamaUser = findViewById(R.id.et_namaUser)
        varEtEmailUser = findViewById(R.id.et_emailUser)
        varEtNoHpUser = findViewById(R.id.et_NoHp)
        varEtPassUser = findViewById(R.id.et_PassUser)
        varEtAlamatUser = findViewById(R.id.et_alamatUser)
        varEtKonPassUser = findViewById(R.id.et_koPassUser)
        varBtnTmbhUser = findViewById(R.id.btnTambahUser)
        firebaseAuth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().getReference("User")
        varBtnTmbhUser.setOnClickListener {
            if (varEtIdUser.text.isEmpty() && varEtNamaUser.text.isEmpty() && varEtEmailUser.text.isEmpty() && varEtNoHpUser.text.isEmpty() && varEtAlamatUser.text.isEmpty() && varEtPassUser.text.isEmpty() && varEtKonPassUser.text.isEmpty()){
                Toast.makeText(
                    this, "Isi Semua Kolom !",
                    Toast.LENGTH_SHORT
                ).show()
            }

            else if (varEtPassUser.text.toString().length<=7){
                Toast.makeText(
                    this, "Password harus lebih dari atau sama dengan 8 ",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if (varEtPassUser.text.toString()!=varEtKonPassUser.text.toString()){
                Toast.makeText(
                    this, "Password Tidak Sama",
                    Toast.LENGTH_SHORT
                ).show()
            }

            else {
                register(varEtEmailUser.text.toString(), varEtPassUser.text.toString())
            }
        }

    }

    override fun onStart() {
        super.onStart()
        cekData()
    }
    fun register(emailku: String, passwordku: String){
        val arrayKu = DataClassAddUser()
        firebaseAuth.createUserWithEmailAndPassword(emailku, passwordku).addOnCompleteListener(this){ task ->
            if (task.isSuccessful){
                val selectedOption: Int = radioGroup!!.checkedRadioButtonId
                radioButton = findViewById(selectedOption)
                arrayKu.IdUser = varEtIdUser.text.toString()
                arrayKu.NamaUser = varEtNamaUser.text.toString()
                arrayKu.EmailUser = varEtEmailUser.text.toString()
                arrayKu.NoHpUser = varEtNoHpUser.text.toString()
                arrayKu.AlamatUser = varEtAlamatUser.text.toString()
                arrayKu.KategoriUser = radioButton.text.toString()
                val taskPush = ref.push()
                taskPush.setValue(arrayKu)
                //et_username.setText("")
                Toast.makeText(this, "Tambah User Berhasil", Toast.LENGTH_SHORT).show()
                varEtIdUser.setText("")
                varEtNamaUser.setText("")
                varEtEmailUser.setText("")
                varEtNoHpUser.setText("")
                varEtAlamatUser.setText("")
                varEtPassUser.setText("")
                varEtKonPassUser.setText("")
                varEtIdUser.requestFocus()
            }else {
                Log.w(TAG, "Gagal menambah akun", task.exception)
                Toast.makeText(baseContext, "Failed $emailku ", Toast.LENGTH_SHORT).show()
            }

        }
    }
    fun cekData(){
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot!!.exists()) {
                    DbUser.clear()
                    for (x in snapshot.children) {
                        val mymy = x.getValue(DataClassViewUser::class.java)
                        mymy!!.uid = x.key.toString()
                        DbUser.add(mymy!!)
                        varJumlhUser = DbUser.count().toString()
                    }
                }
                varTvJumlhUser.setText(varJumlhUser)
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }
}