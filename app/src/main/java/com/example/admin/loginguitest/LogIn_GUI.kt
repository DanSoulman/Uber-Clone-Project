package com.example.admin.loginguitest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.admin.loginguitest.R.id.emailTextField
import java.sql.ResultSet
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.ArrayList


class LogIn_GUI : AppCompatActivity() {

    lateinit var loginButton : Button
    lateinit var emailTextField : TextView
    lateinit var passwordTextField : TextView
    lateinit var contClass : Controller


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in__gui)

        //var dbCon = Connection_Test()
        //dbCon.start()

        loginButton = findViewById(R.id.LogInButton) as Button
        emailTextField = findViewById(R.id.emailTextField) as TextView
        passwordTextField = findViewById(R.id.PasswordField) as TextView


    }

    fun logInHandler(v : View){

        var results : ResultSet? = null

        if(logInFieldsEmpty() == false){
            if(isEmailValid(emailTextField.text.toString()) == true){

                //Shared_Resource.stmt = "Select * from \"public\".\"users\" where" +
                //        " email like " + emailTextField.text + " and secret like " +
                //        passwordTextField.text

                //sS.start()

                //while(Shared_Resource.finished != true){

                //}

                //results = Shared_Resource.rs


                if(results != null)
                    emailTextField.error = "Please enter valid credentials"
            }
            else
                emailTextField.error = "Please enter a valid email"
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

    fun signUpHandler(){

    }
}
