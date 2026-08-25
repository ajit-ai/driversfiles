package com.driversfiles.www.fs;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Files storage services for saving, reading and deleting files external to the application base.
 * Note that this class will not accept ".." in the path to any of its methods.
 */
public interface FileStoreService {

	/**
	 * Saves a file to the filesystem. The provided InputStream will be closed by this method.
	 *
	 * @param path the path to the file
	 * @param in the input stream to read
	 * @throws IOException if an error occurs
	 */
	File saveFile(String path, InputStream in) throws IOException;

	/**
	 * Saves a file to the filesystem.
	 *
	 * @param path the path to the file
	 * @param content the content to store
	 * @param charset the character set of the content
	 * @throws IOException if an error occurs
	 */
	void saveFile(String path, String content, String charset) throws IOException;

	/**
	 * Saves a file to the filesystem.
	 *
	 * @param path the path to the file
	 * @param content the content to store
	 * @throws IOException if an error occurs
	 */
	void saveFile(String path, byte[] content) throws IOException;

	/**
	 * Saves a BufferedImage instance to the filesystem.
	 * 
	 * @param path the relative path for file to saved to
	 * @param formatName image format (ie JPG, PNG, GIF)
	 * @param image BufferedImage instance to save
	 * @return a resulting File instance
	 * @throws IOException
	 */
	File saveImage(String path, String formatName, BufferedImage image) throws IOException;
	
	/**
	 * Deletes a file or directory.
	 *
	 * @param path the path to delete
	 * @return true if successful, false if otherwise
	 */
	boolean delete(String path);

	/**
	 * Checks if the given path exists.
	 *
	 * @param path the path to check
	 * @return true if exists, false if otherwise
	 */
	boolean exists(String path);

	/**
	 * List files on the specified path.
	 *
	 * @param path the path to check files
	 * 
	 * @return list of files inside the specified folder
	 */
	List<File> listFiles(String path);

	/**
	 * Opens an input stream to the file at the given path.
	 *
	 * @param path the path to the file to read
	 * @return the input stream to the file or null if not found
	 * @throws IOException if an error occurs
	 */
	InputStream readFile(String path) throws IOException;

	/**
	 * Reads the contents of the file at the given path into a string.
	 *
	 * @param path the path to the file
	 * @param charset the character set encoding
	 * @return the contents of the file or null if not found
	 * @throws IOException if an error occurs
	 */
	String readFileToString(String path, String charset) throws IOException;

	/**
	 * Reads the contents of the file at the given path into an array of bytes.
	 *
	 * @param path the path to the file
	 * @return the contents of the file or null if not found
	 * @throws IOException if an error occurs
	 */
	byte[] readFileToBytes(String path) throws IOException;

	/**
	 * Returns a file object to the file or directory at the given path.
	 *
	 * @param path the path to the file or directory
	 * @return the file object
	 */
	File getFile(String path);

	/**
	 * Returns a file output stream to the file or directory at the given path.
	 * If parent directories do not exist to the path, they will be created.
	 *
	 * @param path the path to the file
	 * @return the output stream
	 */
	FileOutputStream getFileOutputStream(String path) throws IOException;

	/**
	 * Helper method which can be used to write a file from the file store out to an HttpServletResponse. This method
	 * will call getOutputStream() on the HttpServletResponse passed into it. Please do not use
	 * HttpServletResponse#getPrintWriter() unless this method returns a false in which case you can be sure
	 * that an OutputStream was not retreived.
	 *
	 * @param res the HttpServletResponse to write out to
	 * @param path the path to the file to be written
	 * @param contentType the content type of the file or null for application/octet-stream
	 * @param filename the filename 
	 * @return true if written successfully, false if the file does not exist
	 * @throws IOException if an error occurs writing to the servlet response
	 */
	boolean writeFile(HttpServletResponse res, String path, String contentType, String filename) throws IOException;
}

