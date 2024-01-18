package com.flump;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class PluginLauncher
{
	public static void main(String[] args) throws Exception
	{
//		ExternalPluginManager.loadBuiltin(ExamplePlugin.class);
//		ExternalPluginManager.loadBuiltin(DetectCombatPlugin.class);
		ExternalPluginManager.loadBuiltin(MousePlugin.class);
		RuneLite.main(args);
	}
}