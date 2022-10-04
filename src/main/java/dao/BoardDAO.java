package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static db.JdbcUtil.*;
import vo.BoardDTO;

// 실제 비즈니스 로직(DB작업)을 처리할 BoardDAO 클래스 정의
// => Board 클래스 인스턴스를 여러개 생성하여 서로 다른 값을 저장할 필요가 없으므로
//    싱글톤 디자인 패턴(Singleton Design Pattern)을 통해
//    하나의 인스턴스를 생성한 후 모두가 공유하도록 정의할 수 있다!
public class BoardDAO {
	// ---------------- 싱글톤 디자인 패턴을 활용한 BoardDAO 인스턴스 생성 작업 --------------
	// 1. 외부에서 인스턴스 생성이 불가능하도록 생성자 정의 시 private 접근제한자 적용
//	private BoardDAO() {}
	
	// 2. 자신의 클래스 내에서 직접 인스턴스 생성하여 변수(instance)에 저장
	// => 외부에서 멤버변에 접근이 불가능하도록 private 접근제한자 적용
	// => 클래스 로딩 시 getInstance
//	private static BoardDAO instance = new BoardDAO();

	// 3. 생성된 인스턴스를 외부로 리턴하기 위한 Getter 정의
	// => 외부에서 인스턴스 생성 없이도 호출이 가능하도록 static 메서드로 정의
	// => 이 때, 2번에서 선언된 멤버변수(instance)도 static 변수로 선언되어야 함
	//    (static 메서드 내에서 접근하는 변수도 static 변수여야하기 때문에)
//	public static BoardDAO getInstance() {
//		return instance;
//	}
	// ---------------------------------------------------------------------------------
	// 1. 멤버변수 선언 및 인스턴스 생성
	private static BoardDAO instance = new BoardDAO();
	// 2. 생성자 정의
	private BoardDAO() {}
	// 3. Getter 정의
	public static BoardDAO getInstance() {
		return instance;
	}
	// ---------------------------------------------------------------------------------
	// 외부 (Service 클래스)로부터 Connection 객체를 전달받아 관리하기 위해
	// Connection 타입 멤버변수와 Setter 메서드 정의
	private Connection con;
	public void setConnection(Connection con) {
		this.con = con;
	}
	// ---------------------------------------------------------------------------------
	// 글 쓰기 작업을 수행할 insertBoard() 메서드 정의
	// => 파라미터 : BoardDAO 객체(board)    리턴타입 : int(insertCount)
	public int insertBoard(BoardDTO board) {
		System.out.println("BoardDAO - insertBoard()");
		
		// INSERT 작업 결과를 리턴받아 저장할 변수 선언
		int insertCount = 0;
		
		// FileBoardDAO 클래스의 메서드를 참고하여 글쓰기  작업 수행
		// 단, board_re_ref는 새 글 번호와 동일한 번호를 사용하고
		// board_re_lev 과 board_re_seq 는 0으로 설정
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			// 새 글 번호를 저장할 변수
			int num = 1;
			// 새 글 번호로 사용될 번호를 생성하기 위해 기존 게시물의 가장 큰 번호 조회
			// => 조회결과가 있을 경울 해당 번호 +1 값을 새 글 번호로 저장
			String sql = "SELECT MAX(board_num) FROM board";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				num = rs.getInt(1) + 1;
			}
			// 사용 완료된 PreparedStatement 객체를 먼저 반환
			close(pstmt);			
			
