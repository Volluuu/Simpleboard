package data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor //모든 필드값을 파라미터로 받는 생성자 생성
@NoArgsConstructor //파라미터가 없는 기본 생성자 생성
public class LoginDto {

	private String user_id;
	private String user_pass;
}
