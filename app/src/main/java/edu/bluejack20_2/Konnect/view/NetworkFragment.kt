package edu.bluejack20_2.Konnect.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.adapters.ConnectionListAdapter
import edu.bluejack20_2.Konnect.viewmodels.NetworkViewModel

class NetworkFragment : Fragment() {

    companion object {
        fun newInstance() = NetworkFragment()
    }

    private lateinit var viewModel: NetworkViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_network, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NetworkViewModel::class.java)

        val clayout = view?.findViewById<ConstraintLayout>(R.id.cl_manage_network)
        val invitations = view?.findViewById<ConstraintLayout>(R.id.cl_invitation)

        clayout?.setOnClickListener {
            Toast.makeText(context, "keklick", Toast.LENGTH_SHORT).show()
        }

        invitations?.setOnClickListener{
            startActivity(Intent(context, InvitationsConnectionActivity::class.java))
        }

        view?.let { loadRecycleView(viewModel, it) }

    }

    @SuppressLint("WrongConstant")
    private fun loadRecycleView(viewModel: NetworkViewModel, view: View){
        var rv = view.findViewById<RecyclerView>(R.id.rv_connection_list)
        var linearLayout = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        linearLayout.stackFromEnd = true
        rv.layoutManager = linearLayout
        rv.setHasFixedSize(true)
        viewModel.loadConnection()
        viewModel.getConnection().observe(viewLifecycleOwner, Observer {
            rv.adapter = ConnectionListAdapter(it)
        })
    }

}