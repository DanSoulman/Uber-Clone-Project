package com.example.admin.loginguitest

class Controller(logIn_GUI: LogIn_GUI) {

    lateinit var loginGUI : LogIn_GUI
    lateinit var dbCon : DataBase_Con
    lateinit var loginService : LogIn_Service
    lateinit var contClass : Controller

    init{
        loginGUI = logIn_GUI

    }
}