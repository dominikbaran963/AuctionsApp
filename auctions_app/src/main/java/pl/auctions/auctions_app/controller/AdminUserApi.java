package pl.auctions.auctions_app.controller;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import pl.auctions.auctions_app.entity.Client;
import pl.auctions.auctions_app.entity.User;
import pl.auctions.auctions_app.payload.request.AdminUserDetailsChangeRequest;
import pl.auctions.auctions_app.payload.response.MessageResponse;
import pl.auctions.auctions_app.repository.ClientRepository;
import pl.auctions.auctions_app.repository.ProvinceRepository;
import pl.auctions.auctions_app.repository.UserRepository;

@CrossOrigin(origins = "*", maxAge = 10000)
@RestController
public class AdminUserApi {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ClientRepository clientRepository;
	
	@Autowired
	ProvinceRepository provinceRepository;
	
	@Autowired
	EntityManager em;
	
	@GetMapping(path = "/api/admin/users")
	public ResponseEntity<?> allClients() throws Exception, ParseException {
		List<Client> clientList = clientRepository.findAll();
		JSONArray ja = new JSONArray();
		for(Client client : clientList) {
			JSONObject jo = new JSONObject();
			jo.put("userId", client.getUser().getUser_id());//imie
			jo.put("firstname", client.getUser().getName());//imie
			jo.put("lastname", client.getUser().getLast_name());//nazwisko
			jo.put("location", client.getMiejscowosc());//miejscowosc
			jo.put("province", client.getWojewodztwo().getNazwa());//province
			jo.put("phone", client.getNrTelefonu());//nrTel
			jo.put("email", client.getUser().getEmail());//email
			jo.put("active", client.getUser().getActive()).toString(); //czy aktywny 
			
			ja.put(jo);
		}
		return ResponseEntity.ok(ja.toString());
	}
	
	@PostMapping(path = "/api/admin/users/{userId}/personal/change")
	public ResponseEntity<?> userDetailsPost(@PathVariable("userId") Long userId , @RequestBody String userRequest) throws Exception, ParseException {
		AdminUserDetailsChangeRequest userDetailsChange = new ObjectMapper().readValue(userRequest, AdminUserDetailsChangeRequest.class);

		Optional<User> client = userRepository.findById(userId);
		
		if(!client.isPresent())
			return (ResponseEntity<?>) ResponseEntity.of(client);
		User user = client.get();
		
		user.setName(userDetailsChange.getFirstname());
		user.setLast_name(userDetailsChange.getLastname());
		user.getKlient().setMiejscowosc(userDetailsChange.getLocation());
		user.getKlient().setIdWojewodztwa(provinceRepository.findByNazwa(userDetailsChange.getProvince()).get().getIdWojewodztwa());
		user.getKlient().setNrTelefonu(userDetailsChange.getPhone());
		user.getKlient().getUser().setEmail(userDetailsChange.getEmail());
		
		userRepository.save(user);
		
		return ResponseEntity.ok(new MessageResponse("Zmiany zapisane pomyślnie"));
	}
}
