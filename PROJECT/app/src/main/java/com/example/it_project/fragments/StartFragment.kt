package com.example.it_project.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.it_project.Communicator
import com.example.it_project.R
import com.example.it_project.activities.MainActivity
import com.example.it_project.utilities.invokeNewActivity
import kotlinx.android.synthetic.main.fragment_start.view.*

class StartFragment : Fragment() {

    private lateinit var communicator: Communicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    var inputText: String? = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView =  inflater.inflate(R.layout.fragment_start, container, false)
        inputText = arguments?.getString("input_txt")
        communicator = activity as Communicator
        //rootView.fragment_text_view.text = inputText
        /*rootView.save.setOnClickListener {
            communicator.passData(rootView.fragment_text_view.text.toString())
        }*/
        rootView.button_no.setOnClickListener {
            fragmentManager?.beginTransaction()?.remove(this@StartFragment)?.commit()
            var intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
        rootView.button_yes.setOnClickListener {
            communicator.passData(ArrayList<String>())
        }
        return rootView
    }
}