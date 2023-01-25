package data.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import data.dto.LoginDto;

import data.dto.TokenDto;
import data.dto.UserDto;
import data.exception.DuplicatedUsernameException;
import data.exception.LoginFailedException;
import data.exception.UserNotFoundException;
import data.jwt.TokenProvider;
import data.mapper.UserMapper;
import data.response.BaseResponse;
import data.response.SingleDataResponse;
import data.service.ResponseService;
import data.service.UserService;


@RestController
@CrossOrigin (origins = "http://localhost:9003")
@RequestMapping("/user")

public class UserController {
	
	@Autowired
	UserMapper userMapper;
	
	@Autowired
	UserService userService;
	
	@Autowired
    PasswordEncoder passwordEncoder;
	
	@Autowired
	ResponseService responseService;
	
	@Autowired
	TokenProvider tokenProvider;
	

	@PostMapping(value = "/join")
	public ResponseEntity<?> join(@RequestBody UserDto dto){
		ResponseEntity responseEntity = null;
		
		 try {
			 if (userMapper.getUserInfo(dto.getUser_id()) != null) {
		            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
		        } else {
		        //비밀번호 암호화
		        dto.setUser_pass(passwordEncoder.encode(dto.getUser_pass()));
		        //DB 저장
	            userMapper.insertUser(dto);
	            //토큰 생성
	            TokenDto token = userService.tokenGenerator(dto.getUser_id());
	            
	            ResponseCookie responseCookie = 
	                ResponseCookie.from(HttpHeaders.SET_COOKIE, token.getRefreshToken())///new Cookie("refreshToken", token.getRefreshToken());
	                .path("/")
	                .maxAge(14 * 24 * 60 * 60) // 14일
	                .httpOnly(true)
	                // .httpOnly(true).secure(true)
	                .build();
	            System.out.println(responseCookie.toString());

	            SingleDataResponse<String> response = responseService.getSingleDataResponse(true, dto.getUser_id(), token.getAccessToken());
	            responseEntity = ResponseEntity.status(HttpStatus.OK)
	                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
	                .body(response);
		        }

	        }catch(DuplicatedUsernameException exception) {
	            BaseResponse response = responseService.getBaseResponse(false, exception.getMessage());
	            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

	        }
	        return responseEntity;
	}
	
	 @PostMapping(value="/login")
	    public ResponseEntity login(@RequestBody LoginDto loginDto, HttpServletResponse response) {
	        ResponseEntity responseEntity = null;
	        try {
	            String user_id = userService.login(loginDto);
	            System.out.println("ID:"+user_id.toString());
	            TokenDto token = userService.tokenGenerator(user_id);
	            System.out.println("token:"+token.toString());
	            ResponseCookie responseCookie = 
	                ResponseCookie.from(HttpHeaders.SET_COOKIE, token.getRefreshToken())///new Cookie("refreshToken", token.getRefreshToken());
	                .path("/")
	                .maxAge(14 * 24 * 60 * 60) // 14일
	                .httpOnly(true)
	                // .secure(true)
	                .build();
	            System.out.println(responseCookie.toString());
	            SingleDataResponse<String> Sresponse = responseService.getSingleDataResponse(true, user_id, token.getAccessToken());
	            
	            System.out.println("respone:"+response.toString());
	            responseEntity = ResponseEntity.status(HttpStatus.OK)
	                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
	                .body(response);
	    
	        } catch (LoginFailedException exception) {
//	            log.debug(exception.getMessage());
	            BaseResponse Bresponse = responseService.getBaseResponse(false, exception.getMessage());

	            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	        }

	        return responseEntity;
	    }
	 
	 @PostMapping(value="/logout")
	    public ResponseEntity logout(        
	        @CookieValue(value = HttpHeaders.SET_COOKIE) Cookie refreshCookie
	    ) {
	        ResponseEntity responseEntity = null;
	        try {
	            ResponseCookie responseCookie = 
	                ResponseCookie.from(HttpHeaders.SET_COOKIE, "")///new Cookie("refreshToken", token.getRefreshToken());
	                .path("/")
	                .httpOnly(true)
	                .secure(true)
	                .maxAge(0).build();
	            BaseResponse response = 
	                responseService.getBaseResponse(true, "로그아웃 성공");
	            responseEntity = ResponseEntity.status(HttpStatus.OK)
	                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
	                .body(response);
	    
	        } catch (LoginFailedException exception) {
//	            log.debug(exception.getMessage());
	            BaseResponse response = responseService.getBaseResponse(false, exception.getMessage());

	            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	        }

	        return responseEntity;
	    }
	    
	 @GetMapping(value="/get")
	    public ResponseEntity isHaveUser(@RequestParam String userId) {
	        ResponseEntity responseEntity = null;
	        // Cookie cookie = new Cookie("name", value)
	        try {
	            boolean isHaveUser = userService.haveUser(userId);
	            String message = isHaveUser ? "회원가입된 유저입니다." : "회원가입 안된 유저입니다.";
	            SingleDataResponse<Boolean> response = responseService.getSingleDataResponse(true, message, isHaveUser);
	            responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(response);


	        }catch(UserNotFoundException exception) {
//	            log.debug(exception.getMessage());
	            BaseResponse response = responseService.getBaseResponse(false, exception.getMessage());
	            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	        }
	        return responseEntity;
	    }
	
//		private BCryptPasswordEncoder bcryptPasswordEncoder;
		
//	 @PostMapping("/signup")
//	    public void signup(@RequestBody UserDto dto) {
//	        if (userMapper.getUserInfo(dto.getUser_id()) != null) {
//	            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
//	        } else {
//	        	System.out.println("Before:"+dto.getUser_pass());
//	        	String endcodedPassword = bcryptPasswordEncoder.encode(dto.getUser_pass());
//	        	System.out.println("After"+endcodedPassword);
//	        	System.out.println("regist"+dto);
//	        	
//	        	dto.setUser_pass(endcodedPassword);
//	        	
//	            userMapper.insertUser(dto);
//	        }
//	    }	 
//	 @GetMapping("/idcheck")
//	    public int idCheck(@RequestParam String user_id) {
//	        return userMapper.idCheck(user_id);
//	    }
	 
//	 @PostMapping("/login")
//	 public String login(@RequestBody UserDto dto, HttpServletRequest request) {
//		return userMapper.login(dto);
//	 }
	 // 로그아웃
//	 @PostMapping("/logout")
//	 public String logout(HttpSession session) {
//	 	session.invalidate();
//	 	return "redirect:/login";
//	 }
}
