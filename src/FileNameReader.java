import java.io.File;
import java.util.ArrayList;
public class FileNameReader {

    ArrayList<String> getName(String path) {
        ArrayList<String> name = new ArrayList<>();
        File[] files = new File(path).listFiles();
        for (File file : files) {
            if (file.isFile()) {
                    name.add(file.getName());
            }
        }
        return name;
    }
}
