/*
 * Copyright (c) 2008-2013 luminis
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

import java.io.IOException;
import java.util.Dictionary;

import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.Descriptor;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = ClearCommand.class, property = {
        CommandProcessor.COMMAND_SCOPE + "=cm",
        CommandProcessor.COMMAND_FUNCTION + "=clear"
})
public class ClearCommand {

    private ConfigurationAdmin configAdmin;

    @Reference
    public void setConfigAdmin(final ConfigurationAdmin configAdmin) {
        this.configAdmin = configAdmin;
    }

    @Descriptor("removes key/value from configuration")
    public void clear(@Descriptor("The PID of the configuration") final String pid,
            @Descriptor("The keys to clear") final String[] keys)
            throws IOException {

        final Configuration config = Configurations.findConfiguration(this.configAdmin, pid);

        if (config == null) {
            System.out.println("no configuration for pid '" + pid + "' (use 'create' to create one)");
            return;
        }

        if (keys.length <= 0) {
            System.err.println("cm clear: missing argument(s), expected <key>");
            return;
        }

        for (final String key : keys) {
            final Dictionary<String, Object> props = config.getProperties();
            if (props.get(key) != null) {
                props.remove(key);
                config.update(props);
                System.out.println("removed key '" + key + "'.");
            } else {
                System.out.println("no such key '" + key + "'");
            }

        }
    }
}