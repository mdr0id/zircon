package org.codetome.zircon.internal.extensions

import org.codetome.zircon.api.util.Maybe

/**
 * Returns an element of this [List] wrapped in an Maybe
 * which is empty if `idx` is out of bounds.
 */
fun <T> List<T>.getIfPresent(idx: Int) =
        if (idx >= size) {
            Maybe.empty()
        } else {
            Maybe.of(get(idx))
        }
