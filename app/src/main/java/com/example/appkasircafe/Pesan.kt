package com.example.appkasircafe

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appkasircafe.adapter.AdapterDataMakanan
import com.example.appkasircafe.adapter.AdapterDataMinuman
import com.example.appkasircafe.adapter.AdapterDataPesanMakanan
import com.example.appkasircafe.adapter.AdapterDataPesanMinuman
import com.example.appkasircafe.dataclassadd.*
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Pesan : AppCompatActivity() {
    lateinit var fireTbMinuman: DatabaseReference
    private lateinit var layoutkuMinuman: RecyclerView
    lateinit var fireTbMakanan: DatabaseReference
   lateinit var fireTbOrder: DatabaseReference
    private lateinit var layoutkuMakanan: RecyclerView
    lateinit var btnPembayaran: Button
    lateinit var refTransaksi: DatabaseReference
    lateinit var waktuIn: String
    lateinit var waktuInId: String
    lateinit var etAtNama : EditText
    lateinit var etNoMeja : EditText
    private var DbMakanan: ArrayList<DataClassViewMenu> = arrayListOf()
    private var DbMinuman: ArrayList<DataClassViewMenu> = arrayListOf()
    private var DbOrder: ArrayList<DataClassViewMenu> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pesan)
        btnPembayaran = findViewById(R.id.btn_ChcOut)
        layoutkuMinuman = findViewById(R.id.rv_dataMinuman)
        etAtNama = findViewById(R.id.et_atNama)
        etNoMeja = findViewById(R.id.et_noMeja)
        fireTbMinuman = FirebaseDatabase.getInstance().getReference("Menu")
        layoutkuMakanan = findViewById(R.id.rv_dataMakanan)
        fireTbMakanan = FirebaseDatabase.getInstance().getReference("Menu")
        fireTbOrder = FirebaseDatabase.getInstance().getReference("Menu")
        btnPembayaran.setOnClickListener {
            if (etAtNama.text.toString() != "" && etNoMeja.text.toString() != ""){
                go()
            }
            else{
                Toast.makeText(this, "Masukan Nama dan Nomor Meja", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onStart(){
        super.onStart()
        loadMeFirstMinuman()
        loadMeFirstMakanan()
        loadMenu()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        for (i in 0 until DbOrder.size) {
            val z = 0
            fireTbOrder = FirebaseDatabase.getInstance().getReference("Menu").child(DbOrder[i].uid)
            fireTbOrder.child("selectedQ").setValue(z)
        }
    }
    private fun loadMenu(){
        fireTbOrder.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot!!.exists()) {
                    DbOrder.clear()
                    for (x in snapshot.children) {
                        val mymy = x.getValue(DataClassViewMenu::class.java)
                        mymy!!.uid = x.key.toString()
                        DbOrder.add(mymy!!)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    private fun go(){
        for (i in 0 until DbOrder.size){
            if (DbOrder[i].SelectedQ != 0){
                refTransaksi = FirebaseDatabase.getInstance().getReference("Keranjang")
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
                val now = Date()
                waktuIn=simpleDateFormat.format(now)
                val dateId = SimpleDateFormat("yyyyMMddHHmmss", Locale.US)
                val nowId = Date()
                waktuInId=dateId.format(nowId)
                val arrayKu = DataClassAddCartProduct()
                arrayKu.id = waktuInId
                arrayKu.nama = DbOrder[i].Nama
                arrayKu.harga = DbOrder[i].Harga.toInt()
                arrayKu.jumlah =DbOrder[i].SelectedQ*DbOrder[i].Harga.toInt()
                arrayKu.waktu = waktuIn
                arrayKu.selectedQuantity = DbOrder[i].SelectedQ
                val taskPush = refTransaksi.push()
                taskPush.setValue(arrayKu)
            }
        }
        val intent = Intent(this, Pembayaran::class.java)
        for (i in 0 until DbOrder.size){
            val z = 0
            fireTbOrder = FirebaseDatabase.getInstance().getReference("Menu").child(DbOrder[i].uid)
            fireTbOrder.child("selectedQ").setValue(z)

            val bundle = Bundle()
            bundle.putString("bp_atasnama", etAtNama.text.toString())
            bundle.putString("bp_nomeja", etNoMeja.text.toString())
            intent.putExtras(bundle)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TASK
            stopService(intent)
            startActivity(intent)
        }
    }
    fun loadMeFirstMinuman(){
        fireTbMinuman.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot!!.exists()) {
                    DbMinuman.clear()
                    for (x in snapshot.children) {
                        val mymy = x.getValue(DataClassViewMenu::class.java)
                        mymy!!.uid = x.key.toString()
                        if (mymy!!.Kategori.equals("Minuman")){
                            DbMinuman.add(mymy!!)
                        }
                    }
                }
                loadMeNowMinuman()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    private fun loadMeFirstMakanan(){
        fireTbMakanan.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot!!.exists()) {
                    DbMakanan.clear()
                    for (x in snapshot.children) {
                        val mymy = x.getValue(DataClassViewMenu::class.java)
                        mymy!!.uid = x.key.toString()
                        if (mymy!!.Kategori.equals("Makanan")){
                            DbMakanan.add(mymy!!)
                        }
                    }
                }
                loadMeNowMakanan()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    private fun loadMeNowMinuman(){
        layoutkuMinuman.layoutManager = LinearLayoutManager(this)
        val dbMinuman = AdapterDataPesanMinuman(DbMinuman)
        layoutkuMinuman.adapter = dbMinuman
    }
    private fun loadMeNowMakanan(){
        layoutkuMakanan.layoutManager = LinearLayoutManager(this)
        val dbMakanan = AdapterDataPesanMakanan(DbMakanan)
        layoutkuMakanan.adapter = dbMakanan
    }
}