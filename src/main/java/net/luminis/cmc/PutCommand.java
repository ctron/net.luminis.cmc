/*
 * Copyright (c) 2018 Jens Reimann
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * - Neither the name of the Luminis nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package net.luminis.cmc;

import static net.luminis.cmc.Configurations.findConfiguration;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.function.Function;

import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = PutCommand.class, property = {
        CommandProcessor.COMMAND_SCOPE + "=cm",
        CommandProcessor.COMMAND_FUNCTION + "=put"
})
public class PutCommand {

    private ConfigurationAdmin configAdmin;

    @Reference
    public void setConfigAdmin(final ConfigurationAdmin configAdmin) {
        this.configAdmin = configAdmin;
    }

    protected void internalPut(final String pid, final String key, final Object value) throws IOException {

        if (key == null || value == null) {
            System.err.println("cm put: missing argument(s), expected <key> <value>");
            return;
        }

        final Configuration config = findConfiguration(this.configAdmin, pid);
        if (config == null) {
            System.out.println("no configuration for pid '" + pid + "' (use 'create' to create one)");
            return;
        }

        Dictionary<String, Object> properties = config.getProperties();
        if (properties == null) {
            properties = new Hashtable<>();
        }

        properties.put(key, value);
        config.update(properties);
    }

    @Descriptor("set string value for service")
    public void put(
            @Descriptor("The type of the value (defaults to string)") @Parameter(names = { "-t",
                    "--type" }, absentValue = "String") String type,
            @Descriptor("The PID of the configuration") final String pid,
            @Descriptor("The name of the property to set") final String key,
            @Descriptor("The value") final String[] value) throws IOException {

        if (value.length == 0) {
            System.err.println("No value to set. Use 'clear' to remove a value.");
            return;
        }

        if (type == null) {
            type = "";
        }

        final Function<String, ?> converter;

        switch (type.toLowerCase()) {
        case "string":
            converter = s -> s;
            break;
        case "short":
            converter = Short::parseShort;
            break;
        case "i":
        case "int":
        case "integer":
            converter = Integer::parseInt;
            break;
        case "l":
        case "long":
            converter = Long::parseLong;
            break;
        case "d":
        case "double":
            converter = Double::parseDouble;
            break;
        case "f":
        case "float":
            converter = Float::parseFloat;
            break;
        case "b":
        case "bool":
        case "boolean":
            converter = Boolean::parseBoolean;
            break;
        case "byte":
            converter = Byte::parseByte;
            break;
        case "c":
        case "char":
        case "character":
            converter = s -> s.isEmpty() ? '\0' : s.charAt(0);
            break;
        default:
            System.err.format("Unknown type name '%s'.");
            return;
        }

        final Object first = converter.apply(value[0]);

        if (value.length == 1) {
            internalPut(pid, key, first);
        } else {
            final Object cvtArray = Array.newInstance(first.getClass(), value.length);
            Array.set(cvtArray, 0, first);
            for (int i = 1; i < value.length; i++) {
                Array.set(cvtArray, i, converter.apply(value[i]));
            }
            internalPut(pid, key, cvtArray);
        }

    }
}
