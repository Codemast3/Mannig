package com.example.mannig.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mannig.R
import com.example.mannig.models.Board

import android.content.Context
import android.widget.TextView
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import org.w3c.dom.Text

open class boarditemsadapter(private val context: Context,
    private var list: ArrayList<Board>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var onClickListener: OnClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return myviewholder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_board, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val model = list[position]

        if (holder is myviewholder) {


            Glide
                .with(context)
                .load(model.image)
                .centerCrop()
                .placeholder(R.drawable.ic_board_place_holder)
                .into(holder.ivBoardImage)
            holder.tvName.text = model.name
            holder.tvCreatedBy.text =
                "Created by : ${model.createdby}"
            holder.itemView.setOnClickListener {
                if (onClickListener != null) {
                    onClickListener!!.onClick(position, model)
                }

            }
        }
    }

    interface OnClickListener {
        fun onClick(position: Int, model: Board)
    }
fun setOnClickListener(onClickListener: OnClickListener){
    this.onClickListener = onClickListener
}
    inner class myviewholder(view: View) : RecyclerView.ViewHolder(view) {
        val ivBoardImage: CircleImageView = view.findViewById(R.id.iv_board_image)
        val tvName: TextView = view.findViewById(R.id.tv_name)
        val tvCreatedBy: TextView = view.findViewById(R.id.tv_created_by)


    }
}