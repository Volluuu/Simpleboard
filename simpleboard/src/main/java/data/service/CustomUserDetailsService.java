package data.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import data.dto.UserDto;
import data.mapper.UserMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // final이나 @NonNull인 필드 값만 파라미터로 받는 생성자 생성
@Service
public class CustomUserDetailsService implements UserDetailsService{

	 @Autowired
	    UserMapper userMapper;

	 //Mapper 수정 필요
	    @Override
	    public UserDetails loadUserByUsername(String user_id) throws UsernameNotFoundException {
	        return userMapper.findUser(user_id)
	                .map(user -> addAuthorities(user))
	                .orElseThrow(() -> new UsernameNotFoundException(user_id + ">ID를 찾을 수 없습니다."));
	    }

	    //Dto를 통해 권한 확인
	    private UserDto addAuthorities(UserDto dto) {
	        dto.setAuthorities(Arrays.asList(new SimpleGrantedAuthority(dto.getAuth())));

	        return dto;
	    }
}
