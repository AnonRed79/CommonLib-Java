import net.anonhub.commonlib.ConfigParser;
import net.anonhub.commonlib.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static net.anonhub.commonlib.ConfigParser.DATA_TYPE_CHAR;

public class Main {
    public static final Path basePath = Paths.get("./resources");

    public static void main(String[] args) {
        Logger logger = new Logger("./resources/logs");
        logger.log("yolo");
    }
}
