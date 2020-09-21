package com.example.it_project

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.it_project.activities.CreateTestActivity
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
            val intentCreateTest = Intent(activity, CreateTestActivity::class.java)
            intentCreateTest.putExtra("TestName", edit_text_test_name.text.toString())
            startActivity(intentCreateTest)
            fragmentManager?.beginTransaction()?.remove(this@CreateTestNameFragment)?.commit()
            createTestIDWithName(edit_text_test_name.text.toString())
        }
        button_exit_create_test_name.setOnClickListener {
            fragmentManager?.beginTransaction()?.remove(this@CreateTestNameFragment)?.commit()
        }
    }
}