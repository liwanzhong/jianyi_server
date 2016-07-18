package com.lanyuan.util.sign;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/7/13.
 */
public class Guava_ListenableFuture_Test {

    // 创建线程池
    final static ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

//    @Test
    public void test(){



        final ListenableFuture<Integer> listenableFuture = executorService.submit(new TaskBak("listenableFuture",3000l,"http://blog.csdn.net/pistolove/article/details/51232004"));

        final ListenableFuture<Integer> listenableFuture2 = executorService.submit(new TaskBak("listenableFuture2",5000l,"http://my.oschina.net/cloudcoder/blog/359598"));

        final ListenableFuture<Integer> listenableFuture3 = executorService.submit(new TaskBak("listenableFuture3",3000l,"http://blog.csdn.net/wwwqjpcom/article/details/51232302"));

        final ListenableFuture<Integer> listenableFuture4 = executorService.submit(new TaskBak("listenableFuture4",5000l,"http://blog.csdn.net/appleheshuang/article/details/7899335"));

        final ListenableFuture<Integer> listenableFutur5 = executorService.submit(new TaskBak("listenableFuture5",3000l,"https://support.mozilla.org/zh-CN/kb/%E7%AE%A1%E7%90%86%E7%94%A8%E6%88%B7%E9%85%8D%E7%BD%AE%E6%96%87%E4%BB%B6"));

        final ListenableFuture<Integer> listenableFuture6 = executorService.submit(new TaskBak("listenableFuture6",5000l,"http://www.jb51.net/article/26470.htm"));

        //同步获取调用结果
        /*try {
            System.out.println(listenableFuture.get());
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e1) {
            e1.printStackTrace();
        }

        //同步获取调用结果
        try {
            System.out.println(listenableFuture2.get());
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e1) {
            e1.printStackTrace();
        }*/

        try {
            Thread.sleep(900000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void test4() throws InterruptedException {
        /*for (int i=0;i<3;i++) {
            final int abc = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        System.setProperty("webdriver.chrome.driver", "G:\\yitian_projects\\chh_manager\\src\\main\\webapp\\components\\chrome\\chromedriver.exe");
                        System.setProperty("webdriver.opera.driver", "C:\\Users\\liwanzhong\\Desktop\\webdriver\\operadriver.exe");

                        ProfilesIni allProfiles = new ProfilesIni();
                        FirefoxProfile profile = allProfiles.getProfile("WebDriver");

                        WebDriver driver = new ChromeDriver();
//                        driver.manage().window().maximize();
//                        WebDriver driver  = new FirefoxDriver();

                        if(driver!=null){
                            driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS); // 设置页面加载超时的最大时长
                            try{
                                System.out.println(abc+".................................");
                                driver.get("http://www.testwo.com/blog/6931");
                                //打开以后等待4秒钟
                                Thread.sleep(3000);
                                File screenShotFile = ((TakesScreenshot) driver) .getScreenshotAs(OutputType.FILE);
                                FileUtils.copyFile(screenShotFile, new File("D:\\idea-workspack\\works\\jianyi_server\\png\\"+abc+".png"));
                                *//*final Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.simple()).takeScreenshot(driver);
                                final BufferedImage image = screenshot.getImage();
                                ImageIO.write(image, "PNG", new File("D:\\idea-workspack\\works\\jianyi_server\\png\\"+abc+".png"));*//*
                                *//*CutStrategy cutting = new VariableCutStrategy(50, 50, 50);
                                ShootingStrategy rotating = new RotatingDecorator(cutting, ShootingStrategies.simple());
                                ShootingStrategy pasting = new ViewportPastingDecorator(rotating)
                                        .withScrollTimeout(2000);
                                final Screenshot screenshot =new AShot()
                                        .shootingStrategy(pasting)
                                        .takeScreenshot(driver);
                                final BufferedImage image = screenshot.getImage();
                                ImageIO.write(image, "PNG", new File("D:\\idea-workspack\\works\\jianyi_server\\png\\"+abc+".png"));*//*
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                            driver.quit();
                        }

                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }).start();

        }*/


        System.setProperty("webdriver.chrome.driver", "G:\\yitian_projects\\chh_manager\\src\\main\\webapp\\components\\chrome\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        try{
//            String URL="C:\\Users\\Administrator\\Desktop\\报告\\报告\\人体生理机能状况评估报告.html";
            String URL="http://www.51testing.com/zhuanti/selenium.html";
            driver.get(URL);
            File screenShotFile = ((TakesScreenshot) driver) .getScreenshotAs(OutputType.FILE);
            if(screenShotFile!=null&&screenShotFile.exists()){//截图已经存在
                FileUtils.copyFile(screenShotFile, new File("G:\\ihavecar\\code_demo\\Selenium\\webdriver\\aabC.png"));
                System.out.println("sssssssss");
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        driver.quit();

        Thread.sleep(88888888);
    }






//    @Test
    public void test3(){

        for (int i=0;i<3;i++){
            final int abc = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        WebDriver driver = null;
                        try {
                            driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),   DesiredCapabilities.firefox()); // 这个URL
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        if(driver!=null){
                            driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS); // 设置页面加载超时的最大时长
                            try{
                                System.out.println(abc+".................................");
                                driver.get("http://localhost:8080/front/login.shtml");
                                //打开以后等待4秒钟
                                Thread.sleep(3000);
                                File screenShotFile = ((TakesScreenshot) driver) .getScreenshotAs(OutputType.FILE);
                                FileUtils.copyFile(screenShotFile, new File("G:\\ihavecar\\git-codes\\jianyi\\png\\"+abc+".png"));
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                            driver.quit();
                        }

                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }).start();
        }

        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }



    /*@Test
    public void test2(){

        //todo 处理请求
        FirefoxDriver driver = new FirefoxDriver(); // 这个URL
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS); // 设置页面加载超时的最大时长
        String DriverServer =null;
        String CaseSession = null;
        try{
            DriverServer = ((FirefoxDriver.LazyCommandExecutor)driver.getCommandExecutor()).getAddressOfRemoteServer().toString();
            CaseSession = driver.getSessionId().toString();
        }
        catch(Exception e){
            e.printStackTrace();
        }



        final ListenableFuture<Integer> listenableFuture = executorService.submit(new Task("listenableFuture",3000l,driver,"http://blog.csdn.net/pistolove/article/details/51232004",DriverServer,CaseSession));

        final ListenableFuture<Integer> listenableFuture2 = executorService.submit(new Task("listenableFuture2",5000l,driver,"http://my.oschina.net/cloudcoder/blog/359598",DriverServer,CaseSession));

        //同步获取调用结果
        try {
            System.out.println(listenableFuture.get());
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e1) {
            e1.printStackTrace();
        }

        //同步获取调用结果
        try {
            System.out.println(listenableFuture2.get());
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e1) {
            e1.printStackTrace();
        }
        driver.quit();

    }*/


    class Task implements Callable<Integer> {
        String str;
        Long sleepMis;
        WebDriver driver = null;
        String urlPaht = null;
        public Task(String str,Long sleepMis,WebDriver driver,String urlPaht,String DriverServer,String CaseSession){
            this.str = str;
            this.sleepMis = sleepMis;
//            this.driver = new MyFirefoxDriver(DriverServer,CaseSession);
            this.driver = driver;
            this.urlPaht = urlPaht;
        }
        @Override
        public Integer call() throws Exception {


            try{
                driver.get(urlPaht);

                //打开以后等待4秒钟
                TimeUnit.MILLISECONDS.sleep(sleepMis);
                File screenShotFile = ((TakesScreenshot) driver) .getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(screenShotFile, new File("G:\\ihavecar\\git-codes\\jianyi\\png\\"+str+".png"));
            }catch (Exception ex){
                ex.printStackTrace();
            }
            System.out.println("call execute.." + str);

            return sleepMis.intValue();
        }
    }


    class TaskBak implements Callable<Integer> {
        String str;
        Long sleepMis;
        String urlPaht = null;
        public TaskBak(String str,Long sleepMis,String urlPaht){
            this.str = str;
            this.sleepMis = sleepMis;
            this.urlPaht = urlPaht;
        }
        @Override
        public Integer call() throws Exception {
            try{
                WebDriver driver = null;
                try {
                    driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),   DesiredCapabilities.firefox()); // 这个URL
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                if(driver!=null){
                    driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS); // 设置页面加载超时的最大时长
                    try{
                        System.out.println(str);
                        driver.get(urlPaht);
                        System.out.println(urlPaht);
                        //打开以后等待4秒钟
                        Thread.sleep(3000);
                        File screenShotFile = ((TakesScreenshot) driver) .getScreenshotAs(OutputType.FILE);
                        FileUtils.copyFile(screenShotFile, new File("G:\\ihavecar\\git-codes\\jianyi\\png\\"+str+".png"));
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                    driver.quit();
                }

            }catch (Exception ex){
                ex.printStackTrace();
            }
            System.out.println("call execute.." + str);

            return sleepMis.intValue();
        }
    }
}
