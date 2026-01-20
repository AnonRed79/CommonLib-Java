import net.anonhub.projects.dragonlib.ConfigParser;

import java.nio.file.Path;
import java.nio.file.Paths;


public class Main {
    public static final Path basePath = Paths.get("./resources");

    public static void main(String[] args) {
        ConfigParser cp = new ConfigParser(basePath+"/assets/.cfg");
        System.out.println(cp.getShortMulti("actions/moveUp"));
    }
}
