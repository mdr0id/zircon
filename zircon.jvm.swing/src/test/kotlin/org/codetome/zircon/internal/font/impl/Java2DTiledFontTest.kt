package org.codetome.zircon.internal.font.impl

import org.assertj.core.api.Assertions.assertThat
import org.codetome.zircon.api.TextCharacter
import org.codetome.zircon.api.resource.CP437TilesetResource
import org.junit.Ignore
import org.junit.Test

@Ignore
class Java2DTiledFontTest {

    val target = CP437TilesetResource.WANDERLUST_16X16.toFont()

    @Test
    fun shouldProperlyCacheFontWhenFetchingRegionTwice() {
        val firstResult = target.fetchRegionForChar(TextCharacter.defaultCharacter())
        val secondResult = target.fetchRegionForChar(TextCharacter.defaultCharacter())

        assertThat(firstResult).isSameAs(secondResult)
    }

    @Test
    fun shouldNotBeTheSameWhenRegionIsLoadedForDifferentTextChars() {
        val firstResult = target.fetchRegionForChar(TextCharacter.defaultCharacter())
        val secondResult = target.fetchRegionForChar(TextCharacter.empty())

        assertThat(firstResult).isNotSameAs(secondResult)
    }

    @Test
    fun shouldBeAbleToDisplayASimpleCharacter() {
        assertThat(target.hasDataForChar('a')).isTrue()
    }

    @Test
    fun shouldNotBeAbleToDisplayControlCharacter() {
        assertThat(target.hasDataForChar(1.toChar())).isFalse()
    }

    @Test(expected = IllegalArgumentException::class)
    fun shouldNotBeAbleToFetchRegionWithTags() {
        target.fetchRegionForChar(TextCharacter.defaultCharacter().withTags("foo"))
    }

    @Test(expected = IllegalArgumentException::class)
    fun shouldNotBeAbleToFetchRegionWithChar() {
        target.fetchRegionForChar(TextCharacter.defaultCharacter().withCharacter(1.toChar()))
    }

    @Test
    fun shouldProperlyReportSize() {
        val expectedSize = 16
        assertThat(target.getWidth()).isEqualTo(expectedSize)
        assertThat(target.getHeight()).isEqualTo(expectedSize)
    }
}
