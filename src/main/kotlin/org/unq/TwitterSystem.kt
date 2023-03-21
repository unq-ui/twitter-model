package org.unq

class TwitterSystem {
    private val idGenerator = IdGenerator()
    val users = mutableListOf<User>()
    val tweets = mutableListOf<Tweet>()

    /**
     * Crea un usuario nuevo.
     * @throws UserException
     *   El email se encuentra repetido.
     *   El username se encuentra repetido.
     *
     */
    fun addNewUser(user: DraftUser): User {
        users.forEach {
            if (it.username == user.username) throw UserException("Username is token")
            if (it.email == user.email) throw UserException("Email is token")
        }
        val newUser = User(idGenerator.nextUserId(), user.username, user.email, user.password, user.image, user.backgroundImage)
        users.add(newUser)
        return newUser
    }

    /**
     * Crea un nuevo tweet.
     * @throws UserException
     *  si el userId del draftTweet no existe.
     */
    fun addNewTweet(tweet: DraftTweet): Tweet {
        val user = getUser(tweet.userId)
        val newTweet = Tweet(
            idGenerator.nextTweetId(),
            NormalTweet(tweet.image),
            user,
            tweet.content,
            tweet.date
        )
        tweets.add(newTweet)
        return newTweet
    }

    /**
     * Devuelve el tweet con el id de tweetId
     *
     * @throws UserException
     *  si el userId del DraftReTweet no existe.
     * @throws TweetException
     *  si el tweetId no existe.
     *  si el tweetId pertenece al mismo usuario.
     */
    fun addReTweet(tweet: DraftReTweet): Tweet {
        val user = getUser(tweet.userId)
        val originTweet = getTweet(tweet.tweetId)
        if (originTweet.user == user) throw TweetException("Can not retweet your own tweet")
        val newTweet = Tweet(
            idGenerator.nextTweetId(),
            ReTweet(originTweet),
            user,
            tweet.content,
            tweet.date
        )
        originTweet.reTweets.add(newTweet)
        tweets.add(newTweet)
        return originTweet
    }

    /**
     * Devuelve el tweet con el id de tweetId
     *
     * @throws UserException
     *  si el userId del DraftReplyTweet no existe.
     * @throws TweetException
     *  si el tweetId no existe.
     */
    fun replyTweet(tweet: DraftReplyTweet): Tweet {
        val user = getUser(tweet.userId)
        val originTweet = getTweet(tweet.tweetId)
        val newTweet = Tweet(
            idGenerator.nextTweetId(),
            ReplayTweet(tweet.image, originTweet),
            user,
            tweet.content,
            tweet.date
        )
        originTweet.replies.add(newTweet)
        tweets.add(newTweet)
        return originTweet
    }

    /**
     * Devuelve el tweet con el id de tweetId
     *
     * @throws UserException
     *  si el userId no existe.
     * @throws TweetException
     *  si el tweetId no existe.
     */
    fun addLike(tweetId: String, userId: String): Tweet {
        val user = getUser(userId)
        val originTweet = getTweet(tweetId)
        originTweet.likes.add(user)
        return originTweet
    }

    /**
     * Devuelve al usuario con el id userId
     *
     * @throws UserException
     *  si el userId o el userToFollowingId no existe.
     */
    fun toggleFollow(userId: String, userToFollowingId: String): User {
        val user = getUser(userId)
        val userToFollow = getUser(userToFollowingId)

        if (user == userToFollow) throw UserException("Can not follow to yourself")

        if (user.following.contains(userToFollow)) {
            user.following.remove(userToFollow)
            userToFollow.followers.remove(user)
        } else {
            user.following.add(userToFollow)
            userToFollow.followers.add(user)
        }

        return user
    }

    /**
     * Devuelve la lista de tweets donde el content contenga el `text`
     */
    fun search(text: String): List<Tweet> {
        return tweets.filter { it.content.contains(text, true) }
    }

    /**
     * Devuelve una lista de tweets de los usuarios que el userId sigue
     * @throws UserException
     *  si el userId no existe.
     */
    fun getFollowingTweets(userId: String): List<Tweet> {
        val user = getUser(userId)
        return tweets.filter { user.following.contains(it.user) }.sortedBy { it.date }
    }

    /**
     * Devuelve los usuarios con mas seguidores que no siga el usuario con el userId
     * @throws UserException
     *  si el userId no existe.
     */
    fun getUsersToFollow(userId: String): List<User> {
        val user = getUser(userId)
        val allUsers = users.sortedBy { it.followers.size }.toMutableList()
        allUsers.removeIf { user == it || user.following.contains(it) }
        return  allUsers.take(10)
    }

    /**
     * Devuelve los post que tengan mas likes
     */
    fun getTrendingTopics(): List<Tweet> {
        return tweets.sortedByDescending { it.likes.size }.take(10).sortedBy { it.date }
    }

    /**
     * Devuelve el usuario con el userId
     * @throws UserException
     *  si el userId no existe.
     */
    fun getUser(userId: String): User {
        return users.find { it.id == userId } ?: throw UserException("Not found user id")
    }

    /**
     * Devuelve el tweet con el tweetId
     * @throws TweetException
     *  si el tweetId no existe.
     */
    fun getTweet(tweetId: String): Tweet {
        return tweets.find { it.id == tweetId } ?: throw TweetException("Not found user id")
    }
}