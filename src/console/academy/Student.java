package console.academy;

public class Student extends Person {
	//[멤버변수]
	public String stNumber;//새롭게 확장한 멤버
	//[인자 생성자]
	public Student(String name, int age,String stNumber) {
		super(name, age);
		this.stNumber = stNumber;
	}////////////////////
	//[멤버 메소드]
	@Override
	String get() {		
		return String.format("%s,학번:%s",super.get(),stNumber);
	}//////////
	@Override
	void print() {
		System.out.println(get());
	}/////////
	
}///////////////
