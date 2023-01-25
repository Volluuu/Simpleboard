package data.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class BoardDto {
	private int board_num;
	private String board_title;
	private String board_content;
	private int user_num;
	private int reboard_num;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private Timestamp board_writeday;
	
	//유저 정보
	private String user_name;
	private String user_pass;
	private String user_email;
	private String user_id;
}
