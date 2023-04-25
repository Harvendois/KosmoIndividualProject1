package console.academy;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import common.utility.CommonUtil;

public class MembershipLogic extends MembershipImpl{

	// 생성자
	public MembershipLogic() {
		fileToIdPassword();
		fileToMap();
	}////////// AcademyLogic()

	// [멤버 변수]
	

	// [멤버메소드]

	// 0-0] 환영 메소드
	public void welcome() {
		if(userId.equalsIgnoreCase("admin")) {
			System.out.println("관리자님, 환영합니다.");
			System.out.println("현재 접속하신 프로그램은 CSV버전입니다.");
		}
		else {
		System.out.println(findUsernameWithId() + "님, 환영합니다");
		}
	}///////
	
	// 0-1] 메뉴 출력용 메소드
	
	public void printMainMenu() {
		System.out.println("=========================================메인 메뉴========================================");
		System.out.println("1.입력 2. 출력 3.수정 4.삭제 5.검색 6.파일저장 7. CSV 파일로 입력 8.CSV 파일로 출력 9.종료 10.ID/PW 출력");
		System.out.println("=======================================================================================");
		System.out.println("===================================메인 메뉴 번호를 입력하세요?===============================");
	}////////// printMainMenu()

	// 0-2] 메뉴 번호 입력용 메소드
	public int getMenuNumber() {
		Scanner sc = new Scanner(System.in);
		int menuStr = 0;
		while(true) {
			try {
				menuStr = Integer.parseInt(getValue("메뉴 번호"));
				break;
			} 
			catch (NumberFormatException e) {
				System.out.println("메뉴에는 숫자만 넣어주세요");
				continue;
			}
		}
		return menuStr;
	}//////// getMenuNumber()

	// 0-3] 메뉴 분기 메소드
	public void printMainMenu(int mainMenu) throws IOException {
		switch (mainMenu) {
		case 1: // 입력
			if(isMember()) {
			addMembers("");
			}
			else {
				System.out.println("멤버 정보 보호를 위해 admin만이 입력/수정/삭제 할 수 있습니다.");
			}
			break;
		case 2: // 출력
			printSubMenu();
			int getSubMenu = getMenuNumber();
			if (getSubMenu == 3)
				break;
			switch (getSubMenu) {
			case 1:
				printMembersByName();
				break;
			case 2:
				printMembersByAge();
				break;
			default:
				System.out.println("서브메뉴에 없는 번호입니다");
			}
			break;
		case 3: // 수정
			if (userId.equals("admin")) {
				while (true) {
					editSubMenu();
					getSubMenu = getMenuNumber();
					if (getSubMenu == 5)
						break;
					else if (!(getSubMenu == 1 | getSubMenu == 2 | getSubMenu == 3 | getSubMenu == 4 | getSubMenu == 5))
						continue;
					Member member = findWithName();
					editMember(member, getSubMenu);
				}///while
			} ////if
			else {
				Set keys = memberMap.keySet();
				for (Object key : keys) {
					List<Member> values = memberMap.get(key);
					for (Member member : values) {
						try {
							if(member.id.equals(userId)) {
								while (true) {
									System.out.println(String.format("정보 보호를 위해 %s님은 본인의 정보만을 수정하실 수 있습니다.", findUsernameWithId()));
									editSubMenu();
									getSubMenu = getMenuNumber();
									if (getSubMenu == 5)
										break;
									else if (!(getSubMenu == 1 | getSubMenu == 2 | getSubMenu == 3 | getSubMenu == 4 | getSubMenu == 5))
										continue;
									editMember(member, getSubMenu);
								}
							}/////if
						}
						catch(NullPointerException e) {
							
						}
						finally {
							if(member.id==null) {
								continue;
							}
						}
					}
					
				}
			}
			break;
		case 4: // 삭제
			if(isMember()) {
				Member member = findWithName();
				System.out.println("해당 멤버를 삭제합니다.");
				deleteMember(member);
			}
			else System.out.println("멤버 정보 보호를 위해 admin만이 입력/수정/삭제 할 수 있습니다.");
			/*
			 * System.out.println("추가적으로 삭제하시려면 'x' 나 'X'를 누르세요."); String moreDelete =
			 * sc.nextLine(); if(moreDelete == "x" | moreDelete =="X") continue; else break;
			 */

			break;
		case 5: // 검색
			printSearchSubMenu();
			getSubMenu = getMenuNumber();
			if (getSubMenu == 4)
				break;
			switch (getSubMenu) {
			case 1:
				Member foundMember = findWithName();
				printFoundName(foundMember);
				break;
			case 2:
				findWithAddress();
				break;
			case 3:
				findWithContact();
				break;
			default:
				System.out.println("서브메뉴에 없는 번호입니다");
			}
			break;
		case 6: // 파일저장
			saveData();
			break;
		case 7: 
			if(isMember()) {
				fileToMembers();
			}
			else {
				System.out.println("멤버 정보 보호를 위해 admin만이 입력/수정/삭제 할 수 있습니다.");
			}
			break;
		case 8: // 현재 아이디 확인
			System.out.println("정보를 파일로 출력하시려면 비밀번호를 다시 입력하세요.");
			password(userId);
			membersToFile();
			break;
		case 9: // 종료
			end();
			break;
		case 10:
			if(isMember()) {
				System.out.println("[ID와 PW들입니다.]");
				Set keys = passwordMap.keySet();
				for (Object key : keys) {
					String value = passwordMap.get(key);
					System.out.println(String.format("%s : %s", key,value ));
				}
			}
			else System.out.println("멤버 정보 보호를 위해 admin만이 입력/수정/삭제 할 수 있습니다.");
			break;
		case 66: if(isMember()) {
			password(userId);
			System.out.println("멤버 정보를 모두 삭제하시겠습니까?");
			if(getValue("삭제를 원하시면 'y'").equalsIgnoreCase("y")) {
				System.out.println("execute order 66");
				memberMap.clear();
			}
			break;
		}
		default:
			System.out.println("메뉴에 없는 번호입니다");
		}//// switch
	}///////// separateMainMenu(int mainMenu)

	
	// 2-1] 출력/보기 서브 메뉴 출력용 메소드
	public void printSubMenu() {
		System.out.println("===================출력 서브 메뉴==================");
		System.out.println("1.이름 정렬 출력 2. 나이 정렬 출력 3. 메인 메뉴로 이동");
		System.out.println("================================================");
	}//////// printSubMenu()

