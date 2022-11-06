package htmlEditor;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class HTMLFileFilter extends FileFilter {
    @Override
    public boolean accept(File file) {
        Path path = file.toPath();
        String fileName = file.getName().toLowerCase();
        if (file.isDirectory()) {
            return true;
        } else {
            if (fileName.endsWith(".html") || fileName.endsWith(".htm")) {
                return true;
            } else {
                return false;
            }
        }

    }

    @Override
    public String getDescription() {
        return "HTML и HTM файлы";
    }
}
