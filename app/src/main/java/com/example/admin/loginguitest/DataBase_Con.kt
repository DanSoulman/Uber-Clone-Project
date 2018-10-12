package com.example.admin.loginguitest

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



object DataBase_Con {

    init {
        createConnection()
    }

    private lateinit var conn: Connection

    // private val jdbcUrl = "jdbc:postgresql://" +
    //        "uber-clone-project.cg42s1hst1qg.us-west-2.rds.amazonaws.com:5432/" +
    //       "Uber_Clone_Database/"


    private val jdbcUrl = "jdbc:mysql://uber-clone-sql.cg42s1hst1qg.us-west-2.rds.amazonaws.com:3306/Uber_Clone_Database?useSSL=false"
    private val username = "root"
    private val password = "root1234"

    private lateinit var con: Connection


    var conCreate: Boolean = false

    fun createConnection() {
        try {
            //Class.forName("org.postgresql.Driver")

            //DriverManager.registerDriver(org.postgresql.Driver())

            Class.forName("com.mysql.jdbc.Driver")

            con = DriverManager.getConnection(jdbcUrl, username, password)
            conCreate = true

        } catch (e: SQLException) {

            e.printStackTrace();
        } catch (e: ClassNotFoundException) {

            e.printStackTrace();
        }

        fun selectStatements(sStatement: String): ArrayList<String> {

            if (conCreate == false)
                createConnection()

            var results = ArrayList<String>()

            var stmt: Statement = conn.createStatement()
            var rs: ResultSet = stmt.executeQuery(sStatement)

            var i = 0
            while (i < rs.fetchSize) {
                results.add(rs.getString(i))
                i++
            }

            return results
        }

        fun insertStatement(insState: String) {
            if (conCreate == false)
                createConnection()

            var stmt: Statement = conn.createStatement()
            stmt.executeUpdate(insState)
        }

        fun closeConnection() {
            con.close()
            conCreate = false
        }


    }

    fun coroutineTest(){
        launch(CommonPool){


        }
    }

}