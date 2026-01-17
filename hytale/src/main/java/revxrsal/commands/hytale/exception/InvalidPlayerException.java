package revxrsal.commands.hytale.exception;

import org.jetbrains.annotations.NotNull;
import revxrsal.commands.exception.InvalidValueException;

/**
 * Thrown when an invalid value for a {@link com.hypixel.hytale.server.core.entity.entities.Player} parameter is inputted in the command
 */
public class InvalidPlayerException extends InvalidValueException {

    public InvalidPlayerException(@NotNull final String input) {
        super(input);
    }
}
