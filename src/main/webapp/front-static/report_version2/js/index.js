// JavaScript Document

$(function(e){
	//赋值
	var pingfen ={ 
		'pingfen_1min': 86,
		'pingfen_1max': 100,
		'pingfen_2min': 73.01,
		'pingfen_2max': 85.99,
		'pingfen_3min': 60,
		'pingfen_3max': 73,
		'pingfen_4min': 60,
		'pingfen_4max': 60.99,
		'pingfen_5min': 40,
		'pingfen_5max': 59.99
	}
	
	//柱状图
	$('.column>ul').each(function(index, element) {
		var liW = 100/$(element).children('li').length+'%';
		$(element).children('li').css('width',liW);
    });
	$('.column-score span').each(function(index, element) {
		var score = $(element).text();
		var Height = score+'%', Top = 105-score+'px';
		//柱形高度变化
		$('.column-real').eq(index).css('height',Height);
		//分数上下移动
		$('.column-score').eq(index).css('top',Top);
		//颜色变化
		if( score>=pingfen.pingfen_1min && score<=pingfen.pingfen_1max ){
			$('.column-score').eq(index).addClass('color-green');
			$('.column-real').eq(index).addClass('column-real-green');
		}else if( score>=pingfen.pingfen_2min && score<=pingfen.pingfen_2max ){
			$('.column-score').eq(index).addClass('color-blue');
			$('.column-real').eq(index).addClass('column-real-blue');
		}else if( score>=pingfen.pingfen_3min && score<=pingfen.pingfen_3max ){
			$('.column-score').eq(index).addClass('color-yellow');
			$('.column-real').eq(index).addClass('column-real-yellow');
		}else if( score>=pingfen.pingfen_4min && score<=pingfen.pingfen_4max ){
			$('.column-score').eq(index).addClass('color-orange');
			$('.column-real').eq(index).addClass('column-real-orange');
		}else if( score>=pingfen.pingfen_5min && score<=pingfen.pingfen_5max ){
			$('.column-score').eq(index).addClass('color-red');
			$('.column-real').eq(index).addClass('column-real-red');
		}
		
    });
	
	//渐变图表
	$('.shade-num span').each(function(index, element) {
		var score = $(element).text();
		//颜色变化
		if( score>=pingfen.pingfen_1min && score<=pingfen.pingfen_1max ){
			$('.shade-num').eq(index).addClass('color-green');
		}else if( score>=pingfen.pingfen_2min && score<=pingfen.pingfen_2max ){
			$('.shade-num').eq(index).addClass('color-blue');
		}else if( score>=pingfen.pingfen_3min && score<=pingfen.pingfen_3max ){
			$('.shade-num').eq(index).addClass('color-yellow');
		}else if( score>=pingfen.pingfen_4min && score<=pingfen.pingfen_4max ){
			$('.shade-num').eq(index).addClass('color-orange');
		}else if( score>=pingfen.pingfen_5min && score<=pingfen.pingfen_5max ){
			$('.shade-num').eq(index).addClass('color-red');
		}
    });
	
	//折叠图表
	$('.vv span').each(function(index, element) {
		var score = $(element).text();
		//颜色变化
		if( score>=pingfen.pingfen_1min && score<=pingfen.pingfen_1max ){
			$('.vv li').eq(index).addClass('bg-green');
		}else if( score>=pingfen.pingfen_2min && score<=pingfen.pingfen_2max ){
			$('.vv li').eq(index).addClass('bg-blue');
		}else if( score>=pingfen.pingfen_3min && score<=pingfen.pingfen_3max ){
			$('.vv li').eq(index).addClass('bg-yellow');
		}else if( score>=pingfen.pingfen_4min && score<=pingfen.pingfen_4max ){
			$('.vv li').eq(index).addClass('bg-orange');
		}else if( score>=pingfen.pingfen_5min && score<=pingfen.pingfen_5max ){
			$('.vv li').eq(index).addClass('bg-red');
		}
    });
	$('.vvv span').each(function(index, element) {
		var score = $(element).text();
		//颜色变化
		if( score>=pingfen.pingfen_1min && score<=pingfen.pingfen_1max ){
			$('.vvv li').eq(index).addClass('bg-green');
		}else if( score>=pingfen.pingfen_2min && score<=pingfen.pingfen_2max ){
			$('.vvv li').eq(index).addClass('bg-blue');
		}else if( score>=pingfen.pingfen_3min && score<=pingfen.pingfen_3max ){
			$('.vvv li').eq(index).addClass('bg-yellow');
		}else if( score>=pingfen.pingfen_4min && score<=pingfen.pingfen_4max ){
			$('.vvv li').eq(index).addClass('bg-orange');
		}else if( score>=pingfen.pingfen_5min && score<=pingfen.pingfen_5max ){
			$('.vvv li').eq(index).addClass('bg-red');
		}
    });
	$('.ul-w').each(function(index, element) {
        var liNum = $(element).children('li').length;
		var liWidth = $(element).children('li').width();
		$(element).css('width',liNum*liWidth);
		
    });
        
	//六边形图表
	$('.six span').each(function(index, element) {
		var score = $(element).text();
		//颜色变化
		if( score>=pingfen.pingfen_1min && score<=pingfen.pingfen_1max ){
			$('.six li').eq(index).addClass('six-green').children('.six-color').addClass('color-green');
		}else if( score>=pingfen.pingfen_2min && score<=pingfen.pingfen_2max ){
			$('.six li').eq(index).addClass('six-blue').children('.six-color').addClass('color-blue');
		}else if( score>=pingfen.pingfen_3min && score<=pingfen.pingfen_3max ){
			$('.six li').eq(index).addClass('six-yellow').children('.six-color').addClass('color-yellow');
		}else if( score>=pingfen.pingfen_4min && score<=pingfen.pingfen_4max ){
			$('.six li').eq(index).addClass('six-orange').children('.six-color').addClass('color-orange');
		}else if( score>=pingfen.pingfen_5min && score<=pingfen.pingfen_5max ){
			$('.six li').eq(index).addClass('six-red').children('.six-color').addClass('color-red');
		}
    });
	
	//圆形图表
	$('.run span').each(function(index, element) {
		var score = $(element).text();
		//颜色变化
		if( score>=pingfen.pingfen_1min && score<=pingfen.pingfen_1max ){
			$('.run-score').eq(index).addClass('bg-green');
		}else if( score>=pingfen.pingfen_2min && score<=pingfen.pingfen_2max ){
			$('.run-score').eq(index).addClass('bg-blue');
		}else if( score>=pingfen.pingfen_3min && score<=pingfen.pingfen_3max ){
			$('.run-score').eq(index).addClass('bg-yellow');
		}else if( score>=pingfen.pingfen_4min && score<=pingfen.pingfen_4max ){
			$('.run-score').eq(index).addClass('bg-orange');
		}else if( score>=pingfen.pingfen_5min && score<=pingfen.pingfen_5max ){
			$('.run-score').eq(index).addClass('bg-red');
		}
    });
	
	//花型图表
	$('.flower span').each(function(index, element) {
		var score = $(element).text();
		//颜色变化
		if( score>=pingfen.pingfen_1min && score<=pingfen.pingfen_1max ){
			$('.flo-score').eq(index).addClass('bg-green');
		}else if( score>=pingfen.pingfen_2min && score<=pingfen.pingfen_2max ){
			$('.flo-score').eq(index).addClass('bg-blue');
		}else if( score>=pingfen.pingfen_3min && score<=pingfen.pingfen_3max ){
			$('.flo-score').eq(index).addClass('bg-yellow');
		}else if( score>=pingfen.pingfen_4min && score<=pingfen.pingfen_4max ){
			$('.flo-score').eq(index).addClass('bg-orange');
		}else if( score>=pingfen.pingfen_5min && score<=pingfen.pingfen_5max ){
			$('.flo-score').eq(index).addClass('bg-red');
		}
    });

	
	//table颜色变化
	$('.font-color span').each(function(index, element) {
		var score = $(element).text();
		//颜色变化
		if( score>=pingfen.pingfen_1min && score<=pingfen.pingfen_1max ){
			$('.font-color').eq(index).addClass('color-green');
			$('.tbody em').eq(index).addClass('bg-green');
		}else if( score>=pingfen.pingfen_2min && score<=pingfen.pingfen_2max ){
			$('.font-color').eq(index).addClass('color-blue');
			$('.tbody em').eq(index).addClass('bg-blue');
		}else if( score>=pingfen.pingfen_3min && score<=pingfen.pingfen_3max ){
			$('.font-color').eq(index).addClass('color-yellow');
			$('.tbody em').eq(index).addClass('bg-yellow');
		}else if( score>=pingfen.pingfen_4min && score<=pingfen.pingfen_4max ){
			$('.font-color').eq(index).addClass('color-orange');
			$('.tbody em').eq(index).addClass('bg-orange');
		}else if( score>=pingfen.pingfen_5min && score<=pingfen.pingfen_5max ){
			$('.font-color').eq(index).addClass('color-red');
			$('.tbody em').eq(index).addClass('bg-red');
		}
    });
	
	//灯泡
	$('.disease-per').each(function(index, element) {
		var score = $(element).text();
		//颜色变化
		if( score>='75%' ){
			$(element).addClass('circle-red');
		}else if( score>='70%' && score<='74.99%'){
			$(element).addClass('circle-orange');
		}else if( score>='60%' && score<='69.99%'){
			$(element).addClass('circle-yellow');
		}else if( score>='55%' && score<='59.99%'){
			$(element).addClass('circle-blue');
		}else if( score>='0%' && score<='54.99%'){
			$(element).addClass('circle-green');
		}
    });

//	$('.ul-Width').each(function(index, element) {
//		var liNum = $(element).children('li').length;
//		var ulWidth = liNum*70;
//		$(element).css('width',ulWidth);
//    });

});



