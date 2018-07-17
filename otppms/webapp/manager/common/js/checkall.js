
/* 是否全选标记 */
var checkedAll = false;
/* 全选/取消全选
* formName 所在form的name值
* checkboxName checkbox的name值
* 注意：所有checkbox的name值都必须一样，这样才能达到全选的效果
*/
function selectAll(cbk_total, ckbName) {
	var ckbAry = ckbArray(ckbName);
	for (var i = 0; i < ckbAry.length; i++) {
		var obj = ckbAry[i];
		obj.checked = cbk_total.checked;
	}
}

function ckbArray(ckbName) {
	var ckbAry = new Array();
	for (var i = 0, j = 0; i < document.getElementsByTagName("*").length; i++) {
		var obj = document.getElementsByTagName("*")[i];
		if (obj.tagName != "INPUT" || obj.type != "checkbox" || obj.name != ckbName || obj.disabled == true) {
			continue;
		}
		ckbAry[j++] = obj;
	}
	return ckbAry;
}

/* 检查是否有checkbox被选中
* formName 所在form的name值
* checkboxName checkbox的name值
* 注意：所有checkbox的name值都必须一样，这样才能达到全选的效果
*/
function checkAll(form) {
	var hasCheck = false;
	$("input[name=key_id]").each(function (i) {
		if (this.checked) {
			hasCheck = true;
		}
	});
	return hasCheck;
}

function checkAllForId(form,id) {
	var hasCheck = false;
	$("input[name="+id+"]").each(function (i) {
		if (this.checked) {
			hasCheck = true;
		}
	});
	return hasCheck;
}

