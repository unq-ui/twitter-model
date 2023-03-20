package org.unq

import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TwitterSystemTest {

    @Test
    fun addNewUserTest() {
        val twitterSystem = TwitterSystem()
        val userDraft = DraftUser("a", "a@gmail.com", "a", "image", "backgroundImage")

        assertEquals(twitterSystem.users.size, 0)
        twitterSystem.addNewUser(userDraft)
        assertEquals(twitterSystem.users.size, 1)
        val user = twitterSystem.users[0]
        assertEquals(user.id, "u_1")
        assertEquals(user.username, userDraft.username)
        assertEquals(user.email, userDraft.email)
        assertEquals(user.password, userDraft.password)
        assertEquals(user.image, userDraft.image)
        assertEquals(user.backgroundImage, userDraft.backgroundImage)
    }

    @Test
    fun addNewUserWithSameEmailTest() {
        val twitterSystem = TwitterSystem()
        val userDraft = DraftUser("a", "a@gmail.com", "a", "image", "backgroundImage")
        val userDraft2 = DraftUser("abc", "a@gmail.com", "a", "image", "backgroundImage")
        assertEquals(twitterSystem.users.size, 0)
        twitterSystem.addNewUser(userDraft)
        assertEquals(twitterSystem.users.size, 1)
        assertFailsWith<UserException>("Email is token") {
            twitterSystem.addNewUser(userDraft2)
        }
    }

    @Test
    fun addNewUserWithSameUsernameTest() {
        val twitterSystem = TwitterSystem()
        val userDraft = DraftUser("a", "a@gmail.com", "a", "image", "backgroundImage")
        val userDraft2 = DraftUser("abc", "a@gmail.com", "a", "image", "backgroundImage")
        assertEquals(twitterSystem.users.size, 0)
        twitterSystem.addNewUser(userDraft)
        assertEquals(twitterSystem.users.size, 1)
        assertFailsWith<UserException>("Username is token") {
            twitterSystem.addNewUser(userDraft2)
        }
    }

    @Test
    fun addNewTweetTest() {
        val twitterSystem = TwitterSystem()
        val userDraft = DraftUser("a", "a@gmail.com", "a", "image", "backgroundImage")
        val tweetDraft = DraftTweet("u_1", "content", "image", LocalDateTime.now())
        twitterSystem.addNewUser(userDraft)

        assertEquals(twitterSystem.tweets.size, 0)
        twitterSystem.addNewTweet(tweetDraft)
        assertEquals(twitterSystem.tweets.size, 1)
        val tweet = twitterSystem.tweets[0]
        assertEquals(tweet.id, "t_1")
        assertEquals(tweet.content, tweetDraft.content)
        assert(tweet.type.isNormalTweet())
        assertEquals(tweet.type.image, tweetDraft.image)
        assertEquals(tweet.date, tweetDraft.date)
    }

    @Test
    fun addNewTweetWithWrongUserIdTest() {
        val twitterSystem = TwitterSystem()
        val tweetDraft = DraftTweet("u_1", "content", "image", LocalDateTime.now())

        assertEquals(twitterSystem.tweets.size, 0)
        assertFailsWith<UserException>("Not found user id") {
            twitterSystem.addNewTweet(tweetDraft)
        }
    }

    @Test
    fun addReTweetTest() {
        val twitterSystem = TwitterSystem()
        val userDraft = DraftUser("a", "a@gmail.com", "a", "image", "backgroundImage")
        val userDraft2 = DraftUser("b", "b@gmail.com", "b", "image", "backgroundImage")
        twitterSystem.addNewUser(userDraft)
        twitterSystem.addNewUser(userDraft2)
        val tweetDraft = DraftTweet("u_1", "content", "image", LocalDateTime.now())
        val reTweetDraft = ReTweetDraft("u_2", "t_1", "content1", LocalDateTime.now())

        twitterSystem.addNewTweet(tweetDraft)
        assertEquals(twitterSystem.tweets.size, 1)
        val tweet = twitterSystem.tweets[0]
        assertEquals(tweet.reTweets.size, 0)
        twitterSystem.addReTweet(reTweetDraft)
        assertEquals(twitterSystem.tweets.size, 2)
        assertEquals(tweet.reTweets.size, 1)

        val reTweet = twitterSystem.tweets[1]
        assertEquals(reTweet.id, "t_2")
        assertEquals(reTweet.content, reTweetDraft.content)
        assertEquals(reTweet.user.id, "u_2")
        assertEquals(reTweet.date, reTweetDraft.date)
    }

    @Test
    fun addReTweetWithSameUserTest() {
        val twitterSystem = TwitterSystem()
        val userDraft = DraftUser("a", "a@gmail.com", "a", "image", "backgroundImage")
        twitterSystem.addNewUser(userDraft)
        val tweetDraft = DraftTweet("u_1", "content", "image", LocalDateTime.now())
        val reTweetDraft = ReTweetDraft("u_1", "t_1", "content1", LocalDateTime.now())

        twitterSystem.addNewTweet(tweetDraft)
        assertEquals(twitterSystem.tweets.size, 1)
        val tweet = twitterSystem.tweets[0]
        assertEquals(tweet.reTweets.size, 0)

        assertFailsWith<TweetException>("Can not retweet your own tweet") {
            twitterSystem.addReTweet(reTweetDraft)
        }
        assertEquals(tweet.reTweets.size, 0)
    }

    @Test
    fun addReTweetWithWrongUserIdTest() {
        val twitterSystem = TwitterSystem()
        val userDraft = DraftUser("a", "a@gmail.com", "a", "image", "backgroundImage")
        twitterSystem.addNewUser(userDraft)
        val tweetDraft = DraftTweet("u_1", "content", "image", LocalDateTime.now())
        val reTweetDraft = ReTweetDraft("u_2", "t_1", "content1", LocalDateTime.now())

        twitterSystem.addNewTweet(tweetDraft)
        assertEquals(twitterSystem.tweets.size, 1)
        val tweet = twitterSystem.tweets[0]
        assertEquals(tweet.reTweets.size, 0)

        assertFailsWith<UserException>("Not found user id") {
            twitterSystem.addReTweet(reTweetDraft)
        }
        assertEquals(tweet.reTweets.size, 0)
    }

    @Test
    fun addReTweetWithWrongTweetIdTest() {
        val twitterSystem = TwitterSystem()
        val userDraft = DraftUser("a", "a@gmail.com", "a", "image", "backgroundImage")
        val userDraft2 = DraftUser("b", "b@gmail.com", "b", "image", "backgroundImage")
        twitterSystem.addNewUser(userDraft)
        twitterSystem.addNewUser(userDraft2)
        val reTweetDraft = ReTweetDraft("u_2", "t_1", "content1", LocalDateTime.now())

        assertEquals(twitterSystem.tweets.size, 0)

        assertFailsWith<TweetException>("Not found tweet id") {
            twitterSystem.addReTweet(reTweetDraft)
        }
    }

    @Test
    fun replyTweetTest() {
        val twitterSystem = TwitterSystem()
        val userDraft = DraftUser("a", "a@gmail.com", "a", "image", "backgroundImage")
        twitterSystem.addNewUser(userDraft)
        val tweetDraft = DraftTweet("u_1", "content", "image", LocalDateTime.now())
        val replyTweetDraft = ReplyTweetDraft("u_1", "t_1", "content1", "image2", LocalDateTime.now())

        twitterSystem.addNewTweet(tweetDraft)
        assertEquals(twitterSystem.tweets.size, 1)
        val tweet = twitterSystem.tweets[0]
        assertEquals(tweet.replies.size, 0)
        twitterSystem.replyTweet(replyTweetDraft)
        assertEquals(twitterSystem.tweets.size, 2)
        assertEquals(tweet.replies.size, 1)

        val reTweet = twitterSystem.tweets[1]
        assertEquals(reTweet.id, "t_2")
        assertEquals(reTweet.content, replyTweetDraft.content)
        assertEquals(reTweet.user.id, "u_1")
        assertEquals(reTweet.date, replyTweetDraft.date)
    }

    @Test
    fun replyTweetWithWrongUserIdTest() {
        val twitterSystem = TwitterSystem()
        val userDraft = DraftUser("a", "a@gmail.com", "a", "image", "backgroundImage")
        twitterSystem.addNewUser(userDraft)
        val tweetDraft = DraftTweet("u_1", "content", "image", LocalDateTime.now())
        val replyTweetDraft = ReplyTweetDraft("u_2", "t_1", "content1", "image2", LocalDateTime.now())

        twitterSystem.addNewTweet(tweetDraft)
        assertEquals(twitterSystem.tweets.size, 1)
        val tweet = twitterSystem.tweets[0]
        assertEquals(tweet.replies.size, 0)

        assertFailsWith<UserException>("Not found user id") {
            twitterSystem.replyTweet(replyTweetDraft)
        }

        assertEquals(twitterSystem.tweets.size, 1)
        assertEquals(tweet.replies.size, 0)
    }

    @Test
    fun replyTweetWithWrongTweetIdTest() {
        val twitterSystem = TwitterSystem()
        val userDraft = DraftUser("a", "a@gmail.com", "a", "image", "backgroundImage")
        twitterSystem.addNewUser(userDraft)
        val tweetDraft = DraftTweet("u_1", "content", "image", LocalDateTime.now())
        val replyTweetDraft = ReplyTweetDraft("u_1", "t_2", "content1", "image2", LocalDateTime.now())

        twitterSystem.addNewTweet(tweetDraft)
        assertEquals(twitterSystem.tweets.size, 1)
        val tweet = twitterSystem.tweets[0]
        assertEquals(tweet.replies.size, 0)

        assertFailsWith<TweetException>("Not found tweet id") {
            twitterSystem.replyTweet(replyTweetDraft)
        }

        assertEquals(twitterSystem.tweets.size, 1)
        assertEquals(tweet.replies.size, 0)
    }

    @Test
    fun addLikeTest() {
        val twitterSystem = TwitterSystem()
        val userDraft = DraftUser("a", "a@gmail.com", "a", "image", "backgroundImage")
        twitterSystem.addNewUser(userDraft)
        val tweetDraft = DraftTweet("u_1", "content", "image", LocalDateTime.now())
        twitterSystem.addNewTweet(tweetDraft)

        val tweet = twitterSystem.tweets[0]
        assertEquals(tweet.likes.size, 0)
        twitterSystem.addLike("t_1", "u_1")
        assertEquals(tweet.likes.size, 1)
        assertEquals(tweet.likes[0], twitterSystem.users[0])
    }

    @Test
    fun addLikeWithWrongTweetIdTest() {
        val twitterSystem = TwitterSystem()
        val userDraft = DraftUser("a", "a@gmail.com", "a", "image", "backgroundImage")
        twitterSystem.addNewUser(userDraft)
        val tweetDraft = DraftTweet("u_1", "content", "image", LocalDateTime.now())
        twitterSystem.addNewTweet(tweetDraft)

        val tweet = twitterSystem.tweets[0]
        assertEquals(tweet.likes.size, 0)
        assertFailsWith<TweetException>("Not found tweet id") {
            twitterSystem.addLike("t_2", "u_1")
        }
        assertEquals(tweet.likes.size, 0)
    }

    @Test
    fun addLikeWithWrongUserIdTest() {
        val twitterSystem = TwitterSystem()
        val userDraft = DraftUser("a", "a@gmail.com", "a", "image", "backgroundImage")
        twitterSystem.addNewUser(userDraft)
        val tweetDraft = DraftTweet("u_1", "content", "image", LocalDateTime.now())
        twitterSystem.addNewTweet(tweetDraft)

        val tweet = twitterSystem.tweets[0]
        assertEquals(tweet.likes.size, 0)
        assertFailsWith<UserException>("Not found user id") {
            twitterSystem.addLike("t_1", "u_2")
        }
        assertEquals(tweet.likes.size, 0)
    }

    @Test
    fun toggleFollowTest() {
        val twitterSystem = TwitterSystem()
        val userDraft = DraftUser("a", "a@gmail.com", "a", "image", "backgroundImage")
        val userDraft2 = DraftUser("b", "b@gmail.com", "b", "image", "backgroundImage")
        twitterSystem.addNewUser(userDraft)
        twitterSystem.addNewUser(userDraft2)

        val user1 = twitterSystem.users[0]
        val user2 = twitterSystem.users[1]

        assertEquals(user1.followers.size, 0)
        assertEquals(user1.following.size, 0)
        assertEquals(user2.followers.size, 0)
        assertEquals(user2.following.size, 0)

        twitterSystem.toggleFollow("u_1", "u_2")

        assertEquals(user1.followers.size, 0)
        assertEquals(user1.following.size, 1)
        assertEquals(user2.followers.size, 1)
        assertEquals(user2.following.size, 0)
    }

    @Test
    fun toggleFollowTwoTimesTest() {
        val twitterSystem = TwitterSystem()
        val userDraft = DraftUser("a", "a@gmail.com", "a", "image", "backgroundImage")
        val userDraft2 = DraftUser("b", "b@gmail.com", "b", "image", "backgroundImage")
        twitterSystem.addNewUser(userDraft)
        twitterSystem.addNewUser(userDraft2)

        val user1 = twitterSystem.users[0]
        val user2 = twitterSystem.users[1]

        assertEquals(user1.followers.size, 0)
        assertEquals(user1.following.size, 0)
        assertEquals(user2.followers.size, 0)
        assertEquals(user2.following.size, 0)

        twitterSystem.toggleFollow("u_1", "u_2")

        assertEquals(user1.followers.size, 0)
        assertEquals(user1.following.size, 1)
        assertEquals(user2.followers.size, 1)
        assertEquals(user2.following.size, 0)

        twitterSystem.toggleFollow("u_1", "u_2")

        assertEquals(user1.followers.size, 0)
        assertEquals(user1.following.size, 0)
        assertEquals(user2.followers.size, 0)
        assertEquals(user2.following.size, 0)
    }

    @Test
    fun toggleFollowWithWrongFirstUserIdTest() {
        val twitterSystem = TwitterSystem()
        val userDraft = DraftUser("a", "a@gmail.com", "a", "image", "backgroundImage")
        val userDraft2 = DraftUser("b", "b@gmail.com", "b", "image", "backgroundImage")
        twitterSystem.addNewUser(userDraft)
        twitterSystem.addNewUser(userDraft2)

        val user1 = twitterSystem.users[0]
        val user2 = twitterSystem.users[1]

        assertEquals(user1.followers.size, 0)
        assertEquals(user1.following.size, 0)
        assertEquals(user2.followers.size, 0)
        assertEquals(user2.following.size, 0)

        assertFailsWith<UserException>("Not found user id") {
            twitterSystem.toggleFollow("u_3", "u_2")
        }

        assertEquals(user1.followers.size, 0)
        assertEquals(user1.following.size, 0)
        assertEquals(user2.followers.size, 0)
        assertEquals(user2.following.size, 0)
    }

    @Test
    fun toggleFollowWithWrongSecondUserIdTest() {
        val twitterSystem = TwitterSystem()
        val userDraft = DraftUser("a", "a@gmail.com", "a", "image", "backgroundImage")
        val userDraft2 = DraftUser("b", "b@gmail.com", "b", "image", "backgroundImage")
        twitterSystem.addNewUser(userDraft)
        twitterSystem.addNewUser(userDraft2)

        val user1 = twitterSystem.users[0]
        val user2 = twitterSystem.users[1]

        assertEquals(user1.followers.size, 0)
        assertEquals(user1.following.size, 0)
        assertEquals(user2.followers.size, 0)
        assertEquals(user2.following.size, 0)

        assertFailsWith<UserException>("Not found user id") {
            twitterSystem.toggleFollow("u_1", "u_3")
        }

        assertEquals(user1.followers.size, 0)
        assertEquals(user1.following.size, 0)
        assertEquals(user2.followers.size, 0)
        assertEquals(user2.following.size, 0)
    }

    @Test
    fun toggleFollowWithSameUserIdTest() {
        val twitterSystem = TwitterSystem()
        val userDraft = DraftUser("a", "a@gmail.com", "a", "image", "backgroundImage")
        twitterSystem.addNewUser(userDraft)

        val user1 = twitterSystem.users[0]

        assertEquals(user1.followers.size, 0)
        assertEquals(user1.following.size, 0)

        assertFailsWith<UserException>("Can not follow to yourself") {
            twitterSystem.toggleFollow("u_1", "u_1")
        }

        assertEquals(user1.followers.size, 0)
        assertEquals(user1.following.size, 0)
    }

    @Test
    fun searchTest() {
        val twitterSystem = TwitterSystem()
        val userDraft = DraftUser("a", "a@gmail.com", "a", "image", "backgroundImage")
        twitterSystem.addNewUser(userDraft)

        val tweetDraft = DraftTweet("u_1", "a", "image", LocalDateTime.now())
        val tweetDraft1 = DraftTweet("u_1", "b", "image", LocalDateTime.now())
        val tweetDraft2 = DraftTweet("u_1", "c", "image", LocalDateTime.now())
        val tweetDraft3 = DraftTweet("u_1", "da", "image", LocalDateTime.now())
        twitterSystem.addNewTweet(tweetDraft)
        twitterSystem.addNewTweet(tweetDraft1)
        twitterSystem.addNewTweet(tweetDraft2)
        twitterSystem.addNewTweet(tweetDraft3)

        assertEquals(twitterSystem.search("a").size, 2)
    }

    @Test
    fun getFollowingTweetsTest() {
        val twitterSystem = TwitterSystem()
        val userDraft = DraftUser("a", "a@gmail.com", "a", "image", "backgroundImage")
        val userDraft2 = DraftUser("b", "b@gmail.com", "b", "image", "backgroundImage")
        val userDraft3 = DraftUser("c", "c@gmail.com", "c", "image", "backgroundImage")
        twitterSystem.addNewUser(userDraft)
        twitterSystem.addNewUser(userDraft2)
        twitterSystem.addNewUser(userDraft3)

        val tweetDraft = DraftTweet("u_1", "a", "image", LocalDateTime.now())
        val tweetDraft1 = DraftTweet("u_1", "b", "image", LocalDateTime.now())
        val tweetDraft2 = DraftTweet("u_1", "c", "image", LocalDateTime.now())
        val tweetDraft3 = DraftTweet("u_2", "da", "image", LocalDateTime.now())
        twitterSystem.addNewTweet(tweetDraft)
        twitterSystem.addNewTweet(tweetDraft1)
        twitterSystem.addNewTweet(tweetDraft2)
        twitterSystem.addNewTweet(tweetDraft3)

        twitterSystem.toggleFollow("u_3", "u_1")

        assertEquals(twitterSystem.getFollowingTweets("u_3").size, 3)
    }

    @Test
    fun getUsersToFollowTest() {
        val twitterSystem = TwitterSystem()
        val userDrafts = listOf(
            DraftUser("a", "a@gmail.com", "a", "image", "backgroundImage"),
            DraftUser("b", "b@gmail.com", "b", "image", "backgroundImage"),
            DraftUser("c", "c@gmail.com", "c", "image", "backgroundImage"),
            DraftUser("d", "d@gmail.com", "d", "image", "backgroundImage"),
            DraftUser("e", "e@gmail.com", "e", "image", "backgroundImage"),
            DraftUser("f", "f@gmail.com", "f", "image", "backgroundImage"),
            DraftUser("g", "g@gmail.com", "g", "image", "backgroundImage"),
            DraftUser("h", "h@gmail.com", "h", "image", "backgroundImage"),
            DraftUser("i", "i@gmail.com", "i", "image", "backgroundImage"),
            DraftUser("j", "j@gmail.com", "j", "image", "backgroundImage"),
            DraftUser("k", "k@gmail.com", "k", "image", "backgroundImage"),
            DraftUser("l", "l@gmail.com", "l", "image", "backgroundImage"),
            DraftUser("m", "m@gmail.com", "m", "image", "backgroundImage"),
            DraftUser("n", "n@gmail.com", "n", "image", "backgroundImage")
        )

        userDrafts.forEach { twitterSystem.addNewUser(it) }

        twitterSystem.users.take(5).forEach { twitterSystem.toggleFollow("u_14", it.id) }

        val usersToFollow = twitterSystem.getUsersToFollow("u_14")

        assertEquals(usersToFollow.map { it.username }, listOf("f", "g", "h", "i", "j", "k", "l", "m"))
    }

    @Test
    fun getUsersToFollowWithWrongUserIdTest() {
        val twitterSystem = TwitterSystem()
        assertFailsWith<UserException>("Not found user id") {
            twitterSystem.getUsersToFollow("u_14")
        }
    }

    @Test
    fun getTrendingTopics() {
        val twitterSystem = TwitterSystem()
        twitterSystem.addNewUser(DraftUser("a", "a@gmail.com", "a", "image", "backgroundImage"))
        twitterSystem.addNewUser(DraftUser("b", "b@gmail.com", "b", "image", "backgroundImage"))
        twitterSystem.addNewUser(DraftUser("c", "c@gmail.com", "c", "image", "backgroundImage"))

        val tweetDrafts = listOf(
            DraftTweet("u_1", "content", "image"),
            DraftTweet("u_1", "content1", "image"),
            DraftTweet("u_1", "content2", "image"),
            DraftTweet("u_1", "content3", "image"),
            DraftTweet("u_1", "content4", "image"),
            DraftTweet("u_1", "content5", "image"),
            DraftTweet("u_1", "content6", "image"),
            DraftTweet("u_1", "content7", "image"),
            DraftTweet("u_1", "content8", "image"),
            DraftTweet("u_1", "content9", "image"),
            DraftTweet("u_1", "content10", "image"),
        )

        tweetDrafts.forEach { twitterSystem.addNewTweet(it) }

        twitterSystem.tweets.take(3).forEach {
            twitterSystem.addLike(it.id, "u_1")
            twitterSystem.addLike(it.id, "u_2")
            twitterSystem.addLike(it.id, "u_3")
        }

        twitterSystem.tweets.takeLast(5).forEach {
            twitterSystem.addLike(it.id, "u_1")
            twitterSystem.addLike(it.id, "u_3")
        }

        val trendingTopic = twitterSystem.getTrendingTopics()

        assertEquals(trendingTopic.size, 10)
        assertEquals(trendingTopic.map { it.id }, listOf("t_1", "t_2", "t_3", "t_4", "t_5", "t_7", "t_8", "t_9", "t_10", "t_11"))
    }

}

