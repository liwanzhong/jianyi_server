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
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;


@Component
@Lazy(false)
public class ReportPDFGenController {

	@Inject
	private PhysicalExaminationRecordMapper physicalExaminationRecordMapper;

	@Inject
	private PhysicalExaminationBigResultMapper physicalExaminationBigResultMapper;


	private static Logger logger = Logger.getLogger(ReportPDFGenController.class);

	private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


	//	@Scheduled(cron = "0 0/5 * * * ? ")
	public void task() throws Exception {
		logger.info("======================================定时任务生成pdf报告================start==============================================================");
		// 获取没有生成的列表
		PhysicalExaminationRecordFormMap physicalExaminationRecordFormMap = new PhysicalExaminationRecordFormMap();
		physicalExaminationRecordFormMap.put("status",2);
		physicalExaminationRecordFormMap.put("before_minute",-10);
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




	/*public String reportGen(PhysicalExaminationRecordFormMap physicalExaminationRecordFormMap)throws Exception{
		String margePdfPath = null;
		//获取当前检测记录的所有检测大项(排序方式)
		PhysicalExaminationBigResultFormMap physicalExaminationBigResultFormMap = new PhysicalExaminationBigResultFormMap();
		physicalExaminationBigResultFormMap.put("examination_record_id",physicalExaminationRecordFormMap.getLong("id"));
		List<PhysicalExaminationBigResultFormMap> physicalExaminationBigResultFormMapList = physicalExaminationBigResultMapper.findByNames(physicalExaminationBigResultFormMap);
		if(CollectionUtils.isNotEmpty(physicalExaminationBigResultFormMapList)){
			List<InputStream> pdfs = new ArrayList<InputStream>();
			//todo 循环拼接url


			StringBuffer pdfFilePath = new StringBuffer(PropertiesUtils.findPropertiesKey(PropertiesUtils.REPORT_PDF_SAVED_PAHT));
			pdfFilePath.append(File.separator);
			if(physicalExaminationRecordFormMap.get("check_time") instanceof  java.util.Date){
				pdfFilePath.append(dateFormat.format(physicalExaminationRecordFormMap.getDate("check_time")));
			}else{
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				pdfFilePath.append(dateFormat.format(simpleDateFormat.parse(physicalExaminationRecordFormMap.get("check_time").toString())));
			}


			pdfFilePath.append(File.separator);
			pdfFilePath.append(physicalExaminationRecordFormMap.getLong("id"));
			File savePathFile = new File(pdfFilePath.toString());
			if(!savePathFile.exists()||!savePathFile.isDirectory()){
				savePathFile.mkdirs();
			}



			//todo 加入多线程执行
			// 创建线程池
			final  ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());


			//首页
			StringBuffer  httpUrl = new StringBuffer(PropertiesUtils.findPropertiesKey(PropertiesUtils.REPORT_URL_PDF_GEN_MAIN));
			httpUrl.append("?");
			httpUrl.append("physicalExaminationRecordFormMap.id=").append(physicalExaminationRecordFormMap.getLong("id"));
			final ListenableFuture<Map<Long,String>> firstPageFuture = executorService.submit(new GenPdfTask(httpUrl.toString(),pdfFilePath.toString()+File.separator+"0.png",pdfFilePath.toString()+File.separator+"0.pdf",null));




			final Map<Long,String> itemMaps = new HashMap<Long, String>();
			List<Long> bigItemIdList = new ArrayList<Long>();
			//检查打项
			for(int i=0;i< physicalExaminationBigResultFormMapList.size();i++){
				httpUrl = new StringBuffer(PropertiesUtils.findPropertiesKey(PropertiesUtils.REPORT_URL_PDF_GEN_ITEM));
				httpUrl.append("?");
				httpUrl.append("bigItemId=").append(physicalExaminationBigResultFormMapList.get(i).getLong("big_item_id")).append("&").append("recordId=").append(physicalExaminationRecordFormMap.getLong("id"));
				final ListenableFuture<Map<Long,String>> itemPageFuture = executorService.submit(new GenPdfTask(httpUrl.toString(),pdfFilePath.toString()+File.separator+physicalExaminationBigResultFormMapList.get(i).getLong("big_item_id")+".png",pdfFilePath.toString()+File.separator+physicalExaminationBigResultFormMapList.get(i).getLong("big_item_id")+".pdf",physicalExaminationBigResultFormMapList.get(i).getLong("big_item_id")));

				bigItemIdList.add(physicalExaminationBigResultFormMapList.get(i).getLong("big_item_id"));
				itemMaps.put(physicalExaminationBigResultFormMapList.get(i).getLong("big_item_id"),"0");

				Futures.addCallback(itemPageFuture, new FutureCallback<Map<Long,String>>() {
					public void onSuccess(Map<Long,String> pdfPathMap) {
						for (Map.Entry<Long, String> entryItem : pdfPathMap.entrySet()) {
							itemMaps.put(entryItem.getKey(),entryItem.getValue());
						}

					}
					public void onFailure(Throwable thrown) {
						try {
							throw thrown;
						} catch (Throwable throwable) {
							throwable.printStackTrace();
						}
					}
				});

			}


			try {
				Map<Long,String> pdfPathMap = firstPageFuture.get();
				if(pdfPathMap !=null){
					for (Map.Entry<Long, String> entryItem : pdfPathMap.entrySet()) {
						pdfs.add(new FileInputStream(entryItem.getValue()));
					}
				}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			} catch (ExecutionException e1) {
				e1.printStackTrace();
			}
			while (true){
				Thread.sleep(300);
				boolean isWait = false;
				for (Map.Entry<Long, String> entryItem : itemMaps.entrySet()) {
					if(StringUtils.equalsIgnoreCase(entryItem.getValue(),"0")){
						isWait = true;
						break;
					}
				}
				if(!isWait){
					break;
				}
			}

			for(Long item:bigItemIdList){
				for (Map.Entry<Long, String> entryItem : itemMaps.entrySet()) {
					if(entryItem.getKey().longValue() == item.longValue()){
						pdfs.add(new FileInputStream(entryItem.getValue()));
						continue;
					}
				}
			}



			final String sickRiskPdfPathBuffer = pdfFilePath.toString();
			// 合并pdf文件到一个文件中
			String mgrgePdfFilePath = pdfFilePath.append(File.separator).append(physicalExaminationRecordFormMap.getLong("id")+"_marge.pdf").toString();
			File  file = new File(mgrgePdfFilePath);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}

			OutputStream output = new FileOutputStream(mgrgePdfFilePath);
			MergePDF.concatPDFs(pdfs, output, true);
			margePdfPath = file.getAbsolutePath();


			// 疾病风险(疾病风险pdf报告只是生成了pdf文件，没有做保存，如果需要下载，直接通过规则路径下载)
			final PhysicalExaminationRecordFormMap temp = physicalExaminationRecordFormMap;

			new Thread(new Runnable() {
				public void run() {
					try{
						StringBuffer httpUrl = new StringBuffer(PropertiesUtils.findPropertiesKey(PropertiesUtils.REPORT_URL_PDF_GEN_SYN));
						httpUrl.append("?");
						httpUrl.append("physicalExaminationRecordFormMap.id=").append(temp.getLong("id"));
						genReportPdfWithImg(httpUrl.toString(), sickRiskPdfPathBuffer+File.separator+"sick_risk.png",sickRiskPdfPathBuffer+File.separator+"sick_risk.pdf");
					}catch (Exception ex){
						logger.error(ex.getMessage());
						ex.printStackTrace();
					}
				}
			}).start();

		}
		if(StringUtils.isNotBlank(margePdfPath)){
			margePdfPath = margePdfPath.replace('\\','/');
		}
		return margePdfPath;
	}*/



	public String reportGen(PhysicalExaminationRecordFormMap physicalExaminationRecordFormMap)throws Exception{
		String margePdfPath = null;
		//获取当前检测记录的所有检测大项(排序方式)
		PhysicalExaminationBigResultFormMap physicalExaminationBigResultFormMap = new PhysicalExaminationBigResultFormMap();
		physicalExaminationBigResultFormMap.put("examination_record_id",physicalExaminationRecordFormMap.getLong("id"));
		List<PhysicalExaminationBigResultFormMap> physicalExaminationBigResultFormMapList = physicalExaminationBigResultMapper.findByNames(physicalExaminationBigResultFormMap);
		if(CollectionUtils.isNotEmpty(physicalExaminationBigResultFormMapList)){
			List<InputStream> pdfs = new ArrayList<InputStream>();
			//todo 循环拼接url


			StringBuffer pdfFilePath = new StringBuffer(PropertiesUtils.findPropertiesKey(PropertiesUtils.REPORT_PDF_SAVED_PAHT));
			pdfFilePath.append(File.separator);
			if(physicalExaminationRecordFormMap.get("check_time") instanceof  java.util.Date){
				pdfFilePath.append(dateFormat.format(physicalExaminationRecordFormMap.getDate("check_time")));
			}else{
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				pdfFilePath.append(dateFormat.format(simpleDateFormat.parse(physicalExaminationRecordFormMap.get("check_time").toString())));
			}


			pdfFilePath.append(File.separator);
			pdfFilePath.append(physicalExaminationRecordFormMap.getLong("id"));
			File savePathFile = new File(pdfFilePath.toString());
			if(!savePathFile.exists()||!savePathFile.isDirectory()){
				savePathFile.mkdirs();
			}

			List<Map<String,String>> itemUrlList = new ArrayList<Map<String, String>>();
			//首页
			StringBuffer  httpUrl = new StringBuffer(PropertiesUtils.findPropertiesKey(PropertiesUtils.REPORT_URL_PDF_GEN_MAIN));
			httpUrl.append("?");
			httpUrl.append("physicalExaminationRecordFormMap.id=").append(physicalExaminationRecordFormMap.getLong("id"));
			Map<String,String> firstPageMap = new HashMap<String, String>();
			firstPageMap.put("REQ_URL",httpUrl.toString());
			firstPageMap.put("PNG_ABS_PATH",pdfFilePath.toString()+File.separator+"0.png");
			firstPageMap.put("PDF_ABS_PATH",pdfFilePath.toString()+File.separator+"0.pdf");
			itemUrlList.add(firstPageMap);



			//检测大项
			for(int i=0;i< physicalExaminationBigResultFormMapList.size();i++){
				httpUrl = new StringBuffer(PropertiesUtils.findPropertiesKey(PropertiesUtils.REPORT_URL_PDF_GEN_ITEM));
				httpUrl.append("?");
				httpUrl.append("bigItemId=").append(physicalExaminationBigResultFormMapList.get(i).getLong("big_item_id")).append("&").append("recordId=").append(physicalExaminationRecordFormMap.getLong("id"));

				Map<String,String> itemPageMap = new HashMap<String, String>();
				itemPageMap.put("REQ_URL",httpUrl.toString());
				itemPageMap.put("PNG_ABS_PATH",pdfFilePath.toString()+File.separator+physicalExaminationBigResultFormMapList.get(i).getLong("big_item_id")+".png");
				itemPageMap.put("PDF_ABS_PATH",pdfFilePath.toString()+File.separator+physicalExaminationBigResultFormMapList.get(i).getLong("big_item_id")+".pdf");
				itemUrlList.add(itemPageMap);
			}

			/*//疾病风险项 todo 暂时屏蔽
			StringBuffer sickRiskhttpUrl = new StringBuffer(PropertiesUtils.findPropertiesKey(PropertiesUtils.REPORT_URL_PDF_GEN_SYN));
			sickRiskhttpUrl.append("?");
			sickRiskhttpUrl.append("physicalExaminationRecordFormMap.id=").append(physicalExaminationRecordFormMap.getLong("id"));
			Map<String,String> sickRiskPageMap = new HashMap<String, String>();
			sickRiskPageMap.put("REQ_URL",sickRiskhttpUrl.toString());
			sickRiskPageMap.put("PNG_ABS_PATH",pdfFilePath+File.separator+"sick_risk.png");
			sickRiskPageMap.put("PDF_ABS_PATH",pdfFilePath+File.separator+"sick_risk.pdf");
			itemUrlList.add(sickRiskPageMap);*/



			//生成报告pdf
			pdfs = genReportPdfWithImg(itemUrlList);

			// 合并pdf文件到一个文件中
			String mgrgePdfFilePath = pdfFilePath.append(File.separator).append(physicalExaminationRecordFormMap.getLong("id")+"_marge.pdf").toString();
			File  file = new File(mgrgePdfFilePath);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			OutputStream output = new FileOutputStream(mgrgePdfFilePath);
			MergePDF.concatPDFs(pdfs, output, true);
			margePdfPath = file.getAbsolutePath();


			// 疾病风险(疾病风险pdf报告只是生成了pdf文件，没有做保存，如果需要下载，直接通过规则路径下载)
			/*final PhysicalExaminationRecordFormMap temp = physicalExaminationRecordFormMap;

			new Thread(new Runnable() {
				public void run() {
					try{
						StringBuffer httpUrl = new StringBuffer(PropertiesUtils.findPropertiesKey(PropertiesUtils.REPORT_URL_PDF_GEN_SYN));
						httpUrl.append("?");
						httpUrl.append("physicalExaminationRecordFormMap.id=").append(temp.getLong("id"));
						List<Map<String,String>> itemUrlList = new ArrayList<Map<String, String>>();
						Map<String,String> sickRiskPageMap = new HashMap<String, String>();
						sickRiskPageMap.put("REQ_URL",httpUrl.toString());
						sickRiskPageMap.put("PNG_ABS_PATH",sickRiskPdfPathBuffer+File.separator+"sick_risk.png");
						sickRiskPageMap.put("PDF_ABS_PATH",sickRiskPdfPathBuffer+File.separator+"sick_risk.pdf");
						itemUrlList.add(sickRiskPageMap);
						genReportPdfWithImg(itemUrlList);
					}catch (Exception ex){
						logger.error(ex.getMessage());
						ex.printStackTrace();
					}
				}
			}).start();*/

		}
		if(StringUtils.isNotBlank(margePdfPath)){
			margePdfPath = margePdfPath.replace('\\','/');
		}
		return margePdfPath;
	}



	public List<InputStream> genReportPdfWithImg(List<Map<String,String>> reqUrlMapList)throws Exception{
		if(CollectionUtils.isEmpty(reqUrlMapList)){
			throw new Exception("生成PDF失败，无请求连接!");
		}
		WebDriver driver = null;
		try {
			driver = new RemoteWebDriver(new URL( PropertiesUtils.findPropertiesKey(PropertiesUtils.REMOTE_WEB_DRIVER_REQ_HTTP)),   DesiredCapabilities.firefox()); // 这个URL
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		if(driver == null){
			throw new Exception("生成浏览器驱动异常!");
		}
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS); // 设置页面加载超时的最大时长
//		driver.manage().window().maximize();

		List<InputStream> pdfList = new ArrayList<InputStream>();
		for(Map<String,String> reqItem:reqUrlMapList){
			try{
				driver.get(reqItem.get("REQ_URL"));
				//打开以后等待4秒钟
				Thread.sleep(3000);
				File screenShotFile = ((TakesScreenshot) driver) .getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(screenShotFile, new File(reqItem.get("PNG_ABS_PATH")));
				File pngFile = new File(reqItem.get("PNG_ABS_PATH"));
				//调用itext将图片转为pdf文件(仅支持一张图片放在一张pdf上)
				convertPNG2PDF(reqItem.get("PDF_ABS_PATH"),pngFile.getAbsolutePath());
				pdfList.add(new FileInputStream(reqItem.get("PDF_ABS_PATH")));
			}catch (Exception ex){
				ex.printStackTrace();
			}
		}

		driver.quit();


		return pdfList;

	}


	public String reportGen_BAK(PhysicalExaminationRecordFormMap physicalExaminationRecordFormMap)throws Exception{
		String margePdfPath = null;
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
			if(physicalExaminationRecordFormMap.get("check_time") instanceof  java.util.Date){
				pdfFilePath.append(dateFormat.format(physicalExaminationRecordFormMap.getDate("check_time")));
			}else{
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				pdfFilePath.append(dateFormat.format(simpleDateFormat.parse(physicalExaminationRecordFormMap.get("check_time").toString())));
			}


			pdfFilePath.append(File.separator);
			pdfFilePath.append(physicalExaminationRecordFormMap.getLong("id"));
			File savePathFile = new File(pdfFilePath.toString());
			if(!savePathFile.exists()||!savePathFile.isDirectory()){
				savePathFile.mkdirs();
			}


			//调用wkhtmltopdf将html转为pdf
			pdfs.add(new FileInputStream(url2PdfConverter(httpUrl.toString(),pdfFilePath.toString(),"0.pdf")));
//			pdfs.add(new FileInputStream(url2PdfConverter(httpUrl.toString(),pdfFilePath.toString(),"0.png")));

			for(int i=0;i< physicalExaminationBigResultFormMapList.size();i++){
				httpUrl = new StringBuffer(PropertiesUtils.findPropertiesKey(PropertiesUtils.REPORT_URL_PDF_GEN_ITEM));
				httpUrl.append("?");
				httpUrl.append("bigItemId=").append(physicalExaminationBigResultFormMapList.get(i).getLong("big_item_id")).append("&").append("recordId=").append(physicalExaminationRecordFormMap.getLong("id"));
				pdfs.add(new FileInputStream(url2PdfConverter(httpUrl.toString(),pdfFilePath.toString(),physicalExaminationBigResultFormMapList.get(i).getLong("big_item_id")+".pdf")));
//				pdfs.add(new FileInputStream(url2PdfConverter(httpUrl.toString(),pdfFilePath.toString(),physicalExaminationBigResultFormMapList.get(i).getLong("big_item_id")+".png")));
			}

			// 合并pdf文件到一个文件中
			String mgrgePdfFilePath = pdfFilePath.append(File.separator).append(physicalExaminationRecordFormMap.getLong("id")+"_marge.pdf").toString();
			File  file = new File(mgrgePdfFilePath);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}

			OutputStream output = new FileOutputStream(mgrgePdfFilePath);
			MergePDF.concatPDFs(pdfs, output, true);
			margePdfPath = file.getAbsolutePath();

		}
		if(StringUtils.isNotBlank(margePdfPath)){
			margePdfPath = margePdfPath.replace('\\','/');
		}
		return margePdfPath;
//		return physicalExaminationRecordFormMap.getLong("id")+"_marge.pdf";
	}



	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

	public String url2PdfConverter(String httpUrl,String saveAsPdfPath,String fileName) throws Exception{
		StringBuffer pngFilePath = new StringBuffer(saveAsPdfPath);
		pngFilePath.append(File.separator);
		pngFilePath.append(fileName);

		Pdf pdf = new Pdf();
		pdf.addParam(new Param("--enable-javascript"));//允许执行js
		pdf.addParam(new Param("--no-stop-slow-scripts"));//不要停止比较慢的js
		pdf.addParam(new Param("--javascript-delay"));//等待js加载
		pdf.addParam(new Param("4000"));
		pdf.addParam(new Param("--no-pdf-compression"));//不压缩pdf，质量更高
		pdf.addParam(new Param("--image-quality"));//pdf中图片的质量
		pdf.addParam(new Param("100"));
		pdf.addPage(httpUrl, PageType.url);
		File pdfFile = pdf.saveAs(pngFilePath.toString());
		/*File pngFile = pdf.saveAs(pngFilePath.toString());
		StringBuffer pdfFilePath  = new StringBuffer(saveAsPdfPath);
		pdfFilePath.append(File.separator);
		pdfFilePath.append(fileName+".pdf");*/

		//调用itext将图片转为pdf文件(仅支持一张图片放在一张pdf上)
//		convertPNG2PDF(pdfFilePath.toString(),pngFile.getAbsolutePath());
		return pdfFile.getAbsolutePath();
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
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, new FileOutputStream(PDF));
		document.open();
