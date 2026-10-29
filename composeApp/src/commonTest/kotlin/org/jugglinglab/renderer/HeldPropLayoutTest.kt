package org.jugglinglab.renderer

import androidx.compose.ui.geometry.Offset
import org.jugglinglab.notation.SequencePreviewBuilder
import kotlin.math.hypot
import kotlin.test.*

class HeldPropLayoutTest {
    @Test fun heldInventoryIsGroupedByTheActualHand() {
        val preview = SequencePreviewBuilder.build("55555", finite = true)
        val hands = HeldPropLayout.hands(preview.pattern, preview.window.start - 0.01)
        assertEquals(listOf(2, 3), hands.map { it.paths.size }.sorted())
        assertEquals(5, hands.flatMap { it.paths }.distinct().size)
    }
    @Test fun airborneBallsAreNotAddedToTheHeldFan() {
        val preview = SequencePreviewBuilder.build("55555", finite = true)
        val time = preview.window.start + 0.1
        val held = HeldPropLayout.hands(preview.pattern, time).flatMap { it.paths }
        assertEquals(4, held.size)
        assertEquals(Offset.Zero, HeldPropLayout.offset(0, 1, 20f, 1f))
    }
    @Test fun everyBallInAHandRemainsIndividuallyVisible() {
        for (count in 2..16) for (direction in listOf(-1f, 1f)) {
            val offsets = (0 until count).map { HeldPropLayout.offset(it, count, 20f, direction) }
            assertEquals(Offset.Zero, offsets.first())
            for (i in offsets.indices) for (j in 0 until i) {
                assertTrue(hypot((offsets[i].x - offsets[j].x).toDouble(),
                    (offsets[i].y - offsets[j].y).toDouble()) >= 20.0,
                    "Held balls $i and $j overlap for inventory $count")
            }
        }
    }
}
