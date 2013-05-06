package com.danwink.simgame;

import java.util.ArrayList;

import org.boris.expr.Expr;
import org.boris.expr.ExprEvaluatable;
import org.boris.expr.parser.ExprParser;
import org.boris.expr.util.SimpleEvaluationContext;

public class FormulaAction extends Action
{
	SimElement parent;
	Stock assignee;
	String formula;
	
	ArrayList<String> vars = new ArrayList<String>();
	
	public FormulaAction( SimElement parent, String formula )
	{
		this.parent = parent;
		String[] parts = formula.split( "=" );
		assignee = parent.getStock( parts[0].trim().toLowerCase() );
		this.formula = parts[1].trim().toLowerCase();
		
		String[] variables = formula.split( "[^a-zA-Z0-9]+" );
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
				c.setVariable( v, ExprParser.parse( "" + parent.getStock( v ).amount ) );
			}
			
			Expr exp = ExprParser.parse( formula );
			if( exp instanceof ExprEvaluatable )
			{
				exp = ((ExprEvaluatable)exp).evaluate( c );
			}
			
			assignee.amount = Float.parseFloat( exp.toString() );
		} catch( Exception ex )
		{
			
		}
	}
}
