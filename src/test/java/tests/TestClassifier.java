/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import com.mai.textanalyzer.word_processing.RusUTF8FileLabelAwareIterator;
import com.mai.textanalyzer.indexing.tf_idf.TfIIdfUtils;
import com.mai.textanalyzer.indexing.tf_idf.TfIdfTextModel;
import java.io.File;
import org.deeplearning4j.bagofwords.vectorizer.TfidfVectorizer;
import org.deeplearning4j.text.documentiterator.LabelledDocument;

/**
 *
 * @author Sergey
 */
public class TestClassifier {

    /**
     * Main method.
     *
     */
//    @Test
    public void test() throws Exception {
        System.out.println("Start vectorizer create");
        TfidfVectorizer vectorizer = TfIIdfUtils.createModel();
        System.out.println("End vectorizer create");

        File folderWithDataForLearning = new File("G:\\DocForTest\\DataForLearning");
        RusUTF8FileLabelAwareIterator tearchingIterator = new RusUTF8FileLabelAwareIterator.Builder()
                .addSourceFolder(folderWithDataForLearning)
                .build();

        LabelledDocument first = tearchingIterator.next();
        System.out.println("Start TfIdfTextModel create");
        TfIdfTextModel modelExample = new TfIdfTextModel(first.getLabel(), vectorizer.transform(first.getContent()));
        System.out.println("End TfIdfTextModel create");

        System.out.println("Start classifier create");
        TestWekaClassifier classifier = new TestWekaClassifier(modelExample, tearchingIterator.getLabelsSource().getLabels());
        System.out.println("End classifier create");
        System.out.println("Start classifier updateModel");
        int count = 0;
        int size = tearchingIterator.getSize();
        while (tearchingIterator.hasNext()) {
            LabelledDocument next = tearchingIterator.next();
            TfIdfTextModel model = new TfIdfTextModel(next.getLabel(), vectorizer.transform(next.getContent()));
            classifier.updateModel(model);
            count++;
            System.out.println(count + "/" + size);
        }
        System.out.println("End classifier updateModel");

        File folderWithDataForTest = new File("G:\\DocForTest\\DataForTestModel");
        RusUTF8FileLabelAwareIterator testingIterator = new RusUTF8FileLabelAwareIterator.Builder()
                .addSourceFolder(folderWithDataForTest)
                .build();

        int guessCount = 0;
        while (testingIterator.hasNextDocument()) {
            LabelledDocument next = testingIterator.next();
            String topic = classifier.classifyMessage(vectorizer.transform(next.getContent()));
            System.out.println("RealTopic: " + topic + " ClassTopic: " + next.getLabel());///!!!!!!!!!!!!!!!!!!!!!!!!!!
            if (topic.equals(next.getLabel())) {
                guessCount++;
            }
        }
        System.out.println("AllSizeDoc: " + testingIterator.getSize() + " GuessTopic: " + guessCount);

//        TestWekaClassifier messageCl;
//        byte[] charArray;
//        try {
//// Read message file into string.
//            String messageFileString = Utils.getOption('m', options);
//            if (messageFileString.length() != 0) {
//                FileInputStream messageFile = new FileInputStream(messageFileString);
//                int numChars = messageFile.available();
//
//                charArray = new byte[numChars];
//                messageFile.read(charArray);
//                messageFile.close();
//            } else {
//                throw new Exception("Name of message file not provided.");
//            }
//// Check if class value is given.
//            String classValue = Utils.getOption('c', options);
//// Check for model file. If existent, read it, otherwise create new
//// one.
//            String modelFileString = Utils.getOption('t', options);
//            if (modelFileString.length() != 0) {
//                try {
//                    FileInputStream modelInFile = new FileInputStream(modelFileString);
//                    ObjectInputStream modelInObjectFile = new ObjectInputStream(modelInFile);
//                    messageCl = (TestWekaClassifier) modelInObjectFile.readObject();
//                    modelInFile.close();
//                } catch (FileNotFoundException e) {
//                    messageCl = new TestWekaClassifier();
//                }
//            } else {
//                throw new Exception("Name of data file not provided.");
//            }
//// Check if there are any options left
//            Utils.checkForRemainingOptions(options);
//// Process message.
//            if (classValue.length() != 0) {
//                messageCl.updateModel(new String(charArray), classValue);
//            } else {
//                messageCl.classifyMessage(new String(charArray));
//            }
//// If class has been given, updated message classifier must be saved
//            if (classValue.length() != 0) {
//                FileOutputStream modelOutFile
//                        = new FileOutputStream(modelFileString);
//                ObjectOutputStream modelOutObjectFile
//                        = new ObjectOutputStream(modelOutFile);
//                modelOutObjectFile.writeObject(messageCl);
//                modelOutObjectFile.flush();
//                modelOutFile.close();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
