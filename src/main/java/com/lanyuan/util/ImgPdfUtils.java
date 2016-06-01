package com.lanyuan.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfWriter;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by liwanzhong on 2016/5/30.
 */
public class ImgPdfUtils {

    public static final String IMAGE = "D:\\idea-workspack\\works\\test13.png";
    public static final String DEST = "D:\\idea-workspack\\works\\background_transparent.pdf";




    public static void BackgroundImage(String dest) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
//        document.add(new Paragraph("Berlin!"));
        PdfContentByte canvas = writer.getDirectContentUnder();
        Image image = Image.getInstance(IMAGE);
//        image.scaleAbsolute(PageSize.A4.rotate());
        image.setScaleToFitHeight(true);
        image.setAbsolutePosition(0, 0);
        canvas.addImage(image);
        document.close();
    }



    public static void BackgroundTransparent(String dest) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        document.add(new Paragraph("Berlin!"));
        PdfContentByte canvas = writer.getDirectContentUnder();
        Image image = Image.getInstance(IMAGE);
        image.scaleAbsolute(PageSize.A4.rotate());
        image.setAbsolutePosition(0, 0);
        canvas.saveState();
        PdfGState state = new PdfGState();
        state.setFillOpacity(0.6f);
        canvas.setGState(state);
        canvas.addImage(image);
        canvas.restoreState();
        document.close();
    }

    public  static void genImgToPdf(String mImgPath,String mFileFullPath)throws Exception{
        Document tDoc = new Document(PageSize.A4, 50, 50, 50, 50); //创建文档
        PdfWriter tWriter = PdfWriter.getInstance(tDoc, new FileOutputStream(mFileFullPath)); //创建写入流
        tWriter.setEncryption(null,"jianyi".getBytes(), PdfWriter.ALLOW_SCREENREADERS|PdfWriter.ALLOW_PRINTING,PdfWriter.STANDARD_ENCRYPTION_128); //加密
        tDoc.open();  //打开文档
        Image tImgCover = Image.getInstance(mImgPath+"cover.jpg");
        tImgCover.setAlignment(Image.UNDERLYING);
            /* 设置图片的位置 */
        tImgCover.setAbsolutePosition(0, 0);
            /* 设置图片的大小 */
        tImgCover.scaleAbsolute(595, 842);
        tDoc.add(tImgCover);             //加载图片
    }


    public static void main(String[] args)throws Exception{
        /*String mImagepath = "D:\\idea-workspack\\works\\test13.png";
        String mFileFullPath = "D:\\idea-workspack\\works\\test13_pdf.pdf";
        genImgToPdf(mImagepath,mFileFullPath);*/

        File file = new File(DEST);
        file.getParentFile().mkdirs();
//        BackgroundTransparent(DEST);

        BackgroundImage(DEST);
    }


}
