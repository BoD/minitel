package org.jraf.minitel.lib.util.escaping

enum class CharacterSize(
    val characterSizeEscape: String,
    val maxCharactersHorizontal: Int,
    val maxCharactersVertical: Int,
    val characterWidth: Int,
    val characterHeight: Int
) {
    NORMAL(SIZE_NORMAL, SCREEN_WIDTH_NORMAL, SCREEN_HEIGHT_NORMAL, 1, 1),
    TALL(SIZE_TALL, SCREEN_WIDTH_TALL, SCREEN_HEIGHT_TALL, 1, 2),
    WIDE(SIZE_WIDE, SCREEN_WIDTH_WIDE, SCREEN_HEIGHT_WIDE, 2, 1),
    DOUBLE(SIZE_DOUBLE, SCREEN_WIDTH_DOUBLE, SCREEN_HEIGHT_DOUBLE, 2, 2),
}