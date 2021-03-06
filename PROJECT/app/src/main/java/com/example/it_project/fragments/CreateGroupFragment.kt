package com.example.it_project.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.it_project.R
import com.example.it_project.activities.CreateTestActivity
import com.example.it_project.activities.GroupsActivity
import com.example.it_project.models.User
import com.example.it_project.utilities.createGroupIDWithName
import com.example.it_project.utilities.createTestIDWithName
import com.example.it_project.utilities.initFirebase
import com.example.it_project.utilities.showToast
import com.example.it_project.values.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_create_group.*
import kotlinx.android.synthetic.main.fragment_create_test_name.*
import kotlinx.android.synthetic.main.fragment_create_test_name.button_commit_create_test_name
import kotlinx.android.synthetic.main.fragment_create_test_name.button_exit_create_test_name

class CreateGroupFragment : AppCompatDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFirebase()
    }

    override fun onStart() {
        super.onStart()
        button_commit_create_group_name.setOnClickListener {
            //NEW_GROUP = edit_text_group_name.text.toString().trim()
            var groupName = edit_text_group_name.text.toString().trim()
            if(!groupName.isEmpty()){
                val creatorNameListener = object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var groupList = ArrayList<String>()
                        for (group in snapshot.child("groups").children) {
                            groupList.add(
                                group.child("group info").child("groupName")
                                    .getValue(String::class.java)!!
                            )
                        }
                        if (!groupList.contains(groupName)) {
                            USER = snapshot.child("users").child(CURRENT_UID)
                                .getValue(User::class.java)
                            var name = USER?.name
                            var secName = USER?.secName
                            var creatorName = "${name} ${secName}"
                            createGroupIDWithName(groupName, 0, creatorName)
                            val intentCreateGroup = Intent(activity, GroupsActivity::class.java)
                            intentCreateGroup.putExtra(
                                "GroupName",
                                edit_text_group_name.text.toString().trim()
                            )
                            startActivity(intentCreateGroup)
                            //activity?.finish()
                            fragmentManager?.beginTransaction()?.remove(this@CreateGroupFragment)
                                ?.commit()
                        } else {
                            Toast.makeText(
                                context,
                                "Уже есть группа с таким названием",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                }
                REF_DATABASE_ROOT.addListenerForSingleValueEvent(creatorNameListener)
            } else {
                showToast(context, "Не все поля заполнены!")
            }
        }
        button_exit_create_group_name.setOnClickListener {
            fragmentManager?.beginTransaction()?.remove(this@CreateGroupFragment)?.commit()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_group, container, false)
    }
}