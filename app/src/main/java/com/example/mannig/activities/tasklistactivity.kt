package com.example.mannig.activities

import android.app.Activity
import android.content.Intent
import android.icu.text.Transliterator.Position
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mannig.R
import com.example.mannig.adapters.tasklistadapter
import com.example.mannig.firestore.firestoreclass
import com.example.mannig.models.Board
import com.example.mannig.models.Card
import com.example.mannig.models.Task
import com.example.mannig.models.User
import com.example.mannig.utils.Constants

class tasklistactivity : baseactivity() {
    private lateinit var mboarddetails:Board
    private var anyChangemade:Boolean = false
    private lateinit var mboarddocumentid: String
     lateinit var massignedmemberdetaillist:ArrayList<User>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_tasklistactivity)

        if (intent.hasExtra(Constants.DOCUMENT_ID)){
            mboarddocumentid = intent.getStringExtra(Constants.DOCUMENT_ID)!!
        }
        firestoreclass().getboarddetails(this,mboarddocumentid)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode ==Activity.RESULT_OK && requestCode == MEMBER_REQUEST_CODE || requestCode == CARD_DETAIL_REQ_CODE ){
            firestoreclass().getboarddetails(this,mboarddocumentid)

        }else{
            Log.e("cancelled","cancelled")
        }
    }
    fun carddetails(tasklistposition:Int,cardpostion:Int){
        val intent = Intent(this,carddetail::class.java)
        intent.putExtra(Constants.BOARD_DETAIL,mboarddetails)
        intent.putExtra(Constants.TASKLISTITEMPOSITION,tasklistposition)
        intent.putExtra(Constants.CARD_LIST_ITEM_POSOTION,cardpostion)
        intent.putExtra(Constants.BOARD_MEMEBERS_LIST,massignedmemberdetaillist)
        startActivityForResult(intent, CARD_DETAIL_REQ_CODE)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_members,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_members ->{
                val intent = (Intent(this,membersactivity::class.java))
                intent.putExtra(Constants.BOARD_DETAIL,mboarddetails)
                startActivityForResult(intent, MEMBER_REQUEST_CODE)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun setupactionbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar_task_list_activity)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = mboarddetails.name
        }
        toolbar.setNavigationOnClickListener { onBackPressed() }

    }
    fun boarddetails(board: Board){
        mboarddetails=board
        setupactionbar()


        firestoreclass().getassignedlistdetails(this,
            mboarddetails.assignedto)
    }
    fun addupdatetasklistsuccess(){

        firestoreclass().getboarddetails(this,mboarddetails.documentId)
    }
    fun createtasklist(taskkistname:String){
        val task = Task(taskkistname,firestoreclass().getCurrentuserid())
        mboarddetails.tasklist.add(0,task)
       mboarddetails.tasklist.removeAt(mboarddetails.tasklist.size-1)
        firestoreclass().addupdatetasklist(this,mboarddetails)
    }
    fun updatetasllist(position: Int,listname:String,model:Task){
        val task = Task(listname,model.createdby)
        mboarddetails.tasklist[position]= task
        mboarddetails.tasklist.removeAt(mboarddetails.tasklist.size-1)
        firestoreclass().addupdatetasklist(this,mboarddetails)
    }
    fun deletetasklist(position: Int){
        mboarddetails.tasklist.removeAt(position)
        mboarddetails.tasklist.removeAt(mboarddetails.tasklist.size-1)
        firestoreclass().addupdatetasklist(this,mboarddetails)
    }
    fun addcardtotasklist(position: Int,cardName: String){
        mboarddetails.tasklist.removeAt(mboarddetails.tasklist.size-1)

        val cardassigneduserlist:ArrayList<String> = ArrayList()
        cardassigneduserlist.add(firestoreclass().getCurrentuserid())
        val card  = Card(cardName,firestoreclass().getCurrentuserid(),cardassigneduserlist)
        val cardlist = mboarddetails.tasklist[position].cards
        cardlist.add(card)
        val task = Task(
           mboarddetails.tasklist[position].title,
           mboarddetails.tasklist[position].createdby,

            cardlist

        )
        mboarddetails.tasklist[position]=task

        firestoreclass().addupdatetasklist(this,mboarddetails)
    }
    fun boardmemebersdeatil(list: ArrayList<User>){
        massignedmemberdetaillist = list
        val addtasklist = Task(resources.getString(R.string.add_list))
       mboarddetails.tasklist.add(addtasklist)
        val rvtasklist:RecyclerView = findViewById(R.id.rv_task_list)
        rvtasklist.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        rvtasklist.setHasFixedSize(true)
        val adapter = tasklistadapter(this,mboarddetails.tasklist)
        rvtasklist.adapter=adapter

    }
    fun updatecaardsintasklist(tasklistposition: Int,cards:ArrayList<Card>){
        mboarddetails.tasklist.removeAt(mboarddetails.tasklist.size - 1)
        mboarddetails.tasklist[tasklistposition].cards = cards
        firestoreclass().addupdatetasklist(this,mboarddetails)
    }
    companion object{
        const val MEMBER_REQUEST_CODE : Int  =13
        const val CARD_DETAIL_REQ_CODE: Int =14
    }
}