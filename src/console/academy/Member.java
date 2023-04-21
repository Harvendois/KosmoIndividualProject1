package console.academy;

import java.io.Serializable;

public class Member implements Comparable<Member>, Serializable{
	//[멤버변수]
	public String name;
	public int age;
	public String addr;
	public String cont; 
	public String id;
	//[기본 생성자]
	public Member() {}
	//[인자 생성자]
	public Member(String id, String name, int age) {		
		this.id = id;
		this.name = name;
		this.age = age;
	}/////////////////
	
	public Member(String id, String name, int age, String address, String contact) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.addr = address;
		this.cont = contact;
	}
	//[멤버 메소드]
	String get() {
		return String.format("아이디:%-12s,이름:%-2s, 나이:%s, 연락처:%s, 주소:%s",id, name,age,cont,addr);
	}
	void print() {
		System.out.println(get());
	}
	@Override
	public int compareTo(Member target) {
		return this.name.compareTo(target.name);
	}

}///////////////////////
