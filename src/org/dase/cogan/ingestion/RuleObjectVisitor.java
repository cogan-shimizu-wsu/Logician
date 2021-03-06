package org.dase.cogan.ingestion;

import static org.semanticweb.owlapi.io.ToStringRenderer.getRendering;
import static org.semanticweb.owlapi.util.OWLAPIStreamUtils.asList;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.semanticweb.owlapi.latex.renderer.LatexBracketChecker;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAnnotationValueVisitor;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataComplementOf;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataIntersectionOf;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataOneOf;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLDataUnionOf;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom;
import org.semanticweb.owlapi.model.OWLDatatypeRestriction;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLDisjointUnionAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLFacetRestriction;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLHasKeyAxiom;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasSelf;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectInverseOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLObjectVisitor;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLPropertyExpression;
import org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.SWRLBuiltInAtom;
import org.semanticweb.owlapi.model.SWRLClassAtom;
import org.semanticweb.owlapi.model.SWRLDArgument;
import org.semanticweb.owlapi.model.SWRLDataPropertyAtom;
import org.semanticweb.owlapi.model.SWRLDataRangeAtom;
import org.semanticweb.owlapi.model.SWRLDifferentIndividualsAtom;
import org.semanticweb.owlapi.model.SWRLIndividualArgument;
import org.semanticweb.owlapi.model.SWRLLiteralArgument;
import org.semanticweb.owlapi.model.SWRLObjectPropertyAtom;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.model.SWRLSameIndividualAtom;
import org.semanticweb.owlapi.model.SWRLVariable;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

public class RuleObjectVisitor implements OWLObjectVisitor
{
	// @formatter:off
		/** AND. */         public static final String		AND			= "* ";
		/** OR. */          public static final String		OR			= "+ ";
		/** NOT. */ 	    public static final String		NOT			= "- ";
		/** ALL. */ 	    public static final String		ALL			= "/A";
		/** SOME. */        public static final String		SOME		= "/E";
		/** HASVALUE. */    public static final String		HASVALUE	= "hasValue ";
		/** MIN. */ 	    public static final String		MIN			= "\\geq";
		/** MAX. */ 		public static final String		MAX			= "\\leq";
		/** MINEX. */ 		public static final String		MINEX		= ">";
		/** MAXEX. */ 		public static final String		MAXEX		= "<";
		/** EQUAL. */ 		public static final String		EQUAL		= "=";
		/** SUBCLASS. */ 	public static final String		SUBCLASS	= "&\\sqsubseteq ";
		/** EQUIV. */ 		public static final String		EQUIV		= "&\\equiv ";
		/** NOT_EQUIV. */ 	public static final String		NOT_EQUIV	= "&\\not\\equiv ";
		/** TOP. */ 		public static final String		TOP			= "\\top ";
		/** BOTTOM. */ 		public static final String		BOTTOM		= "\\bot ";
		/** SELF. */ 		public static final String		SELF		= "\\textsf{Self} ";
		/** CIRC. */ 		public static final String		CIRC		= "\\circ ";
		/** INVERSE */ 		public static final String		INVERSE		= "^-";
		/** RARROW */		public static final String		RARROW		= "> ";
		// @formatter:on
	///////////////////////////////////////////////////////////////

	private final StringWriter		writer;
	private final OWLDataFactory	df;
	private ShortFormProvider		shortFormProvider;

	///////////////////////////////////////////////////////////////

	private Stack<Integer>			scope;
	private int						usedVars;
	private boolean					suppress;
	private boolean					inverse;
	private boolean					reflexive;

	private boolean					useHybridSyntax;

	///////////////////////////////////////////////////////////////
	public RuleObjectVisitor(StringWriter writer, OWLDataFactory df)
	{
		this.writer = writer;
		this.df = df;
		shortFormProvider = new SimpleShortFormProvider();
		//////////////////////////////////////////////////
		this.useHybridSyntax = false;
		this.reset();
	}

	public void reset()
	{
		this.scope = new Stack<>();
		this.usedVars = 1;
		this.suppress = false;
		this.inverse = false;
		this.reflexive = false;
	}

