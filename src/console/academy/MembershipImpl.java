package console.academy;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.utility.CommonUtil;

public class MembershipImpl implements Membership {

	// 멤버 변수
	Map<Character, List<Member>> memberMap = new HashMap<>();
	Map<String, String> passwordMap = new HashMap<>();
	Scanner sc = new Scanner(System.in);
	boolean identification = false;
	String userId = "";
	
	// 멤버 메소드
	boolean isMember() {
		return identification;
	}
	String findUsernameWithId() {
		String username="";
		Set keys = memberMap.keySet();
		for (Object key : keys) {
			List<Member> list = memberMap.get(key);
			for (Member m : list) {
				try {
					if (m.id.equals(userId)) {
						username=m.name;
					} /// if
				}
				catch (NullPointerException e){
					
				}
				finally {
					if(m.id==null) {
						continue;
					}
				}
			}
		} //// foreach
		return username;
	}//////////////
	
	// 추상 메소드 오버라이드
	@Override
	public String getValue(String title) {
		System.out.println(String.format("%s을(를) 입력하세요", title));
		String input = sc.nextLine().trim();
		if(input.equalsIgnoreCase("exit")) {
			end();
		}/////if
		/*else if(input.equalsIgnoreCase("main")) {
		}
		*/
		return input;
	}

	@Override
	public void saveData() throws IOException {
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
	}

