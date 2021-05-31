package edu.bluejack20_2.Konnect.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.MetadataChanges
import edu.bluejack20_2.Konnect.models.ChatDetail
import edu.bluejack20_2.Konnect.repositories.ChatRepository
import org.w3c.dom.Document

class ChatListViewModel : ViewModel() {

    private val chatList = MutableLiveData<MutableList<ChatDetail>>()

    private val fbUser = FirebaseAuth.getInstance().currentUser

    fun getChatList(): MutableLiveData<MutableList<ChatDetail>> {
        return chatList
    }

    fun loadNewChat(){
        ChatRepository.loadRoomChat().addOnSuccessListener {
            val connectionList = ArrayList<ChatDetail>()
            for(doc in it){
                ChatRepository.getLastMessage(doc.id).addSnapshotListener{data, e ->
                    if(e != null){
                        return@addSnapshotListener
                    }

                    for (dc in data!!.documentChanges) {
                        when (dc.type) {
                            DocumentChange.Type.ADDED -> {
//                                Log.wtf("test", "added city: ${dc.document.data}")
                                val chatDetail = ChatDetail()
                                chatDetail.lastMessage = dc.document.data["text"].toString()
                                chatDetail.chatRoomDoc = doc.id
                                val from = dc.document.data["from"] as DocumentReference
                                val to = dc.document.data["to"] as DocumentReference
                                if(fbUser.uid != from.id){
                                    from.get().addOnSuccessListener {user ->
                                        Log.wtf("test", "added from city: ${dc.document.data}")
                                        chatDetail.connectionName = user["name"].toString()
                                        chatDetail.connectionDoc = user.id
                                        chatDetail.photoURL = user["photoUrl"].toString()
                                        connectionList += chatDetail
                                        chatList.value = connectionList
                                    }
                                }else{
                                    Log.wtf("test", "added to city: ${dc.document.data}")
                                    to.get().addOnSuccessListener {user ->
                                        Log.wtf("data yang null", user.toString())
                                        chatDetail.connectionName = user["name"].toString()
                                        chatDetail.connectionDoc = user.id
                                        chatDetail.photoURL = user["photoUrl"].toString()
                                        connectionList += chatDetail
                                        chatList.value = connectionList
                                    }
                                }

                            }
                            DocumentChange.Type.REMOVED -> {
                                for(data in chatList.value!!){
                                    val head = dc.document.data["header"] as DocumentReference
                                    if(data.chatRoomDoc == head.id){
                                        chatList.value?.remove(data)
                                        break
                                    }
                                }

                                Log.wtf("test", "Removed city: ${dc.document.data}")
                            }
                        }
                    }
                }
            }
        }
    }

    fun loadChatRoom() {

        ChatRepository.loadRoomChat().addOnSuccessListener { document ->
            val connectionList = ArrayList<ChatDetail>()
            for (doc in document) {
                val test = doc.data["members"] as List<DocumentReference>
//                Log.wtf("id dari docnya ", doc.id)
                for (id in test) {

                    if (id.id == fbUser.uid) {
                        continue
                    }


                    id.get().addOnSuccessListener { it ->


                        ChatRepository.getLastMessage(doc.id)
                            .addSnapshotListener{ lastmsg, error ->

                                if (error != null) {
                                    return@addSnapshotListener
                                }
                                if (lastmsg != null) {

                                    for (msg in lastmsg) {
                                        val chatDetail = ChatDetail()
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