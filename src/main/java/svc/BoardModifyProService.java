package svc;

import java.sql.Connection;

import dao.BoardDAO;
import db.JdbcUtil;
import vo.BoardDTO;

public class BoardModifyProService {

	public boolean isBoardWriter(int board_num, String board_pass) {
		boolean isBoardWriter = false;
		
		Connection con = JdbcUtil.getConnection();
		
		BoardDAO dao = BoardDAO.getInstance();
		dao.setConnection(con);
		
		isBoardWriter = dao.isBoardWriter(board_num, board_pass);
		
		JdbcUtil.close(con);
		
		return isBoardWriter;
	}

	public boolean updateBoard(BoardDTO board) {
		boolean isModifySuccess = false;
		Connection con = JdbcUtil.getConnection();
		
		BoardDAO dao = BoardDAO.getInstance();
		dao.setConnection(con);
		
		int updateCount = dao.updateBoard(board);
		
		if(updateCount > 0) {
			JdbcUtil.commit(con);
			isModifySuccess = true;
		} else {
			JdbcUtil.rollback(con);
		}
		
		JdbcUtil.close(con);
		
		return isModifySuccess;
	}
	
}
