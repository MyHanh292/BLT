package com.example.chamsocthucung2.ui.components

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val items = listOf(
        "home_screen",
        "appointment_detail_screen",
        "pet_profiledetail",
        "chat_screen",
        "profile_screen"
    )
    val icons = listOf(
        Icons.Default.Home,
        Icons.Default.CalendarToday,
        Icons.Default.Person,
        Icons.Default.Chat,
        Icons.Default.AccountBox
    )
    val labels = listOf(
        "Home",
        "Đặt lịch",
        "Hồ sơ",
        "Chat",
        "Tài khoản"
    )

    BottomNavigation(
        modifier = Modifier.navigationBarsPadding(),
        backgroundColor = Color(0xFFFF9800),
        contentColor = Color(0xFF424242)
    ) {
        items.forEachIndexed { index, route ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        icons[index],
                        contentDescription = labels[index],
                        tint = if (currentRoute == route) Color(0xFFFFF3E0) else Color(0xFF424242)
                    )
                },
                label = {
                    Text(
                        labels[index],
                        color = if (currentRoute == route) Color(0xFFFFF3E0) else Color(0xFF424242)
                    )
                },
                selected = currentRoute == route,
                onClick = {
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                selectedContentColor = Color(0xFFFF5722),
                unselectedContentColor = Color(0xFF424242)
            )
        }
    }
}