package org.unq

import java.time.LocalDateTime

class User(
    val id: String,
    val username: String,
    var email: String,
    var password: String,
    var image: String,
    var backgroundImage: String,
    val following: MutableList<User> = mutableListOf(),
    val followers: MutableList<User> = mutableListOf()
)

open class Tweet(
    val id: String,
    val type: TweetType,
    val user: User,
    val content: String,
    val date: LocalDateTime = LocalDateTime.now(),
    val replies: MutableList<Tweet> = mutableListOf() ,
    val reTweets:  MutableList<Tweet> = mutableListOf(),
    val likes: MutableList<User> = mutableListOf(),
)

open class TweetType(val tweet: Tweet?, val image: String?) {
    open fun isReTweet() = false
    open fun isReplayTweet() = false
    open fun isNormalTweet() = false
}

class ReTweet(tweet: Tweet) : TweetType(tweet, null) {
    override fun isReTweet() = true
}

class ReplayTweet(image: String?, tweet: Tweet): TweetType(tweet, image) {
    override fun isReplayTweet() = true
}

class NormalTweet(image: String?): TweetType(null, image) {
    override fun isNormalTweet() = true
}
