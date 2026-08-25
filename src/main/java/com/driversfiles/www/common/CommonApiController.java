package com.driversfiles.www.common;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.driversfiles.www.core.dao.DocumentDao;
import com.driversfiles.www.core.dao.PersonDao;
import com.driversfiles.www.core.data.Document;
import com.driversfiles.www.core.data.Person;
import com.driversfiles.www.core.data.PersonType;
import com.driversfiles.www.fs.FileStoreService;

/**
 * Handles common API requests.
 *
 * @author Mark Burns
 */
@Controller
public class CommonApiController {
	
	@Autowired
	@Qualifier("documentDao")
	private DocumentDao documentDao;
	
	@Autowired
	@Qualifier("fileStoreService")
	private FileStoreService fileStoreService;

	@Autowired
	@Qualifier("personDao")
	private PersonDao personDao;


	@RequestMapping(value = "/secure/api/persontypes.json", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public PersonType[] getPersonTypes() {    
	    return PersonType.values();
	}
	
	@RequestMapping(value = "/secure/api/user/search.json", produces="application/json")
	@ResponseBody
	public String[][] doUserSearch(@RequestParam String firstName,
			@RequestParam String lastName, @RequestParam String email,
			@RequestParam String type, @RequestParam String companyName,
			@RequestParam String companyNumber) {

		PersonType personType = null;
		if (type != null && !type.isEmpty())
			personType = PersonType.valueOf(type);

		List<Person> list = personDao.getPeople(firstName, lastName, email,
				personType, companyName, companyNumber, 0, 50);

		String[][] map = new String[list.size()][2];
		for (int i = 0; i < list.size(); i++) {
			Person person = list.get(i);
			map[i][0] = person.getEmail();
			map[i][1] = person.getFirstName().concat(" ")
					.concat(person.getLastName()).concat(" ")
					.concat(person.getEmail());
		}

		return map;
	}

	@RequestMapping(value = "/secure/api/user/validateswitch.json", produces="application/json")
	@ResponseBody
	public String validateUserSwitch(@RequestParam String username) {    

		Person person = personDao.findByEmail(username);
		if (person != null && person.getType() != PersonType.ADMIN) {
			return "true";
		}
		
		return "false";
	}
	
	@RequestMapping(value = "/secure/api/document/{docUuid}", method = RequestMethod.GET)
	public void getDocument(@PathVariable String docUuid, HttpServletResponse res) throws IOException {
		
		// Stream the doc to the browser
		Document doc = documentDao.findByUuid(docUuid);
		fileStoreService.writeFile(res, "documents/" + doc.getFilename(), "application/pdf",  doc.getFilename());
	}

	@RequestMapping(value = "/secure/api/trucks/{truckUuid}/documents/{name}.{ext}", method = RequestMethod.GET)
	public void getTruckDocument(@PathVariable String truckUuid, @PathVariable String name, 
			@PathVariable String ext, HttpServletResponse res) throws IOException {
		
		// Stream the doc to the browser
		String filename = String.format("trucks/%s/documents/%s.%s", truckUuid, name, ext);
		fileStoreService.writeFile(res, filename, null,  name);
	}


}
