package ru.gb.searchmovies

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar (
    text : String,
    actionText : String,
    action: () -> Unit,
    length : Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar.make(this, text, length)
        .setAction(actionText) { action }
        .show()
}
fun View.showSnackBar (
    text : Int,
    actionText : Int,
    action: () -> Unit,
    length : Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar.make(this, text, length)
        .setAction(actionText) { action }
        .show()
}
fun View.showSnackBar (
    text : String,
    length : Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar.make(this, text, length)
        .show()
}
fun View.showSnackBar (
    text : Int,
    length : Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar.make(this, text, length)
        .show()
}

fun View.hide () {
    if (visibility != View.GONE)
        visibility = View.GONE
}

fun View.show () {
    if (visibility != View.VISIBLE)
        visibility = View.VISIBLE
}

fun View.hideIf(condition : () -> Boolean) {
    if (visibility != View.GONE && condition())
        visibility = View.GONE
}