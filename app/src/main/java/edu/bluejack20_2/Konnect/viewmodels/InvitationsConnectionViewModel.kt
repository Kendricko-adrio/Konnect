package edu.bluejack20_2.Konnect.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import edu.bluejack20_2.Konnect.repositories.UserRepository

class InvitationsConnectionViewModel: ViewModel() {
    private val invitationsList = MutableLiveData<MutableList<DocumentReference>>()
    private val fbUser = FirebaseAuth.getInstance().currentUser

    fun getInvitationsList(): MutableLiveData<MutableList<DocumentReference>>{
        return invitationsList
    }

    fun loadInvitations(){
        UserRepository.getUserByDoc(fbUser.uid).addSnapshotListener{it, e ->
            if (e != null) {
                Log.w("error", "Listen failed.", e)
                return@addSnapshotListener
            }
            if(it?.get("inbound_request") == null) return@addSnapshotListener
            val inboundRequest = it["inbound_request"] as MutableList<DocumentReference>
            invitationsList.value = inboundRequest
        }
    }

}