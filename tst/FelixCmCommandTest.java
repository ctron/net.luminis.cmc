package net.luminis.cmc;

import java.util.List;

import junit.framework.TestCase;

public class FelixCmCommandTest extends TestCase {

    public void testCommandLine1()
    {
        String cmdLine = "cm get";
        List<String> result = new FelixCmCommand(null).parseCommandLine(cmdLine);
        assertEquals(2, result.size());
    }

    public void testCommandLine1WithSpaces()
    {
        String cmdLine = "cm  get";
        List<String> result = new FelixCmCommand(null).parseCommandLine(cmdLine);
        assertEquals(2, result.size());
    }

    public void testCommandLine2()
    {
        String cmdLine = "cm put thepid key value";
        List<String> result = new FelixCmCommand(null).parseCommandLine(cmdLine);
        assertEquals(5, result.size());
        assertEquals("put", result.get(1));
        assertEquals("thepid", result.get(2));
        assertEquals("key", result.get(3));
        assertEquals("value", result.get(4));
    }

    public void testCommandLine2WithSpaces()
    {
        String cmdLine = "cm put  thepid key  value";
        List<String> result = new FelixCmCommand(null).parseCommandLine(cmdLine);
        assertEquals(5, result.size());
        assertEquals("put", result.get(1));
        assertEquals("thepid", result.get(2));
        assertEquals("key", result.get(3));
        assertEquals("value", result.get(4));
    }

}