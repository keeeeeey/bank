package bank;

public class MemberServiceEx {

	public static void main(String args[]) {
		MemberService memberService = new MemberService();
		boolean result = memberService.login("abc", "123");
		if (result) {
			System.out.println("로그인 되었습니다.");
		} else {
			System.out.println("ID 또는 PassWord가 올바르지 않습니다.");
		}
		memberService.logout("abc");
	}
	
}
