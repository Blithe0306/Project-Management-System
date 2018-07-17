Array.prototype.remove = function (s) {
	for (var i = 0; i < this.length; i++) {
		if (s == this[i]) {
			this.splice(i, 1);
		}
	}
};
function Map() {
	/** 存放键的数组(遍历用到) */
	this.keys = new Array();
	/** 存放数据 */
	this.data = new Object();
	/**
	 * 放入一个键值对
	 * @param {String} key
	 * @param {Object} value
	 */
	this.put = function (key, value) {
		if (this.data[key] == null) {
			this.keys.push(key);
		}
		this.data[key] = value;
	};
	/**
	 * 获取某键对应的值
	 * @param {String} key
	 * @return {Object} value
	 */
	this.get = function (key) {
		return this.data[key];
	};
	/**
	 * 删除一个键值对
	 * @param {String} key
	 */
	this.remove = function (key) {
		this.keys.remove(key);
		this.data[key] = null;
	};
	/**
	 * 遍历Map,执行处理函数
	 * 
	 * @param {Function} 回调函数 function(key,value,index){..}
	 */
	this.each = function (fn) {
		if (typeof fn != "function") {
			return;
		}
		var len = this.keys.length;
		for (var i = 0; i < len; i++) {
			var k = this.keys[i];
			fn(k, this.data[k], i);
		}
	};
	/**
	 * 获取键值数组(类似Java的entrySet())
	 * @return 键值对象{key,value}的数组
	 */
	this.entrys = function () {
		var len = this.keys.length;
		var entrys = new Array(len);
		for (var i = 0; i < len; i++) {
			entrys[i] = {key:this.keys[i], value:this.data[i]};
		}
		return entrys;
	};
	/**
	 * 判断Map是否为空
	 */
	this.isEmpty = function () {
		return this.keys.length == 0;
	};
	/**
	 * 获取键值对数量
	 */
	this.size = function () {
		return this.keys.length;
	};
	/**
	 * 重写toString 
	 */
	this.toString = function () {
		var s = "{";
		for (var i = 0; i < this.keys.length; i++, s += ",") {
			var k = this.keys[i];
			s += k + "=" + this.data[k];
		}
		s += "}";
		return s;
	};
}



//功能模块关联对应数据封装
var m = new Map();
//点击任何一个操作权限————————关联勾选查询权限

// 首页授权
m.put("000002", "000001");

// 管理员
m.put("0101", "0100");
m.put("010101", "0100");//添加
m.put("010102", "0100");//编辑
m.put("010103", "0100");//删除
m.put("010104", "0100");//变更创建人
m.put("010105", "0100");//启用/禁用
m.put("010106", "0100");//锁定/解锁
m.put("010107", "0100");//修改密码
m.put("010108", "0100");//绑定令牌
m.put("010109", "0100");//解绑令牌
m.put("010110", "0100");//更换令牌

// 管理员角色
m.put("0103", "0102");
m.put("010301", "0102");//添加
m.put("010302", "0102");//编辑
m.put("010303", "0102");//删除
m.put("010304", "0102");//变更创建人


// 用户
m.put("0201", "0200");
m.put("020101", "0200");//添加
m.put("020102", "0200");//编辑
m.put("020103", "0200");//删除
m.put("020106", "0200");//更换令牌
m.put("020107", "0200");//设置静态密码
m.put("020108", "0200");//锁定/解锁
m.put("020109", "0200");//启用/禁用
m.put("020110", "0200");//添加Radius配置
m.put("020112", "0200");//设置后端认证
m.put("020113", "0200");//设置本地认证模式

// 令牌
m.put("0301", "0300");
m.put("030101", "0300");//启用/停用
m.put("030102", "0300");//锁定/解锁
m.put("030103", "0300");//挂失/解挂
m.put("030104", "0300");//作废
m.put("030105", "0300");//删除
m.put("030106", "0300");//令牌认证
m.put("030108", "0300");//设置应急口令
m.put("030109", "0300");//取消分配
m.put("030111", "0300");//获取激活码
m.put("030112", "0300");//获取一级解锁码
m.put("030113", "0300");//获取二级解锁码
m.put("030114", "0300");//软件令牌分发
m.put("0303", "0300");//软件令牌分发

m.put("040002", "040001");//添加----认证服务器列表
m.put("040003", "040001");//编辑----认证服务器列表
m.put("040004", "040001");//删除----认证服务器列表

m.put("040102", "040101");//添加----认证代理列表
m.put("040103", "040101");//编辑----认证代理列表
m.put("040104", "040101");//删除----认证代理列表
m.put('040105', '040101');//下载代理配置文件----认证代理列表
m.put('040106', '040101');//解除代理服务器----认证代理列表
m.put('040107', '040101');//启用/禁用----认证代理列表

m.put('040202','040201');//添加----后端认证列表
m.put('040203','040201');//编辑----后端认证列表
m.put('040204','040201');//删除----后端认证列表
m.put('040205','040201');//启用/禁用----后端认证列表

m.put('040302','040301');//添加----认证代理配置列表
m.put('040303','040301');//添加----认证代理配置列表
m.put('040304','040301');//添加----认证代理配置列表

m.put('060003','060002');//查看个人及由个人创建的其他管理员的日志----查看个人的管理日志

m.put('060101','060102');

var cm = new Map();
//级联取消

// 首页授权
cm.put("000001", "000002");

// 管理员
cm.put("0100", "0101,010101,010102,010103,010104,010105,010106,010107,010108,010109,010110");

// 管理员角色
cm.put("0102", "0103,010301,010302,010303,010304");

// 用户
cm.put("020001", "020003,020004,020005,020002");

// 令牌
cm.put("0300", "0301,030101,030102,030103,030104,030105,030106,030108,030109,030111,030112,030113,030114,0303");

cm.put("040001", "040002,040003,040004");	
cm.put("040101", "040102,040103,040104,040105,040106,040107");
cm.put("040201", "040202,040203,040204,040205");
cm.put("040301", "040302,040303,040304");
cm.put("060002","060001,060003"); 
cm.put("060003","060001"); 
cm.put("060102","060101"); 
cm.put('030001','030003,030004,030005,030002');
cm.put('030201','030203,030204,030202');

/**
 *根据当前勾选的id在map中查找value
 *return value
 */
function getcmkey(ck_key) {
	return cm.data[ck_key];
}


function getkey(ck_key) {
	var s = "";
	m.each(function (key, value, index) {
		if (ck_key == key) {
			s = key;
			return s;
		}
	});
	return s;
}
/**
 *根据当前勾选的id在map中查找key
 *return key关联的value 字符串
 */
function getkeys(key) {
	return m.data[key];
}
