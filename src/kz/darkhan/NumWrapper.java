package kz.darkhan;

public class NumWrapper {
    private int num;
    private boolean isEven;

    public NumWrapper(int num) {
        this.num = num;
        this.isEven = num % 2 == 0;
    }

    public int getNum() {
        return num;
    }

    public boolean isEven() {
        return isEven;
    }
}
