package com.lanyuan.util.sign;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/7/13.
 */
public class Guava_ListenableFuture_Test {

    // 创建线程池
    final static ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

    @Test
    public void test(){



        final ListenableFuture<Integer> listenableFuture = executorService.submit(new TaskBak("listenableFuture",3000l,"http://blog.csdn.net/pistolove/article/details/51232004"));

        final ListenableFuture<Integer> listenableFuture2 = executorService.submit(new TaskBak("listenableFuture2",5000l,"http://my.oschina.net/cloudcoder/blog/359598"));

        final ListenableFuture<Integer> listenableFuture3 = executorService.submit(new TaskBak("listenableFuture3",3000l,"http://blog.csdn.net/wwwqjpcom/article/details/51232302"));

        final ListenableFuture<Integer> listenableFuture4 = executorService.submit(new TaskBak("listenableFuture4",5000l,"https://learnsoapui.wordpress.com/"));

        final ListenableFuture<Integer> listenableFutur5 = executorService.submit(new TaskBak("listenableFuture5",3000l,"http://www.programcreek.com/java-api-examples/index.php?api=org.junit.rules.TestRule"));

        final ListenableFuture<Integer> listenableFuture6 = executorService.submit(new TaskBak("listenableFuture6",5000l,"http://www.programcreek.com/java-api-examples/index.php?api=org.openqa.selenium.remote.HttpCommandExecutor"));

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

        try {
            Thread.sleep(900000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



    @Test
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

    }


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
                //todo 处理请求
                WebDriver driver = new FirefoxDriver(); // 这个URL
                driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS); // 设置页面加载超时的最大时长(最大等待时间)
                driver.get(urlPaht);

                //打开以后等待4秒钟
                TimeUnit.MILLISECONDS.sleep(sleepMis);
                File screenShotFile = ((TakesScreenshot) driver) .getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(screenShotFile, new File("G:\\ihavecar\\git-codes\\jianyi\\png\\"+str+".png"));
                driver.quit();
            }catch (Exception ex){
                ex.printStackTrace();
            }
            System.out.println("call execute.." + str);

            return sleepMis.intValue();
        }
    }
}
