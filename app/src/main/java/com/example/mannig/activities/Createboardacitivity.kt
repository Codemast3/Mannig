package com.example.mannig.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.mannig.R
import com.example.mannig.firestore.firestoreclass
import com.example.mannig.models.Board
import com.example.mannig.utils.Constants
import com.example.mannig.utils.Constants.PICK_IMAGE_REQUEST_CODE
import com.example.mannig.utils.Constants.READ_STORAGE_PERMISSION_CODE
import com.example.mannig.utils.Constants.getfileextension
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException
class Createboardacitivity : baseactivity() {
    private var mselectedimagefileuri: Uri? = null
    private lateinit var mUsername: String
    private var mboardimageurl:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_createboardacitivity)




        setupactionbar()
        if(intent.hasExtra(Constants.NAME)){
            mUsername = intent.getStringExtra(Constants.NAME)!!
        }
        val navuserimgg: ImageView = findViewById(R.id.iv_board_image)
        navuserimgg.setOnClickListener {
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
        val btncreate: Button = findViewById(R.id.btn_create)
        btncreate.setOnClickListener{
            if(mselectedimagefileuri != null){
                uploadboardimg()
            }else{
                createboard()
            }
        }
    }
    private fun createboard(){
        val assingeduserarraylist: ArrayList<String> = ArrayList()
        assingeduserarraylist.add(getcureentuserid())
        val boardname : AppCompatEditText = findViewById(R.id.et_board_name)
        var board = Board(
            boardname.text.toString(),
            mboardimageurl,
            mUsername,
            assingeduserarraylist


        )
        firestoreclass().createboard(this,board)
    }
    private fun uploadboardimg (){
        val sref : StorageReference = FirebaseStorage.getInstance().reference.child("BOARD_IMAGE"+ System.currentTimeMillis()+"."
                +Constants.getfileextension(this,mselectedimagefileuri))
        sref.putFile(mselectedimagefileuri!!).addOnSuccessListener {
                taskSnapshot->
            Log.i(
                "BOARD Image URL",
                taskSnapshot.metadata!!.reference!!.downloadUrl.toString()

            )
            taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                    uri ->
                Log.i("Downloadable image url ",uri.toString())
                mboardimageurl = uri.toString()


               createboard()
            }



        }.addOnFailureListener{
                exception->
            Toast.makeText(
                this,
                exception.message,
                Toast.LENGTH_LONG
            ).show()

        }
    }
    fun boardcreatesuccess(){
        setResult(Activity.RESULT_OK)
        finish()

    }

    private fun setupactionbar() {
        val toolbarcreateactuv: Toolbar = findViewById(R.id.toolbar_create_board_activity)
        setSupportActionBar(toolbarcreateactuv)
        val actionBar = supportActionBar
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24)
        }
        toolbarcreateactuv.setNavigationOnClickListener{onBackPressed()}


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
            val navuserimgg: ImageView = findViewById(R.id.iv_board_image)
            try {
                Glide
                    .with(this)
                    .load(mselectedimagefileuri)
                    .centerCrop()
                    .placeholder(R.drawable.ic_board_place_holder)
                    .into(navuserimgg)
            } catch (e: IOException) {
                e.printStackTrace()
            }


        }
    }
}