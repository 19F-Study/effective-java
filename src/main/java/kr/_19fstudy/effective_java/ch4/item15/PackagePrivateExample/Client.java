package kr._19fstudy.effective_java.ch4.item15.PackagePrivateExample;

public class Client {
	public static void main(String[] args) {
		PackagePrivate packagePrivate = new PackagePrivate();
		System.out.println(packagePrivate.example());
	}
}
