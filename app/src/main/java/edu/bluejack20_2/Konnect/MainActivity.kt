package edu.bluejack20_2.Konnect

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
// Write a message to the database
//        val database = FirebaseDatabase.getInstance()
//        val myRef = database.getReference("message")
//
//        myRef.setValue("Hello, World!")
        // Create a new user with a first and last name
        val data = hashMapOf(
                "a" to "a",
                "b" to "b",
                "c" to "c"
        )

        FirebaseFirestore.getInstance().collection("Test").add(data)
    }
}