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
import java.util.*;

/**
 * CmCommand implementation for OSGi RFC-147. See http://felix.apache.org/site/rfc-147-overview.html
 */
public class GogoCmCommand {

    private BundleContext context;
    private CmCommandProcessor cmdProcessor;

    public GogoCmCommand(BundleContext context) {
        this.context = context;
        cmdProcessor = new CmCommandProcessor(context);
        // Override standard help, because we've a slightly different command syntax
        cmdProcessor.registerCommand("help", new HelpCommand("cmc:"));
    }

    public Dictionary getServiceProperties() {
        Dictionary dict = new Hashtable();
        dict.put("osgi.command.scope", "cmc");
        dict.put("osgi.command.function", new String[] { "help", "list", "get", "getv", "put", "puts", "del", "create", "createf", "clear" });
        return dict;
    }

    public void help(String[] args) {
        execute("help", args);
    }

    public void list(String[] args) {
        execute("list", args);
    }

    public void get(String[] args) {
        execute("get", args);
    }

    public void getv(String[] args) {
        execute("getv", args);
    }

    public void put(String[] args) {
        execute("put", args);
    }

    public void puts(String[] args) {
        execute("puts", args);
    }

    public void del(String[] args) {
        execute("del", args);
    }

    public void create(String[] args) {
        execute("create", args);
    }

    public void createf(String[] args) {
        execute("createf", args);
    }

    public void clear(String[] args) {
        execute("clear", args);
    }

    private void execute(String cmd, String[] args) {
        List<String> commandLine = new ArrayList<String>();
        commandLine.add(cmd);
        commandLine.addAll(Arrays.asList(args));
        cmdProcessor.execute(commandLine, null, System.out, System.err);
    }
}