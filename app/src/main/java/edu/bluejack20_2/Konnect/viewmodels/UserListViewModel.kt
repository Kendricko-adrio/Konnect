package edu.bluejack20_2.Konnect.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import edu.bluejack20_2.Konnect.repositories.UserRepository

class UserListViewModel: ViewModel() {
    val connectionList = MutableLiveData<MutableList<DocumentReference>>()

    fun getList(): MutableLiveData<MutableList<DocumentReference>>{
        return connectionList
    }

    fun loadData(){
        val tempList = mutableListOf<DocumentReference>()
        UserRepository.getUserByDoc(FirebaseAuth.getInstance().currentUser.uid).get().addOnSuccessListener{

            val temp = it["connections_ref"] as List<DocumentReference>
            for(doc in temp){
                Log.wtf("data yang masuk", doc.toString())
                tempList.add(doc)
            }
            connectionList.value = tempList
        }
    }

}