package com.example.mannig.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mannig.R
import com.example.mannig.activities.tasklistactivity
import com.example.mannig.models.Card
import com.example.mannig.models.selectedmembers

open class cardlistiteamsadaoter (
    private val context:Context,
            private var list:ArrayList<Card>,

):RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private var onClickListener: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return Myviewholder(
            LayoutInflater.from(context).inflate(
                R.layout.item_card,
                parent,
                false

            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder,  position: Int) {
        val model = list[position]
        if (holder is Myviewholder){
            if (model.labelcolor.isNotEmpty()){
                holder.viewlabelcolor.visibility = View.VISIBLE
                holder.viewlabelcolor.setBackgroundColor(Color.parseColor(model.labelcolor))
            }else{
                holder.viewlabelcolor.visibility = View.GONE
            }
            holder.tvcardname.text=model.name
            if ((context as tasklistactivity)
                    .massignedmemberdetaillist.size>0 ){
                val selectedmemberlist : ArrayList<selectedmembers> = ArrayList()
                for (i in context.massignedmemberdetaillist.indices){
                    for (j in model.assignedto) {
                        if (context.massignedmemberdetaillist[i].id == j) {
                            val selectedmembers = selectedmembers(
                                context.massignedmemberdetaillist[i].id,
                                context.massignedmemberdetaillist[i].image
                            )
                            selectedmemberlist.add(selectedmembers)

                        }
                    }
                }
                if (selectedmemberlist.size>0){
                    if(selectedmemberlist.size ==1&&selectedmemberlist[0].id == model.createdby){

                        holder.rvcardselectedmember.visibility = View.GONE
                    }else{
                        holder.rvcardselectedmember.visibility = View.VISIBLE
                        holder.rvcardselectedmember.layoutManager = GridLayoutManager(context,4)
                        val adapter= cardmemberlistitemadapter(context,selectedmemberlist,false)
                        holder.rvcardselectedmember.adapter = adapter
                        adapter.setOnClickListener (object : cardmemberlistitemadapter.OnClickListener{
                            override fun onClick() {
                                if (onClickListener!=null){
                                    onClickListener!!.onClick(position)
                                }
                            }


                        })
                    }
                }else{
                    holder.rvcardselectedmember.visibility = View.GONE
                }
            }


                holder.itemView.setOnClickListener {
                if(onClickListener!=null){
                    onClickListener!!.onClick(position)
                }

            }
        }
    }
fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }
    interface OnClickListener  {
        fun onClick(position: Int)
    }






    inner class Myviewholder(view:View):RecyclerView.ViewHolder(view){
        val tvcardname :       TextView          = view.findViewById(R.id.tv_card_name)
        val viewlabelcolor: View = view.findViewById(R.id.view_label_color)
        val rvcardselectedmember: RecyclerView = view.findViewById(R.id.rv_card_selected_members_list)
    }

}