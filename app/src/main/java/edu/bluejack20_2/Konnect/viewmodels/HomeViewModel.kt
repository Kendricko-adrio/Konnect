package edu.bluejack20_2.Konnect.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.QuerySnapshot
import edu.bluejack20_2.Konnect.models.ActivityPost
import edu.bluejack20_2.Konnect.models.City
import edu.bluejack20_2.Konnect.models.Story
import edu.bluejack20_2.Konnect.models.User
import edu.bluejack20_2.Konnect.repositories.ActivityPostRepository
import edu.bluejack20_2.Konnect.repositories.CityRepository
import edu.bluejack20_2.Konnect.repositories.StoryRepository
import edu.bluejack20_2.Konnect.repositories.UserRepository

class HomeViewModel : ViewModel() {
    val TAG = "HOME_VIEW_MODEL"

    suspend fun getAllCities(): List<City> {
        var res = mutableListOf<City>()
        try {
            lateinit var cities : QuerySnapshot
            cities = CityRepository.getAll()
            for (city in cities) {
                val c = city.toObject(City::class.java)
                c.id = city.id
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

    suspend fun getCurrentUser(): User {
        val user = UserRepository.getCurrentUser()
        if(user != null) {
            return user
        }
        return User()
    }

    suspend fun getAllUsers(): List<User> {
        return UserRepository.getAll()
    }

    suspend fun getStoriesFromConnections(userId: String): HashMap<User, MutableList<Story>> {
        return StoryRepository.getStoriesFromConnections(userId)
    }
}