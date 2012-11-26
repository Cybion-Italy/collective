package com.collective.document.analyzer.jena;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.collective.document.analyzer.domain.UrlMapped;
import thewebsemantic.Bean2RDF;

public class JenaOntologyModelBuilder {

	public JenaOntologyModelBuilder() {}

	public static OntModel buildOntologyModel(UrlMapped urlMapped) {

		OntModel memoryModel = ModelFactory
				.createOntologyModel(OntModelSpec.OWL_MEM_RDFS_INF);

		// add prefixes (dcterms, proconsult namespaces, etc.. )
		memoryModel.setNsPrefix("dcterms", "http://purl.org/dc/terms/");
		memoryModel.setNsPrefix("proconsult", "http://www.cybion.it/proconsult/");

		final Bean2RDF writer = new Bean2RDF(memoryModel);

		writer.save(urlMapped);
//		memoryModel.write(System.out, "N3");
		return memoryModel;
	}

}
