package com.danwink.simgame;

import java.util.ArrayList;

public class Button extends SimElement
{
	ArrayList<Action> onClick = new ArrayList<Action>();
	
	public void click()
	{
		for( Action a : onClick )
		{
			a.run();
		}
	}
}
