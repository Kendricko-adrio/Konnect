package edu.bluejack20_2.Konnect.viewmodels

import edu.bluejack20_2.Konnect.models.User
import edu.bluejack20_2.Konnect.repositories.UserRepository

class EditProfileViewModel {

    private val TAG = "EDIT_PROFILE_VM"

    suspend fun getUserByDocument(id: String): User {
        val user = UserRepository.getUserByDocument(id)
        if(user != null) {
            return user
        }
        return User()
    }

    suspend fun updateUserProfile(user: User) {
        UserRepository.updateUserProfile(user)
    }
}