/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ca.gosyer.jui.domain.global.interactor

import ca.gosyer.jui.domain.global.service.GlobalRepositoryOld
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.singleOrNull
import me.tatarka.inject.annotations.Inject
import org.lighthousegames.logging.logging

class GetGlobalMeta
    @Inject
    constructor(
        private val globalRepositoryOld: GlobalRepositoryOld,
    ) {
        suspend fun await(onError: suspend (Throwable) -> Unit = {}) =
            asFlow()
                .catch {
                    onError(it)
                    log.warn(it) { "Failed to get global meta" }
                }
                .singleOrNull()

        fun asFlow() = globalRepositoryOld.getGlobalMeta()

        companion object {
            private val log = logging()
        }
    }
