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

	// 0-1] 메뉴 출력용 메소드

	public void printMainMenu() {
		System.out.println("==================메인 메뉴===================");
		System.out.println("1.입력 2. 출력 3.수정 4.삭제 5.검색 6.파일저장 9.종료");
		System.out.println("============================================");
		System.out.println("===========메인 메뉴 번호를 입력하세요?============");
	}////////// printMainMenu()

	// 0-2] 메뉴 번호 입력용 메소드
	public int getMenuNumber() {
		Scanner sc = new Scanner(System.in);
		int menuStr = 0;
		try {
			menuStr = Integer.parseInt(getValue("메뉴 번호"));
		} catch (NumberFormatException e) {
			System.out.println("메뉴에는 숫자만 넣어주세요");
			System.out.println("메뉴 번호를 입력하세요?");
			getMenuNumber();
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
				Member user;
				Set keys = memberMap.keySet();
				for (Object key : keys) {
					List<Member> values = memberMap.get(key);
					for (Member member : values) {
						if(member.id.equals(userId)) {
							while (true) {
								System.out.println(String.format("정보 보흐를 위해 %s님은 본인의 정보만을 수정하실 수 있습니다.", findUsernameWithId()));
								editSubMenu();
								getSubMenu = getMenuNumber();
								if (getSubMenu == 5)
									break;
								else if (!(getSubMenu == 1 | getSubMenu == 2 | getSubMenu == 3 | getSubMenu == 4 | getSubMenu == 5))
									continue;
								editMember(member, getSubMenu);
							}
							break;
						}
					}
					break;
				}
			}
			break;
		case 4: // 삭제
			if(isMember()) {
				Member member = findWithName();
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
		case 9: // 종료
			end();
			break;
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
			System.out.println(String.format("[%c에 해당하는 인원들]", key));
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
			String newName = getValue("수정하실 새로운 이름");
			char firstChar = CommonUtil.getJaeum(newName);
			List<Member> listMember;
			if (!memberMap.containsKey(firstChar)) {
				listMember = new ArrayList<>();
			} else {
				listMember = memberMap.get(firstChar);
			}
			listMember.add(new Member(member.id, newName, member.age, member.addr, member.cont));
			memberMap.put(firstChar, listMember);
			deleteMember(member);
			revisedTitle = newName;
			
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
		System.out.println(String.format("%s 의 %s가/이 %s 로 수정되었습니다", member.name, revisedTitle, revised));

	}////////// editMemberName(int index)

	// 4] 삭제 메소드
	private void deleteMember(Member member) {
		String deleteName = member.name;
		char key = findKey(deleteName);
		List<Member> list = memberMap.get(key);
		list.remove(member);
		if(list.isEmpty()) {
			//비게 될 Key, jaeum은 지워버리기
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
			name = receiveName();
			char jaeum = findKey(name);
			Member member = findMember(jaeum, name);
			if(!(member==null)) {
				return member;
			}
			else {
				System.out.println("멤버를 다시 찾아주세요");
				continue;
			}
		}////while
	}/////findWithName

	// 5-1-1] 이름을 받는 메소드
	private String receiveName() {
		String name = inputName();
		return name;
	}/////// receiveName()

	// 5-1-2] Key를 찾는 메소드
	private char findKey(String name) {
		int index = 0;
		char key = common.utility.CommonUtil.getJaeum(name);
		return key;
	}////////////// findKey()

	// 5-1-3] member을 찾는 메소드
	public Member findMember(char key, String name) {
		List<Member> list = memberMap.get(key);
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
			return null;
		}
		return memberSearched;
	}////// findmember

	// 5-1-4] 이름으로 검색된 멤버를 출력하는 메소드
	private void printFoundName(Member member) {
		if (member.name==null) {
			System.out.println("메인 메뉴로 돌아갑니다.");
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


	

	// 9] 종료 메소드
	private void end() {
		System.out.println("프로그램을 종료 합니다");
		System.out.println(String.format("%s님, 안녕히가세요.", findUsernameWithId()));
		System.exit(0); // 정상적인 종료라고 0을 줌.
	}

}///////// class
