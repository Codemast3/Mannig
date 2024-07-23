package com.example.mannig.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.*

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

import com.example.mannig.R
import com.example.mannig.firestore.firestoreclass
import com.example.mannig.models.User
import com.example.mannig.utils.Constants
import com.example.mannig.utils.Constants.PICK_IMAGE_REQUEST_CODE
import com.example.mannig.utils.Constants.READ_STORAGE_PERMISSION_CODE
import com.example.mannig.utils.Constants.showiamgechosser
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException

class MyProfileActivity : baseactivity() {


    private var mselectedimagefileuri: Uri? = null
    private lateinit var muserdetails: User
    private var mprofileimageurl: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_my_profile)

        setupactionbar()
        firestoreclass().Loaduserdata(this)
        val navuserimg: ImageView = findViewById(R.id.iv_profile_user_image)
        navuserimg.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.READ_MEDIA_IMAGES
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    Constants.showiamgechosser(this)
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),
                        Constants.READ_STORAGE_PERMISSION_CODE
                    )
                }
            } else {
                if (ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    Constants.showiamgechosser(this)
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        Constants.READ_STORAGE_PERMISSION_CODE
                    )
                }
            }
        }
        val btnupdate: Button = findViewById(R.id.btn_update)
        btnupdate.setOnClickListener {
            if (
                mselectedimagefileuri != null
            ) {
                uploaduserimage()
            }else{
                updateuserprofiledata()
            }
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showiamgechosser(this)
            }
        } else {
            Toast.makeText(
                this,
                "oops,ypu just denied the permission for storage . you can change it from settings ",
                Toast.LENGTH_LONG

            ).show()
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST_CODE && data!!.data != null) {
            mselectedimagefileuri = data.data
            val navuserimg: ImageView = findViewById(R.id.iv_profile_user_image)
            try {
                Glide
                    .with(this@MyProfileActivity)
                    .load(mselectedimagefileuri)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(navuserimg)
            } catch (e: IOException) {
                e.printStackTrace()
            }


        }
    }

    fun setupactionbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar_my_profile_activity)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = resources.getString(R.string.nav_my_profile)
        }
        toolbar.setNavigationOnClickListener { onBackPressed() }

    }

    fun setuserdatainui(user: User) {
        muserdetails = user


        val navuserimg: ImageView = findViewById(R.id.iv_profile_user_image)
        Glide
            .with(this@MyProfileActivity)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(navuserimg)


        val etname: AppCompatEditText = findViewById(R.id.et_name)
        val etemail: AppCompatEditText = findViewById(R.id.et_email)
        etname.setText(user.name)
        etemail.setText(user.email)
        if (user.mobile != 0L) {
            val etmobile: AppCompatEditText = findViewById(R.id.et_mobile)
            etmobile.setText(user.mobile.toString())
        }


    }
    private fun updateuserprofiledata(){
        val userHashpmap = HashMap<String,Any>()

        if(mprofileimageurl.isNotEmpty() && mprofileimageurl != muserdetails.image){
            userHashpmap["image"]
            userHashpmap[Constants.IMAGE]=mprofileimageurl

        }
        val etname: AppCompatEditText =findViewById(R.id.et_name)
        if (etname.text.toString() != muserdetails.name){
            userHashpmap[Constants.NAME]=etname.text.toString()


        }
        val etnumber: AppCompatEditText = findViewById(R.id.et_mobile)
        if(etnumber.text.toString() != muserdetails.mobile.toString()){
            userHashpmap[Constants.MOBILE]=etnumber.text.toString().toLong()

        }

        firestoreclass().updateuserprofiledata(this,userHashpmap)


    }
    private fun uploaduserimage
                (){
                    if (mselectedimagefileuri != null){

                        val sref : StorageReference = FirebaseStorage.getInstance().reference.child("USER_IMAGE "+ System.currentTimeMillis()+"."
                                +Constants.getfileextension(this,mselectedimagefileuri))
                        sref.putFile(mselectedimagefileuri!!).addOnSuccessListener {
                            taskSnapshot->
                            Log.i(
                                "Firebase Image URL",
                                taskSnapshot.metadata!!.reference!!.downloadUrl.toString()

                            )
                            taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                                uri ->
                                Log.e("Downloadable image url ",uri.toString())
                                mprofileimageurl = uri.toString()


                                updateuserprofiledata()
                            }



                        }.addOnFailureListener{
                            exception->
                            Toast.makeText(
                                this@MyProfileActivity,
                                exception.message,
                                Toast.LENGTH_LONG
                            ).show()

                        }
                    }
                }


    fun profileupdatesuccess(){
        setResult(Activity.RESULT_OK)
        finish()
    }
}







