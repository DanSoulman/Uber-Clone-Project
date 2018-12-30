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
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.R.id.text

import com.stripe.android.Stripe
import com.stripe.android.TokenCallback
import com.stripe.android.model.Card
import com.stripe.android.model.Token
import com.stripe.android.view.CardInputWidget
import com.stripe.model.Charge
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

    private var functions: FirebaseFunctions = FirebaseFunctions.getInstance()

    /*-------------------------------------*/
    /*-----------Stripe Objects-----------*/

    lateinit var mCardInputWidget : CardInputWidget
    var mCard : Card? = null
    var mStripe : Stripe? = null

    /*--------------------------------------*/

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

        mCardInputWidget = view.findViewById(R.id.card_input_widget)

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

        var topUpButton = view.findViewById<Button>(R.id.payment_button)

        topUpButton.setOnClickListener(){
            ConfirmButton()
        }

    }

    fun ConfirmButton(){

        Log.d(TAG, "Confirm button, pressed")
        mCard = mCardInputWidget.card

        if(mCard == null) {
            Toast.makeText(homepage, "Invalid Card Data", Toast.LENGTH_SHORT).show()
            return
        }

        if(!mCard!!.validateCard()){
            Toast.makeText(homepage, "Invalid Card Data", Toast.LENGTH_SHORT).show()
            return
        }

        mCard!!.name = billingName.text.toString()
        mCard!!.addressLine1 = billingAddress.text.toString()

        createStripeToken()

    }

    fun createStripeToken(){

        mStripe = Stripe(homepage, getString(R.string.stripe_token_key))
        mStripe!!.createToken(mCard!!, object : TokenCallback {

                    override fun onSuccess(token : Token) {
                        //sendToken(token)
                        // Send token to your server
                        localCharge(token)

                    }

                    override fun onError(error : Exception) {
                        // Show localized error message
                        Toast.makeText(homepage, "Error creating the token", Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Error creating the token")


                    }
                }
        )

    }

    private fun localCharge(token: Token) {
        var cost = topUpValue.text.toString().toDouble()
        var amount : Int = (cost * 100).toInt()
        var chargeDetails : MutableMap<String, Any> = HashMap<String, Any>()
        chargeDetails.put("amount", amount)
        chargeDetails.put("currency", "eur")
        chargeDetails.put("description", "TransportAI Account TopUp")
        //chargeDetails.put("source", token)
        chargeDetails.put("card", token.id);

        var runnable = Runnable {
            com.stripe.Stripe.apiKey = getString(R.string.stripe_private_key)
            var charge = Charge.create(chargeDetails)

            dbRef.collection("Users")
                    .whereEqualTo("email", fbAuth.currentUser!!.email)
                    .get().addOnCompleteListener {
                if(it.isSuccessful){
                    var document = it.result!!.documents[0].id

                    var resultVal = it.result!!.documents[0].data!!.getValue("balance")

                    var balance = 0.0
                    if(resultVal is String)
                        balance = resultVal.toDouble()

                    else
                        balance = resultVal as Double

                    balance += cost

                    var dataToSave = hashMapOf<String, Any>(
                            "balance" to balance
                    )

                    dbRef.collection("Users").document(document).update(dataToSave)

                    homepage.runOnUiThread(){
                        clearText()
                        currentBalanceView.text = "€" + balance.toString()
                        accountBalanceAfterView.text = "€" + "0.0"

                    }
                    Toast.makeText(homepage, "Charge, successfully applied", Toast.LENGTH_SHORT).show()

                }
            }
        }

        Thread(runnable).start()
    }

    private fun sendToken(token: Token) : Task<Toast> {
        //var data : MutableMap<String, Any> = HashMap<String, Any>()

        var cost : Double = topUpValue.text.toString().toDouble()
        var amount : Int = (cost * 100).toInt()

        //data.put("card", token.id);
        //data.put("amount", amount)

        val data = hashMapOf(
                "card" to token.id,
                "amount" to amount
        )

        return functions.getHttpsCallable("stripeCharge").call(data).continueWith {

            Log.d(TAG, "Function called")
            if(it.isSuccessful) {
                Log.d(TAG, "Success")
                Toast.makeText(homepage, "Charge, successfully applied", Toast.LENGTH_SHORT)
            }
            else
                Toast.makeText(homepage, "Charge, failed", Toast.LENGTH_SHORT)

        }

    }

    fun hideKeyboard(activity : Activity) {
        var imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.getCurrentFocus();

        if (view == null) {
            view = View(activity);
        }

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    fun clearText(){
        billingName.text.clear()
        billingAddress.text.clear()
        topUpValue.text.clear()
        mCardInputWidget.clear()
    }
}
