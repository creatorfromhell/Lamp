package revxrsal.commands.hytale.util;

import com.hypixel.hytale.server.core.Message;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextDecoration.State;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for converting Adventure {@link Component} objects to Hytale's {@link Message} format.
 * This class provides methods to transform textual and styled components into a format
 * that Hytale applications can understand.
 *
 * The class primarily focuses on converting {@link TextComponent} instances while handling
 * attributes like color, decorations, click events, and children components. If strict text-only
 * processing is enabled, non-text components will trigger an exception.
 *
 * This class is final and cannot be extended.
 * @author Daniel "creatorfromhell" Vidmar
 */
public final class AdventureToHytaleMessageConverter {

    public static Message convert(final Component component, final boolean strictTextOnly) {

        if(component == null) return Message.empty();

        if(!(component instanceof TextComponent)) {

            if (strictTextOnly) throw new IllegalArgumentException("Non-TextComponent encountered: " + component.getClass().getName());

            return Message.empty();
        }

        return convertText((TextComponent) component, strictTextOnly);
    }

    private static Message convertText(final TextComponent textComponent, final boolean strictTextOnly) {

        final Message msg = Message.raw(textComponent.content());

        final TextColor color = textComponent.color();
        if (color != null) {
            msg.color(color.asHexString());
        }

        // Decorations (Adventure uses tri-state)
        applyDecoration(textComponent, TextDecoration.BOLD, msg::bold);
        applyDecoration(textComponent, TextDecoration.ITALIC, msg::italic);

        final Key font = textComponent.font();
        if (font != null && "minecraft".equals(font.namespace()) && "uniform".equals(font.value())) {
            msg.monospace(true);
        }

        // Click event -> link (only OPEN_URL maps cleanly)
        final ClickEvent click = textComponent.clickEvent();
        if (click != null && click.action() == ClickEvent.Action.OPEN_URL) {
            msg.link(click.value());
        }

        //Our children nodes.
        final List<Component> children = textComponent.children();
        if (!children.isEmpty()) {

            final List<Message> convertedChildren = new ArrayList<>(children.size());
            for (final Component child : children) {
                if (child instanceof final TextComponent childTextComponent) {
                    convertedChildren.add(convertText(childTextComponent, strictTextOnly));

                } else if (strictTextOnly) {

                    throw new IllegalArgumentException("Non-TextComponent child encountered: " + child.getClass().getName());
                } else {
                    //In the end, just try to legacy serialize it.
                    convertedChildren.add(Message.raw(LegacyComponentSerializer.builder().build().serialize(child)));
                }
            }
            msg.insertAll(convertedChildren);
        }

        return msg;
    }

    private static void applyDecoration(final TextComponent textComponent, final TextDecoration decoration, final BooleanConsumer setter) {
        final State state = textComponent.decoration(decoration);
        if (state == State.TRUE) {
            setter.accept(true);
        } else if (state == State.FALSE) {
            setter.accept(false);
        }
    }

    @FunctionalInterface
    private interface BooleanConsumer {
        void accept(boolean value);
    }
}