			sql = "INSERT INTO board VALUES (?,?,?,?,?,?,?,?,?,?,?,now())";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.setString(2, board.getBoard_name());
			pstmt.setString(3, board.getBoard_pass());
			pstmt.setString(4, board.getBoard_subject());
			pstmt.setString(5, board.getBoard_content());
			pstmt.setString(6, board.getBoard_file());
			pstmt.setString(7, board.getBoard_real_file());
			pstmt.setInt(8, num); // board_re_ref
			pstmt.setInt(9, 0); // board_re_lev
			pstmt.setInt(10, 0); // board_re_seq
			pstmt.setInt(11, 0); // board_readcount
			insertCount = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("insertBoard - SQL 구문 오류 : " + e.getMessage());
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		return insertCount;
	} 
	
	
	// 전체 게시물 수를 조회할 selectListCount() 메서드 정의
	// 파라미터 : 없음    리턴타입 : int(ListCount)
	public int selectListCount() {
		int ListCount = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT COUNT(*) FROM board";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				ListCount = rs.getInt(1);
			}
		} catch (SQLException e) {
			System.out.println("selectListcount - SQL 구문 오류 : " + e.getMessage());
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		return ListCount;
	}
	
	public ArrayList<BoardDTO> selectBoardList(int pageNum, int listLimit) {
		ArrayList<BoardDTO> boardList = null;
		BoardDTO board = null;
		// 시작 행 번호 계산
		int startRow = (pageNum-1)*listLimit;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
//			String sql = "SELECT * FROM board "
//					+ "ORDER BY board_num DESC "
//					+ "LIMIT ?,?";
			String sql = "SELECT * FROM board "
					+ "ORDER BY board_re_ref DESC, board_re_seq ASC "
					+ "LIMIT ?,?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, listLimit);
			rs = pstmt.executeQuery();
			
			boardList = new ArrayList<BoardDTO>();
			while(rs.next()) {
				board = new BoardDTO();
				board.setBoard_num(rs.getInt(1));
				board.setBoard_name(rs.getString(2));
				board.setBoard_pass(rs.getString(3));
				board.setBoard_subject(rs.getString(4));
				board.setBoard_content(rs.getString(5));
				board.setBoard_file(rs.getString(6));
				board.setBoard_real_file(rs.getString(7));
				board.setBoard_re_ref(rs.getInt(8));
				board.setBoard_re_lev(rs.getInt(9));
				board.setBoard_re_seq(rs.getInt(10));
				board.setBoard_readcount(rs.getInt(11));
				board.setBoard_date(rs.getDate(12));
				
				boardList.add(board);
			}
			// 확인
//			System.out.println(boardList.get(0));
//			System.out.println(boardList.get(1));
			
		} catch (SQLException e) {
			System.out.println("selectBoardList - SQL 구문 오류 : " + e.getMessage());
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		return boardList;
	}
	public void updateReadcount(int board_num) {
		PreparedStatement pstmt = null;
		
		try {
			String sql = "UPDATE board SET board_readcount = board_readcount + 1 WHERE board_num=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, board_num);
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("updateReadcount - SQL 구문 오류 : " + e.getMessage());
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
	}
	
	public BoardDTO selectBoard(int board_num) {
		BoardDTO board = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT * FROM board WHERE board_num=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, board_num);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				board = new BoardDTO();
				board.setBoard_num(rs.getInt("board_num"));
				board.setBoard_name(rs.getString("board_name"));
				board.setBoard_pass(rs.getString("board_pass"));
				board.setBoard_subject(rs.getString("board_subject"));
				board.setBoard_content(rs.getString("board_content"));
				board.setBoard_file(rs.getString("board_file"));
				board.setBoard_real_file(rs.getString("board_real_file"));
				board.setBoard_re_ref(rs.getInt("board_re_ref"));
				board.setBoard_re_lev(rs.getInt("board_re_lev"));
				board.setBoard_re_seq(rs.getInt("board_re_seq"));
				board.setBoard_readcount(rs.getInt("board_readcount"));
				board.setBoard_date(rs.getDate("board_date"));
			}
		} catch (Exception e) {
			System.out.println("selectBoard - SQL 구문 오류 : " + e.getMessage());
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		return board;
	}
	
	public boolean isBoardWriter(int board_num, String board_pass) {
		boolean isBoardWriter = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT * FROM board WHERE board_num=? AND board_pass=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, board_num);
			pstmt.setString(2, board_pass);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				isBoardWriter = true;
			}
		} catch (SQLException e) {
			System.out.println("isBoardWriter - SQL 구문 오류 : " + e.getMessage());
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		return isBoardWriter;
	}
	public int removeBoard(int board_num) {
		int deleteCount = 0;
		PreparedStatement pstmt = null;
		
		try {
			String sql = "DELETE FROM board WHERE board_num=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, board_num);
			deleteCount = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("removeBoard - SQL 구문 오류 : " + e.getMessage());
			e.printStackTrace();
		}
		return deleteCount;
	}
	public int updateBoard(BoardDTO board) {
		int updateCount = 0;
		PreparedStatement pstmt = null;
		
		try {
			String sql = "UPDATE board SET board_name=?,board_subject=?,board_content=? WHERE board_num=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, board.getBoard_name());
			pstmt.setString(2, board.getBoard_subject());
			pstmt.setString(3, board.getBoard_content());
			pstmt.setInt(4, board.getBoard_num());
			updateCount = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("updateBoard - SQL 구문 오류 : " + e.getMessage());
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		return updateCount;
	}
	public int insertReplyBoard(BoardDTO board) {
		int insertCount = 0;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		
		try {
			// 새 글 번호를 저장할 변수
			int num = 1;
			// 새 글 번호로 사용될 번호를 생성하기 위해 기존 게시물의 가장 큰 번호 조회
			// => 조회결과가 있을 경울 해당 번호 +1 값을 새 글 번호로 저장
			String sql = "SELECT MAX(board_num) FROM board";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				num = rs.getInt(1) + 1;
			}
			
			// 기존 답글들에 대한 순서번호(board_re_seq) 증가 작업 처리 
			// => 원본글의 참조글번호(board_re_ref) 와 같고 (같은 레코드들 중에서)
			//    원본글의 순서번호(board_re_seq)보다 큰 레코드들의 순서번호를 1씩 증가시키기
			sql = "UPDATE board SET board_re_seq = board_re_seq + 1 "
					+ "WHERE board_re_ref = ? AND board_re_seq > ?";
			pstmt2 = con.prepareStatement(sql);
			pstmt2.setInt(1, board.getBoard_re_ref());
			pstmt2.setInt(2, board.getBoard_re_seq());
			int updateCount = pstmt2.executeUpdate();
			
			
			sql = "INSERT INTO board VALUES (?,?,?,?,?,?,?,?,?,?,?,now())";
			pstmt2 = con.prepareStatement(sql);
			pstmt2.setInt(1, num);
			pstmt2.setString(2, board.getBoard_name());
			pstmt2.setString(3, board.getBoard_pass());
			pstmt2.setString(4, board.getBoard_subject());
			pstmt2.setString(5, board.getBoard_content());
			pstmt2.setString(6, "");
			pstmt2.setString(7, "");
			// ---------- 주의! 답글 관련 번호 -----------
			pstmt2.setInt(8, board.getBoard_re_ref());
			pstmt2.setInt(9, board.getBoard_re_lev()+1);
			pstmt2.setInt(10, board.getBoard_re_seq()+1);
			// -------------------------------------------
			pstmt2.setInt(11, 0);
			insertCount = pstmt2.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("insertReplyBoard - SQL 구문 오류 : " + e.getMessage());
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		return insertCount;
	}
	
	
}



























