package com.example.admin.loginguitest

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.startActivity
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
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.example.admin.loginguitest.DataClasses.AddressDetails
import com.example.admin.loginguitest.R.id.emailDisplay
import com.example.admin.loginguitest.R.id.uName
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.PlaceDetectionClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_homepage.*
import kotlinx.android.synthetic.main.app_bar_homepage.*
import kotlinx.android.synthetic.main.content_homepage.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import java.util.*

class homepage : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    lateinit var emailDisplay : TextView

    lateinit var uName : TextView

    private lateinit var fbAuth : FirebaseAuth

    var dbRef : FirebaseFirestore = FirebaseFirestore.getInstance()

    var collectionReference : CollectionReference = dbRef.collection("Users")

    lateinit var mDrawerLayout : DrawerLayout

    lateinit var mMap: GoogleMap

    private lateinit var currentLocationRetrieve : FusedLocationProviderClient

    lateinit var mGeoDataClient : Geocoder

    // Construct a PlaceDetectionClient
    lateinit var mPlaceDetectionClient : PlaceDetectionClient

    val TAG = "MapsActivity"

    var location: LatLng? = null

    val PLACE_PICKER_REQUEST = 1

    lateinit var placeOrder : PlacesOrder

    companion object {
        var TAG = "HOMEPAGE"
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
                .addOnCompleteListener( OnCompleteListener<QuerySnapshot> { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            Log.d(TAG, document.id + " => " + document.data)
                            uName.text = document.data.getValue("name").toString()
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.exception)
                    }
                })


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
        /*
        when (item.itemId) {
            //R.id.action_settings -> return true
            else ->
            return super.onOptionsItemSelected(item)
        }
        */

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


            Log.d(TAG, "Permission to pull map location granted")

            return true
        }

        else {
            Log.d(TAG, "Permission for location not granted")

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                var place = PlacePicker.getPlace(data, this)
                var toastMsg = String.format("Place: %s", place.name)

                addMarker(place.latLng, place.name.toString())
                moveCamera(place.latLng, 14f)

                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show()

                var current = LatLng(mMap.myLocation.latitude, mMap.myLocation.longitude)

                var order = Order(uName.text.toString(), emailDisplay.text.toString(),
                        current, place.latLng, place.name.toString())

                var intent : Intent = Intent(this, OrderConfirm :: class.java)

                intent.putExtra("orderObject", order)

                startActivity(intent)

            }
        }
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
}