	/********************************************************/
	public void setShortFormProvider(ShortFormProvider shortFormProvder)
	{
		shortFormProvider = shortFormProvder;
	}

	public boolean isUseHybridSyntax()
	{
		return useHybridSyntax;
	}

	public void setUseHybridSyntax(boolean useHybridSyntax)
	{
		this.useHybridSyntax = useHybridSyntax;
	}

	/********************************************************/
	private void writeSpace()
	{
		writer.write(" ");
	}

	private void write(Object o)
	{
		writer.write(o.toString());
	}

	private void write(int i)
	{
		writer.write(Integer.toString(i));
	}

	private void writeOpenBrace()
	{
		writer.append("{");
	}

	private void writeCloseBrace()
	{
		writer.append("}");
	}

	private void writeScope()
	{
		writer.write(curScope());
	}

	/********************************************************/
	private String curScope()
	{
		return Integer.toString(scope.peek());
	}

	private String upScope()
	{
		Integer i = scope.pop();
		String s = curScope();
		scope.push(i);
		return s;
	}

	/********************************************************/
	@Override
	public void visit(OWLClass ce)
	{
		String name = escapeName(shortFormProvider.getShortForm(ce));
		write(name);

		if(!suppress)
		{
			write("(");
			writeScope();
			write(")");
			writeSpace();
		}
	}

	@Override
	public void visit(OWLSubClassOfAxiom axiom)
	{
		// Entering new scope
		scope.push(usedVars++);
		// Write quantifier
		write(ALL);
		writeScope();
		writeSpace();
		// Write implication in preorder
		write(RARROW);
		axiom.getSubClass().accept(this);
		axiom.getSuperClass().accept(this);
		///////////////////////
		scope.pop();
	}

	@Override
	public void visit(OWLDisjointClassesAxiom axiom)
	{
		// Get Classes
		List<OWLClassExpression> classExpressions = asList(axiom.classExpressions());

		suppress = true;
		write("#AllDifferent(");
		// Write each class
		for(Iterator<OWLClassExpression> it = classExpressions.iterator(); it.hasNext();)
		{
			it.next().accept(this);
			if(it.hasNext())
			{
				write(",");
			}
		}
		write(")");
		suppress = false;
	}

	@Override
	public void visit(OWLEquivalentClassesAxiom axiom)
	{
		List<OWLClassExpression> classExpressions = asList(axiom.classExpressions());

		if(classExpressions.size() > 2) // TODO
		{
			suppress = true;
			write("#EquivalentClasses(");
			// Write each class
			for(Iterator<OWLClassExpression> it = classExpressions.iterator(); it.hasNext();)
			{
				it.next().accept(this);
				if(it.hasNext())
				{
					write(",");
				}
			}
			write(")");
			suppress = false;
		}
		else
		{
			// TODO
			// Split into two axioms
			OWLClassExpression lhs = classExpressions.get(0);
			OWLClassExpression rhs = classExpressions.get(1);

			df.getOWLSubClassOfAxiom(lhs, rhs).accept(this);
			write("|");
			this.reset();
			df.getOWLSubClassOfAxiom(rhs, lhs).accept(this);
		}
	}

	/*************** OBJECTS ***********************/
	@Override
	public void visit(OWLObjectProperty property)
	{
		String prop = escapeName(shortFormProvider.getShortForm(property));

		String s = "";
		if(!suppress)
		{
			s += "(";
			if(inverse)
			{
				s += curScope();
				s += ",";
				s += reflexive ? curScope() : upScope();
			}
			else
			{
				s += reflexive ? curScope() : upScope();
				s += ",";
				s += curScope();
			}
			s += ")";
		}

		write(prop + s);
		if(!suppress)
		{
			writeSpace();
		}
	}

	@Override
	public void visit(OWLObjectInverseOf property)
	{
		this.inverse = true;
		property.getInverse().accept(this);
		this.inverse = false;
	}

