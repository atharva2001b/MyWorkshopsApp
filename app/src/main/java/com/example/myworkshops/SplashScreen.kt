package com.example.myworkshops

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val logo: ImageView = findViewById(R.id.splash_logo)
        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_run_n_fade)
        logo.startAnimation(slideAnimation)

        Handler().postDelayed({
            logo.visibility = View.INVISIBLE
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2500)

    }
}