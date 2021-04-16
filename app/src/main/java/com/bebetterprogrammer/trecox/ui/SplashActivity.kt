package com.bebetterprogrammer.trecox.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import com.bebetterprogrammer.trecox.BuildConfig
import com.bebetterprogrammer.trecox.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val versionName = BuildConfig.VERSION_NAME
        appBottomLine.text = "Designed @ bebetterprogrammer.com | v$versionName"

        Handler().postDelayed(
            {
                val i = Intent(this, OnBoardingActivity::class.java)
                startActivity(i)
                finish()
            }, 2000)
    }
}