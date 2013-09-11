/*
 * Copyright (c) 2008-2013 luminis
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

import org.osgi.framework.BundleContext;
import org.osgi.service.cm.Configuration;

import java.io.IOException;
import java.util.Dictionary;
import java.util.List;

public class ClearCommand extends AbstractCmSubCommand {

    @Override
    @SuppressWarnings("unchecked")
    protected void doCommand(BundleContext context, String cmd, List args, String commandLine) throws IOException {

        Configuration config = findConfiguration(pid);
        if (config == null) {
            out.println("no configuration for pid '" + pid + "' (use 'create' to create one)");
            return;
        }

        if (args.size() >= 1) {
            String key = (String) args.get(0);
            Dictionary props = config.getProperties();
            if (props.get(key) != null) {
                props.remove(key);
                config.update(props);
                out("removed key '" + key + "'.");
            }
            else {
                out("no such key '" + key + "'");
            }
        } else {
            error("cm clear: missing argument(s), expected <key>");
        }
    }
}