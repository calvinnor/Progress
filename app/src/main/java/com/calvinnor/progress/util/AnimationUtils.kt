@file: JvmName("AnimationUtils")

package com.calvinnor.progress.util

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator

/**
 * Fades between two provided colors
 *
 * @param from: The initial color
 * @param to: The final color
 * @param update: Function which will receive color updates.
 */
fun fadeColors(from: Int, to: Int, update: (color: Int) -> Any) {
    if (from == to) return
    val anim = ValueAnimator.ofInt(from, to)
    anim.setEvaluator(ArgbEvaluator())
    anim.addUpdateListener { animation ->
        update(animation.animatedValue as Int)
    }
    anim.start()
}