	// 2-2] 이름을 오름차순으로 정렬하여 출력하는 메소드
	private void printMembersByName() {
		System.out.println("[오름차순으로 이름 정렬된 현재 인원 목록]");
		Set<Character> keys = memberMap.keySet();
		for (Object key : keys) {
			List<Member> value = memberMap.get(key);
			Collections.sort(value);
			System.out.println(String.format("[%c에 해당하는 인원들: %d명]", key, value.size()));
			for (Member member : value) {
				member.print();
			}
		}
	}////////////// printMembersByName()

	// 2-3] 나이로 정렬해서 출력하는 메소드
	private void printMembersByAge() {
		System.out.println("[오름차순으로 나이 정렬된 현재 인원 목록]");
		Set<Character> keys = memberMap.keySet();
		for (Object key : keys) {
			List<Member> value = memberMap.get(key);
			Collections.sort(value, new Comparator<Member>() {

				@Override
				public int compare(Member src, Member target) {
					return src.age - target.age;
				}
			}); ///// override
			System.out.println(String.format("[%c에 해당하는 인원들]", key));
			for (Member member : value) {
				member.print();
			}
		} //// foreach
	}////////////// printMembersByAge()

	// 3-1] 수정할 정보 선택지를 출력하는 메소드
	public void editSubMenu() {
		System.out.println("========================출력 서브 메뉴=======================");
		System.out.println("1.이름 수정 2. 나이 수정 3. 주소 수정 4. 번호 수정  5. 메인 메뉴로 이동");
		System.out.println("==========================================================");
	}//////// printSubMenu()

	// 3-2] 멤버를 수정해주는 메소드
	private void editMember(Member member, int subMenuIndex) {
		String revisedTitle = "";
		String revised = "";
		Scanner sc = new Scanner(System.in);
		
		if (subMenuIndex == 1) {
			String newName = inputName();
			char firstChar = CommonUtil.getJaeum(newName);
			List<Member> listMember;
			if (!memberMap.containsKey(firstChar)) {
				listMember = new ArrayList<>();
			} else {
				listMember = memberMap.get(firstChar);
			}
			listMember.add(new Member(member.id, newName, member.age, member.addr, member.cont));
			memberMap.put(firstChar, listMember);
			List<Member> oldMemberList;
			char deleteChar = CommonUtil.getJaeum(member.name);
			oldMemberList = memberMap.get(deleteChar);
			oldMemberList.remove(member);
			if(oldMemberList.isEmpty()) {
				memberMap.remove(deleteChar);
			}
			revisedTitle = "이름";
			revised = newName;
			
		} else if (subMenuIndex == 2) {
			System.out.println(String.format("[기존 나이: %s]", member.age));
			int newAge = Integer.parseInt(getValue("수정된 나이"));
			member.age = newAge;
			revisedTitle = "나이";
			revised = String.valueOf(newAge);
		} else if (subMenuIndex == 3) {
			System.out.println(String.format("[기존 주소: %s]", member.addr));
			String newAddr = getValue("수정된 주소");
			member.addr = newAddr;
			revisedTitle = "주소";
			revised = newAddr;
		} else if (subMenuIndex == 4) {
			System.out.println(String.format("[기존 번호: %s]", member.cont));
			String newCont = getValue("수정된 전화번호");
			member.cont = newCont;
			revisedTitle = "번호";
			revised = newCont;
		}
		System.out.println(String.format("%s 님의 %s가/이 %s 로 수정되었습니다", member.name, revisedTitle, revised));

	}////////// editMemberName(int index)

