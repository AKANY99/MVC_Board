package svc;

import java.sql.Connection;
import java.util.ArrayList;

import dao.BoardDAO;
//import db.JdbcUtil;
import vo.BoardDTO;

// JdbcUtil 클래스의 static 메서드를 클래스명 없이 메서드명만으로 접근하기 위해서는
// static import 기능을 활용하여 JdbcUtil 클래스의 static 메서드를 포함시켜야 한다.
// => 기본 문법 : import static 패키지명.클래스명.메서드명;
// 1) 특정 메서드만 포항시킬 경우 : import static 패키지명.클래스명.메서드명;
//import static db.JdbcUtil.getConnection;
//import static db.JdbcUtil.close;
// 2) 클래스 내의 모든 메서드를 포함시킬 경우 : import static 패키지명.클래스명.*;
import static db.JdbcUtil.*;

public class BoardListService {
	
	// 전체 게시물 갯수 조회 작업을 요청할 getListCount() 메서드 정의
	public int getListCount() {
		System.out.println("BoardListService - getListCount");
		// 1. 리턴할 데이터를 저장할 변수 선언 - 공통
		int listCount = 0;
		
		// 2. Connection 객체 가져오기 - 공통
//		Connection con = JdbcUtil.getConnection();
		// static import 로 포함시킨 getConnection() 메서드 호출
		Connection con = getConnection();
		
		// 3. BoardDAO 객체 가져오기 - 공통
		BoardDAO dao = BoardDAO.getInstance();
		
		// 4. BoardDAO 객체에 Connection 객체 전달하기 - 공통
		dao.setConnection(con);
		
		// 5. BoardDAO 객체의 selectListCount() 메서드 호출하여 전체 게시물 수 조회
		listCount = dao.selectListCount();
		
		// 6. Connection 객체 반환 - 공통
//		JdbcUtil.close(con);
		// static import 로 포함시킨 close() 메서드 호출
		close(con);
		
		// 7. 조회결과 리턴
		return listCount;
	}

	// 전체 게시물 목록 조회 작업을 요청할 getBoardList() 메서드 정의
	// => 파라미터 : 현재페이지번호(pageNum), 페이지 당 게시물 수(listLimit)
			// => 리턴타입 : ArrayList<BoardDTO> 
	public ArrayList<BoardDTO> getBoardList(int pageNum, int listLimit) {
		System.out.println("BoardListService - getBoardList");
		ArrayList<BoardDTO> articleList = null;
		Connection con = getConnection();
		BoardDAO dao = BoardDAO.getInstance();
		dao.setConnection(con);
		
		articleList = dao.selectBoardList(pageNum, listLimit);
		
		
		close(con);
		
		return articleList;
	}
}
