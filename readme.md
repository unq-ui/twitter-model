# UNQ » UIs » Dominio » Twitter

[![](https://jitpack.io/v/unq-ui/twitter-model.svg)](https://jitpack.io/#unq-ui/twitter-model)


Construcción de Interfaces de Usuario, Universidad Nacional de Quilmes.

## TP 2023s1

### Dependencia

Agregar el repositorio:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Agregar la dependencia:

```xml
<dependency>
    <groupId>com.github.unq-ui</groupId>
    <artifactId>twitter-model</artifactId>
    <version>v1.1.0</version>
</dependency>
```

### Como usar el modelo


```kotlin

val twitterSystem = initTwitterSystem()

```

### Interfaz de uso

```kotlin
package org.unq

class TwitterSystem {

    val users = mutableListOf<User>()
    val tweets = mutableListOf<Tweet>()

    /**
     * Crea un usuario nuevo.
     * @throws UserException
     *   El email se encuentra repetido.
     *   El username se encuentra repetido.
     *
     */
    fun addNewUser(user: DraftUser): User

    /**
     * Crea un nuevo tweet.
     * @throws UserException
     *  si el userId no existe.
     */
    fun addNewTweet(tweet: DraftTweet): Tweet

    /**
     * Devuelve el tweet con el id de tweetId.
     *
     * @throws UserException
     *  si el userId no existe.
     * @throws TweetException
     *  si el tweetId no existe.
     *  si el tweetId pertenece al mismo usuario.
     */
    fun addReTweet(tweet: DraftReTweet): Tweet

    /**
     * Devuelve el tweet con el id de tweetId.
     *
     * @throws UserException
     *  si el userId no existe.
     * @throws TweetException
     *  si el tweetId no existe.
     */
    fun replyTweet(tweet: DraftReplyTweet): Tweet

    /**
     * Devuelve el tweet con el id de tweetId.
     *
     * @throws UserException
     *  si el userId no existe.
     * @throws TweetException
     *  si el tweetId no existe.
     */
    fun addLike(tweetId: String, userId: String): Tweet

    /**
     * Devuelve al usuario con el id userId
     *
     * @throws UserException
     *  si el userId o el userToFollowingId no existe.
     */
    fun toggleFollow(userId: String, userToFollowingId: String): User

    /**
     * Devuelve la lista de tweets donde el content contenga el `text`
     */
    fun search(text: String): List<Tweet>

    /**
     * Devuelve una lista de tweets de los usuarios que el userId sigue
     * @throws UserException
     *  si el userId no existe.
     */
    fun getFollowingTweets(userId: String): List<Tweet>

    /**
     * Devuelve los usuarios con mas seguidores que no siga el usuario con el userId
     * @throws UserException
     *  si el userId no existe.
     */
    fun getUsersToFollow(userId: String): List<User>

    /**
     * Devuelve los post que tengan mas likes
     */
    fun getTrendingTopics(): List<Tweet>

    /**
     * Devuelve el usuario con el userId
     * @throws UserException
     *  si el userId no existe.
     */
    fun getUser(userId: String): User

    /**
     * Devuelve el tweet con el tweetId
     * @throws TweetException
     *  si el tweetId no existe.
     */
    fun getTweet(tweetId: String): Tweet
}
```

### Clases

```Kotlin

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
    open fun isReTweet(): Boolean
    open fun isReplayTweet(): Boolean
    open fun isNormalTweet(): Boolean
}

class ReTweet(tweet: Tweet) : TweetType(tweet, null)

class ReplayTweet(image: String?, tweet: Tweet): TweetType(tweet, image)

class NormalTweet(image: String?): TweetType(null, image)

```

## Información

* TODA interaccion tiene que pasar por TiktokSystem (con la interfaz definida previamente).

* El TiktokSystem es el encargado de setear los ids de cada elemento que se agrega el sistema.
  * Para simplificar se utilizan objetos draft

```kotlin
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
```
