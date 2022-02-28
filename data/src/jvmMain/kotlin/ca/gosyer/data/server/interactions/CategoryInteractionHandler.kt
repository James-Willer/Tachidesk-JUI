/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ca.gosyer.data.server.interactions

import ca.gosyer.core.lang.withIOContext
import ca.gosyer.data.models.Category
import ca.gosyer.data.models.Manga
import ca.gosyer.data.server.Http
import ca.gosyer.data.server.ServerPreferences
import ca.gosyer.data.server.requests.addMangaToCategoryQuery
import ca.gosyer.data.server.requests.categoryDeleteRequest
import ca.gosyer.data.server.requests.categoryModifyRequest
import ca.gosyer.data.server.requests.categoryReorderRequest
import ca.gosyer.data.server.requests.createCategoryRequest
import ca.gosyer.data.server.requests.getCategoriesQuery
import ca.gosyer.data.server.requests.getMangaCategoriesQuery
import ca.gosyer.data.server.requests.getMangaInCategoryQuery
import ca.gosyer.data.server.requests.removeMangaFromCategoryRequest
import io.ktor.client.request.delete
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod
import io.ktor.http.Parameters
import me.tatarka.inject.annotations.Inject

class CategoryInteractionHandler @Inject constructor(
    client: Http,
    serverPreferences: ServerPreferences
) : BaseInteractionHandler(client, serverPreferences) {

    suspend fun getMangaCategories(mangaId: Long) = withIOContext {
        client.get<List<Category>>(
            serverUrl + getMangaCategoriesQuery(mangaId)
        )
    }

    suspend fun getMangaCategories(manga: Manga) = getMangaCategories(manga.id)

    suspend fun addMangaToCategory(mangaId: Long, categoryId: Long) = withIOContext {
        client.get<HttpResponse>(
            serverUrl + addMangaToCategoryQuery(mangaId, categoryId)
        )
    }
    suspend fun addMangaToCategory(manga: Manga, category: Category) = addMangaToCategory(manga.id, category.id)
    suspend fun addMangaToCategory(manga: Manga, categoryId: Long) = addMangaToCategory(manga.id, categoryId)
    suspend fun addMangaToCategory(mangaId: Long, category: Category) = addMangaToCategory(mangaId, category.id)

    suspend fun removeMangaFromCategory(mangaId: Long, categoryId: Long) = withIOContext {
        client.delete<HttpResponse>(
            serverUrl + removeMangaFromCategoryRequest(mangaId, categoryId)
        )
    }
    suspend fun removeMangaFromCategory(manga: Manga, category: Category) = removeMangaFromCategory(manga.id, category.id)
    suspend fun removeMangaFromCategory(manga: Manga, categoryId: Long) = removeMangaFromCategory(manga.id, categoryId)
    suspend fun removeMangaFromCategory(mangaId: Long, category: Category) = removeMangaFromCategory(mangaId, category.id)

    suspend fun getCategories(dropDefault: Boolean = false) = withIOContext {
        client.get<List<Category>>(
            serverUrl + getCategoriesQuery()
        ).let { categories ->
            if (dropDefault) {
                categories.filterNot { it.name.equals("default", true) }
            } else categories
        }
    }

    suspend fun createCategory(name: String) = withIOContext {
        client.submitForm<HttpResponse>(
            serverUrl + createCategoryRequest(),
            formParameters = Parameters.build {
                append("name", name)
            }
        )
    }

    suspend fun modifyCategory(categoryId: Long, name: String? = null, isLanding: Boolean? = null) = withIOContext {
        client.submitForm<HttpResponse>(
            serverUrl + categoryModifyRequest(categoryId),
            formParameters = Parameters.build {
                if (name != null) {
                    append("name", name)
                }
                if (isLanding != null) {
                    append("isLanding", isLanding.toString())
                }
            }
        ) {
            method = HttpMethod.Patch
        }
    }
    suspend fun modifyCategory(category: Category, name: String? = null, isLanding: Boolean? = null) = modifyCategory(category.id, name, isLanding)

    suspend fun reorderCategory(to: Int, from: Int) = withIOContext {
        client.submitForm<HttpResponse>(
            serverUrl + categoryReorderRequest(),
            formParameters = Parameters.build {
                append("to", to.toString())
                append("from", from.toString())
            }
        ) {
            method = HttpMethod.Patch
        }
    }

    suspend fun deleteCategory(categoryId: Long) = withIOContext {
        client.delete<HttpResponse>(
            serverUrl + categoryDeleteRequest(categoryId)
        )
    }
    suspend fun deleteCategory(category: Category) = deleteCategory(category.id)

    suspend fun getMangaFromCategory(categoryId: Long) = withIOContext {
        client.get<List<Manga>>(
            serverUrl + getMangaInCategoryQuery(categoryId)
        )
    }
    suspend fun getMangaFromCategory(category: Category) = getMangaFromCategory(category.id)
}