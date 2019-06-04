package com.tongameyerogatman.logintest.Util

import android.util.Patterns

class Valid {
    companion object {
        fun isValidEmail(email : String): Boolean
                = email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}