package com.example.admin.loginguitest


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class sign_in_google_frag : Fragment() {

    lateinit var login : LogIn_GUI
    lateinit var mobileNum : EditText
    lateinit var mobileButton : Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_in_google, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mobileNum = view.findViewById(R.id.mobileEditText)
        mobileButton = view.findViewById(R.id.mobileButton)
        mobileButton.setOnClickListener {

            if(mobileNum.text.isEmpty()){
                Toast.makeText(login, "Please do not leave the mobile field empty", Toast.LENGTH_SHORT)
            }
            else{
                login.dataToSave.put("phone", mobileNum.text.toString())

                login.collectionReference.add(login.dataToSave as Map<String, String>)
                        .addOnSuccessListener{
                            Log.d("QueryDB", "Task Succeeded inserted user into collection")
                            var intent = Intent(login, homepage :: class.java)

                            startActivity(intent)
                        }
            }
        }


    }
}
