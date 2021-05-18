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
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.adapters.SearchDetailAdapter
import edu.bluejack20_2.Konnect.view.HomeActivity
import edu.bluejack20_2.Konnect.viewmodels.SearchViewModel

class SearchFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    @SuppressLint("WrongConstant")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)

        val rv_invite = view?.findViewById<RecyclerView>(R.id.rv_search)
        if (rv_invite != null) {
            rv_invite.layoutManager =
                LinearLayoutManager(context, LinearLayout.VERTICAL, false)
            rv_invite.setHasFixedSize(true)
            viewModel.getStringInput().observe(viewLifecycleOwner, Observer {
                rv_invite.adapter = SearchDetailAdapter(it)
            })
        }



    }

}