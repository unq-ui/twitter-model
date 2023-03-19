package org.unq

class IdGenerator {
    private var currentUserId = 0
    private var currentTweetId = 0

    fun nextUserId(): String = "u_${++currentUserId}"
    fun nextTweetId(): String = "t_${++currentTweetId}"
}