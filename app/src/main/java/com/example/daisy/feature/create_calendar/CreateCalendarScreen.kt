package com.example.daisy.feature.create_calendar

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.daisy.feature.create_calendar.pages.CreateCalendarDate
import com.example.daisy.feature.create_calendar.pages.CreateCalendarRecipient
import kotlinx.coroutines.launch

@Composable
fun CreateCalendarScreen(){

    CreateCalendarContent()
}

@Composable
fun CreateCalendarContent(

) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val pagerTabs = createCalendarPagerTabs()
    val scope = rememberCoroutineScope()

    CreateCalendarTabs(
        pagerTabs = pagerTabs,
        currentPage = pagerState.currentPage,
        onClick = {
            scope.launch {
                pagerState.scrollToPage(it)
            }
        }
    )

    HorizontalPager(
        state = pagerState
    ) { page ->
        Column(modifier = Modifier.fillMaxSize()) {
            CreateCalendarPagerContent(
                page = page,
            )
        }
    }
}

@Composable
fun CreateCalendarPagerContent(
    page: Int
) {
    when (page) {
        0 -> CreateCalendarDate()
        1 -> CreateCalendarRecipient()
    }
}

@Composable
fun CreateCalendarTabs(
    pagerTabs: List<CreateCalendarPagerTab>,
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
fun createCalendarPagerTabs(): List<CreateCalendarPagerTab> = listOf(
    CreateCalendarPagerTab.DateRange,
    CreateCalendarPagerTab.Recipient,
)

sealed class CreateCalendarPagerTab(
    val title: String,
) {
    data object DateRange : CreateCalendarPagerTab("DateRange")
    data object Recipient : CreateCalendarPagerTab("Recipient")
}
