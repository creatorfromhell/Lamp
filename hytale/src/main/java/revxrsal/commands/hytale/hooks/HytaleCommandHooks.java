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
package revxrsal.commands.hytale.hooks;

import com.hypixel.hytale.server.core.command.system.CommandManager;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.command.ExecutableCommand;
import revxrsal.commands.hook.CancelHandle;
import revxrsal.commands.hook.CommandRegisteredHook;
import revxrsal.commands.hook.CommandUnregisteredHook;
import revxrsal.commands.hytale.actor.ActorFactory;
import revxrsal.commands.hytale.actor.HytaleCommandActor;

import java.util.HashMap;
import java.util.Map;


public final class HytaleCommandHooks<A extends HytaleCommandActor> implements CommandRegisteredHook<A>, CommandUnregisteredHook<A> {

    private final Map<String, HytaleCommand<A>> registered = new HashMap<>();

    private final JavaPlugin plugin;
    private final ActorFactory<A> actorFactory;

    public HytaleCommandHooks(final JavaPlugin plugin, final ActorFactory<A> actorFactory) {
        this.plugin = plugin;
        this.actorFactory = actorFactory;
    }

    @Override
    public void onRegistered(@NotNull final ExecutableCommand<A> command, @NotNull final CancelHandle cancelHandle) {

        final String name = command.firstNode().name();
        if (!registered.containsKey(name)) {

            final String description = (command.description() == null)? "" : command.description();
            final HytaleCommand<A> hytaleCommand = new HytaleCommand<>(name, description, command.lamp(), actorFactory, command.permission());
            if(!hytaleCommand.hasBeenRegistered()) {
                plugin.getCommandRegistry().registerCommand(hytaleCommand);
            }
            registered.put(name, hytaleCommand);
        }
    }

    @Override public void onUnregistered(@NotNull final ExecutableCommand<A> command, @NotNull final CancelHandle cancelHandle) {
        final String label = command.firstNode().name();

        // check there's no other '/label' command. if so, unregister.
        if (!command.lamp().registry().any(c -> c != command && c.firstNode().name().equals(label))) {

            CommandManager.get().getCommandRegistration().entrySet().removeIf(entry ->
                    entry.getKey().equalsIgnoreCase(label)
                            && entry.getValue().getOwner() != null
                            && entry.getValue().getOwner().equals(plugin)
            );
        }
    }
}

