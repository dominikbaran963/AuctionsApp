package pl.auctions.auctions_app.controller;

import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import pl.auctions.auctions_app.entity.User;
import pl.auctions.auctions_app.payload.request.PasswordChangeRequest;
import pl.auctions.auctions_app.payload.request.UserDetailsRequest;
import pl.auctions.auctions_app.payload.response.MessageResponse;
import pl.auctions.auctions_app.repository.ClientRepository;
import pl.auctions.auctions_app.repository.ProvinceRepository;
import pl.auctions.auctions_app.repository.RoleRepository;
import pl.auctions.auctions_app.repository.UserRepository;

@CrossOrigin(origins = "*", maxAge = 10000)
@RestController

public class UserPanelController {
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	ProvinceRepository provinceRepository;

	@Autowired
	ClientRepository clientRepository;

	@RequestMapping(path = "/api/users/{userId}")
	public ResponseEntity<?> userDetails(@PathVariable("userId") Long userId) throws Exception, ParseException {
		Optional<User> client = userRepository.findById(userId);
		if(!client.isPresent())
			return (ResponseEntity<?>) ResponseEntity.of(client);
		String jsonString = new JSONObject()
				.put("firstname", client.get().getName())//imie
				.put("lastname", client.get().getLast_name())//nazwisko
				.put("location", client.get().getKlient().getMiejscowosc())//miejscowosc
				.put("province", client.get().getKlient().getWojewodztwo().getNazwa())//province
				.put("phone", client.get().getKlient().getNrTelefonu())//nrTel
				.put("email", client.get().getEmail())//email
				.put("active", client.get().getActive()).toString(); //czy aktywny 
		return ResponseEntity.ok(jsonString);
	}
	
	@PostMapping(path = "/api/users/{userId}/personal")
	public ResponseEntity<?> userDetailsPost(@PathVariable("userId") Long userId , @RequestBody String userRequest) throws Exception, ParseException {
		UserDetailsRequest userDetails = new ObjectMapper().readValue(userRequest, UserDetailsRequest.class);
		Optional<User> client = userRepository.findById(userId);
		
		if(!client.isPresent())
			return (ResponseEntity<?>) ResponseEntity.of(client);
		User user = client.get();
		user.setName(userDetails.getFirstname());
		user.setLast_name(userDetails.getLastname());
		user.getKlient().setMiejscowosc(userDetails.getLocation());
		user.getKlient().setWojewodztwo(provinceRepository.findByNazwa(userDetails.getProvince()).get());
		user.getKlient().setNrTelefonu(userDetails.getPhone());
		
		userRepository.save(user);
		
		return ResponseEntity.ok(new MessageResponse("Zmiany zapisane pomyślnie"));
	}
	
	@PostMapping(path = "/api/users/{userId}/password")
	public ResponseEntity<?> userDetailsPassword(@PathVariable("userId") Long userId , @RequestBody String request) throws Exception, ParseException {
		PasswordChangeRequest paaswordChange = new ObjectMapper().readValue(request, PasswordChangeRequest.class);
		Optional<User> client = userRepository.findById(userId);
		if(!client.isPresent())
			return (ResponseEntity<?>) ResponseEntity.of(client);
		User user = client.get();
		if(passwordEncoder.matches(paaswordChange.getOldPassword(), user.getPassword())) {
			user.setPassword(passwordEncoder.encode(paaswordChange.getNewPassword()));
			userRepository.save(user);
		}
		else {
			ResponseEntity.badRequest();
		}
		return ResponseEntity.ok(new MessageResponse("Hasło zostało zmienione pomyślnie"));
	}
	
	@PostMapping(path = "/api/users/{userId}/email")
	public ResponseEntity<?> userDetailsEmail(@PathVariable("userId") Long userId , @RequestBody String request) throws Exception, ParseException {
		Optional<User> client = userRepository.findById(userId);
		if(!client.isPresent())
			return (ResponseEntity<?>) ResponseEntity.of(client);
		User user = client.get();
		user.setEmail(user.getEmail());
		userRepository.save(user);
		return ResponseEntity.ok(new MessageResponse("Hasło zostało zmienione pomyślnie"));
	}
	
	@DeleteMapping(path = "/api/users/{userId}")
	public ResponseEntity<?> userDelete(@PathVariable("userId") Long userId) throws Exception, ParseException {
		Optional<User> client = userRepository.findById(userId);
		if(!client.isPresent())
			return (ResponseEntity<?>) ResponseEntity.of(client);
		User user = client.get();
		user.setActive((long) 0);
		userRepository.save(user);
		return ResponseEntity.ok(new MessageResponse("Konto zostało usunięte pomyślnie"));
	}
}
