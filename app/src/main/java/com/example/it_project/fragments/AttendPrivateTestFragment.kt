package com.example.it_project.fragments

import android.content.Intent
import android.os.Bundle
import android.renderscript.Sampler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import com.example.it_project.R
import com.example.it_project.activities.AttendingTestActivity
import com.example.it_project.utilities.initFirebase
import com.example.it_project.values.DATABASE_ROOT_NEW_GROUP
import com.example.it_project.values.DATABASE_ROOT_USER
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_attend_private_test.*

class AttendPrivateTestFragment : AppCompatDialogFragment() {

    private var testName: String? = ""

    private var groupList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFirebase()
        fillGroupList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_attend_private_test, container, false)
    }

    override fun onStart() {
        super.onStart()
        button_exit_attend_test.setOnClickListener {
            fragmentManager?.beginTransaction()?.remove(this@AttendPrivateTestFragment)?.commit()
        }

        button_commit_attend_test.setOnClickListener {
            var testId = edit_text_test_id.text.toString().trim()
            val listener = object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var found = false
                    for(group in groupList) {
                        for(test in snapshot.child(group).child("tests").children) {
                            var dataId = test.child("ID").getValue(String::class.java)
                            if(dataId == testId) {
                                found = true
                                val dialog = AlertDialog.Builder(activity!!)
                                    .setTitle("Подтверждение")
                                    .setMessage("Вы уверены, что хотите пройти данный тест?")
                                    .setPositiveButton("Да") {dialog, which ->
                                        var intent = Intent(activity, AttendingTestActivity::class.java)
                                        intent.putExtra("privacy", test.child("test info").child("privacy").getValue(String::class.java))
                                        intent.putExtra("testName", test.child("test name").getValue(String::class.java))
                                        intent.putExtra("subject", test.child("test info").child("subject").getValue(String::class.java))
                                        intent.putExtra("testCreator", test.child("test info").child("testCreator").getValue(String::class.java))
                                        intent.putExtra("time", test.child("test info").child("time").getValue(String::class.java))
                                        activity?.startActivity(intent)
                                        activity?.finish()
                                    }
                                    .setNegativeButton("Нет") { dialog, which ->
                                        found = false
                                    }
                                dialog.show()
                            }
                        }
                    }
                    if(!found) {
                        Toast.makeText(context, "Нет теста с таким Id, либо Вы не состоите в группе, для которой доступен этот тест", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            }
            DATABASE_ROOT_NEW_GROUP.addListenerForSingleValueEvent(listener)
        }
    }

    private fun fillGroupList() {
        val listener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(groupList.size > 1) {groupList.clear()}
                for(groupName in snapshot.children) {
                    var name = groupName.getValue(String::class.java)
                    groupList.add(name!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        DATABASE_ROOT_USER.child("in groups").addValueEventListener(listener)
    }

}