	// 4] 삭제 메소드
	private void deleteMember(Member member) {
		String deleteName = member.name;
		char key = common.utility.CommonUtil.getJaeum(deleteName);
		List<Member> list = memberMap.get(key);
		char deleteChar = CommonUtil.getJaeum(member.name);
		list.remove(member);
		if(list.isEmpty()) {
			memberMap.remove(deleteChar);
		}
		System.out.println(String.format("%s 멤버의 정보가 삭제되었습니다", deleteName));
	}//////////////////// deleteMember(int indexEdit)

	// 5] 검색 서브메뉴 출력용 메소드

	public void printSearchSubMenu() {
		System.out.println("======================검색 서브 메뉴======================");
		System.out.println("1.이름으로 검색 2. 주소로 검색 3. 전화번호로 검색 4.메인 메뉴로 이동");
		System.out.println("팁: 이름으로 검색하는게 가장 빨라요!");
		System.out.println("=======================================================");
	}//////// printSearchSubMenu()

	// [5-1] 이름으로 검색하는 메소드
	public Member findWithName() {
		String name;
		while(true) {
			name = inputName();
			int index = 0;
			char jaeum = common.utility.CommonUtil.getJaeum(name);
			List<Member> list = memberMap.get(jaeum);
			Member memberSearched = new Member();
			try {
				for (Member member : list) {
					if (member.name.equals(name)) {
						memberSearched = member;
						break;
					} /// if
				} //// foreach
			}
			catch(NullPointerException e) {
				System.out.println("검색하신 사람은 없는 멤버입니다.");
			}
			if(!(memberSearched==null || memberSearched.name==null)) {
				return memberSearched;
			}
			else {
				System.out.println("멤버를 다시 찾아주세요");
				continue;
			}
		}////while
	}/////findWithName

	// 5-1-4] 이름으로 검색된 멤버를 출력하는 메소드
	private void printFoundName(Member member) {
		if (member.name==null) {
			System.out.println("메인 메뉴로 돌아갑니다.");
			return;
		}
		else {
			System.out.println(String.format("[%s 멤버 정보]", member.name));
			member.print();
		}
	}

	// [5-2] 나이로 검색하는 메소드 //나이대로 검색 가능하게.
	/*
	 * private void findWithAge() { Scanner sc = new Scanner(System.in);
	 * System.out.println("어떤 나이로 검색하시겠습니까?"); int age2 =
	 * Integer.parseInt(sc.nextLine().trim());
	 * System.out.println(String.format("[%s 세인 인원들 검색 결과]", age2)); for (member p :
	 * listMember) { if (p.age == age2) { p.print(); } /// if } //// foreach
	 * }//////// findWithSubject()
	 */

	// [5-3] 주소로 검색하는 메소드
	private void findWithAddress() {
		String address = getValue("검색하실 주소");
		System.out.println(String.format("[%s 에 사는 인원들]", address));
		Set keys = memberMap.keySet();
		for (Object key : keys) {
			List<Member> list = memberMap.get(key);
			for (Member p : list)
				if (p.addr.equals(address)) {
					p.print();
				} /// if
		} //// foreach
	}///////// findWithAddress()

	// [5-4]전번으로 검색하는 메소드
	private void findWithContact() {
		String contact = getValue("검색하실 전화번호");
		System.out.println(String.format("[%s 의 전화번호를 가진 인원들]", contact));
		Set keys = memberMap.keySet();
		for (Object key : keys) {
			List<Member> list = memberMap.get(key);
			for (Member p : list)
				if (p.addr.equals(contact)) {
					p.print();
				} /// if
		} //// foreach
	}//////// findWithContact()


	

	

}///////// class
