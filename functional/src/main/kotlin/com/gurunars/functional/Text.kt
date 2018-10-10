package com.gurunars.functional

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.widget.EditText
import android.widget.TextView

data class TextAppearance(
    val textStyle: Set<TextStyle> = setOf(),
    val color: Color = Color("#000000"),
    val size: Size = 12.sp,
    val gravity: Gravity = Gravity.LEFT
) {
    enum class TextStyle {
        BOLD, ITALIC, UNDERLINE, STRIKE_THROUGH
    }
}

interface WithTextAppearence {
    val textAppearance: TextAppearance
}

fun<T: WithTextAppearence, V: TextView> textAppearanceChangeSpec() = changeSpecs<T, V> {
    rendersTo({ textAppearance.color }) { setTextColor(it.value) }
    rendersTo({ textAppearance.gravity }) { gravity = it.value }
    rendersTo({ textAppearance.size }) { textSize = context.toInt(it).toFloat() }
    rendersTo({ textAppearance.textStyle }) {
        when {
            it.isEmpty()
            -> setTypeface(typeface, Typeface.NORMAL)
            it.containsAll(setOf(
                TextAppearance.TextStyle.BOLD,
                TextAppearance.TextStyle.ITALIC
            ))
            -> setTypeface(typeface, Typeface.BOLD_ITALIC)
            it.contains(TextAppearance.TextStyle.BOLD)
            -> setTypeface(typeface, Typeface.BOLD)
            it.contains(TextAppearance.TextStyle.ITALIC)
            -> setTypeface(typeface, Typeface.ITALIC)
        }

        paintFlags = when {
            it.contains(TextAppearance.TextStyle.UNDERLINE)
            -> paintFlags or Paint.UNDERLINE_TEXT_FLAG
            it.contains(TextAppearance.TextStyle.STRIKE_THROUGH)
            -> paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            else
            -> paintFlags
        }
    }
}

data class Input(
    override val textAppearance: TextAppearance = TextAppearance(),
    val value: Box<String>
): WithTextAppearence

class InputBinder : ElementBinder {
    private val changeSpec = changeSpecs<Input, EditText> {
        rendersTo({ value })  { setText(it.get()) }
    } + textAppearanceChangeSpec()

    override val empty = Text()
    override fun getEmptyTarget(context: Context) = EditText(context).apply {
        setSingleLine(true)
    }
    override fun diff(context: Context, old: Any, new: Any): List<Mutation> = changeSpec.diff(
        old as Input,
        new as Input
    )
}

data class Text(
    override val textAppearance: TextAppearance = TextAppearance(),
    val value: String = ""
): WithTextAppearence

class TextBinder : ElementBinder {
    private val changeSpec = changeSpecs<Text, TextView> {
        rendersTo({ value })  { text = it }
    } + textAppearanceChangeSpec()

    override val empty = Text()
    override fun getEmptyTarget(context: Context) = TextView(context).apply {
        setSingleLine(true)
    }
    override fun diff(context: Context, old: Any, new: Any): List<Mutation> = changeSpec.diff(
        old as Text,
        new as Text
    )
}