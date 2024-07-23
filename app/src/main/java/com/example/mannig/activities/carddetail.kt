package com.example.mannig.activities

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mannig.R
import com.example.mannig.adapters.cardmemberlistitemadapter
import com.example.mannig.dialogs.labelcolorlistdialog
import com.example.mannig.dialogs.memberlistdialog
import com.example.mannig.firestore.firestoreclass
import com.example.mannig.models.Board
import com.example.mannig.models.Card
import com.example.mannig.models.Task
import com.example.mannig.models.User
import com.example.mannig.models.selectedmembers
import com.example.mannig.utils.Constants
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class carddetail : baseactivity() {
    private lateinit var mboarddetails  : Board
    private var mtaslistposition = -1
    private var mcardposition = -1
    private var mselectedcolor = ""
    private lateinit var mmemberdeatilslist :ArrayList<User>
    private var mselecteduedatemilliseconds: Long =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_carddetail)

        getintentdata()
        setupactionbar()
        val etnamecarddetails :EditText = findViewById(R.id.et_name_card_details)
        etnamecarddetails.setText( mboarddetails.tasklist[mtaslistposition].cards[mcardposition].name)
        etnamecarddetails.setSelection(etnamecarddetails.text.toString().length)
        mselectedcolor = mboarddetails.tasklist[mtaslistposition].cards[mcardposition].labelcolor
        if (mselectedcolor.isNotEmpty()){
            setcolor()
        }

        val btnupdate: Button = findViewById(R.id.btn_update_card_details)
        btnupdate.setOnClickListener {
            if (etnamecarddetails.text.toString().isNotEmpty())
                updatecardetails()
            else {
                Toast.makeText(this@carddetail,
                    "enter card name  ",Toast.LENGTH_SHORT).show()
            }
        }
        val tvselectedlabel: TextView = findViewById(R.id.tv_select_label_color)
          tvselectedlabel.setOnClickListener{
         labelcolorlistdialog()
}
        val tvselectmemer:TextView = findViewById(R.id.tv_select_members)
        tvselectmemer.setOnClickListener {
            memberlistdialog()
        }

         setupselectedmemberslist()
        mselecteduedatemilliseconds = mboarddetails.tasklist[mtaslistposition].cards[mcardposition].duedate
        if(mselecteduedatemilliseconds > 0){
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH)
           val selectedate = simpleDateFormat.format(Date( mselecteduedatemilliseconds))
            val tv_select_due_date: TextView = findViewById(R.id.tv_select_due_date)
            tv_select_due_date.text = selectedate
        }
        val tv_select_due_date: TextView = findViewById(R.id.tv_select_due_date)
