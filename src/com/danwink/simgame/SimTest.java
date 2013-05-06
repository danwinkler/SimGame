package com.danwink.simgame;

import javax.vecmath.Vector2f;

import com.phyloa.dlib.dui.AWTComponentEventMapper;
import com.phyloa.dlib.dui.DEventMapper;
import com.phyloa.dlib.dui.DUI;
import com.phyloa.dlib.renderer.Graphics2DRenderer;

public class SimTest extends Graphics2DRenderer
{
	Sim s;
	
	DUI dui;
	
	public void initialize()
	{
		size( 800, 600 );
		
		s = new Sim( "simdef.xml" );
		
		AWTComponentEventMapper em = new AWTComponentEventMapper();
		em.register( canvas );
		dui = new DUI( em );
		
		int i = 0;
		for( SimElement se : s.children )
		{
			if( se instanceof Button )
			{
				
			}
		}
	}

	@Override
	public void update()
	{
		s.tick();
		
		//RENDER
		int x = 100;
		for( String key : s.children.keySet() )
		{
			SimElement e = s.children.get( key );
			float amount = 0;
			if( e instanceof Stock )
			{
				amount = ((Stock)e).amount;
			} else if( e instanceof Agent )
			{
				amount = ((Agent)e).agents.size();
			}
			g.drawString( e.name + ": " + amount, x, 30 );
			
			x += 100;
		}
	}
	
	public static void main( String[] args )
	{
		SimTest st = new SimTest();
		st.begin();
	}
}
