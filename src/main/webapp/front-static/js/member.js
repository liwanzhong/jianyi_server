// JavaScript Document
$(function(){
	//头部下拉
	$('.header-nav>li').hover(function(){
    	$(this).children('ul').stop().slideToggle(300);
    });
	
	//导航
	$('.nav li a').click(function() {
        $(this).addClass('current').parent().siblings().children().removeClass('current');
    });
	
	//页码
	$('.table-pages li a').click(function() {
        $(this).addClass('current').parent().siblings().children().removeClass('current');
    });
	
	//新增会员
	//性别
	$('.member-infor input[name="sex"]').click(function(){
		$(this).addClass('male-check').siblings().removeClass('male-check');
	});
	//切除手术
	$('.member-infor input[name="operation"]').click(function() {
		if( $(this).hasClass('oper-check')){
			$(this).removeClass('oper-check');
		}else{
			$(this).addClass('oper-check');
		}
    });
	
	//弹窗
	$('.check-btn').click(function() {
		$('.popup').fadeIn(200);
		//弹窗居中
		var winHeight = $(window).height(); //浏览器当前窗口可视区域高度 
		var winWidth = $(window).width(); //浏览器当前窗口可视区域宽度
		var boxHeight = $('.popup-box').height(); //元素内容高度
		var boxWidth = $('.popup-box').width(); //元素内容宽度
		var boxPaddingT = parseInt($('.popup-box').css('padding-top'));
		var boxPaddingB = parseInt($('.popup-box').css('padding-bottom'));
		var boxPaddingL = parseInt($('.popup-box').css('padding-left'));
		var boxPaddingR = parseInt($('.popup-box').css('padding-right'));
		var allboxW = boxWidth+boxPaddingL+boxPaddingR; //元素总宽
		var allboxH = boxHeight+boxPaddingT+boxPaddingB; //元素总高
		var boxTop = (winWidth-allboxW)/2; //定位left值
		var boxLeft = (winHeight-allboxH)/2; //定位top值
		$('.popup-box').css({'left':boxTop,'top':boxLeft}); //赋值
    });
	function boxHide() {
		$('.popup').fadeOut(200);
    }
	$('.dark').click(boxHide);
	$('.popup-btn').click(boxHide);
});




