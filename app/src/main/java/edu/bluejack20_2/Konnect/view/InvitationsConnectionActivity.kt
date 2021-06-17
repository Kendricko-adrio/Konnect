package edu.bluejack20_2.Konnect.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_2.Konnect.R
import edu.bluejack20_2.Konnect.adapters.InvitationsConnectionAdapter
import edu.bluejack20_2.Konnect.adapters.InvitationsConnectionHolder
import edu.bluejack20_2.Konnect.base.BaseActivity
import edu.bluejack20_2.Konnect.viewmodels.InvitationsConnectionViewModel

class InvitationsConnectionActivity : BaseActivity() {
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        changeFont()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invitations_connection)

        val viewModel = ViewModelProvider(this).get(InvitationsConnectionViewModel::class.java)

        val rv_invite = findViewById<RecyclerView>(R.id.rv_invitations_connections)
        rv_invite.layoutManager = LinearLayoutManager(applicationContext, LinearLayout.VERTICAL, false)
        rv_invite.setHasFixedSize(true)

        viewModel.loadInvitations()
        viewModel.getInvitationsList().observe(this, Observer {
            rv_invite.adapter = InvitationsConnectionAdapter(it)
        })

    }
}