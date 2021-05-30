package edu.bluejack20_2.Konnect.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.adapters.PostRecyclerAdapter
import edu.bluejack20_2.Konnect.viewmodels.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var postAdapter: PostRecyclerAdapter = PostRecyclerAdapter()

    private val TAG: String = "HOME_FRAGMENT"

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
        tv_nopost_aware.visibility = View.GONE
        // Code ViewModel here
        lifecycleScope.launch {
            val data = viewModel.getAllPosts()
            Log.wtf("data yang terstore ke rv", data.toString())
            if(data.size == 0){
                tv_nopost_aware.visibility = View.VISIBLE
                home_post_recycler_view.visibility = View.GONE
            }
            val users = viewModel.getAllUsers()
            initRecyclerView()
            postAdapter.submitData(data, users)
        }
    }

    private fun initRecyclerView() {
        home_post_recycler_view?.apply {
            if(activity != null) {
                layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                postAdapter = PostRecyclerAdapter()
                adapter = postAdapter
            }
            else {
                Log.wtf(TAG, "Context null")
            }
        }
    }
}