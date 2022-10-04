package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vo.ActionForward;

/*
 * FrontController 로 요철이 전달되면 비즈니스 로직 처리 시 Action 클래스로 이동해야하는데
 * 이 때, Action 클래스의 메서드를 호출하여 request, response 객체를 전달하고
 * 요청받은 작업 진행 후 포워딩 할 정보를 공통의 ActionForward 객체 형태로 저장 후 리턴해야함
 * 또한, 처리할 예외들을 throws 하는 작업도 공통으로 기술해야함
 * => 위의 과정들을 각각의 Action 클래스에서 동일한 형태로 수행해야하므로 코드의 통일성 필요
 * => 따라서, 각 Action 클래스에서 수행할 메서드를 하나의 추상메서드로 제공하기 위해 
 *    Action 인터페이스를 설계하고, 공통 항목인 execute() 메서드를 추상메서드로 정의한 뒤
 *    각 Action 클래스에서 Action 인터페이스를 상속(구현)받아 execute() 메서드를 구현하면
 *    코드의 통일성이 향상되고, 차후에 다형성 활용도 가능해짐
 * => 추상클래스보다 인터페이스로 구현하면 종은 점은 차후 다른 클래스 상속에 문제가 없으며
 *    인터페이스는 다중 구현 갯수에 제한이 없기 때문에 추상메서드만 있을 경우 인터페이스가 유리
 */
public interface Action {
	// 각 요청을 받아들일 execute() 메서드를 추상메서드로 정의
	// => 파라미터 : HttpServletRequest 객체, HttpServletResponse 객체
	// => 리턴타입 : ActionForward타입(포워딩 정보)
	// => 내부에서 발생하는 모든 예외를 외부로 위임(던짐)
	public abstract ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception;
	
}
