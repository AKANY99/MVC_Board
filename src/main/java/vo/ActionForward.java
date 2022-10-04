package vo;

/*
 * FrontController(서블릿 클래스)에서 클라이언트로부터 요철을 받아
 * Actiom 클래스 등에서 작업을 처리한 후 View 페이지 또는 다른 서블릿 주소를 요청(= 포워딩)할 때
 * 포워딩 할 URL(주소)과 포워딩 방식(Redirect vs Dispatcher)을 관리할 클래스 정의
 */
public class ActionForward {
	private String path; // 포워딩 주소를 저장할 변수
	private boolean isRedirect; // 포워딩 방식을 저장할 변수(true : Redirect, false : Dispatcher)
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public boolean isRedirect() {
		return isRedirect;
	}
	public void setRedirect(boolean isRedirect) {
		this.isRedirect = isRedirect;
	}
	
}
