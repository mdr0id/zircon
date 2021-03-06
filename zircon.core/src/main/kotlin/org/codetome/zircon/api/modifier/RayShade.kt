package org.codetome.zircon.api.modifier

import org.codetome.zircon.api.Modifier

data class RayShade(val opacity: Float = 1.0f,
                    val threshold: Float = 0.0f,
                    val strength: Float = 0.5f,
                    val raysOnly: Boolean = false) : Modifier {

    private val cacheKey = "RayShade-$opacity-$threshold-$strength-$raysOnly"

    override fun generateCacheKey() = cacheKey
}
