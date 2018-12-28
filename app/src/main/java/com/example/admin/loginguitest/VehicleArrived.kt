package com.example.admin.loginguitest


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class VehicleArrived : Fragment() {

    lateinit var mInstanceOfHomepage : homepage
    lateinit var mRegistrationNumber : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vehicle_arrived, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var regNumText = view.findViewById<TextView>(R.id.vehicle_arrived_registration_number)
        regNumText.text = mRegistrationNumber

        var confirmButton = view.findViewById<Button>(R.id.vehicle_arrived_button)

        confirmButton.setOnClickListener {
            mInstanceOfHomepage.removeFragment(this)
            mInstanceOfHomepage.returnOrder()
        }
    }
}
