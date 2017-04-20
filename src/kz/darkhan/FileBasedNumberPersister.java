package kz.darkhan;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class FileBasedNumberPersister extends AbstractNumbersPersister {

    private static FileBasedNumberPersister instance = null;

    private FileBasedNumberPersister() {
    }

    public static FileBasedNumberPersister getInstance() {
        if (instance == null) {
            instance = new FileBasedNumberPersister();
        }
        return instance;
    }

    @Override
    public boolean persist(List<NumWrapper> numbers) {
        String filename = "output.txt";
        try {
            PrintWriter writer = new PrintWriter(filename, "UTF-8");
            for (NumWrapper n : numbers) {
                writer.println(n.getNum() + "," + n.isEven());
            }
            writer.close();
            return true;
        } catch (IOException e) {
            System.err.println("IOException while writing to file " + filename);
            e.printStackTrace();
            return false;
        }
    }
}
