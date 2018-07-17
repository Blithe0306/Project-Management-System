(function($){
	var methods={
			formatUdid:function() {
				$(this).keyup(function(){
					$(this).val($(this).val().replace(/\s(?=\d)/g,'').replace(/(\d{5})(?=\d)/g,"$1 "))
				})
			}
		}
		$.fn.inputFormat=function(method){
			if (methods[method]) {
	            return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
	        }else if (typeof method === 'object' || !method) {
	            return methods.init.apply(this, arguments);
	        }else {
	            $.error('Method ' + method + ' does not exist on jQuery.inputFormat');
	        }
		}
})(jQuery);

function Trim(str,is_global) {
     var result;
     result = str.replace(/(^\s+)|(\s+$)/g,"");
     if(is_global.toLowerCase()=="g"){
         result = result.replace(/\s/g,"");
     }
     return result;
}