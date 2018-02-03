package org.jraf.minitel.lib.util.color

typealias AwtColor = java.awt.Color

fun rgbToHsl(color: AwtColor): Triple<Float, Float, Float> {
    val components = color.getRGBColorComponents(null)
    return rgbToHsl(components[0], components[1], components[2])
}

fun rgbToHsl(r: Float, g: Float, b: Float): Triple<Float, Float, Float> {
    val max = Math.max(Math.max(r, g), b)
    val min = Math.min(Math.min(r, g), b)
    val c = (max - min).toFloat()

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