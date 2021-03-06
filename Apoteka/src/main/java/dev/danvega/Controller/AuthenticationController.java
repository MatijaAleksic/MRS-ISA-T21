package dev.danvega.Controller;

import dev.danvega.DTO.FirstLoginChangePasswordDTO;
import dev.danvega.DTO.LoginResponseDTO;
import dev.danvega.DTO.RegisterPatientDTO;
import dev.danvega.Model.*;
import dev.danvega.Services.*;
import dev.danvega.exception.ResourceConflictException;
import dev.danvega.security.TokenUtils;
import dev.danvega.security.auth.JwtAuthenticationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
/*
import rs.ac.uns.ftn.informatika.spring.security.exception.ResourceConflictException;
import rs.ac.uns.ftn.informatika.spring.security.model.User;
import rs.ac.uns.ftn.informatika.spring.security.model.UserRequest;
import rs.ac.uns.ftn.informatika.spring.security.model.UserTokenState;
import rs.ac.uns.ftn.informatika.spring.security.security.TokenUtils;
import rs.ac.uns.ftn.informatika.spring.security.security.auth.JwtAuthenticationRequest;
import rs.ac.uns.ftn.informatika.spring.security.service.UserService;
import rs.ac.uns.ftn.informatika.spring.security.service.impl.CustomUserDetailsService;*/

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

//Kontroler zaduzen za autentifikaciju korisnika
@RestController
@RequestMapping(value = "api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private PatientService patientService;

	@Autowired
	private AdministratorService administratorService;

	@Autowired
	private DermathologistService dermathologistService;

	@Autowired
	private PharmacistService pharmacistService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// Prvi endpoint koji pogadja korisnik kada se loguje.
	// Tada zna samo svoje korisnicko ime i lozinku i to prosledjuje na backend.
	@PostMapping("/login")
	public ResponseEntity<UserTokenState> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest,
																	HttpServletResponse response) throws Exception {
		Authentication authentication;
		String jwt = "";
		int expiresIn = tokenUtils.getExpiredIn();
		if(!authenticationRequest.getPassword().equals("")) {
			authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
							authenticationRequest.getPassword()));
			// Ubaci korisnika u trenutni security kontekst
			SecurityContextHolder.getContext().setAuthentication(authentication);

			// Kreiraj token za tog korisnika
			User user = (User) authentication.getPrincipal();
			jwt = tokenUtils.generateToken(user.getUsername());
		}


		User found_user = null;
		try {
			found_user = userService.findByUsername(authenticationRequest.getUsername());
		}catch(Exception e){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		String ret = null;
		if(found_user.getClass() == Patient.class){
			ret = "patient";
		}else if(found_user.getClass() == Administrator.class){
			ret = "administrator";
		}if(found_user.getClass() == Pharmacist.class){
			ret = "pharmacist";
		}if(found_user.getClass() == Dermatologist.class){
			ret = "dermatologist";
		}

		LoginResponseDTO respo = new LoginResponseDTO(ret,Boolean.toString(found_user.getFirstTimeLogin()),found_user.getId());
		UserTokenState token = new UserTokenState(jwt, expiresIn, respo);

		// Vrati token kao odgovor na uspesnu autentifikaciju

		return ResponseEntity.ok(token);
	}

	// Endpoint za registraciju novog korisnika
	@PostMapping("/signup")
	public ResponseEntity<String> addUser(@RequestBody UserRequest userRequest, UriComponentsBuilder ucBuilder) throws Exception {
		User existUser = this.userService.findByUsername(userRequest.getUsername());
		if (existUser != null) {
			throw new ResourceConflictException(existUser.getId(), "Username already exists");
		}

		User user = this.userService.saveUsers(userRequest);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/user/{userId}").buildAndExpand(user.getId()).toUri());
		return new ResponseEntity<>("Uspesna registracija", HttpStatus.CREATED);
	}

	@PostMapping(path = "/first-login")
	public ResponseEntity<String> firstLogin(@RequestBody FirstLoginChangePasswordDTO firstLoginChangePasswordDTO) throws Exception {
		try {
			User user;
			user = patientService.findOne(firstLoginChangePasswordDTO.getUserId());
			if(user == null)
			{
				user = administratorService.findOne(firstLoginChangePasswordDTO.getUserId());
			}
			if(user == null)
			{
				user = dermathologistService.findOne(firstLoginChangePasswordDTO.getUserId());
			}
			if(user == null)
			{
				user = pharmacistService.findOne(firstLoginChangePasswordDTO.getUserId());
			}


			if(user != null) {
				if (user.getClass() == Patient.class) {
					patientService.first_login_update_password(passwordEncoder.encode(firstLoginChangePasswordDTO.getNewPassword()), firstLoginChangePasswordDTO.getUserId());
				} else if (user.getClass() == Administrator.class) {
					administratorService.first_login_update_password(passwordEncoder.encode(firstLoginChangePasswordDTO.getNewPassword()), firstLoginChangePasswordDTO.getUserId());
				}
				if (user.getClass() == Pharmacist.class) {
					pharmacistService.first_login_update_password(passwordEncoder.encode(firstLoginChangePasswordDTO.getNewPassword()), firstLoginChangePasswordDTO.getUserId());
				}
				if (user.getClass() == Dermatologist.class) {
					dermathologistService.first_login_update_password(passwordEncoder.encode(firstLoginChangePasswordDTO.getNewPassword()), firstLoginChangePasswordDTO.getUserId());
				}
			}
			else
			{
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}catch(Exception e){
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>("Uspesna izmenjena sifra!", HttpStatus.OK);
	}

	// U slucaju isteka vazenja JWT tokena, endpoint koji se poziva da se token osvezi
	@PostMapping(value = "/refresh")
	public ResponseEntity<UserTokenState> refreshAuthenticationToken(HttpServletRequest request) {

		String token = tokenUtils.getToken(request);
		String username = this.tokenUtils.getUsernameFromToken(token);
		User user = (User) this.userDetailsService.loadUserByUsername(username);

		if (this.tokenUtils.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
			String refreshedToken = tokenUtils.refreshToken(token);
			int expiresIn = tokenUtils.getExpiredIn();

			return ResponseEntity.ok(new UserTokenState(refreshedToken, expiresIn));
		} else {
			UserTokenState userTokenState = new UserTokenState();
			return ResponseEntity.badRequest().body(userTokenState);
		}
	}

	@RequestMapping(value = "/change-password", method = RequestMethod.POST)
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> changePassword(@RequestBody PasswordChanger passwordChanger) {
		userDetailsService.changePassword(passwordChanger.oldPassword, passwordChanger.newPassword);

		Map<String, String> result = new HashMap<>();
		result.put("result", "success");
		return ResponseEntity.accepted().body(result);
	}

	static class PasswordChanger {
		public String oldPassword;
		public String newPassword;
	}
}