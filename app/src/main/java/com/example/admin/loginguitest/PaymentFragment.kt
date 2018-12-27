package com.example.admin.loginguitest


import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_payment.*

class PaymentFragment : Fragment() {

    lateinit var homepage: homepage

    /*--------Text Views---------*/
    lateinit var accountNameView : TextView

    lateinit var currentBalanceView : TextView

    lateinit var accountBalanceAfterView : TextView

    /*------Edit Texts-----------*/

    lateinit var billingName : EditText

    lateinit var billingAddress : EditText

    lateinit var topUpValue : EditText

    /*------AccountValues-------*/
    var name : String? = null

    var balance : String? = null

    /*-------FireBaseObjects---------------*/
    var fbAuth = FirebaseAuth.getInstance()

    var dbRef = FirebaseFirestore.getInstance()

    var collectionReference : CollectionReference = dbRef.collection("Users")

    companion object {
        val TAG = "PaymentFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        collectionReference
                .whereEqualTo("email", fbAuth.currentUser!!.email)
                .get()
                .addOnSuccessListener {
                    for (document in it.documents) {
                        Log.d(TAG, document.id + " => " + document.data)
                        name = document.data!!.getValue("name").toString()
                        balance = document.data!!.getValue("balance").toString()

                        updateName()
                    }
                }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }

    fun updateName(){
        if(name != null && balance != null){
            accountName.text = name
            currentBalanceView.text = "€" + balance
        }
    }

    fun updateTopUpValue(){

        var bal         : Double = balance!!.toDouble()
        var topUpValue  : Double = topUpValue.text.toString().toDouble()

        var afterVal    : Double = bal + topUpValue

        accountBalanceAfterView.text = "€" + afterVal

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountNameView = view.findViewById(R.id.accountName)
        currentBalanceView = view.findViewById(R.id.accountBalance)
        accountBalanceAfterView = view.findViewById(R.id.accountBalanceAfter)

        billingName = view.findViewById(R.id.textView5)
        billingAddress = view.findViewById(R.id.textView6)
        topUpValue = view.findViewById(R.id.textView7)

        topUpValue.setOnFocusChangeListener(){ view: View, hasFocus: Boolean ->

            if(!hasFocus){
                updateTopUpValue()
            }
        }

        topUpValue.setOnKeyListener(object: View.OnKeyListener {
            override fun onKey(view:View, keyCode:Int, keyevent: KeyEvent):Boolean {
                if ((keyevent.getAction() === KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    updateTopUpValue()
                    hideKeyboard(homepage)

                    return true
                }
                return false
            }
        })


    }

    fun hideKeyboard(activity : Activity) {
        var imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.getCurrentFocus();

        if (view == null) {
            view = View(activity);
        }

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
