package console.academy;

import java.io.IOException;
import java.net.PasswordAuthentication;

public class MembershipApp {

	public static void main(String[] args) throws IOException {
		//배열 사용]
		MembershipLogic logic = new MembershipLogic();
		//0. 로그인
		logic.password(logic.login());
		logic.welcome();
		while(true) {
		//1. 메인 메뉴 출력
		logic.printMainMenu();
		//2. 메인 메뉴 번호 입력받기
		int mainMenu = logic.getMenuNumber();
		//3. 메인메뉴에 따른 분기
		logic.printMainMenu(mainMenu);
		}///////while
	}///////main

}////////class
