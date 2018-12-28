package com.example.admin.loginguitest


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.maps.DirectionsApiRequest
import com.google.maps.PendingResult
import com.google.maps.model.DirectionsResult

class VehicleArrivingFragment : Fragment() {

    lateinit var mInstanceOfhomepage: homepage

    lateinit var mVehicleAssigned : String
    var dbRef : FirebaseFirestore = FirebaseFirestore.getInstance()
    var collectionReference : CollectionReference = dbRef.collection("Vehicles")

    lateinit var mEstimatedTimeText : TextView
    lateinit var mRegistrationNumber : TextView
    lateinit var mRatingNumber : TextView

    var moveCamera = false

    var mInstanceOfSelf = this

    companion object {
        var TAG = "VehicleArriving"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_vehicle_arriving, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mEstimatedTimeText = view.findViewById(R.id.time_till_arrival)
        mRegistrationNumber = view.findViewById(R.id.registration_number)
        mRatingNumber = view.findViewById(R.id.rating_value)

        collectionReference.document(mVehicleAssigned).get().addOnCompleteListener {
            if(it.isSuccessful){
                mRatingNumber.text = it.result!!.data!!.getValue("rating") as String
                mRegistrationNumber.text = it.result!!.data!!.getValue("registration") as String
            }
            else Log.d(TAG, "Failed to get Vehicle Document")
        }

        Thread(getTimeToArrival).start()
    }

    var getTimeToArrival = Runnable() {
        var finished = false

        while(!finished){
            collectionReference.document(mVehicleAssigned).get().addOnCompleteListener {
                if(it.isSuccessful){
                    var vehicleLocation = it.result!!.data!!.getValue("location") as GeoPoint
                    var vLocation = LatLng(vehicleLocation.latitude, vehicleLocation.longitude)

                    getTimeFromVehicle(vLocation)

                    mInstanceOfhomepage.runOnUiThread() {
                        if (mInstanceOfhomepage.mVehicleMarker == null) {
                            mInstanceOfhomepage.mVehicleMarker = mInstanceOfhomepage.mMap
                                    .addMarker(MarkerOptions()
                                            .position(vLocation)
                                            .title("Vehicle " + mRegistrationNumber.text.toString()))
                        } else {
                            mInstanceOfhomepage.mVehicleMarker!!.setPosition(vLocation)
                        }
                    }

                    if(mInstanceOfhomepage.distanceChecker(mInstanceOfhomepage.getCurrentLocation(false), vLocation) < 10){
                        finished = true
                        mInstanceOfhomepage.runOnUiThread(){
                            mInstanceOfhomepage.vehicleArrived(mInstanceOfSelf, mRegistrationNumber.text.toString())
                        }
                    }
                }
                else Log.d(TAG, "Failed to get Vehicle Document")
            }

            if(!finished)
                Thread.sleep(2000)
        }
    }

    private fun getTimeFromVehicle(dest : LatLng){
        var destin : com.google.maps.model.LatLng
                = com.google.maps.model.LatLng(dest.latitude, dest.longitude)

        var userLocation = mInstanceOfhomepage.getCurrentLocation(false)

        Log.d(homepage.TAG, "calculateDirections: calculating directions.")

        var directions = DirectionsApiRequest(mInstanceOfhomepage.mGeoApiContext)

        directions.alternatives(true)
        directions.origin(com.google.maps.model.LatLng(userLocation.latitude, userLocation.longitude))

        Log.d(homepage.TAG, "calculateDirections: destination: " + destin.toString())

        directions.destination(destin).setCallback(object: PendingResult.Callback<DirectionsResult> {

            override fun onResult(result: DirectionsResult) {

                Log.d(homepage.TAG, "calculateDirections: routes: " + result.routes[0].toString())
                Log.d(homepage.TAG, "calculateDirections: duration: " + result.routes[0].legs[0].duration)
                Log.d(homepage.TAG, "calculateDirections: distance: " + result.routes[0].legs[0].distance)
                Log.d(homepage.TAG, "calculateDirections: geocodedWayPoints: " + result.geocodedWaypoints[0].toString())

                mInstanceOfhomepage.runOnUiThread(){
                    mEstimatedTimeText.text = result.routes[0].legs[0].duration.humanReadable

                    if(moveCamera == false) {
                        var geoWaypoint = result.routes[0].overviewPolyline.decodePath()[result.routes[0].overviewPolyline.decodePath().size / 2]
                        var camMove = LatLng(geoWaypoint.lat, geoWaypoint.lng)
                        mInstanceOfhomepage.moveCamera(camMove, 8f)
                        moveCamera = true
                    }
                }
            }

            override fun onFailure(e:Throwable) {

                Log.e(homepage.TAG, "calculateDirections: Failed to get directions: " + e.message)

                e.printStackTrace()
            }
        })
    }
}
