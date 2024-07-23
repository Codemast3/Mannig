package com.example.mannig.activities

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mannig.R
import com.example.mannig.R.*
import com.example.mannig.adapters.boarditemsadapter
import com.example.mannig.firestore.firestoreclass
import com.example.mannig.models.Board
import com.example.mannig.models.User
import com.example.mannig.utils.Constants
import com.google.android.material.floatingactionbutton.FloatingActionButton



import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceIdReceiver
import com.google.firebase.installations.FirebaseInstallationsApi
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : baseactivity(), NavigationView.OnNavigationItemSelectedListener{
    companion object{
        const val MY_PROFILE_REQ_CODE : Int = 11
        const val CREATE_BOARD_REQ_CODE: Int = 12
    }
    private lateinit var musernamme: String
    private lateinit var msharedprefrences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupactionbar()

        val navigationView: NavigationView =  findViewById(id.nav_view);
        navigationView.setNavigationItemSelectedListener(this)
        msharedprefrences = this.getSharedPreferences(Constants.PROGEMANAG_PREFERENCES,
            MODE_PRIVATE
        )
        val tokenupdated =msharedprefrences.getBoolean(Constants.FCM_TOKEN_UPDATED,false)
        if (tokenupdated){
            firestoreclass().Loaduserdata(this,true)

        }else{
           getFCMToken()

        }
        firestoreclass().Loaduserdata(this,true)

        val fab: FloatingActionButton = findViewById(id.fab_create_board)
        fab.setOnClickListener{
            val intent = Intent(this,Createboardacitivity::class.java)
            intent.putExtra(Constants.NAME,musernamme)
            startActivityForResult(intent, CREATE_BOARD_REQ_CODE)
        }





    }
    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast


            // Update the token in the Firestore database
            updatefcmtoken(token)
        }
    }
    fun populateboardlist(boardList: ArrayList<Board>){
        val rvboardlist: RecyclerView = findViewById(id.rv_boards_list)
        val tvnobords: TextView = findViewById(id.tv_no_boards_available)
        if(boardList.size>0){
            rvboardlist.visibility = View.VISIBLE
            tvnobords.visibility = View.GONE
            rvboardlist.layoutManager = LinearLayoutManager(this)
           rvboardlist.setHasFixedSize(true)
            val adapter = boarditemsadapter(this,boardList)
           rvboardlist.adapter = adapter
            adapter.setOnClickListener(object: boarditemsadapter.OnClickListener{
                override fun onClick(position: Int, model: Board) {
                    val intent = Intent(this@MainActivity,tasklistactivity::class.java)
                    intent.putExtra(Constants.DOCUMENT_ID,model.documentId)
                    startActivity(intent)
                }
            })

        }else{
           rvboardlist.visibility = View.GONE
          tvnobords.visibility = View.VISIBLE

        }
    }
    private fun setupactionbar(){
        val toolbarmainactiv: Toolbar = findViewById(id.toolbar_mainactivity)

        setSupportActionBar(toolbarmainactiv)
        toolbarmainactiv.setNavigationIcon(drawable.ic_action_navigation_menu)
        toolbarmainactiv.setNavigationOnClickListener{
            toggledrawer()

        }
    }
   private fun toggledrawer(){
        val drawerlayoutt: DrawerLayout = findViewById(id.drawer_layout)
        if(drawerlayoutt.isDrawerOpen(GravityCompat.START)){
            drawerlayoutt.closeDrawer(GravityCompat.START)

        }else{
            drawerlayoutt.openDrawer(GravityCompat.START)
        }
    }


    override fun onBackPressed() {


        val drawerlayoutt: DrawerLayout = findViewById(id.drawer_layout)
       if(drawerlayoutt.isDrawerOpen(GravityCompat.START)){
            drawerlayoutt.closeDrawer(GravityCompat.START)
                }else{
           doublebacktoexit()
       }
    }
    fun updatenavigationuserdetails(user: User,readBoardslist:Boolean){
        musernamme = user.name
        val navuserimg: ImageView = findViewById(id.nav_user_image)

        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(drawable.ic_user_place_holder)
            .into(navuserimg)
        val tvusername: TextView = findViewById(id.tv_username)
        tvusername.text=user.name
        if (readBoardslist){
            firestoreclass().getboardlist(this)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == MY_PROFILE_REQ_CODE) {
        firestoreclass().Loaduserdata(this,true)

        }else if (resultCode == RESULT_OK && requestCode == CREATE_BOARD_REQ_CODE){
            firestoreclass().getboardlist(this)

        }
        else{
            Log.e("cancelled","cancelled")
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {


        when(item.itemId){
            id.nav_myprofile ->{
              startActivityForResult(Intent(this, MyProfileActivity::class.java),
                  MY_PROFILE_REQ_CODE)

            }
            id.nav_sign_out->{
                FirebaseAuth.getInstance().signOut()

                msharedprefrences.edit().clear().apply()
                val intent = Intent(this,Introactivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }


        }
        val drawerlayoutt: DrawerLayout = findViewById(id.drawer_layout)
        drawerlayoutt.closeDrawer(GravityCompat.START)
        return true

    }
    fun tokenupdatesucess(){
        val editor : SharedPreferences.Editor = msharedprefrences.edit()
        editor.putBoolean(Constants.FCM_TOKEN_UPDATED,true)
        editor.apply()
        firestoreclass().Loaduserdata(this,true)

    }
       private fun updatefcmtoken(token:String){
           val userhashmap = HashMap<String,Any>()
           userhashmap[Constants.FCM_TOKEN] = token
           firestoreclass().updateuserprofiledata(this,userhashmap)
       }
}