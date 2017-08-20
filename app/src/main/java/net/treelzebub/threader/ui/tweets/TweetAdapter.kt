package net.treelzebub.threader.ui.tweets

import android.content.Context
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
import net.treelzebub.threader.android.toast
import net.treelzebub.threader.data.Tweet

/**
 * Created by Tre Murillo on 8/19/2017
 */
class TweetAdapter(private val c: Context)  : RecyclerView.Adapter<TweetAdapter.ViewHolder>() {

    private var tweets = listOf<Tweet>()

    private val inflater by lazy { LayoutInflater.from(c) }

    fun setTweets(tweets: List<Tweet>, indexChanged: Int = -1) {
        this.tweets = tweets
        if (indexChanged == -1) {
            notifyDataSetChanged()
        } else {
            notifyItemChanged(indexChanged)
        }
    }

    fun addTweet(index: Int = tweets.lastIndex.coerceAtLeast(0)) {
        val copy = ArrayList(tweets)
        copy.add(index, Tweet())
        setTweets(copy)
    }

    fun removeTweet(index: Int = tweets.lastIndex) {
        val copy = ArrayList(tweets)
        copy.removeAt(index)
        setTweets(copy)
    }

    fun clear() = setTweets(listOf())

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_tweet_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.set(tweets[position])

    override fun getItemCount() = tweets.size

    override fun getItemViewType(position: Int) = 1

    override fun getItemId(position: Int) = tweets[position].hashCode().toLong()

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val tweetText: EditText by lazy { view.text }
        private val count: TextView by lazy { view.count }
        private val actions: LinearLayout by lazy { view.actions }
        private val add: View by lazy { view.more }
        private val remove: View by lazy { view.less }
        private val copy: View by lazy { view.copy }

        fun set(tweet: Tweet) {
            tweetText.setText(tweet.text)
            tweetText.setOnFocusChangeListener { view, hasFocus ->
                actions.visibility = if (hasFocus) View.VISIBLE else View.GONE
            }
            tweetText.requestFocus()
            tweetText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(e: Editable?) {}
                override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    count.text = "${140 - Tweet.count(s.toString())}"
                    // TODO if count < 10,  red text, Spannable stuff...
                }
            })
            count.text = "${140 - tweet.count}"

            add.setOnClickListener { addTweet() }
            remove.setOnClickListener { removeTweet() }
            copy.setOnClickListener {
                c.copyToClipboard(tweetText.text.toString())
                c.toast("Copied tweet.")
            }
        }
    }
}