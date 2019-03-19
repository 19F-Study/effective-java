package kr._19fstudy.effective_java.ch12.item86;


import java.io.Serializable;

public class Product implements Serializable {
    private long id;
    private String name;

    public Product(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