	@Override
	public void visit(OWLInverseObjectPropertiesAxiom axiom)
	{
		scope.push(usedVars++);
		write(ALL);
		writeScope();
		writeSpace();
		scope.push(usedVars++);
		write(ALL);
		writeScope();
		writeSpace();
		write(RARROW);
		axiom.getFirstProperty().accept(this);
		axiom.getSecondProperty().accept(this);
	}

	@Override
	public void visit(OWLEquivalentObjectPropertiesAxiom axiom)
	{
		suppress = true;
		for(Iterator<OWLObjectPropertyExpression> it = axiom.properties().iterator(); it.hasNext();)
		{
			it.next().accept(this);
			if(it.hasNext())
			{
				writeSpace();
				write(EQUIV);
				writeSpace();
			}
		}
		suppress = false;
	}

	/************ Object Quantifiers **************/
	@Override
	public void visit(OWLObjectAllValuesFrom ce)
	{
		scope.push(usedVars++);
		write(ALL);
		writeScope();
		writeSpace();
		write(RARROW);
		ce.getProperty().accept(this);
		ce.getFiller().accept(this);
		scope.pop();
	}

	@Override
	public void visit(OWLObjectSomeValuesFrom ce)
	{
		scope.push(usedVars++);
		write(SOME);
		writeScope();
		writeSpace();
		write(AND);
		ce.getProperty().accept(this);
		ce.getFiller().accept(this);
		scope.pop();
	}

	/************* Object Cardinality ************/

	@Override
	public void visit(OWLObjectExactCardinality ce)
	{
		scope.push(usedVars++);
		write(EQUAL);
		write(ce.getCardinality());
		writeScope();
		ce.getProperty().accept(this);
		write(AND);
		ce.getFiller().accept(this);
		scope.pop();
	}

	@Override
	public void visit(OWLObjectMaxCardinality ce)
	{
		int max = ce.getCardinality();
		for(int i = 0; i < max; i++)
		{
			write(ALL);
			scope.push(usedVars++);
			writeScope();
			writeSpace();
		}

		// TODO
		write(AND);
		ce.getProperty().accept(this);
		ce.getFiller().accept(this);

		// Pop off all used variables.
		for(int i = 0; i < max; i++, scope.pop())
			;
	}

	@Override
	public void visit(OWLObjectMinCardinality ce)
	{
		int min = ce.getCardinality();

		for(int i = 0; i < min; i++)
		{
			write(SOME);
			scope.push(usedVars++);
			writeScope();
			writeSpace();
		}

		// TODO
		write(AND);
		ce.getProperty().accept(this);
		ce.getFiller().accept(this);

		// Pop off all used variables.
		for(int i = 0; i < min; i++, scope.pop())
			;
	}

	/*********************************************/
	@Override
	public void visit(OWLFunctionalObjectPropertyAxiom axiom)
	{
		write("#Functional");
		write("(");
		suppress = true;
		axiom.getProperty().accept(this);
		suppress = false;
		write(")");
	}

	/************* Object Individuals ************/
	// TODO
	@Override
	public void visit(OWLObjectHasValue ce)
	{
		write(SOME);
		ce.getProperty().accept(this);
		writeSpace();
		writeOpenBrace();
		ce.getFiller().accept(this);
		writeCloseBrace();
	}

	// TODO
	@Override
	public void visit(OWLObjectOneOf ce)
	{
		write("#OneOf");
		write("(");
		for(Iterator<? extends OWLIndividual> it = ce.individuals().iterator(); it.hasNext();)
		{
			it.next().accept(this);
			if(it.hasNext())
			{
				write(",");
			}
		}
		write(")");
	}

	@Override
	public void visit(OWLNamedIndividual individual)
	{
		String name = escapeName(shortFormProvider.getShortForm(individual));

		write(name);
	}

	// TODO
	@Override
	public void visit(OWLObjectHasSelf ce)
	{
		reflexive = true;
		ce.getProperty().accept(this);
		reflexive = false;
	}

	/************* Object Operations ****************/
	@Override
	public void visit(OWLObjectComplementOf ce)
	{
		write(NOT);
		ce.getOperand().accept(this);
	}

