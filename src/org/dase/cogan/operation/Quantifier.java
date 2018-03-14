package org.dase.cogan.operation;

/**
 * This abstract class is for defining the default behavior for both
 * quantifiers. It prevents code duplication in the getter/setter for their
 * labels and tostring methods
 * 
 * @author Cogs
 *
 */
public abstract class Quantifier extends UnaryOperator
{
	private String boundVar;
	
	public boolean isQuantifier()
	{
		return true;
	}
	
	public String getBoundVar()
	{
		return boundVar;
	}

	public void setBoundVar(String boundVar)
	{
		this.boundVar = boundVar;
	}

	public String toString()
	{
		// Construct
		String line = super.getLabel() + boundVar;
		line += "[";
		line += super.getFormula().toString();
		line += "]";
		// Done
		return line;
	}
}
