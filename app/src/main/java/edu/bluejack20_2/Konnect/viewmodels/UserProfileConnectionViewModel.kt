package edu.bluejack20_2.Konnect.viewmodels

import edu.bluejack20_2.Konnect.models.User
import edu.bluejack20_2.Konnect.repositories.UserRepository

class UserProfileConnectionViewModel {
    private val TAG = "USER_PROF_CONN_VM"

    suspend fun getConnections(id: String): MutableList<User> {
        return UserRepository.getConnections(id)
    }
}