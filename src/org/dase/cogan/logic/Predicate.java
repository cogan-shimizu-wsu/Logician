package org.dase.cogan.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dase.cogan.operations.Negation;
import org.dase.cogan.operations.Node;

public class Predicate extends Node
{
	private List<String> args;

	/** Default Constructor */
	public Predicate(String predToken)
	{
		// Extract and set the name of the predicate
		int argIndex = predToken.indexOf("(");
		String label = predToken.substring(0, argIndex);
		super.setLabel(label);
		/* Extract the bound variables */
		// Get the strings
		String argString = predToken.substring(argIndex + 1, predToken.length() - 1);
		// Tokenize
		String[] argTokens = argString.split(",");
		// Add to the arglist
		args = new ArrayList<>();
		for(String arg : argTokens)
		{
			args.add(arg);
		}
	}

	/** Probably better practice than instanceof? */
	public boolean isPredicate()
	{
		return true;
	}

	public boolean isLiteral()
	{
		return true;
	}
	
	/** Negation of an atom P is -P */
	public Node negate()
	{
		// Negate
		Node negate = new Negation(this);
		// Done
		return negate;
	}

	/** By definition, Predicates are already in NNF */
	public Node toNNF()
	{
		return this;
	}

	/**
	 * Properly format the args with commas and parens such that it is a valid
	 * latex string
	 */
	public String toLatexString()
	{
		String line = super.getLatexLabel();
		line += "(";
		for(Iterator<String> i = args.iterator(); i.hasNext();)
		{
			String arg = "x_{" + i.next() + "}";
			if(i.hasNext())
			{
				arg += ",";
			}
			line += arg;
		}
		line += ")";
		return line;
	}

	/** Properly format the args with commas and parens. */
	public String toString()
	{
		String line = super.getLabel();
		line += "(";
		for(Iterator<String> i = args.iterator(); i.hasNext();)
		{
			String arg = i.next();
			if(i.hasNext())
			{
				arg += ",";
			}
			line += arg;
		}
		line += ")";
		return line;
	}
	
	/** returns the arity of the predicate (i.e. number of args) */
	public int getArity()
	{
		return args.size();
	}

	public List<String> getArgs()
	{
		return this.args;
	}
}
