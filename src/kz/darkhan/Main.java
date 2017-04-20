package kz.darkhan;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        if (args.length == 1) {
            try {
                Integer to = Integer.parseInt(args[0]);
                System.out.println(String.format("Вы передали аргумент %d", to));
                List<NumWrapper> numbers = new ArrayList<>();
                for (int i = 1; i <= to; i++) {
                    numbers.add(new NumWrapper(i * i));
                }

                for (NumWrapper n: numbers) {
                    System.out.println(n.getNum());
                }
            } catch (NumberFormatException e) {
                System.out.println(String.format("Аргумент %s не является целым числом", args[0]));
            }
        } else {
            System.out.println("Передайте одно целое число как аргумент");
        }
    }
}
