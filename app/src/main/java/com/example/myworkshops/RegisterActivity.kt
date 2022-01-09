package com.example.myworkshops

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.myworkshops.Constants.Constants
import com.example.myworkshops.Constants.Constants.loggedin
import com.example.myworkshops.Constants.Constants.sharedPreferencesName
import com.example.myworkshops.Constants.Constants.username
import com.example.myworkshops.database.Database
import com.example.myworkshops.database.Workshop
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    lateinit var inpName: TextInputEditText
    lateinit var inpUsername: TextInputEditText
    lateinit var inpPassword: TextInputEditText
    lateinit var inpConfPassword: TextInputEditText
    lateinit var btnRegister: Button
    lateinit var txtBackToLogin: TextView
    lateinit var sharedPreferences: SharedPreferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        sharedPreferences = this.getSharedPreferences(sharedPreferencesName, MODE_PRIVATE)

        inpName = findViewById(R.id.inp_name)
        inpUsername = findViewById(R.id.inp_username)
        inpPassword = findViewById(R.id.inp_password)
        inpConfPassword = findViewById(R.id.inp_conf_pass)
        btnRegister = findViewById(R.id.btn_register)
        txtBackToLogin = findViewById(R.id.txt_back_to_login)

        if(intent.getStringExtra("username") != null){
            inpUsername.setText(intent.getStringExtra("username"))
        }
        if(intent.getStringExtra("password") != null){
            inpPassword.setText(intent.getStringExtra("password"))
        }

        btnRegister.setOnClickListener {

            if(inpUsername.length() < 3 ||
                    inpName.length() <2||
                    inpConfPassword.length() < 3||
                    inpPassword.length()<3){

                Toast.makeText(this, "Please enter details correctly.", Toast.LENGTH_SHORT).show()

            }else if (inpPassword.text.toString() != inpConfPassword.text.toString()){

                Toast.makeText(this,"Wrong confirmed Password!", Toast.LENGTH_SHORT).show()

            }else if(sharedPreferences.getString(inpUsername.text.toString(), "") == ""){

                sharedPreferences.edit().putString(inpUsername.text.toString()+"1", inpPassword.text.toString()) //username1 : password
                    .putString(inpUsername.text.toString(), inpName.text.toString())                             // username : name
                    .putString(loggedin, inpUsername.text.toString())
                    .apply()
                username = inpUsername.text.toString()


                CoroutineScope(Dispatchers.IO).launch {
                    setWorkshops(applicationContext)
                    val dao = Database.getDatabase(applicationContext, inpUsername.text.toString()).dao()
                }

                Toast.makeText(this, " Your account registered successfully !", Toast.LENGTH_SHORT).show()

                val intentToMainActivity = Intent(this, MainActivity::class.java).apply {
                    putExtra("username", inpUsername.text.toString())
                }
                Handler().postDelayed({
                    startActivity(intentToMainActivity)
                },200)

            }else{
                Toast.makeText(this, "Username already registered! please enter a different one" , Toast.LENGTH_SHORT).show()
            }
        }

        txtBackToLogin.setOnClickListener {
            val intentToMainActivity = Intent(this, LoginActivity::class.java)
            inpUsername.setText("")
            inpPassword.setText("")
            inpConfPassword.setText("")
            inpName.setText("")
            startActivity(intentToMainActivity)
        }
    }

    suspend fun setWorkshops(context: Context){
        val dao = Database.getDatabase(context, username).dao()
        dao.addWorkshop(Workshop("0","ROS Workshop","Robot operating system is gaining huge popularity. Don't remain behind register now!","1499 Rs", false,R.drawable.ros))
        dao.addWorkshop(Workshop("1","Android Development","Woulden't you like to make you own android apps!! Don't miss this limited offer!","1999 Rs", false,R.drawable.android))
        dao.addWorkshop(Workshop("2","Web Development","Every business needs it! design and develop your website!!","2499 Rs", false, R.drawable.web))
        dao.addWorkshop(Workshop("3","Artificial Intellegence","AI is gaining huge popularity given its revolutionary power! don't miss out on it!","3999 Rs", false,R.drawable.ai))
        dao.addWorkshop(Workshop("4","AWS Workshop","Everything needs computation power and energy, world is switchng towards cloud, when are you?","3999 Rs", false,R.drawable.aws))

    }
}