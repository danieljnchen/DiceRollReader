package com.cs4261.dicerollreader

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class RollHistory : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roll_history)
        setSupportActionBar(findViewById(R.id.toolbar))

        var email = getIntent().getStringExtra("email")
        val toast = Toast.makeText(this, "Logged in as ${email}", Toast.LENGTH_LONG)
        toast.show()

        val rollHistoryTitle = findViewById<TextView>(R.id.rollHistoryTitle)
        rollHistoryTitle.setText("Roll History for ${email}")

        var uid = getIntent().getStringExtra("uid")
        if (uid == null) {
            Log.w("debug", "Null user string")
            // if user string is null, something is wrong -- go back to login screen
            startActivity(Intent(this, MainActivity::class.java))
        }

        val logoutButton = findViewById<Button>(R.id.logoutButton)
        logoutButton?.setOnClickListener() {
            startActivity(Intent(this, MainActivity::class.java))
        }

        val rollDisplay = findViewById<RecyclerView>(R.id.rollDisplay)
        val layoutManager = LinearLayoutManager(this)

        val database = Firebase.database.reference
        val userRef = database.child("userrolls").child("users").child(uid as String)
        var userRolls : ArrayList<Int> = arrayListOf<Int>()

        rollDisplay.layoutManager = layoutManager
        val adapter = CustomAdapter(userRolls)
        rollDisplay.adapter = adapter

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post : List<Int>? = dataSnapshot.getValue<List<Int>>()
                userRolls.clear()
                if (post != null) {
                    userRolls.addAll(post)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("debug", "loadPost:onCancelled", error.toException())
            }
        }
        userRef.addValueEventListener(postListener)
    }
}

class CustomAdapter(private val dataSet: ArrayList<Int>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView

        init {
            // Define click listener for the ViewHolder's View.
            textView = view.findViewById(R.id.rollRowText)
        }
    }

    // Create new views (invoked by the layout manager)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.roll_row, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.text = dataSet?.get(position).toString()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}