	@Override
	public void visit(OWLObjectUnionOf ce)
	{
		List<? extends OWLClassExpression> operands = asList(ce.operands());
		int nOps = operands.size();

		write(OR);
		for(int i = 0; i < nOps; i++)
		{
			operands.get(i).accept(this);
			if(i + 2 < nOps)
			{
				write(OR);
			}
		}
	}

	@Override
	public void visit(OWLObjectIntersectionOf ce)
	{
		List<? extends OWLClassExpression> operands = asList(ce.operands());
		int nOps = operands.size();

		write(AND);
		for(int i = 0; i < nOps; i++)
		{
			operands.get(i).accept(this);
			if(i + 2 < nOps)
			{
				write(AND);
			}
		}
	}

	/************* Object Domain/Range ***********/
	@Override
	public void visit(OWLObjectPropertyDomainAxiom axiom)
	{
		scope.push(usedVars++);
		write(ALL);
		writeScope();
		writeSpace();
		write(RARROW);
		df.getOWLObjectSomeValuesFrom(axiom.getProperty(), df.getOWLThing()).accept(this);
		axiom.getDomain().accept(this);
		scope.pop();
	}

	@Override
	public void visit(OWLObjectPropertyRangeAxiom axiom)
	{
		scope.push(usedVars++);
		write(SOME);
		writeScope();
		writeSpace();
		write(RARROW);
		df.getOWLThing().accept(this);
		df.getOWLObjectAllValuesFrom(axiom.getProperty(), axiom.getRange()).accept(this);
		scope.pop();
	}

	/*********************************************/
	@Override
	public void visit(OWLDataProperty property)
	{
		String prop = escapeName(shortFormProvider.getShortForm(property));

		String s = "";
		if(!suppress)
		{
			s += "(";
			if(inverse)
			{
				s += curScope();
				s += ",";
				s += reflexive ? curScope() : upScope();
			}
			else
			{
				s += reflexive ? curScope() : upScope();
				s += ",";
				s += curScope();
			}
			s += ")";
		}

		write(prop + s);
		if(!suppress)
		{
			writeSpace();
		}
	}

	/************** Data Quantifiers ***************/
	@Override
	public void visit(OWLDataAllValuesFrom ce)
	{
		scope.push(usedVars++);
		write(ALL);
		writeScope();
		writeSpace();
		write(RARROW);
		ce.getProperty().accept(this);
		ce.getFiller().accept(this);
		scope.pop();
	}

	@Override
	public void visit(OWLDataSomeValuesFrom ce)
	{
		scope.push(usedVars++);
		write(SOME);
		writeScope();
		writeSpace();
		write(RARROW);
		ce.getProperty().accept(this);
		ce.getFiller().accept(this);
		scope.pop();
	}

	/*************** Data Cardinality **************/
	@Override
	public void visit(OWLDataExactCardinality ce)
	{
		scope.push(usedVars++);
		write(EQUAL);
		write(ce.getCardinality());
		writeScope();
		ce.getProperty().accept(this);
		write(AND);
		ce.getFiller().accept(this);
		scope.pop();
	}

	@Override
	public void visit(OWLDataMaxCardinality ce)
	{
		int max = ce.getCardinality();
		for(int i = 0; i < max; i++)
		{
			write(ALL);
			scope.push(usedVars++);
			writeScope();
			writeSpace();
		}

		// TODO
		write(AND);
		ce.getProperty().accept(this);
		ce.getFiller().accept(this);

		// Pop off all used variables.
		for(int i = 0; i < max; i++, scope.pop())
			;
	}

	@Override
	public void visit(OWLDataMinCardinality ce)
	{
		int min = ce.getCardinality();

		for(int i = 0; i < min; i++)
		{
			write(SOME);
			scope.push(usedVars++);
			writeScope();
			writeSpace();
		}

		// TODO
		write(AND);
		ce.getProperty().accept(this);
		ce.getFiller().accept(this);

		// Pop off all used variables.
		for(int i = 0; i < min; i++, scope.pop())
			;
	}

