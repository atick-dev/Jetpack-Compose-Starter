/*
 * Copyright 2023 Atick Faisal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.atick.core.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable

@Stable
@Composable
fun <T> StatefulComposable(
    state: UiState<T>,
    onShowLoadingDialog: (Boolean) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    content: @Composable (T) -> Unit,
) {
    content(state.data)
    when (state) {
        is UiState.Loading -> {
            onShowLoadingDialog(true)
        }

        is UiState.Success -> {
            onShowLoadingDialog(false)
        }

        is UiState.Error -> {
            onShowLoadingDialog(false)
            LaunchedEffect(onShowSnackbar) {
                onShowSnackbar(state.exception.message.toString(), null)
            }
        }
    }
}

sealed class UiState<out T>(val data: T) {
    data class Loading<T>(val d: T) : UiState<T>(d)
    data class Success<T>(val d: T) : UiState<T>(d)
    data class Error<T>(val exception: Exception, val d: T) : UiState<T>(d)
}
