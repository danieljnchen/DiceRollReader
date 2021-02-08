package com.cs4261.dicerollreader

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_ui)

        val loginButton = findViewById<Button>(R.id.loginButton)

        val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())

        loginButton?.setOnClickListener() {
            startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN
            )
        }
    }

    // [START auth_fui_result]
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            //val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val uid = FirebaseAuth.getInstance().currentUser?.uid
                val email = FirebaseAuth.getInstance().currentUser?.email
                Log.i("debug", "Logged in as ${email}")

                val intent = Intent(this, RollHistory::class.java)
                intent.putExtra("uid", uid)
                intent.putExtra("email", email)
                startActivity(intent)
            } else {
                Log.w("debug","Log in failed")
            }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 123
    }
}