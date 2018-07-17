/*
	滚动定位
*/
function scrollPosition(scrolldom) {
	jQuery.scrollto = function(scrolldom,scrolltime) {
		$(scrolldom).click( function(){ 
			var scrolltodom = $(this).attr("date-scroll");
			$(this).addClass("thisscroll").siblings().removeClass("thisscroll");
			$('html,body').animate({
				scrollTop:$(scrolltodom).offset().top},scrolltime
			);
			return false;
		}); 
	};
	$.scrollto("#scrollnav a",600);
}

/*
	文本返回顶部设置
*/
function backToTopOper() {
	var $backToTopTxt = "返回顶部", 
		$backToTopEle = $('<div class="backToTop"></div>').appendTo($("body"))
						.text($backToTopTxt).attr("title", $backToTopTxt).click(
						function() {
		            		$("html, body").animate({ scrollTop: 0 }, 120);
		 				}), 
		$backToTopFun = function() {
		     var st = $(document).scrollTop(),winh = $(window).height();
		     (st > 0)? $backToTopEle.show(): $backToTopEle.hide();
		        //IE6下的定位
		     if (!window.XMLHttpRequest) {
		        $backToTopEle.css("top", st + winh - 166);
		     }
		};
		$(window).bind("scroll", $backToTopFun);
		$(function() { $backToTopFun(); });
}

/*
	图片返回顶部设置
*/    
function goTopEx(){
        var obj=document.getElementById("goTopBtn");
        function getScrollTop(){
            return window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop;
        }
        function setScrollTop(value){
            if(document.documentElement.scrollTop){
               document.documentElement.scrollTop=value;
            }else{
               document.body.scrollTop=value;
            }
        }    
		//getScrollTop()>500 距离顶部500px 时候显示返回顶部按钮。自己设置
        window.onscroll=function(){getScrollTop()>50?obj.style.display="":obj.style.display="none";}  
        obj.onclick=function(){
        	var goTop=setInterval(scrollMove,10);
        	function scrollMove(){
             setScrollTop(getScrollTop()/1.1);
             if(getScrollTop()<1)clearInterval(goTop);
        	}
        }
}

    
 