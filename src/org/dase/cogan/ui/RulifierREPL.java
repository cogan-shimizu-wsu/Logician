package org.dase.cogan.ui;

import java.util.Scanner;

import org.dase.cogan.ingestion.OntologyIngestor;
import org.dase.cogan.ingestion.StringIngestor;
import org.dase.cogan.logic.CannotConvertToRuleException;
import org.dase.cogan.logic.Expression;

public class RulifierREPL
{
	/** Empty constructor */
	public RulifierREPL()
	{

	}

	public void ontoTest()
	{
		String pathname = "resources/";
		pathname += "trajectory.owl";
		OntologyIngestor.ingest(pathname);
	}
	
	/**
	 * this method is a 'read eval print loop' used for testing the ingestion
	 * code.
	 */
	public void stringTest()
	{
		// Init resources for loop
		Scanner keyboard = new Scanner(System.in);
		boolean loop = true;

		while(loop)
		{
			System.out.println("Type 'exit' to quit.");
			System.out.print("Enter a string for ingestion: ");

			// TODO debug code!
			// String line = keyboard.nextLine();
			String line = "/A1 /A2 > a(1,2) b(1,2)";

			loop = !line.equals("exit");
			if(loop)
			{
				Expression e = StringIngestor.ingest(line);
				System.out.println("The string ingested is: " + e);
				System.out.println("The negation is:        " + e.negated());
				System.out.println("The NNF is:             " + e.NNF());
				System.out.println("Printing scope listing for the NNF: ");
				e.NNF().printScope();
				System.out.println("The clausal form is:    " + e.toClausalForm());
				try
				{
					System.out.println("The rule form is:       " + e.toRule());
				}
				catch(CannotConvertToRuleException e1)
				{
					System.out.println("\tThe expression could not be converted to a rule.");
				}
			}

			// TODO
			loop = false;
		}

		// close resources
		keyboard.close();
	}
}
