package com.example.admin.loginguitest


import android.app.Activity
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import com.example.admin.loginguitest.DataClasses.AddressDetails
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.GeoDataApi
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.PlaceBuffer
import com.google.android.gms.location.places.Places
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.android.synthetic.main.activity_order_confirm.*
import kotlinx.android.synthetic.main.fragment_places_order.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import org.w3c.dom.Text
import java.text.DecimalFormat


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 *
 */
class PlacesOrder : Fragment(), GoogleApiClient.OnConnectionFailedListener {

    lateinit var testVal : String

    lateinit var homepage : homepage

    lateinit var location : LatLng

    lateinit var autoCompleteFragment : AutoCompleteTextView

    lateinit var mPlaceAutocompleteAdapter: PlaceAutocompleteAdapter

    lateinit var mGoogleApiClient : GoogleApiClient

    lateinit var mPlace : Place

    lateinit var destinationView : TextView

    lateinit var distanceView : TextView

    lateinit var costView : TextView

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

        location = homepage.getCurrentLocation()

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

        var cLocation = homepage.getCurrentAddress()

        locationView.text = splitString(cLocation.mAddress)

        var balanceView = view.findViewById<TextView>(R.id.textView13)

        balanceView.text = "€13.50"

        destinationView = view.findViewById(R.id.textView17)
        distanceView = view.findViewById(R.id.textView18)
        costView = view.findViewById(R.id.textView19)

        var cancelButton = view.findViewById<Button>(R.id.cancelButton)

        cancelButton.setOnClickListener {
            homepage.placeOrderCancelButton(it)
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

            destinationView.text = splitString(mPlace.address.toString())

            var distanceVal = CalculationByDistance(location, mPlace.latLng)
            var costVal     = distanceVal * 0.10
            val df = DecimalFormat("#.##")

            distanceView.text = df.format(distanceVal).toString() + "km"
            costView.text = "€" + df.format(costVal).toString()

            places.release()

        }
    }
    /* ------------------------------------------------------- */

    fun CalculationByDistance(StartP: LatLng, EndP: LatLng): Double {

        val Radius = 6371// radius of earth in Km
        val lat1 = StartP.latitude
        val lat2 = EndP.latitude
        val lon1 = StartP.longitude
        val lon2 = EndP.longitude
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + (Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2))
        val c = 2 * Math.asin(Math.sqrt(a))
        val valueResult = Radius * c
        val km = valueResult / 1
        val newFormat = DecimalFormat("####")
        val kmInDec = Integer.valueOf(newFormat.format(km))
        val meter = valueResult % 1000
        val meterInDec = Integer.valueOf(newFormat.format(meter))
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec)

        return Radius * c
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mGoogleApiClient.stopAutoManage(homepage)
        mGoogleApiClient.disconnect()
    }
}
