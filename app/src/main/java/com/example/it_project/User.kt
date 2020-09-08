package com.example.it_project

open class User {
    var id: String? = null
    var name: String? = null
    var secName: String? = null
    var email: String? = null

    constructor() {}

    constructor(
        id: String?,
        name: String?,
        secName: String?,
        email: String?
        ) {
        this.id = id
        this.name = name
        this.secName = secName
        this.email = email
    }
}