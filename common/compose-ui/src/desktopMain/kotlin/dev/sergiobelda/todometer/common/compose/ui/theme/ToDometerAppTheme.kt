/*
 * Copyright 2023 Sergio Belda
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

package dev.sergiobelda.todometer.common.compose.ui.theme

import androidx.compose.runtime.Composable
import dev.sergiobelda.todometer.common.compose.ui.designsystem.theme.DarkColorScheme
import dev.sergiobelda.todometer.common.compose.ui.designsystem.theme.LightColorScheme
import dev.sergiobelda.todometer.common.compose.ui.designsystem.theme.ToDometerDarkColors
import dev.sergiobelda.todometer.common.compose.ui.designsystem.theme.ToDometerLightColors
import dev.sergiobelda.todometer.common.compose.ui.designsystem.theme.ToDometerTheme
import dev.sergiobelda.todometer.common.compose.ui.designsystem.theme.Type
import dev.sergiobelda.todometer.common.compose.ui.designsystem.theme.shapes

@Composable
actual fun ToDometerAppTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val toDometerColors = if (darkTheme) ToDometerDarkColors else ToDometerLightColors
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    ToDometerTheme(
        toDometerColors = toDometerColors,
        colorScheme = colorScheme,
        shapes = shapes,
        typography = Type.typography,
        content = content
    )
}
