package data.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import data.dto.ReboardDto;

@Mapper
public interface ReboardMapper {
	public int getReboardCount(int board_num);
	public List<ReboardDto> reboardByUser(int board_num, int user_num);
	//글 작성
	public void insertReboard(ReboardDto dto);
	//수정
	public void updateReboard(ReboardDto dto);
	//삭제
	public void deleteReboard(int reboard_num,int user_num);
}
