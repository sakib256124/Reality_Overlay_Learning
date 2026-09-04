package com.rola.app.global.community

import com.rola.app.domain.model.CommunityPost
import com.rola.app.domain.model.DiscussionThread
import com.rola.app.domain.model.LearningGroup
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommunityManager @Inject constructor() {
    fun feed(
        posts: List<CommunityPost>,
        languageCode: String? = null,
        topic: String? = null,
    ): List<CommunityPost> = posts
        .filter { languageCode == null || it.languageCode == languageCode }
        .filter { topic == null || it.tags.any { tag -> tag.equals(topic, ignoreCase = true) } }
        .sortedByDescending { it.createdAt }

    fun discussionHealth(thread: DiscussionThread): String = when {
        thread.messageCount >= 50 -> "Highly active"
        thread.messageCount >= 10 -> "Active"
        else -> "Starting"
    }

    fun internationalGroups(
        groups: List<LearningGroup>,
        countryCode: String,
    ): List<LearningGroup> = groups
        .filter { countryCode in it.countryCodes || it.countryCodes.size > 1 }
        .sortedByDescending { it.memberIds.size }
}
