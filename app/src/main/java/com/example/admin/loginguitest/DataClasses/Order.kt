package com.example.admin.loginguitest.DataClasses

import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.internal.measurement.zzsl.init
import com.google.android.gms.maps.model.LatLng
import com.google.api.Billing
import com.google.firebase.Timestamp
import java.io.Serializable

class Order(){


    lateinit var name   : String

    lateinit var email  : String

    lateinit var source : LatLng

    lateinit var destination : LatLng

    lateinit var destinationName : String

    lateinit var sourceAddress: String

    lateinit var timeStamp : Timestamp

    var distance : Long = 0

    var cost = 0.0

    constructor(name : String, email : String, source : LatLng,
                destination: LatLng, destinationName : String,
                sourceAddress : String, cost : Double,
                distance : Long, time : Timestamp) : this() {

        this.name   = name

        this.email  = email

        this.source = source

        this.destination = destination

        this.destinationName = destinationName

        this.sourceAddress = sourceAddress

        this.distance = distance

        this.cost = cost

        this.timeStamp = time

    }


}