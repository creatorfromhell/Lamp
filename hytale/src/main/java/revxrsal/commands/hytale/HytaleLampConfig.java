/*
 * This file is part of lamp, licensed under the MIT License.
 *
 *  Copyright (c) Revxrsal <reflxction.github@gmail.com>
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package revxrsal.commands.hytale;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import revxrsal.commands.Lamp;
import revxrsal.commands.LampBuilderVisitor;
import revxrsal.commands.hytale.actor.ActorFactory;
import revxrsal.commands.hytale.actor.HytaleCommandActor;
import revxrsal.commands.process.MessageSender;

import static revxrsal.commands.hytale.HytaleVisitors.*;
import static revxrsal.commands.util.Preconditions.notNull;

/**
 * A collective object that contains all Bukkit-only properties and allows for
 * easy customizing and chaining using a builder
 *
 * @param <A> The actor type.
 */
@AllArgsConstructor
public final class HytaleLampConfig<A extends HytaleCommandActor> implements LampBuilderVisitor<A> {

    private final ActorFactory<A> actorFactory;
    private final JavaPlugin plugin;

    /**
     * Returns a new {@link Builder} with the given plugin.
     *
     * @param plugin Plugin to create for
     * @param <A>    The actor type
     * @return The {@link Builder}
     */
    public static <A extends HytaleCommandActor> Builder<A> builder(@NotNull final JavaPlugin plugin) {
        notNull(plugin, "plugin");
        return new Builder<>(plugin);
    }

    /**
     * Returns a new {@link HytaleLampConfig} with the given plugin,
     * containing the default settings.
     *
     * @param plugin Plugin to create for
     * @return The {@link Builder}
     */
    public static HytaleLampConfig<HytaleCommandActor> createDefault(@NotNull final JavaPlugin plugin) {
        notNull(plugin, "plugin");
        return new HytaleLampConfig<>(
                ActorFactory.defaultFactory(plugin),
                plugin
        );
    }

    @Override public void visit(final Lamp.@NotNull Builder<A> builder) {
        builder.accept(legacyColorCodes())
                .accept(hytaleSenderResolver())
                .accept(hytaleParameterTypes())
                .accept(hytaleExceptionHandler())
                .accept(hytalePermissions())
                .accept(registrationHooks(plugin, actorFactory))
                .accept(pluginContextParameters(plugin));
    }

    /**
     * Represents a builder for {@link HytaleLampConfig}
     *
     * @param <A> The actor type
     */
    public static class Builder<A extends HytaleCommandActor> {

        // avoid loading BukkitArgumentTypes because it may trigger a
        // ClassNotFoundError
        private final @NotNull JavaPlugin plugin;
        private ActorFactory<A> actorFactory;
        private @Nullable MessageSender<A, Message> messageSender;

        Builder(@NotNull final JavaPlugin plugin) {
            this.plugin = plugin;
        }

        /**
         * Sets the {@link ActorFactory}. This allows to supply custom implementations for
         * the {@link HytaleLampConfig} interface.
         *
         * @param actorFactory The actor factory
         * @return This builder
         * @see ActorFactory
         */
        public @NotNull Builder<A> actorFactory(@NotNull final ActorFactory<A> actorFactory) {
            this.actorFactory = actorFactory;
            return this;
        }

        /**
         * Registers the default message sender used by {@link HytaleCommandActor#reply(Message)}
         *
         * @param messageSender The sender to use
         * @return This builder instance
         */
        public @NotNull Builder<A> messageSender(@Nullable final MessageSender<? super A, Message> messageSender) {
            //noinspection unchecked
            this.messageSender = (MessageSender<A, Message>) messageSender;
            return this;
        }

        /**
         * Returns a new {@link HytaleLampConfig} from this builder
         *
         * @return The newly created config
         */
        @Contract("-> new")
        public @NotNull HytaleLampConfig<A> build() {
            if (this.actorFactory == null) {
                //noinspection unchecked
                this.actorFactory = (ActorFactory<A>) ActorFactory.defaultFactory(
                        plugin,
                        messageSender
                );
            }
            return new HytaleLampConfig<>(
                    this.actorFactory,
                    this.plugin
            );
        }
    }
}
