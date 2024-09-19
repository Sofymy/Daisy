package com.example.daisy.feature.calendars

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.daisy.feature.calendars.created_calendars.CreatedCalendarsScreen
import com.example.daisy.feature.calendars.received_calendars.ReceivedCalendarsScreen
import com.example.daisy.ui.common.elements.PrimaryTextField
import com.example.daisy.ui.theme.DarkGrey
import com.example.daisy.ui.theme.MediumGrey
import com.example.daisy.ui.theme.Purple
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CalendarsScreen(
    onNavigateToCreatedCalendar: (String) -> Unit
){
    CalendarsScreenContent(
        onNavigateToCreatedCalendar
    )
}

@Composable
fun CalendarsScreenContent(
    onNavigateToCreatedCalendar: (String) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = {2})
    val show = remember {
        mutableStateOf(false)
    }

    Column(
        Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ){
            CalendarsTabViewBackGround()
            TabView(pagerState)
        }
        Spacer(modifier = Modifier.height(20.dp))
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> ReceivedCalendarsScreen()
                1 -> CreatedCalendarsScreen(onNavigateToCreatedCalendar)
            }
        }
    }
}

@Composable
fun CalendarsTabViewBackGround(

) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 25.dp)
            .background(MediumGrey, RoundedCornerShape(0.dp, 0.dp, 50.dp, 50.dp))
        ,
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            Modifier
                .clip(RoundedCornerShape(50.dp))
                .background(MediumGrey)
                .fillMaxWidth()
                .matchParentSize()
                .blur(20.dp)
                .drawBehind {
                    drawCircle(
                        Color.Black.copy(.2f),
                        center = Offset(150f, size.height + 400),
                        radius = 650f
                    )
                }
        )
        Column {
            PrimaryTextField(value = "", onValueChange = {}, icon = Icons.Default.Search, placeholderText = "Search Calendar")
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}


enum class SubComposeID {
    PRE_CALCULATE_ITEM,
    ITEM,
    INDICATOR
}

data class TabPosition(
    val left: Dp, val width: Dp
)


// SOURCE: https://medium.com/@gautier.louis/tab-row-made-easy-in-jetpack-compose-8eaa5a602744

@Composable
fun TabView(
    pagerState: PagerState
) {
    var selectedTabPosition by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    val items = listOf(
        "Received", "Created"
    )

    LaunchedEffect(pagerState.currentPage) {
        selectedTabPosition = pagerState.currentPage
    }

    TabRow(
            selectedTabPosition = selectedTabPosition
            ) {
        items.forEachIndexed { index, s ->
            TabTitle(s, position = index, isSelected = index == selectedTabPosition) {
                selectedTabPosition = index
                scope.launch {
                    pagerState.animateScrollToPage(index)
                }
            }
        }
    }
}

@Composable
fun TabRow(
    containerColor: Color = MediumGrey,
    indicatorColor: Color = MaterialTheme.colorScheme.primary,
    containerShape: Shape = RoundedCornerShape(57.dp),
    indicatorShape: Shape = RoundedCornerShape(50.dp),
    paddingValues: PaddingValues = PaddingValues(7.dp),
    animationSpec: AnimationSpec<Dp> = tween(durationMillis = 250, easing = FastOutSlowInEasing),
    fixedSize: Boolean = true,
    selectedTabPosition: Int = 0,
    tabItem: @Composable () -> Unit
) {
    Surface(
        color = containerColor,
        shape = containerShape,
        modifier = Modifier
            .border(1.dp, Color.White.copy(.1f), indicatorShape)
    ) {
        SubcomposeLayout(
            Modifier
                .padding(paddingValues)
                .selectableGroup()
        ) { constraints ->
            val tabMeasurable: List<Placeable> = subcompose(SubComposeID.PRE_CALCULATE_ITEM, tabItem)
                .map { it.measure(constraints) }

            val itemsCount = tabMeasurable.size
            val maxItemWidth = tabMeasurable.maxOf { it.width }
            val maxItemHeight = tabMeasurable.maxOf { it.height }

            val tabPlaceable = subcompose(SubComposeID.ITEM, tabItem).map {
                val c = if (fixedSize) constraints.copy(
                    minWidth = maxItemWidth,
                    maxWidth = maxItemWidth,
                    minHeight = maxItemHeight
                ) else constraints
                it.measure(c)
            }

            val tabPositions = tabPlaceable.mapIndexed { index, placeable ->
                val itemWidth = if (fixedSize) maxItemWidth else placeable.width
                val x = if (fixedSize) {
                    maxItemWidth * index
                } else {
                    val leftTabWith = tabPlaceable.take(index).sumOf { it.width }
                    leftTabWith
                }
                TabPosition(x.toDp(), itemWidth.toDp())
            }

            val tabRowWidth = if (fixedSize) maxItemWidth * itemsCount
            else tabPlaceable.sumOf { it.width }

            layout(tabRowWidth, maxItemHeight) {
                subcompose(SubComposeID.INDICATOR) {
                    Box(
                        Modifier
                            .tabIndicator(tabPositions[selectedTabPosition], animationSpec)
                            .fillMaxWidth()
                            .height(maxItemHeight.toDp())
                            .background(color = indicatorColor, indicatorShape)
                    )
                }.forEach {
                    it.measure(Constraints.fixed(tabRowWidth, maxItemHeight)).placeRelative(0, 0)
                }

                tabPlaceable.forEachIndexed { index, placeable ->
                    val x = if (fixedSize) {
                        maxItemWidth * index
                    } else {
                        val leftTabWith = tabPlaceable.take(index).sumOf { it.width }
                        leftTabWith
                    }
                    placeable.placeRelative(x, 0)
                }
            }
        }
    }
}

fun Modifier.tabIndicator(
    tabPosition: TabPosition,
    animationSpec: AnimationSpec<Dp>,
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "tabIndicatorOffset"
        value = tabPosition
    }
) {
    val currentTabWidth by animateDpAsState(
        targetValue = tabPosition.width,
        animationSpec = animationSpec, label = ""
    )
    val indicatorOffset by animateDpAsState(
        targetValue = tabPosition.left,
        animationSpec = animationSpec, label = ""
    )
    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorOffset)
        .width(currentTabWidth)
        .fillMaxHeight()
}

@Composable
fun TabTitle(
    title: String,
    position: Int,
    isSelected: Boolean,
    onClick: (Int) -> Unit,
) {
    val textColor = animateColorAsState(targetValue = if(isSelected) Color.White else Color.White.copy(.2f),
        label = ""
    )

    Text(
        text = title,
        Modifier
            .wrapContentWidth(Alignment.CenterHorizontally)
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) { onClick(position) },
        color = textColor.value,
        fontWeight = FontWeight.Bold,
    )
}