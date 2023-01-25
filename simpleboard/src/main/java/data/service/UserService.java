package data.service;


import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import data.dto.LoginDto;
import data.dto.TokenDto;
import data.dto.UserDto;
import data.exception.DuplicatedUsernameException;
import data.exception.LoginFailedException;
import data.jwt.TokenProvider;
import data.mapper.UserMapper;
import lombok.RequiredArgsConstructor;



@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
	 // 암호화 위한 엔코더
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final TokenProvider tokenProvider;

    // 회원가입 시 저장시간을 넣어줄 DateTime형
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
    Date time = new Date();
    String localTime = format.format(time);

    @Autowired
    UserMapper userMapper;


    /**
     * 유저 회원가입
     * @param userVo
     */
    @Transactional
    public boolean join(UserDto dto) {
        // 가입된 유저인지 확인
        if (userMapper.findUserId(dto.getUser_id()).isPresent()) {
            System.out.println("!!!");
            throw new DuplicatedUsernameException("이미 가입된 유저에요");
        }

        
        // 가입 안했으면 아래 진행
        UserDto userVo = UserDto.builder()
        .user_id(dto.getUser_id())
        .user_pass(passwordEncoder.encode(dto.getUser_pass()))
        .auth("ROLE_USER")
        .build();

        userMapper.insertUser(userVo);
        // userMapper.addRole(userVo);
        
        return userMapper.findUserId(userVo.getUser_id()).isPresent();
    }
    /**
     * 토큰 발급받는 메소드
     * @param loginDTO 로그인 하는 유저의 정보
     * @return result[0]: accessToken, result[1]: refreshToken
     */
    public String login (LoginDto loginDto) {

        UserDto dto = userMapper.findUser(loginDto.getUser_id())//indUserByUsername(loginDto.getUsername())
                .orElseThrow(() -> new LoginFailedException("잘못된 아이디입니다"));
        
        if (!passwordEncoder.matches(loginDto.getUser_pass(), dto.getPassword())) {
            throw new LoginFailedException("잘못된 비밀번호입니다");
        }

        return dto.getUser_id();
        // return loginDTO.getUserId();
        // // return tokenGenerator(userDto);
    }

    /**
     * 유저가 db에 있는지 확인하는 함수
     * @param userid 유저의 아이디 입력
     * @return 유저가 있다면: true, 유저가 없다면: false
     */
    public boolean haveUser(String user_id) {
        // IdDTO idDTO = IdDTO.builder().userId(userid).build();
        if (userMapper.findUserId(user_id).isPresent()) {
            return true;
        }else {
            return false;
        }
    }

    /**
     * 유저의 아이디를 찾는 함수
     * @param userId 유저의 아이디 입력
     * @return 유저의 아이디가 없다면 에러를 뱉고, 있다면 userId리턴
     */
    public UserDto findUserId(String user_id) {
        return userMapper.findUserId(user_id)
                .orElseThrow(() -> 
                    new DuplicatedUsernameException("중복된 유저!!!!"));
    }

    public TokenDto tokenGenerator(String user_id) {
        
        UserDto dto = userMapper.findUser(user_id)//indUserByUsername(loginDto.getUsername())
        .orElseThrow(() -> new LoginFailedException("잘못된 아이디입니다"));

        return TokenDto.builder()
        .accessToken("Bearer" + tokenProvider.createAcessToken(dto.getUser_id(), Collections.singletonList(dto.getAuth())))
        .refreshToken("Bearer" + tokenProvider.createRefreshToken(dto.getUser_id(), Collections.singletonList(dto.getAuth())))
        .build();
    }

    
}
