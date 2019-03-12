package kr._19fstudy.effective_java.ch3.item11;

import java.util.Objects;

public class PhoneNumber {
    private volatile int hashCode;

    private int areaCode;
    private int prefix;
    private int lineNum;

    PhoneNumber(int areaCode, int prefix, int lineNum) {
        this.areaCode = areaCode;
        this.prefix = prefix;
        this.lineNum = lineNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhoneNumber)) return false;
        PhoneNumber that = (PhoneNumber) o;
        return areaCode == that.areaCode &&
                prefix == that.prefix &&
                lineNum == that.lineNum;
    }

    // auto generate hashCode
    @Override
    public int hashCode() {
        return Objects.hash(areaCode, prefix, lineNum);
    }

    /*
    // 전형적인 hashCode
    @Override
    public int hashCode() {
        int result = Integer.hashCode(areaCode);
        result = 31 * result + Integer.hashCode(prefix);
        result = 31 * result + Integer.hashCode(lineNum);

        return result;
    }
    */

    /*
    // hashCode lazy initialization
    @Override
    public int hashCode() {
        int result = hashCode;

        if (result == 0) {
            result = 31 * result + Integer.hashCode(prefix);
            result = 31 * result + Integer.hashCode(lineNum);
            hashCode = result;
        }

        return result;
    }
    */
}
