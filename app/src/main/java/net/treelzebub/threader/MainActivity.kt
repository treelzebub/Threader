package net.treelzebub.threader

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_tweet_view.view.*
import net.treelzebub.threader.android.copyToClipboard
import net.treelzebub.threader.data.Tweet
import net.treelzebub.threader.data.TweetStore
import net.treelzebub.threader.ui.tweets.TweetAdapter
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity(), TweetAdapter.TweetAdapterListener {

    private val tweetAdapter = TweetAdapter(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setupRecycler()
    }

    private fun setupRecycler() {
        with(recycler) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            itemAnimator = DefaultItemAnimator()
            adapter = tweetAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        val saved = TweetStore.load(this)
        tweetAdapter.load(saved)
    }

    override fun onPause() {
        TweetStore.persist(this, tweetAdapter.tweets)
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_ready -> {
                ready()
                true
            }
            R.id.action_clear -> {
                alert {
                    message = "Clear thread?"
                    positiveButton("Yes") {
                        TweetStore.clear(this@MainActivity)
                        tweetAdapter.clear()
                    }
                    negativeButton("No", {})
                }.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onTweetAdded(position: Int, tweet: Tweet) {
        // Does its best to request focus on the new view.
        // TODO Probably should find out how to do this properly, cuz this is super hacky.
        val layoutManager = recycler.layoutManager
        doAsync {
            if (layoutManager.canScrollVertically()) {
                layoutManager.smoothScrollToPosition(recycler, RecyclerView.State(), position)
                while (layoutManager.isSmoothScrolling) {
                    Thread.sleep(100L)
                }
            }
            uiThread {
                val child = recycler.findViewWithTag<View>(tweet)
                child?.text?.requestFocus()
            }
        }
    }

    override fun onTweetRemoved(position: Int) {
        toast("Removed tweet ${position + 1}")
    }

    @SuppressLint("SetTextI18n")
    override fun onTweetFocused(tweetNumber: Int, total: Int) {
        thread_count.text = "$tweetNumber / $total"
    }

    private fun ready() {
        val layoutManager = recycler.layoutManager
        doAsync {
            layoutManager.smoothScrollToPosition(recycler, RecyclerView.State(), 0)
            while (layoutManager.isSmoothScrolling) {
                Thread.sleep(100L)
            }
            uiThread {
                val tweetView = layoutManager.getChildAt(0).text
                tweetView.requestFocus()
                tweetView.selectAll()
                val text = tweetView.text.toString()
                if (text.isNotBlank()) {
                    copyToClipboard(text)
                    toast("Copied tweet.")
                }
            }
        }
    }
}