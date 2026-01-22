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
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgumentType;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import revxrsal.commands.annotation.Range;
import revxrsal.commands.hytale.actor.HytaleCommandActor;
import revxrsal.commands.node.ParameterNode;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static revxrsal.commands.util.Classes.wrap;

/**
 * The ArgumentConversionFactory class serves as a factory for creating and managing
 * converters that handle the transformation of arguments from one data type to another.
 *
 * This class is intended to provide a centralized and extensible mechanism to define
 * logic for type conversion operations. It can be useful in scenarios requiring dynamic
 * type transformations, such as parsing input arguments or mapping data between
 * different systems or layers.
 *
 * It typically supports registration and retrieval of conversion strategies, allowing
 * clients to perform conversions without directly coupling to specific implementation
 * details of the converters.
 */
public class ArgumentConversionFactory {

    public final Map<Class<?>, ArgumentConverter> converters = new ConcurrentHashMap<>();

    public ArgumentConversionFactory() {
        registerDefaults();
    }

    public void addConverter(final Class<?> type, final ArgumentConverter converter) {
        converters.put(type, converter);
    }

    public ArgumentType<?> convert(final ParameterNode<? extends HytaleCommandActor, ?> parameterNode) {
        final Class<?> parameterType = wrap(parameterNode.type());

        if(converters.containsKey(parameterType)) return converters.get(parameterType).convert(parameterNode);

        for(final ArgumentConverter converter : converters.values()) {

            if(converter.appliesToType(parameterType, parameterNode)) {
                return converter.convert(parameterNode);
            }
        }
        throw new IllegalArgumentException("No converter found for parameter type: " + parameterType);
    }

    public void registerDefaults() {

        addConverter(PlayerRef.class, parameterNode -> ArgTypes.PLAYER_REF);

        addConverter(World.class, _ -> ArgTypes.WORLD);

        addConverter(String.class, _ -> ArgTypes.STRING);

        //hytale doesn't currently support characters out of box
        addConverter(Character.class, _ -> ArgTypes.STRING);

        addConverter(Float.class, _ -> ArgTypes.FLOAT);

        addConverter(Double.class, _ -> ArgTypes.DOUBLE);

        //hytale doesn't currently support longs, bytes, or shorts so we just convert them accordingly
        addConverter(Long.class, _ -> ArgTypes.INTEGER);
        addConverter(Byte.class, _ -> ArgTypes.INTEGER);
        addConverter(Short.class, _ -> ArgTypes.INTEGER);

        addConverter(Boolean.class, _ -> ArgTypes.BOOLEAN);

        addConverter(Integer.class, parameterNode -> {

            final Range range = parameterNode.annotations().get(Range.class);
            if(range == null) {
                return ArgTypes.INTEGER;
            }

            return ArgTypes.INT_RANGE;
        });

        addConverter(UUID.class, _ -> ArgTypes.UUID);
    }
}