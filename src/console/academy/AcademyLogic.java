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
		fileToMap();
	}////////// AcademyLogic()

	// [멤버 변수]
	List<Person> listPerson = new ArrayList<>();
	Map<Character, List<Person>> memberMap = new HashMap<>();
	Map<String, String> password = new HashMap<>();
	
	
	// [멤버메소드]
	
	// 0-0] 로그인 메소드
	public String login(){
		System.out.println("**********멤버 관리자 프로그램에 접속하신것을 환영합니다*********");
		System.out.println("");
		System.out.println("로그인하셔야 프로그램 사용이 가능합니다");
		System.out.println("첫 로그인이라면 admin 권한으로 로그인하세요");
		System.out.println("");
		password.put("admin", "12345678");
		Scanner sc = new Scanner(System.in);
		String x = "";
		while(true) {
			System.out.println("이름을 입력하세요");
			String id = sc.nextLine().trim();
			Set keys = password.keySet();
			for (Object identity : keys) {
				if(id.equals(identity)) {
					x=(String)identity;
					return x;
				}
				else {
					System.out.println("등록되지 않은 아이디 입니다");
				}
			}////for
			continue;
		}//////while
	}///////login()
	
	public void password(String x) {
		Scanner sc = new Scanner(System.in);
		while (true) {

			System.out.println(x+"의 비밀번호를 입력하세요");
			String pw = sc.nextLine().trim();
			if (pw.equals(password.get(x))) {
				System.out.println(x + "님, 환영합니다");
				return;
			} else {
				System.out.println("비밀번호가 틀렸습니다");
				continue;
			}
		}
	}
	
	// 0-1] map 불러오는 메소드 
	private Map<Character, List<Person>> fileToMap() {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(
					new FileInputStream("src/console/academy/member.txt"));
			Object obj = ois.readObject();
			if(obj instanceof HashMap) {
				memberMap = (HashMap<Character, List<Person>>) obj;
				Set keys = memberMap.keySet();
				for (Object key : keys) {
					List<Person> values = memberMap.get(key);
					for (Person listValues : values) {
						listPerson.add(listValues);
					}
					/*for(int i=0; i<listPerson.size(); i++) {
						listPerson.get(i).print();
					}*/
				}
					
			}
		} 
		catch (FileNotFoundException | ClassNotFoundException e) {
				System.out.println("파일을 찾는데 오류");
			e.printStackTrace();
		} 
		catch (IOException e) {
				System.out.println("파일을 불러오는데 오류");
			e.printStackTrace();
		}
		finally {
			try {
				if(ois!=null) ois.close();
				return memberMap;
			} 
			catch (IOException e) {
				System.out.println("파일 닫기시 오류");}
		}
		return null;
	}
	
	// 0-2] 메뉴 출력용 메소드

	public void printMainMenu() {
		System.out.println("==================메인 메뉴===================");
		System.out.println("1.입력 2. 출력 3.수정 4.삭제 5.검색 6.파일저장 9.종료");
		System.out.println("============================================");
		System.out.println("메인 메뉴 번호를 입력하세요?");
	}////////// printMainMenu()

	// 0-3] 메뉴 번호 입력용 메소드
	public int getMenuNumber() {
		Scanner sc = new Scanner(System.in);
		int menuStr=0;
		try {
			menuStr = Integer.parseInt(sc.nextLine().trim());
		}
		catch (NumberFormatException e) {
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
			while(true){
				editSubMenu();
				getSubMenu = getMenuNumber();
				if(getSubMenu == 5)
					break;
				else if(!(getSubMenu == 1 |getSubMenu ==  2 |getSubMenu == 3 |getSubMenu == 4|getSubMenu==5))
					continue;
				int indexEdit = findWithName();
				editMember(indexEdit, getSubMenu);
			}
			break;
		case 4: // 삭제
			Scanner sc = new Scanner(System.in);
			int i=0;
			
				int indexEdit = findWithName();
				deleteMember(indexEdit);
				/*System.out.println("추가적으로 삭제하시려면 'x' 나 'X'를 누르세요.");
				String moreDelete = sc.nextLine();
				if(moreDelete == "x" | moreDelete =="X")
					continue;
				else break;*/
			
			break;
		case 5: // 검색
			printSearchSubMenu();
			getSubMenu = getMenuNumber();
			if (getSubMenu == 4)
				break;
			switch (getSubMenu) {
			case 1:
				int foundIndex = findWithName();
				printFoundName(foundIndex);
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
			System.out.println("프로그램을 종료 합니다");
			System.exit(0); // 정상적인 종료라고 0을 줌.
			break;
		default:
			System.out.println("메뉴에 없는 번호입니다");
		}//// switch
	}///////// separateMainMenu(int mainMenu)

	// 1] 인원 추가 메소드
	private void addMembers() throws FileNotFoundException, IOException {
		Scanner sc = new Scanner(System.in);
		String name;
		while (true) {
			System.out.println("인원의 이름을 입력하세요");
			name = sc.nextLine().trim();
			if(common.utility.CommonUtil.isKorean(name)) {}
			else {
				System.out.println("이 프로그램은 영문 이름을 지원하지 않습니다.");
				continue;}
			try {
				Integer.parseInt(name);
			} catch (NumberFormatException e) {
				break;
			}
			System.out.println("이름에 숫자는 없어요. ");
			continue;
		} ///// while
		System.out.println(String.format("%s님의 나이를 입력하세요", name));
		int age = Integer.parseInt(sc.nextLine().trim());
		System.out.println(String.format("%s님의 주소를 입력하세요", name));
		String addr = sc.nextLine().trim();
		System.out.println(String.format("%s님의 전화번호를 (010-xxxx-xxxx) 형식으로 입력하세요", name));
		String cont = sc.nextLine().trim();
		Person p = new Person(name, age, addr, cont);
		listPerson.add(p);

		char firstChar = CommonUtil.getJaeum(name);
		List<Person> listPerson = new ArrayList<>();
		if (!memberMap.containsKey(firstChar)) {
			listPerson = new ArrayList<>();
		} else {
			listPerson = memberMap.get(firstChar);
		}
		Object o = listPerson.add(new Person(name, age, addr, cont));
		memberMap.put(firstChar, listPerson);

		System.out.println(listPerson.add(p) ? p.name + "님이 추가되었습니다" : p.name + "님이 추가되지 못했습니다");
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
	private void editMember(int index, int subMenuIndex) {
		String revisedTitle = "";
		String revised="";
		Scanner sc = new Scanner(System.in);
		if(subMenuIndex == 1) {
			System.out.println("새로운 이름을 입력하세요");
			String newName = sc.nextLine().trim();
			listPerson.get(index).name = newName;
			revisedTitle ="이름";
			revised=newName;
		}
		else if(subMenuIndex == 2) {
			System.out.println("새로운 나이를 입력하세요");
			int newAge= Integer.parseInt(sc.nextLine().trim());
			listPerson.get(index).age = newAge;
			revisedTitle ="나이";
			revised=String.valueOf(newAge);
		}
		else if(subMenuIndex ==3) {
			System.out.println("새로운 주소를 입력하세요");
			String newAddr = sc.nextLine().trim();
			listPerson.get(index).addr = newAddr;
			revisedTitle ="주소";
			revised=newAddr;
		}
		else if(subMenuIndex ==4) {
			System.out.println("새로운 번호를 입력하세요");
			String newCont = sc.nextLine().trim();
			listPerson.get(index).cont = newCont;
			revisedTitle ="번호";
			revised=newCont;
		}
		System.out.println(String.format(
				"%s 의 %s가/이 %s 로 수정되었습니다",listPerson.get(index).name, revisedTitle, revised));
		
	}////////// editMemberName(int index)
	
	// 4] 삭제 메소드
	private void deleteMember(int indexEdit) {
		String deleteName = listPerson.get(indexEdit).name;
		listPerson.remove(indexEdit);
		System.out.println(String.format("%s 멤버의 정보가 삭제되었습니다", deleteName));
	}////////////////////deleteMember(int indexEdit)
	
	// 5] 검색 서브메뉴 출력용 메소드

	public void printSearchSubMenu() {
		System.out.println("======================검색 서브 메뉴======================");
		System.out.println("1.이름으로 검색 2. 주소로 검색 3. 전화번호로 검색 4.메인 메뉴로 이동");
		System.out.println("=======================================================");
	}//////// printSearchSubMenu()

	// [5-1] 이름으로 검색하는 메소드
	private int findWithName() {
		Scanner sc = new Scanner(System.in);
		String name;
		while(true) {
			System.out.println("찾으시는 멤버의 이름을 입력하세요");
			name = sc.nextLine().trim();
			if(common.utility.CommonUtil.isKorean(name)) {
				break;}
			else {
				System.out.println("이 프로그램은 영문 이름을 지원하지 않습니다.");
				continue;}
		}
		int index = 0;
		for (Person p : listPerson) {
			if (p.name.equals(name)) {
				index = listPerson.indexOf(p);
			} /// if
		} //// foreach
		return index;
	}////////////// findWithName()
	
	//5-1-1] 검색된 결과를 출력하는 메소드
	private void printFoundName(int index) {
		System.out.println(String.format("[%s 멤버 정보]", listPerson.get(index).name));
		listPerson.get(index).print();
	}

	// [5-2] 나이로 검색하는 메소드 //나이대로 검색 가능하게.
	private void findWithAge() {
		Scanner sc = new Scanner(System.in);
		System.out.println("어떤 나이로 검색하시겠습니까?");
		int age2 = Integer.parseInt(sc.nextLine().trim());
		System.out.println(String.format("[%s 세인 인원들 검색 결과]", age2));
		for (Person p : listPerson) {
			if (p.age == age2) {
				p.print();
			} /// if
		} //// foreach
	}//////// findWithSubject()

	// [5-3] 주소로 검색하는 메소드
	private void findWithAddress() {
		Scanner sc = new Scanner(System.in);
		System.out.println("어떤 학번으로 검색하시겠습니까?");
		String address = sc.nextLine().trim();
		System.out.println(String.format("[%s 에 사는 인원들]", address));
		for (Person p : listPerson) {
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
		for (Person p : listPerson) {
			if (p.cont.equals(contact)) {
				p.print();
			} /// if
		} //// foreach
	}//////// findWithContact()

	//6] 파일 저장 메소드.
	private void saveData() throws IOException {
		if(listPerson.isEmpty()) {
			System.out.println("저장하실 데이터가 없습니다.");
			return;
		}
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(
					new FileOutputStream("src/console/academy/member.txt"));
				out.writeObject(memberMap);
			System.out.println("저장되었습니다.");
		}
			
		catch(IOException e) {
			System.out.println("파일 저장 오류");
		}
		finally {
			if(out != null) out.close();
		}
	}

}///////// class
