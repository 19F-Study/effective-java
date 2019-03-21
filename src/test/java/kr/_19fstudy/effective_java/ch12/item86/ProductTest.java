package kr._19fstudy.effective_java.ch12.item86;

import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class ProductTest {
    private static final String FILE_NAME = "serialized.txt";

    @Test
    public void serialize() throws Exception {
        Product product = new Product(123L, "생수");
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(product);
            byte[] serialized = baos.toByteArray();
            writeToFile(serialized);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deserialize() throws Exception {
        byte[] serialized = readFromFile();
        try (ByteArrayInputStream bais = new ByteArrayInputStream(serialized);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            Object objectMember = ois.readObject();
            Product product = (Product) objectMember;
            System.out.println(product);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeToFile(byte[] message) throws Exception {
        Files.deleteIfExists(Paths.get(FILE_NAME));
        FileOutputStream stream = new FileOutputStream(FILE_NAME);
        stream.write(message);
    }

    private byte[] readFromFile() throws IOException {
        return Files.readAllBytes(Paths.get(FILE_NAME));
    }
}