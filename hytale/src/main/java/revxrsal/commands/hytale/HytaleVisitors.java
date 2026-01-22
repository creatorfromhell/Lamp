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

import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.console.ConsoleSender;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.PluginBase;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.Lamp;
import revxrsal.commands.LampBuilderVisitor;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.exception.CommandExceptionHandler;
import revxrsal.commands.hytale.actor.ActorFactory;
import revxrsal.commands.hytale.actor.HytaleCommandActor;
import revxrsal.commands.hytale.annotation.CommandPermission;
import revxrsal.commands.hytale.exception.HytaleExceptionHandler;
import revxrsal.commands.hytale.hooks.HytaleCommandHooks;
import revxrsal.commands.hytale.parameters.PlayerParameterType;
import revxrsal.commands.hytale.parameters.WorldParameterType;
import revxrsal.commands.hytale.sender.HytalePermissionFactory;
import revxrsal.commands.hytale.sender.HytaleSenderResolver;
import revxrsal.commands.parameter.ContextParameter;

import static revxrsal.commands.hytale.util.HytaleUtils.legacyColorize;


/**
 * Includes modular building blocks for hooking into the Hytale
 * platform.
 * <p>
 * Accept individual functions using {@link Lamp.Builder#accept(LampBuilderVisitor)}
 */
public final class HytaleVisitors {

    /**
     * Makes the default format for {@link CommandActor#reply(String)} and {@link CommandActor#error(String)}
     * take the legacy ampersand ChatColor-coded format
     *
     * @param <A> The actor type
     * @return The visitor
     */
    public static <A extends HytaleCommandActor> @NotNull LampBuilderVisitor<A> legacyColorCodes() {
        return builder -> builder
                .defaultMessageSender((actor, message) -> actor.sendRawMessage(legacyColorize(message)))
                .defaultErrorSender((actor, message) -> actor.sendRawMessage(legacyColorize("&c" + message)));
    }

    /**
     * Handles the default Hytale exceptions
     *
     * @param <A> The actor type
     * @return The visitor
     */
    public static <A extends HytaleCommandActor> @NotNull LampBuilderVisitor<A> hytaleExceptionHandler() {
        //noinspection unchecked
        return builder -> builder.exceptionHandler((CommandExceptionHandler<A>) new HytaleExceptionHandler());
    }

    /**
     * Resolves the sender type {@link CommandSender}, {@link Player} and {@link ConsoleSender}
     * for parameters that come first in the command.
     *
     * @param <A> The actor type
     * @return The visitor
     */
    public static <A extends HytaleCommandActor> @NotNull LampBuilderVisitor<A> hytaleSenderResolver() {
        return builder -> builder.senderResolver(new HytaleSenderResolver());
    }

    /**
     * Registers the following parameter types:
     * <ul>
     *     <li>{@link Player}</li>
     *     <li>{@link World}</li>
     * </ul>
     *
     * @return The visitor
     */
    public static <A extends HytaleCommandActor> @NotNull LampBuilderVisitor<A> hytaleParameterTypes() {
        return builder -> {
            builder.parameterTypes()
                    .addParameterTypeLast(PlayerRef.class, new PlayerParameterType())
                    .addParameterTypeLast(World.class, new WorldParameterType());
        };
    }

    /**
     * Adds a registration hook that injects Lamp commands into Hytale
     *
     * @param plugin The plugin instance to bind commands to
     * @return The visitor
     */
    public static @NotNull LampBuilderVisitor<HytaleCommandActor> registrationHooks(@NotNull final JavaPlugin plugin) {
        return registrationHooks(plugin, ActorFactory.defaultFactory(plugin));
    }

    /**
     * Adds a registration hook that injects Lamp commands into Hytale.
     * <p>
     * This function allows to specify a custom {@link ActorFactory} to
     * use custom implementations of {@link HytaleCommandActor}
     *
     * @param plugin                The plugin instance to bind commands to
     * @param actorFactory          The actor factory. This allows for creating custom {@link HytaleCommandActor}
     *                              implementations
     * @return The visitor
     */
    public static <A extends HytaleCommandActor> @NotNull LampBuilderVisitor<A> registrationHooks(
            @NotNull final JavaPlugin plugin,
            @NotNull final ActorFactory<A> actorFactory
    ) {
        final HytaleCommandHooks<A> hooks = new HytaleCommandHooks<>(plugin, actorFactory);
        return builder -> builder.hooks().onCommandRegistered(hooks);
    }

    /**
     * Adds {@link PluginBase} dependencies and type resolvers
     *
     * @param plugin Plugin to supply
     * @param <A>    The actor type
     * @return The visitor
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <A extends HytaleCommandActor> @NotNull LampBuilderVisitor<A> pluginContextParameters(final JavaPlugin plugin) {
        return builder -> {
            builder.parameterTypes().addContextParameterLast(PluginBase.class, (parameter, context) -> plugin);
            builder.parameterTypes().addContextParameterLast(plugin.getClass(), (ContextParameter) (parameter, context) -> plugin);
            builder.dependency(PluginBase.class, plugin);
            builder.dependency((Class) plugin.getClass(), plugin);
        };
    }

    /**
     * Adds support for the {@link CommandPermission} annotation
     *
     * @param <A> The actor type
     * @return This visitor
     */
    public static <A extends HytaleCommandActor> @NotNull LampBuilderVisitor<A> hytalePermissions() {
        return builder -> builder
                .permissionFactory(HytalePermissionFactory.INSTANCE);
    }
}
