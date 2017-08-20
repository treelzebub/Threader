package net.treelzebub.threader.ui.tweets

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import net.treelzebub.threader.data.Tweet
import net.treelzebub.threader.data.linkedListOf

/**
 * Created by Tre Murillo on 8/20/2017
 */
class TweetAdapter2 : RecyclerView.Adapter<TweetHolder>() {

    var tweets = linkedListOf<Tweet>()
        private set

    fun addTweet(index: Int) {
//        if (tweets.size <= 1) {
//            tweets = linkedListOf(Tweet(index))
//        } else {
//            tweets =
//        }
    }

    override fun onBindViewHolder(holder: TweetHolder?, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TweetHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class TweetHolder(view: View) : RecyclerView.ViewHolder(view) {

}