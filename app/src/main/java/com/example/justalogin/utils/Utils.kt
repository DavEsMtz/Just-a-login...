package com.example.justalogin.utils

import android.app.Activity
import android.graphics.Typeface
import android.text.Selection
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.justalogin.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


fun View.show(): View {
    if (visibility != View.VISIBLE)
        visibility = View.VISIBLE
    return this
}

fun View.hide(): View {
    if (visibility != View.GONE)
        visibility = View.GONE
    return this
}

/**
 * Handler to collect a flow in a secure way
 */
fun <T> Fragment.safeFlowCollection(
    flow: Flow<T>,
    state: Lifecycle.State = Lifecycle.State.RESUMED,
    action: (T) -> Unit
) =
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(state) {
            flow.collect { action(it) }
        }
    }

fun Activity.hideSoftKeyboard() {
    currentFocus?.let {
        val inputMethodManager =
            ContextCompat.getSystemService(this, InputMethodManager::class.java)
        inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

/**
 *  Handles a spannable text for link
 */
fun TextView.makeLink(link: Pair<String, () -> Unit>) {
    /* This can be improved with a list to handle multiple links
    * handling position updates with [startLinkIndex]*/
    val spannableString = SpannableString(this.text)
    val clickableSpan = object : ClickableSpan() {
        override fun updateDrawState(textPaint: TextPaint) {
            super.updateDrawState(textPaint)
            // Change text appearance here
            textPaint.color = resources.getColor(R.color.purpleDark)
            textPaint.isUnderlineText = false
            textPaint.typeface = Typeface.DEFAULT_BOLD
        }

        override fun onClick(view: View) {
            Selection.setSelection((view as TextView).text as Spannable, 0)
            view.invalidate()
            link.second.invoke()
        }
    }
    val startLinkIndex: Int = this.text.toString().indexOf(link.first)
    spannableString.setSpan(
        clickableSpan, startLinkIndex, startLinkIndex + link.first.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    text = spannableString
    movementMethod = LinkMovementMethod.getInstance()
}