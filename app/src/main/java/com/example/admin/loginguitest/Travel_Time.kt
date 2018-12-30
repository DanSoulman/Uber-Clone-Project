package com.example.admin.loginguitest


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.DirectionsApiRequest
import com.google.maps.PendingResult
import com.google.maps.model.DirectionsResult

class Travel_Time : Fragment() {

    lateinit var mInstanceOfhomepage: homepage
    var finished = false

    lateinit var distanceText : TextView
    lateinit var durationText : TextView

    var dbRef = FirebaseFirestore.getInstance()
    var fbAuth = FirebaseAuth.getInstance()

    companion object {
        var TAG = "TRAVEL_TIME"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_travel__time, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        distanceText = view.findViewById(R.id.distance_number)
        durationText = view.findViewById(R.id.time_till_arrival_destination)

        Thread(checkDistanceAndTime).start()
    }

    var checkDistanceAndTime = Runnable{

        while(!finished){
            getTimeFromVehicle(mInstanceOfhomepage.orderObj.destination)
            Thread.sleep(2000)
        }

        pushFinalDetailsToDB()

        mInstanceOfhomepage.runOnUiThread() {

            mInstanceOfhomepage.removeFragment(this)

            var fragM = TripFinished()

            fragM.mInstanceOfhomepage = mInstanceOfhomepage

            var fm = mInstanceOfhomepage.supportFragmentManager.beginTransaction()

            fm.replace(R.id.framelay, fragM).commit()

        }

    }

    private fun getTimeFromVehicle(dest : LatLng){
        var destin : com.google.maps.model.LatLng
                = com.google.maps.model.LatLng(dest.latitude, dest.longitude)

        var userLocation : LatLng? = null

        mInstanceOfhomepage.runOnUiThread() {
            var locate = mInstanceOfhomepage.mMap.myLocation
            userLocation = LatLng(locate.latitude, locate.longitude)
        }

        while(userLocation == null){

        }

        Log.d(homepage.TAG, "calculateDirections: calculating directions.")

        var directions = DirectionsApiRequest(mInstanceOfhomepage.mGeoApiContext)

        directions.alternatives(true)
        directions.origin(com.google.maps.model.LatLng(userLocation!!.latitude, userLocation!!.longitude))

        Log.d(TAG, "calculateDirections: destination: " + destin.toString())

        directions.destination(destin).setCallback(object: PendingResult.Callback<DirectionsResult> {

            override fun onResult(result: DirectionsResult) {
                Log.d(TAG, "Update")
                Log.d(TAG, "calculateDirections: routes: " + result.routes[0].toString())
                Log.d(TAG, "calculateDirections: duration: " + result.routes[0].legs[0].duration)
                Log.d(TAG, "calculateDirections: distance: " + result.routes[0].legs[0].distance)
                Log.d(TAG, "calculateDirections: geocodedWayPoints: " + result.geocodedWaypoints[0].toString())

                mInstanceOfhomepage.runOnUiThread(){
                    if(!finished) {
                        distanceText.text = result.routes[0].legs[0].distance.humanReadable
                        durationText.text = result.routes[0].legs[0].duration.humanReadable

                        if(result.routes[0].legs[0].distance.inMeters < mInstanceOfhomepage.DISTANCE_FOR_TRIP_TO_END){
                            finished = true
                        }
                    }
                }
            }

            override fun onFailure(e:Throwable) {

                Log.e(homepage.TAG, "calculateDirections: Failed to get directions: " + e.message)

                e.printStackTrace()
            }
        })
    }

    fun pushFinalDetailsToDB(){
        dbRef.collection("Users")
                .whereEqualTo("email", fbAuth.currentUser!!.email)
                .get().addOnCompleteListener {
                    if(it.isSuccessful){
                        Log.d("Trip Completed", "Balance Updated")
                        var documentReference = it.result!!.documents[0].id

                        var stringBalance = it.result!!.documents[0].data!!.getValue("balance")
                        var balance = 0.0

                        if(stringBalance is Double)
                            balance = stringBalance

                        else if(stringBalance is String)
                            balance = stringBalance.toDouble()

                        balance -= mInstanceOfhomepage.orderObj.cost

                        if(mInstanceOfhomepage.orderObj.cost == 0.0)
                            balance -= 1

                        var tripsObj = it.result!!.documents[0].data!!.getValue("trips")

                        var tripsCount = 0
                        if(tripsObj is String)
                            tripsCount = tripsObj.toInt()

                        else if(tripsObj is Int)
                            tripsCount = tripsObj

                        tripsCount++

                        var mutableMap : MutableMap<String, Any> = HashMap<String, Any>()
                        mutableMap.put("balance", balance.toString())
                        mutableMap.put("trips", tripsCount.toString())

                        dbRef.collection("Users").document(documentReference).update(mutableMap)

                        mutableMap = HashMap<String, Any>()
                        mutableMap.put("active", false)

                        dbRef.collection("Vehicles").document(mInstanceOfhomepage.orderObj.vehicle).update(mutableMap)

                    }
                }
    }
}
