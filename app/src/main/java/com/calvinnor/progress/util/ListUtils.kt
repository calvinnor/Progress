package com.calvinnor.progress.util

fun <T> MutableList<T>.swap(left: Int, right: Int) {
    val leftElem = this.get(left)
    val rightElem = this.get(right)
    this.apply {
        set(left, rightElem)
        set(right, leftElem)
    }
}

fun <T> MutableList<T>.positionOf(elem: T): Int {
    this.forEachIndexed { index, t ->
        if (elem!!.equals(t)) return index
    }
    return -1
}
