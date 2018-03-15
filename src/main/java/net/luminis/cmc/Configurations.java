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

import java.lang.reflect.Array;
import java.util.Dictionary;
import java.util.Enumeration;

import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

public final class Configurations {

    private Configurations() {
    }

    public static Configuration findConfiguration(final ConfigurationAdmin configAdmin, final String pid) {
        // As ConfigurationAdmin.getConfiguration creates the configuration if
        // it is not yet there, we check its existence first
        try {
            final Configuration[] configurations = configAdmin.listConfigurations("(service.pid=" + pid + ")");
            if (configurations != null && configurations.length > 0) {
                return configurations[0];
            }
        } catch (final RuntimeException e) {
            throw e;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public static void print(final Configuration configuration) {
        System.out.println("");
        System.out.println("Configuration for service (pid) \"" + configuration.getPid() + "\"");
        System.out.println("(bundle location = " + configuration.getBundleLocation() + ")");
        print(configuration.getProperties());
    }

    private static void print(final Dictionary<String, ?> properties) {
        int maxKeyLength = 0;
        Enumeration<String> keys = properties.keys();
        while (keys.hasMoreElements()) {
            final String key = keys.nextElement();
            if (key.length() > maxKeyLength) {
                maxKeyLength = key.length();
            }
        }
        int maxTypeLength = 0;
        final Enumeration<?> values = properties.elements();
        while (values.hasMoreElements()) {
            final Object value = values.nextElement();
            String stringType = "";
            if (value != null) {
                stringType = value.getClass().getName();
            }
            if (stringType.length() > maxTypeLength) {
                maxTypeLength = stringType.length();
            }
        }

        System.out.println();
        final String format = "%-" + (maxKeyLength + 2) + "s %-" + (maxTypeLength + 2) + "s %s";
        System.out.println(String.format(format, "key", "type", "value"));
        System.out.println(String.format(format, "------", "------", "------"));
        keys = properties.keys();

        while (keys.hasMoreElements()) {
            final String key = keys.nextElement();
            final Object value = properties.get(key);

            if (value != null && value.getClass().isArray()) {
                for (int i = 0; i < Array.getLength(value); i++) {
                    final Object v = Array.get(value, i);
                    System.out.println(String.format(format,
                            i == 0 ? key : "",
                            i == 0 && value != null ? value.getClass().getName() : "",
                            v != null ? v : "<null>"));
                }

            } else {
                System.out.println(String.format(format,
                        key,
                        value != null ? value.getClass().getName() : "",
                        value != null ? value : "<null>"));
            }
        }
        System.out.println();
    }

}
