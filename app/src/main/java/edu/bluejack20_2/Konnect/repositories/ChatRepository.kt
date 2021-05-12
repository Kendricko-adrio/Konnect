package edu.bluejack20_2.Konnect.repositories

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import edu.bluejack20_2.Konnect.models.Chat

object ChatRepository{

    private val db = FirebaseFirestore.getInstance()

    fun loadConnection(): Task<QuerySnapshot>{
//        Log.wtf("test", FirebaseAuth.getInstance().currentUser.uid)
        return db.collection("chat_rooms")
                .whereArrayContains("members", FirebaseAuth.getInstance().currentUser.uid)
                .get()

    }

}