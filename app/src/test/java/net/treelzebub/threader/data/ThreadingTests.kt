package net.treelzebub.threader.data

import net.treelzebub.threader.ui.tweets.TweetAdapter
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import kotlin.test.assertEquals


/**
 * Created by Tre Murillo on 8/20/2017
 */
@RunWith(RobolectricTestRunner::class)
class ThreadingTests {

    private val context by lazy { RuntimeEnvironment.application }

    private var addedIndex = -1
    private var removedIndex = -1
    private var hasBeenFocused = -1

    private val listener = object : TweetAdapter.TweetAdapterListener {
        override fun onTweetAdded(position: Int, tweet: Tweet) {
            addedIndex = position
        }

        override fun onTweetRemoved(position: Int) {
            removedIndex = position
        }

        override fun onTweetFocused(tweetNumber: Int, total: Int) {
            hasBeenFocused = tweetNumber
        }
    }

    private fun tweetListOfSize(n: Int, startingIdx: Int = 0)
            = List(n){}.mapIndexed { i, _ -> Tweet(startingIdx + i) }

    @Test
    fun testAddTweets() {
        val adapter = TweetAdapter(context, listener)
        assert(adapter.tweets.isEmpty())

        adapter.clear()
        assertEquals(tweetListOfSize(1), adapter.tweets)

        adapter.addTweet(0)
        val tweets = adapter.tweets
        assertEquals(2, tweets.size)
        assertEquals(tweetListOfSize(2), tweets)

        adapter.clear()
        val list = tweetListOfSize(10)
        adapter.load(list)
        assertEquals(list, adapter.tweets)

        adapter.addTweet(5)
        assertEquals(tweetListOfSize(11), adapter.tweets)
    }

    @Test
    fun testRemoveTweet() {
        val adapter = TweetAdapter(context, listener)
        adapter.clear()

        adapter.removeTweet(0)
        assertEquals(tweetListOfSize(1), adapter.tweets)

        adapter.load(tweetListOfSize(10))
        adapter.removeTweet(4)
        assertEquals(tweetListOfSize(9), adapter.tweets)
    }
}