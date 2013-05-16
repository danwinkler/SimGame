package com.danwink.simgame;

import java.util.ArrayList;

import org.boris.expr.Expr;
import org.boris.expr.ExprEvaluatable;
import org.boris.expr.parser.ExprParser;
import org.boris.expr.util.SimpleEvaluationContext;

public class FormulaAction extends Action
{
	Stock assignee;
	String formula;
	
	ArrayList<String> vars = new ArrayList<String>();
	
	public FormulaAction( String formula )
	{
		String[] parts = formula.split( "=" );
		assignee = (Stock)Sim.sim.allElements.get( parts[0].trim().toLowerCase() );
		this.formula = parts[1].trim().toLowerCase();
		
		String[] variables = formula.split( "[^a-zA-Z0-9.]+" );
		for( String v : variables )
		{
			if( !v.substring( 0, 1 ).matches( "[0-9]" ) )
			{
				vars.add( v.trim().toLowerCase() );
			}
		}
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
			
			Expr exp = ExprParser.parse( formula );
			if( exp instanceof ExprEvaluatable )
			{
				exp = ((ExprEvaluatable)exp).evaluate( c );
			}
			
			assignee.amount = Float.parseFloat( exp.toString() );
		} catch( Exception ex )
		{
			ex.printStackTrace();
		}
	}
}
