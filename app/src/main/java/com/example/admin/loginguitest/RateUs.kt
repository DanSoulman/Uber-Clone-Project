package com.example.admin.loginguitest


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.firebase.firestore.FirebaseFirestore

class RateUs : Fragment() {

    lateinit var mInstanceOfhomepage: homepage

    var mInstanceOfSelf = this

    var dbRef = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rate_us, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var thumbUp = view.findViewById<ImageView>(R.id.thumbUp)
        var thumbDown = view.findViewById<ImageView>(R.id.thumbDown)

        thumbUp.setOnClickListener(){
            thumbUpPressed()
        }

        thumbDown.setOnClickListener(){
            thumbDownPressed()
        }
    }

    fun thumbUpPressed(){
        dbRef.collection("Vehicles")
                .document(mInstanceOfhomepage.orderObj.vehicle)
                .get()
                .addOnCompleteListener {
            if(it.isSuccessful){
                if(it.result!!.data!!.getValue("positive counter") != null){
                    var stringCounter = it.result!!.data!!.getValue("positive counter") as String
                    var positiveCounter = stringCounter.toInt()
                    positiveCounter += 1

                    var tempMap : MutableMap<String, Any> = HashMap<String, Any>()
                    tempMap.put("positive counter", positiveCounter.toString())

                    dbRef.collection("Vehicles").document(it.result!!.id).update(tempMap)
                    endFragment()
                }
            }
        }
    }

    fun thumbDownPressed(){
        dbRef.collection("Vehicles")
                .document(mInstanceOfhomepage.orderObj.vehicle)
                .get()
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        if(it.result!!.data!!.getValue("positive counter") != null){
                            var stringCounter = it.result!!.data!!.getValue("negative counter") as String
                            var negativeCounter = stringCounter.toInt()
                            negativeCounter += 1

                            var tempMap : MutableMap<String, Any> = HashMap<String, Any>()
                            tempMap.put("negative counter", negativeCounter.toString())

                            dbRef.collection("Vehicles").document(it.result!!.id).update(tempMap)
                            endFragment()
                        }
                    }
                }
    }

    fun endFragment(){
        mInstanceOfhomepage.runOnUiThread(){
            mInstanceOfhomepage.removeFragment(mInstanceOfSelf)
            mInstanceOfhomepage.resetState()
        }
    }
}
