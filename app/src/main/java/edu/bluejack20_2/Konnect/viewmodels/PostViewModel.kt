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
}