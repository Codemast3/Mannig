package com.example.mannig.activities

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mannig.R
import com.google.firebase.auth.FirebaseAuth


import org.w3c.dom.Text

open class baseactivity : AppCompatActivity() {


    private var doublebacktoexitpressedonce = false
    private lateinit var mprogressdialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_baseactivity)

    }

    fun showProgressDialog(text: String) {
        val tvprog: TextView = this.findViewById(R.id.tv_progress_text)
        mprogressdialog = Dialog(this)

        /*Set the screen content from a layout resource.
            The resource will be inflated, adding all top-level views to the screen.*/
        mprogressdialog.setContentView(R.layout.dialog_progress)


        //Start the dialog and display it on screen.
        mprogressdialog.show()
    }

    fun hideprogressdialog() {
        mprogressdialog.dismiss()
    }

    fun getcureentuserid(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }


    fun doublebacktoexit() {
        if (doublebacktoexitpressedonce) {
            super.onBackPressed()
            return
        }
        this.doublebacktoexitpressedonce = true
        Toast.makeText(
            this,
            resources.getString(R.string.please_click_back_again_to_exit),
            Toast.LENGTH_SHORT
        ).show()
        Handler().postDelayed({ doublebacktoexitpressedonce = false }, 2000)
    }


    fun showErrorSnackBar(message: String) {
        val snackBar =
            Snackbar.make(
                findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_LONG
            )
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(
            ContextCompat.getColor(
                this@baseactivity,
                R.color.snackbar_error_color
            )
        )
        snackBar.show()
    }
}






