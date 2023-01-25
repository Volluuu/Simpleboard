package data.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import data.dto.BoardDto;

@Mapper
public interface BoardMapper {
	//총 수량
	public int getBoardTortal();
	//작상자가 작성한 수량
	public int getBoardUser(int user_num);
	
	public BoardDto getBoardData(int board_num);
	
	public List<BoardDto> boardByUser(Map<String, Object> map);
	//글 작성
	public void insertBoard(BoardDto dto);
	//수정
	public void updateBoard(BoardDto dto);
	//삭제
	public void deleteBoard(int board_num);
}

