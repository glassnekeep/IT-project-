package com.example.it_project

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.mikepenz.materialdrawer.AccountHeader

lateinit var NEW_USER: User
lateinit var AUTH: FirebaseAuth
lateinit var CURRENT_UID: String
lateinit var REF_DATABASE_ROOT: DatabaseReference
var USER: User? = null
lateinit var header: AccountHeader
const val NODE_USERS = "users"
const val CHILD_NAME = "name"
const val CHILD_SECOND_NAME = "secName"
const val EMAIL = "email"