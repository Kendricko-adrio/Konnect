package edu.bluejack20_2.Konnect.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.Konnect.R


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
                "a" to "aasd",
                "b" to "basd",
                "c" to "casd"
        )
        // test push ke branch
        // apakah aku berhasil jadi cabang nico
        FirebaseFirestore.getInstance().collection("Test").add(data)
    }
}