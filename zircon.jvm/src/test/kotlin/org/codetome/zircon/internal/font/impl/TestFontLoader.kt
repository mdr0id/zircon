package org.codetome.zircon.internal.font.impl

import org.codetome.zircon.api.TextCharacter
import org.codetome.zircon.api.font.TextureRegionMetadata
import org.codetome.zircon.api.font.Font
import org.codetome.zircon.api.font.FontTextureRegion
import org.codetome.zircon.api.font.FontLoader
import org.codetome.zircon.internal.font.MetadataPickingStrategy
import org.codetome.zircon.api.util.Identifier
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_ARGB

class TestFontLoader : FontLoader {

    override fun fetchPhysicalFont(size: Float,
                                   path: String,
                                   cacheFonts: Boolean,
                                   withAntiAlias: Boolean)  = object: Font {

        val uuid = Identifier.randomIdentifier()

        override fun getWidth() = size.toInt()

        override fun getHeight() = size.toInt()

        override fun hasDataForChar(char: Char) = false

        override fun fetchRegionForChar(textCharacter: TextCharacter) = object : FontTextureRegion<BufferedImage> {

            override fun generateCacheKey() = textCharacter.generateCacheKey()

            override fun getBackend() = BufferedImage(getWidth(), getHeight(), TYPE_INT_ARGB)

        }

        override fun fetchMetadataForChar(char: Char) = listOf<TextureRegionMetadata>()

        override fun getId() = uuid

    }

    override fun fetchTiledFont(width: Int,
                                height: Int,
                                path: String,
                                cacheFonts: Boolean,
                                metadata: Map<Char, List<TextureRegionMetadata>>,
                                metadataPickingStrategy: MetadataPickingStrategy)  = object: Font {

        val uuid = Identifier.randomIdentifier()

        override fun getWidth() = width

        override fun getHeight() = height

        override fun hasDataForChar(char: Char) = false

        override fun fetchRegionForChar(textCharacter: TextCharacter) = object : FontTextureRegion<BufferedImage> {

            override fun generateCacheKey() = textCharacter.generateCacheKey()

            override fun getBackend() = BufferedImage(getWidth() * 16, getHeight() * 16, TYPE_INT_ARGB)

        }

        override fun fetchMetadataForChar(char: Char) = metadata[char]!!

        override fun getId() = uuid

    }
}
