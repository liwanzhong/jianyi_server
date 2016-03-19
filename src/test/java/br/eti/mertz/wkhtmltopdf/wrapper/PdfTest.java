package br.eti.mertz.wkhtmltopdf.wrapper;

import br.eti.mertz.wkhtmltopdf.wrapper.page.PageType;
import br.eti.mertz.wkhtmltopdf.wrapper.params.Param;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.junit.Test;

import java.io.ByteArrayInputStream;

public class PdfTest {

    /*@Test
    public void testCommand() throws Exception {
        Pdf pdf = new Pdf();
        pdf.addToc();
        pdf.addParam(new Param("--enable-javascript"), new Param("--html-header", "file:///example.html"));
        pdf.addPage("http://www.google.com", PageType.url);
        System.out.println(pdf.getCommand());;
    }

    @Test
    public void testPdfFromStringTo() throws Exception {

        // GIVEN a html template containing special characters that java stores in utf-16 internally
        Pdf pdf = new Pdf();
        pdf.addPage("<html><head><meta charset=\"utf-8\"></head><h1>contetns</h1></html>", PageType.htmlAsString);
        pdf.addToc();
        pdf.addParam(new Param("--enable-javascript"), new Param("--html-header", "file:///example.html"));
        pdf.saveAs("output.pdf");

        // WHEN
        byte[] pdfBytes = pdf.getPDF();

        PDFParser parser = new PDFParser(new ByteArrayInputStream(pdfBytes));

        // that is a valid PDF (otherwise an IOException occurs)
        parser.parse();
        PDFTextStripper pdfTextStripper = new PDFTextStripper();
        String pdfText = pdfTextStripper.getText(new PDDocument(parser.getDocument()));

        System.out.println(pdfText);


    }*/
}
