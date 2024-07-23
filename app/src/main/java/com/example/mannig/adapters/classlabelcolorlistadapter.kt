package com.example.mannig.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mannig.R
import android.graphics.Color
import android.widget.ImageView

class classlabelcolorlistadapter(
    private val context: Context,
    private var list: ArrayList<String>,
    private val mselectedcolor:String):RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        var onitemclicklistener: Onitemclclicklistener?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return Myviewholder(LayoutInflater.from(context).inflate(R.layout.item_label_color,parent,false))




    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       val item = list[position]
        if (holder is Myviewholder){
            holder.viewmain.setBackgroundColor(Color.parseColor(item))
            if (item==mselectedcolor){
                holder.ivselectcolor.visibility = View.VISIBLE

            }else{
                holder.ivselectcolor.visibility = View.GONE
            }
            holder.itemView.setOnClickListener {
                if (onitemclicklistener!=null){
                    onitemclicklistener!!.onClick(position,item)
                }
            }
        }
    }
    inner class Myviewholder(view:View):RecyclerView.ViewHolder(view){
        val viewmain:  View     =view.findViewById(R.id.view_main)
        val ivselectcolor:      ImageView         = view.findViewById(R.id.iv_selected_color)
    }

    interface Onitemclclicklistener{
        fun onClick(position: Int,color: String)
    }

}
