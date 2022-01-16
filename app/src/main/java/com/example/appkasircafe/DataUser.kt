package com.example.appkasircafe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appkasircafe.adapter.AdapterDataUser
import com.example.appkasircafe.dataclassadd.DataClassViewUser
import com.google.firebase.database.*

class DataUser : AppCompatActivity() {
    lateinit var fireTbUser: DatabaseReference
    private lateinit var layoutku: RecyclerView
    private var DbUser: ArrayList<DataClassViewUser> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_user)
        layoutku = findViewById(R.id.rv_dataUser)
        fireTbUser = FirebaseDatabase.getInstance().getReference("User")
    }
    override fun onStart(){
        super.onStart()
        loadMeFirst()
    }
    private fun loadMeFirst(){
        fireTbUser.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot!!.exists()) {
                    DbUser.clear()
                    for (x in snapshot.children) {
                        val mymy = x.getValue(DataClassViewUser::class.java)
                        mymy!!.uid = x.key.toString()
                        DbUser.add(mymy!!)
                    }
                }
                loadMeNow()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun loadMeNow(){
        layoutku.layoutManager = LinearLayoutManager(this)
        val valDbUsr = AdapterDataUser(DbUser)
        layoutku.adapter = valDbUsr
    }
}