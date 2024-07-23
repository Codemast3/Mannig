package com.example.mannig.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mannig.R
import com.example.mannig.activities.tasklistactivity
import com.example.mannig.models.Task
import java.util.Collections

open class tasklistadapter (private val context: Context,
                            private var list: ArrayList<Task>
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private var mpositiondraggedfrrom = -1
    private var  mpositiondraggedto = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_task,parent,false)
        val layoutParams = LinearLayout.LayoutParams(
            (parent.width*0.7).toInt(),LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins((15.todp().topx()),0,(40.todp()).topx(),0)
        view.layoutParams= layoutParams
        return Myviewholder(view)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, @SuppressLint("RecyclerView") position: Int) {

        val model =list[position]
        if (holder is Myviewholder){
            if (position==list.size-1){
                holder.tvaddtasklist.visibility=View.VISIBLE
                holder.llaylout.visibility = View.GONE
            }else{
                holder.tvaddtasklist.visibility=View.GONE
                holder.llaylout.visibility = View.VISIBLE

            }
            holder.tvatasklist.text = model.title
            holder.tvaddtasklist.setOnClickListener{
                holder.tvaddtasklist.visibility=View.GONE
                holder.laylout.visibility = View.VISIBLE
            }
            holder.ibcloselistname.setOnClickListener{
                holder.tvaddtasklist.visibility=View.VISIBLE
                holder.laylout.visibility = View.GONE
            }
            holder.ibdonename.setOnClickListener{
                val listname = holder.ettasklistname.text.toString()
                if(listname.isNotEmpty()){
                    if (context is tasklistactivity){
                        context.createtasklist(listname)
                    }
                }else{
                    Toast.makeText(context,"please enter list name.",Toast.LENGTH_SHORT).show()
                }
            }
            holder.ibeditlstname.setOnClickListener{
                holder.ettasklistname .setText(model.title)
                holder.lltitleview.visibility=View.GONE
                holder.cvedittasklist.visibility=View.VISIBLE
            }
            holder.ibcloseeditname.setOnClickListener{
                holder.lltitleview.visibility=View.VISIBLE
                holder.cvedittasklist.visibility = View.GONE
            }
            holder.ibdonelistname.setOnClickListener {
               val listname = holder.eteditasklistname.text.toString()
                if(listname.isNotEmpty()){
                    if (context is tasklistactivity){
                        context.updatetasllist(position,listname,model)
                    }
                }else{
                    Toast.makeText(context,"please enter a list name.",Toast.LENGTH_SHORT).show()
                }

            }
            holder.ibdeletellist.setOnClickListener {
                alertDialogForDeleteList(position,model.title)
            }
            holder.tvaaddcard.setOnClickListener {
                holder.tvaaddcard.visibility = View.GONE
                holder.addcard.visibility = View.VISIBLE



            }
            holder.ibclosecarddname.setOnClickListener {
                holder.tvaaddcard.visibility = View.VISIBLE
                holder.addcard.visibility = View.GONE
            }

            holder.ibdonecardname.setOnClickListener {
                val cardname = holder.etcardname.text.toString()
                if(cardname.isNotEmpty()){
                    if (context is tasklistactivity){
                        context.addcardtotasklist(position,cardname)
                    }
                }else{
                    Toast.makeText(context,"please enter a card name.",Toast.LENGTH_SHORT).show()
                }

            }
            holder.rvcardlist.layoutManager = LinearLayoutManager(context)
            holder.rvcardlist.setHasFixedSize(true)
            val adapter = cardlistiteamsadaoter(context,model.cards)
            holder.rvcardlist.adapter = adapter
            adapter.setOnClickListener(
                object : cardlistiteamsadaoter.OnClickListener{
                    override fun onClick(cardposition: Int){
                        if (context is tasklistactivity){
                            context.carddetails(position, cardposition )
                        }
                    }
                }

            )
            val dividerItemDecoration = DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL)
            holder.rvcardlist.addItemDecoration(dividerItemDecoration)

            val helper = ItemTouchHelper(
                object: ItemTouchHelper.SimpleCallback(
                    ItemTouchHelper.UP or ItemTouchHelper.DOWN,0
                ) {
                    override fun onMove(
                        recyclerView: RecyclerView,
                        dragged: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                    ): Boolean {
                        val draggedposition = dragged.adapterPosition
                        val targetpos = target.adapterPosition
                        if (mpositiondraggedto == -1) {
                            mpositiondraggedfrrom == draggedposition
                        }
                        mpositiondraggedto = targetpos
                        Collections.swap(list[position].cards, draggedposition, targetpos)
                        adapter.notifyItemMoved(draggedposition, targetpos)
                        return false
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                    }

                    override fun clearView(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder
                    ) {
                        super.clearView(recyclerView, viewHolder)
                        if (mpositiondraggedfrrom!=0 && mpositiondraggedto!=1 && mpositiondraggedfrrom!= mpositiondraggedto) {
                            (context as tasklistactivity).updatecaardsintasklist(
                                position,
                                list[position].cards
                            )
                        }
                        mpositiondraggedfrrom = -1
                        mpositiondraggedto = -1

                    }

                }
            )
            helper.attachToRecyclerView(holder.rvcardlist)

        }
    }
    private fun alertDialogForDeleteList(position: Int, title: String) {
        val builder = AlertDialog.Builder(context)
        //set title for alert dialog
        builder.setTitle("Alert")
        //set message for alert dialog
        builder.setMessage("Are you sure you want to delete $title.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            dialogInterface.dismiss() // Dialog will be dismissed

            if (context is tasklistactivity) {
                context.deletetasklist(position)
            }
        }

        //performing negative action
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss() // Dialog will be dismissed
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
        alertDialog.show()  // show the dialog to UI
    }



    private fun Int.todp():Int =(this/Resources.getSystem().displayMetrics.density).toInt()
    private fun Int.topx():Int =(this*Resources.getSystem().displayMetrics.density).toInt()
   inner class Myviewholder(view: View) :RecyclerView.ViewHolder(view){
       val tvaddtasklist: TextView = view.findViewById(R.id.tv_add_task_list)
       val llaylout: LinearLayout = view.findViewById(R.id.ll_task_item)
       val tvatasklist: TextView = view.findViewById(R.id.tv_task_list_title)
       val laylout: CardView = view.findViewById(R.id.cv_add_task_list_name)
       val ibcloselistname : ImageButton = view.findViewById(R.id.ib_close_list_name)
       val ibdonename : ImageButton = view.findViewById(R.id.ib_done_list_name)
       val ettasklistname : EditText = view.findViewById(R.id.et_task_list_name)
       val ibeditlstname: ImageView = view.findViewById(R.id.ib_edit_list_name)
       val lltitleview: LinearLayout = view.findViewById(R.id.ll_title_view)
       val cvedittasklist: CardView = view.findViewById(R.id.cv_edit_task_list_name)
       val ibdonelistname: ImageButton = view.findViewById(R.id.ib_done_edit_list_name)
       val eteditasklistname : EditText = view.findViewById(R.id.et_edit_task_list_name)
       val ibdeletellist : ImageButton = view.findViewById(R.id.ib_delete_list)
       val tvaaddcard: TextView= view.findViewById(R.id.tv_add_card)
       val addcard: CardView = view.findViewById(R.id.cv_add_card)
       val ibclosecarddname: ImageView = view.findViewById(R.id.ib_close_card_name)
       val ibdonecardname: ImageButton = view.findViewById(R.id.ib_done_card_name)
       val etcardname : EditText = view.findViewById(R.id.et_card_name)
       val rvcardlist:RecyclerView = view.findViewById(R.id.rv_card_list)
       val ibcloseeditname:   ImageButton             = view.findViewById(R.id.ib_close_editable_view)





   }


    }