package revxrsal.commands.hytale.sender;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import revxrsal.commands.Lamp;
import revxrsal.commands.annotation.list.AnnotationList;
import revxrsal.commands.hytale.actor.HytaleCommandActor;
import revxrsal.commands.hytale.annotation.CommandPermission;

public enum HytalePermissionFactory implements revxrsal.commands.command.CommandPermission.Factory<HytaleCommandActor> {
    INSTANCE;

    @Override
    public @Nullable revxrsal.commands.command.CommandPermission<HytaleCommandActor> create(@NotNull final AnnotationList annotations, @NotNull final Lamp<HytaleCommandActor> lamp) {
        final CommandPermission permissionAnn = annotations.get(revxrsal.commands.hytale.annotation.CommandPermission.class);
        if (permissionAnn == null)
            return null;
        return new HytaleCommandPermission(permissionAnn.value());
    }
}
