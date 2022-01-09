package com.example.myworkshops

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.myworkshops.Constants.Constants.loggedin
import com.example.myworkshops.Constants.Constants.sharedPreferencesName
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import org.w3c.dom.Text

class LoginActivity : AppCompatActivity() {

    lateinit var btnLogin: Button
    lateinit var inpUsername: TextInputEditText
    lateinit var inpPassword: TextInputEditText
    lateinit var txtToRegister: TextView
    lateinit var txtContWithoutLogin: TextView
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = this.getSharedPreferences(sharedPreferencesName, MODE_PRIVATE)

        btnLogin = findViewById(R.id.btn_signin)
        inpPassword = findViewById(R.id.inp_password_)
        inpUsername = findViewById(R.id.inp_username_)
        txtToRegister = findViewById(R.id.txt_regester_here)
        txtContWithoutLogin = findViewById(R.id.txt_cont_without_login)

        btnLogin.setOnClickListener {
            if(inpPassword.length() < 3 || inpUsername.length() < 3){
                Toast.makeText(this, "Please enter details correctly..", Toast.LENGTH_SHORT).show()
            }else{
                if(varify(inpUsername.text.toString(), inpPassword.text.toString())){
                    val intentToMainActivity = Intent(this, MainActivity::class.java).apply {
                        putExtra("username", inpUsername.text.toString())
                    }
                    sharedPreferences.edit().putString(loggedin, inpUsername.text.toString()).apply()
                    startActivity(intentToMainActivity)
                    finish()
                }else{
                    Toast.makeText(this, "Please enter correct Credentials!", Toast.LENGTH_SHORT).show()
                    inpPassword.setText("")
                }
            }
        }


        txtToRegister.setOnClickListener {
            val intentToRegister = Intent(this, RegisterActivity::class.java).apply {
                putExtra("username", inpUsername.text.toString())
                putExtra("password", inpPassword.text.toString())
            }
            startActivity(intentToRegister)
        }

        txtContWithoutLogin.setOnClickListener {
            val intentToMainActivity = Intent(this, MainActivity::class.java)
            startActivity(intentToMainActivity)
            finish()
        }
    }


    private fun varify(username: String, password: String):Boolean{
        return sharedPreferences.getString(username+"1", "") == password //username1 is key for password // username is key for name //stored in shared pref.
    }
}