package com.driversfiles.www.fs;

import com.netradius.commons.io.IOHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * {@inheritDoc}
 */
@Service("fileStoreService")
public class FileStoreServiceImpl implements FileStoreService {

	private static final Logger log = LoggerFactory.getLogger(FileStoreService.class);
	private static final String[][] EXTENSION_MIME_MAP = {{"pdf", "jpg", "gif", "png", "tif"},
														{"application/pdf", "image/jpeg", "image/gif", "image/png", "image/tif"}};

	@Autowired
	@Qualifier("externalResourcePath")
	private String fileStorePath;

	/**
	 * Checks path for path violations.
	 *
	 * @param path the path to check
	 */
	protected void check(String path) {
		if (path != null && path.contains("..")) {
			throw new SecurityException("Invalid path [" + path + "]. Path cannot contain '..'");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean delete(final String path) {
		final File file = getFile(path);
		if (file == null) {
			return false;
		}
		if (log.isDebugEnabled()) {
			log.debug("Deleting [" + file.getAbsolutePath() + "]");
		}
		return deleteHelper(file);
	}

	/**
	 * Helper method to recursively delete directories since directories cannot
	 * be deleted unless they are empty.
	 *
	 * @param file the directory or file to delete
	 * @return true if successful, false if otherwise
	 */
	protected boolean deleteHelper(final File file) {
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				deleteHelper(child);
			}
		}
		return file.delete();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean exists(final String path) {
		return getFile(path).exists();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<File> listFiles(String path) {
		List<File> fileList = new ArrayList<File>();
		check(path);
		File folder = new File(appendPath(fileStorePath, path));
		if (folder.isDirectory()) {
			File[] files = folder.listFiles();
			for (File file : files) {
				// we only list files, not directories
				if (!file.isDirectory()) {
					fileList.add(file);
				}
			}
		}
		return fileList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File getFile(final String path) {
		check(path);
		return new File(appendPath(fileStorePath, path));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileOutputStream getFileOutputStream(String path) throws IOException {
		final File file = getFile(path);
		if (!file.getParentFile().mkdirs()) {
			throw new IOException("Failed to create directories for " + file.getAbsolutePath());
		}
		return new FileOutputStream(file);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream readFile(final String path) throws IOException {
		assert path != null;
		final File file = getFile(path);
		if (file != null && file.isFile()) {
			if (log.isDebugEnabled()) {
				log.debug("Reading file [" + file.getAbsolutePath() + "]");
			}
			return new FileInputStream(file);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String readFileToString(final String path, final String charset)
			throws IOException {
		assert path != null;
		final byte[] buf = readFileToBytes(path);
		if (buf == null) {
			return null;
		}
		return new String(buf, charset);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] readFileToBytes(final String path) throws IOException {
		assert path != null;
		final InputStream in = readFile(path);
		if (in == null) {
			return null;
		}
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		final byte[] buf = new byte[100 * 1024]; // 100KB
		try {
			int read = in.read(buf);
			while (read != -1) {
				out.write(buf, 0, read);
				read = in.read(buf);
			}
		} finally {
			in.close();
		}
		return out.toByteArray();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File saveFile(final String path, final InputStream in) throws IOException {
		assert in != null;
		check(path);
		final byte[] buf = new byte[100 * 1024]; // 100KB
		OutputStream out = null;
		try {
			final File file = new File(appendPath(fileStorePath, path));
			if (log.isDebugEnabled()) {
				log.debug("Saving file [" + file.getAbsolutePath() + "]");
			}
			if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
				throw  new IOException("Unable to create directories for " + file.getAbsolutePath());
			}
			out = new FileOutputStream(file);
			int read = in.read(buf);
			while (read != -1) {
				out.write(buf, 0, read);
				read = in.read(buf);
			}
			return file;
		} finally {
			IOHelper.close(in);
			IOHelper.close(out);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveFile(String path, String content, String charset) throws IOException {
		saveFile(path, content.getBytes(charset));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveFile(String path, byte[] content) throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(content);
		saveFile(path, in);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public File saveImage(String path, String formatName, BufferedImage image) throws IOException {
		
		
		final File file = new File(appendPath(fileStorePath, path));
		if (log.isDebugEnabled()) {
			log.debug("Saving file [" + file.getAbsolutePath() + "]");
		}
		if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
			throw  new IOException("Unable to create directories for " + file.getAbsolutePath());
		}

		ImageIO.write(image, formatName, file);
		
		
		return file;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean writeFile(HttpServletResponse res, String path, String contentType, String filename)
			throws IOException {
		
		InputStream in = null;
		OutputStream out;
		try {
			// Setup InputStream
			File file = getFile(path);
			if (file == null || !file.canRead()) {
				return false;
			}
			in = new FileInputStream(file);
			byte[] buf = new byte[100 * 1024]; // 100KB
			int read = in.read(buf);

			// Setup OutputStream
			out = res.getOutputStream();

			// Setup response headers
			if (contentType == null) {
				contentType = getContentType(file);
			}
			if (contentType != null) {
				res.setContentType(contentType);
			} else {
				res.setContentType("application/octet-stream");
			}

			if (filename != null) {
				res.setHeader("Content-Disposition", "attachment; filename=" + filename + ";");
			}
			res.setHeader("Content-Length", Long.toString(file.length()));

			// Write the rest of the file
			while (read != -1) {
				out.write(buf, 0, read);
				read = in.read(buf);
			}
			out.flush();
			return true;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					log.error("Error closing input stream: " + e.getMessage(), e);
				}
			}
			// We do not want to close the OutputStream because other things may be written to it
		}
	}
	
    private String appendPath(String path1, String path2) {
    	
        if (path1 == null || path1.length() == 0) return path2 == null? "" : path2;
        if (path2 == null || path2.length() == 0) return path1 == null? "" : path1;

        if (path2.startsWith("/")) {
           path2 = path2.substring(1);
       }
       if (path1.endsWith("/")) {
           return path1 + path2;
       }
       else {
           return path1 + "/" + path2;
       }
	}

	private String getContentType(File file) {
		
		String filename = file.getName();
		int index = filename.lastIndexOf('.');
		if (index > 0) {
			String ext = filename.substring(index+1);
			
			for (int i = 0; i < EXTENSION_MIME_MAP[0].length; i++) {
				String docExt = EXTENSION_MIME_MAP[0][i];
				if (docExt.equals(ext)) {
					return EXTENSION_MIME_MAP[1][i];
				}
			}
		}
		return null;
	}

}
