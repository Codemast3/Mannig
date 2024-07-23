package com.example.mannig.dialogs

import android.app.Dialog
import android.content.Context
import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mannig.R
import com.example.mannig.adapters.classlabelcolorlistadapter


abstract class labelcolorlistdialog(
    context: Context,
    private var list :
    ArrayList<String>,
    private var title: String = "",
    private var mselectedcolor: String = ""
):Dialog(context) {
    private var adapter:classlabelcolorlistadapter?=null
    private lateinit var myTextView: TextView
    private lateinit var mrvlist: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view  = LayoutInflater.from(context).inflate(R.layout.dialog_list,null)
        setContentView(view)
        setCanceledOnTouchOutside(true)
        setCancelable(true)
        setuprecyclerview(view)


    }
    private fun setuprecyclerview(view: View){
      myTextView = findViewById(R.id.tvTitle)


            myTextView.text = title

        mrvlist = findViewById(R.id.rvList)


        mrvlist.layoutManager = LinearLayoutManager(context)
        adapter = classlabelcolorlistadapter(context,list,mselectedcolor)
        mrvlist.adapter = adapter
        adapter!!.onitemclicklistener = object : classlabelcolorlistadapter.Onitemclclicklistener{
            override fun onClick(position: Int, color: String) {
               dismiss()
                onitemselected(color)

            }

        }

    }
    protected abstract fun onitemselected(color: String)



}