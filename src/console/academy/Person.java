package console.academy;

import java.io.Serializable;
import java.sql.Date;

public class Person implements Comparable<Person>, Serializable{
	//[멤버변수]
	public String name;
	public int age;
	public String id;
	public int salary;
	public String department;
	public String position;
	public Date hireDate;
	//[기본 생성자]
	public Person() {}
	//[인자 생성자]
	public Person(String id, String name, int age, String addr, String cont) {
		this.id = id;
		this.name = name;
		this.age = age;
	}
	public Person(String name, int age, String id, int salary, String department,
			String position, String rank, Date hireDate) {
		this.name = name;
		this.age = age;
		this.id = id;
		this.salary = salary;
		this.department = department;
		this.position = position;
		this.hireDate = hireDate;
	}
	//[멤버 메소드]
	String get() {
		return String.format("아이디:%-12s이름:%-2s, 나이:%s, 연봉:%s, 부서:%s, 직급:%s, 입사일:%s",id, name,age,salary, department, position, hireDate);
	}
	void print() {
		System.out.println(get());
	}
	@Override
	public int compareTo(Person target) {
		return this.name.compareTo(target.name);
	}

}///////////////////////
