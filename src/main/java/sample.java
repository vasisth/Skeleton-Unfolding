import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class sample {


public static void main(String[] args) throws IOException {
    FileInputStream inputStream = null;
    String path= "/Users/narayanavasisthakandarpa/output.txt";
    Scanner sc = null;
    Set<String> set = new HashSet<>();
    try {
        inputStream = new FileInputStream(path);
        sc = new Scanner(inputStream, "UTF-8");
        while (sc.hasNextLine()) {
            String line = sc.nextLine();


            if(line.charAt(0)!='(')
                System.out.println(line);

            else
                set.add(line);

            if(set.size()%1000000==0) System.out.println(set.size());
        }
        // note that Scanner suppresses exceptions
        if (sc.ioException() != null) {
            throw sc.ioException();
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        if (inputStream != null) {
            inputStream.close();
        }
        if (sc != null) {
            sc.close();
        }
    }

}
}
