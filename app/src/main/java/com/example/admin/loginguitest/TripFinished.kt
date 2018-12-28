package com.example.admin.loginguitest


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.fragment_trip_finished.*

class TripFinished : Fragment() {

    lateinit var mInstanceOfhomepage : homepage

    var mInstanceOfSelf = this

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_trip_finished, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var finishButton = view.findViewById<Button>(R.id.tripFinButton)

        finishButton.setOnClickListener {
            if(mInstanceOfhomepage.mMarker != null)
                mInstanceOfhomepage.mMarker!!.remove()

            if(mInstanceOfhomepage.mPolyline != null)
                mInstanceOfhomepage.mPolyline!!.remove()

            mInstanceOfhomepage.removeFragment(mInstanceOfSelf)
            mInstanceOfhomepage.resetState()
        }

        var rateUsButton = view.findViewById<Button>(R.id.RateUsButton)

        rateUsButton.setOnClickListener {
            if(mInstanceOfhomepage.mMarker != null)
                mInstanceOfhomepage.mMarker!!.remove()

            if(mInstanceOfhomepage.mPolyline != null)
                mInstanceOfhomepage.mPolyline!!.remove()

            mInstanceOfhomepage.startRatingFragment(this)
        }
    }

}
