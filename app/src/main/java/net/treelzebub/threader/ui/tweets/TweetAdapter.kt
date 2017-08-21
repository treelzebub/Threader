package net.treelzebub.threader.ui.tweets

import android.content.Context
import android.support.annotation.VisibleForTesting
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.item_tweet_view.view.*
import net.treelzebub.threader.R
import net.treelzebub.threader.android.copyToClipboard
import net.treelzebub.threader.android.setGone
import net.treelzebub.threader.android.setVisibleGone
import net.treelzebub.threader.data.Tweet
import org.jetbrains.anko.toast

/**
 * Created by Tre Murillo on 8/19/2017
 */
class TweetAdapter(
        private val c: Context,
        private val listener: TweetAdapterListener
)  : RecyclerView.Adapter<TweetAdapter.ViewHolder>() {

    interface TweetAdapterListener {
        fun onTweetAdded(position: Int, tweet: Tweet)
        fun onTweetRemoved(position: Int)
        fun onTweetFocused(tweetNumber: Int, total: Int)
    }

    var tweets = listOf<Tweet>()
        private set

    private val inflater by lazy { LayoutInflater.from(c) }

    fun load(tweets: List<Tweet> = listOf()) {
        if (tweets.isEmpty()) {
            this.tweets = listOf(Tweet(0))
        } else {
            this.tweets = tweets
        }
        notifyDataSetChanged()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun addTweet(afterIndex: Int) {
        val newIndex = afterIndex + 1
        val copy = tweets.toMutableList()
        copy.add(newIndex, Tweet(newIndex))
        tweets = copy.indexTweets()
        notifyItemInserted(newIndex)
        listener.onTweetAdded(newIndex, tweets[newIndex])
    }

    private fun List<Tweet>.indexTweets() = mapIndexed { i, it -> Tweet(i, it.text) }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun removeTweet(index: Int) {
        val copy = tweets.toMutableList()
        copy.removeAt(index)
        tweets = copy.indexTweets()
        if (tweets.isEmpty()) {
            load()
            notifyItemChanged(index)
        } else {
            notifyItemRemoved(index)
        }
        listener.onTweetRemoved(index)
    }

    fun clear() {
        tweets = listOf(Tweet(0))
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_tweet_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.set(position)
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

        fun set(position: Int) {
            val tweet = tweets[position]
            itemView.tag = tweet
            itemView.setOnClickListener {
                tweetText.requestFocus()
            }
            actions.setGone()
            tweetText.setText(tweet.text)
            tweetText.setOnFocusChangeListener { _, hasFocus ->
                actions.setVisibleGone(hasFocus)
                val tweetNumber = position + 1
                if (hasFocus) listener.onTweetFocused(tweetNumber, tweets.size)
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
                addTweet(position)
            }
            remove.setOnClickListener {
                removeTweet(position)
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