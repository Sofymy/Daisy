package com.example.daisy.feature.new_calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.daisy.feature.new_calendar.pages.NewCalendarDate
import com.example.daisy.feature.new_calendar.pages.NewCalendarRecipient
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
    val pagerState = rememberPagerState(pageCount = { 2 })
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

    HorizontalPager(
        state = pagerState,
        userScrollEnabled = false
    ) { page ->
        Column(modifier = Modifier.fillMaxSize()) {
            NewCalendarPagerContent(
                page = page,
                onClickNext = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                },
                onClickCreate = {
                    viewModel.onEvent(NewCalendarUserEvent.CreateCalendar)
                }
            )
        }
    }
}

@Composable
fun NewCalendarPagerContent(
    page: Int,
    onClickNext: () -> Unit,
    onClickCreate: () -> Unit
) {
    when (page) {
        0 -> NewCalendarDate(onClickNext = onClickNext)
        1 -> NewCalendarRecipient(onClickCreate = onClickCreate)
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
