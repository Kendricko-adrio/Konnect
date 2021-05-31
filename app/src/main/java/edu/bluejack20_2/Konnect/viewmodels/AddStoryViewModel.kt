package edu.bluejack20_2.Konnect.viewmodels

import edu.bluejack20_2.Konnect.models.Story
import edu.bluejack20_2.Konnect.repositories.StoryRepository

class AddStoryViewModel {

    suspend fun addStory(story: Story) {
        StoryRepository.addStory(story)
    }

}