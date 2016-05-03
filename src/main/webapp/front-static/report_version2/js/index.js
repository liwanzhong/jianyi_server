// JavaScript Document

$(function(e){
	//柱状图
	$('.column-score span').each(function(index, element) {
		var score = $(element).text();
		var Height = score+'%', Top = 105-score+'px';
		//柱形高度变化
		$('.column-real').eq(index).css('height',Height);
		//分数上下移动
		$('.column-score').eq(index).css('top',Top);
		//颜色变化
		if( score>=90 ){
			$('.column-score').eq(index).addClass('color-green');
			$('.column-real').eq(index).addClass('column-real-green');
		}else if( score>=80 && score<89.99){
			$('.column-score').eq(index).addClass('color-blue');
			$('.column-real').eq(index).addClass('column-real-blue');
		}else if( score>=70 && score<79.99){
			$('.column-score').eq(index).addClass('color-yellow');
			$('.column-real').eq(index).addClass('column-real-yellow');
		}else if( score>=60 && score<69.99){
			$('.column-score').eq(index).addClass('color-orange');
			$('.column-real').eq(index).addClass('column-real-orange');
		}else{
			$('.column-score').eq(index).addClass('color-red');
			$('.column-real').eq(index).addClass('column-real-red');
		}
		
    });
	
	//渐变图表
	$('.shade-num span').each(function(index, element) {
		var score = $(element).text();
		//颜色变化
		if( score>=90 ){
			$('.shade-num').eq(index).addClass('color-green');
		}else if( score>=80 && score<89.99){
			$('.shade-num').eq(index).addClass('color-blue');
		}else if( score>=70 && score<79.99){
			$('.shade-num').eq(index).addClass('color-yellow');
		}else if( score>=60 && score<69.99){
			$('.shade-num').eq(index).addClass('color-orange');
		}else{
			$('.shade-num').eq(index).addClass('color-red');
		}
    });
	
	//折叠图表
	$('.vv span').each(function(index, element) {
		var score = $(element).text();
		//颜色变化
		if( score>=90 ){
			$('.vv li').eq(index).addClass('bg-green');
		}else if( score>=80 && score<89.99){
			$('.vv li').eq(index).addClass('bg-blue');
		}else if( score>=70 && score<79.99){
			$('.vv li').eq(index).addClass('bg-yellow');
		}else if( score>=60 && score<69.99){
			$('.vv li').eq(index).addClass('bg-orange');
		}else{
			$('.vv li').eq(index).addClass('bg-red');
		}
    });
	
	//六边形图表
	$('.six span').each(function(index, element) {
		var score = $(element).text();
		//颜色变化
		if( score>=90 ){
			$('.six li').eq(index).addClass('six-green').children('.six-color').addClass('color-green');
		}else if( score>=80 && score<89.99){
			$('.six li').eq(index).addClass('six-blue').children('.six-color').addClass('color-blue');
		}else if( score>=70 && score<79.99){
			$('.six li').eq(index).addClass('six-yellow').children('.six-color').addClass('color-yellow');
		}else if( score>=60 && score<69.99){
			$('.six li').eq(index).addClass('six-orange').children('.six-color').addClass('color-orange');
		}else{
			$('.six li').eq(index).addClass('six-red').children('.six-color').addClass('color-red');
		}
    });
	
	//table颜色变化
	$('.font-color span').each(function(index, element) {
		var score = $(element).text();
		//颜色变化
		if( score>=90 ){
			$('.font-color').eq(index).addClass('color-green');
			$('.tbody em').eq(index).addClass('bg-green');
		}else if( score>=80 && score<89.99){
			$('.font-color').eq(index).addClass('color-blue');
			$('.tbody em').eq(index).addClass('bg-blue');
		}else if( score>=70 && score<79.99){
			$('.font-color').eq(index).addClass('color-yellow');
			$('.tbody em').eq(index).addClass('bg-yellow');
		}else if( score>=60 && score<69.99){
			$('.font-color').eq(index).addClass('color-orange');
			$('.tbody em').eq(index).addClass('bg-orange');
		}else{
			$('.font-color').eq(index).addClass('color-red');
			$('.tbody em').eq(index).addClass('bg-red');
		}
    });
	
	//灯泡
	$('.disease-per').each(function(index, element) {
		var score = $(element).text();
		//颜色变化
		if( score>='75%' ){
			$(this).addClass('circle-red');
		}else if( score>='70%' && score<'74.99%'){
			$(element).addClass('circle-orange');
		}else if( score>='60%' && score<'69.99%'){
			$(element).addClass('circle-yellow');
		}else if( score>='55%' && score<'59.99%'){
			$(element).addClass('circle-blue');
		}else{
			$(element).addClass('circle-green');
		}
    });

});



