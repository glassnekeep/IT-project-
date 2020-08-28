package com.example.it_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout

class SplashActivity : AppCompatActivity() {

    private var imageView: ImageView? = null
    private var animation: Animation? = null
    private var progressBar: ProgressBar? = null
    private var layout: ConstraintLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
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
                    ActivityUtilities.getInstance().invokeNewActivity(this@SplashActivity, MainActivity::class.java, true)
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
        }, SPLASH_DURATION.toLong())
    }

    override fun onResume() {
        super.onResume()
        initFunctionality()
    }
}