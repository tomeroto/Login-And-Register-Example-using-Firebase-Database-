package com.tongameyerogatman.logintest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.*
import com.tongameyerogatman.logintest.Model.User
import android.content.Intent

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var userDatabaseReference: DatabaseReference

    private var emailEditText : EditText? = null
    private var passwordEditText : EditText? = null

    private var submitButton : Button? = null
    private var registerButton : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userDatabaseReference = FirebaseDatabase.getInstance().getReference("users")

        // widget pairing...
        emailEditText = findViewById(R.id.emailEditText)
        emailEditText?.setText(intent.getStringExtra("logged_in_email"))
        passwordEditText = findViewById(R.id.passwordEditText)
        submitButton = findViewById(R.id.submitButton)
        registerButton = findViewById(R.id.registerButton)

        submitButton!!.setOnClickListener(this)
        registerButton!!.setOnClickListener(this)
    }

    // Click Listener Method
    override fun onClick(v: View) {
        when (v.id) {
            R.id.submitButton -> {


                val inputEmail = emailEditText?.text.toString().trim()
                val inputPassword = passwordEditText?.text.toString().trim()

                if(!inputEmail.isNullOrBlank() && !inputPassword.isNullOrBlank()){

                    submitButton?.setText("Wait")
                    submitButton?.setEnabled(false)

                    userDatabaseReference.orderByChild("email").equalTo(inputEmail).addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {

                            var isLoginMatch : Boolean? = false
                            var userFullname : String? = null
                            var userEmail : String? = null
                            var userPassword : String? = null

                            for (userSnapshot in dataSnapshot.children) {
                                val user = userSnapshot.getValue(User::class.java)

                                userFullname = user!!.fullname
                                userEmail = user!!.email
                                userPassword = user!!.password

                                if(userPassword == inputPassword){
                                    isLoginMatch = true
                                    break
                                }
                            }

                            if(isLoginMatch == true){
                                val intent = Intent(this@MainActivity, MapsActivity::class.java)
                                intent.putExtra("logged_in_fullname", userFullname)
                                startActivity(intent)
                            }else{
                                val builder = AlertDialog.Builder(this@MainActivity)
                                builder.setTitle(android.R.string.dialog_alert_title)
                                builder.setMessage(R.string.invalid_login_message)
                                builder.setPositiveButton(android.R.string.ok) { dialog, which ->
                                    dialog.dismiss()
                                }
                                builder.show()
                            }

                            submitButton?.setText(R.string.submit)
                            submitButton?.setEnabled(true)
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Getting user failed
                            val builder = AlertDialog.Builder(this@MainActivity)
                            builder.setTitle(android.R.string.dialog_alert_title)
                            builder.setMessage(databaseError.message)
                            builder.setPositiveButton(android.R.string.ok) { dialog, which ->
                                Toast.makeText(this@MainActivity,
                                    android.R.string.yes, Toast.LENGTH_SHORT).show()
                            }
                            builder.show()

                            submitButton?.setText(R.string.submit)
                            submitButton?.setEnabled(true)
                        }
                    })
                }else{
                    Toast.makeText(this@MainActivity, "Email or Password cannot be empty!", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.registerButton -> {
                val intent = Intent(this@MainActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }


}
