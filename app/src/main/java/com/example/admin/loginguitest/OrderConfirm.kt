package com.example.admin.loginguitest

import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import java.text.DecimalFormat
import com.google.android.gms.maps.model.LatLng

class OrderConfirm : Activity() {

    companion object {
        val TAG = "Order Confirm Activity"
    }

    lateinit var nameDisplay    : TextView
    lateinit var location       : TextView
    lateinit var distance       : TextView
    lateinit var cost           : TextView

    lateinit var order : Order

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_confirm)
        order = intent.getSerializableExtra("orderObject") as Order

        Log.d(TAG, order.name)

        nameDisplay = findViewById(R.id.nameDisplay)
        location    = findViewById(R.id.locationDisplay)
        distance    = findViewById(R.id.distanceDisplay)
        cost        = findViewById(R.id.costDisplay)
        displayText()
    }

    fun displayText(){

        var distanceVal = CalculationByDistance(order.getSource(), order.getDestination())
        var costVal     = distanceVal * 0.10
        val df = DecimalFormat("#.##")



        nameDisplay.text = order.name
        location.text   = order.destinationName
        distance.text   = df.format(distanceVal).toString()
        cost.text       = df.format(costVal).toString()

    }

    fun CalculationByDistance(StartP: LatLng, EndP: LatLng): Double {

        val Radius = 6371// radius of earth in Km
        val lat1 = StartP.latitude
        val lat2 = EndP.latitude
        val lon1 = StartP.longitude
        val lon2 = EndP.longitude
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + (Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2))
        val c = 2 * Math.asin(Math.sqrt(a))
        val valueResult = Radius * c
        val km = valueResult / 1
        val newFormat = DecimalFormat("####")
        val kmInDec = Integer.valueOf(newFormat.format(km))
        val meter = valueResult % 1000
        val meterInDec = Integer.valueOf(newFormat.format(meter))
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec)

        return Radius * c
    }
}
