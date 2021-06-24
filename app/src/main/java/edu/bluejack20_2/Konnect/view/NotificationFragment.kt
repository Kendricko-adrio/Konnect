package edu.bluejack20_2.Konnect.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.adapters.NotificationAdapter
import edu.bluejack20_2.Konnect.models.Notification
import edu.bluejack20_2.Konnect.models.User
import edu.bluejack20_2.Konnect.services.DateUtil
import edu.bluejack20_2.Konnect.viewmodels.NotificationViewModel
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.coroutines.launch

class NotificationFragment : Fragment() {

    companion object {
        fun newInstance() = NotificationFragment()
    }
    private val TAG = "NOTIF_FRAGMENT"
    private lateinit var viewModel: NotificationViewModel
    private lateinit var user: User

    private var notificationAdapter = NotificationAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)

        lifecycleScope.launch {
            user = viewModel.getCurrentUser()
            viewModel.listenToNewNotification(user.id)

            viewModel.getNotificationList().observe(viewLifecycleOwner, Observer {
                // Update UI
                initRecyclerView()
                // sort the data here
                val list: MutableList<Notification> = it
                val sortedList = list.sortedByDescending { it.createdAt }
                notificationAdapter.submitData(sortedList)
            })
        }
    }

    private fun initRecyclerView() {
        notification_recycler_view.apply {
            if(activity != null) {
                layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                notificationAdapter = NotificationAdapter()
                adapter = notificationAdapter
            }
            else {
                Log.wtf(TAG, "Context null")
            }
        }
    }

}