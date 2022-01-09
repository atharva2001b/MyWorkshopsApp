package com.example.myworkshops.fragments

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.myworkshops.Constants.Constants.loggedin
import com.example.myworkshops.Constants.Constants.name
import com.example.myworkshops.Constants.Constants.sharedPreferencesName
import com.example.myworkshops.Constants.Constants.username
import com.example.myworkshops.LoginActivity
import com.example.myworkshops.R


class Profile : Fragment() {


    lateinit var sharedPreferences: SharedPreferences
    lateinit var txtName: TextView
    lateinit var txtUsername: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        sharedPreferences = requireContext().getSharedPreferences(sharedPreferencesName, MODE_PRIVATE)

        txtName = view.findViewById(R.id.txt_name)
        txtUsername = view.findViewById(R.id.txt_username)

        if(sharedPreferences.getString(loggedin, "") == ""){
            val dialog =  AlertDialog.Builder(requireContext()).apply {
                setTitle("Access Profile")
                setMessage("You need to log in to access Profile. would you like to login?")
                setPositiveButton("Yes"){text, listner->
                    val intentToLoginActivity = Intent(context, LoginActivity::class.java)
                    ContextCompat.startActivity(context, intentToLoginActivity, null)
                }
                setNegativeButton("No"){text, listner->
                    txtUsername.setText("")
                    txtName.setText("")
                }
            }
            dialog.create()
            dialog.show()
        }else{
            txtName.setText(name)
            txtUsername.setText(username)
        }



        return view
    }


}