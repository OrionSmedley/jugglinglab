// Display-only separation of props held by the same hand.
// Copyright 2026 Juggling Lab contributors. Licensed under the project's GPL.
package org.jugglinglab.renderer

import androidx.compose.ui.geometry.Offset
import org.jugglinglab.jml.JmlEvent
import org.jugglinglab.jml.JmlPattern
import kotlin.math.min

internal object HeldPropLayout {
    data class Hand(val juggler: Int, val hand: Int, val paths: List<Int>)

    fun hands(pattern: JmlPattern, time: Double): List<Hand> = buildList {
        for (j in 1..pattern.numberOfJugglers) {
            for (h in listOf(JmlEvent.RIGHT_HAND, JmlEvent.LEFT_HAND)) {
                val paths = (1..pattern.numberOfPaths).filter { pattern.layout.isHandHoldingPath(j, h, time, it) }
                if (paths.size > 1) add(Hand(j, h, paths))
            }
        }
    }

    // The first prop remains at the physical hand. Others sit in a compact fan.
    // Screen-space offsets remain readable when the camera is rotated or stereo.
    fun offset(index: Int, count: Int, diameter: Float, direction: Float): Offset {
        if (count < 2 || index == 0) return Offset.Zero
        val columns = min(4, count)
        val col = index % columns
        val row = index / columns
        val spacing = diameter * 1.08f
        return Offset(direction * col * spacing, -(row * spacing + col * diameter * 0.3f))
    }
}
