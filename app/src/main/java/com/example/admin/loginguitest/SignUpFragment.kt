package com.example.admin.loginguitest


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.HashMap

class SignUpFragment : Fragment() {

    lateinit var nameTextField: EditText
    lateinit var emailTextField: EditText
    lateinit var passwordTextField: EditText
    lateinit var passwordTextField1: EditText
    lateinit var mobileNumberField : EditText

    lateinit var submit: Button
    lateinit var facebook: Button

    lateinit var name: String

    lateinit var number : String

    lateinit var mInstanceOfLogIn_GUI: LogIn_GUI

    lateinit var fb: FirebaseAuth
    var dbRef: FirebaseFirestore = FirebaseFirestore.getInstance()
    var collectionReference : CollectionReference = dbRef.collection("Users")

    companion object {
        var TAG = "SignUpFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fb = FirebaseAuth.getInstance()

        nameTextField = view.findViewById(R.id.nameTextField)
        emailTextField = view.findViewById(R.id.emailTextField)
        passwordTextField = view.findViewById(R.id.passwordTextField)
        passwordTextField1 = view.findViewById(R.id.passwordTextField1)
        mobileNumberField = view.findViewById(R.id.mobile_number_field)

        var button : Button = view.findViewById(R.id.sign_up_button)

        button.setOnClickListener() {
            signUpService()
        }
    }

    fun signUpService() {
        if (signUpFieldsEmpty() == false) {

            var passwordFieldOne: String = passwordTextField.text.toString()
            var passwordFieldTwo: String = passwordTextField1.text.toString()

            if (passwordFieldTwo.equals(passwordFieldOne)) {

                signUpToAuth(emailTextField.text.toString(), passwordTextField.text.toString())
                name = nameTextField.text.toString()
                //TODO: read in numberTextField to number

                clearText()
            } else {
                Toast.makeText(context, "Make sure passwords match",
                        Toast.LENGTH_SHORT).show()


                clearText()

            }

        }

    }

    private fun signUpToAuth(email: String, password: String) {
        fb.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail: success")
                        val user = fb.currentUser
                        Toast.makeText(context, "Authentication Success",
                                Toast.LENGTH_SHORT).show()

                        fireStoreSignUp()
                    }

                    else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", it.exception)
                        Toast.makeText(context, "Authentication Failed",
                                Toast.LENGTH_SHORT).show()

                        try {
                            throw it.exception!!
                        } catch (weakPassword: FirebaseAuthWeakPasswordException) {
                            Log.d(TAG, "onComplete: weak_password")
                            passwordTextField.error = "Do not enter a weak password"


                        }
                        // if user enters wrong password.
                        catch (malformedEmail: FirebaseAuthInvalidCredentialsException) {
                            Log.d(TAG, "onComplete: malformed_email")
                            emailTextField.error = "Do not enter a malformed email"


                        } catch (existEmail: FirebaseAuthUserCollisionException) {
                            Log.d(TAG, "onComplete: exist_email")
                            emailTextField.error = "This email already exists, log in with your account"


                        } catch (e: Exception) {
                            Log.d(TAG, "onComplete: " + e.message)
                        }
                    }
                }
    }

    private fun fireStoreSignUp() {
        var dataToSave: MutableMap<String, Any> = HashMap<String, Any>()

        dataToSave.put("balance", "0")
        dataToSave.put("name", name)
        dataToSave.put("phone", mobileNumberField.text.toString())
        dataToSave.put("email", fb.currentUser!!.email!!)
        dataToSave.put("trips", "0")

        dbRef.collection("Users").add(dataToSave as Map<String, String>)

        mInstanceOfLogIn_GUI.runOnUiThread(){
            mInstanceOfLogIn_GUI.startHomepage()
        }

    }

    private fun signUpFieldsEmpty(): Boolean {
        //TODO: Add ability to check if number field empty
        if (nameTextField.text.isBlank() || emailTextField.text.isBlank() || passwordTextField.text.isBlank() || passwordTextField1.text.isBlank()) {
            Toast.makeText(context, "Please do not leave ANY field Empty.", Toast.LENGTH_SHORT).show()
            clearText()

            return true

        }

        return false
    }

    private fun clearText() {
        nameTextField.text.clear()
        emailTextField.text.clear()
        passwordTextField.text.clear()
        passwordTextField1.text.clear()
    }

}
