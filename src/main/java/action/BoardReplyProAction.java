package action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import svc.BoardReplyService;
import vo.ActionForward;
import vo.BoardDTO;

public class BoardReplyProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("BoardReplyProAction");
		ActionForward forward = null;
		
		int board_num = Integer.parseInt(request.getParameter("board_num"));
		int board_re_ref = Integer.parseInt(request.getParameter("board_re_ref"));
		int board_re_lev = Integer.parseInt(request.getParameter("board_re_lev"));
		int board_re_seq = Integer.parseInt(request.getParameter("board_re_seq"));
		String board_name = request.getParameter("board_name");
		String board_pass = request.getParameter("board_pass");
		String board_subject = request.getParameter("board_subject");
		String board_content = request.getParameter("board_content");
		BoardDTO board = new BoardDTO();
		board.setBoard_num(board_num);
		board.setBoard_re_ref(board_re_ref);
		board.setBoard_re_lev(board_re_lev);
		board.setBoard_re_seq(board_re_seq);
		board.setBoard_name(board_name);
		board.setBoard_pass(board_pass);
		board.setBoard_subject(board_subject);
		board.setBoard_content(board_content);
//		System.out.println(board);
		
		// BoardReplyService 의 replyBoard() 메서드를 호출하여 답글 등록 작업 요청
		// 파라미터 : BoardDTO 객체      리턴타입 : boolean(isReplySuccess)
		BoardReplyService service = new BoardReplyService();
		boolean isReplySuccess = service.replyBoard(board);
		
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		if(isReplySuccess) {
			forward = new ActionForward();
			forward.setPath("BoardList.bo?pageNum=" + Integer.parseInt(request.getParameter("pageNum")));
			forward.setRedirect(true);
		} else {
			out.println("<script>");
			out.println("alert('답글 등록 실패!')");
			out.println("history.back()");			
			out.println("</script>");
		}
		return forward;
	}

}
