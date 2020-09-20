package com.example.it_project

import com.example.it_project.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.mikepenz.materialdrawer.AccountHeader

lateinit var NEW_USER: User
lateinit var AUTH: FirebaseAuth
lateinit var CURRENT_UID: String
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var DATABASE_ROOT_USER: DatabaseReference
lateinit var DATABASE_ROOT_NEW_QUESTION: DatabaseReference
var USER: User? = null
lateinit var header: AccountHeader
const val NODE_USERS = "users"
const val NODE_TEST = "tests"
const val CHILD_NAME = "name"
const val CHILD_SECOND_NAME = "secName"
const val EMAIL = "email"