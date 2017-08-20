package net.treelzebub.threader

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.item_tweet_view.view.*
import net.treelzebub.threader.android.copyToClipboard
import net.treelzebub.threader.android.dismissKeyboard
import net.treelzebub.threader.android.toast
import net.treelzebub.threader.data.Tweet
import net.treelzebub.threader.data.TweetStore
import net.treelzebub.threader.ui.tweets.TweetAdapter

class MainActivity : AppCompatActivity() {

    private val tweetAdapter = TweetAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        setSupportActionBar(toolbar)
        setupRecycler()
    }

    private fun setupRecycler() {
        with(recycler) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = tweetAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {}
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) dismissKeyboard()
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        val saved = TweetStore.load(this)
        tweetAdapter.setTweets(if (saved.isEmpty()) listOf(Tweet()) else saved)
        ready()
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
                // TODO are you sure?
                tweetAdapter.clear()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun ready() {
        val tweetView = recycler.getChildAt(0) ?: return
        recycler.smoothScrollToPosition(tweetAdapter.tweets.lastIndex)
        val text = tweetView.text.toString()
        if (text.isNotBlank()) {
            copyToClipboard(text)
            toast("Copied tweet.")
        }
    }
}
