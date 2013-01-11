/*
 * Copyright (c) 2008 luminis
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

import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.framework.BundleContext;

public class CmCommandProcessor {

	private BundleContext context;
	private Map<String, CmSubCommand> commands;
	
	public CmCommandProcessor(BundleContext context) {
		this.context = context;
		commands = new HashMap<String, CmSubCommand>();
		commands.put("help", new Help());
		commands.put("list", new ListCommand());
        commands.put("get", new GetCommand());
        commands.put("getv", new GetCommand());
        commands.put("put", new PutCommand());
        commands.put("puts", new PutCommand());
		commands.put("del", new DeleteCommand());
		commands.put("create", new CreateCommand());
		commands.put("createf", new CreateFactoryCommand());
	}

    public void registerCommand(String cmd, CmSubCommand command) {
        commands.put(cmd, command);
    }

	public void execute(List<String> args, String commandLine, PrintStream out, PrintStream err) {
		
		if (args.size() >= 1) {
			String cmd = (String) args.get(0);
			if (commands.containsKey(cmd)) {
				((CmSubCommand) commands.get(cmd)).execute(context, cmd, args, commandLine, out, err);
			}
			else {
				new Help().execute(context, null, args, null, out, err);
			}
		}
		else {
			new Help().execute(context, null, args, null, out, err);
		}
	}
	
	static public class Help implements CmSubCommand {

		public void execute(BundleContext context, String cmd, List args, String cmdLine,
				PrintStream out, PrintStream err) {
			
			err.println("Usage:");
			err.println(" cm help                  print this help message");
			err.println(" cm list                  list all known configurations");
            err.println(" cm get <pid>             show configuration for service <pid>");
            err.println(" cm getv <pid>            verbose get (shows value types also)");
            err.println(" cm put <pid> key value   set string value for service <pid>");
            err.println(" cm puts <pid> key value  set \"simple\" value for service <pid>: value is \"true\", \"false\",");
            err.println("                          a char in single quotes, an int, or a number, with appended: ");
            err.println("                          i (Integer), l (Long), f (Float), d (Double), b (Byte), s (Short)"); 
            err.println(" cm del <pid>             deletes configuration for service <pid>");
            err.println(" cm create <pid> [<loc>]  creates configuration for service <pid> (with optional bundle location)");
            err.println(" cm createf <factoryPid> [<loc>] creates configuration for service factory <factoryPid> (with optional bundle location)");
		}
	}
	

}