tv_select_due_date.setOnClickListener{
    showDataPicker()
}
    }
    fun addupdatetasklistsuccess(){
        setResult(Activity.RESULT_OK)
        finish()

    }
    private fun setupactionbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar_card_details_activity)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = mboarddetails.tasklist[mtaslistposition].cards[mcardposition].name

        }
        toolbar.setNavigationOnClickListener { onBackPressed() }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
   menuInflater.inflate(R.menu.menu_delete_card,menu)
        return super.onCreateOptionsMenu(menu)
    }
    private fun colorsList():ArrayList<String>{
        val colorsList:ArrayList<String> = ArrayList()
        colorsList.add("#43C86F")
        colorsList.add("#0C90F1")
        colorsList.add("#F72400")
        colorsList.add("#7A8089")
        colorsList.add("#D57C1D")
        colorsList.add("#770000")
        colorsList.add("#0022F8")
        return colorsList

    }
    private fun setcolor(){
        val tvselectlabelcolor:   TextView   = findViewById(R.id.tv_select_label_color)
        tvselectlabelcolor.text= ""
        tvselectlabelcolor.setBackgroundColor(Color.parseColor(mselectedcolor))

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_delete_card->{
                alertDialogForDeleteCard( mboarddetails.tasklist[mtaslistposition].cards[mcardposition].name)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun getintentdata(){
        if (intent.hasExtra(Constants.BOARD_DETAIL)){
            mboarddetails = intent.getParcelableExtra(Constants.BOARD_DETAIL)!!
        }
        if (intent.hasExtra(Constants.TASKLISTITEMPOSITION)){
            mtaslistposition = intent.getIntExtra(Constants.TASKLISTITEMPOSITION,-1)
        }
        if (intent.hasExtra(Constants.CARD_LIST_ITEM_POSOTION)){
            mcardposition = intent.getIntExtra(Constants.CARD_LIST_ITEM_POSOTION,-1)
        }
        if (intent.hasExtra(Constants.BOARD_MEMEBERS_LIST)){
            mmemberdeatilslist = intent.getParcelableArrayListExtra(
                Constants.BOARD_MEMEBERS_LIST)!!
            
        }

    }
    private fun updatecardetails() {
        val etname: AppCompatEditText = findViewById(R.id.et_name_card_details)
        val cardName = etname.text.toString()

        if (cardName.isNotEmpty()) {
            val card = Card(
                cardName,
                mboarddetails.tasklist[mtaslistposition].cards[mcardposition].createdby,
                mboarddetails.tasklist[mtaslistposition].cards[mcardposition].assignedto,
                mselectedcolor,
                mselecteduedatemilliseconds

            )
            val tassklist:ArrayList<Task> = mboarddetails.tasklist
            tassklist.removeAt(tassklist.size-1)

            mboarddetails.tasklist[mtaslistposition].cards[mcardposition] = card
            firestoreclass().addupdatetasklist(this, mboarddetails)
        } else {
            Toast.makeText(this@carddetail, "Enter card name", Toast.LENGTH_SHORT).show()
        }
    }
    private fun alertDialogForDeleteCard(cardName: String) {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle(resources.getString(R.string.alert))
        //set message for alert dialog
        builder.setMessage(
            resources.getString(
                R.string.confirmation_message_to_delete_card,
                cardName
            )
        )
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, which ->
            dialogInterface.dismiss() // Dialog will be dismissed
            deletecard()
        }
        //performing negative action
        builder.setNegativeButton(resources.getString(R.string.no)) { dialogInterface, which ->
            dialogInterface.dismiss() // Dialog will be dismissed
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
        alertDialog.show()  // show the dialog to UI
    }
    private fun deletecard() {
        val cardlist: ArrayList<Card> = mboarddetails.tasklist[mtaslistposition].cards
        if (mcardposition != -1 && mcardposition < cardlist.size) {
            cardlist.removeAt(mcardposition)
            // Update task list with the modified card list
            mboarddetails.tasklist[mtaslistposition].cards = cardlist
            firestoreclass().addupdatetasklist(this@carddetail, mboarddetails)
        } else {
            // Handle invalid position or out of bounds scenarios
            Toast.makeText(this@carddetail, "Invalid card position", Toast.LENGTH_SHORT).show()
        }

    }
    private fun memberlistdialog() {
        var cardassignedmmeberlist =
            mboarddetails.tasklist[mtaslistposition].cards[mcardposition].assignedto
        if (cardassignedmmeberlist.size > 0) {
            for (i in mmemberdeatilslist.indices) {
                for (j in cardassignedmmeberlist) {
                    if (mmemberdeatilslist[i].id == j) {
                        mmemberdeatilslist[i].selected = true
                    }
                }
            }


        } else {
            for (i in mmemberdeatilslist.indices) {

                mmemberdeatilslist[i].selected = false
            }


        }
        val listdialog = object : memberlistdialog(
            this,
            mmemberdeatilslist,
            resources.getString(R.string.str_select_member)
        ) {
            override fun onItemSelected(user: User, action: String) {
             if (action==Constants.SELECT){
                 if(!mboarddetails.tasklist[mtaslistposition].cards[mcardposition].assignedto
                     .contains(user.id)){
                     mboarddetails.tasklist[mtaslistposition].cards[mcardposition].assignedto
                         .add(user.id)
                 }
             }else{
                 mboarddetails.tasklist[mtaslistposition].cards[mcardposition].assignedto
                     .remove(user.id)

                 for (i in mmemberdeatilslist.indices){
                     if (mmemberdeatilslist[i].id==user.id){
                         mmemberdeatilslist[i].selected=false

                     }
                 }


             }
                setupselectedmemberslist()




            }

        }
        listdialog.show()



    }
    private fun labelcolorlistdialog(){
        val colorlist:ArrayList<String> = colorsList()
        val listdialog = object : labelcolorlistdialog(
            this,
            colorlist,
            resources.getString(R.string.str_select_label_color),
            mselectedcolor)
            {
                override fun onitemselected(color: String) {
                  mselectedcolor = color
                    setcolor()

                }

            }
        listdialog.show()


    }
    private fun setupselectedmemberslist(){
        val cardassignedmmeberlist = mboarddetails.tasklist[mtaslistposition].cards[mcardposition].assignedto
        val selectedmemberslist : ArrayList<selectedmembers> = ArrayList()

            for (i in mmemberdeatilslist.indices) {
                for (j in cardassignedmmeberlist) {
                    if (mmemberdeatilslist[i].id == j) {
                        val selectedmember = selectedmembers(
                            mmemberdeatilslist[i].id,
                            mmemberdeatilslist[i].image
                        )
                        selectedmemberslist.add(selectedmember)
                    }
                }
            }
        if (selectedmemberslist.size>0){
            selectedmemberslist.add(selectedmembers("",""))
            val tvselectmembers:   TextView     = findViewById(R.id.tv_select_members)
            tvselectmembers.visibility = View.GONE
            val rvselectmembers: RecyclerView = findViewById(R.id.rv_selected_members_list)
            rvselectmembers.visibility = View.VISIBLE
            rvselectmembers.layoutManager = GridLayoutManager(
                this,6
            )
            val adapter  = cardmemberlistitemadapter(this,selectedmemberslist,true)
            rvselectmembers.adapter=adapter
            adapter.setOnClickListener(
                object : cardmemberlistitemadapter.OnClickListener {
                    override fun onClick() {
                       memberlistdialog()
                    }

                }
            )

        }else{
            val tvselectmembers:   TextView     = findViewById(R.id.tv_select_members)
            tvselectmembers.visibility  = View.VISIBLE
            val rvselectmembers: RecyclerView = findViewById(R.id.rv_selected_members_list)
            rvselectmembers.visibility = View.GONE


        }

    }

    private fun showDataPicker() {
        /**
         * This Gets a calendar using the default time zone and locale.
         * The calender returned is based on the current time
         * in the default time zone with the default.
         */
        val c = Calendar.getInstance()
        val year =
            c.get(Calendar.YEAR) // Returns the value of the given calendar field. This indicates YEAR
        val month = c.get(Calendar.MONTH) // This indicates the Month
        val day = c.get(Calendar.DAY_OF_MONTH) // This indicates the Day

        /**
         * Creates a new date picker dialog for the specified date using the parent
         * context's default date picker dialog theme.
         */
        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                /*
                  The listener used to indicate the user has finished selecting a date.
                 Here the selected date is set into format i.e : day/Month/Year
                  And the month is counted in java is 0 to 11 so we need to add +1 so it can be as selected.

                 Here the selected date is set into format i.e : day/Month/Year
                  And the month is counted in java is 0 to 11 so we need to add +1 so it can be as selected.*/

                // Here we have appended 0 if the selected day is smaller than 10 to make it double digit value.
                val sDayOfMonth = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
                // Here we have appended 0 if the selected month is smaller than 10 to make it double digit value.
                val sMonthOfYear =
                    if ((monthOfYear + 1) < 10) "0${monthOfYear + 1}" else "${monthOfYear + 1}"

                val selectedDate = "$sDayOfMonth/$sMonthOfYear/$year"
                // Selected date it set to the TextView to make it visible to user.
                val tv_select_due_date: TextView = findViewById(R.id.tv_select_due_date)
                tv_select_due_date.text = selectedDate

                /**
                 * Here we have taken an instance of Date Formatter as it will format our
                 * selected date in the format which we pass it as an parameter and Locale.
                 * Here I have passed the format as dd/MM/yyyy.
                 */

                /**
                 * Here we have taken an instance of Date Formatter as it will format our
                 * selected date in the format which we pass it as an parameter and Locale.
                 * Here I have passed the format as dd/MM/yyyy.
                 */
                /**
                 * Here we have taken an instance of Date Formatter as it will format our
                 * selected date in the format which we pass it as an parameter and Locale.
                 * Here I have passed the format as dd/MM/yyyy.
                 */
                /**
                 * Here we have taken an instance of Date Formatter as it will format our
                 * selected date in the format which we pass it as an parameter and Locale.
                 * Here I have passed the format as dd/MM/yyyy.
                 */
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

                // The formatter will parse the selected date in to Date object
                // so we can simply get date in to milliseconds.
                val theDate = sdf.parse(selectedDate)

                /** Here we have get the time in milliSeconds from Date object
                 */

                /** Here we have get the time in milliSeconds from Date object
                 */

                /** Here we have get the time in milliSeconds from Date object
                 */

                /** Here we have get the time in milliSeconds from Date object
                 */
                mselecteduedatemilliseconds = theDate!!.time
            },
            year,
            month,
            day
        )
        dpd.show() // It is used to show the datePicker Dialog.
    }
}