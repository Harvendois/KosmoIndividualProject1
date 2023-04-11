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

public class AcademyLogic {

	// 생성자
	public AcademyLogic() {
		fileToIdPassword();
		fileToMap();
	}////////// AcademyLogic()

	// [멤버 변수]
	Map<Character, List<Person>> memberMap = new HashMap<>();
	Map<String, String> passwordMap = new HashMap<>();
	String userId = "";
	//boolean identification = false;

	// [멤버메소드]

	// 0-0-1] 번외: 회원가입 메소드
	public void register() throws FileNotFoundException, IOException {
		Scanner sc = new Scanner(System.in);
		String idRegister;
		addMembers();
		while (true) {
			System.out.println("사용하실 아이디를 6자이상, 12자 이하로 입력하세요");
			idRegister = sc.nextLine().trim();
			if (idRegister.length() < 6) {
				System.out.println("아이디는 6자이상입니다.");
				continue;
			}
			if (idRegister.length() > 12) {
				System.out.println("아이디는 12자가 최대입니다.");
				continue;
			}
			break;
		}

		Set keys = passwordMap.keySet();
		for (Object idMap : keys) {
			if (idRegister.equals(idMap)) {
				System.out.println("이미 존재하는 아이디/이름 입니다. 로그인하세요");
				return;
			}
		}
		System.out.println("새로운 계정을 만드셨네요! 계정에 사용될 비밀번호를 입력하세요.");
		String pwRegister = sc.nextLine().trim();
		passwordMap.put(idRegister, pwRegister);
		try {
			saveIdPassword();
		} catch (IOException e) {
			System.out.println("저장 오류");
		}
	}//// register()

	// 0-0] 로그인 메소드
	public String login() throws FileNotFoundException, IOException {
		System.out.println("****************멤버 관리자 프로그램에 접속하신것을 환영합니다***************");
		System.out.println("                                                        ");
		System.out.println("              로그인하셔야 프로그램 사용이 가능합니다               ");
		System.out.println("            첫 로그인이라면 admin 아이디로 로그인하세요              ");
		System.out.println("                                                             ");
		passwordMap.put("admin", "12345678");
		Scanner sc = new Scanner(System.in);
		String registerKey = "q";
		String x = "override";
		System.out.println("    회원가입을 원하시면 'q'를 누르세요. / 로그인을 원하시면 아무키나 누르세요.");
		System.out.println("******************************************************************");
		String regOrLog = sc.nextLine().trim();
		if (regOrLog.equals(registerKey)) {
			register();
		}
		int loginAttemptCount = 0;
		while (true) {
			if(loginAttemptCount==3) {
				loginAttemptCount=0;
				System.out.println("id가 없으신가요?");
				System.out.println("계정을 만드시려면 'y', 이어하시려면 아무키나 눌러주세요.");
				String whetherOrNot = sc.nextLine().trim();
				if(whetherOrNot.equals("y")) {
					System.out.println("회원가입창으로 이동합니다");
					register();}
			}
			System.out.println("============================로그인 창==============================");
			System.out.println("ID를 입력하세요");
			String id = sc.nextLine().trim();
			loginAttemptCount++;
			if (id.equals(x)) {
				System.out.println("id override successful");
				//identification = true;
				return "aaa";
			}
			if (id.equals("admin")) {
				//identification = true;
			}
			Set keys = passwordMap.keySet();
			for (Object identity : keys) {
				if (id.equals(identity)) {
					x = (String) identity;
					return x;
				}
			} //// for
			System.out.println("등록되지 않은 아이디 입니다");
			continue;
		} ////// while
	}/////// login()

	// 0-0-2] 비밀번호 입력 메소드
	public void password(String x) {
		Scanner sc = new Scanner(System.in);
		while (true) {
			if (x == "aaa") {
				System.out.println("pw override successful.");
				return;
			}
			System.out.println(x + "의 비밀번호를 입력하세요");
			String pw = sc.nextLine().trim();
			if (pw.equals(passwordMap.get(x))) {
				System.out.println(x + "님, 환영합니다");
				return;
			} else {
				System.out.println("비밀번호가 틀렸습니다");
				continue;
			}
		}
	}

