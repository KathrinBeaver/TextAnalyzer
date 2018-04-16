/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.files_utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * класс для создания выборок
 *
 * @author Sergey
 */
public final class FilesUtils {

    private static final double PERCENT_OF_TEST_DOC = 30;// процент документов, который будет перенесен

    private FilesUtils() {
    }

    public static void main(String[] args) throws IOException {
        createDocsForTest(new File("E:\\DataForClassifier\\RootFolderSize250\\DocForLearning"), new File("E:\\DataForClassifier\\RootFolderSize250\\DocForTest"));

    }

    /**
     * создает тестовую выборку аналогичной структуры, перенося около 30% из
     * directoryWithAllDocs в directoryWithDocsforTest
     *
     * @param directoryWithAllDocs
     * @param directoryWithDocsforTest
     * @throws java.io.IOException
     */
    public static void createDocsForTest(File directoryWithAllDocs, File directoryWithDocsforTest) throws IOException {
        if (!directoryWithAllDocs.exists() || !directoryWithAllDocs.isDirectory()) {
            throw new RuntimeException(directoryWithAllDocs.getPath() + " dont exist or not directory");
        }
        if (!directoryWithDocsforTest.exists() || !directoryWithDocsforTest.isDirectory()) {
            throw new RuntimeException(directoryWithDocsforTest.getPath() + " dont exist or not directory");
        }

        for (File topicDir : directoryWithAllDocs.listFiles((file) -> {
            return file.isDirectory();
        })) {
            File testTopicDir = new File(directoryWithDocsforTest + "\\" + topicDir.getName());
            if (!testTopicDir.exists()) {
                Files.createDirectory(testTopicDir.toPath());
            }
            long amountTestDoc = Math.round((double) topicDir.listFiles().length * (PERCENT_OF_TEST_DOC / 100));
            for (File file : topicDir.listFiles()) {
                if (file.isDirectory()) {
                    throw new RuntimeException(topicDir.getPath() + " directory can't contains folder");
                }
                if (!file.renameTo(new File(testTopicDir, file.getName()))) {
                    throw new RuntimeException(" error while moving the file: " + file);
                }
                amountTestDoc--;
                if (amountTestDoc <= 0) {
                    break;
                }
            }
        }
    }

}
