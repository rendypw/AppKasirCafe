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
import com.example.appkasircafe.databinding.ActivityTambahMakananBinding
import com.example.appkasircafe.dataclassadd.DataClassAddMenu
import com.example.appkasircafe.dataclassadd.DataClassViewMenu
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlin.collections.ArrayList

class TambahMakanan : AppCompatActivity() {
    lateinit var binding : ActivityTambahMakananBinding
    lateinit var gambarUrl : String
    private var imageUri : Uri? = null
    lateinit var varEtIdMknn: EditText
    private var varJumlhMakanan : String? = null
    lateinit var varTvJumlhMakanan : TextView
    lateinit var varTvTitleGambar : TextView
    lateinit var varEtNamMknn: EditText
    lateinit var varEtHrgMknn: EditText
    lateinit var varBtnTmbhMknn: Button
    lateinit var varTvNamauser : TextView
    lateinit var varNamaUser : String
    lateinit var ref: DatabaseReference
    private var DbMakanan: ArrayList<DataClassViewMenu> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_tambah_makanan)
        binding = ActivityTambahMakananBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonSelectImage.setOnClickListener{
            selectImage()
        }
        ref = FirebaseDatabase.getInstance().getReference("Menu")
        varTvNamauser = findViewById(R.id.tvnamauser)
        varTvJumlhMakanan = findViewById(R.id.tvjmlhMakanan)
        varTvTitleGambar = findViewById(R.id.tvNamaGambarr)
        varEtIdMknn = findViewById(R.id.et_idMknan)
        varEtNamMknn =findViewById(R.id.et_namaMknan)
        varEtHrgMknn = findViewById(R.id.et_HrgMknan)
        varBtnTmbhMknn=findViewById(R.id.btnTambahMknan)
        varBtnTmbhMknn.setOnClickListener {
            if (varEtIdMknn.text.toString() !="" && varEtNamMknn.text.toString() !="" && varEtHrgMknn.text.toString() !="") {
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
        cekData()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK){
            imageUri = data?.data!!
            varTvTitleGambar.setText(varEtNamMknn.text.toString())
            varTvTitleGambar.setVisibility(View.VISIBLE)
            binding.imageViewUpl.setImageURI(imageUri)
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
        val fileName = varEtIdMknn.text.toString()
        val storageReference = FirebaseStorage.getInstance().getReference("Makanan/$fileName")
        storageReference.putFile(imageUri!!).
                addOnSuccessListener {
                    val result = it.metadata!!.reference!!.downloadUrl;
                    result.addOnSuccessListener {
                        var imageLink = it.toString()
                        gambarUrl = imageLink
                        binding.imageViewUpl.setImageURI(null)
                       // Toast.makeText(this@TambahMakanan, "Berhasil Upload Image", Toast.LENGTH_SHORT).show()
                        if (progressDialog.isShowing) progressDialog.dismiss()
                        simpan()
                    }
                }.addOnFailureListener{
            if (progressDialog.isShowing) progressDialog.dismiss()
            Toast.makeText(this@TambahMakanan, "Gagal Upload Image", Toast.LENGTH_SHORT).show()
        }
    }
    fun simpan(){
        val arrayKu = DataClassAddMenu()
        arrayKu.ID = varEtIdMknn.text.toString()
        arrayKu.Nama = varEtNamMknn.text.toString()
        arrayKu.Harga = varEtHrgMknn.text.toString()
        arrayKu.Kategori = "Makanan"
        arrayKu.GambarSrc = gambarUrl
        val taskPush = ref.push()
        taskPush.setValue(arrayKu)
        var x: Int
        Toast.makeText(
            this, "Makanan berhasil ditambah",
            Toast.LENGTH_SHORT
        ).show()
        varTvTitleGambar.setVisibility(View.INVISIBLE)
        varEtIdMknn.setText("")
        varEtNamMknn.setText("")
        varEtHrgMknn.setText("")
        varEtIdMknn.requestFocus()
        cekData()
    }
    fun cekData(){
        val myValue = intent.extras
        if (myValue!=null){
            varNamaUser = myValue.getString("bp_namauser").toString()
            varTvNamauser.setText(varNamaUser)
        }
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val n : Int = 1
                var id : String? = null
                if (snapshot!!.exists()) {
                    DbMakanan.clear()
                    for (x in snapshot.children) {
                        val mymy = x.getValue(DataClassViewMenu::class.java)
                        mymy!!.uid = x.key.toString()
                        if (mymy!!.Kategori == "Makanan"){
                            DbMakanan.add(mymy!!)
                            varJumlhMakanan = DbMakanan.count().toString()
                            varTvJumlhMakanan.setText(varJumlhMakanan)
                        }
                        id=mymy!!.ID
                    }
                }
                if (id==null){
                    varEtIdMknn.setText("1")
                    varTvJumlhMakanan.setText("0")
                }
                else{
                    var AutoInc : Int
                    AutoInc = id.toString().toInt()+n
                    varEtIdMknn.setText(AutoInc.toString())
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}