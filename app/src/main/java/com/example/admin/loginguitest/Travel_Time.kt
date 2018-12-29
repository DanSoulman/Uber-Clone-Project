package com.example.admin.loginguitest


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.admin.loginguitest.DataClasses.Order

class Travel_Time : Fragment() {

    lateinit var mInstanceOfhomepage: homepage
    lateinit var mOrderObj : Order

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_travel__time, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    var updateTimeToArrival : Runnable =  Runnable{

        var finished = false

        while(!finished){

            Thread.sleep(2000)
        }
    }
}