	/*********************************************/
	@Override
	public void visit(OWLDataHasValue ce)
	{
		write(SOME);
		ce.getProperty().accept(this);
		write(".");
		ce.getFiller().accept(this);
	}

	@Override
	public void visit(OWLAsymmetricObjectPropertyAxiom axiom)
	{
		write("#Assymetric");
		write("(");
		suppress = true;
		axiom.getProperty().accept(this);
		suppress = false;
		write(")");
	}

	@Override
	public void visit(OWLDataPropertyAssertionAxiom axiom)
	{
		axiom.getProperty().accept(this);
		writeSpace();
		write("(");
		axiom.getSubject().accept(this);
		writeSpace();
		axiom.getObject().accept(this);
		write(")");
	}

	@Override
	public void visit(OWLDataPropertyDomainAxiom axiom)
	{
		scope.push(usedVars++);
		write(ALL);
		writeScope();
		writeSpace();
		write(RARROW);
		df.getOWLDataSomeValuesFrom(axiom.getProperty(), df.getTopDatatype()).accept(this);
		axiom.getDomain().accept(this);
		scope.pop();
	}

	@Override
	public void visit(OWLDataPropertyRangeAxiom axiom)
	{
		scope.push(usedVars++);
		write(SOME);
		writeScope();
		writeSpace();
		write(RARROW);
		df.getOWLThing().accept(this);
		df.getOWLDataAllValuesFrom(axiom.getProperty(), axiom.getRange()).accept(this);
		scope.pop();
	}

	@Override
	public void visit(OWLSubDataPropertyOfAxiom axiom)
	{
		scope.push(usedVars++);
		write(ALL);
		writeScope();
		writeSpace();
		scope.push(usedVars++);
		write(ALL);
		writeScope();
		writeSpace();

		write(RARROW);
		axiom.getSubProperty().accept(this);
		axiom.getSuperProperty().accept(this);

		scope.pop();
		scope.pop();
	}

	@Override
	public void visit(OWLDeclarationAxiom axiom)
	{
		write("Declaration");
		axiom.getEntity().accept(this);
	}

	@Override
	public void visit(OWLDifferentIndividualsAxiom axiom)
	{
		for(Iterator<OWLIndividual> it = axiom.individuals().iterator(); it.hasNext();)
		{
			writeOpenBrace();
			it.next().accept(this);
			writeCloseBrace();
			if(it.hasNext())
			{
				writeSpace();
				write(NOT_EQUIV);
				writeSpace();
			}
		}
	}

	@Override
	public void visit(OWLDisjointDataPropertiesAxiom axiom)
	{
		for(Iterator<OWLDataPropertyExpression> it = axiom.properties().iterator(); it.hasNext();)
		{
			it.next().accept(this);
			if(it.hasNext())
			{
				writeSpace();
				write(NOT_EQUIV);
				writeSpace();
			}
		}
	}

	@Override
	public void visit(OWLDisjointObjectPropertiesAxiom axiom)
	{
		write("#Disjoint");
		write("(");
		for(Iterator<OWLObjectPropertyExpression> it = axiom.properties().iterator(); it.hasNext();)
		{
			it.next().accept(this);
			if(it.hasNext())
			{
				write(",");
			}
		}
		write(")");
	}

	@Override
	public void visit(OWLDisjointUnionAxiom axiom)
	{
		write("#DisjointClasses");
		write("(");

		for(Iterator<OWLClassExpression> it = axiom.classExpressions().iterator(); it.hasNext();)
		{
			it.next().accept(this);
			if(it.hasNext())
			{
				write(",");
			}
		}

		write(")");
	}

	/*********************************************/
	@Override
	public void visit(OWLDatatype node)
	{
		String name = getRendering(node);
		write(name);

		if(!suppress)
		{
			write("(");
			writeScope();
			write(")");
			writeSpace();
		}
	}

	@Override
	public void visit(OWLLiteral node)
	{
		write(getRendering(node.getDatatype()));
		write("(");
		write("``" + node.getLiteral() + "''");
		write(")");
		writeSpace();
	}

