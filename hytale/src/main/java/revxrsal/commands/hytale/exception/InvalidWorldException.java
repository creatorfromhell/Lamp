package revxrsal.commands.hytale.exception;

import org.jetbrains.annotations.NotNull;
import revxrsal.commands.exception.InvalidValueException;

/**
 * Thrown when an invalid value for a {@link com.hypixel.hytale.server.core.universe.world.World} parameter
 * is inputted in the command
 */
public class InvalidWorldException extends InvalidValueException {

    public InvalidWorldException(@NotNull final String input) {
        super(input);
    }
}
