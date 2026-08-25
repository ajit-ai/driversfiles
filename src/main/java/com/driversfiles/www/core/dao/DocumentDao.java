package com.driversfiles.www.core.dao;

import java.util.List;

import com.driversfiles.www.core.data.Document;
import com.driversfiles.www.core.data.Person;

/**
 * Data access methods for document data.
 *
 * @author Ajit Kumar
 */
public interface DocumentDao extends Dao<Document, Long> {

	/**
	 * Gets all documents for a Person.
	 *
	 * @param person the person
	 * @return the list of documents
	 */
	List<Document> getDocuments(Person person);
	
	/**
	 * Gets a specific document type for a person. 
	 * Returns null if one does not exist.
	 *
	 * @param person the person
	 * @param type the type of document
	 * @return the Document
	 */
	Document getDocument(Person person, String type);
	
}
