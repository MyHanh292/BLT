package com.example.chamsocthucung2.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

fun parseMarkdown(text: String): AnnotatedString {
    val annotatedString = buildAnnotatedString {
        var index = 0
        var inBold = false
        var inItalic = false

        while (index < text.length) {
            when {
                text.startsWith("**", index) -> {
                    if (inBold) {
                        pushStyle(SpanStyle(fontWeight = FontWeight.Normal))
                        inBold = false
                    } else {
                        pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                        inBold = true
                    }
                    index += 2
                }
                text.startsWith("*", index) -> {
                    if (inItalic) {
                        pushStyle(SpanStyle(fontStyle = FontStyle.Normal))
                        inItalic = false
                    } else {
                        pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                        inItalic = true
                    }
                    index += 1
                }
                else -> {
                    append(text[index])
                    index++
                }
            }
        }
    }
    return annotatedString
}