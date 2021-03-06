package org.codetome.zircon.api.builder.graphics

import org.codetome.zircon.api.Position
import org.codetome.zircon.api.Size
import org.codetome.zircon.api.TextCharacter
import org.codetome.zircon.api.builder.Builder
import org.codetome.zircon.api.font.Font
import org.codetome.zircon.api.graphics.Layer
import org.codetome.zircon.api.graphics.TextImage
import org.codetome.zircon.api.util.Maybe
import org.codetome.zircon.internal.graphics.DefaultLayer

/**
 * Use this to build [Layer]s. Defaults are:
 * - size: [Size.one()]
 * - filler: [TextCharacter.empty()]
 * - offset: [Position.defaultPosition()]
 * - has no text image by default
 */
data class LayerBuilder(private var font: Font = Layer.defaultFont(),
                        private var size: Size = Layer.defaultSize(),
                        private var filler: TextCharacter = Layer.defaultFiller(),
                        private var offset: Position = Position.defaultPosition(),
                        private var textImage: Maybe<TextImage> = Maybe.empty()) : Builder<Layer> {

    /**
     * Sets the [Font] to use with the resulting [Layer].
     */
    fun font(font: Font) = also {
        this.font = font
    }

    /**
     * Sets the size for the new [org.codetome.zircon.api.graphics.Layer].
     * Default is 1x1.
     */
    fun size(size: Size) = also {
        this.size = size
    }

    /**
     * The new [org.codetome.zircon.api.graphics.Layer] will be filled by this [TextCharacter].
     * Defaults to `EMPTY`.
     */
    fun filler(filler: TextCharacter) = also {
        this.filler = filler
    }

    /**
     * Sets the `offset` for the new [org.codetome.zircon.api.graphics.Layer].
     * Default is 0x0.
     */
    fun offset(offset: Position) = also {
        this.offset = offset
    }

    /**
     * Uses the given [TextImage] and converts it to a [Layer].
     */
    fun textImage(textImage: TextImage) = also {
        this.textImage = Maybe.of(textImage)
    }

    override fun build(): Layer = if (textImage.isPresent) {
        DefaultLayer(size = textImage.get().getBoundableSize(),
                filler = filler,
                offset = offset,
                textImage = textImage.get(),
                initialFont = font)
    } else {
        DefaultLayer(
                size = size,
                filler = filler,
                offset = offset,
                initialFont = font)
    }

    override fun createCopy() = copy()

    companion object {

        fun newBuilder() = LayerBuilder()
    }
}
