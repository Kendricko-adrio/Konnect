package edu.bluejack20_2.Konnect.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.adapters.ChatDetailAdapter
import edu.bluejack20_2.Konnect.models.ChatDetail
import edu.bluejack20_2.Konnect.repositories.ChatRepository


class ChatListFragment : Fragment() {


    private lateinit var fbUser: FirebaseUser

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    //    @SuppressLint("WrongConstant")
//    @SuppressLint("WrongConstant")
    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fbUser = FirebaseAuth.getInstance().currentUser

        val view = inflater.inflate(R.layout.fragment_chat_list, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.rvUserList) as RecyclerView

        recyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayout.VERTICAL, false)

        val connectionList = ArrayList<ChatDetail>()
        ChatRepository.loadLastChat().addOnSuccessListener { document ->

            for (doc in document){
                val test = doc.data["members"] as List<DocumentReference>
                Log.wtf("id dari docnya ", doc.id)
                for(id in test)
                {
                    if(id.id.equals(fbUser.uid)) continue
                    val chatDetail = ChatDetail()

                    id.get().addOnSuccessListener { it ->
                        chatDetail.chatRoomDoc = doc.id
                        chatDetail.connectionName = it["name"] as String
                        chatDetail.connectionDoc = id.id
                        chatDetail.photoURL = it["photoUrl"] as String

                        ChatRepository.getLastMessage(doc.id).addOnSuccessListener { lastmsg ->
                            Log.wtf("apakah ada data", lastmsg.isEmpty.toString())
                            for (msg in lastmsg){
                                Log.wtf("apakah ada data", msg["text"] as String)
                                chatDetail.lastMessage = msg["text"] as String
                            }
                            connectionList += chatDetail
                            val adapter = ChatDetailAdapter(connectionList)
                            recyclerView.adapter = adapter
                            recyclerView.setHasFixedSize(true)
                        }

                    }
                }

            }

        }
        Log.wtf("data id", fbUser.photoUrl.toString())

        return view

    }

}