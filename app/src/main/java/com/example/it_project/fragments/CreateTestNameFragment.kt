package com.example.it_project.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.it_project.R
import com.example.it_project.activities.CreateTestActivity
import com.example.it_project.utilities.createTestIDWithName
import com.example.it_project.utilities.createTestWithName
import com.example.it_project.utilities.initFirebase
import com.example.it_project.values.CURRENT_TEST_ID
import com.example.it_project.values.CURRENT_TEST_NAME
import com.example.it_project.values.CURRENT_TEST_PRIVACY
import com.example.it_project.values.REF_DATABASE_ROOT
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_create_test_name.*

class CreateTestNameFragment : AppCompatDialogFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFirebase()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_test_name, container, false)
    }

    override fun onResume() {
        super.onResume()
        button_commit_create_test_name.setOnClickListener {
            CURRENT_TEST_NAME = edit_text_test_name.text.toString().trim()
            val listener = object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var listTests = ArrayList<String>()
                    for(privateTest in snapshot.child("private tests").children) {
                        listTests.add(privateTest.child("test name").getValue(String::class.java)!!)
                    }
                    for(publicTest in snapshot.child("public test").children) {
                        listTests.add(publicTest.child("test name").getValue(String::class.java)!!)
                    }
                    if(!listTests.contains(CURRENT_TEST_NAME)) {
                        var testName = edit_text_test_name.text.toString().trim()
                        var subject = spinner_subject.selectedItem.toString()
                        var privacy = spinner_privacy.selectedItem.toString()
                        var time = edit_text_time.text.toString().trim()
                        val intentCreateTest = Intent(activity, CreateTestActivity::class.java)
                        intentCreateTest.putExtra("TestName", edit_text_test_name.text.toString().trim())
                        startActivity(intentCreateTest)
                        fragmentManager?.beginTransaction()?.remove(this@CreateTestNameFragment)?.commit()
                        CURRENT_TEST_ID = createTestIDWithName(testName)
                        CURRENT_TEST_PRIVACY = privacy
                        createTestWithName(testName, subject, privacy, CURRENT_TEST_ID!!, time)
                    } else {
                        Toast.makeText(context, "Уже есть тест с таким названием", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            }
            REF_DATABASE_ROOT.addListenerForSingleValueEvent(listener)
        }
        button_exit_create_test_name.setOnClickListener {
            fragmentManager?.beginTransaction()?.remove(this@CreateTestNameFragment)?.commit()
        }
    }
}