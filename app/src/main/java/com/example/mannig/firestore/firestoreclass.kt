package com.example.mannig.firestore

import android.app.Activity
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.widget.Toast
import com.example.mannig.activities.Createboardacitivity
import com.example.mannig.activities.MainActivity
import com.example.mannig.activities.MyProfileActivity
import com.example.mannig.activities.Signinactivity
import com.example.mannig.activities.Signup
import com.example.mannig.activities.carddetail
import com.example.mannig.activities.membersactivity
import com.example.mannig.activities.tasklistactivity
import com.example.mannig.models.Board
import com.example.mannig.models.User
import com.example.mannig.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlin.math.log


class firestoreclass {
    private val mfirestore = FirebaseFirestore.getInstance()

    fun registeruser(activity: Signup,userInfo: com.example.mannig.models.User){
        mfirestore.collection(Constants.USERS)
            .document(getCurrentuserid())
            .set(userInfo, SetOptions.merge()).addOnSuccessListener {
              activity.userRegisterSuccess()
            }


    }
    fun createboard(activity: Createboardacitivity,board: Board){
                mfirestore.collection(Constants.BOARDS)
                    .document()
                    .set(board, SetOptions.merge())
                    .addOnSuccessListener {
                        Log.e(activity.javaClass.simpleName, "Board Create Success")
                        Toast.makeText(activity, "Board Created successfully!", Toast.LENGTH_SHORT).show()
                    activity.boardcreatesuccess()
                    }.addOnFailureListener{
                        exception->
                        Log.e(
                            activity.javaClass.simpleName,
                            "error while creating board",
                            exception
                        )
                    }
    }
    fun getboardlist(activity: MainActivity)
    {mfirestore.collection(Constants.BOARDS)
        .whereArrayContains(Constants.ASSIGNED_TO,getCurrentuserid())
        .get()
        .addOnSuccessListener {
            document ->
                Log.i(activity.javaClass.simpleName,document.documents.toString())
            val boardlist: ArrayList<Board> = ArrayList()
           for(i in document.documents){
    val board = i.toObject(Board::class.java)!!
    board.documentId=i.id
    boardlist.add(board)
}
            activity.populateboardlist(boardlist)
        }.addOnFailureListener{
            e->
            Log.e(activity.javaClass.simpleName,"Error while creating a board",e)
        }
    }
    fun addupdatetasklist(activity: Activity,board: Board){
        val tasklisthashmap = HashMap<String,Any>()
    tasklisthashmap[Constants.TASK_LIST] = board.tasklist

        mfirestore.collection(Constants.BOARDS)
            .document(board.documentId)
            .update(tasklisthashmap
            )
            .addOnSuccessListener {
                Log.e(activity.javaClass.simpleName,"tasklist updated successfully.")
                if(activity is tasklistactivity)
                activity.addupdatetasklistsuccess()
                else if (activity is carddetail)
                    activity.addupdatetasklistsuccess()


            }.addOnFailureListener{
                    exception->


                Log.e(activity.javaClass.simpleName,"error while creating a board",exception)
            }

    }
    fun updateuserprofiledata(activity: Activity, userHashMap: HashMap<String,Any>){
        mfirestore.collection(Constants.USERS)
            .document(getCurrentuserid())
            .update(userHashMap)
            .addOnSuccessListener {
                Log.i(activity.javaClass.simpleName, "profile data update")
                Toast.makeText(activity, "profile updated successfully!", Toast.LENGTH_SHORT).show()
                when(activity ){
                    is MainActivity->{
                        activity.tokenupdatesucess()
                    }
                    is MyProfileActivity->{
                        activity.profileupdatesuccess()


                    }
                }


            }.addOnFailureListener{
                e->
                Log.e(
                    activity.javaClass.simpleName,
                    "error while creating a board",
                    e
                )
                Toast.makeText(activity,"error updating profile",Toast.LENGTH_SHORT).show()
            }

    }
    fun Loaduserdata(activity: Activity,readBoardslist:Boolean = false){
        mfirestore.collection(Constants.USERS)
            .document(getCurrentuserid())
            .get().addOnSuccessListener { document->
                Log.e(activity.javaClass.simpleName, document.toString())
              val Loggedinuser = document.toObject(User::class.java)
               if(Loggedinuser!=null)
                   when(activity){
                       is Signinactivity->{
                           activity.signInsuccess(Loggedinuser)

                       }
                       is MainActivity->{
                           activity.updatenavigationuserdetails(Loggedinuser,readBoardslist)
                       }
                       is MyProfileActivity->{
                           activity.setuserdatainui(Loggedinuser)

                       }
                   }

            }
    }

    fun getCurrentuserid(): String{
        var currentuser = FirebaseAuth.getInstance().currentUser
        var currentuserid = " "

            if (currentuser != null) {
                currentuserid=currentuser.uid
            }

        return currentuserid
    }
    fun getboarddetails(activity: tasklistactivity,documentId: String) {
        mfirestore.collection(Constants.BOARDS)
            .document(documentId)
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())
                val board = document.toObject(Board::class.java)!!
                board.documentId = documentId

               activity.boarddetails(board)
            }.addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "Error while creating a board", e)
            }
    }
    fun getassignedlistdetails(activity:Activity,assignedto:ArrayList<String>){
        mfirestore.collection(Constants.USERS)
            .whereIn(Constants.ID,assignedto)
            .get()
            .addOnSuccessListener {
                document->

                Log.e(activity.javaClass.simpleName,document.documents.toString())
            val userslist:ArrayList<User> = ArrayList()
                for (i in document.documents){
                    val user = i.toObject(User::class.java)!!
                    userslist.add(user)

                }
                if (activity is membersactivity)
                activity.setupmemberlist(userslist)
                else if (activity is tasklistactivity)
                    activity.boardmemebersdeatil(userslist)
            }.addOnFailureListener {
                  e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "error while creating board.",
                    e
                )

             }
    }
    fun getuserdetails(activity: membersactivity,email: String)
    {
        mfirestore.collection(Constants.USERS)
            .whereEqualTo(Constants.EMAIL,email)
            .get()
            .addOnSuccessListener {
                document->
                if (document.documents.size>0){
                    val user  = document.documents[0].toObject(User::class.java)!!
                    activity.memeberdetails(user)
                }else{
                    activity.showErrorSnackBar("No such member found")
                }
            }
            .addOnFailureListener {e->
                Log.e(
                    activity.javaClass.simpleName,
                    "error while getting user details",
                    e
                )

            }

    }
    fun assignedmembertoboard(activity: membersactivity,board: Board,user: User){


        val assignedtohashmap = HashMap<String,Any>()
        assignedtohashmap[Constants.ASSIGNED_TO] = board.assignedto
        mfirestore.collection(Constants.BOARDS)
            .document(board.documentId)
            .update(assignedtohashmap)
            .addOnSuccessListener {
                activity.memberassignedsuccess(user)
            }
            .addOnFailureListener { e->
                Log.e(
                    activity.javaClass.simpleName,"Error while creating a board",
                    e
                )
            }




    }

    fun assignedmembertoboard(activity: tasklistactivity, board: ArrayList<String>) {

    }
}