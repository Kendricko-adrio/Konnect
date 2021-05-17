package edu.bluejack20_2.Konnect.models

import android.provider.ContactsContract
import com.google.common.collect.ImmutableList
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import java.util.*

class Chat(
//        private val member: ImmutableList<String>,
//        private val messages: ImmutableList<Message>
) {
//    lateinit var members: List<String>
//    lateinit var messages: List<Map<String, String>>
//    private fun test(){
//        val caaaa = Message("a", "a", "a", "a")
//
//    }

    lateinit var from: DocumentReference
    lateinit var to: DocumentReference
    lateinit var created_at: Timestamp
    lateinit var text: String
    lateinit var fromPhoto: String
    lateinit var toPhoto: String
}