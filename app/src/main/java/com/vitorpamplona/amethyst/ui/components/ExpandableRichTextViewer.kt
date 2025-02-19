package com.vitorpamplona.amethyst.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vitorpamplona.amethyst.R
import com.vitorpamplona.amethyst.ui.actions.ImmutableListOfLists
import com.vitorpamplona.amethyst.ui.screen.loggedIn.AccountViewModel
import com.vitorpamplona.amethyst.ui.theme.ButtonBorder
import com.vitorpamplona.amethyst.ui.theme.secondaryButtonBackground

const val SHORT_TEXT_LENGTH = 350

@Composable
fun ExpandableRichTextViewer(
    content: String,
    canPreview: Boolean,
    modifier: Modifier,
    tags: ImmutableListOfLists<String>,
    backgroundColor: MutableState<Color>,
    accountViewModel: AccountViewModel,
    nav: (String) -> Unit
) {
    var showFullText by remember { mutableStateOf(false) }

    val whereToCut = remember(content) {
        // Cuts the text in the first space after 350
        val firstSpaceAfterCut = content.indexOf(' ', SHORT_TEXT_LENGTH).let { if (it < 0) content.length else it }
        val firstNewLineAfterCut = content.indexOf('\n', SHORT_TEXT_LENGTH).let { if (it < 0) content.length else it }

        minOf(firstSpaceAfterCut, firstNewLineAfterCut)
    }

    val text by remember(content) {
        derivedStateOf {
            if (showFullText) {
                content
            } else {
                content.take(whereToCut)
            }
        }
    }

    Box {
        RichTextViewer(
            text,
            canPreview,
            modifier.align(Alignment.TopStart),
            tags,
            backgroundColor,
            accountViewModel,
            nav
        )

        if (content.length > whereToCut && !showFullText) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .drawBehind {
                        drawRect(
                            Brush.verticalGradient(
                                colors = listOf(
                                    backgroundColor.value.copy(alpha = 0f),
                                    backgroundColor.value
                                )
                            )
                        )
                    }
            ) {
                ShowMoreButton() {
                    showFullText = !showFullText
                }
            }
        }
    }
}

@Composable
fun ShowMoreButton(onClick: () -> Unit) {
    Button(
        modifier = Modifier.padding(top = 10.dp),
        onClick = onClick,
        shape = ButtonBorder,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.secondaryButtonBackground
        ),
        contentPadding = PaddingValues(vertical = 6.dp, horizontal = 16.dp)
    ) {
        Text(text = stringResource(R.string.show_more), color = Color.White)
    }
}
