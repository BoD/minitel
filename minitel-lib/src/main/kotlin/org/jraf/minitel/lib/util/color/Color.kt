/*
 * This source is part of the
 *      _____  ___   ____
 *  __ / / _ \/ _ | / __/___  _______ _
 * / // / , _/ __ |/ _/_/ _ \/ __/ _ `/
 * \___/_/|_/_/ |_/_/ (_)___/_/  \_, /
 *                              /___/
 * repository.
 *
 * Copyright (C) 2018 Benoit 'BoD' Lubek (BoD@JRAF.org)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.jraf.minitel.lib.util.color

typealias AwtColor = java.awt.Color

fun rgbToHsl(color: AwtColor): Triple<Float, Float, Float> {
    val components = color.getRGBColorComponents(null)
    return rgbToHsl(components[0], components[1], components[2])
}

fun rgbToHsl(r: Float, g: Float, b: Float): Triple<Float, Float, Float> {
    val max = Math.max(Math.max(r, g), b)
    val min = Math.min(Math.min(r, g), b)
    val c = (max - min)

    var h_ = 0f
    if (c == 0f) {
        h_ = 0f
    } else if (max == r) {
        h_ = (g - b) / c
        if (h_ < 0) h_ += 6f
    } else if (max == g) {
        h_ = (b - r) / c + 2f
    } else if (max == b) {
        h_ = (r - g) / c + 4f
    }
    val h = 60f * h_

    val l = (max + min) * 0.5f

    val s: Float
    if (c == 0f) {
        s = 0f
    } else {
        s = c / (1 - Math.abs(2f * l - 1f))
    }

    return Triple(h, s, l)
}