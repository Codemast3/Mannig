package com.example.mannig.activities

import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mannig.R
import com.example.mannig.firestore.firestoreclass
import com.example.mannig.models.User
import com.google.firebase.auth.FirebaseAuth.*
import com.google.firebase.auth.FirebaseUser


class Signup : baseactivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }


        setupactionbar()
        val btnsignup: Button = findViewById(R.id.btn_sign_up)
        btnsignup.setOnClickListener{
            registeruser()
        }
        }
    fun userRegisterSuccess(){
        Toast.makeText(
            this, " you have successfully registered  ",

                     Toast.LENGTH_LONG
        ).show()
        getInstance().signOut()
        finish()

    }



    private fun setupactionbar() {
        val signupg: Toolbar = findViewById(R.id.toolbar_sign_up_activity)
        setSupportActionBar(signupg)
        val actionBar = supportActionBar
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
           actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24)
        }
        signupg.setNavigationOnClickListener {
            onBackPressed()




        }


    }


        private fun registeruser() {
            val et_name: AppCompatEditText = findViewById(R.id.et_name)
            val name: String = et_name.text.toString().trim { it <= ' ' }
            val et_email: AppCompatEditText = findViewById(R.id.et_email)
            val email: String = et_email.text.toString().trim { it <= ' ' }
            val et_password: AppCompatEditText = findViewById(R.id.et_password)
            val password: String = et_password.text.toString().trim { it <= ' ' }
            if (vaidateform(name, email, password)) {

                getInstance()
                    .createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->

                        if (task.isSuccessful) {
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            val resgisteremail = firebaseUser.email!!
                           val user = User(firebaseUser.uid,name,resgisteremail)
                            firestoreclass().registeruser(this,user)

                        } else {
                            Toast.makeText(
                                this,
                                task.exception!!.message, Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
            }
        }


        private fun vaidateform(name: String, email: String, password: String): Boolean {
            return when {
                TextUtils.isEmpty(name) -> {
                    showErrorSnackBar("please enter a name")
                    false
                }

                TextUtils.isEmpty(email) -> {
                    showErrorSnackBar("please enter an email")
                    false
                }

                TextUtils.isEmpty(password) -> {
                    showErrorSnackBar("please enter a password")
                    false
                }

                else -> {
                    true
                }
            }
        }
    }
