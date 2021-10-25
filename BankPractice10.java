//	- 아이디, 패스워드 패턴 지정하기
package bank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

public class BankPractice10 {
	boolean delete;
	boolean deleteCheck;
	boolean run;
	boolean login;
	boolean idCheck;
	boolean pwCheck;
	boolean transferCheck;
	int cnt;
	String sql;
	String address;
	
	List<Member> list;
	Member session;
	Member transferSession;
	Scanner scanner;

	private Connection conn;
	private Statement stmt;
	private ResultSet rs;
	private ResultSetMetaData rsmd;
	private PreparedStatement pstmt;
	
	public BankPractice10() {
		run = true;
		list = new Vector<>();
		scanner = new Scanner(System.in);
		conn = DBActionBank.getInstance().getConnection();
	}
	
	public void load() {
		try {
			sql = "SELECT * FROM BANK";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			rsmd = rs.getMetaData();
			int cols = rsmd.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= cols; i++) {
					rs.getString(i);
				}
				Member member = new Member(rs.getString(1), 
										   rs.getString(2), 
										   rs.getString(3), 
										   Integer.parseInt(rs.getString(4)), 
										   rs.getString(5),
										   rs.getString(6));
				list.add(member);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void save() {
		try {
			for (int i = 0; i < list.size(); i++) {
				sql = "UPDATE BANK SET PW = '" + list.get(i).getPw() + "', BALANCE = '" + list.get(i).getBalance() + "' WHERE ID = '" + list.get(i).getId() + "' ";
				stmt = conn.createStatement();
				stmt.executeUpdate(sql);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				} 
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void deleteVector() {
		for (int i = 0; i < list.size(); i++) {
			list.remove(i);
		}
	}
	
	public void createDB() {
		try {
			sql = createMember();
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				} 
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	} 
	
	public void deleteDB() {
		try {
			sql = deleteMember();
			stmt = conn.createStatement();
			int result = stmt.executeUpdate(sql);
			System.out.println(result > 0 ? "회원탈퇴가 완료되었습니다. 안녕히 가세요." : "아이디 또는 패스워드가 일치하지 않습니다. 회원탈퇴에 실패하였습니다.");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void selectDB() {
		try {
			sql = "select * from BANK";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			rsmd = rs.getMetaData();
			int cols = rsmd.getColumnCount();
			for (int i = 1; i <= cols; i++) {
				String columnName = rsmd.getColumnName(i);
				System.out.print(columnName + "\t");
			}
			System.out.println();
			while (rs.next()) {
				for (int i = 1; i <= cols; i++) {
					System.out.print(rs.getString(i) + "\t");
				}
				System.out.println();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				} 
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
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
	
	public String createMember() {
		System.out.print("회원이름 : ");
		String name = scanner.next();
		System.out.print("아이디 : ");
		String id = scanner.next();
		//아이디 중복확인
		for (int i = 0; i < list.size(); i++) {
			if (id.equals(list.get(i).getId())) {
				System.out.println("이미 존재하는 아이디입니다.");
				idCheck = false;
				break;
			} else {
				idCheck = true;
			}
		}
		while (!idCheck) {
			System.out.print("아이디 : ");
			id = scanner.next();
			//아이디 중복확인
			for (int i = 0; i < list.size(); i++) {
				if (id.equals(list.get(i).getId())) {
					System.out.println("이미 존재하는 아이디입니다.");
					idCheck = false;
					break;
				} else {
					idCheck = true;
				}
			}
		}
		System.out.println("사용 가능한 아이디입니다.");
		System.out.print("암호 : ");
		String pw = scanner.next();
		//암호 확인
		System.out.print("암호확인 : ");
		String pw2 = scanner.next();
		if (pw.equals(pw2)) {
			System.out.println("암호가 일치합니다.");
			pwCheck = true;
		} else {
			System.out.println("암호가 일치하지 않습니다.");
			pwCheck = false;
		}
		while (!pwCheck) {
			System.out.print("암호 : ");
			pw = scanner.next();
			//암호 확인
			System.out.print("암호확인 : ");
			pw2 = scanner.next();
			if (pw.equals(pw2)) {
				System.out.println("암호가 일치합니다.");
				pwCheck = true;
			} else {
				System.out.println("암호가 일치하지 않습니다.");
				pwCheck = false;
			}
		}
		System.out.print("잔고 : ");
		int balance = scanner.nextInt();
		selectAddress();
		
		String account_num = "110-" + (int)((Math.random() * 999) + 1) + "-" + (int)((Math.random() * 999999) + 1);  
		
		sql = "INSERT INTO BANK VALUES('" + name + "', '" + id + "', '" + pw + "', '" + balance + "', '" + account_num + "', '" + address + "')";
		
		System.out.println("회원가입이 완료되었습니다." + name + "님의 계좌번호는 " + account_num + "입니다.");
		return sql;
	}
	
	public void selectAddress() {
		StringBuilder sb = new StringBuilder();
		System.out.println("주소지 검색");
		System.out.println("거주하는 읍/면/동을 입력하시오.");
		String dong = scanner.next();
		try {
			String sql = "SELECT * FROM ZIPCODE WHERE DONG = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dong);
			rs = pstmt.executeQuery();
			rsmd = rs.getMetaData();
			int cols = rsmd.getColumnCount();
			for (int i = 1; i <= cols; i++) {
				String columnName = rsmd.getColumnName(i);
				System.out.print(columnName + "\t");
			}
			System.out.println();
			int add_num = 1;
			String zipcode = "";
			while (rs.next()) {
				System.out.print(add_num + ". ");
				for (int i = 1; i <= cols; i++) {
					System.out.print(rs.getString(i) + "\t");
				}
				add_num++;
				System.out.println();
			}
			System.out.print("번호 : ");
			int num = scanner.nextInt();
			rs = pstmt.executeQuery();
			rsmd = rs.getMetaData();
			int idx = 1;
						
			while (rs.next()) {
				if (idx == num) {
					sb.append("(").
					   append(rs.getString("ZIPCODE")).
					   append(")").
					   append(rs.getString("SIDO")).
					   append(" ").
					   append(rs.getString("GUGUN")).
					   append(" ").
					   append(rs.getString("DONG")).
					   append(" ").
					   append(rs.getString("RI")).
					   append(" ").
					   append(rs.getString("BLDG")).
					   append(" ");
					zipcode = rs.getString("ZIPCODE");
				}
				idx++;
			}
			
			System.out.println("우편번호 : " + zipcode);
			System.out.print("상세주소 : ");
			scanner.nextLine();
			String detail = scanner.nextLine();
			sb.append(detail);
			address = sb.toString();
			System.out.println(address);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

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
			System.out.println("올바른 입력값이 아닙니다.");
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
			System.out.println("출금 후 잔액 : " + session.getBalance());
		}
	}
	
	public void balance() {
		System.out.println(session.getName() + "님의 잔액는 " + session.getBalance() + "원 입니다.");
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
	
	public void updateMember() {
		System.out.print("기존 비밀번호 : ");
		String pw = scanner.next();
		if (pw.equals(session.getPw())) {
			System.out.print("새 비밀번호 : ");
			String pwNew = scanner.next();
			System.out.print("새 비밀번호 확인 : ");
			String pwNew2 = scanner.next();
			if (pwNew.equals(pwNew2)) {
				session.setPw(pwNew);
				System.out.println("비밀번호가 변경되었습니다.");
			} else {
				System.out.println("비밀번호가 일치하지 않습니다.");
			}
		} else {
			System.out.println("비밀번호가 올바르지 않습니다.");
		}
	}
	
	public void menu() {
		do {
			deleteVector();
			load();
			System.out.println("-------------------------------------------------");
			System.out.println("1.회원가입 | 2.로그인 | 3.회원 리스트 | 4.회원탈퇴 | 5.종료");
			System.out.println("-------------------------------------------------");
			System.out.print("선택> ");
			int menuNum = scanner.nextInt();
			switch (menuNum) {
				case 1 :
					createDB();
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
								updateMember();
								break;
							case 6 :
								save();
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
		BankPractice10 bankpractice10 = new BankPractice10();
		bankpractice10.menu();
		System.out.println("프로그램 종료");
	}

}
