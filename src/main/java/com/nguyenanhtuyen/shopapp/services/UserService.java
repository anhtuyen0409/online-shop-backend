package com.nguyenanhtuyen.shopapp.services;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nguyenanhtuyen.shopapp.components.JwtTokenUtil;
import com.nguyenanhtuyen.shopapp.dtos.UserDTO;
import com.nguyenanhtuyen.shopapp.exceptions.DataNotFoundException;
import com.nguyenanhtuyen.shopapp.models.Role;
import com.nguyenanhtuyen.shopapp.models.User;
import com.nguyenanhtuyen.shopapp.repositories.RoleRepository;
import com.nguyenanhtuyen.shopapp.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
	
	private final UserRepository userRepository;
	
	private final RoleRepository roleRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	private final JwtTokenUtil jwtTokenUtil;
	
	private final AuthenticationManager authenticationManager;

	@Override
	public User createUser(UserDTO userDTO) throws DataNotFoundException {
		
		String phoneNumber = userDTO.getPhoneNumber();
		
		//kiểm tra sdt đã tồn tại chưa?
		if(userRepository.existsByPhoneNumber(phoneNumber)) {
			throw new DataIntegrityViolationException("Phone number already exists");
		}
		
		//convert userDTO => user
		User newUser = User.builder()
				.fullName(userDTO.getFullName())
				.phoneNumber(userDTO.getPhoneNumber())
				.password(userDTO.getPassword())
				.address(userDTO.getAddress())
				.dateOfBirth(userDTO.getDateOfBirth())
				.facebookAccountId(userDTO.getFacebookAccountId())
				.googleAccountId(userDTO.getGoogleAccountId())
				.build();
		Role role = roleRepository.findById(userDTO.getRoleId())
				.orElseThrow(() -> new DataNotFoundException("Role not found"));
		newUser.setActive(true);
		newUser.setRole(role);
		
		//kiểm tra nếu không có accountId (facebook, google) thì yêu cầu mật khẩu
		if(userDTO.getGoogleAccountId() == 0 && userDTO.getFacebookAccountId() == 0) {
			String password = userDTO.getPassword();
			String encodedPassword = passwordEncoder.encode(password); // mã hoá password
			newUser.setPassword(encodedPassword);
		}
		
		return userRepository.save(newUser);
		
	}

	@Override
	public String login(String phoneNumber, String password) throws Exception {
		
		Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
		if(optionalUser.isEmpty()) {
			throw new DataNotFoundException("Invalid phone number or password");
		}
		
		User existingUser = optionalUser.get();
		
		// check password
		if(existingUser.getFacebookAccountId() == 0 && existingUser.getGoogleAccountId() == 0) {
			if(!passwordEncoder.matches(password, existingUser.getPassword())) {
				throw new BadCredentialsException("Wrong phone number or password");
			}
		}
		
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				phoneNumber, password, existingUser.getAuthorities());
		// authenticate with spring security
		authenticationManager.authenticate(authenticationToken);
		
		return jwtTokenUtil.generateToken(existingUser);
	}

}
