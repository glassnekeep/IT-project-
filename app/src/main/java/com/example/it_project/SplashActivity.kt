package com.example.it_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null
    private var imageView: ImageView? = null
    private var animation: Animation? = null
    private var progressBar: ProgressBar? = null
    private var layout: ConstraintLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        auth = FirebaseAuth.getInstance()
        layout = findViewById<View>(R.id.splashLayout) as ConstraintLayout
        imageView = findViewById<View>(R.id.ivSplashIcon) as ImageView
        animation = AnimationUtils.loadAnimation(baseContext, R.anim.rotate)
        progressBar = findViewById<View>(R.id.progressBar) as ProgressBar
    }

    private fun initFunctionality() {
        layout!!.postDelayed( {
            progressBar!!.visibility = View.GONE
            imageView!!.startAnimation(animation)
            animation!!.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    //ActivityUtilities.getInstance().invokeNewActivity(this@SplashActivity, RegisterActivity::class.java, true)
                    //startActivity(Intent(this@SplashActivity, SignInActivity::class.java))
                    if(currentUser != null) {
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    }
                    else {
                        startActivity(Intent(this@SplashActivity, SignInActivity::class.java))
                    }
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
        }, SPLASH_DURATION.toLong())
    }

    override fun onResume() {
        super.onResume()
        currentUser = auth.currentUser
        initFunctionality()
    }
}