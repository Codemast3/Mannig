package com.example.mannig.activities

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mannig.R
import com.example.mannig.adapters.memberslistitemadapter
import com.example.mannig.firestore.firestoreclass
import com.example.mannig.models.Board
import com.example.mannig.models.User
import com.example.mannig.utils.Constants
import com.google.api.Distribution.BucketOptions.Linear

open class membersactivity : baseactivity() {
    private lateinit var mboarddetails: Board
    private lateinit var massignedmemberlist:ArrayList<User>
    private var anychangesmade: Boolean =false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_membersactivity)
        if(intent.hasExtra(Constants.BOARD_DETAIL)){
            mboarddetails = intent.getParcelableExtra<Board>(Constants.BOARD_DETAIL)!!

        }
        setupactionbar()
        firestoreclass().getassignedlistdetails(this,mboarddetails.assignedto)

    }
    fun setupactionbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar_members_activity)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = resources.getString(R.string.members)
        }
        toolbar.setNavigationOnClickListener { onBackPressed() }

    }
    fun setupmemberlist(list: ArrayList<User>){

        massignedmemberlist= list
        val rvmemberlist:   RecyclerView   = findViewById(R.id.rv_members_list)
                  rvmemberlist.layoutManager = LinearLayoutManager(this)
        rvmemberlist.hasFixedSize()
        val adapter = memberslistitemadapter(this,list)
        rvmemberlist.adapter = adapter
    }
    fun memeberdetails(user: User){
        mboarddetails.assignedto.add(user.id)
        firestoreclass().assignedmembertoboard(this,mboarddetails,user)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_members,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_add_members ->{
                dialogsearchmember()
                return true

            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun dialogsearchmember(){


        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_search_members)
        val tvAdd: TextView = dialog.findViewById(R.id.tv_add)
        val tvcancel:TextView = dialog.findViewById(R.id.tv_cancel)
        val etemail: AppCompatEditText = dialog.findViewById(R.id.et_email_search_member)
        // Set an OnClickListener on tv_add
        tvAdd.setOnClickListener {
            val email = etemail.text.toString()
            if(email.isNotEmpty()){
                dialog.dismiss()
               firestoreclass().getuserdetails(this,email)
            }else{
                Toast.makeText(
                    this@membersactivity,
                    "please enter members email address.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            // Your code to handle the click event
        }

        // Show the dialog

        tvcancel.setOnClickListener{
            dialog.dismiss()

        }
        dialog.show()
    }

    override fun onBackPressed() {
        if (anychangesmade){
            setResult(
                Activity.RESULT_OK
            )
        }
        super.onBackPressed()
    }
    fun memberassignedsuccess(user:User){
        massignedmemberlist.add(user)
        anychangesmade = true
        setupmemberlist(massignedmemberlist)
    }

}