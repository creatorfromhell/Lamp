package revxrsal.commands.hytale.sender;

import org.jetbrains.annotations.NotNull;
import revxrsal.commands.command.CommandPermission;
import revxrsal.commands.hytale.actor.HytaleCommandActor;

import java.util.Objects;

/**
 * A Bukkit-adapted wrapper for {@link CommandPermission}
 */
public final class HytaleCommandPermission implements CommandPermission<HytaleCommandActor> {
    private final @NotNull String permission;

    /**
     *
     */
    public HytaleCommandPermission(@NotNull final String permission) {this.permission = permission;}

    @Override public boolean isExecutableBy(@NotNull final HytaleCommandActor actor) {
        return actor.sender().hasPermission(permission);
    }

    public @NotNull String permission() {return permission;}

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        final HytaleCommandPermission that = (HytaleCommandPermission) obj;
        return Objects.equals(this.permission, that.permission);
    }

    @Override
    public int hashCode() {
        return Objects.hash(permission);
    }

    @Override
    public String toString() {
        return "BukkitCommandPermission[permission=" + permission + "]";
    }

}