	/************* Assertions *********************/
	@Override
	public void visit(OWLClassAssertionAxiom axiom)
	{
		axiom.getIndividual().accept(this);
		writeSpace();
		write(":");
		writeSpace();
		axiom.getClassExpression().accept(this);
	}

	@Override
	public void visit(OWLAnnotationAssertionAxiom axiom)
	{
		write("Annotation");
		axiom.getSubject().accept(this);
		writeSpace();
		axiom.getProperty().accept(this);
		writeSpace();
		axiom.getValue().accept(this);
	}

	@Override
	public void visit(OWLEquivalentDataPropertiesAxiom axiom)
	{
		for(Iterator<OWLDataPropertyExpression> it = axiom.properties().iterator(); it.hasNext();)
		{
			it.next().accept(this);
			if(it.hasNext())
			{
				writeSpace();
				write(NOT_EQUIV);
				writeSpace();
			}
		}
	}

	@Override
	public void visit(OWLFunctionalDataPropertyAxiom axiom)
	{
		write(TOP);
		writeSpace();
		write(SUBCLASS);
		writeSpace();
		df.getOWLDataMaxCardinality(1, axiom.getProperty()).accept(this);
	}

	public void visit(OWLImportsDeclaration axiom)
	{
		write("ImportsDeclaration");
		axiom.getIRI().accept(this);
	}

	@Override
	public void visit(OWLInverseFunctionalObjectPropertyAxiom axiom)
	{
		write(TOP);
		writeSpace();
		write(SUBCLASS);
		writeSpace();
		OWLObjectPropertyExpression property = axiom.getProperty();
		if(property.isAnonymous())
		{
			df.getOWLObjectMaxCardinality(1, property).accept(this);
		}
		else
		{
			OWLObjectPropertyExpression prop = df.getOWLObjectInverseOf(property.asOWLObjectProperty());
			df.getOWLObjectMaxCardinality(1, prop).accept(this);
		}
	}

	@Override
	public void visit(OWLIrreflexiveObjectPropertyAxiom axiom)
	{
		write("#IrreflexiveObjectProperty");
		write("(");
		suppress = true;
		axiom.getProperty().accept(this);
		suppress = false;
		write(")");
	}

	@Override
	public void visit(OWLNegativeDataPropertyAssertionAxiom axiom)
	{
		write(NOT);
		axiom.getProperty().accept(this);
		write("(");
		axiom.getSubject().accept(this);
		write(", ");
		axiom.getObject().accept(this);
		write(")");
	}

	@Override
	public void visit(OWLNegativeObjectPropertyAssertionAxiom axiom)
	{
		write(NOT);
		axiom.getProperty().accept(this);
		write("(");
		axiom.getSubject().accept(this);
		write(", ");
		axiom.getObject().accept(this);
		write(")");
	}

	@Override
	public void visit(OWLObjectPropertyAssertionAxiom axiom)
	{
		axiom.getProperty().accept(this);
		write("(");
		axiom.getSubject().accept(this);
		write(", ");
		axiom.getObject().accept(this);
		write(")");
	}

	@Override
	public void visit(OWLSubPropertyChainOfAxiom axiom)
	{
		for(Iterator<OWLObjectPropertyExpression> it = axiom.getPropertyChain().iterator(); it.hasNext();)
		{
			it.next().accept(this);
			if(it.hasNext())
			{
				writeSpace();
				write(CIRC);
				writeSpace();
			}
		}
		writeSpace();
		write(SUBCLASS);
		writeSpace();
		axiom.getSuperProperty().accept(this);
	}

	@Override
	public void visit(OWLSubObjectPropertyOfAxiom axiom)
	{
		scope.push(usedVars++);
		write(ALL);
		writeScope();
		writeSpace();
		scope.push(usedVars++);
		write(ALL);
		writeScope();
		writeSpace();

		write(RARROW);
		axiom.getSubProperty().accept(this);
		axiom.getSuperProperty().accept(this);

		scope.pop();
		scope.pop();
	}

