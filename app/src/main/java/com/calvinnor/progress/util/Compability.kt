@file:JvmName("Compatibility")

package com.calvinnor.progress.util

import android.os.Build

fun isOreo() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
