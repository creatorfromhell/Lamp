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
package revxrsal.commands.hytale.arguments;


import com.hypixel.hytale.server.core.command.system.arguments.system.Argument;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgumentType;
import revxrsal.commands.hytale.actor.HytaleCommandActor;
import revxrsal.commands.node.ParameterNode;

import java.util.function.Function;

import static revxrsal.commands.util.Classes.wrap;

/**
 * Interface for converting command argument types. This interface defines methods
 * to check compatibility between types and to provide a conversion function.
 */
@FunctionalInterface
public interface ArgumentConverter {

    default boolean appliesToType(final Class<?> type, final ParameterNode<? extends HytaleCommandActor, ?> parameterNode) {
        return wrap(parameterNode.type()) == wrap(type);
    }

    default boolean appliesToType(final Class<?> type, final Class<?> parameterType) {
        return wrap(parameterType) == wrap(type);
    }

    ArgumentType<?> convert(final ParameterNode<? extends HytaleCommandActor, ?> parameterNode);
}