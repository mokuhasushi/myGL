package myGL;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Utils {
    public static String readFile(String fileName) {
        try {
            return Files.readString(Path.of(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return Path.of(fileName).toString();
        }
    }
    public static void main (String [] args) {
        System.out.println(readFile("src/main/resources/lesson1.frag"));
    }
}
