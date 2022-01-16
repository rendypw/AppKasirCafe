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
import com.example.appkasircafe.databinding.ActivityUpdateMakananBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UpdateMakanan : AppCompatActivity() {
    lateinit var binding : ActivityUpdateMakananBinding
    lateinit var gambarUrl : String
    private var imageUri : Uri? = null
    lateinit var varTvTitleGambar : TextView
    lateinit var varetIdMakanan : EditText
    lateinit var varetNamaMakanan : EditText
    lateinit var varetHargaMakanan : EditText
    lateinit var varBpIdMakanan : String
    lateinit var varBpNamaMakanan : String
    lateinit var varBpHargaMakanan : String
    lateinit var varBpUidMakanan : String
    lateinit var varBtnSmpnMakanan : Button
    lateinit var ref: DatabaseReference
    lateinit var refGambar: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateMakananBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_update_makanan)
        declaration()
        myfunction()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK){
            imageUri = data?.data!!
            varTvTitleGambar.setText(varetNamaMakanan.text.toString())
            varTvTitleGambar.setVisibility(View.VISIBLE)
            binding.imageViewUplUpt.setImageURI(imageUri)
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
        val fileName = varetIdMakanan.text.toString()
        val storageReference = FirebaseStorage.getInstance().getReference("Makanan/$fileName")

        storageReference.putFile(imageUri!!).
        addOnSuccessListener {
            val result = it.metadata!!.reference!!.downloadUrl;
            result.addOnSuccessListener {

                var imageLink = it.toString()
                gambarUrl = imageLink
                binding.imageViewUplUpt.setImageURI(null)
                // Toast.makeText(this@TambahMakanan, "Berhasil Upload Image", Toast.LENGTH_SHORT).show()
                if (progressDialog.isShowing) progressDialog.dismiss()
                simpan()
            }
        }.addOnFailureListener{
            if (progressDialog.isShowing) progressDialog.dismiss()
            Toast.makeText(this@UpdateMakanan, "Gagal Upload Image", Toast.LENGTH_SHORT).show()

        }
    }

    fun declaration(){
        getMyData()
        varetIdMakanan = findViewById(R.id.et_UpidMakanan)
        varetNamaMakanan = findViewById(R.id.et_UpnamaMakanan)
        varetHargaMakanan = findViewById(R.id.et_UpHrgMakanan)
        varTvTitleGambar = findViewById(R.id.tvNamaGambarUpt)
        varBtnSmpnMakanan = findViewById(R.id.btnSmpnMakanan)
        varetIdMakanan.setText(varBpIdMakanan)
        varetNamaMakanan.setText(varBpNamaMakanan)
        varetHargaMakanan.setText(varBpHargaMakanan)
        ref = FirebaseDatabase.getInstance().getReference("Menu").child(varBpUidMakanan)
        val title  = varetIdMakanan.text.toString()
        refGambar = FirebaseStorage.getInstance().getReference("Minuman/$title")
    }
    fun getMyData(){
        val myValue = intent.extras
        if (myValue!=null){
            varBpUidMakanan = myValue.getString("bp_uid").toString()
            varBpIdMakanan = myValue.getString("bp_idMakanan").toString()
            varBpNamaMakanan = myValue.getString("bp_namaMakanan").toString()
            varBpHargaMakanan = myValue.getString("bp_HargaMakanan").toString()
        }
    }
    fun myfunction(){
        binding.buttonSelectImageUpt.setOnClickListener{
            selectImage()
        }
        varBtnSmpnMakanan.setOnClickListener {
            if (varetIdMakanan.text.toString() !="" && varetNamaMakanan.text.toString() !="" && varetHargaMakanan.text.toString() !="") {
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
        ref.child("id").setValue(varetIdMakanan.text.toString())
        ref.child("nama").setValue(varetNamaMakanan.text.toString())
        ref.child("harga").setValue(varetHargaMakanan.text.toString())
        ref.child("gambarSrc").setValue(gambarUrl)
        varetIdMakanan.setText("")
        varetNamaMakanan.setText("")
        varetHargaMakanan.setText("")
        varetIdMakanan.requestFocus()
        varTvTitleGambar.setVisibility(View.INVISIBLE)
        Toast.makeText(
            this, "Berhasil Disimpan",
            Toast.LENGTH_SHORT
        ).show()
    }
}