package common.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CommonUtil {
	//[문자열이 숫자 형식이면 true, 아니면 false 반환]
	public static boolean isNumber(String value) {
		for(int i=0; i<value.length();i++) {
			int codeValue=Character.codePointAt(value, i);
			if(!(codeValue>='0' && codeValue<='9')) return false;	
		}
		return true;
	}//////////isNumber
	
	//두 날짜 차이를 반환하는 메소드
	//반환타입:long
	//매개변수:String타입의 두 날짜, 날짜패턴,구분자(단위) 
	
	public static long getDiffBetweenDates(String stFDate, String stSDate, String pattern, char delim) throws ParseException {
		//1] 매개변수에 전달된 pattern으로 SimpleDateFormat 객체생성
		SimpleDateFormat dateFormat =  new SimpleDateFormat(pattern);
		//2] String -> Date: parse()
			Date fDate = dateFormat.parse(stFDate); //fDate라는 데이트 클래스 멤버 변수를 선언 그리고 parse로 초기화
			Date sDate = dateFormat.parse(stSDate);
				
		//3]시간차 구하기:getTime()
			long fTime = fDate.getTime(); //ms단위로 1970년 1월1일 00:00:00 GMT 부터 시간 받아오기. 
			long sTime = sDate.getTime(); //왜 저 때냐고? 저때로 설정이 되어있다. epoch가 그거야. 
			long diff = Math.abs(fTime-sTime);
				
		//4]매개변수 delim에 따른 날짜 차이 변환
			switch(Character.toUpperCase(delim)) {
				case 'D': return diff/1000/60/60/24;
				case 'H': return diff/1000/60/60;
				case 'M': return diff/1000/60;
				default: return diff/1000;
				}
	}///////getDiffBetweenDates

	//이름에서 자음을 구해 반환하는 메소드]
	public static char getJaeum(String name) {
		//김길동->ㄱ, 박길동->ㅂ, 홍길동->ㅎ
		char [] jaeum = name.toCharArray();
		// 방법1]
		/*
		if (jaeum[0] >= '가' && jaeum[0] < '나')
			return 'ㄱ';
		else if (jaeum[0] >= '나' && jaeum[0] < '다')
			return 'ㄴ';
		else if (jaeum[0] >= '다' && jaeum[0] < '라')
			return 'ㄷ';
		else if (jaeum[0] >= '라' && jaeum[0] < '마')
			return 'ㄹ';
		else if (jaeum[0] >= '마' && jaeum[0] < '바')
			return 'ㅁ';
		else if (jaeum[0] >= '바' && jaeum[0] < '사')
			return 'ㅂ';
		else if (jaeum[0] >= '사' && jaeum[0] < '아')
			return 'ㅅ';
		else if (jaeum[0] >= '아' && jaeum[0] < '자')
			return 'ㅇ';
		else if (jaeum[0] >= '자' && jaeum[0] < '차')
			return 'ㅈ';
		else if (jaeum[0] >= '차' && jaeum[0] < '카')
			return 'ㅊ';
		else if (jaeum[0] >= '카' && jaeum[0] < '타')
			return 'ㅋ';
		else if (jaeum[0] >= '타' && jaeum[0] < '파')
			return 'ㅌ';
		else if (jaeum[0] >= '파' && jaeum[0] < '하')
			return 'ㅍ';
		else if (jaeum[0] >= '하' && jaeum[0] <= '힣')
			return 'ㅎ'; */
		
		//방법2]
		char [] startChar = {'가','나','다','라','마','바','사','아','자','차','카','타','파','하'};
		char [] endChar = {'낗','닣','띻','맇','밓','삫','앃','잏','찧','칳','킿','팋','핗','힣'};
		char [] returnChar = {'ㄱ','ㄴ','ㄷ','ㄹ','ㅁ','ㅂ','ㅅ','ㅇ','ㅈ','ㅊ','ㅋ','ㅌ','ㅍ','ㅎ'};
		
		for(int i=0;i<startChar.length;i++)
			if(jaeum[0] >= startChar[i] && jaeum[0] <= endChar[i])
				return returnChar[i];
		return '0'; //자음이 아닌 경우
	}
	
	//문자열이 한국어로 써졌는지 아닌지 확인하는 메소드
	public static boolean isKorean(String name) {
		char check [] = name.toCharArray();
		for(int i=0;i<check.length;i++) {
			if(!(check[0] >= '가' && check[0] <= '힣'))
				return false;}
		return true;
	}////////////isKorean()
	
	public static String inputName() {
		Scanner sc = new Scanner(System.in);
		String name;
		while (true) {
			System.out.println("멤버의 이름을 입력하세요");
			name = sc.nextLine().trim();
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

	public static int inputAge(String name) {
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println(String.format("%s님의 나이를 입력하세요", name));
			String ageStr = sc.nextLine().trim();
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
	public static String inputAddr(String name) {
		Scanner sc = new Scanner(System.in);
		String addr;
		while (true) {
			System.out.println(String.format("%s님의 주소를 (xx동) 입력하세요", name));
			addr = sc.nextLine().trim();
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
	public static String inputCont(String name) {
		Scanner sc = new Scanner(System.in);
		String cont;
		while(true) {
			System.out.println(String.format("%s님의 전화번호를 (010-xxxx-xxxx) 형식으로 입력하세요", name));
			cont = sc.nextLine().trim();
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
	
}///////class
