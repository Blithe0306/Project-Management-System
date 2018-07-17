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
m.put("010001", "010104,010005");//管理员添加-查询角色、授权管理员角色
m.put("010002", "010104,010005");//管理员编辑-查询角色、授权管理员角色
m.put("020001", "020104,030009");//添加用户-用户组查询、令牌查询
m.put("020102", "020104");//编辑用户-用户组查询 
m.put("020013", "030009");//更换令牌-令牌查询 
m.put("020005", "030009");//绑定令牌-令牌查询  
m.put("020006", "030009");//解绑令牌-令牌查询  
m.put("020009", "020004", "030009");//批量绑定-用户查询、令牌查询
m.put("040101", "040004");//添加代理- 认证服务器查询
m.put("040102", "040004");//编辑代理-认证服务器查询

/**
 *根据当前勾选的id在map中查找key
 *return key
 */
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
