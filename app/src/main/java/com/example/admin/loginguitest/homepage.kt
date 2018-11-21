package com.example.admin.loginguitest

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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

class homepage : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    lateinit var emailDisplay : TextView

    lateinit var uName : TextView

    private lateinit var fbAuth : FirebaseAuth

    var dbRef : FirebaseFirestore = FirebaseFirestore.getInstance()

    var collectionReference : CollectionReference = dbRef.collection("Users")

    lateinit var frag : Fragment

    private lateinit var mMap: GoogleMap
    private lateinit var currentLocationRetrieve : FusedLocationProviderClient

    val TAG = "MapsActivity"

    lateinit var location: LatLng

    companion object {
        var TAG = "HOMEPAGE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)
        //setSupportActionBar(toolbar)

        fbAuth = FirebaseAuth.getInstance()

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        var nView : View = nav_view.getHeaderView(0)

        emailDisplay = nView.findViewById(R.id.emailDisplay)

        emailDisplay.text = fbAuth.currentUser!!.email

        uName = nView.findViewById(R.id.uName)

        addingNames()

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }


    private fun addingNames(){

        emailDisplay.text = fbAuth.currentUser!!.email

        collectionReference
                .whereEqualTo("email", fbAuth.currentUser!!.email)
                .get()
                .addOnSuccessListener (){ }

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
        /*
        when (item.itemId) {
            //R.id.action_settings -> return true
            else ->
            return super.onOptionsItemSelected(item)
        }
        */
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

        location = LatLng(51.8860, 8.5336)


        try{
            currentLocationRetrieve.lastLocation.addOnSuccessListener {
                it?.let {
                    location = LatLng(it.latitude, it.longitude)
                }

                moveCamera(location, 0f)

                if(it == null){
                    Log.d(TAG, "Location value is null")
                }
            }}

        catch(e : SecurityException){
            Log.d(TAG, "Couldn't pull Co-Ordinates")
        }


        return location
    }

    fun moveCamera(location : LatLng, zoom : Float){

        mMap.addMarker(MarkerOptions().position(location).title("Your current location"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoom))

    }

    private fun checkPermission() : Boolean{
        if (ContextCompat.checkSelfPermission(this as AppCompatActivity, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            return true
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
}
