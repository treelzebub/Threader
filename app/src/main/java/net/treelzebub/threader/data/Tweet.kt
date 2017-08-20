package net.treelzebub.threader.data

/**
 * Created by Tre Murillo on 8/19/2017
 *
 * @param text The text of the tweet
 * @property count The character count. Defaults to
 */
data class Tweet(val text: String? = null) {

    companion object {
        // Twitter allows long urls in tweets by only counting the first several characters.
        private const val MAX_URL_LENGTH = 23
        private val URL_SCHEMES = listOf("http://", "https://")

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
}
