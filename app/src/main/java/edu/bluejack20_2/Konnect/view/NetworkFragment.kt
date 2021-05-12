package edu.bluejack20_2.Konnect.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        // TODO: Use the ViewModel
    }

}