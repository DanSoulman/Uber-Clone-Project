package com.example.admin.loginguitest

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_homepage.*
import kotlinx.android.synthetic.main.app_bar_homepage.*

class homepage : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var emailDisplay : TextView

    lateinit var uName : TextView

    private lateinit var fbAuth : FirebaseAuth

    var dbRef : FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)
        setSupportActionBar(toolbar)

        fbAuth = FirebaseAuth.getInstance()

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        var nView : View = nav_view.getHeaderView(0)

        emailDisplay = nView.findViewById(R.id.emailDisplay)

        emailDisplay.text = fbAuth.currentUser!!.email

        uName = nView.findViewById(R.id.uName)


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
}
