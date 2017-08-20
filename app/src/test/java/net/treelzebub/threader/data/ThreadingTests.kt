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

    private var hasBeenAdded = -1
    private var hasBeenFocused = -1

    private val listener = object : TweetAdapter.TweetAdapterListener {
        override fun onTweetAdded(position: Int, tweet: Tweet) {
            hasBeenAdded = position
        }

        override fun onTweetFocused(tweetNumber: Int, total: Int) {
            hasBeenFocused = tweetNumber
        }
    }

    private fun tweetListOfSize(n: Int, startingIdx: Int = 0)
            = List(n){}.mapIndexed { i, _ -> Tweet(startingIdx + i) }

    @Test
    fun testSplit() {
        val list = tweetListOfSize(4)
        val splitAtZero = list.splitAt(0)
        assertEquals(listOf(list.first()), splitAtZero.first)
        assertEquals(list.drop(1), splitAtZero.second)

        val splitAtOne = list.splitAt(1)
        assertEquals(listOf(Tweet(0), Tweet(1)), splitAtOne.first)
        assertEquals(listOf(Tweet(2), Tweet(3)), splitAtOne.second)

        val splitAtThree = list.splitAt(3)
        assertEquals(listOf(Tweet(0), Tweet(1), Tweet(2), Tweet(3)), splitAtThree.first)
        assertEquals(listOf(), splitAtThree.second)
    }

    @Test
    fun testIncrementIndices() {
        val SPLIT_AT = 5
        val list = tweetListOfSize(10)
        val split = list.splitAt(SPLIT_AT)
        assertEquals(6, split.first.size)
        assertEquals(4, split.second.size)

        val first = split.first + Tweet(SPLIT_AT + 1)
        assertEquals(tweetListOfSize(7), first)

        val second = split.second.incrementIndices()
        assertEquals(tweetListOfSize(4, 7), second)

        val result = first + second
        assertEquals(11, result.size)
        assertEquals(tweetListOfSize(11), result)
    }

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
        adapter.setTweets(list)
        assertEquals(list, adapter.tweets)

        adapter.addTweet(5)
        assertEquals(tweetListOfSize(11), adapter.tweets)
    }
}