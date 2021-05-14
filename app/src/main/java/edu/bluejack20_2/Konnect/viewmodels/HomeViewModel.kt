package edu.bluejack20_2.Konnect.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.getField
import edu.bluejack20_2.Konnect.models.ActivityPost
import edu.bluejack20_2.Konnect.models.City
import edu.bluejack20_2.Konnect.models.User
import edu.bluejack20_2.Konnect.repositories.ActivityPostRepository
import edu.bluejack20_2.Konnect.repositories.CityRepository
import edu.bluejack20_2.Konnect.repositories.UserRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class HomeViewModel : ViewModel() {
    val TAG = "HOME_VIEW_MODEL"

    suspend fun getAllCities(): List<City> {
        var res = mutableListOf<City>()
        try {
            val cities = CityRepository.getAll()
            for (city in cities) {
                val c = city.toObject(City::class.java)
                if (c != null) {
                    Log.wtf(TAG, c.name)
                    res.add(c)
                }
            }
            return res
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
        return emptyList()
    }

    suspend fun getAllPosts(): List<ActivityPost> {
        return ActivityPostRepository.getAll()
    }
}