	// 0-1] map 불러오는 메소드
	@SuppressWarnings("unchecked")
	private Map<Character, List<Person>> fileToMap() {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream("src/console/academy/member.txt"));
			Object obj = ois.readObject();
			if (obj instanceof HashMap) {
				memberMap = (HashMap<Character, List<Person>>) obj;
				Set keys = memberMap.keySet();
				for (Object key : keys) {
					List<Person> values = memberMap.get(key);
					memberMap.put((Character) key, values);
					/*
					 * for(int i=0; i<listPerson.size(); i++) { listPerson.get(i).print(); }
					 */
				}

			}

		} catch (FileNotFoundException | ClassNotFoundException e) {
			System.out.println("파일을 찾는데 오류");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("파일을 불러오는데 오류");
			e.printStackTrace();
		} finally {
			try {
				if (ois != null)
					ois.close();
				return memberMap;
			} catch (IOException e) {
				System.out.println("파일 닫기시 오류");
			}
		}
		return null;
	}

	// 0-1-1] 로그인 정보를 불러오는 메소드
	@SuppressWarnings("unchecked")
	private Map<String, String> fileToIdPassword() {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream("src/console/academy/idPassword.txt"));
			Object obj = ois.readObject();
			if (obj instanceof HashMap) {
				passwordMap = (HashMap<String, String>) obj;
				Set keys = passwordMap.keySet();
				for (Object key : keys) {
					String values = passwordMap.get(key);
					passwordMap.put((String) key, values);
					/*
					 * for(int i=0; i<listPerson.size(); i++) { listPerson.get(i).print(); }
					 */
				}

			}

		} catch (FileNotFoundException | ClassNotFoundException e) {
			System.out.println("파일을 찾는데 오류");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("파일을 불러오는데 오류");
			e.printStackTrace();
		} finally {
			try {
				if (ois != null)
					ois.close();
				return passwordMap;
			} catch (IOException e) {
				System.out.println("파일 닫기시 오류");
			}
		}
		return null;
	}//////////// fileToIdPassword()

	// 0-2] 메뉴 출력용 메소드

	public void printMainMenu() {
		System.out.println("==================메인 메뉴===================");
		System.out.println("1.입력 2. 출력 3.수정 4.삭제 5.검색 6.파일저장 9.종료");
		System.out.println("============================================");
		System.out.println("===========메인 메뉴 번호를 입력하세요?============");
	}////////// printMainMenu()

	// 0-3] 메뉴 번호 입력용 메소드
	public int getMenuNumber() {
		Scanner sc = new Scanner(System.in);
		int menuStr = 0;
		try {
			menuStr = Integer.parseInt(sc.nextLine().trim());
		} catch (NumberFormatException e) {
			System.out.println("메인메뉴에는 숫자만 넣어주세요");
			System.out.println("메인 메뉴 번호를 입력하세요?");
			getMenuNumber();
		}
		return menuStr;
	}//////// getMenuNumber()

	// 0-4] 메뉴 분기 메소드
	public void printMainMenu(int mainMenu) throws IOException {
		switch (mainMenu) {
		case 1: // 입력
			addMembers();
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
			while (true) {
				editSubMenu();
				getSubMenu = getMenuNumber();
				if (getSubMenu == 5)
					break;
				else if (!(getSubMenu == 1 | getSubMenu == 2 | getSubMenu == 3 | getSubMenu == 4 | getSubMenu == 5))
					continue;
				Person person = findWithName();
				editMember(person, getSubMenu);
			}
			break;
		case 4: // 삭제
			Scanner sc = new Scanner(System.in);
			int i = 0;

			Person person = findWithName();
			deleteMember(person);
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
				printFoundName(findWithName());
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

	// 1] 인원 추가 메소드
	private void addMembers() throws FileNotFoundException, IOException {
		Scanner sc = new Scanner(System.in);
		String name = common.utility.CommonUtil.inputName();
		int age = common.utility.CommonUtil.inputAge(name);
		String addr = common.utility.CommonUtil.inputAddr(name);
		String cont = common.utility.CommonUtil.inputCont(name);

		char firstChar = CommonUtil.getJaeum(name);
		List<Person> listPerson;
		if (!memberMap.containsKey(firstChar)) {
			listPerson = new ArrayList<>();
		} else {
			listPerson = memberMap.get(firstChar);
		}
		listPerson.add(new Person(name, age, addr, cont));
		memberMap.put(firstChar, listPerson);
		System.out.println(name + "님이 추가되었습니다");
	}////////// addMembers()

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
			List<Person> value = memberMap.get(key);
			Collections.sort(value);
			System.out.println(String.format("[%c에 해당하는 인원들]", key));
			for (Person person : value) {
				person.print();
			}
		}
	}////////////// printMembersByName()

	// 2-3] 나이로 정렬해서 출력하는 메소드
	private void printMembersByAge() {
		System.out.println("[오름차순으로 나이 정렬된 현재 인원 목록]");
		Set<Character> keys = memberMap.keySet();
		for (Object key : keys) {
			List<Person> value = memberMap.get(key);
			Collections.sort(value, new Comparator<Person>() {

				@Override
				public int compare(Person src, Person target) {
					return src.age - target.age;
				}
			}); ///// override
			System.out.println(String.format("[%c에 해당하는 인원들]", key));
			for (Person person : value) {
				person.print();
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
	private void editMember(Person person, int subMenuIndex) {
		String revisedTitle = "";
		String revised = "";
		Scanner sc = new Scanner(System.in);
		if (subMenuIndex == 1) {
			System.out.println("새로운 이름을 입력하세요");
			String newName = sc.nextLine().trim();
			person.name = newName;
			revisedTitle = "이름";
			revised = newName;
		} else if (subMenuIndex == 2) {
			System.out.println("새로운 나이를 입력하세요");
			System.out.println(String.format("[기존 나이: %s]", person.age));
			int newAge = Integer.parseInt(sc.nextLine().trim());
			person.age = newAge;
			revisedTitle = "나이";
			revised = String.valueOf(newAge);
		} else if (subMenuIndex == 3) {
			System.out.println("새로운 주소를 입력하세요");
			System.out.println(String.format("[기존 주소: %s]", person.addr));
			String newAddr = sc.nextLine().trim();
			person.addr = newAddr;
			revisedTitle = "주소";
			revised = newAddr;
		} else if (subMenuIndex == 4) {
			System.out.println("새로운 번호를 입력하세요");
			System.out.println(String.format("[기존 나이: %s]", person.cont));
			String newCont = sc.nextLine().trim();
			person.cont = newCont;
			revisedTitle = "번호";
			revised = newCont;
		}
		System.out.println(String.format("%s 의 %s가/이 %s 로 수정되었습니다", person.name, revisedTitle, revised));

	}////////// editMemberName(int index)

	// 4] 삭제 메소드
	private void deleteMember(Person person) {
		String deleteName = person.name;
		char key = findKey(deleteName);
		List<Person> list = memberMap.get(key);
		list.remove(person);
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
	public Person findWithName() {
		String name;
		name = receiveName();
		char jaeum = findKey(name);
		Person person = findPerson(jaeum, name);
		return person;
	}

	// 5-1-1] 이름을 받는 메소드
	private String receiveName() {
		String name = common.utility.CommonUtil.inputName();
		return name;
	}/////// receiveName()

	// 5-1-2] Key를 찾는 메소드
	private char findKey(String name) {
		int index = 0;
		char key = common.utility.CommonUtil.getJaeum(name);
		return key;
	}////////////// findKey()

	// 5-1-3] Person을 찾는 메소드
	public Person findPerson(char key, String name) {
		List<Person> list = memberMap.get(key);
		Person personSearched = new Person();
		try {
			for (Person person : list) {
				if (person.name.equals(name)) {
					personSearched = person;
					break;
				} /// if
			} //// foreach
		}
		catch(NullPointerException e) {
			System.out.println("검색하신 사람은 없는 멤버입니다.");
		}
		return personSearched;
	}////// findName()

	// 5-1-4] 이름으로 검색된 멤버를 출력하는 메소드
	private void printFoundName(Person person) {
		if (person.name==null) {
			System.out.println("메인 메뉴로 돌아갑니다.");
		}
		else {
			System.out.println(String.format("[%s 멤버 정보]", person.name));
			person.print();
		}
	}

	// [5-2] 나이로 검색하는 메소드 //나이대로 검색 가능하게.
	/*
	 * private void findWithAge() { Scanner sc = new Scanner(System.in);
	 * System.out.println("어떤 나이로 검색하시겠습니까?"); int age2 =
	 * Integer.parseInt(sc.nextLine().trim());
	 * System.out.println(String.format("[%s 세인 인원들 검색 결과]", age2)); for (Person p :
	 * listPerson) { if (p.age == age2) { p.print(); } /// if } //// foreach
	 * }//////// findWithSubject()
	 */

	// [5-3] 주소로 검색하는 메소드
	private void findWithAddress() {
		Scanner sc = new Scanner(System.in);
		System.out.println("어떤 학번으로 검색하시겠습니까?");
		String address = sc.nextLine().trim();
		System.out.println(String.format("[%s 에 사는 인원들]", address));
		Set keys = memberMap.keySet();
		for (Object key : keys) {
			List<Person> list = memberMap.get(key);
			for (Person p : list)
				if (p.addr.equals(address)) {
					p.print();
				} /// if
		} //// foreach
	}///////// findWithAddress()

	// [5-4]전번으로 검색하는 메소드
	private void findWithContact() {
		Scanner sc = new Scanner(System.in);
		System.out.println("검색하실 번호를 숫자로만 입력하세요");
		String contact = sc.nextLine().trim();
		System.out.println(String.format("[%s 의 전화번호를 가진 인원들]", contact));
		Set keys = memberMap.keySet();
		for (Object key : keys) {
			List<Person> list = memberMap.get(key);
			for (Person p : list)
				if (p.addr.equals(contact)) {
					p.print();
				} /// if
		} //// foreach
	}//////// findWithContact()

	// 6] 파일 저장 메소드.
	private void saveData() throws IOException {
		if (memberMap.isEmpty()) {
			System.out.println("저장하실 데이터가 없습니다.");
			return;
		}
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new FileOutputStream("src/console/academy/member.txt"));
			out.writeObject(memberMap);
			System.out.println("저장되었습니다.");
		}

		catch (IOException e) {
			System.out.println("파일 저장 오류");
		} finally {
			if (out != null)
				out.close();
		}
	}//// saveData()

	// 6-1] 로그인 정보를 저장하는 메소드
	private void saveIdPassword() throws IOException {
		if (passwordMap.isEmpty()) {
			System.out.println("저장하실 데이터가 없습니다.");
			return;
		}
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new FileOutputStream("src/console/academy/idPassword.txt"));
			out.writeObject(passwordMap);
			System.out.println("저장되었습니다.");
		}

		catch (IOException e) {
			System.out.println("파일 저장 오류");
		} finally {
			if (out != null)
				out.close();
		}
	}//// saveIdPassword()

	// 9] 종료 메소드
	private void end() {
		System.out.println("프로그램을 종료 합니다");
		System.out.println(String.format("%s님, 안녕히가세요.", userId));
		System.exit(0); // 정상적인 종료라고 0을 줌.
	}

}///////// class
