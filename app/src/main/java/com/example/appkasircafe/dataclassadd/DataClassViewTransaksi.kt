package com.example.appkasircafe.dataclassadd

data class DataClassViewTransaksi (
        var uid : String = "",
        var id : String = "",
        var nama : String = "",
        var harga : Int = 0,
        var jumlah : Int = 0,
        var waktu : String = "",
        var selectedQuantity : Int = 0
)