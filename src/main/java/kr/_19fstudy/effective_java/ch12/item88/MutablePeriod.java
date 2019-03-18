package kr._19fstudy.effective_java.ch12.item88;

import kr._19fstudy.effective_java.ch12.item87.Member;

import java.io.*;
import java.util.Date;

public class MutablePeriod {
    public final Period period;
    public final Date start;
    public final Date end;

    public MutablePeriod() {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

            Period periodForSerialize = new Period(new Date(), new Date());
            System.out.println(periodForSerialize);

            objectOutputStream.writeObject(periodForSerialize);

            byte[] ref = {0x71, 0, 0x7e, 0, 5};
            byteArrayOutputStream.write(ref);
            ref[4] = 4;
            byteArrayOutputStream.write(ref);

            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));

            period = (Period) objectInputStream.readObject();
            start = (Date) objectInputStream.readObject();
            end = (Date) objectInputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new AssertionError(e);
        }
    }

    public static void main(String[] args) {
        MutablePeriod mutablePeriod = new MutablePeriod();
        Period period = mutablePeriod.period;
        Date pEnd = mutablePeriod.end;

        pEnd.setYear(78);
        System.out.println(period);


        pEnd.setYear(69);
        System.out.println(period);
    }
}
