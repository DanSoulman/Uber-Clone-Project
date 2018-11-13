package com.example.admin.loginguitest


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import java.util.HashMap
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.auth.api.signin.GoogleSignInOptions



class SignUpController : AppCompatActivity() {

    lateinit var nameTextField      : TextView
    lateinit var emailTextField     : TextView
    lateinit var passwordTextField  : TextView
    lateinit var passwordTextField1 : TextView

    lateinit var submit     : Button
    lateinit var facebook   : Button
    lateinit var twitter    : Button

    lateinit var name : String

    //TODO: add variable to store number

    lateinit var fb : FirebaseAuth

    companion object {
        var TAG = "SignUpActivity"
        var dbRef : FirebaseFirestore = FirebaseFirestore.getInstance()
    }

    //TODO: Add the Mobile number Info here
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_controller)

        fb = FirebaseAuth.getInstance()

        nameTextField = findViewById(R.id.nameTextField) as TextView
        emailTextField = findViewById(R.id.emailTextField) as TextView
        passwordTextField = findViewById(R.id.passwordTextField) as TextView
        passwordTextField1 = findViewById(R.id.passwordTextField1) as TextView
        //TODO: call it numberTextField
    }

    fun signUpService(v : View){
        if(signUpFieldsEmpty() == false){

            var passwordFieldOne : String = passwordTextField.text.toString()
            var passwordFieldTwo : String = passwordTextField1.text.toString()

            if(passwordFieldTwo.equals(passwordFieldOne)){

                signUpToAuth(emailTextField.text.toString(), passwordTextField.text.toString())
                name = nameTextField.text.toString()
                //TODO: read in numberTextField to number

                clearText()
            }

            else{
                Toast.makeText(baseContext, "Make sure passwords match",
                        Toast.LENGTH_SHORT).show()


                clearText()


            }

        }

    }

    private fun signUpToAuth(email : String, password : String){
        fb.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        Log.d(TAG, "createUserWithEmail: success")
                        val user = fb.currentUser
                        Toast.makeText(baseContext, "Authentication Success",
                                Toast.LENGTH_SHORT).show()

                        fireStoreSignUp()

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication Failed",
                                Toast.LENGTH_SHORT).show()

                        try{
                            throw task.exception!!
                        }
                        catch (weakPassword : FirebaseAuthWeakPasswordException)
                        {
                            Log.d(TAG, "onComplete: weak_password")
                            passwordTextField.error = "Do not enter a weak password"


                        }
                        // if user enters wrong password.
                        catch (malformedEmail : FirebaseAuthInvalidCredentialsException)
                        {
                            Log.d(TAG, "onComplete: malformed_email")
                            emailTextField.error = "Do not enter a malformed email"


                        }
                        catch (existEmail : FirebaseAuthUserCollisionException)
                        {
                            Log.d(TAG, "onComplete: exist_email")
                            emailTextField.error = "This email already exists, log in with your account"


                        }
                        catch (e : Exception)
                        {
                            Log.d(TAG, "onComplete: " + e.message)
                        }
                    }

                    // ...
                }
    }

    private fun fireStoreSignUp(){
        var dataToSave : MutableMap<String, String> = HashMap<String, String>()

        //val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        //var number : String?

        /*
        try {
            number = tm.line1Number

        }
        catch(s : SecurityException){
           number = null

        }
        */

        dataToSave.put("name", name)
        //dataToSave.put("lastname", name[1])
        dataToSave.put("email", fb.currentUser!!.email!!)

        //TODO: dataToSave.put("mobile", number!!) Have a text field called number, pull the phone number

        dbRef.collection("Users").add(dataToSave as Map<String, String>)


    }

    private fun signUpFieldsEmpty(): Boolean {
        //TODO: Add ability to check if number field empty
        if (nameTextField.text.isBlank() || emailTextField.text.isBlank() || passwordTextField.text.isBlank() || passwordTextField1.text.isBlank()) {
            Toast.makeText(baseContext, "Please do not leave ANY field Empty.", Toast.LENGTH_SHORT).show()
            clearText()
            return true

        }

        return false
    }

    fun signUpViaGoogle(v : View){
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.

        Log.d(TAG, "Signing up Via Google")
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        // Build a GoogleSignInClient with the options specified by gso.
        var mGoogleSignInClient = GoogleSignIn.getClient(this, gso)


    }


    private fun clearText() {
        nameTextField.text = ""
        emailTextField.text = ""
        passwordTextField.text = ""
        passwordTextField1.text = ""
    }
}