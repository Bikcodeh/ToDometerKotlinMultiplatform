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

package com.sergiobelda.backend.model

import com.sergiobelda.backend.database.entity.NewTagEntity
import com.sergiobelda.backend.database.entity.TagEntity
import java.util.UUID

fun TagEntity.toTag() = Tag(
    id = id.toString(),
    color = color,
    name = name
)

fun Iterable<TagEntity>.toTagList() = this.map { tagEntity ->
    tagEntity.toTag()
}

fun NewTag.toNewTagEntity() = NewTagEntity(
    id = id?.let { UUID.fromString(it) },
    color = color,
    name = name
)
