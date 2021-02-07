package com.cs4261.dicerollreader

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random
import kotlin.random.nextInt

class RollHistory : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roll_history)
        setSupportActionBar(findViewById(R.id.toolbar))

        val logout_button = findViewById<Button>(R.id.logout_button)
        logout_button?.setOnClickListener() {
            startActivity(Intent(this, MainActivity::class.java))
        }

        val rollDisplay = findViewById<RecyclerView>(R.id.roll_display)
        val layoutManager = LinearLayoutManager(this)

        val listVals = ArrayList<String>()
        rollDisplay.layoutManager = layoutManager
        val adapter = CustomAdapter(listVals)
        rollDisplay.adapter = adapter

        val refresh_button = findViewById<Button>(R.id.refresh_button)
        refresh_button?.setOnClickListener() {
            listVals.clear()
            repeat(30) {
                listVals.add(Random.nextInt(0..100).toString())
            }
            adapter.notifyDataSetChanged()
        }
    }
}

class CustomAdapter(private val dataSet: ArrayList<String>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView

        init {
            // Define click listener for the ViewHolder's View.
            textView = view.findViewById(R.id.textView2)
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
        viewHolder.textView.text = dataSet[position]
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}