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

import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.Descriptor;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = ListCommand.class, property = {
        CommandProcessor.COMMAND_SCOPE + "=cm",
        CommandProcessor.COMMAND_FUNCTION + "=list"
})
public class ListCommand {

    private ConfigurationAdmin configAdmin;

    @Reference
    public void setConfigAdmin(final ConfigurationAdmin configAdmin) {
        this.configAdmin = configAdmin;
    }

    @Descriptor("list all known configurations")
    public void list() throws IOException {
        try {
            Configuration[] configurations;
            configurations = this.configAdmin.listConfigurations(null);
            System.out.println("Configuration list:");
            System.out.println("----------------------------");
            if (configurations == null || configurations.length == 0) {
                System.out.println("   (none)");
                return;
            }
            int maxPidLength = 0;
            for (final Configuration c : configurations) {
                if (c.getPid().length() > maxPidLength) {
                    maxPidLength = c.getPid().length();
                }
            }
            final int tabPosition = maxPidLength + 4;
            for (final Configuration c : configurations) {
                final String bundleLocation = c.getBundleLocation();
                final StringBuilder tab = new StringBuilder();
                for (int i = tabPosition; i >= c.getPid().length(); i--) {
                    tab.append(" ");
                }
                System.out.println(c.getPid() + (bundleLocation != null ? tab + bundleLocation : ""));
            }
        } catch (final InvalidSyntaxException e) {
            // impossible
        }
    }
}
