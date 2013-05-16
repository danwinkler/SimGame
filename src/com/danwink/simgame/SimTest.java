package com.danwink.simgame;

import java.util.HashMap;

import org.boris.expr.util.SimpleEvaluationContext;

import com.phyloa.dlib.dui.AWTComponentEventMapper;
import com.phyloa.dlib.dui.DButton;
import com.phyloa.dlib.dui.DUI;
import com.phyloa.dlib.dui.DUIEvent;
import com.phyloa.dlib.dui.DUIListener;
import com.phyloa.dlib.renderer.Graphics2DRenderer;

public class SimTest extends Graphics2DRenderer implements DUIListener
{
	Sim s;
	
	DUI dui;
	
	HashMap<DButton, Button> buttonMap = new HashMap<DButton, Button>();
	
	public void initialize()
	{
		size( 800, 600 );
		
		s = new Sim( "simdef.xml" );
		
		AWTComponentEventMapper em = new AWTComponentEventMapper();
		em.register( canvas );
		dui = new DUI( em );
		dui.addDUIListener( this );
		
		int i = 0;
		for( String str : s.allElements.keySet() )
		{
			SimElement se = s.allElements.get( str );
			if( se instanceof Button )
			{
				Button b = (Button)se;
				DButton db = new DButton( b.name, i, getHeight()-50, 100, 50 );
				buttonMap.put( db, b );
				dui.add( db );
				i += 100;
			}
		}
	}

	@Override
	public void update()
	{
		if( m.clicked )
		{
			if( s.bm != null )
			{
				s.bm.build();
			}
			m.clicked = false;
		}
		
		s.tick();
		dui.update();
		
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
		
		dui.render( this );
	}
	
	public static void main( String[] args )
	{
		SimTest st = new SimTest();
		st.begin();
	}

	@Override
	public void event( DUIEvent event )
	{
		Button b = buttonMap.get( event.getElement() );
		if( b != null )
		{
			b.click();
		}
	}
}
