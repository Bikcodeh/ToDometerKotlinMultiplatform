/*
 * Copyright 2021 Sergio Belda
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

package dev.sergiobelda.todometer.common.compose.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.sergiobelda.todometer.common.resources.MR
import dev.sergiobelda.todometer.common.resources.ToDometerSymbols
import dev.sergiobelda.todometer.common.resources.stringResource

@Composable
internal fun ToDometerTitle(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = ToDometerSymbols.IsotypeMonochrome,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = null,
            modifier = Modifier.size(ToDometerTitleIconSize)
        )
        Text(
            text = stringResource(MR.strings.app_name),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(
                start = ToDometerTitleTextPaddingStart,
                bottom = ToDometerTitleTextPaddingBottom
            )
        )
    }
}

private val ToDometerTitleIconSize: Dp = 24.dp
private val ToDometerTitleTextPaddingStart: Dp = 6.dp
private val ToDometerTitleTextPaddingBottom: Dp = 4.dp
