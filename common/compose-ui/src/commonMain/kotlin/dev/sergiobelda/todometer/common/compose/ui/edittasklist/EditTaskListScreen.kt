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

package dev.sergiobelda.todometer.common.compose.ui.edittasklist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import dev.sergiobelda.todometer.common.compose.ui.components.SaveActionTopAppBar
import dev.sergiobelda.todometer.common.compose.ui.designsystem.components.ToDometerContentLoadingProgress
import dev.sergiobelda.todometer.common.compose.ui.designsystem.components.ToDometerTitledTextField
import dev.sergiobelda.todometer.common.compose.ui.values.TextFieldPadding
import dev.sergiobelda.todometer.common.resources.MR
import dev.sergiobelda.todometer.common.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskListScreen(
    navigateBack: () -> Unit,
    editTaskList: (String) -> Unit,
    editTaskListUiState: EditTaskListUiState
) {
    var taskListName by rememberSaveable { mutableStateOf("") }
    var taskListNameInputError by remember { mutableStateOf(false) }
    editTaskListUiState.taskList?.let { taskList ->
        taskListName = taskList.name
    }
    Scaffold(
        topBar = {
            SaveActionTopAppBar(
                navigateBack = navigateBack,
                title = stringResource(MR.strings.edit_task_list),
                isSaveButtonEnabled = !editTaskListUiState.isLoading && editTaskListUiState.taskList != null,
                onSaveButtonClick = {
                    if (taskListName.isBlank()) {
                        taskListNameInputError = true
                    } else {
                        editTaskList(taskListName)
                        navigateBack()
                    }
                }
            )
        },
        content = { paddingValues ->
            if (editTaskListUiState.isLoading) {
                ToDometerContentLoadingProgress()
            } else {
                Column(modifier = Modifier.padding(paddingValues)) {
                    ToDometerTitledTextField(
                        title = stringResource(MR.strings.name),
                        value = taskListName,
                        onValueChange = {
                            taskListName = it
                            taskListNameInputError = false
                        },
                        placeholder = { Text(stringResource(MR.strings.enter_task_list_name)) },
                        singleLine = true,
                        isError = taskListNameInputError,
                        errorMessage = stringResource(MR.strings.field_not_empty),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.padding(TextFieldPadding)
                    )
                }
            }
        }
    )
}
