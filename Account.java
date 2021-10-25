package bank;

public class Account {
	private String name;
	private String id;
	private String pw;
	private int balance;
	private String account_num;
	
	public Account(String name, String id, String pw, int balance, String account_num) {
		this.name = name;
		this.id = id;
		this.pw = pw;
		this.balance = balance;
		this.account_num = account_num;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	public String getAccount_num() {
		return account_num;
	}
	public void setAccount_num(String account_num) {
		this.account_num = account_num;
	}
}
