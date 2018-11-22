package com.example.admin.loginguitest

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.sql.ResultSet
import java.util.HashMap
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.ArrayList


class LogIn_GUI : AppCompatActivity() {

    lateinit var loginButton        : Button
    lateinit var emailTextField     : TextView
    lateinit var passwordTextField  : TextView

    private lateinit var fbAuth     : FirebaseAuth
    var dbRef: FirebaseFirestore = FirebaseFirestore.getInstance()
    var collectionReference : CollectionReference = dbRef.collection("Users")

    var dataToSave : MutableMap<String, String> = HashMap<String, String>()

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            //.requestIdToken(getString(R.string.default_web_client_id))
            .requestIdToken("146078760213-01dddiigen9ds5a13it2qbokshd89jer.apps.googleusercontent.com")
            //Client ID WebClient 146078760213-01dddiigen9ds5a13it2qbokshd89jer.apps.googleusercontent.com
            //App 146078760213-v5evi53aeuckik8hnapcp835altrghtu.apps.googleusercontent.com
            .requestEmail()
            .build()

    private val RC_SIGN_IN = 1

    companion object {
        var TAG = "MainActivity"
    }


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in__gui)

        loginButton = findViewById(R.id.LogInButton) as Button
        emailTextField = findViewById(R.id.emailTextField) as TextView
        passwordTextField = findViewById(R.id.PasswordField) as TextView

        fbAuth = FirebaseAuth.getInstance()

        if(fbAuth.currentUser != null){
            fbAuth.signOut()
        }


        if(fbAuth.currentUser != null) Log.d(TAG, "currentUser is " + fbAuth.currentUser!!.email)

        else Log.d(TAG, "currentUser is null")
    }

    fun logInHandler(v : View){

        if(logInFieldsEmpty() == false){
            if(isEmailValid(emailTextField.text.toString()) == true){

                FireBaseSignIn(emailTextField.text.toString(), passwordTextField.text.toString());
            }
            else
                emailTextField.error = "Please enter a valid email"
        }



    }

    private fun FireBaseSignIn(name : String, password : String) {

        fbAuth.signInWithEmailAndPassword(name, password).addOnCompleteListener(this) { task ->

            if (task.isSuccessful) {
                val user = fbAuth.currentUser
                Log.d(TAG, "signInWithEmail:success")
                Toast.makeText(baseContext, "Success.",
                        Toast.LENGTH_SHORT).show()

                Thread.sleep(1000)

                //var intent = Intent(this, MapsActivity :: class.java)

                var intent : Intent = Intent(this, homepage :: class.java)

                startActivity(intent)

            } else {
                Log.w(TAG, "signInWithEmail:failure", task.exception)
                Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

            }


        }
    }

    private fun isEmailValid(email : String) : Boolean{
        var expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        var pattern : Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        var matcher : Matcher = pattern.matcher(email)

        return matcher.matches()
    }

    private fun logInFieldsEmpty() : Boolean{

        if(emailTextField.text.isBlank() || passwordTextField.text.isBlank()) {
            if (emailTextField.text.isBlank() && passwordTextField.text.isBlank())
                emailTextField.setError("Please do not leave Email and Password Fields Empty")
            else if (emailTextField.text.isBlank())
                emailTextField.setError("Please do not leave Email Field Empty")
            else
                passwordTextField.setError("Please do not Password Field Empty")

            clearText()
        }

        return false
    }

    private fun clearText(){

        emailTextField.text = ""
        passwordTextField.text = ""

    }

    fun signUpHandler(v : View){
        var intent : Intent = Intent(this, SignUpController :: class.java)
        startActivity(intent)
    }

    //LOGGING IN THROUGH GOOGLE PLUS --------------------------------------------
    fun signUpViaGoogle(v: View) {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.

        Log.d(TAG, "Signing up Via Google")

        // Build a GoogleSignInClient with the options specified by gso.
        var mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        mGoogleSignInClient.signOut()
        val signInIntent = mGoogleSignInClient.signInIntent


        startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)

        } else {
            Toast.makeText(this, "Problem in execution order :(", Toast.LENGTH_LONG).show()
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)!!

            firebaseAuthWithGoogle(account)

        } catch (e: ApiException) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        fbAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        val user = fbAuth.currentUser

                        collectionReference
                                .whereEqualTo("email", user!!.email)
                                .get()
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        /*
                                        for (document in task.result!!) {
                                            Log.d(TAG, document.id + " => " + document.data)
                                            //uName.text = document.data.getValue("name").toString()
                                        }
                                        */

                                        if(task.result!!.size() < 1){

                                            Log.d("QueryDB", "Task succeeded user doesn't exist")

                                            emailTextField.visibility = View.INVISIBLE
                                            passwordTextField.visibility = View.INVISIBLE
                                            loginButton.visibility = View.INVISIBLE

                                            var signUpRel = findViewById<RelativeLayout>(R.id.signUpRel)

                                            //signUpRel.visibility = View.INVISIBLE

                                            dataToSave.put("name", acct.displayName!!)
                                            dataToSave.put("email", fbAuth.currentUser!!.email!!)
                                            dataToSave.put("balance", "0")

                                            var signInFrag = sign_in_google_frag()
                                            signInFrag.login = this

                                            var fm = supportFragmentManager.beginTransaction()

                                            fm.replace(R.id.mobileOverlay, signInFrag)
                                                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                                                    .commit()

                                        }

                                        else {
                                            Log.d("QueryDB", "Task succeeded user already exists")
                                            var intent = Intent(this, homepage::class.java)
                                            startActivity(intent)
                                        }
                                    } else {
                                        Log.d("QueryDB", "Error getting documents: ", task.exception)
                                    }
                                }

                    }
                    else {

                        Log.w(TAG, "signInWithCredential:failure", task.exception)

                        Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()

                    }

                }
    }

}

