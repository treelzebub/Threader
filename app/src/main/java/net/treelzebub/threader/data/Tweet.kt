package net.treelzebub.threader.data

/**
 * Created by Tre Murillo on 8/19/2017
 *
 * @param position The position of the tweet in the thread
 * @param text The text of the tweet
 *
 * @property count The number of characters in the tweet
 * @property remaining The remaining character count
 */
data class Tweet(override var position: Int = 0, var text: String? = null) : Indexed {

    companion object {
        private val URL_SCHEMES = listOf("http://", "https://")
        private const val MAX_TWEET_LENGTH = 140
        // Twitter allows long urls in tweets by only counting the first several characters.
        private const val MAX_URL_LENGTH = 23

        // TODO handle more than one url in tweet
        fun count(string: String?): Int {
            if (string == null) return 0

            var count = string.length
            val hasUrl = URL_SCHEMES.any { it in string }
            if (hasUrl) {
                // TODO urls max out at 23 chars, then it stops counting
            }
            return count
        }
    }

    val count: Int get() = count(text)
    val remaining: Int get() = MAX_TWEET_LENGTH - count
}
