package org.jraf.minitel.lib.util.escaping

enum class CharacterSize(val characterSizeEscape: String, val screenWidth: Int, val screenHeight: Int) {
    NORMAL(SIZE_NORMAL, SCREEN_WIDTH_NORMAL, SCREEN_HEIGHT_NORMAL),
    TALL(SIZE_TALL, SCREEN_WIDTH_TALL, SCREEN_HEIGHT_TALL),
    WIDE(SIZE_WIDE, SCREEN_WIDTH_WIDE, SCREEN_HEIGHT_WIDE),
    DOUBLE(SIZE_DOUBLE, SCREEN_WIDTH_DOUBLE, SCREEN_HEIGHT_DOUBLE),
}