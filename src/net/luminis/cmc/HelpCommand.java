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

import java.io.PrintStream;
import java.util.List;

public class HelpCommand implements CmSubCommand {

    private String commandPrefix;

    public HelpCommand() {
        commandPrefix = "cm ";
    }

    public HelpCommand(String specialCommandPrefix) {
        commandPrefix = specialCommandPrefix;
    }

    public void execute(BundleContext context, String cmd, List args, String cmdLine,
                        PrintStream out, PrintStream err) {

        String spacesPrefix = "        ".substring(0, commandPrefix.length());

        out.println("Usage:");
        out.println(commandPrefix + "help                  print this help message");
        out.println(commandPrefix + "list                  list all known configurations");
        out.println(commandPrefix + "get <pid>             show configuration for service <pid>");
        out.println(commandPrefix + "getv <pid>            verbose get (shows value types also)");
        out.println(commandPrefix + "put <pid> key value   set string value for service <pid>");
        out.println(commandPrefix + "puts <pid> key value  set \"simple\" value for service <pid>: value is \"true\", \"false\",");
        out.println(spacesPrefix  + "                      a char in single quotes, an int, or a number, with appended: ");
        out.println(spacesPrefix  + "                      i (Integer), l (Long), f (Float), d (Double), b (Byte), s (Short)");
        out.println(commandPrefix + "clear <pid> key       removes key/value from configuration");
        out.println(commandPrefix + "del <pid>             deletes configuration for service <pid>");
        out.println(commandPrefix + "create <pid> [<loc>]  creates configuration for service <pid> (with optional bundle location)");
        out.println(commandPrefix + "createf <factoryPid>  [<loc>] creates configuration for service factory <factoryPid> (with optional bundle location)");
    }
}
