package console.academy;

import java.io.Serializable;

public class Person implements Comparable<Person>, Serializable{
	//[멤버변수]
	public String name;
	public int age;
	public String addr;
	public String cont; 
	//[기본 생성자]
	public Person() {}
	//[인자 생성자]
	public Person(String name, int age) {		
		this.name = name;
		this.age = age;
	}/////////////////
	
	public Person(String name, int age, String address, String contact) {
		this.name = name;
		this.age = age;
		this.addr = address;
		this.cont = contact;
	}
	//[멤버 메소드]
	String get() {
		return String.format("이름:%s,나이:%s,연락처:%s,주소:%s",name,age,cont,addr);
	}
	void print() {
		System.out.println(get());
	}
	@Override
	public int compareTo(Person target) {
		return this.name.compareTo(target.name);
	}

}///////////////////////
