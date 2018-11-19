package com.example.admin.loginguitest


import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.example.admin.loginguitest.DataClasses.Order
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.PlaceBuffer
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.model.DirectionsResult
import java.text.DecimalFormat
import java.util.concurrent.atomic.AtomicBoolean


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 *
 */
class PlacesOrder : Fragment(), GoogleApiClient.OnConnectionFailedListener {

    lateinit var testVal : String

    lateinit var homepage : homepage

    lateinit var autoCompleteFragment : AutoCompleteTextView

    lateinit var mPlaceAutocompleteAdapter: PlaceAutocompleteAdapter

    lateinit var mGoogleApiClient : GoogleApiClient

    lateinit var mPlace : Place

    lateinit var destinationView : TextView

    lateinit var distanceView : TextView

    lateinit var costView : TextView

    //-------------------------OrderDetails------------------------------
    lateinit var order : Order

    //Customer Details
    lateinit var name : String
    lateinit var email : String
    lateinit var source : LatLng
    lateinit var sourceAddress : String

    //Trip Details
    lateinit var destinationAddress : String
    lateinit var destination : LatLng
    var distance : Long = 0
    var cost = 0.0

    //-------------------------------------------------------------------

    //---Test Data---

    lateinit var compareVal : String

    lateinit var result : DirectionsResult

    var placeOrder = this

    //---------------

    companion object {
        val TAG = "Order Place Screen"
        val LAT_LNG_BOUNDS = LatLngBounds(LatLng(-40.00, -168.00), LatLng(71.00, 136.00))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_places_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        source = homepage.getCurrentLocation()

        autoCompleteFragment = view.findViewById<AutoCompleteTextView>(R.id.input_search) as AutoCompleteTextView

        mGoogleApiClient = GoogleApiClient.Builder(homepage).addApi(Places.GEO_DATA_API).addApi(Places.PLACE_DETECTION_API).
                enableAutoManage(homepage, this)
                .build()

        mPlaceAutocompleteAdapter = PlaceAutocompleteAdapter(homepage, mGoogleApiClient, LAT_LNG_BOUNDS, null)

        autoCompleteFragment.setAdapter(mPlaceAutocompleteAdapter)

        autoCompleteFragment.setOnItemClickListener(autoCompleteFragmentListener)


        var nameView = view.findViewById<TextView>(R.id.textView11)

        nameView.text = homepage.uName.text

        var locationView = view.findViewById<TextView>(R.id.textView12)

        this.name = nameView.text.toString()

        this.sourceAddress = homepage.getCurrentAddress().mAddress!!

        locationView.text = splitString(sourceAddress)

        var balanceView = view.findViewById<TextView>(R.id.textView13)

        var balance = 13.50

        balanceView.text = "€" + balance.toString()

        destinationView = view.findViewById(R.id.textView17)

        distanceView = view.findViewById(R.id.textView18)

        costView = view.findViewById(R.id.textView19)

        var cancelButton = view.findViewById<Button>(R.id.cancelButton)


        compareVal = destinationView.text.toString()

        cancelButton.setOnClickListener {
            homepage.placeOrderCancelButton(it)
        }

        var confirmButton = view.findViewById<Button>(R.id.submitButton)

        confirmButton.setOnClickListener {
            if(!compareVal.equals(destinationView.text.toString()))
                        homepage.returnOrder(it, order)

            else
                Toast.makeText(homepage,
                        "Please Enter a Destination to confirm or cancel",
                        Toast.LENGTH_SHORT).show()

        }

    }

    fun splitString(mAddress : String) : String{
        return mAddress.split(',')[0]
    }
    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

   fun hideKeyboard(activity : Activity) {
       var imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
       var view = activity.getCurrentFocus();

       if (view == null) {
           view = View(activity);
       }

       imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /* -----------------Google AutoComplete API--------------- */

    private var autoCompleteFragmentListener : AdapterView.OnItemClickListener =
            object : AdapterView.OnItemClickListener {

                override fun onItemClick(adapterView : AdapterView<*>, view1: View, i: Int, l: Long) {

                    hideKeyboard(homepage)

                    var item = mPlaceAutocompleteAdapter.getItem(i)
                    var placeID = item.placeId

                    var placeResult: PendingResult<PlaceBuffer> = Places.
                            GeoDataApi.getPlaceById(mGoogleApiClient, placeID)

                    placeResult.setResultCallback(mUpdatePlaceDetailsCallback)

                }

            }

    private val mUpdatePlaceDetailsCallback = object :  ResultCallback<PlaceBuffer> {
        override fun onResult(places : PlaceBuffer) {
            if (!places.getStatus().isSuccess())
            {
                Log.d(TAG, "onResult: Place query did not complete successfully: " + places.getStatus().toString())
                places.release()
                return
            }

            val place = places.get(0)

            mPlace = place

            var tempLatLng = mPlace.latLng

            homepage.calculateDirections(tempLatLng, placeOrder)
            
            //These are set here as the mPlace object will be released at the
            //end of the this block of code
            destinationView.text = splitString(mPlace.address.toString())

            destination = mPlace.latLng

            places.release()

        }
    }

    fun setDestinationDetails(){

        distance = result.routes[0].legs[0].distance.inMeters

        cost = (distance / 1000) * 0.8

        val df = DecimalFormat("#.##")

        distanceView.text = (distance / 1000).toString() + "km"

        costView.text = "€" + df.format(cost).toString()

        destinationAddress = destinationView.text.toString()

        order = Order(name, homepage.emailDisplay.text.toString(),
                source, destination, destinationAddress,
                sourceAddress, cost, distance)


    }
    /* ------------------------------------------------------- */

    override fun onDestroyView() {
        super.onDestroyView()

        mGoogleApiClient.stopAutoManage(homepage)
        mGoogleApiClient.disconnect()
    }
}
