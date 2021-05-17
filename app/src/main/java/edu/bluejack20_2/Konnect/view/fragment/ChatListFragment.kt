package edu.bluejack20_2.Konnect.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.adapters.ChatDetailAdapter
import edu.bluejack20_2.Konnect.models.ChatDetail
import edu.bluejack20_2.Konnect.repositories.ChatRepository
import edu.bluejack20_2.Konnect.viewmodels.ChatListViewModel


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
        val viewModel = ViewModelProvider(this).get(ChatListViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_chat_list, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.rvUserList) as RecyclerView

        recyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayout.VERTICAL, false)


        viewModel.loadChatRoom()
        viewModel.getChatList().observe(viewLifecycleOwner, Observer {
            val adapter = ChatDetailAdapter(it)
            recyclerView.adapter = adapter
            recyclerView.setHasFixedSize(true)
        })

//        Log.wtf("data id", fbUser.photoUrl.toString())

        return view

    }

}