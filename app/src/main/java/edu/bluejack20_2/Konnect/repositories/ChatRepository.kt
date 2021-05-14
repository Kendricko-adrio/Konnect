package edu.bluejack20_2.Konnect.repositories

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import edu.bluejack20_2.Konnect.models.Chat
import kotlinx.coroutines.tasks.await

object ChatRepository{

    private val db = FirebaseFirestore.getInstance()

    fun getLastMessage(header: String): Task<QuerySnapshot>{
        val ref = db.collection("chat_rooms").document(header)
        return db.collection("chat_detail")
            .whereEqualTo("header", ref)
            .limit(1)
            .orderBy("created_at", Query.Direction.ASCENDING)
            .get()
    }

    fun loadLastChat(): Task<QuerySnapshot>{
        val ref = db.collection("users").document(FirebaseAuth.getInstance().currentUser.uid)
//        Log.wtf("test", FirebaseAuth.getInstance().currentUser.uid)
        return db.collection("chat_rooms")
                .whereArrayContains("members", ref)
                .get()
    }

    fun listenChatText(header: String): Query{
        val ref = db.collection("chat_rooms").document(header)
        return db.collection("chat_detail")
            .whereEqualTo("header", ref)
            .orderBy("created_at", Query.Direction.ASCENDING)
    }

    fun setChatText(map: Map<String, Any>){
        FirebaseFirestore.getInstance().collection("chat_detail").add(map)
    }

    suspend fun loadChatText(header: String): MutableList<Chat>{

        val list = mutableListOf<Chat>()

        val ref = db.collection("chat_rooms").document(header)
        val data = db.collection("chat_detail")
            .whereEqualTo("header", ref)
            .orderBy("created_at", Query.Direction.ASCENDING).get().await()

        for(doc in data){
            val chat = Chat()
            chat.text = doc["text"].toString()
            chat.from = doc["from"] as DocumentReference
            chat.to = doc["to"] as DocumentReference
            chat.created_at = doc["created_at"] as Timestamp
            list.add(chat)
        }

        return list
    }

}