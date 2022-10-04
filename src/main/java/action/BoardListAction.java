package action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import svc.BoardListService;
import vo.ActionForward;
import vo.BoardDTO;
import vo.PageInfo;

public class BoardListAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("BoardListAction - execute");
		
		ActionForward forward = null;
		
		// 페이징 처리를 위한 변수 선언
		int pageNum = 1;
		int listLimit = 10;
		int pageLimit = 10;

		if(request.getParameter("pageNum") != null) {
			pageNum = Integer.parseInt(request.getParameter("pageNum"));
		}
		
		// 페이징 처리에 필요한 전체 게시물 갯수 조회 작업 요청
		// BoardListService 클래스 인스턴스 생성 후 getListCount() 메서드 호출하여 총 게시물 수 조회
		// => 파라미터 : 없음    리턴타입 : int(listCount)
		BoardListService service = new BoardListService();
		int listCount = service.getListCount();
		System.out.println("전체 게시물 수 : " + listCount);
		
		//페이징 처리를 위한 계산 처리
		//1. 현재 페이지에서 표시할 전체 페이지 수 계산
		int maxPage = (int)Math.ceil((double)listCount / listLimit);
		//2. 현재 페이지에서 보여줄 시작 페이지 번호
		int startPage = ((int)((double)pageNum / pageLimit + 0.9) - 1) * pageLimit + 1;
		//3. 현재 페이지에서 보여줄 끝 페이지 번호
		int endPage = startPage + pageLimit - 1;
		//4. 만약, 끝 페이지가 현재 페이지에서 표시할 총 페이지 수보다 클 경우
		// 끝 페이지를 총 페이지 수로 대체
		if(endPage > maxPage) {
			endPage = maxPage;
		}
		
		// 페이징 처리 정보를 PageInfo 객체에 저장
		PageInfo pageInfo = new PageInfo(pageNum, maxPage, startPage, endPage, listCount);
		
		// BoardListService 객체의 getBoardList() 메서드를 호출하여 게시물 목록 가져오기
		// => 파라미터 : 현재페이지번호(pageNum), 페이지 당 게시물 수(listLimit)
		// => 리턴타입 : ArrayList<BoardDTO>
		ArrayList<BoardDTO> boardList = service.getBoardList(pageNum, listLimit);
		
		// 뷰 페이지(jsp)에서 사용할 데이터가 저장된 객체들을 전달하기 위해 
		// request 객체의 setAttribute() 메세드를 호출하여 객체 저장
		request.setAttribute("pageInfo", pageInfo);
		request.setAttribute("boardList", boardList);
		
		// ActionForward 객체 생성하여 포워딩 정보 저장
		// => board 디렉토리 내의 qna_board_list.jsp 페이지 지정
		// => URL 및 request 객체 유지한 채 포워딩을 위해 Dispatcher 방식 지정
		forward = new ActionForward();
		forward.setPath("board/qna_board_list.jsp");
		forward.setRedirect(false);
		
		return forward;
	}

}
