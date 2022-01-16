package com.example.appkasircafe

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appkasircafe.adapter.AdapterDataPembayaran
import com.example.appkasircafe.dataclassadd.DataClassAddNota
import com.example.appkasircafe.dataclassadd.DataClassAddTransaksi
import com.example.appkasircafe.dataclassadd.DataClassViewCartProduct
import com.example.appkasircafe.dataclassadd.DataClassViewMenu
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class Pembayaran : AppCompatActivity() {
    lateinit var fireTbKeranjang: DatabaseReference
    private lateinit var layoutku: RecyclerView
    private var backPressedTime:Long = 0
    lateinit var btnSelesai : Button
    lateinit var refTransaksi: DatabaseReference
    lateinit var refBill: DatabaseReference
    lateinit var varBpAtNama : String
    lateinit var varBpNoMeja : String
    lateinit var waktuIn: String
    lateinit var varIdbill : String
    lateinit var varWktBill : String
    lateinit var tvVarTotBay : TextView
    lateinit var refUpdtItmSelect: DatabaseReference
    lateinit var fireTbOrder: DatabaseReference
    private var DbOrder: ArrayList<DataClassViewMenu> = arrayListOf()
    private var DbKeranjang: ArrayList<DataClassViewCartProduct> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembayaran)
        val myValue = intent.extras
        if (myValue!=null){
            varBpAtNama = myValue.getString("bp_atasnama").toString()
            varBpNoMeja = myValue.getString("bp_nomeja").toString()
        }
        tvVarTotBay = findViewById(R.id.tvTotalBayar)
        btnSelesai = findViewById(R.id.btn_done)
        layoutku = findViewById(R.id.rv_dataMakanan)
        fireTbOrder = FirebaseDatabase.getInstance().getReference("Menu")
        fireTbKeranjang = FirebaseDatabase.getInstance().getReference("Keranjang")
        btnSelesai.setOnClickListener {
            addTrans()

        }
    }
    override fun onStart(){
        super.onStart()
        loadMeFirst()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        fireTbKeranjang.removeValue()
        startActivity(Intent(this, Home::class.java))
        finishAffinity()

    }
    private fun loadMeFirst(){
        fireTbKeranjang.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot!!.exists()) {
                    DbKeranjang.clear()
                    for (x in snapshot.children) {
                        val mymy = x.getValue(DataClassViewCartProduct::class.java)
                        mymy!!.uid = x.key.toString()
                        DbKeranjang.add(mymy!!)
                    }
                }
                loadMeNow()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    private fun addTrans(){
        for (i in 0 until DbKeranjang.size){
            val arrayKu = DataClassAddTransaksi()
            refTransaksi = FirebaseDatabase.getInstance().getReference("Transaksi")
            arrayKu.id = DbKeranjang[i].id
            arrayKu.nama = DbKeranjang[i].nama
            arrayKu.harga = DbKeranjang[i].harga
            arrayKu.jumlah =DbKeranjang[i].jumlah
            arrayKu.waktu = DbKeranjang[i].waktu
            arrayKu.selectedQuantity = DbKeranjang[i].selectedQuantity
            val taskPush = refTransaksi.push()
            taskPush.setValue(arrayKu)
            varIdbill=DbKeranjang[i].id
            varWktBill=DbKeranjang[i].waktu
        }
        addNota()
        finishAffinity()
    }
    private fun addNota(){
        var total = 0
        for (i in 0 until DbKeranjang.size){
            total += DbKeranjang[i].jumlah
        }
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val now = Date()
        waktuIn=simpleDateFormat.format(now)
        val arrayKu = DataClassAddNota()
        refBill = FirebaseDatabase.getInstance().getReference("Nota")
        arrayKu.id = varIdbill
        arrayKu.atNama = varBpAtNama
        arrayKu.noMeja = varBpNoMeja
        arrayKu.waktu = waktuIn
        arrayKu.totalBayar = total
        val taskPush = refBill.push()
        taskPush.setValue(arrayKu)
        out()
        finishAffinity()
    }
   /* private fun reselec(){
        fireTbOrder = FirebaseDatabase.getInstance().getReference("Menu")
        fireTbOrder.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot!!.exists()) {
                    DbOrder.clear()
                    for (x in snapshot.children) {
                        val mymy = x.getValue(DataClassViewMenu::class.java)
                        mymy!!.uid = x.key.toString()
                        if (mymy!!.SelectedQ != 0){
                            DbOrder.add(mymy!!)
                            val z = 0
                            refUpdtItmSelect = FirebaseDatabase.getInstance().getReference("Menu").child(mymy!!.uid)
                            refUpdtItmSelect.child("selectedQ").setValue(z)

                        }
                    }
                }
                out()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }*/
    private fun out(){
        val intent = Intent(this, Home::class.java)
        fireTbKeranjang.removeValue()
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finishAffinity()
        finish()
    }
    private fun loadMeNow(){
        layoutku.layoutManager = LinearLayoutManager(this)
        val dbPmbyrn = AdapterDataPembayaran(DbKeranjang)
        layoutku.adapter = dbPmbyrn

        var total = 0
        for (i in 0 until DbKeranjang.size){
            total += DbKeranjang[i].jumlah
        }
        tvVarTotBay.setText(total.toString())
    }
}