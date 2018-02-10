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