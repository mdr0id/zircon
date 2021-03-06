package org.codetome.zircon.api

import org.assertj.core.api.Assertions.assertThat
import org.codetome.zircon.api.builder.TextCharacterBuilder
import org.codetome.zircon.api.builder.graphics.StyleSetBuilder
import org.codetome.zircon.api.color.ANSITextColor
import org.codetome.zircon.api.color.ANSITextColor.*
import org.codetome.zircon.api.color.TextColor
import org.codetome.zircon.api.interop.Modifiers
import org.codetome.zircon.api.modifier.SimpleModifiers.*
import org.junit.Test

@Suppress("UsePropertyAccessSyntax")
class DefaultTextCharacterTest {

    @Test
    fun shouldGenerateProperCacheKey() {
        val result = TextCharacterBuilder.newBuilder()
                .character('x')
                .backgroundColor(ANSITextColor.GREEN)
                .foregroundColor(TextColor.fromString("#aabbcc"))
                .modifiers(Bold)
                .tag("foo", "bar")
                .build()
                .generateCacheKey()

        assertThat(result).isEqualTo("x170187204255GREENBoldbarfoo")
    }

    @Test
    fun defaultCharacterShouldBeEmptyStringWithBlackAndWhiteAndNoModifiers() {
        assertThat(TextCharacter.defaultCharacter().getCharacter()).isEqualTo(' ')
        assertThat(TextCharacter.defaultCharacter().getBackgroundColor()).isEqualTo(BLACK)
        assertThat(TextCharacter.defaultCharacter().getForegroundColor()).isEqualTo(WHITE)
        assertThat(TextCharacter.defaultCharacter().getModifiers()).isEmpty()
    }

    @Test
    fun shouldProperlyReportHavingABorderWhenThereIsBorder() {
        assertThat(TextCharacterBuilder.newBuilder()
                .modifiers(Modifiers.border())
                .build().hasBorder()).isTrue()
    }

    @Test
    fun shouldProperlyReportHavingABorderWhenThereIsNoBorder() {
        assertThat(TextCharacterBuilder.newBuilder()
                .build().hasBorder()).isFalse()
    }

    @Test
    fun shouldNotBeEmptyWhenNotEmpty() {
        assertThat(TextCharacter.defaultCharacter().isNotEmpty()).isTrue()
    }

    @Test
    fun shouldBeEmptyWhenEmpty() {
        assertThat(TextCharacter.empty().isNotEmpty()).isFalse()
    }

    @Test
    fun shouldProperlyRemoveModifiersWhenWithoutModifiersIsCalled() {
        assertThat(TextCharacterBuilder.newBuilder()
                .modifiers(Modifiers.bold())
                .build()
                .withoutModifiers(setOf(Modifiers.bold()))
                .getModifiers())
                .isEmpty()
    }

    @Test
    fun shouldProperlyCreateCopyWithStyleWhenWithStyleIsCalled() {
        val style = StyleSetBuilder.newBuilder()
                .foregroundColor(ANSITextColor.BLUE)
                .backgroundColor(ANSITextColor.CYAN)
                .modifiers(Modifiers.bold())
                .build()

        val copy = TextCharacterBuilder.newBuilder()
                .build()
                .withStyle(style)

        assertThat(copy.getModifiers()).isEqualTo(style.getModifiers())
        assertThat(copy.getBackgroundColor()).isEqualTo(style.getBackgroundColor())
        assertThat(copy.getForegroundColor()).isEqualTo(style.getForegroundColor())
    }

    @Test
    fun boldModifierShouldBeBold() {
        assertThat(TextCharacterBuilder.newBuilder().modifiers(Modifiers.bold()).build().isBold()).isTrue()
    }

    @Test
    fun underlinedModifierShouldBeUnderlined() {
        assertThat(TextCharacterBuilder.newBuilder().modifiers(Modifiers.underline()).build().isUnderlined()).isTrue()
    }

    @Test
    fun crossedOutModifierShouldBeCrossedOut() {
        assertThat(TextCharacterBuilder.newBuilder().modifiers(Modifiers.crossedOut()).build().isCrossedOut()).isTrue()
    }

    @Test
    fun italicModifierShouldBeItalic() {
        assertThat(TextCharacterBuilder.newBuilder().modifiers(Modifiers.italic()).build().isItalic()).isTrue()
    }

    @Test
    fun blinkingModifierShouldBeBlinking() {
        assertThat(TextCharacterBuilder.newBuilder().modifiers(Modifiers.blink()).build().isBlinking()).isTrue()
    }

    @Test
    fun shouldBeSameButWithCharChangedWhenWithCharIsCalled() {
        assertThat(TextCharacter.create(
                character = 'a',
                styleSet = StyleSetBuilder.newBuilder()
                        .foregroundColor(EXPECTED_FG_COLOR)
                        .backgroundColor(EXPECTED_BG_COLOR)
                        .modifiers(EXPECTED_MODIFIERS)
                        .build())
                .withCharacter(EXPECTED_CHAR))
                .isEqualTo(EXPECTED_TEXT_CHARACTER)
    }

    @Test
    fun shouldBeSameButWithFGChangedWhenWithForegroundColorIsCalled() {
        assertThat(TextCharacter.create(
                character = EXPECTED_CHAR,
                styleSet = StyleSetBuilder.newBuilder()
                        .foregroundColor(GREEN)
                        .backgroundColor(EXPECTED_BG_COLOR)
                        .modifiers(EXPECTED_MODIFIERS)
                        .build())
                .withForegroundColor(EXPECTED_FG_COLOR))
                .isEqualTo(EXPECTED_TEXT_CHARACTER)
    }

