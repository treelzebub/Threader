package net.treelzebub.threader

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.item_tweet_view.view.*
import net.treelzebub.threader.android.copyToClipboard
import net.treelzebub.threader.android.toast
import net.treelzebub.threader.ui.tweets.TweetAdapter

class MainActivity : AppCompatActivity() {

    private val adapter = TweetAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        addTweetView(0)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_done -> {
                recycler.smoothScrollToPosition(0)
                copyToClipboard(recycler.getChildAt(0).text.toString())
                toast("Copied tweet.")
                true
            }
            R.id.action_clear -> {
                // TODO are you sure?
                adapter.clear()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Add a new TweetEditText below the given index. Default will add to the end of the list.
    private fun addTweetView(index: Int) = adapter.addTweet()

    private fun removeTweetView(index: Int) = adapter.removeTweet(index)
}
