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
import com.google.common.collect.ImmutableList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.adapters.ChatDetailAdapter
import edu.bluejack20_2.Konnect.adapters.ChatTabAdapter
import edu.bluejack20_2.Konnect.models.ChatDetail
import edu.bluejack20_2.Konnect.models.User
import edu.bluejack20_2.Konnect.repositories.ChatRepository
import edu.bluejack20_2.Konnect.repositories.UserRepository


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
        ChatRepository.loadConnection().addOnSuccessListener { document ->

            for (doc in document){
                val test: List<String> = doc.data["members"] as List<String>
                for(id in test)
                {
                    if(id.equals(fbUser.uid)) continue
                    val chatDetail = ChatDetail()
                    Log.wtf("data id", id)
                    UserRepository.getUserByDoc(id).addOnSuccessListener { document->
                        // insert data
                        chatDetail.connectionName = document["name"] as String
                        chatDetail.photoURL = document["photoURL"] as String
                        chatDetail.connectionDoc = id

                        val test = doc.data["messages"] as List<Map<String, String>>

                        chatDetail.lastMessage = test[test.lastIndex]["text"] as String
                        Log.wtf("last message ", chatDetail.lastMessage)
                        Log.wtf("data ", document["name"] as String)
                        connectionList += chatDetail
                        val adapter = ChatDetailAdapter(connectionList)
                        recyclerView.adapter = adapter
                        recyclerView.setHasFixedSize(true)
                    }
//                    Log.wtf("data ", id)
//                    val user = User(id)
//                    users += user
                }

            }

        }
        Log.wtf("data id", fbUser.photoUrl.toString())

        return view

    }

}