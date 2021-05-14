package edu.bluejack20_2.Konnect.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import edu.bluejack20_2.Konnect.R
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

        var clayout = view?.findViewById<ConstraintLayout>(R.id.cl_manage_network)
        if (clayout != null) {
            clayout.setOnClickListener {
                Toast.makeText(context, "keklick", Toast.LENGTH_SHORT).show()
            }
        }
        // TODO: Use the ViewModel
    }

}