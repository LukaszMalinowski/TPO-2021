package zad1;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Futil {

    public static void processDir(String dirName, String resultFileName) {
        try {
            clearResultFile(resultFileName);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            FileChannel outChannel = new FileOutputStream(resultFileName).getChannel();
            final long[] previousPosition = {0};

            Files.walkFileTree(Paths.get(dirName), new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.getFileName().toString().equals(resultFileName))
                        return FileVisitResult.CONTINUE;

                    if (file.toString().endsWith(".txt")) {
                        FileChannel inChannel = new FileInputStream(file.toFile()).getChannel();
                        outChannel.transferFrom(inChannel, previousPosition[0], inChannel.size());
                        previousPosition[0] = outChannel.size();
                        inChannel.close();
                    }

                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });

            outChannel.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void clearResultFile(String resultFileName) throws IOException {
        Files.deleteIfExists(Paths.get(resultFileName));
        Files.createFile(Paths.get(resultFileName));
    }
}
