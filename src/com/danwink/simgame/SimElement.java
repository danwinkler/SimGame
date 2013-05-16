package com.danwink.simgame;

import java.util.ArrayList;
import java.util.HashMap;

public class SimElement
{
	SimElement parent;
	String name;
	private String fullname;
	
	HashMap<String, SimElement> children = new HashMap<String, SimElement>();
	ArrayList<Action> ontick = new ArrayList<Action>();
	
	public void tick()
	{
		for( String k : children.keySet() )
		{
			children.get( k ).tick();
		}
		for( Action a : ontick )
		{
			a.run();
		}
	}
	
	public Stock getStock( String name )
	{
		Stock ret = null;
		SimElement e = children.get( name );
		if( this instanceof Stock && name.equals( this.name ) )
		{
			ret = (Stock)this;
		}
		else if( e != null && e instanceof Stock )
		{
			ret = (Stock)e;
		}
		else if( parent != null )
		{
			ret = parent.getStock( name );
		}
		
		return ret;
	}
	
	public String getFullName()
	{
		if( fullname != null ) return fullname;
		
		if( parent != null )
		{
			String parentname = parent.getFullName();
			if( !parentname.equals( "" ) )
			{
				fullname = parentname + "." + name;
			}
			else
			{
				fullname = name;
			}
		}
		else 
		{
			fullname = name;
		}
		
		return fullname;
	}
}
