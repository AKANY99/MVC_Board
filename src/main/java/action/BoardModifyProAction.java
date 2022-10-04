package action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import svc.BoardModifyProService;
import vo.ActionForward;
import vo.BoardDTO;

public class BoardModifyProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("BoardModifyProAction");
		ActionForward forward = null;
		
		BoardDTO board = new BoardDTO();
		int board_num = Integer.parseInt(request.getParameter("board_num"));
		String board_pass = request.getParameter("board_pass");
		board.setBoard_num(board_num);
		board.setBoard_name(request.getParameter("board_name"));
		board.setBoard_pass(board_pass);
		board.setBoard_subject(request.getParameter("board_subject"));
		board.setBoard_content(request.getParameter("board_content"));
		
		BoardModifyProService service = new BoardModifyProService();
		boolean isBoardWriter = service.isBoardWriter(board_num, board_pass);
		
		boolean isModifySuccess = service.updateBoard(board);
		
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		forward = new ActionForward();
		if(isBoardWriter) {
//			out.println("<script>");
//			out.println("alert('수정 권한 있음!')");
//			out.println("</script>");
			if(isModifySuccess) {
				forward.setPath("BoardDetail.bo?board_num=" + board_num + "&pageNum=" + request.getParameter("pageNum"));
				forward.setRedirect(true);				
			} else {
				out.println("<script>");
				out.println("alert('수정 실패!')");
				out.println("history.back()");
				out.println("</script>");				
			}
		} else {
			out.println("<script>");
			out.println("alert('수정 권한 없음!')");
			out.println("history.back()");
			out.println("</script>");
		}
		
		
		
		
		return forward;
	}

}
