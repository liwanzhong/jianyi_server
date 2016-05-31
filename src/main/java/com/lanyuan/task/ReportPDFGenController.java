package com.lanyuan.task;

import br.eti.mertz.wkhtmltopdf.wrapper.Pdf;
import br.eti.mertz.wkhtmltopdf.wrapper.page.PageType;
import br.eti.mertz.wkhtmltopdf.wrapper.params.Param;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.lanyuan.entity.PhysicalExaminationBigResultFormMap;
import com.lanyuan.entity.PhysicalExaminationRecordFormMap;
import com.lanyuan.mapper.PhysicalExaminationBigResultMapper;
import com.lanyuan.mapper.PhysicalExaminationRecordMapper;
import com.lanyuan.util.MergePDF;
import com.lanyuan.util.PropertiesUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
@Lazy(false)
public class ReportPDFGenController {

	@Inject
	private PhysicalExaminationRecordMapper physicalExaminationRecordMapper;

	@Inject
	private PhysicalExaminationBigResultMapper physicalExaminationBigResultMapper;


	private static Logger logger = Logger.getLogger(ReportPDFGenController.class);

	private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


	@Scheduled(cron = "1 * * * * ? ")
	public void task() throws Exception {
		logger.info("======================================定时任务生成pdf报告================start==============================================================");
		// 获取没有生成的列表
		PhysicalExaminationRecordFormMap physicalExaminationRecordFormMap = new PhysicalExaminationRecordFormMap();
		physicalExaminationRecordFormMap.put("status",2);
		//查询条件
		List<PhysicalExaminationRecordFormMap> physicalExaminationRecordFormMapList =physicalExaminationRecordMapper.findListWill2GenPdfReport(physicalExaminationRecordFormMap);

		if(CollectionUtils.isNotEmpty(physicalExaminationRecordFormMapList)){
			logger.info("======================================有【"+physicalExaminationRecordFormMapList.size()+"】条检测记录需要生成报告==============================================================");
			// 迭代生成pdf报告
			for(PhysicalExaminationRecordFormMap item:physicalExaminationRecordFormMapList){
				item.put("status",3);
				item.put("update_time",dateTimeFormat.format(new Date()));
				physicalExaminationRecordMapper.editEntity(item);
				String pdfPath = null;
				try{
					pdfPath = reportGen(item);
				}catch (Exception ex){
					ex.printStackTrace();
				}
				item.put("status",4);
				item.put("report_gentime",dateTimeFormat.format(new Date()));
				item.put("update_time",dateTimeFormat.format(new Date()));
				item.put("report_path",pdfPath);
				physicalExaminationRecordMapper.editEntity(item);

			}

		}
		logger.info("======================================定时任务生成pdf报告================end==============================================================");
	}




