package svc;

import java.sql.Connection;

import dao.BoardDAO;
import db.JdbcUtil;

public class BoardDeleteProService {

	public boolean isBoardWriter(int board_num, String board_pass) {
		boolean isBoardWriter = false;
		
		Connection con = JdbcUtil.getConnection();
		
		BoardDAO dao = BoardDAO.getInstance();
		dao.setConnection(con);
		
		isBoardWriter = dao.isBoardWriter(board_num, board_pass);
		
		JdbcUtil.close(con);
		
		return isBoardWriter;
	}

	public boolean removeBoard(int board_num) {
		boolean isDeleteSuccess = false;
		
		Connection con = JdbcUtil.getConnection();
		
		BoardDAO dao = BoardDAO.getInstance();
		dao.setConnection(con);
		
		int deleteCount = dao.removeBoard(board_num);
		
		if(deleteCount > 0) {
			isDeleteSuccess = true;
			JdbcUtil.commit(con);			
		} else {
			JdbcUtil.rollback(con);
		}
		
		JdbcUtil.close(con);
		
		return isDeleteSuccess;
	}
	
}
