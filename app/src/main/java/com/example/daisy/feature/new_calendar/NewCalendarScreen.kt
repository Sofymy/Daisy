package com.example.daisy.feature.new_calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.daisy.feature.new_calendar.pages.NewCalendarDate
import com.example.daisy.feature.new_calendar.pages.NewCalendarPersonalize
import com.example.daisy.feature.new_calendar.pages.NewCalendarPreview
import com.example.daisy.feature.new_calendar.pages.NewCalendarRecipient
import com.example.daisy.ui.common.elements.PrimaryButton
import com.example.daisy.ui.common.elements.SecondaryButton
import com.example.daisy.ui.theme.DarkGrey
import com.example.daisy.ui.theme.Purple
import com.example.daisy.ui.util.UiEvent
import kotlinx.coroutines.launch

@Composable
fun NewCalendarScreen(
    onNavigateToHome: () -> Unit
){
    NewCalendarContent(
        onNavigateToHome = onNavigateToHome
    )
}

@Composable
fun NewCalendarContent(
    viewModel: NewCalendarViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(pageCount = { 4 })
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when(event) {
                is UiEvent.Success -> {
                    onNavigateToHome()
                }
                is UiEvent.Error -> {

                }
            }
        }
    }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false
        ) { page ->
            Column(modifier = Modifier.fillMaxSize()) {
                NewCalendarPagerContent(
                    page = page,
                )
            }
        }
        Row(
            Modifier
                .background(Brush.verticalGradient(listOf(Color.Transparent, DarkGrey, DarkGrey)))
                .padding(bottom = 55.dp, start = 20.dp, end = 20.dp, top = 20.dp)
                .fillMaxWidth()
        ) {
            val isFirstPage = pagerState.currentPage == 0
            val isLastPage = pagerState.currentPage == 3

            if (!isFirstPage) {
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    PrimaryButton(
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        },
                    ) {
                        Text("Back", color = Purple)
                    }
                }
                Spacer(modifier = Modifier.weight(0.1f))
            }

            if (isLastPage) {
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    SecondaryButton(
                        onClick = {
                            viewModel.onEvent(NewCalendarUserEvent.CreateCalendar)
                        },
                    ) {
                        Text("Create calendar")
                    }
                }
            } else {
                Column(
                    Modifier.weight(1f)
                ) {
                    SecondaryButton(
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                    ) {
                        Text("Next")
                    }
                }
            }

        }
    }
}

@Composable
fun NewCalendarPagerContent(
    page: Int,
) {
    when (page) {
        0 -> NewCalendarDate()
        1 -> NewCalendarRecipient()
        2 -> NewCalendarPersonalize()
        3 -> NewCalendarPreview()
    }
}

@Composable
fun NewCalendarTabs(
    pagerTabs: List<NewCalendarPagerTab>,
    currentPage: Int,
    onClick: (Int) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = currentPage,
        indicator = { tabPositions ->
            if (currentPage < tabPositions.size) {
                SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[currentPage]),
                    height = 5.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        divider = {},
        modifier = Modifier.padding(bottom = 20.dp)
    ) {
        pagerTabs.forEachIndexed { index, tab ->
            Tab(
                selectedContentColor = MaterialTheme.colorScheme.onBackground,
                unselectedContentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                text = { Text(tab.title) },
                selected = currentPage == index,
                onClick = {
                    onClick(index)
                }
            )
        }
    }
}

@Composable
fun newCalendarPagerTabs(): List<NewCalendarPagerTab> = listOf(
    NewCalendarPagerTab.DateRange,
    NewCalendarPagerTab.Recipient,
)

sealed class NewCalendarPagerTab(
    val title: String,
) {
    data object DateRange : NewCalendarPagerTab("DateRange")
    data object Recipient : NewCalendarPagerTab("Recipient")
}


// SOURCE: https://blog.canopas.com/jetpack-compose-typewriter-animation-with-highlighted-texts-74397fee42f1

@Composable
fun NewCalendarTypewriterText(
    baseText: String,
    underlinedText: String,
) {

    val totalTextBeforePart = baseText.length
    var partTextRects by remember { mutableStateOf(listOf<Rect>()) }

    Text(
        text = buildAnnotatedString {
            append(baseText)
            append(underlinedText)
        },
        style = TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 30.sp,
            letterSpacing = -(1.6).sp,
            lineHeight = 60.sp
        ),
        modifier = Modifier
            .drawBehind {
                val borderSize = 14.sp.toPx()
                partTextRects.forEach { rect ->
                    val selectedRect = rect.translate(0f, -borderSize / 1.5f)
                    drawLine(
                        color = Purple,
                        start = Offset(selectedRect.left, selectedRect.bottom),
                        end = selectedRect.bottomRight,
                        strokeWidth = borderSize
                    )
                }
            },
        onTextLayout = { layoutResult ->
            val partTextEnd =
                totalTextBeforePart + underlinedText.length.coerceAtMost(layoutResult.layoutInput.text.length)

            partTextRects =
                if (totalTextBeforePart <= partTextEnd && partTextEnd <= layoutResult.layoutInput.text.length) {
                    layoutResult.getBoundingBoxesForRange(
                        start = totalTextBeforePart,
                        end = partTextEnd -1
                    )
                } else {
                    emptyList()
                }
        }
    )
}


fun TextLayoutResult.getBoundingBoxesForRange(start: Int, end: Int): List<Rect> {
    var prevRect: Rect? = null
    var firstLineCharRect: Rect? = null
    val boundingBoxes = mutableListOf<Rect>()
    for (i in start..end) {
        val rect = getBoundingBox(i)
        val isLastRect = i == end

        if (isLastRect && firstLineCharRect == null) {
            firstLineCharRect = rect
            prevRect = rect
        }
        if (!isLastRect && rect.right == 0f) continue

        if (firstLineCharRect == null) {
            firstLineCharRect = rect
        } else if (prevRect != null) {
            if (prevRect.bottom != rect.bottom || isLastRect) {
                boundingBoxes.add(
                    firstLineCharRect.copy(right = prevRect.right)
                )
                firstLineCharRect = rect
            }
        }
        prevRect = rect
    }
    return boundingBoxes
}