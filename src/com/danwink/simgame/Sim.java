package com.danwink.simgame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.phyloa.dlib.util.DFile;

public class Sim extends SimElement
{
	static Sim sim;
	
	HashMap<String, SimElement> allElements = new HashMap<String, SimElement>();
	
	BuildMode bm;
	
	public Sim( String deffile )
	{
		name = "";
		Sim.sim = this;
		try
		{
			loadDefinition( deffile );
		} catch( DocumentException e )
		{
			e.printStackTrace();
		}
	}
	
	public void loadDefinition( String file ) throws DocumentException
	{
		SAXReader reader = new SAXReader();
		Document doc = reader.read( new File( file ) );
		Element root = doc.getRootElement();
		Iterator<Element> i = root.elementIterator();
		while( i.hasNext() )
		{
			Element n = i.next();
			loadElement( this, n );
		}	
	}
	
	void loadElement( SimElement parent, Element n )
	{
		String name = n.getName();
		if( "agent".equals( name ) )
		{
			addAgentDef( parent, n );
		} 
		else if( "stock".equals( name ) )
		{
			addStockDef( parent, n );
		} 
		else if( "ontick".equals( name ) )
		{
			addOnTickDef( parent, n );
		}
		else if( "button".equals( name ) )
		{
			addButtonDef( parent, n );
		}
	}
	
	void addOnTickDef( SimElement parent, Element n )
	{
		parent.ontick.addAll( processActions( n ) );
	}
	
	void addAgentDef( SimElement parent, Element n )
	{
		Agent a = new Agent();
		a.name = n.attributeValue( "name" );
		a.parent = parent;
		parent.children.put( a.name, a );
		
		allElements.put( a.getFullName(), a );
		
		Iterator<Element> i = n.elementIterator();
		while( i.hasNext() )
		{
			Element c = i.next();
			loadElement( a, c );
		}
	}
	
	void addButtonDef( SimElement parent, Element n )
	{
		Button b = new Button();
		b.name = n.attributeValue( "name" );
		b.parent = parent;
		
		allElements.put( b.getFullName(), b );
		
		ArrayList<Action> actions = processActions( n );
		b.onClick.addAll( actions );
	}
	
	ConditionalAction buildCAction( Element n )
	{
		ConditionalAction ca = new ConditionalAction( n.attributeValue( "case" ) );
		ArrayList<Action> actions = processActions( n );
		ca.actions.addAll( actions );
		return ca;
	}
	
	ArrayList<Action> processActions( Element n )
	{
		ArrayList<Action> actions = new ArrayList<Action>();
		Iterator<Element> i = n.elementIterator();
		while( i.hasNext() )
		{
			Element child = i.next();
			String name = child.getName();
			if( "buildmode".equals( name ) )
			{
				BuildMode bm = new BuildMode((Agent)allElements.get( child.attributeValue( "agent" ) ) );
				bm.actions.addAll( processActions( child ) );
				actions.add( bm );
			}
			else if( "if".equals( name ) )
			{
				actions.add( buildCAction( child ) );
			}
			else if( "formula".equals( name ) )
			{
				actions.add( new FormulaAction( child.getText() ) );
			}
		}
		return actions;
	}
	
	void addStockDef( SimElement parent, Element n )
	{
		Stock s = new Stock();
		s.name = n.attributeValue( "name" );
		s.parent = parent;
		if( n.attribute( "start" ) != null )
			s.amount = Float.parseFloat( n.attributeValue( "start" ) );
		parent.children.put( s.name, s );
		
		allElements.put( s.getFullName(), s );
		
		Iterator<Element> i = n.elementIterator();
		while( i.hasNext() )
		{
			Element c = i.next();
			loadElement( s, c );
		}
	}
}
