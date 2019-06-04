package com.tongameyerogatman.logintest.Model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var fullname: String? = "",
    var email: String? = "",
    var password: String? = ""
)