package bank;

public class MemberService {
	public Member member;
	public MemberService() {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		System.out.print("회원이름 : ");
		String name = scanner.next();
		System.out.print("아이디 : ");
		String id = scanner.next();
		System.out.print("암호 : ");
		String pw = scanner.next();
		System.out.print("잔고 : ");
		int balance = scanner.nextInt();
//		member = new Member(name, id, pw, balance);
	}
	public boolean login(String id, String pw) {
		boolean result = false;
		if(id.equals(member.getId()) && pw.equals(member.getPw())) {
			System.out.println(member.getName() + "님");
			result = true;
		} 
		return result;
	}
	public void logout(String id) {
		System.out.println("로그아웃 되었습니다.");
	}
}
