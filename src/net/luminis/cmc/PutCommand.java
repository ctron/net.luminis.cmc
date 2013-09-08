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
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.service.cm.Configuration;

public class PutCommand extends AbstractCmSubCommand {

	@Override
	@SuppressWarnings("unchecked")
	protected void doCommand(BundleContext context, String cmd, List args, String commandLine) throws IOException {

        Configuration config = findConfiguration(pid);
        if (config == null) 
        {
            out.println("no configuration for pid '" + pid + "' (use 'create' to create one)"); 
            return;
        }

	    if (args.size() >= 2) {
            Object value = null;
	        if (cmd.equals("puts")) {
	            value = parseValue(args.subList(1, args.size()));
	        }
	        else {
	            // The "value" argument can be a string containing spaces. On Equinox the command 
	            // parsing is done by the platform, and a value containing spaces is passed as one
	            // string. On Felix, we must parse the command line ourselves.
	            if (commandLine != null) {
	                // Must parse the command line ourselves
	                String[] strings = commandLine.trim().split(" +", 5);
	                value = strings.length == 5? strings[4]: null;
	            }
	            else {
	                // Command line is parsed by platform, use args as passed.
	                value = args.get(1);
	            }
	        }
	        if (value != null) {
	            Dictionary properties = config.getProperties();
	            if (properties == null)
	                properties = new Hashtable();

	            properties.put(args.get(0), value);
	            config.update(properties);
	        }
	    }
	    else
	        error("cm put: missing argument(s), expected <key> <value>");
	}

	private Object parseValue(List valueArgs) {
	    Object value;
        String rawValue = (String) valueArgs.get(0);
        if (rawValue.equalsIgnoreCase("true")) {
            return true;
        }
        else if (rawValue.equalsIgnoreCase("false")) {
            return false;
        }
        else if (rawValue.startsWith("'")) {
            value = parseChar(rawValue, false);
        }
        else if (rawValue.endsWith("l") || rawValue.endsWith("L")) {
            value = parseLong(rawValue.substring(0, rawValue.length() - 1));
        }
        else if (rawValue.endsWith("f") || rawValue.endsWith("F")) {
            value = parseFloat(rawValue.substring(0, rawValue.length() - 1));
        }
        else if (rawValue.endsWith("d") || rawValue.endsWith("D")) {
            value = parseDouble(rawValue.substring(0, rawValue.length() - 1));
        }
        else if (rawValue.endsWith("b") || rawValue.endsWith("B")) {
            value = parseByte(rawValue.substring(0, rawValue.length() - 1));
        }
        else if (rawValue.endsWith("s") || rawValue.endsWith("S")) {
            value = parseShort(rawValue.substring(0, rawValue.length() - 1));
        }
        else {
            value = parseInt(rawValue);
            if (value == null) {
                error("cannot parse argument \"" + rawValue + "\"");
            }
        }
        return value;
    }

    private Object parseInt(String rawValue) {
        try {
            if (rawValue.endsWith("i") || rawValue.endsWith("I")) {
                rawValue = rawValue.substring(0, rawValue.length() - 1);
            }
            return Integer.parseInt(rawValue);
        } catch (NumberFormatException e) {
            error("invalid argument (\"" + rawValue + "\"), cannot parse int");
            return null;
        }
    }

    private Object parseShort(String rawValue) {
        try {
            return Short.parseShort(rawValue);
        } catch (NumberFormatException e) {
            error("invalid argument (\"" + rawValue + "\"), cannot parse short");
            return null;
        }
    }

    private Object parseByte(String rawValue) {
        try {
            return Byte.parseByte(rawValue);
        } catch (NumberFormatException e) {
            error("invalid argument (\"" + rawValue + "\"), cannot parse byte");
            return null;
        }
    }

    private Object parseDouble(String rawValue) {
        try {
            return Double.parseDouble(rawValue);
        } catch (NumberFormatException e) {
            error("invalid argument (\"" + rawValue + "\"), cannot parse double");
            return null;
        }
    }

    private Object parseFloat(String rawValue) {
        try {
            return Float.parseFloat(rawValue);
        } catch (NumberFormatException e) {
            error("invalid argument (\"" + rawValue + "\"), cannot parse float");
            return null;
        }
    }

    private Object parseLong(String rawValue) {
        try {
            return Long.parseLong(rawValue);
        } catch (NumberFormatException e) {
            error("invalid argument (\"" + rawValue + "\"), cannot parse long");
            return null;
        }
    }

    private Object parseChar(String rawValue, boolean objectWrapper) {
		if (rawValue.length() == 3 && rawValue.charAt(0) == '\'' && rawValue.charAt(2) == '\'')
			return objectWrapper? new Character(rawValue.charAt(1)): rawValue.charAt(1);
		else {
			error("invalid argument: expected 'c'");
			return null;
		}
	}
}