	@Override
	public void visit(OWLReflexiveObjectPropertyAxiom axiom)
	{
		write("#ReflexiveProperty");
		write("(");
		suppress = true;
		axiom.getProperty().accept(this);
		suppress = false;
		write(")");
	}

	@Override
	public void visit(OWLSameIndividualAxiom axiom)
	{
		for(Iterator<OWLIndividual> it = axiom.individuals().iterator(); it.hasNext();)
		{
			writeOpenBrace();
			it.next().accept(this);
			writeCloseBrace();
			if(it.hasNext())
			{
				writeSpace();
				write("=");
				writeSpace();
			}
		}
	}

	@Override
	public void visit(OWLSymmetricObjectPropertyAxiom axiom)
	{
		write("#Symmetric");
		write("(");
		suppress = true;
		axiom.getProperty().accept(this);
		suppress = false;
		write(")");
	}

	@Override
	public void visit(OWLDatatypeDefinitionAxiom axiom)
	{
		axiom.getDatatype().accept(this);
		write(EQUIV);
		axiom.getDataRange().accept(this);
	}

	@Override
	public void visit(OWLTransitiveObjectPropertyAxiom axiom)
	{
		write("#TransitiveProperty");
		write("(");
		suppress = true;
		axiom.getProperty().accept(this);
		suppress = false;
		write(")");
	}

	@Override
	public void visit(OWLOntology ontology)
	{
		// nothing to do here
	}

	@Override
	public void visit(OWLDataComplementOf node)
	{
		write(NOT);
		node.getDataRange().accept(this);
	}

	// TODO
	@Override
	public void visit(OWLDataOneOf node)
	{
		write("#OneOf");
		write("(");
		for(Iterator<? extends OWLLiteral> it = node.values().iterator(); it.hasNext();)
		{
			it.next().accept(this);
			if(it.hasNext())
			{
				write(",");
			}
		}
		write(")");
	}

	@Override
	public void visit(OWLFacetRestriction node)
	{
		String facet = node.getFacet().toString();
		if(facet.equalsIgnoreCase("minInclusive"))
		{
			write(MIN);
		}
		else if(facet.equalsIgnoreCase("minExclusive"))
		{
			write(MINEX);
		}
		else if(facet.equalsIgnoreCase("maxInclusive"))
		{
			write(MAX);
		}
		else if(facet.equalsIgnoreCase("maxExclusive"))
		{
			write(MAXEX);
		}
		else
		{
			write(facet);
		}

		node.getFacetValue().accept(this);
	}

	@Override
	public void visit(OWLDatatypeRestriction node)
	{
		write("DataTypeRestriction");
		write("(");
		node.getDatatype().accept(this);
		write(":");

		List<OWLFacetRestriction> facetRestrictions = asList(node.facetRestrictions());
		for(int i = 0; i < facetRestrictions.size(); i++)
		{
			OWLFacetRestriction r = facetRestrictions.get(i);
			writeSpace();
			r.accept(this);
			if(i != facetRestrictions.size() - 1)
			{
				writeSpace();
				write("and");
			}
		}

		write(")");
	}

	@Override
	public void visit(OWLHasKeyAxiom axiom)
	{
		axiom.getClassExpression().accept(this);
		write("&~hasKey~");
		write("(");
		for(Iterator<OWLPropertyExpression> it = axiom.propertyExpressions().iterator(); it.hasNext();)
		{
			it.next().accept(this);
			if(it.hasNext())
			{
				write(",");
				writeSpace();
			}
		}
		write(")");
	}

	@Override
	public void visit(OWLDataIntersectionOf node)
	{
		for(Iterator<? extends OWLDataRange> it = node.operands().iterator(); it.hasNext();)
		{
			it.next().accept(this);
			if(it.hasNext())
			{
				writeSpace();
				write(AND);
				writeSpace();
			}
		}
	}

	@Override
	public void visit(OWLDataUnionOf node)
	{
		for(Iterator<? extends OWLDataRange> it = node.operands().iterator(); it.hasNext();)
		{
			it.next().accept(this);
			if(it.hasNext())
			{
				writeSpace();
				write(OR);
				writeSpace();
			}
		}
	}