    @Test
    fun shouldBeSameButWithBGChangedWhenWithBackgroundColorIsCalled() {
        assertThat(TextCharacter.create(
                character = EXPECTED_CHAR,
                styleSet = StyleSetBuilder.newBuilder()
                        .foregroundColor(EXPECTED_FG_COLOR)
                        .backgroundColor(RED)
                        .modifiers(EXPECTED_MODIFIERS)
                        .build())
                .withBackgroundColor(EXPECTED_BG_COLOR))
                .isEqualTo(EXPECTED_TEXT_CHARACTER)
    }

    @Test
    fun shouldBeSameButWithModifiersChangedWhenWithModifiersIsCalled() {
        assertThat(TextCharacter.create(
                character = EXPECTED_CHAR,
                styleSet = StyleSetBuilder.newBuilder()
                        .foregroundColor(EXPECTED_FG_COLOR)
                        .backgroundColor(EXPECTED_BG_COLOR)
                        .modifiers(setOf(Blink))
                        .build())
                .withModifiers(EXPECTED_MODIFIERS)).isEqualTo(EXPECTED_TEXT_CHARACTER)
    }

    @Test
    fun shouldBeSameButWithModifierRemovedWhenWithModifierIsCalled() {
        assertThat(TextCharacter.create(
                character = EXPECTED_CHAR,
                styleSet = StyleSetBuilder.newBuilder()
                        .foregroundColor(EXPECTED_FG_COLOR)
                        .backgroundColor(EXPECTED_BG_COLOR)
                        .modifiers(setOf(Bold))
                        .build())
                .withModifiers(Modifiers.italic())).isEqualTo(
                TextCharacter.create(
                        character = EXPECTED_CHAR,
                        styleSet = StyleSetBuilder.newBuilder()
                                .foregroundColor(EXPECTED_FG_COLOR)
                                .backgroundColor(EXPECTED_BG_COLOR)
                                .modifiers(setOf(Italic))
                                .build()))
    }

    @Test
    fun shouldBeSameButWithModifierRemovedWhenWithoutModifierIsCalled() {
        assertThat(TextCharacter.create(
                character = EXPECTED_CHAR,
                styleSet = StyleSetBuilder.newBuilder()
                        .foregroundColor(EXPECTED_FG_COLOR)
                        .backgroundColor(EXPECTED_BG_COLOR)
                        .modifiers(setOf(Modifiers.bold(), Modifiers.italic(), Modifiers.blink()))
                        .build())
                .withoutModifiers(Modifiers.blink()))
                .isEqualTo(EXPECTED_TEXT_CHARACTER)
    }

    @Test
    fun shouldReturnSameTextCharacterWhenWithCharIsCalledWithSameChar() {
        assertThat(EXPECTED_TEXT_CHARACTER.withCharacter(EXPECTED_CHAR))
                .isSameAs(EXPECTED_TEXT_CHARACTER)
    }

    @Test
    fun shouldReturnSameTextCharacterWhenWithFGColorIsCalledWithSameFGColor() {
        assertThat(EXPECTED_TEXT_CHARACTER.withForegroundColor(EXPECTED_FG_COLOR))
                .isSameAs(EXPECTED_TEXT_CHARACTER)
    }

    @Test
    fun shouldReturnSameTextCharacterWhenWithBGColorIsCalledWithSameBGColor() {
        assertThat(EXPECTED_TEXT_CHARACTER.withBackgroundColor(EXPECTED_BG_COLOR))
                .isSameAs(EXPECTED_TEXT_CHARACTER)
    }

    @Test
    fun shouldReturnSameTextCharacterWhenWithModifierIsCalledWithSameModifier() {
        assertThat(EXPECTED_TEXT_CHARACTER.withModifiers(Modifiers.bold(), Modifiers.italic()))
                .isSameAs(EXPECTED_TEXT_CHARACTER)
    }

    @Test
    fun shouldReturnSameTextCharacterWhenWithModifierSIsCalledWithSameModifierS() {
        assertThat(EXPECTED_TEXT_CHARACTER.withModifiers(EXPECTED_MODIFIERS))
                .isSameAs(EXPECTED_TEXT_CHARACTER)
    }

    @Test
    fun shouldReturnSameTextCharacterWhenWithoutModifierIsCalledWithNonPresentModifier() {
        assertThat(EXPECTED_TEXT_CHARACTER.withoutModifiers(Modifiers.crossedOut()))
                .isSameAs(EXPECTED_TEXT_CHARACTER)
    }

    companion object {
        val EXPECTED_CHAR = 'x'
        val EXPECTED_FG_COLOR = TextColor.fromString("#aabbcc")
        val EXPECTED_BG_COLOR = TextColor.fromString("#223344")
        val EXPECTED_MODIFIERS = setOf(Modifiers.bold(), Modifiers.italic())

        val EXPECTED_TEXT_CHARACTER = TextCharacter.create(
                character = EXPECTED_CHAR,
                styleSet = StyleSetBuilder.newBuilder()
                        .foregroundColor(EXPECTED_FG_COLOR)
                        .backgroundColor(EXPECTED_BG_COLOR)
                        .modifiers(EXPECTED_MODIFIERS)
                        .build())
    }

}
