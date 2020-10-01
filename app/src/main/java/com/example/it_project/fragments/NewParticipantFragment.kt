package com.example.it_project.fragments

import android.content.Intent
import android.os.Bundle
import android.renderscript.Sampler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.it_project.R
import com.example.it_project.activities.ParticipantsActivity
import com.example.it_project.models.ParticipantModel
import com.example.it_project.models.User
import com.example.it_project.utilities.addParticipantToGroup
import com.example.it_project.utilities.initFirebase
import com.example.it_project.utilities.showToast
import com.example.it_project.values.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_create_group.*
import kotlinx.android.synthetic.main.fragment_new_participant.*


class NewParticipantFragment : AppCompatDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFirebase()
    }

    override fun onStart() {
        super.onStart()
        button_commit_add_participant.setOnClickListener {
            NEW_PARTICIPANT_ID = edit_text_participant_id.text.toString()
            var newParticipantID = NEW_PARTICIPANT_ID
            var currentGroupName = CURRENT_GROUP_NAME
            if((newParticipantID != null) && (currentGroupName != null)) {
                val idCheckListener = object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.child(newParticipantID).exists()) {
                            var user: User? = snapshot.child(newParticipantID).getValue(User::class.java)
                            var name = user?.name
                            var secName = user?.secName
                            var newParticipant = ParticipantModel("${name} ${secName}", newParticipantID)
                            addParticipantToGroup(newParticipant, newParticipantID, currentGroupName)
                            val intentWithExtras: Intent = Intent(activity, ParticipantsActivity::class.java)
                            intentWithExtras.putExtra("name", name!!)
                            intentWithExtras.putExtra("secName", secName)
                            intentWithExtras.putExtra("id", newParticipantID)
                            context?.startActivity(intentWithExtras)
                        }
                        else {
                            showToast(context, "Нет пользователя с таким ID")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        showToast(context, "Нет пользователя с таким ID")
                    }
                }
                REF_DATABASE_ROOT.child(NODE_USERS).addValueEventListener(idCheckListener)
            }
            //val intentParticipantsActivity = Intent(activity, ParticipantsActivity::class.java)
            //startActivity(intentParticipantsActivity)
        }

        button_exit_add_participant.setOnClickListener {
            fragmentManager?.beginTransaction()?.remove(this@NewParticipantFragment)?.commit()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_participant, container, false)
    }
}