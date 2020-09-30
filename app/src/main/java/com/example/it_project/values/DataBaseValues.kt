package com.example.it_project.values

import com.example.it_project.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.mikepenz.materialdrawer.AccountHeader

var administrator: Long? = 12344545

lateinit var NEW_USER: User
lateinit var AUTH: FirebaseAuth
lateinit var CURRENT_UID: String
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var DATABASE_ROOT_USER: DatabaseReference
lateinit var DATABASE_ROOT_NEW_TEST: DatabaseReference
lateinit var DATABASE_ROOT_TEST_IDS: DatabaseReference
lateinit var DATABASE_ROOT_GROUP_IDS: DatabaseReference
lateinit var DATABASE_ROOT_NEW_GROUP: DatabaseReference
lateinit var TEST_NAME: String
var ADMIN_STATUS: String? = ""
var USER: User? = null
lateinit var header: AccountHeader
const val NODE_GROUP_INFO = "group info"
const val NODE_GROUP_IDS = "group IDs"
const val NODE_USERS = "users"
const val NODE_TEST = "tests"
const val NODE_GROUP = "groups"
const val NODE_TEST_IDS = "test IDs"
const val NODE_ID = "ID"
const val NODE_QUESTIONS = "questions"
const val NODE_ANSWERS = "answers"
const val CHILD_NAME = "name"
const val CHILD_SECOND_NAME = "secName"
const val EMAIL = "email"