package svc;

import java.sql.Connection;

import dao.BoardDAO;
import db.JdbcUtil;
import vo.BoardDTO;

// Action 클래스로부터 요청(지시)릉 받아 DAO 클래스와 상호작용을 통해
// 실제 비즈니스 로직(DB작업)을 지시하는 클래스
// => 주로, Connection 객체 관리, DAO 객체 관리, DAO 객체의 각 메서드 호출,
//    작업 수행 후 결과에 대한 판별을 통해 데이터베이스 트랜잭션 관리 등을 수행
public class BoardWriteProService {
	
	// 글쓰기 작업 요청을 위한 registBoard() 메서드 정의
	// => 파라미터 : BoardDTO 객체    리턴타입 : boolean(isWriteSuccess)
	public boolean registBoard(BoardDTO board) {
		// 1. 글쓰기 작업 요청 처리 결과를 판별하여 리턴하기 위한 boolean 타입 변수 선언
		boolean isWriteSuccess = false;
		
		// 2. JdbcUtil 클래스로부터 Connection Pool 에 저장된 Connection 객체 가져오기 - 공통
		// => Service 클래스에서 트랜잭션 관리를 위해 Connection 객체에 접근해야하기 때문에
		Connection con = JdbcUtil.getConnection();
		
		// 3. BoardDAO 클래스로부터 BoardDAO 인스턴스 가져오기 - 공통
		// => 싱글톤 디자인 패턴으로 생성된 BoaraDAO 인스턴스 가져오기
//		BoardDAO dao = new BoardDAO(); // 생성자가 private 이므로 접근 불가
		BoardDAO dao = BoardDAO.getInstance();
		
		// 4. BoardDAO 인스턴스에 Connection 객체 전달하기 - 공통
		dao.setConnection(con);

		// 5. BoardDAO 인스턴스의 XXX 메서드 호출하여 요청받은 XXX 작업 수행 및 결과 리턴받기
		// BoardDAO 객체의 insertBoard() 메서드를 호출하여 글쓰기 작업 수행 후 결과 리턴받기
		// => 파라미터 : BoardDAO 객체(board)    리턴타입 : int(insertCount)
		int insertCount = dao.insertBoard(board);
		
		
		// 6. 리턴받은 작업수행 결과를 통해 판별 후 트랜잭션 처리 작업 수행
		if(insertCount > 0) {
			// 트랜잭션 적용을 위해 JdbcUtil 클래스의 commit() 메서드를 호출하여 commit 작업 수행
			JdbcUtil.commit(con);
			// 작업 처리 결과를 성공으로 표시하기 위해 isWriteSuccess 를 ture 로 변경
			isWriteSuccess = true;
		} else {
			// 트랜잭션 취소를 위해 JdbcUtil 클래스의 rollback() 메서드를 호출하여 rollback 작업 수행
			JdbcUtil.rollback(con);
		}
		
		// 7. Connection 객체 반환 - 공통
		JdbcUtil.close(con);
		
		// 8. 작업 수행 결과 리턴
		return isWriteSuccess;
	}
	
}
