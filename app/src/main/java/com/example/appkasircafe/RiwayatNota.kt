package com.example.appkasircafe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appkasircafe.adapter.AdapterDataNota
import com.example.appkasircafe.adapter.AdapterDataTransaksi
import com.example.appkasircafe.dataclassadd.DataClassViewNota
import com.example.appkasircafe.dataclassadd.DataClassViewTransaksi
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RiwayatNota.newInstance] factory method to
 * create an instance of this fragment.
 */
class RiwayatNota : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    var fragmentView : View? = null
    var firedatabase : FirebaseDatabase? = null
    var BalList : ArrayList<DataClassViewNota> = arrayListOf()
    var ref : DatabaseReference? = null
    lateinit var waktuIn: String

    private lateinit var mRecyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentView= inflater.inflate(R.layout.fragment_riwayat_nota, container, false)
        firedatabase = FirebaseDatabase.getInstance()
        mRecyclerView = fragmentView?.findViewById(R.id.rv_dataNota)!!
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(context)

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val now = Date()
        waktuIn=simpleDateFormat.format(now)
        BalList = arrayListOf<DataClassViewNota>()
        ref = FirebaseDatabase.getInstance().getReference("Nota")
        ref?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0!!.exists()){
                    BalList.clear()
                    for (h in p0.children){
                        val bal = h.getValue(DataClassViewNota::class.java)
                        if (bal!!.waktu == waktuIn){
                            BalList.add(bal!!)
                        }
                    }
                }
                BalList.sortByDescending {
                    it.waktu
                }
                val adapter = AdapterDataNota(BalList)
                mRecyclerView.setAdapter(adapter)
            }
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented")
                //To change body of created functions use File | Settings | File Templates.
            }
        })
        return  fragmentView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RiwayatNota.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                RiwayatNota().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}