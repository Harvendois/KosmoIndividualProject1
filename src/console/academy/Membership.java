package console.academy;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface Membership {
	//멤버 상수
	
	//추상 메소드
	String getValue(String title);
	void saveData() throws IOException;
	void saveIdPassword() throws IOException;
	String login() throws FileNotFoundException, IOException;
	void register() throws FileNotFoundException, IOException;
	void password(String x);
	Map<Character, List<Member>> fileToMap();
	Map<String, String> fileToIdPassword();
	String inputName();
	int inputAge(String name);
	String inputAddr(String name);
	String inputCont(String name);
}
