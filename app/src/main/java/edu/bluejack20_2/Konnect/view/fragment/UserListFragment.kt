package edu.bluejack20_2.Konnect.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.adapters.ChatDetailAdapter
import edu.bluejack20_2.Konnect.adapters.UserAdapter
import edu.bluejack20_2.Konnect.adapters.UserListAdapter
import edu.bluejack20_2.Konnect.models.User
import edu.bluejack20_2.Konnect.repositories.ChatRepository
import edu.bluejack20_2.Konnect.repositories.UserRepository
import edu.bluejack20_2.Konnect.viewmodels.UserListViewModel
import kotlinx.android.synthetic.main.fragment_user_list.*


class UserListFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_list, container, false)

        val viewModel = ViewModelProvider(this).get(UserListViewModel::class.java)
        val recyclerView: RecyclerView = view.findViewById(R.id.rvShowConnection) as RecyclerView

        recyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayout.VERTICAL, false)

        viewModel.loadData()
        viewModel.getList().observe(viewLifecycleOwner, Observer {
            val adapter = UserListAdapter(it)
            recyclerView.adapter = adapter
            recyclerView.setHasFixedSize(true)
        })

        return view
    }

}