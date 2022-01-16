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
import com.example.appkasircafe.databinding.ActivityUpdateMinumanBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UpdateMinuman : AppCompatActivity() {
    lateinit var binding : ActivityUpdateMinumanBinding
    lateinit var gambarUrl : String
    private var imageUri : Uri? = null
    lateinit var varTvTitleGambar : TextView
    lateinit var varetIdMinuman : EditText
    lateinit var varetNamaMinuman : EditText
    lateinit var varetHargaMinuman : EditText
    lateinit var varBpIdMinuman : String
    lateinit var varBpNamaMinuman : String
    lateinit var varBpHargaMinuman : String
    lateinit var varBpUidMinuman : String
    lateinit var varBtnSmpnMinuman : Button
    lateinit var ref: DatabaseReference
    lateinit var refGambar: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_update_minuman)
        binding = ActivityUpdateMinumanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        declaration()
        myfunction()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK){
            imageUri = data?.data!!
            varTvTitleGambar.setText(varetNamaMinuman.text.toString())
            varTvTitleGambar.setVisibility(View.VISIBLE)
            binding.imageViewUplUptMinuman.setImageURI(imageUri)
        }
    }
    fun selectImage(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)
    }
    fun uploadImage(){
        refGambar.delete()
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Upload Gambar ...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val fileName = varetIdMinuman.text.toString()
        val storageReference = FirebaseStorage.getInstance().getReference("Minuman/$fileName")

        storageReference.putFile(imageUri!!).
        addOnSuccessListener {
            val result = it.metadata!!.reference!!.downloadUrl;
            result.addOnSuccessListener {

                var imageLink = it.toString()
                gambarUrl = imageLink
                binding.imageViewUplUptMinuman.setImageURI(null)
                // Toast.makeText(this@TambahMakanan, "Berhasil Upload Image", Toast.LENGTH_SHORT).show()
                if (progressDialog.isShowing) progressDialog.dismiss()
                simpan()
            }
        }.addOnFailureListener{
            if (progressDialog.isShowing) progressDialog.dismiss()
            Toast.makeText(this@UpdateMinuman, "Gagal Upload Image", Toast.LENGTH_SHORT).show()

        }
    }

    fun declaration(){
        getMyData()
        varetIdMinuman = findViewById(R.id.et_UpidMinuman)
        varetNamaMinuman = findViewById(R.id.et_UpnamaMinuman)
        varetHargaMinuman = findViewById(R.id.et_UpHrgMinuman)
        varBtnSmpnMinuman = findViewById(R.id.btnSmpnMinuman)
        varTvTitleGambar = findViewById(R.id.tvNamaGambarUptMinuman)
        varetIdMinuman.setText(varBpIdMinuman)
        varetNamaMinuman.setText(varBpNamaMinuman)
        varetHargaMinuman.setText(varBpHargaMinuman)
        ref = FirebaseDatabase.getInstance().getReference("Menu").child(varBpUidMinuman)
        val title  = varetIdMinuman.text.toString()
        refGambar = FirebaseStorage.getInstance().getReference("Minuman/$title")
    }
    fun getMyData(){
        val myValue = intent.extras
        if (myValue!=null){
            varBpUidMinuman = myValue.getString("bp_uid").toString()
            varBpIdMinuman = myValue.getString("bp_idMinuman").toString()
            varBpNamaMinuman = myValue.getString("bp_namaMinuman").toString()
            varBpHargaMinuman = myValue.getString("bp_HargaMinuman").toString()
        }
    }
    fun myfunction(){
        binding.buttonSelectImageUptMinuman.setOnClickListener{
            selectImage()
        }
        varBtnSmpnMinuman.setOnClickListener {
            if (varetIdMinuman.text.toString() !="" && varetNamaMinuman.text.toString() !="" && varetHargaMinuman.text.toString() !="") {
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
    fun simpan(){
        ref.child("id").setValue(varetIdMinuman.text.toString())
        ref.child("nama").setValue(varetNamaMinuman.text.toString())
        ref.child("harga").setValue(varetHargaMinuman.text.toString())
        ref.child("gambarSrc").setValue(gambarUrl)
        varetIdMinuman.setText("")
        varetNamaMinuman.setText("")
        varetHargaMinuman.setText("")
        varetIdMinuman.requestFocus()
        Toast.makeText(
            this, "Berhasil Disimpan",
            Toast.LENGTH_SHORT
        ).show()
    }
}