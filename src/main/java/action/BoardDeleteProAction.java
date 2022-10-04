package action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vo.ActionForward;
import db.JdbcUtil;
import db.JdbcUtil.*;
import svc.BoardDeleteProService;

public class BoardDeleteProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("BoardDeleteFormAction");
		ActionForward forward = null;
		
		int board_num = Integer.parseInt(request.getParameter("board_num"));
		String board_pass = request.getParameter("board_pass");
		System.out.println(board_num + ", " + board_pass);
		
		BoardDeleteProService service = new BoardDeleteProService();
		boolean isBoardWriter = service.isBoardWriter(board_num, board_pass);
		
		// 1) response 객체의 setContentType() 메서드를 호출하여 응답 문서 타입(cotentType) 지정
		response.setContentType("text/html; charset=UTF-8");
		// 2) response 객체의 getWriter() 메서드를 호출하여 출력 스트림(PrintWriter) 객체 리턴받기
		PrintWriter out = response.getWriter();
		
		if(!isBoardWriter) {
			out.println("<script>");
			out.println("alert('삭제 권한이 없습니다!')");
			out.println("history.back()");
			out.println("</script>");
		} else {
			boolean isDeleteSuccess = service.removeBoard(board_num);
			if(isDeleteSuccess) {
				forward = new ActionForward();
				forward.setPath("BoardList.bo?pageNum=" + request.getParameter("pageNum"));
				forward.setRedirect(true);
			} else {
				out.println("alert('삭제 실패!')");
				out.println("history.back()");
			}
		}
		return forward;
	}

}
