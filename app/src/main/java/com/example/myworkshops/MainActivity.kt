package com.example.myworkshops

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import com.example.myworkshops.Constants.Constants.loggedin
import com.example.myworkshops.Constants.Constants.name
import com.example.myworkshops.Constants.Constants.sharedPreferencesName
import com.example.myworkshops.Constants.Constants.username
import com.example.myworkshops.database.Database
import com.example.myworkshops.database.Workshop
import com.example.myworkshops.fragments.AvailableWorkshops
import com.example.myworkshops.fragments.Contacts
import com.example.myworkshops.fragments.Dashboard
import com.example.myworkshops.fragments.Profile
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    var previousMenuItem: MenuItem? = null

    lateinit var navView: NavigationView
    lateinit var sharedPreferences: SharedPreferences
    lateinit var drawerLayout: DrawerLayout
    lateinit var txtProfileName: TextView
    lateinit var imgNavHeadProfile: ImageView
    lateinit var txtLogout: MenuItem


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        sharedPreferences = this.getSharedPreferences(sharedPreferencesName, MODE_PRIVATE)

        username = sharedPreferences.getString(loggedin, "")!!
        name = sharedPreferences.getString(username, "")!!

        setTransparentStatusBar()

        drawerLayout = findViewById(R.id.drawerLayout)
        navView  = findViewById(R.id.navView)
        toolbar = findViewById(R.id.toolbar)
        val headerView = navView.getHeaderView(0)
        txtProfileName = headerView.findViewById(R.id.txt_profile_name)
        imgNavHeadProfile = headerView.findViewById(R.id.nav_profile_image)
        val menu = navView.menu
        txtLogout = menu[4]
        Picasso.get().load(R.drawable.profile_placeholder).error(R.drawable.workshop).into(imgNavHeadProfile)

        if(username!=""){
            txtProfileName.setText(name)
            openDashboard()
            txtLogout.title = "Logout"

        }else{
            CoroutineScope(Dispatchers.IO
            ).launch {
                setWorkshops(applicationContext)
                CoroutineScope(Dispatchers.Main).launch{
                    txtProfileName.setText("Not Logged in")
                    openAvailWorkshops()
                    txtLogout.title = "Sign in"
                }
            }

        }


        setUpToolbar()
        toggle = ActionBarDrawerToggle(this, drawerLayout , R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener {

            if(previousMenuItem != null){
                previousMenuItem?.isChecked = false
            }

            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it

            when (it.itemId) {
                R.id.dashboard -> {
                    if(sharedPreferences.getString(loggedin, "") == ""){
                        val dialog =  AlertDialog.Builder(this).apply {
                            setTitle("Access Dashboard")
                            setMessage("You need to log in to access Dashboard. would you like to login?")
                            setPositiveButton("Yes"){text, listner->
                                val intentToLoginActivity = Intent(context, LoginActivity::class.java)
                                ContextCompat.startActivity(context, intentToLoginActivity, null)
                                finish()
                            }
                            setNegativeButton("No"){text, listner->
                                //do nothing
                            }
                        }
                        dialog.create()
                        dialog.show()
                    }else{
                        drawerLayout.closeDrawers()
                        Handler().postDelayed({
                            openDashboard()
                        }, 200)
                    }

                }

                R.id.availableWorkshops->{
                    drawerLayout.closeDrawers()
                    Handler().postDelayed({
                        openAvailWorkshops()
                    },200)

                }

                R.id.contacts->{
                    drawerLayout.closeDrawers()
                    Handler().postDelayed({
                        openContacts()
                    },200)

                }

                R.id.menu_logout->{

                    if(sharedPreferences.getString(loggedin, "") != ""){
                        val dialog =  AlertDialog.Builder(this).apply {
                            setTitle("Logout")
                            setMessage("are you sure you want to logout?")
                            setPositiveButton("Yes"){text, listner->
                                val intentToLoginActivity = Intent(context, LoginActivity::class.java)
                                username = ""
                                sharedPreferences.edit().remove(loggedin).apply()
                                ContextCompat.startActivity(context, intentToLoginActivity, null)
                                finish()
                            }
                            setNegativeButton("No"){text, listner->
                                //do nothing
                            }
                        }
                        dialog.create()
                        dialog.show()
                    }else{
                        drawerLayout.closeDrawers()

                        val intentToLoginActivity = Intent(this, LoginActivity::class.java)
                        username = ""
                        sharedPreferences.edit().remove(loggedin).apply()

                        Handler().postDelayed({
                            ContextCompat.startActivity(this, intentToLoginActivity, null)
                        }, 200)
                    }
                }

                R.id.profile ->{
                    if(sharedPreferences.getString(loggedin, "") == ""){
                        val dialog =  AlertDialog.Builder(this).apply {
                            setTitle("Access Dashboard")
                            setMessage("You need to log in to access Profile. would you like to login?")
                            setPositiveButton("Yes"){text, listner->
                                val intentToLoginActivity = Intent(context, LoginActivity::class.java)
                                Handler().postDelayed({
                                    ContextCompat.startActivity(context, intentToLoginActivity, null)
                                    finish()
                                },200)

                            }
                            setNegativeButton("No"){text, listner->
                                //do nothing
                            }
                        }
                        dialog.create()
                        dialog.show()
                    }else{
                        drawerLayout.closeDrawers()
                        Handler().postDelayed({
                            openProfile()
                        },200)
                    }
                }
            }
            true
        }

    }

    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Workshops"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun Activity.setTransparentStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    fun openDashboard(){

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, Dashboard())
            .commit()
        navView.setCheckedItem(R.id.dashboard)
        supportActionBar?.title = "Dashboard"
    }

    fun openAvailWorkshops(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, AvailableWorkshops())
            .commit()
        navView.setCheckedItem(R.id.availableWorkshops)
        supportActionBar?.title = "Available Workshops"
    }

    fun openContacts(){
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.anim_fade_in,R.anim.anim_fade_out)
            .replace(R.id.frame, Contacts())
            .commit()
        navView.setCheckedItem(R.id.contacts)
        supportActionBar?.title = "Contacts"
    }

    fun openProfile(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, Profile())
            .commit()
        navView.setCheckedItem(R.id.profile)
        supportActionBar?.title = "Profile"
    }




    suspend fun setWorkshops(context: Context){
        val dao = Database.getDatabase(context, userid = username).dao()
        dao.addWorkshop(Workshop("0","ROS Workshop","Robot operating system is gaining huge popularity. Don't remain behind register now!","1499 Rs", false,R.drawable.ros))
        dao.addWorkshop(Workshop("1","Android Development","Woulden't you like to make you own android apps!! Don't miss this limited offer!","1999 Rs", false,R.drawable.android))
        dao.addWorkshop(Workshop("2","Web Development","Every business needs it! design and develop your website!!","2499 Rs", false, R.drawable.web))
        dao.addWorkshop(Workshop("3","Artificial Intellegence","AI is gaining huge popularity given its revolutionary power! don't miss out on it!","3999 Rs", false,R.drawable.ai))
        dao.addWorkshop(Workshop("4","AWS Workshop","Everything needs computation power and energy, world is switchng towards cloud, when are you?","3999 Rs", false,R.drawable.aws))
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frame)
        when (frag) {
            !is Dashboard ->{
                if(username!=""){
                    openDashboard()
                }else{
                    val dialog =  AlertDialog.Builder(this).apply {
                        setTitle("Access Dashboard")
                        setMessage("You need to log in to access Dashboard. would you like to login?")
                        setPositiveButton("Yes"){text, listner->
                            val intentToLoginActivity = Intent(context, LoginActivity::class.java)
                            ContextCompat.startActivity(context, intentToLoginActivity, null)
                            finish()
                        }
                        setNegativeButton("No"){text, listner->
                            super.onBackPressed()
                        }
                    }
                    dialog.create()
                    dialog.show()
                }

            }
            else ->
                super.onBackPressed()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

}