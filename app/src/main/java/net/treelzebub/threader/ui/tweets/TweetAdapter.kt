package net.treelzebub.threader.ui.tweets

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.item_tweet_view.view.*
import net.treelzebub.threader.R
import net.treelzebub.threader.android.copyToClipboard
import net.treelzebub.threader.android.setVisibleGone
import net.treelzebub.threader.data.Tweet
import net.treelzebub.threader.runtime.TAG
import org.jetbrains.anko.toast
import java.util.*

/**
 * Created by Tre Murillo on 8/19/2017
 */
class TweetAdapter(
        private val c: Context,
        private val listener: TweetAdapterListener
)  : RecyclerView.Adapter<TweetAdapter.ViewHolder>() {

    interface TweetAdapterListener {
        fun onTweetAdded(position: Int)
//        fun onTweetRemoved(position: Int)
        fun onTweetFocused(tweetNumber: Int, total: Int)
    }

    var tweets = listOf<Tweet>()
        private set

    private val inflater by lazy { LayoutInflater.from(c) }

    fun setTweets(tweets: List<Tweet>, indexChanged: Int = -1) {
        this.tweets = tweets.indexTweets()
        if (indexChanged == -1) {
            notifyDataSetChanged()
        } else {
            notifyItemInserted(indexChanged)
        }
    }

    fun addTweet(position: Int) {
        Log.d(TAG, "adding new tweet at position $position")
        val copy = LinkedList(tweets)
        copy.add(position, Tweet(position))
        setTweets(copy)
    }

    fun removeTweet(index: Int) {
        val copy = LinkedList(tweets)
        copy.removeAt(index)
        if (copy.isEmpty()) clear() else setTweets(copy)
    }

    // move this into a Threading object
    private fun List<Tweet>.indexTweets(): List<Tweet> {
        return sortedBy { it.position }
                .mapIndexed { i, it -> Tweet(i + 1, it.text) }
                .apply {
                    Log.d(TAG, map { it.position }.joinToString(", "))
                }
    }

    fun clear() = setTweets(listOf(Tweet()))

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_tweet_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.set(tweets[position])
    }

    override fun getItemCount() = tweets.size

    override fun getItemViewType(position: Int) = 1

    override fun getItemId(position: Int) = tweets[position].hashCode().toLong()

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val tweetText: EditText     by lazy { view.text }
        private val count: TextView         by lazy { view.count }
        private val actions: LinearLayout   by lazy { view.actions }
        private val add: View               by lazy { view.more }
        private val remove: View            by lazy { view.less }
        private val copy: View              by lazy { view.copy }

        fun set(tweet: Tweet) {
            val position = tweets.indexOf(tweet)
            tweetText.setText(tweet.text)
            tweetText.setOnFocusChangeListener { _, hasFocus ->
                actions.setVisibleGone(hasFocus)
                if (hasFocus) listener.onTweetFocused(position + 1, tweets.size)
            }

            tweetText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(e: Editable?) {}
                override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    tweet.text = s.toString()
                    count.text = "${140 - Tweet.count(s.toString())}"
                    // TODO if count < 10,  red text, Spannable stuff...
                }
            })
            count.text = "${tweet.remaining}"

            add.setOnClickListener {
                val newPosition = position + 1
                Log.d(TAG, "adding after position $position")
                addTweet(newPosition)
                listener.onTweetAdded(newPosition)
            }
            remove.setOnClickListener {
                removeTweet(position)
                // listener.onTweetRemoved(position)
            }
            copy.setOnClickListener {
                val text = tweetText.text.toString()
                if (text.isBlank()) return@setOnClickListener
                c.copyToClipboard(text)
                c.toast("Copied tweet.")
            }
        }
    }
}