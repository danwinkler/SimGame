package com.danwink.simgame;

import java.util.ArrayList;

public class BuildMode extends Action
{
	Agent a;
	ArrayList<Action> actions = new ArrayList<Action>();
	
	public BuildMode( Agent a )
	{
		this.a = a;
	}

	public void run()
	{
		Sim.sim.bm = this;
	}
	
	public void build()
	{
		for( Action ac : actions )
		{
			ac.run();
		}
	}
}