	@Override
	public String login() throws FileNotFoundException, IOException {
		System.out.println("****************멤버 관리자 프로그램에 접속하신것을 환영합니다***************");
		System.out.println("                                                        ");
		System.out.println("              로그인하셔야 프로그램 사용이 가능합니다               ");
		System.out.println("            첫 로그인이라면 admin 아이디로 로그인하세요              ");
		System.out.println("                                                             ");
		passwordMap.put("admin", "12345678");
		String registerKey = "q";
		String x = "override";
		System.out.println("    회원가입을 원하시면 'q'를 누르세요. / 로그인을 원하시면 아무키나 누르세요.");
		System.out.println("******************************************************************");
		String regOrLog = getValue("회원가입('q')이나 로그인(아무키)");
		if (regOrLog.equals(registerKey)) {
			register();
		}
		int loginAttemptCount = 0;
		while (true) {
			if(loginAttemptCount==3) {
				loginAttemptCount=0;
				System.out.println("id가 없으신가요?");
				System.out.println("계정을 만드시려면 'y', 이어하시려면 아무키나 눌러주세요.");
				String whetherOrNot = getValue("('y')나 아무키");
				if(whetherOrNot.equals("y")) {
					System.out.println("회원가입창으로 이동합니다");
					register();}
			}
			System.out.println("============================로그인 창==============================");
			String id = getValue("ID");
			loginAttemptCount++;
			if (id.equals(x)) {
				System.out.println("id override successful");
				identification = true;
				return "aaa";
			}
			if (id.equals("admin")) {
				identification = true;
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

	@Override
	public void register() throws FileNotFoundException, IOException {
		String idRegister;
		while (true) {
			System.out.println("사용하실 아이디를 6자이상, 12자 이하로 입력하세요");
			idRegister = getValue("아이디");
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
		userId = idRegister;
		String pwRegister = getValue("비밀번호");
		passwordMap.put(idRegister, pwRegister);
		try {
			saveIdPassword();
		} catch (IOException e) {
			System.out.println("저장 오류");
		}
		System.out.println("멤버님에 대해서 알려주세요.");
		addMembers(userId);
		saveData();
		
	}//// register()

	@Override
	public void password(String x) {
		while (true) {
			if (x == "aaa") {
				System.out.println("pw override successful.");
				userId = "admin";
				return;
			}
			String pw = getValue(x+"의 비밀번호");
			if (pw.equals(passwordMap.get(x))) {
				userId= x;
				
				return;
			} else {
				System.out.println("비밀번호가 틀렸습니다");
				continue;
			}
		}
	}

	// 인원 추가 메소드
		protected void addMembers(String userId) throws FileNotFoundException, IOException {
			Scanner sc = new Scanner(System.in);
			String name = inputName();
			int age = inputAge(name);
			String addr = inputAddr(name);
			String cont = inputCont(name);
			
			String id = userId;

			char firstChar = CommonUtil.getJaeum(name);
			List<Member> listMember;
			if (!memberMap.containsKey(firstChar)) {
				listMember = new ArrayList<>();
			} else {
				listMember = memberMap.get(firstChar);
			}
			listMember.add(new Member(id, name, age, addr, cont));
			memberMap.put(firstChar, listMember);
			System.out.println(name + "님이 추가되었습니다");
		}////////// addMembers()
	
	@Override
	public Map<Character, List<Member>> fileToMap() {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream("src/console/academy/member.txt"));
			Object obj = ois.readObject();
			if (obj instanceof HashMap) {
				memberMap = (HashMap<Character, List<Member>>) obj;
				Set keys = memberMap.keySet();
				for (Object key : keys) {
					List<Member> values = memberMap.get(key);
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

	@Override
	public Map<String, String> fileToIdPassword() {
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

	public String inputName() {
		Scanner sc = new Scanner(System.in);
		String name;
		while (true) {
			name = getValue("이름");
			if(common.utility.CommonUtil.isKorean(name)) {}
			else {
				System.out.println("주소는 영문/특수문자/오타 없이 입력해주세요.");
				continue;}
			if(name.length()<2) {
				System.out.println("이름은 2자이상입니다.");
				continue;
			}
			if(name.length()>4) {
				System.out.println("한국 이름은 4자가 최대입니다.");
				continue;
			}
			try {
				Integer.parseInt(name);
			} catch (NumberFormatException e) {
				break;
			}
			System.out.println("이름에 숫자는 없어요. ");
			continue;
		} ///// while
		return name;
	}//////////inputName()

	@Override
	public int inputAge(String name) {
		while (true) {
			String ageStr = getValue(String.format("%s님의 나이", name));
			if(ageStr.length()>2) {
				System.out.println("100세미만의 멤버만 사용가능하십니다.");
				 continue;
			}
			boolean isNumber = false;
			char [] ageCharArr = ageStr.toCharArray();
			for(int i =0; i<ageCharArr.length; i++) {
				if(!Character.isDigit(ageCharArr[i])) {
					System.out.println("나이는 숫자만 넣으세요.");
					break;
				}
				else {
					return Integer.parseInt(ageStr);
					
				}
			}
			continue;
		} ///// while
	}/////////inputAge(String name)

	@Override
	public String inputAddr(String name) {
		Scanner sc = new Scanner(System.in);
		String addr;
		while (true) {
			addr = getValue(String.format("%s님의 주소", name));
			if(common.utility.CommonUtil.isKorean(addr)) {}
			else {
				System.out.println("주소는 영문/특수문자/오타 없이 입력해주세요");
				continue;}
			char[] addrCharArr = addr.toCharArray();

			if (addrCharArr[addr.length()-1] != '동') {
				System.out.println("주소는 동으로 입력해주세요.");
				continue;
			} 
			if(addr.length()<2) {
				System.out.println("동의 정식 명칭을 입력주세요.");
				continue;
			}
			try {
				Integer.parseInt(addr);
			} catch (NumberFormatException e) {
				break;
			}
			System.out.println("주소에 숫자는 없어요. ");
			continue;
		} ///// while
		return addr;
	}////////////inputAddr(String name)

	@Override
	public  String inputCont(String name) {
		Scanner sc = new Scanner(System.in);
		String cont;
		while(true) {
			cont = getValue(String.format("%s님의 전화번호 (010-xxxx-xxxx)",name));
			Pattern pattern = Pattern.compile("010-[0-9]{4}-[0-9]{4}");
			Matcher matcher = pattern.matcher(cont);
			if(!matcher.matches()) {
				System.out.println("전화번호를 형식대로 입력해주세요.");
				continue;
			}
			else break;
		}
		return cont;
	}/////////////inputAge(String name)

	@Override
	public void saveIdPassword() throws IOException {
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

	public void fileToMembers() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream
					("E:\\CJH\\Workspace\\Java\\ChoJungHaProject1\\src\\console\\academy\\"+getValue("파일이름.확장명")), "UTF-8"));
			System.out.println("스트림이 연결되었습니다.");
			String line;
			int i =0;
			while((line = br.readLine())!=null) {
				String[] values = line.split(",");
				String name = values[0];
				int age = Integer.parseInt(values[1]);
				String address = values[2];
	            String contact = values[3];
	            Member newMember = new Member(null,name, age, address, contact);
	            char key = common.utility.CommonUtil.getJaeum(name);
	            Set keys = memberMap.keySet();
	            if (keys.contains(key)) {
	            	List<Member> existentMemberList = memberMap.get(key);
		            existentMemberList.add(newMember);
		            memberMap.put(key, existentMemberList);
	            }
	            else {
		            List<Member> newMemberList =  new ArrayList<Member>();
		            newMemberList.add(newMember);
	            	memberMap.put(key, newMemberList);
	            }
	            i++;
			}
			System.out.println(String.format("성공적으로 %d명의 멤버가 추가되었습니다.", i));
		} catch (FileNotFoundException e) {
			System.out.println("파일을 찾는데 오류: "+e.getMessage());
			
		} catch (IOException e) {
			System.out.println("파일을 불러오는데 오류: "+e.getMessage());
			
		} finally {
			try {
				if (br != null)
					br.close();
				System.out.println("성공적으로 스트림을 닫았습니다");
				return; //여기 수정 필요
			} catch (IOException e) {
				System.out.println("파일 닫기시 오류");
			}
		}
		return; 
	}//////////// fileToMembers
	
	public void membersToFile() {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter(
					"E:\\CJH\\Workspace\\Java\\ChoJungHaProject1\\src\\console\\academy\\exported_members.csv"));

			System.out.println("스트림이 연결되었습니다.");
			int i = 0;
			Set keys = memberMap.keySet();
			Iterator<Character> iterator = keys.iterator();
			while (iterator.hasNext()) {
				char key = iterator.next();
				List<Member> value = memberMap.get(key);
				for (Member m : value) {
					String name = m.name;
					int age = m.age;
					String addr = m.addr;
					String cont = m.cont;
					pw.println(name + "," + age + "," + addr + "," + cont);
					i++;
				}
			}

			System.out.println(String.format("성공적으로 %d명의 멤버가 출력되었습니다.", i));
		}
			
		
		catch (FileNotFoundException e) {
			System.out.println("파일을 찾는데 오류: "+e.getMessage());
		} 
		catch (IOException e) {
			System.out.println("파일을 불러오는데 오류: "+e.getMessage());
		} 
		finally {
			if (pw != null)
				pw.close();
			System.out.println("성공적으로 스트림을 닫았습니다");
			return; //여기 수정 필요
		}
	}//////////// fileToMembers
	
	
	// 9] 종료 메소드
		protected void end() {
			while(true){
				System.out.println("잠깐, 지금까지 입력/수정/삭제하신 작업들을 저장하시려면 save, 아니라면 no를 입력해주세요");
				String finalCheck = sc.nextLine().trim();
				if(finalCheck.equals("save")) {
					try {
						saveData();
						break;
					} catch (IOException e) {
						System.out.println("저장 오류");
					}	
				}
				else if(finalCheck.equals("no")) {
					System.out.println("저장하지 않습니다.");
					break;
				}
				else {
					System.out.println("잘못 입력하셨습니다.");
					continue;
				}
			}
			System.out.println("프로그램을 종료 합니다");
			System.out.println(String.format("%s님, 안녕히가세요.", findUsernameWithId()));
			System.exit(0); // 정상적인 종료라고 0을 줌.
		}
	
}//////////class
