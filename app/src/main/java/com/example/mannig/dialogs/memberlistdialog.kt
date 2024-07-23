package com.example.mannig.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mannig.R
import com.example.mannig.adapters.memberslistitemadapter
import com.example.mannig.models.User

abstract class memberlistdialog (


    context: Context,
    private var list: ArrayList<User>,
    private val title: String = ""
    ) : Dialog(context) {

        private var adapter: memberslistitemadapter? = null
    private lateinit var myTextView: TextView
    private lateinit var mrvlist: RecyclerView

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState ?: Bundle())

            val view = LayoutInflater.from(context).inflate(R.layout.dialog_list, null)

            setContentView(view)
            setCanceledOnTouchOutside(true)
            setCancelable(true)
            setUpRecyclerView(view)
        }

        private fun setUpRecyclerView(view: View) {
             myTextView = findViewById(R.id.tvTitle)

            myTextView.text = title

            if (list.size > 0) {
                mrvlist = findViewById(R.id.rvList)


                mrvlist.layoutManager = LinearLayoutManager(context)
                adapter = memberslistitemadapter(context, list)
               mrvlist.adapter = adapter

                adapter!!.setOnClickListener(object :
                   memberslistitemadapter.OnClickListener {
                    override fun onClick(position: Int, user: User, action:String) {
                        dismiss()
                        onItemSelected(user, action)
                    }
                })
            }
        }

        protected abstract fun onItemSelected(user: User, action:String)
    }
