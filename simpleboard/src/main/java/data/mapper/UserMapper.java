package data.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import data.dto.UserDto;

@Mapper
public interface UserMapper {
	
	//test용 유저정보 받아오기
	public UserDto getTestUser();
	//이메일을 통한 유저 정보
	public UserDto getUserInfo(String user_id);
    public UserDto getUserByNum(int user_num);
    public void insertUser(UserDto dto);
   
    //로그인
    public UserDto login(UserDto dto);
    // 아이디 중복 체크
    public int idCheck(String user_id);
    // 이전 비밀번호 일치 체크
    public int passCheck(UserDto dto);
    
    
    //아이디와 유저 찾기
    Optional<UserDto> findUser(String user_id);
    Optional<UserDto> findUserId(String user_id);
    
    
    // 비밀번호 수정
    public void passChange(UserDto dto);

    //회원 탈퇴
    public void deleteUser(int u_num);


}