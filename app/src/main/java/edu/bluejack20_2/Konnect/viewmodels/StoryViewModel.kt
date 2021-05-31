package edu.bluejack20_2.Konnect.viewmodels

import edu.bluejack20_2.Konnect.models.Story
import edu.bluejack20_2.Konnect.models.User
import edu.bluejack20_2.Konnect.repositories.StoryRepository
import edu.bluejack20_2.Konnect.repositories.UserRepository

class StoryViewModel {

    suspend fun getStoriesFromConnections(userId: String): HashMap<User, MutableList<Story>> {
        return StoryRepository.getStoriesFromConnections(userId)
    }

    suspend fun getStoryByUserId(userId: String): MutableList<Story> {
        return StoryRepository.getStoryByUserId(userId)
    }

    suspend fun getUserById(userId: String): User? {
        return UserRepository.getUserByDocument(userId)
    }

}