package org.unq

import java.time.LocalDateTime

class DraftUser(
    val username: String,
    val email: String,
    val password: String,
    val image: String,
    val backgroundImage: String
)

class DraftTweet(
    val userId: String,
    val content: String,
    val image: String?,
    val date: LocalDateTime = LocalDateTime.now()
)

class DraftReTweet(
    val userId: String,
    val tweetId: String,
    val content: String,
    val date: LocalDateTime = LocalDateTime.now()
)
class DraftReplyTweet (
    val userId: String,
    val tweetId: String,
    val content: String,
    val image: String?,
    val date: LocalDateTime = LocalDateTime.now()
)