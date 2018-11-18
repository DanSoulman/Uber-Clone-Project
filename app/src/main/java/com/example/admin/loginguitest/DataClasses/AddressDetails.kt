package com.example.admin.loginguitest.DataClasses

class AddressDetails(address : String, area : String, city : String, country : String, postalCode : String){

    lateinit var mAddress : String
    lateinit var mArea : String
    lateinit var mCity : String
    lateinit var mCountry : String
    lateinit var mPostalCode : String

    init{

        mAddress = address
        mArea = area
        mCity = city
        mCountry = country
        mPostalCode = postalCode

    }
}