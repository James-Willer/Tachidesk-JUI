package ca.gosyer.ui.reader.navigation

import ca.gosyer.ui.reader.model.Navigation

/**
 * Visualization of default state without any inversion
 * +---+---+---+
 * | P | P | P |   P: Previous
 * +---+---+---+
 * | P | M | N |   M: Menu
 * +---+---+---+
 * | N | N | N |   N: Next
 * +---+---+---+
 */
open class LNavigation : ViewerNavigation() {

    override var regions: List<Region> = listOf(
        Region(
            rect = Rect(0, 33, 33, 66),
            type = Navigation.PREV
        ),
        Region(
            rect = Rect(0, 0, 100, 33),
            type = Navigation.PREV
        ),
        Region(
            rect = Rect(66, 33, 100, 66),
            type = Navigation.NEXT
        ),
        Region(
            rect = Rect(0, 66, 100, 100),
            type = Navigation.NEXT
        )
    )
}