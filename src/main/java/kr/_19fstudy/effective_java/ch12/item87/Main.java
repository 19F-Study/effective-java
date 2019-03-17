package kr._19fstudy.effective_java.ch12.item87;

import java.io.*;

public class Main {
    private static final String FILE_PATH = "/Users/summerb/Desktop/test/member.txt";

    public static void main(String[] args) {
        Member member = new Member("Summer", "Seller Settlements", 20);

        try {
            serialize(member);
            Object deSerializedMember = deSerialize();
            System.out.println(deSerializedMember);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    static void serialize(Member member) throws IOException {
        FileOutputStream fos = new FileOutputStream(FILE_PATH);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(member);
        oos.close();
    }

    static Object deSerialize() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(FILE_PATH);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object member = (Member) ois.readObject();
        ois.close();

        return member;
    }
}
