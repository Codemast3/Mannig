package com.example.mannig.activities

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mannig.R
import org.w3c.dom.Text

class Introactivity : baseactivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContentView(R.layout.activity_introactivity)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val typeface: Typeface =
            Typeface.createFromAsset(assets,"blade.ttf")
        val tvappname: TextView = findViewById(R.id.tv_app_name_intro)
        tvappname.typeface=typeface
        val btn: Button = findViewById(R.id.btn_sign_in_intro)
        btn.setOnClickListener{
            startActivity(Intent(this@Introactivity, Signinactivity::class.java))

        }
        val btnn: Button = findViewById(R.id.btn_sign_up_intro)
        btnn.setOnClickListener {
            startActivity(Intent(this@Introactivity, Signup::class.java))
        }

    }
}