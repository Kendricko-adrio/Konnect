package edu.bluejack20_2.Konnect.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack20_2.Konnect.repositories.UserRepository

class NetworkViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    private val connections = MutableLiveData<MutableList<DocumentReference>>()

    fun getConnection(): MutableLiveData<MutableList<DocumentReference>>{
        return connections
    }

    fun loadConnection(){
        val tempList = mutableListOf<DocumentReference>()
        UserRepository.getUserByDoc(FirebaseAuth.getInstance().currentUser.uid).get().addOnSuccessListener{

            val temp = it["connections_ref"] as List<DocumentReference>
            for(doc in temp){
                Log.wtf("data yang masuk", doc.toString())
                tempList.add(doc)
            }
            connections.value = tempList
        }
    }

//    fun loadConnection(){
//        val tempList = mutableListOf<DocumentReference>()
//        UserRepository.getUserByDoc(FirebaseAuth.getInstance().currentUser.uid).addSnapshotListener{it, e ->
//            if (e != null) {
//                Log.w("error", "Listen failed.", e)
//                return@addSnapshotListener
//            }
//            if(it?.get("connections_ref") == null) return@addSnapshotListener
//
//            val temp = it["connections_ref"] as List<DocumentReference>
//            for(doc in temp){
//                Log.wtf("data yang masuk", doc.toString())
//                tempList.add(doc)
//            }
//            connections.value = tempList
//        }
//    }


}