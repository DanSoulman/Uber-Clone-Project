package com.example.admin.loginguitest

import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.internal.measurement.zzsl.init
import com.google.android.gms.maps.model.LatLng
import com.google.api.Billing
import java.io.Serializable

class Order() : Serializable { //Parcelable{

    /*override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }*/

    lateinit var name   : String
    lateinit var email  : String

    var sourceLat   : Double = 0.0
    var sourceLong  : Double = 0.0

    var destinationLat  : Double = 0.0
    var destinationLong : Double = 0.0

    lateinit var destinationName : String

    constructor(name : String, email : String, source : LatLng, destination: LatLng, destinationName : String) : this() {
        this.name   = name
        this.email  = email

        this.sourceLat  = source.latitude
        this.sourceLong = source.longitude

        this.destinationLat     = destination.latitude
        this.destinationLong    = destination.longitude

        this.destinationName    = destinationName

    }

    fun getSource()      : LatLng{ return LatLng(sourceLat, sourceLong) }
    fun getDestination() : LatLng{ return LatLng(destinationLat, destinationLong)}

    /*
    constructor(parc : Parcel) : this() {
        this.name = parc.readString()
        this.email = parc.readString()
        this.source = parc.read as LatLng
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Order> {
        override fun createFromParcel(parcel: Parcel): Order {
            return Order(parcel)
        }

        override fun newArray(size: Int): Array<Order?> {
            return arrayOfNulls(size)
        }
    }
    */
}