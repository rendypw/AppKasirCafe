package com.example.appkasircafe

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.appkasircafe.databinding.ActivityTambahMinumanBinding
import com.example.appkasircafe.dataclassadd.DataClassViewMenu
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlin.collections.ArrayList

class TambahMinuman : AppCompatActivity() {
    lateinit var binding : ActivityTambahMinumanBinding
    lateinit var gambarUrl : String
    private var imageUri : Uri? = null
    lateinit var varTvTitleGambar : TextView
    lateinit var varEtIdMnmn: EditText
    lateinit var varBpNamauser : String
    private var varJumlahMinuman : String? = null
    lateinit var varTvJumlahMinuman : TextView
    lateinit var varTvNamauser : TextView
    lateinit var varEtNamMnmn: EditText
    lateinit var varEtHrgMnmn: EditText
    lateinit var varBtnTmbhMnmn: Button
    lateinit var ref: DatabaseReference
    private var DbMinuman: ArrayList<DataClassViewMenu> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_minuman)
        binding = ActivityTambahMinumanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonSelectImage.setOnClickListener{
            selectImage()
        }
        ref = FirebaseDatabase.getInstance().getReference("Menu")
        varTvTitleGambar = findViewById(R.id.tvNamaGambarr)
        varTvJumlahMinuman = findViewById(R.id.tvjmlhMinuman)
        varTvNamauser = findViewById(R.id.tvnamauser)
        varEtIdMnmn = findViewById(R.id.et_idMnman)
        varEtNamMnmn =findViewById(R.id.et_namaMnuman)
        varEtHrgMnmn = findViewById(R.id.et_HrgMnman)
        varBtnTmbhMnmn=findViewById(R.id.btnTambahMnman)
        varBtnTmbhMnmn.setOnClickListener {
            if (varEtIdMnmn.text.toString() !="" && varEtNamMnmn.text.toString() !="" && varEtHrgMnmn.text.toString() !=""){
                if (imageUri!= null){
                    uploadImage()
                }else{
                    Toast.makeText(
                        this, "Gambar tidak Boleh Kosong !!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }else{
                Toast.makeText(
                    this, "Kolom tidak Boleh Kosong !!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    override fun onStart() {
        super.onStart()
        cekJumlah()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK){
            imageUri = data?.data!!
            varTvTitleGambar.setText(varEtNamMnmn.text.toString())
            varTvTitleGambar.setVisibility(View.VISIBLE)
            binding.imageViewUplMinuman.setImageURI(imageUri)
        }
    }
    fun selectImage(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }
    fun uploadImage(){
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Upload Gambar ...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val fileName = varEtIdMnmn.text.toString()
        val storageReference = FirebaseStorage.getInstance().getReference("Minuman/$fileName")
        storageReference.putFile(imageUri!!).
        addOnSuccessListener {
            val result = it.metadata!!.reference!!.downloadUrl;
            result.addOnSuccessListener {
                var imageLink = it.toString()
                gambarUrl = imageLink
                binding.imageViewUplMinuman.setImageURI(null)
                // Toast.makeText(this@TambahMakanan, "Berhasil Upload Image", Toast.LENGTH_SHORT).show()
                if (progressDialog.isShowing) progressDialog.dismiss()
                simpan()
            }
        }.addOnFailureListener{
            if (progressDialog.isShowing) progressDialog.dismiss()
            Toast.makeText(this@TambahMinuman, "Gagal Upload Image", Toast.LENGTH_SHORT).show()
        }
    }
    fun simpan(){
        val arrayKu = DataClassViewMenu()
        arrayKu.ID = varEtIdMnmn.text.toString()
        arrayKu.Nama = varEtNamMnmn.text.toString()
        arrayKu.Harga = varEtHrgMnmn.text.toString()
        arrayKu.Kategori = "Minuman"
        arrayKu.GambarSrc = gambarUrl
        val taskPush = ref.push()
        taskPush.setValue(arrayKu)
        var x: Int
        Toast.makeText(
            this, "Minuman berhasil ditambah",
            Toast.LENGTH_SHORT
        ).show()
        varTvTitleGambar.setVisibility(View.INVISIBLE)
        varEtIdMnmn.setText("")
        varEtNamMnmn.setText("")
        varEtHrgMnmn.setText("")
        varEtIdMnmn.requestFocus()
        cekJumlah()
    }
    fun cekJumlah(){
        val myValue = intent.extras
        if (myValue!=null){
            varBpNamauser = myValue.getString("bp_namauser").toString()
            varTvNamauser.setText(varBpNamauser)
        }

        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val n : Int = 1
                var id : String? = null
                if (snapshot!!.exists()) {
                    DbMinuman.clear()
                    for (x in snapshot.children) {
                        val mymy = x.getValue(DataClassViewMenu::class.java)
                        mymy!!.uid = x.key.toString()
                        if (mymy!!.Kategori=="Minuman"){
                            DbMinuman.add(mymy!!)
                            varJumlahMinuman = DbMinuman.count().toString()
                            varTvJumlahMinuman.setText(varJumlahMinuman)
                        }
                        id=mymy!!.ID
                    }
                }
                if (id==null){
                    varEtIdMnmn.setText("1")
                    varTvJumlahMinuman.setText("0")
                }
                else{
                    var AutoIncm : Int
                    AutoIncm = id.toString().toInt()+n
                    varEtIdMnmn.setText(AutoIncm.toString())
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}