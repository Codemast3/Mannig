package com.example.mannig.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mannig.R

import com.example.mannig.models.selectedmembers
import de.hdodenhof.circleimageview.CircleImageView

open class cardmemberlistitemadapter(
    private val context: Context,
    private val list:ArrayList<selectedmembers>,
    private val assignmember:Boolean
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var onClickListener: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
           return Myviewholder(LayoutInflater.from(context).inflate(
                R.layout.item_card_selected_members,
                parent,
                false
            ))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is Myviewholder){
            if (position==list.size-1 && assignmember){
                holder.ivaddmem.visibility = View.VISIBLE
                holder.ivselectedmemimg.visibility = View.GONE

            }else{
                holder.ivaddmem.visibility = View.GONE
                holder.ivselectedmemimg.visibility = View.VISIBLE

                Glide
                    .with(context)
                    .load(model.image)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(holder.ivselectedmemimg)


            }
            holder.itemView.setOnClickListener {
                if (onClickListener!=null){
                    onClickListener!!.onClick()
                }
            }
        }
    }
    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }
    interface OnClickListener  {
        fun onClick()
    }


    inner class Myviewholder(view: View):RecyclerView.ViewHolder(view){
        val ivaddmem: CircleImageView          =view.findViewById(R.id.iv_add_member)
        val ivselectedmemimg: CircleImageView          =view.findViewById(R.id.iv_selected_member_image)


    }
}