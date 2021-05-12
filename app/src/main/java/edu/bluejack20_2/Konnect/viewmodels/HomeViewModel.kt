package edu.bluejack20_2.Konnect.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import edu.bluejack20_2.Konnect.models.City
import edu.bluejack20_2.Konnect.repositories.CityRepository

class HomeViewModel : ViewModel() {
    val TAG = "HOME_VIEW_MODEL"

    fun getAllCities() {
        var cities : List<City> = emptyList()
        CityRepository.getAll().get()
                .addOnSuccessListener { document ->
                    for(doc in document) {
                        Log.wtf(TAG, doc.toString())
                    }
                }
    }
}