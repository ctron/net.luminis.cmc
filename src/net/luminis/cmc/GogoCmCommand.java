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
        cmdProcessor.registerCommand("help", new Help());
    }

    public Dictionary getServiceProperties() {
        Dictionary dict = new Hashtable();
        dict.put("osgi.command.scope", "cmc");
        dict.put("osgi.command.function", new String[] { "help", "list", "get", "getv", "put", "puts", "del", "create", "createf"});
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

    private void execute(String cmd, String[] args) {
        List<String> commandLine = new ArrayList<String>();
        commandLine.add(cmd);
        commandLine.addAll(Arrays.asList(args));
        cmdProcessor.execute(commandLine, null, System.out, System.err);
    }

    static public class Help implements CmSubCommand {

		public void execute(BundleContext context, String cmd, List args, String cmdLine,
				PrintStream out, PrintStream err) {

			err.println("Usage:");
			err.println(" cmc:help                  print this help message");
			err.println(" cmc:list                  list all known configurations");
            err.println(" cmc:get <pid>             show configuration for service <pid>");
            err.println(" cmc:getv <pid>            verbose get (shows value types also)");
            err.println(" cmc:put <pid> key value   set string value for service <pid>");
            err.println(" cmc:puts <pid> key value  set \"simple\" value for service <pid>: value is \"true\", \"false\",");
            err.println("                           a char in single quotes, an int, or a number, with appended: ");
            err.println("                           i (Integer), l (Long), f (Float), d (Double), b (Byte), s (Short)");
            err.println(" cmc:del <pid>             deletes configuration for service <pid>");
            err.println(" cmc:create <pid> [<loc>]  creates configuration for service <pid> (with optional bundle location)");
            err.println(" cmc:createf <factoryPid> [<loc>] creates configuration for service factory <factoryPid> (with optional bundle location)");
		}
	}
}