package edu.bluejack20_2.Konnect.viewmodels

import androidx.lifecycle.ViewModel
import edu.bluejack20_2.Konnect.models.ActivityPost
import edu.bluejack20_2.Konnect.models.User
import edu.bluejack20_2.Konnect.repositories.ActivityPostRepository
import edu.bluejack20_2.Konnect.repositories.UserRepository

class PostViewModel : ViewModel() {
    val TAG = "POST_VIEW_MODEL"

    suspend fun getCurrentUser(): User {
        val user = UserRepository.getCurrentUser()
        if(user != null) {
            return user
        }
        return User()
    }

    suspend fun addPost(post: ActivityPost, user: User) {
        ActivityPostRepository.addPost(post, user)
    }

    suspend fun getAllUserIDByUsername(tags: MutableList<String>): MutableList<String> {
        var results = mutableListOf<String>()
        var users: MutableList<User> = UserRepository.getAll()
        for(tag in tags) {
            results.add("")
            for(user in users)
                if(user.username == tag)
                    results.add(user.id)
        }
        return results
    }

    public fun getUserTagsFromConnection(tag: String, user: User): MutableList<User> {
        var connections = mutableListOf<User>()
        for(connection in user.connections) {
            if(connection.username.contains(tag)) {
                connections.add(connection)
            }
        }
        return connections
    }
}