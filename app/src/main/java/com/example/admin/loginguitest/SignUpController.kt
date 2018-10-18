package com.example.admin.loginguitest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class SignUpController : AppCompatActivity() {

    lateinit var nameTextField  : TextView
    lateinit var emailTextField : TextView
    lateinit var passwordTextField : TextView
    lateinit var passwordTextField1 : TextView
    lateinit var Submit : Button
    lateinit var Facebook : ImageButton
    lateinit var Twitter : ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_controller)
    }

    private fun signUpFieldsEmpty() : Boolean{

        if(nameTextField.text.isBlank() || emailTextField.text.isBlank() || passwordTextField.text.isBlank() ||  passwordTextField1.text.isBlank()) {
            Toast.makeText(baseContext, "Please do not leave ANY field Empty.", Toast.LENGTH_SHORT).show()
            clearText()
        }
}

    private fun clearText(){

        nameTextField.text = ""
        emailTextField.text = ""
        passwordTextField.text = ""
        passwordTextField1.text = ""
    }