package com.example.admin.loginguitest.DataClasses

class AddressDetails(address : String?, area : String?, city : String?, country : String?, postalCode : String?){

    var mAddress : String?
    var mArea : String?
    var mCity : String?
    var mCountry : String?
    var mPostalCode : String?

    init{

        mAddress = address
        mArea = area
        mCity = city
        mCountry = country
        mPostalCode = postalCode

    }
}