	public String reportGen(PhysicalExaminationRecordFormMap physicalExaminationRecordFormMap)throws Exception{
		//获取当前检测记录的所有检测大项(排序方式)
		PhysicalExaminationBigResultFormMap physicalExaminationBigResultFormMap = new PhysicalExaminationBigResultFormMap();
		physicalExaminationBigResultFormMap.put("examination_record_id",physicalExaminationRecordFormMap.getLong("id"));
		List<PhysicalExaminationBigResultFormMap> physicalExaminationBigResultFormMapList = physicalExaminationBigResultMapper.findByNames(physicalExaminationBigResultFormMap);
		if(CollectionUtils.isNotEmpty(physicalExaminationBigResultFormMapList)){
			List<InputStream> pdfs = new ArrayList<InputStream>();
			//todo 循环拼接url
			//生成首页pdf
			StringBuffer  httpUrl = new StringBuffer(PropertiesUtils.findPropertiesKey(PropertiesUtils.REPORT_URL_PDF_GEN_MAIN));
			httpUrl.append("?");
			httpUrl.append("physicalExaminationRecordFormMap.id=").append(physicalExaminationRecordFormMap.getLong("id"));

			StringBuffer pdfFilePath = new StringBuffer(PropertiesUtils.findPropertiesKey(PropertiesUtils.REPORT_PDF_SAVED_PAHT));
			pdfFilePath.append(File.separator);
			pdfFilePath.append(dateFormat.format(physicalExaminationRecordFormMap.getDate("check_time")));
			pdfFilePath.append(File.separator);
			pdfFilePath.append(physicalExaminationRecordFormMap.getLong("id"));
			File savePathFile = new File(pdfFilePath.toString());
			if(!savePathFile.exists()||!savePathFile.isDirectory()){
				savePathFile.mkdirs();
			}


			//调用wkhtmltopdf将html转为pdf
//			pdfs.add(new FileInputStream(url2PdfConverter(httpUrl.toString(),pdfFilePath.toString(),"0.pdf")));
			pdfs.add(new FileInputStream(url2PdfConverter(httpUrl.toString(),pdfFilePath.toString(),"0.png")));

			for(int i=0;i< physicalExaminationBigResultFormMapList.size();i++){
				httpUrl = new StringBuffer(PropertiesUtils.findPropertiesKey(PropertiesUtils.REPORT_URL_PDF_GEN_ITEM));
				httpUrl.append("?");
				httpUrl.append("bigItemId=").append(physicalExaminationBigResultFormMapList.get(i).getLong("big_item_id")).append("&").append("recordId=").append(physicalExaminationRecordFormMap.getLong("id"));
//				pdfs.add(new FileInputStream(url2PdfConverter(httpUrl.toString(),pdfFilePath.toString(),physicalExaminationBigResultFormMapList.get(i).getLong("big_item_id")+".pdf")));
				pdfs.add(new FileInputStream(url2PdfConverter(httpUrl.toString(),pdfFilePath.toString(),physicalExaminationBigResultFormMapList.get(i).getLong("big_item_id")+".png")));
			}

			// 合并pdf文件到一个文件中
			String mgrgePdfFilePath = pdfFilePath.append(File.separator).append(physicalExaminationRecordFormMap.getLong("id")+"_marge.pdf").toString();
			File  file = new File(mgrgePdfFilePath);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}

			OutputStream output = new FileOutputStream(mgrgePdfFilePath);
			MergePDF.concatPDFs(pdfs, output, true);

		}
		return physicalExaminationRecordFormMap.getLong("id")+"_marge.pdf";
	}



	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

	public String url2PdfConverter(String httpUrl,String saveAsPdfPath,String fileName) throws Exception{
		StringBuffer pngFilePath = new StringBuffer(saveAsPdfPath);
		pngFilePath.append(File.separator);
		pngFilePath.append(fileName);

		Pdf pdf = new Pdf();
		pdf.addParam(new Param("--enable-javascript"));
		pdf.addParam(new Param("--no-stop-slow-scripts"));
//		pdf.addParam(new Param("--javascript-delay 4000"));
		pdf.addParam(new Param("--javascript-delay"));
		pdf.addParam(new Param("4000"));
		pdf.addPage(httpUrl, PageType.url);
		File pngFile = pdf.saveAs(pngFilePath.toString());
		StringBuffer pdfFilePath  = new StringBuffer(saveAsPdfPath);
		pdfFilePath.append(File.separator);
		pdfFilePath.append(fileName+".pdf");

		//调用itext将图片转为pdf文件(仅支持一张图片放在一张pdf上)
		convertPNG2PDF(pdfFilePath.toString(),pngFile.getAbsolutePath());
		return pdfFilePath.toString();
	}


	/**
	 * 将图片转换为pdf文件
	 * @param PDF PDF文件路径
	 * @param IMAGE 图片路径
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public void convertPNG2PDF(String PDF,String IMAGE) throws IOException, DocumentException {
		Document document = new Document(PageSize.A4.rotate());
		PdfWriter.getInstance(document, new FileOutputStream(PDF));
		document.open();
//        document.add(new Paragraph("Berlin!"));
        /*PdfContentByte canvas = writer.getDirectContentUnder();
        Image image = Image.getInstance(IMAGE);
        image.scaleAbsolute(PageSize.A4.rotate());
        image.setAbsolutePosition(0, 0);*/
		Image image = Image.getInstance(IMAGE);
		image.scaleAbsolute(PageSize.A4.rotate());//设置图片的绝对位置（即PDF的A4的大小，就是正好覆盖A4的整个页面）
//		image.scaleAbsolute(PageSize.A4.rotate().getWidth(),PageSize.A4.rotate().getHeight());
		image.setAbsolutePosition(0, 0);//设置图片放在PDF的起始位置
		document.add(image);
		document.close();

	}
}