package edu.bluejack20_2.Konnect.viewmodels

import edu.bluejack20_2.Konnect.models.Skill
import edu.bluejack20_2.Konnect.repositories.SkillRepository

class SkillInputViewModel {
    suspend fun addSkill(skill: Skill, userId: String) {
        SkillRepository.addSkill(skill, userId)
    }

    suspend fun deleteSkill(skillId: String, userId: String) {
        SkillRepository.deleteSkill(skillId, userId)
    }
}