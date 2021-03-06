package org.dase.cogan.operations;

public class Disjunction extends BinaryOperator
{
	/** Default Constructor */
	public Disjunction(Node leftFormula, Node rightFormula)
	{
		super.setLabel("+");
		super.setLeftFormula(leftFormula);
		super.setRightFormula(rightFormula);
	}

	/** Negation requires demorgan's law */
	public Node negate()
	{
		// Negate both sides
		Node left = super.getLeftFormula().negate();
		Node right = super.getRightFormula().negate();
		// Flip the sign
		Node conjunction = new Conjunction(left, right);
		// Done
		return conjunction;
	}

	/** convert each of the disjuncts to NNF */
	public Node toNNF()
	{
		// Get the NNF of both sides
		Node left = super.getLeftFormula().toNNF();
		Node right = super.getRightFormula().toNNF();
		// Disjoin
		Node disjunction = new Disjunction(left, right);
		// Done
		return disjunction;
	}
	
	/** Infix latex string */
	public String toLatexString()
	{
		String line = super.getLeftFormula().toLatexString();
		line += " \\vee ";
		line += super.getRightFormula().toLatexString();
		
		return line; // TODO document
	}
}
