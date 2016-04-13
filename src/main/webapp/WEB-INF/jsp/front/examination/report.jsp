<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>人体机能检测报告</title>
<link type="text/css" rel="stylesheet" href="${ctx}/front-static/report/css/base.css" />
<link type="text/css" rel="stylesheet" href="${ctx}/front-static/report/css/index.css" />
<script src="${ctx}/front-static/report/js/jquery-1.12.1.min.js"></script>
<script src="${ctx}/front-static/report/js/radialIndicator.js"></script>
</head>

<body>
<div class="wrap">
	<div class="header">
    	<h1>人体机能检测报告</h1>
        <ol class="clearfix">
        	<li><a href="#">下一页</a></li>
        	<li style="margin-right:10px;"><a href="#">上一页</a></li>
        </ol>
        <div class="msg">
        	<ul class="clearfix">
        		<li>${physicalExaminationRecordFormMap.name}</li>
        		<li>${physicalExaminationRecordFormMap.sex}</li>
        		<li>${physicalExaminationRecordFormMap.birthday}岁</li>
        		<li>体重：${physicalExaminationRecordFormMap.body_height}kg</li>
        		<li>身高：${physicalExaminationRecordFormMap.weight}cm</li>
                <li class="time">${physicalExaminationRecordFormMap.check_time}</li>
        	</ul>
        </div>
    </div>
    <div class="main-top">
    	<button class="health">我离健康有多远？</button>
        <div id="indicatorContainer">
        	<span class="grade">总体评分</span>
            <span class="bim">BIM</span>
        </div>
		<script>
        $('#indicatorContainer').radialIndicator({
            radius: 60, //圆的半径，默认50
            barBgColor: '#41c1bc', //刻度条的背景颜色,默认#eee
            barColor: {
                0: '#f00',
                60: '#ec890a',
                70: '#e5bf16',
                80: '#3da2ec',
                100: '#28980a'
            }, //刻度条的颜色
            barWidth: 10, //刻度条的宽度，默认5
            initValue: 72, //圆形指示器初始化的值
            roundCorner:false, //刻度条是否圆角
            percentage: false //显示百分数
        });  
		$('#indicatorContainer').radialIndicator({
            radius: 40, //圆的半径，默认50
            barBgColor: '#41c1bc', //刻度条的背景颜色,默认#eee
            barColor: '#41c1bc', //刻度条的颜色
            barWidth: 10, //刻度条的宽度，默认5
            initValue:29.86, //圆形指示器初始化的值
            roundCorner:false, //刻度条是否圆角
            percentage: false //显示百分数
        });  
                
        </script>
    </div>
    <div class="main-bottom">
    	<ul class="clearfix">
    		<li>01科学检测</li>
    		<li>02专业评估</li>
    		<li>03健康干预</li>
    	</ul>
    	<button class="health">检测项总体评估</button>
        <span class="score-1">细胞功能</span>
        <span class="score-2">心血管功能</span>
        <span class="score-3">脑血管<br>神经功能</span>
        <span class="score-4">呼吸功能</span>
        <span class="score-5">消化<br>吸收功能</span>
        <span class="score-6">肝胆功能</span>
        <span class="score-7">肾脏功能</span>
        <span class="score-8">胰岛细胞功能</span>
        <span class="score-9">内分泌<br>系统功能</span>
        <span class="score-10">免疫<br>系统功能</span>
        <span class="score-11">骨、关节功能</span>
        <span class="score-12">女性<br>性激素功能</span>
        <span class="score-13">女性<br>性器官功能</span>
        <span class="score-14">皮肤功能</span>
        <div id="indicatorContainer2">
        </div>
		<script>
			$('#indicatorContainer2').radialIndicator({
				radius: 30, //圆的半径，默认50
				barBgColor: '#8df8f1', //刻度条的背景颜色,默认#eee
				barColor: {
					0: '#f00',
					60: '#ec890a',
					70: '#e5bf16',
					80: '#3da2ec',
					100: '#28980a'
				}, //刻度条的颜色
				barWidth: 7, //刻度条的宽度，默认5
				initValue: 80, //圆形指示器初始化的值
				roundCorner:false, //刻度条是否圆角
				percentage: false //显示百分数
			});  
			$('#indicatorContainer2').radialIndicator({
				radius: 30, //圆的半径，默认50
				barBgColor: '#8df8f1', //刻度条的背景颜色,默认#eee
				barColor: {
					0: '#f00',
					60: '#ec890a',
					70: '#e5bf16',
					80: '#3da2ec',
					100: '#28980a'
				}, //刻度条的颜色
				barWidth: 7, //刻度条的宽度，默认5
				initValue: 90, //圆形指示器初始化的值
				roundCorner:false, //刻度条是否圆角
				percentage: false //显示百分数
			});  
			$('#indicatorContainer2').radialIndicator({
				radius: 30, //圆的半径，默认50
				barBgColor: '#8df8f1', //刻度条的背景颜色,默认#eee
				barColor: {
					0: '#f00',
					60: '#ec890a',
					70: '#e5bf16',
					80: '#3da2ec',
					100: '#28980a'
				}, //刻度条的颜色
				barWidth: 7, //刻度条的宽度，默认5
				initValue: 70, //圆形指示器初始化的值
				roundCorner:false, //刻度条是否圆角
				percentage: false //显示百分数
			}); 
			$('#indicatorContainer2').radialIndicator({
				radius: 30, //圆的半径，默认50
				barBgColor: '#8df8f1', //刻度条的背景颜色,默认#eee
				barColor: {
					0: '#f00',
					60: '#ec890a',
					70: '#e5bf16',
					80: '#3da2ec',
					100: '#28980a'
				}, //刻度条的颜色
				barWidth: 7, //刻度条的宽度，默认5
				initValue: 72, //圆形指示器初始化的值
				roundCorner:false, //刻度条是否圆角
				percentage: false //显示百分数
			});   
			$('#indicatorContainer2').radialIndicator({
				radius: 30, //圆的半径，默认50
				barBgColor: '#8df8f1', //刻度条的背景颜色,默认#eee
				barColor: {
					0: '#f00',
					60: '#ec890a',
					70: '#e5bf16',
					80: '#3da2ec',
					100: '#28980a'
				}, //刻度条的颜色
				barWidth: 7, //刻度条的宽度，默认5
				initValue: 80, //圆形指示器初始化的值
				roundCorner:false, //刻度条是否圆角
				percentage: false //显示百分数
			});  
			$('#indicatorContainer2').radialIndicator({
				radius: 30, //圆的半径，默认50
				barBgColor: '#8df8f1', //刻度条的背景颜色,默认#eee
				barColor: {
					0: '#f00',
					60: '#ec890a',
					70: '#e5bf16',
					80: '#3da2ec',
					100: '#28980a'
				}, //刻度条的颜色
				barWidth: 7, //刻度条的宽度，默认5
				initValue: 80, //圆形指示器初始化的值
				roundCorner:false, //刻度条是否圆角
				percentage: false //显示百分数
			});  
			$('#indicatorContainer2').radialIndicator({
				radius: 30, //圆的半径，默认50
				barBgColor: '#8df8f1', //刻度条的背景颜色,默认#eee
				barColor: {
					0: '#f00',
					60: '#ec890a',
					70: '#e5bf16',
					80: '#3da2ec',
					100: '#28980a'
				}, //刻度条的颜色
				barWidth: 7, //刻度条的宽度，默认5
				initValue: 80, //圆形指示器初始化的值
				roundCorner:false, //刻度条是否圆角
				percentage: false //显示百分数
			});  
			$('#indicatorContainer2').radialIndicator({
				radius: 30, //圆的半径，默认50
				barBgColor: '#8df8f1', //刻度条的背景颜色,默认#eee
				barColor: {
					0: '#f00',
					60: '#ec890a',
					70: '#e5bf16',
					80: '#3da2ec',
					100: '#28980a'
				}, //刻度条的颜色
				barWidth: 7, //刻度条的宽度，默认5
				initValue: 80, //圆形指示器初始化的值
				roundCorner:false, //刻度条是否圆角
				percentage: false //显示百分数
			});  
			$('#indicatorContainer2').radialIndicator({
				radius: 30, //圆的半径，默认50
				barBgColor: '#8df8f1', //刻度条的背景颜色,默认#eee
				barColor: {
					0: '#f00',
					60: '#ec890a',
					70: '#e5bf16',
					80: '#3da2ec',
					100: '#28980a'
				}, //刻度条的颜色
				barWidth: 7, //刻度条的宽度，默认5
				initValue: 80, //圆形指示器初始化的值
				roundCorner:false, //刻度条是否圆角
				percentage: false //显示百分数
			});
			$('#indicatorContainer2').radialIndicator({
				radius: 30, //圆的半径，默认50
				barBgColor: '#8df8f1', //刻度条的背景颜色,默认#eee
				barColor: {
					0: '#f00',
					60: '#ec890a',
					70: '#e5bf16',
					80: '#3da2ec',
					100: '#28980a'
				}, //刻度条的颜色
				barWidth: 7, //刻度条的宽度，默认5
				initValue: 88, //圆形指示器初始化的值
				roundCorner:false, //刻度条是否圆角
				percentage: false //显示百分数
			});
			$('#indicatorContainer2').radialIndicator({
				radius: 30, //圆的半径，默认50
				barBgColor: '#8df8f1', //刻度条的背景颜色,默认#eee
				barColor: {
					0: '#f00',
					60: '#ec890a',
					70: '#e5bf16',
					80: '#3da2ec',
					100: '#28980a'
				}, //刻度条的颜色
				barWidth: 7, //刻度条的宽度，默认5
				initValue: 60, //圆形指示器初始化的值
				roundCorner:false, //刻度条是否圆角
				percentage: false //显示百分数
			});
			$('#indicatorContainer2').radialIndicator({
				radius: 30, //圆的半径，默认50
				barBgColor: '#8df8f1', //刻度条的背景颜色,默认#eee
				barColor: {
					0: '#f00',
					60: '#ec890a',
					70: '#e5bf16',
					80: '#3da2ec',
					100: '#28980a'
				}, //刻度条的颜色
				barWidth: 7, //刻度条的宽度，默认5
				initValue:78, //圆形指示器初始化的值
				roundCorner:false, //刻度条是否圆角
				percentage: false //显示百分数
			});
			$('#indicatorContainer2').radialIndicator({
				radius: 30, //圆的半径，默认50
				barBgColor: '#8df8f1', //刻度条的背景颜色,默认#eee
				barColor: {
					0: '#f00',
					60: '#ec890a',
					70: '#e5bf16',
					80: '#3da2ec',
					100: '#28980a'
				}, //刻度条的颜色
				barWidth: 7, //刻度条的宽度，默认5
				initValue: 78, //圆形指示器初始化的值
				roundCorner:false, //刻度条是否圆角
				percentage: false //显示百分数
			});
			$('#indicatorContainer2').radialIndicator({
				radius: 30, //圆的半径，默认50
				barBgColor: '#8df8f1', //刻度条的背景颜色,默认#eee
				barColor: {
					0: '#f00',
					60: '#ec890a',
					70: '#e5bf16',
					80: '#3da2ec',
					100: '#28980a'
				}, //刻度条的颜色
				barWidth: 7, //刻度条的宽度，默认5
				initValue: 65, //圆形指示器初始化的值
				roundCorner:false, //刻度条是否圆角
				percentage: false //显示百分数
			});
                
        </script>
    </div>
</div>
</body>
</html>
