package org.codetome.zircon.examples;

import org.codetome.zircon.TerminalUtils;
import org.codetome.zircon.api.Size;
import org.codetome.zircon.api.graphics.TextCharacterString;
import org.codetome.zircon.api.interop.*;
import org.codetome.zircon.api.terminal.Terminal;

import static org.codetome.zircon.api.color.ANSITextColor.BLACK;
import static org.codetome.zircon.api.resource.CP437TilesetResource.TAFFER_20X20;

public class TextCharacterStringExample {

    private static final int TERMINAL_WIDTH = 42;
    private static final int TERMINAL_HEIGHT = 16;
    private static final Size SIZE = Sizes.create(TERMINAL_WIDTH, TERMINAL_HEIGHT);

    public static void main(String[] args) {
        final Terminal terminal = TerminalUtils.fetchTerminalBuilder(args)
                .font(TAFFER_20X20.toFont())
                .initialTerminalSize(SIZE)
                .build();
        terminal.setCursorVisibility(false);

        TextCharacterString tcs = TextCharacterStrings.newBuilder()
                .foregroundColor(TextColors.fromString("#eeffee"))
                .backgroundColor(TextColors.fromString("#223344"))
                .modifiers(Modifiers.underline())
                .text("This is some text which is too long to fit on one line...")
                .build();

        terminal.draw(tcs, Positions.defaultPosition());

        terminal.flush();
    }

    private static void putEmptySpace(Terminal terminal) {
        terminal.resetColorsAndModifiers();
        terminal.setForegroundColor(BLACK);
        terminal.putCharacter(' ');
    }

}
