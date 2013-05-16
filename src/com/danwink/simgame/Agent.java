package com.danwink.simgame;

import java.util.ArrayList;

public class Agent extends SimElement
{
	ArrayList<AgentInstance> agents = new ArrayList<AgentInstance>();
	
	public void tick()
	{
		for( AgentInstance a : agents )
		{
			a.tick();
		}
	}
	
	public class AgentInstance extends SimElement
	{
		
	}
}
