package com.example.admin.loginguitest


import android.net.wifi.hotspot2.pps.HomeSp
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.admin.loginguitest.DataClasses.Order
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class waitForConfirmation : Fragment() {

    //FireStore
    var dbRef = FirebaseFirestore.getInstance()
    var collectionReference = dbRef.collection("Trips")

    //CurrentUser
    var fbAuth = FirebaseAuth.getInstance()

    //Order
    lateinit var order : Order

    //HomePage
    lateinit var homepage : homepage

    final var instanceOfSelf = this

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wait_for_confirmation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var text : TextView = view.findViewById(R.id.textView8)

        var r = Runnable {
            var finished = false
            while(!finished) {
                collectionReference
                        .whereEqualTo("user", fbAuth.currentUser!!.email)
                        .get()
                        .addOnCompleteListener() {
                            if (it.isSuccessful) {

                                for (document in it.result!!.documents) {
                                    if (order.timeStamp.toDate().equals(document.data!!.getValue("Date"))) {
                                        if (document.data!!.getValue("Active").equals(true) &&
                                                !document.data!!.getValue("vehicle").equals(homepage.DEFAULT_VEHICLE_TAG)) {
                                            homepage.runOnUiThread() {
                                                Log.d("Confirmation", "Trip Confirmed")
                                                text.text = "Trip Confirmed"
                                            }
                                            homepage.orderObj.vehicle = document.data!!.getValue("vehicle") as String

                                            finished = true
                                        } else {
                                            Log.d("Confirmation", "Active field not updated")
                                        }
                                    }
                                }
                            }
                        }

                    Thread.sleep(5000)
                    if(finished) {
                        Log.d("Confirmation", "Sleeping before removing Order")

                        homepage.runOnUiThread() {
                            Log.d("Confirmation", "Removing Order")
                            homepage.removeFragment(instanceOfSelf)
                            //homepage.returnOrder()
                            homepage.startVehicleFragment()
                        }
                    }

            }


        }

        Thread(r).start()
    }
}
