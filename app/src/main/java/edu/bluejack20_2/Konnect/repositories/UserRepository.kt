package edu.bluejack20_2.Konnect.repositories

import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User

object UserRepository {

    private val db = FirebaseFirestore.getInstance()

    fun createUserFirebase(user: FirebaseUser) {
        val findDoc = db.collection("users").document(user.uid)
        findDoc.get()
                .addOnSuccessListener { document ->
                    if(!document.exists()){
                        // Create new user
                        val data = hashMapOf(
                                "name" to user.displayName,
                                "email" to user.email,
                                "phoneNumber" to user.phoneNumber,
                                "providerId" to user.providerId
                        )
                        db.collection("users").document(user.uid).set(data)
                    }else{
                        // User already exists
                        Log.wtf("test", "DocumentSnapshot data: ${document.get("name")}")
                    }
                }

    }
}