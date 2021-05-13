package edu.bluejack20_2.Konnect.repositories

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import edu.bluejack20_2.Konnect.models.Chat

object ChatRepository{

    private val db = FirebaseFirestore.getInstance()

    fun getLastMessage(header: String): Task<QuerySnapshot>{
        val ref = db.collection("chat_rooms").document(header)
        return db.collection("chat_detail")
            .whereEqualTo("header", ref)
            .limit(1)
            .get()
    }

    fun loadLastChat(): Task<QuerySnapshot>{
        val ref = db.collection("users").document(FirebaseAuth.getInstance().currentUser.uid)
//        Log.wtf("test", FirebaseAuth.getInstance().currentUser.uid)
        return db.collection("chat_rooms")
                .whereArrayContains("members", ref)
                .get()
    }

    fun loadChatText(header: String): Query{
        val ref = db.collection("chat_rooms").document(header)
        return db.collection("chat_detail")
            .whereEqualTo("header", ref)
            .orderBy("created_at", Query.Direction.ASCENDING)

    }


}