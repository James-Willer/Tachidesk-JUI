/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ca.gosyer.jui.ui.base

import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.compositionLocalOf
import ca.gosyer.jui.core.di.AppScope
import ca.gosyer.jui.ui.ViewModelComponent
import ca.gosyer.jui.ui.base.image.ImageLoaderProvider
import ca.gosyer.jui.uicore.vm.ContextWrapper
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.LocalImageLoader
import me.tatarka.inject.annotations.Provides

interface UiComponent {
    val imageLoader: ImageLoader

    val contextWrapper: ContextWrapper

    val hooks: Array<ProvidedValue<out Any>>

    @AppScope
    @Provides
    fun imageLoaderFactory(imageLoaderProvider: ImageLoaderProvider): ImageLoader = imageLoaderProvider.get()

    @Provides
    fun getHooks(viewModelComponent: ViewModelComponent) = arrayOf(
        LocalViewModels provides viewModelComponent,
        LocalImageLoader provides imageLoader
    )
}

val LocalViewModels =
    compositionLocalOf<ViewModelComponent> { throw IllegalArgumentException("ViewModelComponent not found") }
