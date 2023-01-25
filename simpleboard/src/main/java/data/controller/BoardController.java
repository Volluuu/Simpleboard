package data.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import data.dto.BoardDto;
import data.dto.UserDto;
import data.mapper.BoardMapper;
import data.mapper.UserMapper;

@RestController
@CrossOrigin (origins = "http://localhost:9003")
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	BoardMapper boardMapper;
	@Autowired
	UserMapper userMapper;
	
	 @GetMapping("/admin")
	 public UserDto userInfo() {
		 return userMapper.getTestUser();
	 }
	
	@GetMapping("/alllist")
	public Map<String, Object> getBoardList(@RequestParam(defaultValue = "1") int currentPage) {
//      System.out.println("ProCP:"+currentPage);

      //페이징처리
      int totalCount = boardMapper.getBoardTortal();
      System.out.println("tcnt:"+totalCount);
      
      int perPage = 5;
      int perBlock = 5;
      int startNum;
      int startPage;
      int endPage;
      int totalPage;
      int no;

      
      totalPage = totalCount / perPage + (totalCount % perPage == 0 ? 0 : 1);

      startPage = (currentPage - 1) / perBlock * perBlock + 1;
      endPage = startPage + perBlock - 1;
     
      if (endPage > totalPage)
          endPage = totalPage;

      
      startNum = (currentPage - 1) * perPage;

      //각페이지당 출력할 시작번호 구하기
      no = totalCount - (currentPage - 1) * perPage;

      System.out.println("page:"+perPage);
      System.out.println("sNum:"+startNum);
      //페이지에서 보여질 글만 가져오기
      Map<String, Object> map = new HashMap<>();
      map.put("startNum", startNum);
      map.put("perPage", perPage);

      List<BoardDto> list = boardMapper.boardByUser(map);
      
      Vector<Integer> barr = new Vector<>();
      for (int i = startPage; i <= endPage; i++) {
          barr.add(i);
      }

      //리액트로 필요한 변수들을 Map에 담아서 보낸가
      Map<String, Object> smap = new HashMap<>();
      smap.put("list", list);
      smap.put("totalCount", totalCount);
      smap.put("barr", barr);
      smap.put("startPage", startPage);
      smap.put("endPage", endPage);
      smap.put("no", no);
      smap.put("totalPage", totalPage); //다음페이지 생성 여부 확인

      return smap;
  }

	@PostMapping("/insertboard")
	public void insertBoard(@RequestBody BoardDto dto) {
		boardMapper.insertBoard(dto);
	}
	@DeleteMapping("/deleteboard")
	public void deleteBoard(@RequestParam int board_num) {
		boardMapper.deleteBoard(board_num);
	}
	
	@GetMapping("/detail")
	public BoardDto detailBoard(@RequestParam int board_num) {
		return boardMapper.getBoardData(board_num);
	}
	
	@PostMapping("/updateboard")
	public void updateBoard(@RequestBody BoardDto dto) {
		boardMapper.updateBoard(dto);
	}
 

}
