package org.dase.cogan.ingestion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.dase.cogan.renderer.RuleRenderer;
import org.semanticweb.owlapi.io.OWLRendererException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import com.google.common.io.Files;

public class OntologyIngestor
{
	public static void ingest(String pathname)
	{
		try
		{
			// Load Resources
			OWLConnector owlConnector = new OWLConnector(pathname);
			RuleRenderer ruleRenderer = new RuleRenderer();
			// Create file to write
			String filename = extractFilename(pathname);
			String output = "output/" + filename + ".tex";
			File outputFile = new File(output);
			outputFile.getParentFile().mkdirs(); // verifiest output directory exists
			PrintWriter pw = new PrintWriter(outputFile);
			// Render the ontology
			ruleRenderer.render(owlConnector.getOntology(), pw);
		}
		catch(OWLOntologyCreationException e)
		{
			System.out.println("Could not load ontology.");
		}
		catch(OWLRendererException e)
		{
			System.out.println("Could not render ontology.");
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Could not create output file.");
		}
	}

	private static void checkFileSystem()
	{
		
	}
	
	/** This method simply extracts the filename from the provided path */
	private static String extractFilename(String pathname)
	{
		// Get nodes
		String[] nodes = pathname.split("/");
		// Get last node
		int length = nodes.length - 1;
		String filenameNode = nodes[length];
		// Get filename from node
		String filename = filenameNode.split("\\.")[0];
		return filename;
	}
}
