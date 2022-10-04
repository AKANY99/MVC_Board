package svc;

import vo.BoardDTO;
import static db.JdbcUtil.*;

import java.sql.Connection;

import dao.BoardDAO;

public class BoardDetailService {

	public void increaseReadcount(int board_num) {
		Connection con = getConnection();
		BoardDAO dao = BoardDAO.getInstance();
		dao.setConnection(con);
		
		dao.updateReadcount(board_num);
		
		commit(con);
		
		close(con);
	}

	public BoardDTO getBoard(int board_num) {
		BoardDTO board = null;
		Connection con = getConnection();
		BoardDAO dao = BoardDAO.getInstance();
		dao.setConnection(con);
		
		board = dao.selectBoard(board_num);
		
		close(con);
		return board;
	}
	
	
}
