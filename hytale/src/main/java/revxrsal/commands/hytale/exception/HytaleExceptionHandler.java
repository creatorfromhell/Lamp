package revxrsal.commands.hytale.exception;

import org.jetbrains.annotations.NotNull;
import revxrsal.commands.exception.*;
import revxrsal.commands.hytale.actor.HytaleCommandActor;
import revxrsal.commands.node.ParameterNode;

import static revxrsal.commands.hytale.util.HytaleUtils.legacyColorize;

public class HytaleExceptionHandler extends DefaultExceptionHandler<HytaleCommandActor> {

    @HandleException
    public void onInvalidPlayer(final InvalidPlayerException e, final HytaleCommandActor actor) {
        actor.error(legacyColorize("&cInvalid player: &e" + e.input() + "&c."));
    }

    @HandleException
    public void onInvalidWorld(final InvalidWorldException e, final HytaleCommandActor actor) {
        actor.error(legacyColorize("&cInvalid world: &e" + e.input() + "&c."));
    }

    @HandleException
    public void onInvalidWorld(final MissingLocationParameterException e, final HytaleCommandActor actor) {
        actor.error(legacyColorize("&cExpected &e" + e.axis().name().toLowerCase() + "&c."));
    }

    @HandleException
    public void onSenderNotConsole(final SenderNotConsoleException e, final HytaleCommandActor actor) {
        actor.error(legacyColorize("&cYou must be the console to execute this command!"));
    }

    @HandleException
    public void onSenderNotPlayer(final SenderNotPlayerException e, final HytaleCommandActor actor) {
        actor.error(legacyColorize("&cYou must be a player to execute this command!"));
    }

    @HandleException
    public void onMoreThanOneEntity(final MoreThanOneEntityException e, final HytaleCommandActor actor) {
        actor.error(legacyColorize("&cOnly one entity is allowed, but the provided selector allows more than one"));
    }

    @Override public void onEnumNotFound(@NotNull final EnumNotFoundException e, @NotNull final HytaleCommandActor actor) {
        actor.error(legacyColorize("&cInvalid choice: &e" + e.input() + "&c. Please enter a valid option from the available values."));
    }

    @Override public void onExpectedLiteral(@NotNull final ExpectedLiteralException e, @NotNull final HytaleCommandActor actor) {
        actor.error(legacyColorize("&cExpected &e" + e.node().name() + "&c, found &e" + e.input() + "&c."));
    }

    @Override public void onInputParse(@NotNull final InputParseException e, @NotNull final HytaleCommandActor actor) {
        switch (e.cause()) {
            case INVALID_ESCAPE_CHARACTER:
                actor.error(legacyColorize("&cInvalid input. Use &e\\\\ &cto include a backslash."));
                break;
            case UNCLOSED_QUOTE:
                actor.error(legacyColorize("&cUnclosed quote. Make sure to close all quotes."));
                break;
            case EXPECTED_WHITESPACE:
                actor.error(legacyColorize("&cExpected whitespace to end one argument, but found trailing data."));
                break;
        }
    }

    @Override
    public void onInvalidListSize(@NotNull final InvalidListSizeException e, @NotNull final HytaleCommandActor actor, @NotNull final ParameterNode<HytaleCommandActor, ?> parameter) {
        if (e.inputSize() < e.minimum())
            actor.error(legacyColorize("&cYou must input at least &e" + fmt(e.minimum()) + " &centries for &e" + parameter.name() + "&c."));
        if (e.inputSize() > e.maximum())
            actor.error(legacyColorize("&cYou must input at most &e" + fmt(e.maximum()) + " &centries for &e" + parameter.name() + "&c."));
    }

    @Override
    public void onInvalidStringSize(@NotNull final InvalidStringSizeException e, @NotNull final HytaleCommandActor actor, @NotNull final ParameterNode<HytaleCommandActor, ?> parameter) {
        if (e.input().length() < e.minimum())
            actor.error(legacyColorize("&cParameter &e" + parameter.name() + " &cmust be at least &e" + fmt(e.minimum()) + " &ccharacters long."));
        if (e.input().length() > e.maximum())
            actor.error(legacyColorize("&cParameter &e" + parameter.name() + " &ccan be at most &e" + fmt(e.maximum()) + " &ccharacters long."));
    }

    @Override public void onInvalidBoolean(@NotNull final InvalidBooleanException e, @NotNull final HytaleCommandActor actor) {
        actor.error(legacyColorize("&cExpected &etrue &cor &efalse&c, found &e" + e.input() + "&c."));
    }

    @Override public void onInvalidDecimal(@NotNull final InvalidDecimalException e, @NotNull final HytaleCommandActor actor) {
        actor.error(legacyColorize("&cInvalid number: &e" + e.input() + "&c."));
    }

    @Override public void onInvalidInteger(@NotNull final InvalidIntegerException e, @NotNull final HytaleCommandActor actor) {
        actor.error(legacyColorize("&cInvalid integer: &e" + e.input() + "&c."));
    }

    @Override public void onInvalidUUID(@NotNull final InvalidUUIDException e, @NotNull final HytaleCommandActor actor) {
        actor.error(legacyColorize("&cInvalid UUID: " + e.input() + "&c."));
    }

    @Override
    public void onMissingArgument(@NotNull final MissingArgumentException e, @NotNull final HytaleCommandActor actor, @NotNull final ParameterNode<HytaleCommandActor, ?> parameter) {
        actor.error(legacyColorize("&cRequired parameter is missing: &e" + parameter.name() + "&c. Usage: &e/" + parameter.command().usage() + "&c."));
    }

    @Override public void onNoPermission(@NotNull final NoPermissionException e, @NotNull final HytaleCommandActor actor) {
        actor.error(legacyColorize("&cYou do not have permission to execute this command!"));
    }

    @Override
    public void onNumberNotInRange(@NotNull final NumberNotInRangeException e, @NotNull final HytaleCommandActor actor, @NotNull final ParameterNode<HytaleCommandActor, Number> parameter) {
        if (e.input().doubleValue() < e.minimum())
            actor.error(legacyColorize("&c" + parameter.name() + " too small &e(" + fmt(e.input()) + ")&c. Must be at least &e" + fmt(e.minimum()) + "&c."));
        if (e.input().doubleValue() > e.maximum())
            actor.error(legacyColorize("&c" + parameter.name() + " too large &e(" + fmt(e.input()) + ")&c. Must be at most &e" + fmt(e.maximum()) + "&c."));
    }

    @Override public void onInvalidHelpPage(@NotNull final InvalidHelpPageException e, @NotNull final HytaleCommandActor actor) {
        if (e.numberOfPages() == 1)
            actor.error(legacyColorize("Invalid help page: &e" + e.page() + "&c. Must be 1."));
        else
            actor.error(legacyColorize("Invalid help page: &e" + e.page() + "&c. Must be between &e1 &cand &e" + e.numberOfPages()));
    }

    @Override public void onUnknownCommand(@NotNull final UnknownCommandException e, @NotNull final HytaleCommandActor actor) {
        actor.error(legacyColorize("&cUnknown command: &e" + e.input() + "&c."));
    }

    @Override public void onValueNotAllowed(@NotNull final ValueNotAllowedException e, @NotNull final HytaleCommandActor actor) {
        final String allowedValues = String.join("&c, &e", e.allowedValues());
        actor.error(legacyColorize("Received an invalid value: &e" + e.input() + "&c. Allowed values: &e" + allowedValues + "&c."));
    }
}
