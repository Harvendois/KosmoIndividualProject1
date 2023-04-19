package console.academy;

import java.io.IOException;
import java.net.PasswordAuthentication;

public class HrApp {

	public static void main(String[] args) throws IOException {
		//배열 사용]
		AdminLogic alogic = new AdminLogic();
		//EmployeeLogic elogic = new EmployeeLogic(); 
		
		//0. 로그인
		alogic.password(alogic.login());
		while(true) {
		//1. 메인 메뉴 출력
		if(alogic.isMember()) {
			alogic.printAdminMainMenu();
		}
		else {
			
		}
		//2. 메인 메뉴 번호 입력받기
		int mainMenu = alogic.getMenuNumber();
		//3. 메인메뉴에 따른 분기
		alogic.printMainMenu(mainMenu);
		}///////while
	}///////main

}////////class
