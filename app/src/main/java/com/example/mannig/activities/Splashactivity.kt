package com.example.mannig.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.WindowManager.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mannig.R
import com.example.mannig.R.id.main
import com.example.mannig.firestore.firestoreclass


class Splashactivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splashactivity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        Handler().postDelayed({
            var curretuserid=firestoreclass().getCurrentuserid()
            if(curretuserid.isNotEmpty()){
                startActivity(Intent(this, MainActivity::class.java))




            }else{
                startActivity(Intent(this, Introactivity::class.java))

            }

            finish()
        },2500)




    }

}