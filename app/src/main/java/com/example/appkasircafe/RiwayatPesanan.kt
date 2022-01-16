package com.example.appkasircafe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class RiwayatPesanan : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat_pesanan)
        val btn1: Button = findViewById(R.id.fragment1)
        val btn2: Button = findViewById(R.id.fragment2)
        var fm = supportFragmentManager
        var ft = fm.beginTransaction()
        ft
            .add(R.id.fragmentin,RiwayatTransaksi())
            .commit()
        btn1.setOnClickListener{
            var fm = supportFragmentManager
            var ft = fm.beginTransaction()
            ft
                .replace(R.id.fragmentin,RiwayatTransaksi())
                .commit()
        }
        btn2.setOnClickListener{
            var fm = supportFragmentManager
            var ft = fm.beginTransaction()
            ft
                    .replace(R.id.fragmentin,RiwayatNota())
                    .commit()
        }
    }
}