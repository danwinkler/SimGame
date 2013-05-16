package com.danwink.simgame;

import java.util.ArrayList;

import org.boris.expr.Expr;
import org.boris.expr.ExprEvaluatable;
import org.boris.expr.parser.ExprParser;
import org.boris.expr.util.SimpleEvaluationContext;

public class ConditionalAction extends Action
{
	String condition;
	ArrayList<Action> actions = new ArrayList<Action>();
	
	ArrayList<String> vars = new ArrayList<String>();
	
	public ConditionalAction( String condition )
	{
		String[] variables = condition.split( "[^a-zA-Z0-9.]+" );
		for( String v : variables )
		{
			if( !v.substring( 0, 1 ).matches( "[0-9]" ) )
			{
				vars.add( v.trim().toLowerCase() );
			}
		}
		
		this.condition = condition;
	}
	
	public void run()
	{
		try
		{
			SimpleEvaluationContext c = new SimpleEvaluationContext();
			
			for( String v : vars )
			{
				SimElement se = Sim.sim.allElements.get( v );
				if( se instanceof Stock )
				{
					Stock s = (Stock)se;
					c.setVariable( v, ExprParser.parse( "" + s.amount ) );
				}
				else if( se instanceof Agent )
				{
					Agent a = (Agent)se;
					c.setVariable( v, ExprParser.parse( "" + a.agents.size() ) );
				}
			}
			
			Expr exp = ExprParser.parse( condition );
			if( exp instanceof ExprEvaluatable )
			{
				exp = ((ExprEvaluatable)exp).evaluate( c );
			}
			if( exp.toString().equals( "TRUE" ) )
			{
				for( Action ac : actions )
				{
					ac.run();
				}
			}
			
		} catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}	
}
