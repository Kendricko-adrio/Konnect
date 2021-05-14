package edu.bluejack20_2.Konnect.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.adapters.PostRecyclerAdapter
import edu.bluejack20_2.Konnect.viewmodels.HomeViewModel
import edu.bluejack20_2.Konnect.viewmodels.SettingViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var postAdapter: PostRecyclerAdapter

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)


        // Code ViewModel here
        lifecycleScope.launch {
            val data = viewModel.getAllPosts()
            initRecyclerView()
            postAdapter.submitData(data)
        }

    }

    private fun initRecyclerView() {
        home_post_recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            postAdapter = PostRecyclerAdapter()
            adapter = postAdapter
        }
    }
}