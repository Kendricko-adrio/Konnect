package edu.bluejack20_2.Konnect.viewmodels

import androidx.lifecycle.ViewModel
import edu.bluejack20_2.Konnect.models.User
import edu.bluejack20_2.Konnect.repositories.UserRepository

class UserProfileViewModel: ViewModel() {

    private val TAG = "USER_PROFILE_VM"

    suspend fun getCurrentUser(): User {
        val user = UserRepository.getCurrentUser()
        if(user != null) {
            return user
        }
        return User()
    }

    suspend fun getUserByDocument(id: String): User {
        val user = UserRepository.getUserByDocument(id)
        if(user != null) {
            return user
        }
        return User()
    }
}