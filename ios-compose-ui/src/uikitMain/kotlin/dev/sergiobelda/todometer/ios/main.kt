/*
 * Copyright 2022 Sergio Belda
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.sergiobelda.todometer.ios

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.main.defaultUIKitMain
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.ComposeUIViewController
import dev.sergiobelda.todometer.common.compose.ui.about.AboutDestination
import dev.sergiobelda.todometer.common.compose.ui.addtask.AddTaskDestination
import dev.sergiobelda.todometer.common.compose.ui.addtasklist.AddTaskListDestination
import dev.sergiobelda.todometer.common.compose.ui.edittask.EditTaskDestination
import dev.sergiobelda.todometer.common.compose.ui.edittasklist.EditTaskListDestination
import dev.sergiobelda.todometer.common.compose.ui.home.HomeDestination
import dev.sergiobelda.todometer.common.compose.ui.settings.SettingsDestination
import dev.sergiobelda.todometer.common.compose.ui.taskdetails.TaskDetailsDestination
import dev.sergiobelda.todometer.common.compose.ui.theme.ToDometerAppTheme
import dev.sergiobelda.todometer.common.core.di.startAppDI
import dev.sergiobelda.todometer.common.domain.preference.AppTheme
import dev.sergiobelda.todometer.common.domain.usecase.apptheme.GetAppThemeUseCase
import dev.sergiobelda.todometer.common.navigation.NavigationController
import dev.sergiobelda.todometer.common.navigation.NavigationGraph
import dev.sergiobelda.todometer.common.navigation.NavigationHost
import dev.sergiobelda.todometer.common.navigation.composableNode
import dev.sergiobelda.todometer.ios.ui.about.AboutRoute
import dev.sergiobelda.todometer.ios.ui.addtask.AddTaskRoute
import dev.sergiobelda.todometer.ios.ui.addtasklist.AddTaskListRoute
import dev.sergiobelda.todometer.ios.ui.edittask.EditTaskRoute
import dev.sergiobelda.todometer.ios.ui.edittasklist.EditTaskListRoute
import dev.sergiobelda.todometer.ios.ui.home.HomeRoute
import dev.sergiobelda.todometer.ios.ui.settings.SettingsRoute
import dev.sergiobelda.todometer.ios.ui.taskdetails.TaskDetailsRoute
import kotlinx.cinterop.useContents
import platform.UIKit.UIApplication

val koin = startAppDI().koin

fun main() {
    defaultUIKitMain(
        "ToDometer",
        ComposeUIViewController {
            val getAppThemeUseCase = koin.get<GetAppThemeUseCase>()
            val appTheme by getAppThemeUseCase.invoke().collectAsState(AppTheme.DARK_THEME)
            val darkTheme: Boolean = when (appTheme) {
                AppTheme.FOLLOW_SYSTEM -> isSystemInDarkTheme()
                AppTheme.DARK_THEME -> true
                AppTheme.LIGHT_THEME -> false
            }
            val navigationController by remember { mutableStateOf(NavigationController()) }
            ToDometerAppTheme(darkTheme) {
                NavigationHost(
                    navigationController,
                    startDestination = HomeDestination.route,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .statusBarPadding()
                ) {
                    homeComposableNode(navigationController)
                    taskDetailsComposableNode(navigationController)
                    addTaskListComposableNode(navigationController)
                    editTaskListComposableNode(navigationController)
                    addTaskComposableNode(navigationController)
                    editTaskComposableNode(navigationController)
                    settingsComposableNode(navigationController)
                    aboutComposableNode(navigationController)
                }
            }
        }
    )
}

private val statusBarInset = object : WindowInsets {
    override fun getTop(density: Density): Int =
        UIApplication.sharedApplication.keyWindow?.safeAreaInsets?.let { safeAreaInsets ->
            val topInset = safeAreaInsets.useContents { this.top }
            (topInset * density.density).toInt()
        } ?: 0

    override fun getLeft(density: Density, layoutDirection: LayoutDirection): Int = 0
    override fun getRight(density: Density, layoutDirection: LayoutDirection): Int = 0
    override fun getBottom(density: Density): Int = 0
}

fun Modifier.statusBarPadding(): Modifier =
    this.windowInsetsPadding(statusBarInset)

private fun NavigationGraph.Builder.homeComposableNode(navigationController: NavigationController) {
    composableNode(destinationId = HomeDestination.route) {
        HomeRoute(
            navigateToAddTaskList = {
                navigationController.navigateTo(AddTaskListDestination.route)
            },
            navigateToEditTaskList = {
                navigationController.navigateTo(EditTaskListDestination.route)
            },
            navigateToAddTask = {
                navigationController.navigateTo(AddTaskDestination.route)
            },
            navigateToTaskDetails = { taskId ->
                navigationController.navigateTo(
                    TaskDetailsDestination.route,
                    TaskDetailsDestination.TaskIdArg to taskId
                )
            },
            navigateToSettings = {
                navigationController.navigateTo(SettingsDestination.route)
            },
            navigateToAbout = {
                navigationController.navigateTo(AboutDestination.route)
            }
        )
    }
}

private fun NavigationGraph.Builder.taskDetailsComposableNode(navigationController: NavigationController) {
    composableNode(destinationId = TaskDetailsDestination.route) {
        val taskId =
            navigationController.getStringArgOrNull(TaskDetailsDestination.TaskIdArg)
                ?: ""
        TaskDetailsRoute(
            taskId = taskId,
            navigateToEditTask = {
                navigationController.navigateTo(
                    EditTaskDestination.route,
                    EditTaskDestination.TaskIdArg to taskId
                )
            },
            navigateBack = { navigationController.navigateTo(HomeDestination.route) }
        )
    }
}

private fun NavigationGraph.Builder.addTaskListComposableNode(navigationController: NavigationController) {
    composableNode(destinationId = AddTaskListDestination.route) {
        AddTaskListRoute(
            navigateBack = { navigationController.navigateTo(HomeDestination.route) }
        )
    }
}

private fun NavigationGraph.Builder.editTaskListComposableNode(navigationController: NavigationController) {
    composableNode(destinationId = EditTaskListDestination.route) {
        EditTaskListRoute(
            navigateBack = { navigationController.navigateTo(HomeDestination.route) }
        )
    }
}

private fun NavigationGraph.Builder.addTaskComposableNode(navigationController: NavigationController) {
    composableNode(destinationId = AddTaskDestination.route) {
        AddTaskRoute(
            navigateBack = { navigationController.navigateTo(HomeDestination.route) }
        )
    }
}

private fun NavigationGraph.Builder.editTaskComposableNode(navigationController: NavigationController) {
    composableNode(destinationId = EditTaskDestination.route) {
        val taskId =
            navigationController.getStringArgOrNull(EditTaskDestination.TaskIdArg)
                ?: ""
        EditTaskRoute(
            taskId = taskId,
            navigateBack = {
                navigationController.navigateTo(
                    TaskDetailsDestination.route,
                    TaskDetailsDestination.TaskIdArg to taskId
                )
            }
        )
    }
}

private fun NavigationGraph.Builder.settingsComposableNode(navigationController: NavigationController) {
    composableNode(destinationId = SettingsDestination.route) {
        SettingsRoute(
            navigateBack = { navigationController.navigateTo(HomeDestination.route) }
        )
    }
}

private fun NavigationGraph.Builder.aboutComposableNode(navigationController: NavigationController) {
    composableNode(destinationId = AboutDestination.route) {
        AboutRoute(
            navigateBack = { navigationController.navigateTo(HomeDestination.route) }
        )
    }
}
