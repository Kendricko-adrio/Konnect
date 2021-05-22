package edu.bluejack20_2.Konnect.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import edu.bluejack20_2.Konnect.adapters.ChatDetailAdapter
import edu.bluejack20_2.Konnect.models.ChatDetail
import edu.bluejack20_2.Konnect.repositories.ChatRepository

class ChatListViewModel : ViewModel() {

    private val chatList = MutableLiveData<MutableList<ChatDetail>>()

    private val fbUser = FirebaseAuth.getInstance().currentUser

    fun getChatList(): MutableLiveData<MutableList<ChatDetail>> {
        return chatList
    }

    fun loadChatRoom() {

        ChatRepository.loadLastChat().addOnSuccessListener { document ->
            val connectionList = ArrayList<ChatDetail>()
            for (doc in document) {
                val test = doc.data["members"] as List<DocumentReference>
                Log.wtf("id dari docnya ", doc.id)
                for (id in test) {

                    if (id.id == fbUser.uid) {
                        continue
                    }


                    id.get().addOnSuccessListener { it ->


                        ChatRepository.getLastMessage(doc.id)
                            .addSnapshotListener { lastmsg, error ->

                                if (error != null) {
                                    return@addSnapshotListener
                                }
                                if (lastmsg != null) {
                                    for (msg in lastmsg) {
                                        val chatDetail = ChatDetail()

                                        Log.wtf("apakah ada data", msg["text"].toString())
                                        chatDetail.lastMessage = msg["text"].toString()
                                        chatDetail.chatRoomDoc = doc.id
                                        chatDetail.connectionName = it["name"].toString()
                                        chatDetail.connectionDoc = id.id
                                        chatDetail.photoURL = it["photoUrl"].toString()
                                        connectionList += chatDetail
                                    }

                                    chatList.value = connectionList
                                }
                            }

                    }

                }

            }

        }
    }
}