//        document.add(new Paragraph("Berlin!"));
        /*PdfContentByte canvas = writer.getDirectContentUnder();
        Image image = Image.getInstance(IMAGE);
        image.scaleAbsolute(PageSize.A4.rotate());
        image.setAbsolutePosition(0, 0);*/
		Image image = Image.getInstance(IMAGE);
		image.scaleAbsolute(PageSize.A4);//设置图片的绝对位置（即PDF的A4的大小，就是正好覆盖A4的整个页面）
//		image.scaleAbsolute(PageSize.A4.rotate().getWidth(),PageSize.A4.rotate().getHeight());
		image.setAbsolutePosition(0, 0);//设置图片放在PDF的起始位置
		document.add(image);
		document.close();

	}

	/*private class GenPdfTask implements  Callable<Map<Long,String>> {

		private String url = null;

		private String pdfPath = null;

		private String pngPath = null;

		private Long bigItemId = 0l;

		public GenPdfTask(String url,String pngPath,String pdfPath,Long bigItemId) {
			this.url = url;
			this.pdfPath = pdfPath;
			this.pngPath = pngPath;
			this.bigItemId = bigItemId;
		}

		@Override
		public Map<Long,String> call() throws Exception {
			String pdfPathIn = genReportPdfWithImg(url, pngPath,pdfPath);
			Map<Long,String> retMap = new HashMap<Long, String>();
			retMap.put(bigItemId,pdfPathIn);
			return retMap;
		}
	}*/
}