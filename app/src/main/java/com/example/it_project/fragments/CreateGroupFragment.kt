package com.example.it_project.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.it_project.R
import com.example.it_project.activities.CreateTestActivity
import com.example.it_project.activities.GroupsActivity
import com.example.it_project.models.User
import com.example.it_project.utilities.createGroupIDWithName
import com.example.it_project.utilities.createTestIDWithName
import com.example.it_project.utilities.initFirebase
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
            NEW_GROUP = edit_text_group_name.text.toString()
            val intentCreateGroup = Intent(activity, GroupsActivity::class.java)
            intentCreateGroup.putExtra("GroupName", edit_text_group_name.text.toString())
            startActivity(intentCreateGroup)
            //activity?.finish()
            fragmentManager?.beginTransaction()?.remove(this@CreateGroupFragment)?.commit()
            var groupName = edit_text_group_name.text.toString()
            val creatorNameListener = object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    USER = snapshot.getValue(User::class.java)
                    var name = USER?.name
                    var secName = USER?.secName
                    var creatorName = "${name} ${secName}"
                    createGroupIDWithName(groupName, 0, creatorName)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            }
            DATABASE_ROOT_USER.addValueEventListener(creatorNameListener)
        }
        button_exit_create_group_name.setOnClickListener {
            fragmentManager?.beginTransaction()?.remove(this@CreateGroupFragment)?.commit()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_group, container, false)
    }
}