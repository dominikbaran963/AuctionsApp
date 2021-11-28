package pl.auctions.auctions_app.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import pl.auctions.auctions_app.entity.ERole;
import pl.auctions.auctions_app.entity.Client;
import pl.auctions.auctions_app.entity.Province;
import pl.auctions.auctions_app.entity.Role;
import pl.auctions.auctions_app.entity.User;
import pl.auctions.auctions_app.payload.request.LoginRequest;
import pl.auctions.auctions_app.payload.request.RegisterRequest;
import pl.auctions.auctions_app.payload.response.JwtResponse;
import pl.auctions.auctions_app.payload.response.MessageResponse;
import pl.auctions.auctions_app.repository.ClientRepository;
import pl.auctions.auctions_app.repository.ProvinceRepository;
import pl.auctions.auctions_app.repository.RoleRepository;
import pl.auctions.auctions_app.repository.UserRepository;
import pl.auctions.auctions_app.security.jwt.JwtUtils;
import pl.auctions.auctions_app.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

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

	@Autowired
	JwtUtils jwtUtils;
	
    @PersistenceContext
    private EntityManager em;
	
	@PostMapping("/api/auth/signin")
	public ResponseEntity<?> authenticateUser(@RequestBody String loginRequest) throws Exception, ParseException {
		
		LoginRequest loginDetails = new ObjectMapper().readValue(loginRequest, LoginRequest.class);
		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginDetails.getEmail(), loginDetails.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		
		if(!(userDetails.getActive() == 1)){
	        return new ResponseEntity<>(
	                "Konto jest nieaktywne:", 
	                HttpStatus.BAD_REQUEST);
		}
		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(new JwtResponse(jwt, 
												 userDetails.getId(), 
												 userDetails.getEmail(), 
												 roles));
	}
	
	@PostMapping("/api/auth/register")
	public ResponseEntity<?> signupUser(@RequestBody String request) throws Exception, ParseException {
		
		RegisterRequest signUpRequest = new ObjectMapper().readValue(request, RegisterRequest.class);
		
		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: email jest już w użyciu!"));
		}

		// Create new user's account
		User user = new User( signUpRequest.getEmail(),
							 encoder.encode(signUpRequest.getPassword()));
		user.setName(signUpRequest.getFirstname());
		user.setLast_name(signUpRequest.getLastname());
		user.setActive((long) 1);
		
		Set<Role> roles = new HashSet<>();
		

		Role userRole = roleRepository.findByRole(ERole.ROLE_USER)
				.orElseThrow(() -> new RuntimeException("Error: Rola nie została znaleziona"));
		roles.add(userRole);

		user.setRoles(roles);
		
		userRepository.save(user);
		
		Client client = new Client();
		client.setCzyAktywny("1");
		client.setIdKlienta(user.getUser_id());
		Optional<Province> province = provinceRepository.findByNazwa(signUpRequest.getProvince());
		client.setIdWojewodztwa(province.get().getIdWojewodztwa());
		client.setMiejscowosc(signUpRequest.getLocation());
		client.setNrTelefonu(signUpRequest.getPhone());
		
		clientRepository.save(client);
		
		return ResponseEntity.ok(new MessageResponse("Użytkownik został zarejestrowany pomyślnie!"));
	}
}