	@Override
	public void visit(OWLAnonymousIndividual individual)
	{
		write(individual.getID().toString());
	}

	@Override
	public void visit(IRI iri)
	{
		write(iri.getShortForm());
	}

	/*********************** TOOLS *****************/
	private void openBracket(OWLClassExpression classExpression)
	{
		if(LatexBracketChecker.requiresBracket(classExpression))
		{
			write("(");
		}
	}

	private void closeBracket(OWLClassExpression classExpression)
	{
		if(LatexBracketChecker.requiresBracket(classExpression))
		{
			write(")");
		}
	}

	private static String escapeName(String name)
	{
		return name.replace("_", "\\_").replace("#", "\\#");
	}

	/******************* SWRL **********************/
	@Override
	public void visit(SWRLLiteralArgument node)
	{
		node.getLiteral().accept(this);
	}

	@Override
	public void visit(SWRLIndividualArgument node)
	{
		node.getIndividual().accept(this);
	}

	@Override
	public void visit(SWRLBuiltInAtom node)
	{
		node.getPredicate().accept(this);
		for(SWRLDArgument d : node.getArguments())
		{
			writeSpace();
			d.accept(this);
		}
	}

	@Override
	public void visit(SWRLClassAtom node)
	{
		node.getArgument().accept(this);
	}

	@Override
	public void visit(SWRLDataRangeAtom node)
	{
		node.getPredicate().accept(this);
	}

	@Override
	public void visit(SWRLDataPropertyAtom node)
	{
		node.getPredicate().accept(this);
	}

	@Override
	public void visit(SWRLDifferentIndividualsAtom node)
	{
		node.allArguments().forEach(a -> {
			writeSpace();
			a.accept(this);
		});
	}

	@Override
	public void visit(SWRLObjectPropertyAtom node)
	{
		node.getPredicate().accept(this);
	}

	@Override
	public void visit(SWRLSameIndividualAtom node)
	{
		node.allArguments().forEach(a -> {
			writeSpace();
			a.accept(this);
		});
	}

	@Override
	public void visit(SWRLRule rule)
	{
		write("SWRLRule");
		rule.head().forEach(a -> a.accept(this));
		write("\\rightarrow");
		rule.body().forEach(a -> a.accept(this));
	}

	@Override
	public void visit(SWRLVariable node)
	{
		write(node.getIRI());
	}

	/******************* ANNOTATIONS ***************/
	@Override
	public void visit(OWLAnnotationProperty property)
	{
		write("AnnotationProperty");
		property.getIRI().accept(this);
	}

	@Override
	public void visit(OWLAnnotation node)
	{
		write("Annotation");
		node.getProperty().getIRI().accept(this);
		node.getValue().accept(this);
	}

	@Override
	public void visit(OWLAnnotationPropertyDomainAxiom axiom)
	{
		write("Domain");
		axiom.getProperty().getIRI().accept(this);
		writeSpace();
		axiom.getDomain().accept(this);
	}

	@Override
	public void visit(OWLAnnotationPropertyRangeAxiom axiom)
	{
		write("Range");
		axiom.getProperty().getIRI().accept(this);
		writeSpace();
		axiom.getRange().accept(this);
	}

	@Override
	public void visit(OWLSubAnnotationPropertyOfAxiom axiom)
	{
		axiom.getSubProperty();
		writeSpace();
		write(SUBCLASS);
		writeSpace();
		axiom.getSuperProperty().accept(this);
	}

	/**
	 * @param value
	 *            value
	 */
	public void visit(OWLAnnotationValue value)
	{
		value.accept(new OWLAnnotationValueVisitor()
		{

			@Override
			public void visit(IRI iri)
			{
				iri.accept(this);
			}

			@Override
			public void visit(OWLAnonymousIndividual individual)
			{
				individual.accept(this);
			}

			@Override
			public void visit(OWLLiteral literal)
			{
				literal.accept(this);
			}
		});
	}
}
