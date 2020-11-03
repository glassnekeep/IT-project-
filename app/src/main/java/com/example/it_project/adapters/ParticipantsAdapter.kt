package com.example.it_project.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.it_project.holders.ParticipantsHolder
import com.example.it_project.R
import com.example.it_project.activities.MainActivity
import com.example.it_project.models.ParticipantModel
import com.example.it_project.utilities.deleteParticipantFromGroup
import java.util.ArrayList

class ParticipantsAdapter(var mContext: Context,
                          var mActivity: Activity,
                          var groupName: String,
                          var participantList: ArrayList<ParticipantModel>): RecyclerView.Adapter<ParticipantsHolder>(), View.OnClickListener{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantsHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_participant, parent, false)
        return ParticipantsHolder(itemView)
    }

    override fun onBindViewHolder(holder: ParticipantsHolder, position: Int) {
        val model: ParticipantModel = participantList[position]
        holder.delete.setOnClickListener {
            //mActivity.openQuitDialog(model.userID)
            val dialog = AlertDialog.Builder(mActivity)
                .setTitle("Удаление участника группы")
                .setMessage("Вы уверены что хотите удалить учатника группы?")
                .setPositiveButton("Да") {dialog, which ->
                    deleteParticipantFromGroup(groupName, model.userID)
                }
                .setNegativeButton("Нет") { dialog, which ->

                }
                //.show()
            dialog.show()
            //deleteParticipantFromGroup(groupName, model.userID)
            notifyDataSetChanged()
        }
        holder.userName.text = "${model.userName}"
        holder.userID.text = "${model.userID}"
        holder.container.background = ContextCompat.getDrawable(mContext, R.drawable.category_item_background)
    }

    override fun getItemCount(): Int {
        return if (participantList != null) participantList.size else 0
    }

    override fun onClick(p0: View?) {

    }

    private fun openQuitDialog(id: String) {
        var quitDialog = AlertDialog.Builder(
            mContext
        )
        quitDialog.setTitle("Вы уверены, что хотите удалить данного пользвователя из группы?")

        quitDialog.setPositiveButton("Да"
        ) { dialog, which ->
            deleteParticipantFromGroup(groupName, id)
        }

        quitDialog.setNegativeButton("Нет"
        ) { dialog, which ->
        }
        quitDialog.show()
    }
}