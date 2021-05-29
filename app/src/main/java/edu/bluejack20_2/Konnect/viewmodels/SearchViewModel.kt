package edu.bluejack20_2.Konnect.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentReference
import edu.bluejack20_2.Konnect.models.SearchData
import edu.bluejack20_2.Konnect.repositories.UserRepository
import org.w3c.dom.Document

class SearchViewModel: ViewModel() {
//    private val stringInput = MutableLiveData<MutableList<DocumentReference>>()
    private val stringInput = MutableLiveData<MutableList<SearchData>>()
    fun getAllUser(): MutableLiveData<MutableList<SearchData>> {
        return stringInput
    }

    private val string = MutableLiveData<String>()

    fun getString(): MutableLiveData<String>{
        return string
    }

    fun setAllUser(){
        UserRepository.searchAllUser().get().addOnSuccessListener {
            val list = mutableListOf<SearchData>()
            if (it != null) {
                for (doc in it){
                    val name = doc["name"].toString()
                    val photoUrl = doc["photoUrl"].toString()
                    val item = SearchData(name, photoUrl, doc.reference)

                    list.add(item)
                }
                stringInput.value = list
            }
        }
    }

    fun setString(str: String){

        string.value = str

//        UserRepository.searchByName(str).addSnapshotListener{it, e ->
//            if(e != null){
//                return@addSnapshotListener
//            }
//            val list = mutableListOf<DocumentReference>()
//            if (it != null) {
//                for (doc in it){
//                    list.add(doc.reference)
//                }
//                stringInput.value = list
//            }
//        }



    }

}