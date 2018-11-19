package com.example.admin.loginguitest

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.example.admin.loginguitest.DataClasses.AddressDetails
import com.example.admin.loginguitest.DataClasses.Order
import com.example.admin.loginguitest.R.id.emailDisplay
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.PlaceDetectionClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.PendingResult
import com.google.maps.model.DirectionsResult
import kotlinx.android.synthetic.main.activity_homepage.*
import java.util.*
import kotlin.collections.ArrayList

class homepage : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    //-----------UI Elements------------
    lateinit var emailDisplay : TextView

    lateinit var uName : TextView

    lateinit var mDrawerLayout : DrawerLayout
    //-----------------------------------

    //------------DataBase Objects------------
    private lateinit var fbAuth : FirebaseAuth

    var dbRef : FirebaseFirestore = FirebaseFirestore.getInstance()

    var collectionReference : CollectionReference = dbRef.collection("Users")

    //---------------------------------------

    //-----Map and Location Objects-------
    lateinit var mMap: GoogleMap

    private lateinit var currentLocationRetrieve : FusedLocationProviderClient

    lateinit var mGeoDataClient : Geocoder

    lateinit var mPlaceDetectionClient : PlaceDetectionClient

    lateinit var mGeoApiContext : GeoApiContext

    var location : LatLng? = null

    val PLACE_PICKER_REQUEST = 1

    var mMarker : Marker? = null

    var mPolyline : Polyline? = null

    //-----------------------------------

    //---------Data Class Objects--------

    lateinit var placeOrder : PlacesOrder

    lateinit var orderObj : Order

    lateinit var results : DirectionsResult

    //-----------------------------------


    companion object {
        val TAG = "HOMEPAGE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        var toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        fbAuth = FirebaseAuth.getInstance()

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        var nView : View = nav_view.getHeaderView(0)

        emailDisplay = nView.findViewById(R.id.emailDisplay)

        emailDisplay.text = fbAuth.currentUser!!.email

        mDrawerLayout = findViewById(R.id.drawer_layout)

        uName = nView.findViewById(R.id.uName)

        addingNames()

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)

        mGeoDataClient = Geocoder(this, Locale.getDefault())

        if(!(::mGeoApiContext.isInitialized)) {
            mGeoApiContext = GeoApiContext.Builder()
                    .apiKey("AIzaSyAd2R1rT8L7h5vOZpDHsnuEMSteFVL9Kdk").build()
        }
    }

    fun calculateDirections(dest : LatLng, placeOrder : Any){

        var destin : com.google.maps.model.LatLng
                = com.google.maps.model.LatLng(dest.latitude, dest.longitude)

        var userLocation = getCurrentLocation()

        Log.d(TAG, "calculateDirections: calculating directions.")

        var directions = DirectionsApiRequest(mGeoApiContext)

        directions.alternatives(true)
        directions.origin(com.google.maps.model.LatLng(userLocation.latitude, userLocation.longitude))

        Log.d(TAG, "calculateDirections: destination: " + destin.toString())

        directions.destination(destin).setCallback(object: PendingResult.Callback<DirectionsResult> {

            override fun onResult(result:DirectionsResult) {

                Log.d(TAG, "calculateDirections: routes: " + result.routes[0].toString())
                Log.d(TAG, "calculateDirections: duration: " + result.routes[0].legs[0].duration)
                Log.d(TAG, "calculateDirections: distance: " + result.routes[0].legs[0].distance)
                Log.d(TAG, "calculateDirections: geocodedWayPoints: " + result.geocodedWaypoints[0].toString())

                if(placeOrder is PlacesOrder) {
                    placeOrder.result = result

                    placeOrder.homepage.runOnUiThread(object : Runnable{

                        override fun run() {
                            placeOrder.setDestinationDetails()
                        }
                    })
                }
            }

            override fun onFailure(e:Throwable) {

                Log.e(TAG, "calculateDirections: Failed to get directions: " + e.message)

                e.printStackTrace()
            }
        })

    }


    private fun addingNames(){

        emailDisplay.text = fbAuth.currentUser!!.email

        collectionReference
                .whereEqualTo("email", fbAuth.currentUser!!.email)
                .get()
                .addOnSuccessListener {  }

        collectionReference
                .whereEqualTo("email", fbAuth.currentUser!!.email)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            Log.d(TAG, document.id + " => " + document.data)
                            uName.text = document.data.getValue("name").toString()
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.exception)
                    }
                }

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.homepage, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            android.R.id.home -> {
                mDrawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_call -> {
                // Handle the camera action
            }
            R.id.nav_funds -> {

            }
            R.id.nav_settings -> {

            }
            R.id.nav_support -> {

            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun getCurrentLocation() : LatLng{
        currentLocationRetrieve = LocationServices.getFusedLocationProviderClient(this as AppCompatActivity)

        if(location == null){
            location = LatLng(51.8860, 8.5336)
        }

        if(checkPermission()){
            currentLocationRetrieve.lastLocation.addOnSuccessListener {

                it?.let {
                    location = LatLng(it.latitude, it.longitude)
                }

                moveCamera(location!!, 15f)

                if(it == null){
                    Log.d(TAG, "Location value is null")
                }

                Log.d(TAG, "Value retrieved successfully")
            }

            if(location == LatLng(51.8860, 8.5336)){
                Log.d(TAG, "Couldn't pull co-ordinates")
            }
        }

        else{
            Log.d(TAG, "Couldn't pull co-ordinates")
        }

        return location!!
    }

    fun getCurrentAddress() : AddressDetails{

        lateinit var addressObj : AddressDetails
        lateinit var lLocation  : LatLng

        var mGeoDataClient = Geocoder(this, Locale.getDefault())

        if(mMap.myLocation != null){
            lLocation = LatLng(mMap.myLocation.latitude, mMap.myLocation.longitude)
        }
        else{
            lLocation = getCurrentLocation()
        }


        var addresses = mGeoDataClient.getFromLocation(
                lLocation.latitude,
                lLocation.longitude, 1)

        addressObj = AddressDetails(
                addresses.get(0).getAddressLine(0),
                addresses.get(0).locality,
                addresses.get(0).adminArea,
                addresses.get(0).countryName,
                addresses.get(0).postalCode)


        return addressObj
    }

    fun moveCamera(location : LatLng, zoom : Float){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoom))

    }

    fun addMarker(location : LatLng, title : String){
        mMap.addMarker(MarkerOptions().position(location).title(title))
    }

    private fun checkPermission() : Boolean{
        if (ContextCompat.checkSelfPermission(this as AppCompatActivity, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = true


            Log.d(TAG, "Permission to pull map source granted")

            return true
        }

        else {
            Log.d(TAG, "Permission for source not granted")

            ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION), 200)
        }

        return false
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

        checkPermission()

        getCurrentLocation()


    }

    @SuppressLint("RestrictedApi")
    fun hideMap(){
        //var viewMap = findViewById<View>(R.id.map)
        //viewMap.visibility = View.INVISIBLE

        //mMap.uiSettings.setMapToolbarEnabled(false)
        mMap.uiSettings.setMyLocationButtonEnabled(false)

        var fabButton = findViewById<FloatingActionButton>(R.id.placePicker)
        fabButton.visibility = View.INVISIBLE
    }

    @SuppressLint("RestrictedApi")
    fun showMap(){

        //mMap.uiSettings.setMapToolbarEnabled(true)
        mMap.uiSettings.setMyLocationButtonEnabled(true)

        var fabButton = findViewById<FloatingActionButton>(R.id.placePicker)
        fabButton.visibility = View.VISIBLE
    }

    fun fabOnClick(v : View){
        Log.d(TAG, "Fab Working")

        hideMap()

        if(mMarker != null)
            mMarker!!.remove()


        if(mPolyline != null)
            mPolyline!!.remove()


        var fragM = PlacesOrder()

        fragM.testVal = "This is a very simple test"
        fragM.homepage = this

        var fm = supportFragmentManager.beginTransaction()

        fm.replace(R.id.framelay, fragM).commit()

        placeOrder = fragM


    }

    fun placeOrderCancelButton(v : View){
        supportFragmentManager.beginTransaction().remove(placeOrder).commit()
        showMap()

    }

    fun returnOrder(view : View, order : Order){
        this.orderObj = order
        this.results = placeOrder.result

        placeOrderCancelButton(view)

        var polyList : MutableList<LatLng> = ArrayList<LatLng>()

        for(each in results.routes[0].overviewPolyline.decodePath()){
            polyList.add(LatLng(each.lat, each.lng))
        }

        mPolyline = mMap.addPolyline((PolylineOptions())
                .clickable(true).addAll(polyList).color(Color.BLUE).width(6f))

        mMarker = mMap.addMarker(MarkerOptions().position(order.destination).title(order.destinationName))


        var halfPoint : Int = polyList.size / 2

        moveCamera(polyList[halfPoint], 8f)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            200 -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try{
                        mMap.isMyLocationEnabled = true
                        mMap.uiSettings.isMyLocationButtonEnabled = true
                        moveCamera(getCurrentLocation(), 50f)
                    }
                    catch(e : SecurityException){ Log.d(TAG, e.message)}
                }
            }
        }
    }

    var mHandler : Handler = object : Handler(Looper.getMainLooper()){

        override fun handleMessage(msg : Message){
            placeOrder.setDestinationDetails()
        }
    }
}
