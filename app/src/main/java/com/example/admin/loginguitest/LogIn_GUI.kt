package com.example.admin.loginguitest

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import java.sql.ResultSet
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.ArrayList


class LogIn_GUI : AppCompatActivity() {

    lateinit var loginButton        : Button
    lateinit var emailTextField     : TextView
    lateinit var passwordTextField  : TextView

    private lateinit var fbAuth     : FirebaseAuth


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
}

