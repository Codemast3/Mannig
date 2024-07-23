package com.example.mannig.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mannig.R
import com.example.mannig.models.User
import com.google.firebase.auth.FirebaseAuth

class Signinactivity : baseactivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_signinactivity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        auth = FirebaseAuth.getInstance()
        setupactionbar()

        val btn_signin: Button = findViewById(R.id.btn_sign_in)
        btn_signin.setOnClickListener { signInregistereduser() }

    }
    fun signInsuccess(user: User){
        startActivity(Intent
            (this@Signinactivity,MainActivity::class.java))
finish()


    }

    private fun setupactionbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar_sign_in_activity)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24)
        }
        toolbar.setNavigationOnClickListener{onBackPressed()}

    }
    private fun signInregistereduser(){
        val et_email: AppCompatEditText = findViewById(R.id.et_email_sign_in)
        val email: String = et_email.text.toString().trim { it <= ' ' }
        val et_password: AppCompatEditText = findViewById(R.id.et_password_signin)
        val password: String = et_password.text.toString().trim { it <= ' ' }
        if(validateForm(email,password)){


            // Sign-In using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        Log.d("sign in","signinwithemail:succes")
                        val user=auth.currentUser
                        startActivity(Intent
                            (this,com.example.mannig.activities.MainActivity::class.java))

                    } else {
                        Log.d("sign in","signinwithemail:succes")
                        Toast.makeText(

                            this@Signinactivity,
                            task.exception!!.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }

    private fun validateForm(email: String, password: String): Boolean {
        return if (TextUtils.isEmpty(email)) {
            showErrorSnackBar("Please enter email.")
            false
        } else if (TextUtils.isEmpty(password)) {
            showErrorSnackBar("Please enter password.")
            false
        } else {
            true
        }
    }
}

    /**
     * A function to get the user details from the firestore database after authentication.
     */
