package net.treelzebub.threader.data

import java.util.*

/**
 * Created by Tre Murillo on 8/20/2017
 */

fun <T> linkedListOf(): LinkedList<T> = LinkedList(emptyList())
fun <T> linkedListOf(element: T): LinkedList<T> = LinkedList(listOf(element))
fun <T> linkedListOf(vararg elements: T): LinkedList<T>
        = if (elements.isNotEmpty()) LinkedList(elements.toList()) else linkedListOf()

fun List<Tweet>.splitAt(index: Int) = partition { index >= it.number }