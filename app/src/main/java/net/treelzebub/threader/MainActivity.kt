package net.treelzebub.threader

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.item_tweet_view.view.*
import net.treelzebub.threader.android.copyToClipboard
import net.treelzebub.threader.data.Tweet
import net.treelzebub.threader.data.TweetStore
import net.treelzebub.threader.ui.tweets.TweetAdapter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity(), TweetAdapter.TweetAdapterListener {

    private val tweetAdapter = TweetAdapter(this, this)

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
//            addOnScrollListener(object : RecyclerView.OnScrollListener() {
//                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {}
//                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
//                        dismissKeyboard()
//                    }
//                }
//            })
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
                // TODO are you sure?
                TweetStore.clear(this)
                tweetAdapter.clear()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onTweetAdded(position: Int, tweet: Tweet) {
        doAsync {
            var child: View? = null
            while (child == null) {
                child = recycler.findViewWithTag(tweet)
                Thread.sleep(15L)
            }
            uiThread {
                recycler.layoutManager.scrollToPosition(position)
                child!!.text.requestFocus()
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
        recycler.smoothScrollToPosition(0)
        runOnUiThread {
            val tweetView = recycler.layoutManager.getChildAt(0).text
            tweetView.selectAll()
            val text = tweetView.text.toString()
            if (text.isNotBlank()) {
                copyToClipboard(text)
                toast("Copied tweet.")
            }
        }
    }
}