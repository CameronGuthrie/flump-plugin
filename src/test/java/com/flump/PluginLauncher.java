package com.flump;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class PluginLauncher
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(TestPlugin.class);
//		PrintStream fileStream = new PrintStream("C:\\Users\\Admin\\Desktop\\RuneLite_Log.txt");
//		System.setOut(fileStream);
//		System.out.println("starting");
		RuneLite.main(args);
	}
}