package net.treelzebub.threader.data

import android.content.Context
import android.os.Build

/**
 * Created by Tre Murillo on 8/19/2017
 */
object TweetStore {

    private const val name = "net.treelzebub.threader"

    private fun prefs(c: Context) = c.getSharedPreferences(name, Context.MODE_PRIVATE)

    fun persist(c: Context, tweets: List<Tweet>) {
        val editor = prefs(c).edit()
        tweets.forEach {
            editor.putString("${it.position}", it.text)
        }
        editor.apply()
    }

    @Suppress("UNCHECKED_CAST")
    fun load(c: Context): List<Tweet> {
        val prefs = prefs(c)
        val all = prefs.all as Map<String, String>
        return all.map {
            Tweet(Integer.parseInt(it.key), it.value)
        }.sortedBy { it.position }
    }

    fun clear(c: Context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            c.deleteSharedPreferences(name)
        } else {
            prefs(c).edit().clear().apply()
        }
    }
}