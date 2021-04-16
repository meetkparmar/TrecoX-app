package com.bebetterprogrammer.trecox.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bebetterprogrammer.trecox.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        FirebaseAuth.getInstance().currentUser?.let {
            Log.i(
                "SplashActivity",
                "User is already logged in with name - ${FirebaseAuth.getInstance().currentUser?.displayName}!"
            )
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }
        btn_next.setOnClickListener {
            val mobileNumber = "+91" + et_mobile_number.text.toString()
            if (mobileNumber.trim().isNotEmpty() && mobileNumber.length == 13) {
                val intent = Intent(this, OtpVerificationActivity::class.java)
                    .putExtra("mobileNumber", mobileNumber)
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext, "Enter valid Mobile number", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}
