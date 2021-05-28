package edu.bluejack20_2.Konnect.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.adapters.UserProfileConnectionRecyclerAdapter
import edu.bluejack20_2.Konnect.models.User
import edu.bluejack20_2.Konnect.viewmodels.UserProfileConnectionViewModel
import kotlinx.android.synthetic.main.activity_user_profile_connection.*
import kotlinx.coroutines.launch

class UserProfileConnectionActivity : AppCompatActivity() {

    private val TAG = "USER_PROF_CONNECT_ACT"
    private val viewModel = UserProfileConnectionViewModel()

    private lateinit var userId: String
    private lateinit var connections: MutableList<User>

    private var userConnectionAdapter : UserProfileConnectionRecyclerAdapter = UserProfileConnectionRecyclerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile_connection)

        loadIntentExtras()
        loadData()
    }

    private fun loadIntentExtras() {
        userId =  intent.getStringExtra("userId")!!
    }

    private fun loadData() {
        lifecycleScope.launch {
            connections = viewModel.getConnections(userId)
            Log.wtf(TAG, "Connections")
            Log.wtf(TAG, connections.toString())
            initRecyclerView()
            userConnectionAdapter.submitData(connections)
        }
    }

    private fun initRecyclerView() {
        user_profile_connection_recycler_view.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            userConnectionAdapter = UserProfileConnectionRecyclerAdapter()
            adapter = userConnectionAdapter
        }
    }

}