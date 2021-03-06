package org.codetome.zircon.api.util

import org.codetome.zircon.api.behavior.Cacheable
import org.codetome.zircon.platform.factory.CacheFactory

/**
 * Simple cache interface for storing [Cacheable]s.
 */
interface Cache<R : Cacheable> {

    /**
     * Retrieves a font region by a `key` if present.
     */
    fun retrieveIfPresent(key: String): Maybe<R>

    /**
     * Caches the given [Cacheable] and then returns it.
     */
    fun store(cacheable: R): R

    companion object {

        /**
         * Creates a new [Cache] for the given [Cacheable] type.
         */
        fun <R : Cacheable> create(): Cache<R> = CacheFactory.create()
    }
}
