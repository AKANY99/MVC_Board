package svc;

import java.sql.Connection;

import dao.BoardDAO;
import db.JdbcUtil;
import vo.BoardDTO;

public class BoardReplyService {

	public boolean replyBoard(BoardDTO board) {
		boolean isReplySuccess = false;
		Connection con = JdbcUtil.getConnection();
		BoardDAO dao = BoardDAO.getInstance();
		dao.setConnection(con);
		
		int insertCount = dao.insertReplyBoard(board);
		
		if(insertCount > 0) {
			JdbcUtil.commit(con);
			isReplySuccess = true;
		} else {
			JdbcUtil.rollback(con);
		}
		
		JdbcUtil.close(con);
		
		return isReplySuccess;
	}

}
