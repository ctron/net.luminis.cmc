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

import java.io.IOException;
import java.io.PrintStream;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

public abstract class AbstractCmSubCommand implements CmSubCommand {
	
	protected String pid;
	protected ConfigurationAdmin cm;
	protected PrintStream out;
	protected PrintStream err;

	/**
	 * @param  commandLine     the full command line as provided by the framework, can be null.
	 */
	public void execute(BundleContext context, String cmd, List args, String commandLine, PrintStream out, PrintStream err) {

		this.out = out;
		this.err = err;
		ServiceReference serviceReference = context.getServiceReference(ConfigurationAdmin.class.getName());
		if (serviceReference != null) {
			cm = (ConfigurationAdmin) context.getService(serviceReference);
		}
		if (cm == null) {
			error("no ConfigurationAdmin service");
			return;
		}

		try {
			int usedArgs = 1; // the first arg is the cm command itself.
			if (args.size() >= 2) {
				pid = (String) args.get(1);
				usedArgs++;
			}
			if (pid == null && needsPid()) {
				error("Missing argument: pid");
				return;
			}
			doCommand(context, cmd, args.subList(usedArgs, args.size()), commandLine);
		} catch (IOException e) {
			error("Configuration Admin service could not access persistent storage: " + e);
		}
	}
	
	protected boolean needsPid() {
		// Default is yes (because most commands do)
		return true;
	}

	protected void out(String line) {
		out.println(line);
	}
	
	protected void error(String line) {
		err.println(line);
	}
	
	public Configuration findConfiguration(String pid) throws IOException
	{
	    // As ConfigurationAdmin.getConfiguration creates the configuration if
        // it is not yet there, we check its existance first
        try {
            Configuration[] configurations = cm.listConfigurations("(service.pid=" + pid + ")");
            if (configurations != null && configurations.length > 0) {
                return configurations[0];
            }
        } catch (InvalidSyntaxException e) {}
        
        return null;
	}
	
	public void print(Configuration configuration, boolean verbose)
	{
	    out.println("");
		out.println("Configuration for service (pid) \"" + configuration.getPid() + "\""); 
		out.println("(bundle location = " + configuration.getBundleLocation() + ")");
		print(configuration.getProperties(), verbose);
	}

	private void print(Dictionary properties, boolean verbose) {
	    int maxKeyLength = 0;
	    Enumeration keys = properties.keys();
	    while (keys.hasMoreElements()) {
	        String key = (String) keys.nextElement();
	        if (key.length() > maxKeyLength) {
	            maxKeyLength = key.length();
	        }
	    }
	    int maxValueLength = 0;
        Enumeration values = properties.elements();
        while (values.hasMoreElements()) {
            Object value = values.nextElement();
            String stringValue = "";
            if (value != null)
                stringValue = value.toString();
            if (stringValue.length() > maxValueLength) {
                maxValueLength = stringValue.length();
            }
        }
	    String format = "%-" + (maxKeyLength + 2) + "s %-" + (maxValueLength + 6) + "s";
	    if (verbose)
	        format += " %s";
	    out.println();
        out.println(String.format(format, "key", "value", "type"));
        out.println(String.format(format, "------", "------", "------"));
        keys = properties.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            Object value = properties.get(key);
            out.println(String.format(format, key, value != null? value.toString(): "<null>", value != null? value.getClass().getName(): ""));
        }
        out.println("");
    }

    protected abstract void doCommand(BundleContext context, String cmd, List args, String commandLine)
		throws IOException;
}
