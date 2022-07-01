/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ca.gosyer.jui.domain.library.interactor

import ca.gosyer.jui.domain.library.service.LibraryRepository
import ca.gosyer.jui.domain.manga.model.Manga
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.singleOrNull
import me.tatarka.inject.annotations.Inject
import org.lighthousegames.logging.logging

class RemoveMangaFromLibrary @Inject constructor(private val libraryRepository: LibraryRepository) {

    suspend fun await(mangaId: Long) = asFlow(mangaId)
        .catch { log.warn(it) { "Failed to remove $mangaId from library" } }
        .singleOrNull()

    suspend fun await(manga: Manga) = asFlow(manga)
        .catch { log.warn(it) { "Failed to remove ${manga.title}(${manga.id}) from library" } }
        .singleOrNull()

    fun asFlow(mangaId: Long) = libraryRepository.removeMangaFromLibrary(mangaId)

    fun asFlow(manga: Manga) = libraryRepository.removeMangaFromLibrary(manga.id)

    companion object {
        private val log = logging()
    }
}