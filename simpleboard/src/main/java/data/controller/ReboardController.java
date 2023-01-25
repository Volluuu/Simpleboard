package data.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import data.dto.ReboardDto;
import data.mapper.ReboardMapper;

@RestController
@CrossOrigin (origins = "http://localhost:9003")
@RequestMapping("/reboard")
public class ReboardController {
	@Autowired
	ReboardMapper reboardMapper;
	
	@PostMapping("/insertReboard")
	public void insertReboard(@RequestBody ReboardDto dto) {
		reboardMapper.insertReboard(dto);
	}
	
	@GetMapping("/reboardList")
	public List<ReboardDto> getReboardList(@RequestParam int user_num, @RequestParam int board_num){
		
		return reboardMapper.reboardByUser(board_num,user_num);
	}
	
	@DeleteMapping("/deleteReboard")
	public void deleteReboard(@RequestParam int reboard_num,int user_num) {
		reboardMapper.deleteReboard(reboard_num,user_num);
	}
	

}
