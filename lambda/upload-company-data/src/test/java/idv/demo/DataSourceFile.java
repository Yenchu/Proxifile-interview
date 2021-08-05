package idv.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DataSourceFile implements DataSource<byte[]> {

    private String file;

    public DataSourceFile(String file) {
        this.file = file;
    }

    @Override
    public byte[] extract() {
        Path path = Paths.get(file);
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
