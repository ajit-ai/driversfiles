package com.driversfiles.www.api;

import com.driversfiles.www.core.dao.DocumentDao;
import com.driversfiles.www.core.dao.PersonDao;
import com.driversfiles.www.core.data.Document;
import com.driversfiles.www.core.data.DocumentType;
import com.driversfiles.www.core.data.Person;
import com.driversfiles.www.fs.FileStoreService;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/driver/me/documents")
public class DocumentsApiController {

	private final PersonDao personDao;
	private final DocumentDao documentDao;
	private final FileStoreService fileStoreService;

	public DocumentsApiController(PersonDao personDao, DocumentDao documentDao,
			FileStoreService fileStoreService) {
		this.personDao = personDao;
		this.documentDao = documentDao;
		this.fileStoreService = fileStoreService;
	}

	public record DocumentDto(String uuid, String typeCode, String filename,
			@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") Date effectiveDate,
			@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") Date expirationDate) {}

	private Person currentUser(Authentication auth) {
		Person p = personDao.findByEmail(auth.getName());
		if (p == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		return p;
	}

	@GetMapping
	public List<DocumentDto> documents(Authentication auth) {
		return documentDao.getDocuments(currentUser(auth)).stream()
				.map(this::toDto).toList();
	}

	@PostMapping
	public DocumentDto upload(Authentication auth,
			@RequestParam("file") MultipartFile file,
			@RequestParam("typeCode") String typeCode,
			@RequestParam(value = "expirationDate", required = false) Date expirationDate)
			throws IOException {
		Person person = currentUser(auth);
		if (file == null || file.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is required");
		}
		DocumentType type;
		try {
			type = DocumentType.valueOf(typeCode);
		} catch (IllegalArgumentException x) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown document type: " + typeCode);
		}
		if (type == DocumentType.DOC_TYPE_CDL && expirationDate == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CDL requires an expiration date");
		}
		String ext = "";
		String original = file.getOriginalFilename();
		if (original != null && original.contains(".")) {
			ext = original.substring(original.lastIndexOf('.') + 1).toLowerCase();
		}
		String filename = UUID.randomUUID() + (ext.isEmpty() ? "" : "." + ext);
		fileStoreService.saveFile("documents/" + filename, file.getInputStream());

		Document doc = new Document(person, type.name(), filename, null, expirationDate);
		documentDao.save(doc);
		return toDto(documentDao.getByUuid(doc.getUuid()));
	}

	@GetMapping("/{uuid}/download")
	public void download(Authentication auth, @PathVariable String uuid,
			HttpServletResponse response) throws IOException {
		Person person = currentUser(auth);
		Document doc = documentDao.getByUuid(uuid);
		if (doc == null || !person.getId().equals(doc.getPerson().getId())) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		fileStoreService.writeFile(response, "documents/" + doc.getFilename(),
				"application/octet-stream", doc.getFilename());
	}

	@DeleteMapping("/{uuid}")
	public void delete(Authentication auth, @PathVariable String uuid) {
		Person person = currentUser(auth);
		Document doc = documentDao.getByUuid(uuid);
		if (doc == null || !person.getId().equals(doc.getPerson().getId())) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		documentDao.delete(doc);
	}

	private DocumentDto toDto(Document d) {
		return new DocumentDto(d.getUuid(), d.getTypeCode(), d.getFilename(),
				d.getEffectiveDate(), d.getExpirationDate());
	}
}
