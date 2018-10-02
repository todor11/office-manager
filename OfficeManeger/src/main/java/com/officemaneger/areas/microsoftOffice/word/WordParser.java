package com.officemaneger.areas.microsoftOffice.word;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;

import java.io.*;
import java.util.List;

public class WordParser {

    private static final String FILE_NAME = "enquiry.docx";
    private static final String BLANK_FILE_NAME = "blank.docx";

    public static void writeWordFile(String newFilePath, List<String> textParagraphs) {
        File folder = new File(newFilePath);
        File[] listOfFiles = folder.listFiles();

        boolean hasWordBlank = false;
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    if (file.getName().equals(BLANK_FILE_NAME)) {
                        hasWordBlank = true;
                        break;
                    }
                }
            }
        }

        if (hasWordBlank) {
            writeFullDocument(newFilePath, textParagraphs);
        } else {
            writeDocumentWithoutHeader(newFilePath, textParagraphs);
        }
    }

    private static void writeFullDocument(String fullPath, List<String> textParagraphs) {
        String blankFile = fullPath + BLANK_FILE_NAME;
        String resultFile = fullPath + FILE_NAME;

        XWPFDocument doc = null;
        try {
            doc = new XWPFDocument(OPCPackage.open(blankFile));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        List<XWPFParagraph> paragraphs = doc.getParagraphs();
        boolean isFinish = false;
        for (int i = 0; i < paragraphs.size(); i++) {
            if (isFinish) {
                break;
            }
            XWPFParagraph p = paragraphs.get(i);
            List<XWPFRun> runs = p.getRuns();
            if (runs != null) {
                for (XWPFRun r : runs) {
                    String text = r.getText(0);
                    if (text != null && text.contains("[text]")) {
                        r.setText("", 0);
                        XmlCursor cursor = p.getCTP().newCursor();
                        for (int j = textParagraphs.size() - 1; j >= 0; j--) {
                            XWPFParagraph paragraph = doc.insertNewParagraph(cursor);
                            XWPFRun run=paragraph.createRun();
                            //CTR ctr = run.getCTR();
                            //ctr.addNewTab();
                            run.setText(textParagraphs.get(j));
                            run.setFontSize(14);
                            cursor = paragraph.getCTP().newCursor();
                        }
                        isFinish = true;
                        break;
                    }
                }
            }
        }
        try {
            doc.write(new FileOutputStream(resultFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeDocumentWithoutHeader(String newFilePath, List<String> textParagraphs) {
        XWPFDocument document= new XWPFDocument();
        //Write the Document in file system
        try {
            FileOutputStream out = new FileOutputStream(new File(newFilePath + FILE_NAME));
            //create Paragraph
            for (int i = 0; i < textParagraphs.size(); i++) {
                XWPFParagraph paragraph = document.createParagraph();
                XWPFRun run=paragraph.createRun();
                //CTR ctr = run.getCTR();
                //ctr.addNewTab();
                run.setText(textParagraphs.get(i));
                run.setFontSize(14);
            }

            document.write(out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

