package revxrsal.commands.hytale.hooks;

import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import revxrsal.commands.Lamp;
import revxrsal.commands.command.CommandPermission;
import revxrsal.commands.hytale.actor.ActorFactory;
import revxrsal.commands.hytale.actor.HytaleCommandActor;

/**
 * HytaleCommand
 *
 * @author creatorfromhell
 * @since 0.0.1.0
 */
public final class HytaleCommand<A extends HytaleCommandActor> extends CommandBase {

    private final @NotNull Lamp<A> lamp;
    private final @NotNull ActorFactory<A> actorFactory;
    private final @Nullable CommandPermission<A> permission;

    public HytaleCommand(@NotNull final String name, @NotNull final String description, @NotNull final Lamp<A> lamp, @NotNull final ActorFactory<A> actorFactory, final CommandPermission<A> permission) {
        super(name, description);
        this.lamp = lamp;
        this.actorFactory = actorFactory;
        this.permission = permission;
    }

    @Override protected void executeSync(@NotNull final CommandContext commandContext) {
        final A actor = actorFactory.create(commandContext.sender(), lamp);

        lamp.dispatch(actor, commandContext.getInputString());
    }

    @Override public boolean hasPermission(@NotNull final CommandSender sender) {
        return permission == null || permission.isExecutableBy(actorFactory.create(sender, lamp));
    }
}