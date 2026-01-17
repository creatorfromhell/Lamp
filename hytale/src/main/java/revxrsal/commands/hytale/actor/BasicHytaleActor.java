package revxrsal.commands.hytale.actor;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.plugin.PluginBase;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.Lamp;
import revxrsal.commands.process.MessageSender;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

final class BasicHytaleActor implements HytaleCommandActor {

    private static final UUID CONSOLE_UUID = new UUID(0, 0);
    private final CommandSender sender;
    private final PluginBase plugin;
    private final MessageSender<HytaleCommandActor, Message> messageSender;
    private final Lamp<HytaleCommandActor> lamp;

    BasicHytaleActor(
            final CommandSender sender,
            final PluginBase plugin,
            final MessageSender<HytaleCommandActor, Message> messageSender,
            final Lamp<HytaleCommandActor> lamp
    ) {
        this.sender = sender;
        this.plugin = plugin;
        this.messageSender = messageSender;
        this.lamp = lamp;
    }

    @Override public @NotNull CommandSender sender() {
        return sender;
    }

    @Override public void reply(@NotNull final Message message) {
        if (messageSender != null)
            messageSender.send(this, message);
    }

    @Override public @NotNull UUID uniqueId() {
        if (isPlayer()) {
            final Ref<EntityStore> ref = ((Player) sender).getReference();

            if (ref == null) return CONSOLE_UUID;

            final PlayerRef plrRef = ref.getStore().getComponent(ref, PlayerRef.getComponentType());

            if (plrRef == null) return CONSOLE_UUID;

            return plrRef.getUuid();
        } else if (isConsole()) {
            return CONSOLE_UUID;
        } else {
            return UUID.nameUUIDFromBytes(name().getBytes(StandardCharsets.UTF_8));
        }
    }

    @Override public Lamp<HytaleCommandActor> lamp() {
        return lamp;
    }

    public PluginBase plugin() {return plugin;}

    public MessageSender<HytaleCommandActor, Message> messageSender() {return messageSender;}

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        final BasicHytaleActor that = (BasicHytaleActor) obj;
        return Objects.equals(this.sender, that.sender) &&
                Objects.equals(this.plugin, that.plugin) &&
                Objects.equals(this.messageSender, that.messageSender) &&
                Objects.equals(this.lamp, that.lamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, plugin, messageSender, lamp);
    }

    @Override
    public String toString() {
        return "BasicBukkitActor[" +
                "sender=" + sender + ", " +
                "plugin=" + plugin + ", " +
                "messageSender=" + messageSender + ", " +
                "lamp=" + lamp + ']';
    }

}