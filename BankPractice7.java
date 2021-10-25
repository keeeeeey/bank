//	- 아이디, 패스워드 패턴 지정하기
//	- 아이디 중복확인 기능 추가
package bank;

import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BankPractice7 {
boolean delete;
boolean deleteCheck;
boolean run;
boolean login;
boolean idCheck;
boolean transferCheck;
List<Member> list;
Member session;
Member transferSession;
Scanner scanner;
int cnt;
String sql;
private Connection conn;
private Statement stmt;
private ResultSet rs;

public BankPractice7() {
	run = true;
	list = new Vector<>();
	scanner = new Scanner(System.in);
	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/app";
	try {
		Class.forName(driver);
		conn = DriverManager.getConnection(url, "root", "kp23156385@");
		stmt = conn.createStatement();
	} catch (Exception e) {
		e.printStackTrace();
	} 
//	finally {
//		try {
//			if (stmt != null) stmt.close();
//			if (conn != null) conn.close();
//		} catch (SQLException e) {}
//	}
}

public void saveDB() {
	try {
		sql = createMember();
		stmt.executeUpdate(sql);
	} catch (Exception e) {} 
} 

public void deleteDB() {
	try {
		sql = deleteMember();
		int result = stmt.executeUpdate(sql);
		System.out.println(result > 0 ? "회원탈퇴가 완료되었습니다. 안녕히 가세요." : "아이디 또는 패스워드가 일치하지 않습니다. 회원탈퇴에 실패하였습니다.");
	} catch (Exception e) {} 
}

public void updateDB() {
	try {
		sql = updateMember();
		stmt.executeUpdate(sql);
	} catch (Exception e) {}
}

public void selectDB() {
	try {
		sql = selectMember();
		rs = stmt.executeQuery(sql);
		while (rs.next()) {
			String name = rs.getString("NAME");
			String id = rs.getString("ID");
			String pw = rs.getString("PW");
			int balance = rs.getInt("BALANCE");
			String account_num = rs.getString("ACCOUNT_NUM");
			System.out.println("NAME : " + name + 
							   ", ID : " + id + 
							   ", PW : " + pw  + 
							   ", BALANCE : " + balance + 
							   ", ACCOUNT_NUM : " + account_num);
		}
	} catch (Exception e) {}
}

public String createMember() {
	System.out.print("회원이름 : ");
	String name = scanner.next();
	System.out.print("아이디 : ");
	String id = scanner.next();
	//아이디 중복확인
	System.out.print("암호 : ");
	String pw = scanner.next();
	//암호 확인
	System.out.print("잔고 : ");
	int balance = scanner.nextInt();
	
	String account_num = "110-" + (int)((Math.random() * 999) + 1) + "-" + (int)((Math.random() * 999999) + 1);  
	
	sql = "insert into BANK values('" + name + "', '" + id + "', '" + pw + "', '" + balance + "', '" + account_num + "')";
	
//	list.add(new Member(name, id, pw, balance, account_num));
	System.out.println("회원가입이 완료되었습니다." + name + "님의 계좌번호는 " + account_num + "입니다.");
	return sql;
}

public boolean logIn() {
	System.out.print("아이디 : ");
	String id = scanner.next();
	System.out.print("암호 : ");
	String pw = scanner.next();
	for (int i = 0; i < list.size(); i++) {
		if (id.equals(list.get(i).getId()) && pw.equals(list.get(i).getPw())) {
			login = true;
			session = list.get(i);
		} 
	}
	return login;			
}

public String selectMember() {
	sql = "select * from BANK";
	return sql;
}

public String deleteMember() {
	System.out.print("정말로 탈퇴하시겠습니까?(Y/N)");
	char answer;
	answer = scanner.next().charAt(0);
	if (answer == 'Y' || answer == 'y') {
		delete = true;
		System.out.print("아이디 : ");
		String id = scanner.next();
		System.out.print("암호 : ");
		String pw = scanner.next();
		sql = "DELETE FROM BANK WHERE ID = '" + id + "' && PW = '" + pw + "'";
	} else if (answer == 'N' || answer == 'n') {
		System.out.println("처음화면으로 돌아갑니다.");
		delete = false;
	} else {
		System.out.println("처음화면으로 돌아갑니다.");
		delete = false;
	}
	return sql;
}

public void deposit() {
	System.out.print("예금 : ");
	session.setBalance(session.getBalance() + scanner.nextInt());
}

public void withdraw() {
	System.out.print("출금 : ");
	int withdraw = scanner.nextInt();
	if (session.getBalance() < withdraw) {
		System.out.println("잔액이 부족합니다.");
	} else {
		session.setBalance(session.getBalance() - withdraw);
	}
}

public void balance() {
	System.out.println(session.getName() + "님의 잔고는 " + session.getBalance() + "원 입니다.");
}

public void transfer() {
	System.out.print("받는사람의 계좌번호(하이픈(-)을 포함하여 입력하시오.) : ");
	String account_num = scanner.next();
	for (int i = 0; i < list.size(); i++) {
		if (account_num.equals(list.get(i).getAccount_num())) {
			transferSession = list.get(i);
			transferCheck = true;
			break;
		} else {
			transferCheck = false;
		}
	}
	if (transferCheck) {
		System.out.print("보낼 금액 : ");
		int money = scanner.nextInt();
		if (session.getBalance() < money) {
			System.out.println("잔액이 부족합니다.");
		} else {
			System.out.println();
			session.setBalance(session.getBalance() - money);
			transferSession.setBalance(transferSession.getBalance() + money);
			System.out.println("계좌이체가 완료되었습니다. 송금 후 잔액 : " + session.getBalance());
		}
	} else {
		System.out.println("존재하지 않는 계좌번호입니다.");
	}
}

public String updateMember() {
	System.out.print("기존 비밀번호 : ");
	String pwOld = scanner.next();
	System.out.print("새 비밀번호 : ");
	String pwNew = scanner.next();
	System.out.print("새 비밀번호 확인 : ");
	String pwNew2 = scanner.next();
	if (pwNew == pwNew2) {
		sql = "UPDATE BANK SET PW = '" + pwNew + "' WHERE PW = '" + pwOld + "'  ";
	} else {
		System.out.println("비밀번호가 일치하지 않습니다.");
	}
	return sql;
}

public void menu() {
	do {
		System.out.println("-------------------------------------------------");
		System.out.println("1.회원가입 | 2.로그인 | 3.회원 리스트 | 4.회원탈퇴 | 5.종료");
		System.out.println("-------------------------------------------------");
		System.out.print("선택> ");
		int menuNum = scanner.nextInt();
		switch (menuNum) {
			case 1 :
				saveDB();
				break;
			case 2 :
				logIn();
				if (login) {
					System.out.println(session.getName() + "님 로그인 되었습니다.");
				} else {
					System.out.println("ID 또는 PassWord가 올바르지 않습니다.");
				}
				while (login) {
					System.out.println("어떤 업무를 도와드릴까요?");
					System.out.println("---------------------------------------------------------");
					System.out.println("1.예금 | 2.출금 | 3.잔고 | 4.계좌이체 | 5.회원정보 수정 | 6.로그아웃");
					System.out.println("---------------------------------------------------------");
					System.out.print("선택> ");
					int todoNum = scanner.nextInt();
					switch (todoNum) {
						case 1 :
							deposit();
							break;
						case 2 :
							withdraw();
							break;
						case 3 :
							balance();
							break;
						case 4 :
							transfer();
							break;
						case 5 :
							updateDB();
							break;
						case 6 :
							System.out.println("로그아웃 되었습니다.");
							login = false;
							break;
					}
				}
				break;
			case 3 :
				selectDB();
				break;
			case 4 :
				deleteDB();
				break;
			case 5 : 
				run = false;
				break;
			default :
				System.out.println("다시 입력하세요");
		}
		System.out.println();
	} while (run);
}

public static void main(String args[]) {
	BankPractice7 bankpractice7 = new BankPractice7();
	bankpractice7.menu();
	System.out.println("프로그램 종료");
}

}
