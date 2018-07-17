(function($) {
    Function.prototype.ligerExtend = function(B, A) {
        if (typeof B != "function") return this;
        this.base = B.prototype;
        this.base.constructor = B;
        var _ = function() {};
        _.prototype = B.prototype;
        this.prototype = new _();
        this.prototype.constructor = this;
        if (A) $.extend(this.prototype, A)
    };
    Function.prototype.ligerDefer = function(A, $, _) {
        var B = this;
        return setTimeout(function() {
            B.apply(A, _ || [])
        }, $)
    };
    window.liger = $.ligerui = {
        version: "V1.1.9",
        managerCount: 0,
        managers: {},
        managerIdPrev: "ligerui",
        error: {
            managerIsExist: "\u7ba1\u7406\u5668id\u5df2\u7ecf\u5b58\u5728"
        },
        getId: function(_) {
            _ = _ || this.managerIdPrev;
            var $ = _ + (1000 + this.managerCount);
            this.managerCount++;
            return $
        },
        add: function($) {
            if (arguments.length == 2) {
                var _ = arguments[1];
                _.id = _.id || _.options.id || arguments[0].id;
                this.addManager(_);
                return
            }
            if (!$.id) $.id = this.getId($.__idPrev());
            if (this.managers[$.id]) throw new Error(this.error.managerIsExist);
            this.managers[$.id] = $
        },
        remove: function(_) {
            if (typeof _ == "string" || typeof _ == "number") delete $.ligerui.managers[_];
            else if (typeof _ == "object" && _ instanceof $.ligerui.core.Component) delete $.ligerui.managers[_.id]
        },
        get: function(_, A) {
            A = A || "ligeruiid";
            if (typeof _ == "string" || typeof _ == "number") return $.ligerui.managers[_];
            else if (typeof _ == "object" && _.length) {
                if (!_[0][A] && !$(_[0]).attr(A)) return null;
                return $.ligerui.managers[_[0][A] || $(_[0]).attr(A)]
            }
            return null
        },
        find: function(C) {
            var B = [];
            for (var _ in this.managers) {
                var A = this.managers[_];
                if (C instanceof Function) {
                    if (A instanceof C) B.push(A)
                } else if (C instanceof Array) {
                    if ($.inArray(A.__getType(), C) != -1) B.push(A)
                } else if (A.__getType() == C) B.push(A)
            }
            return B
        },
        run: function(E, B, D) {
            if (!E) return;
            D = $.extend({
                defaultsNamespace: "ligerDefaults",
                methodsNamespace: "ligerMethods",
                controlNamespace: "controls",
                idAttrName: "ligeruiid",
                isStatic: false,
                hasElement: true,
                propertyToElemnt: null
            }, D || {});
            E = E.replace(/^ligerGet/, "");
            E = E.replace(/^liger/, "");
            if (this == null || this == window || D.isStatic) {
                if (!$.ligerui.plugins[E]) $.ligerui.plugins[E] = {
                    fn: $["liger" + E],
                    isStatic: true
                };
                return new $.ligerui[D.controlNamespace][E]($.extend({}, $[D.defaultsNamespace][E] || {}, $[D.defaultsNamespace][E + "String"] || {}, B.length > 0 ? B[0] : {}))
            }
            if (!$.ligerui.plugins[E]) $.ligerui.plugins[E] = {
                fn: $.fn["liger" + E],
                isStatic: false
            };
            if (/Manager$/.test(E)) return $.ligerui.get(this, D.idAttrName);
            this.each(function() {
                if (this[D.idAttrName] || $(this).attr(D.idAttrName)) {
                    var _ = $.ligerui.get(this[D.idAttrName] || $(this).attr(D.idAttrName));
                    if (_ && B.length > 0) _.set(B[0]);
                    return
                }
                if (B.length >= 1 && typeof B[0] == "string") return;
                var C = B.length > 0 ? B[0] : null,
                    A = $.extend({}, $[D.defaultsNamespace][E] || {}, $[D.defaultsNamespace][E + "String"] || {}, C || {});
                if (D.propertyToElemnt) A[D.propertyToElemnt] = this;
                if (D.hasElement) new $.ligerui[D.controlNamespace][E](this, A);
                else new $.ligerui[D.controlNamespace][E](A)
            });
            if (this.length == 0) return null;
            if (B.length == 0) return $.ligerui.get(this, D.idAttrName);
            if (typeof B[0] == "object") return $.ligerui.get(this, D.idAttrName);
            if (typeof B[0] == "string") {
                var A = $.ligerui.get(this, D.idAttrName);
                if (A == null) return;
                if (B[0] == "option") {
                    if (B.length == 2) return A.get(B[1]);
                    else if (B.length >= 3) return A.set(B[1], B[2])
                } else {
                    var C = B[0];
                    if (!A[C]) return;
                    var _ = Array.apply(null, B);
                    _.shift();
                    return A[C].apply(A, _)
                }
            }
            return null
        },
        defaults: {},
        methods: {},
        core: {},
        controls: {},
        plugins: {}
    };
    $.ligerDefaults = {};
    $.ligerMethos = {};
    $.ligerui.defaults = $.ligerDefaults;
    $.ligerui.methods = $.ligerMethos;
    $.fn.liger = function(_) {
        if (_) return $.ligerui.run.call(this, _, arguments);
        else return $.ligerui.get(this)
    };
    $.ligerui.core.Component = function($) {
        this.events = this.events || {};
        this.options = $ || {};
        this.children = {}
    };
    $.extend($.ligerui.core.Component.prototype, {
        __getType: function() {
            return "$.ligerui.core.Component"
        },
        __idPrev: function() {
            return "ligerui"
        },
        set: function(_, C) {
            if (!_) return;
            if (typeof _ == "object") {
                var D;
                if (this.options != _) {
                    $.extend(this.options, _);
                    D = _
                } else D = $.extend({}, _);
                if (C == undefined || C == true) for (var B in D) if (B.indexOf("on") == 0) this.set(B, D[B]);
                if (C == undefined || C == false) for (B in D) if (B.indexOf("on") != 0) this.set(B, D[B]);
                return
            }
            var A = _;
            if (A.indexOf("on") == 0) {
                if (typeof C == "function") this.bind(A.substr(2), C);
                return
            }
            this.trigger("propertychange", _, C);
            if (!this.options) this.options = {};
            this.options[A] = C;
            var E = "_set" + A.substr(0, 1).toUpperCase() + A.substr(1);
            if (this[E]) this[E].call(this, C);
            this.trigger("propertychanged", _, C)
        },
        get: function($) {
            var _ = "_get" + $.substr(0, 1).toUpperCase() + $.substr(1);
            if (this[_]) return this[_].call(this, $);
            return this.options[$]
        },
        hasBind: function($) {
            var A = $.toLowerCase(),
                _ = this.events[A];
            if (_ && _.length) return true;
            return false
        },
        trigger: function($, C) {
            var B = $.toLowerCase(),
                A = this.events[B];
            if (!A) return;
            C = C || [];
            if ((C instanceof Array) == false) C = [C];
            for (var D = 0; D < A.length; D++) {
                var _ = A[D];
                if (_.handler.apply(_.context, C) == false) return false
            }
        },
        bind: function($, D, C) {
            if (typeof $ == "object") {
                for (var B in $) this.bind(B, $[B]);
                return
            }
            if (typeof D != "function") return false;
            var A = $.toLowerCase(),
                _ = this.events[A] || [];
            C = C || this;
            _.push({
                handler: D,
                context: C
            });
            this.events[A] = _
        },
        unbind: function($, D) {
            if (!$) {
                this.events = {};
                return
            }
            var A = $.toLowerCase(),
                _ = this.events[A];
            if (!_ || !_.length) return;
            if (!D) delete this.events[A];
            else for (var C = 0, B = _.length; C < B; C++) if (_[C].handler == D) {
                _.splice(C, 1);
                break
            }
        },
        destroy: function() {
            $.ligerui.remove(this)
        }
    });
    $.ligerui.core.UIComponent = function(_, B) {
        $.ligerui.core.UIComponent.base.constructor.call(this, B);
        var A = this._extendMethods();
        if (A) $.extend(this, A);
        this.element = _;
        this._init();
        this._preRender();
        this.trigger("render");
        this._render();
        this.trigger("rendered");
        this._rendered()
    };
    $.ligerui.core.UIComponent.ligerExtend($.ligerui.core.Component, {
        __getType: function() {
            return "$.ligerui.core.UIComponent"
        },
        _extendMethods: function() {},
        _init: function() {
            this.type = this.__getType();
            if (!this.element) this.id = this.options.id || $.ligerui.getId(this.__idPrev());
            else this.id = this.options.id || this.element.id || $.ligerui.getId(this.__idPrev());
            $.ligerui.add(this);
            if (!this.element) return;
            var attributes = this.attr();
            if (attributes && attributes instanceof Array) for (var i = 0; i < attributes.length; i++) {
                var name = attributes[i];
                this.options[name] = $(this.element).attr(name)
            }
            var p = this.options;
            if ($(this.element).attr("ligerui")) {
                try {
                    var attroptions = $(this.element).attr("ligerui");
                    if (attroptions.indexOf("{") != 0) attroptions = "{" + attroptions + "}";
                    eval("attroptions = " + attroptions + ";");
                    if (attroptions) $.extend(p, attroptions)
                } catch (e) {}
            }
        },
        _preRender: function() {},
        _render: function() {},
        _rendered: function() {
            if (this.element) $(this.element).attr("ligeruiid", this.id)
        },
        attr: function() {
            return []
        },
        destroy: function() {
            if (this.element) $(this.element).remove();
            this.options = null;
            $.ligerui.remove(this)
        }
    });
    $.ligerui.controls.Input = function(_, A) {
        $.ligerui.controls.Input.base.constructor.call(this, _, A)
    };
    $.ligerui.controls.Input.ligerExtend($.ligerui.core.UIComponent, {
        __getType: function() {
            return "$.ligerui.controls.Input"
        },
        attr: function() {
            return ["nullText"]
        },
        setValue: function($) {
            return this.set("value", $)
        },
        getValue: function() {
            return this.get("value")
        },
        setEnabled: function() {
            return this.set("disabled", false)
        },
        setDisabled: function() {
            return this.set("disabled", true)
        },
        updateStyle: function() {}
    });
    $.ligerui.win = {
        top: false,
        mask: function(A) {
            function _() {
                if (!$.ligerui.win.windowMask) return;
                var _ = $(window).height() + $(window).scrollTop();
                $.ligerui.win.windowMask.height(_)
            }
            if (!this.windowMask) {
                this.windowMask = $("<div class='l-window-mask' style='display: block;'></div>").appendTo("body");
                $(window).bind("resize.ligeruiwin", _);
                $(window).bind("scroll", _)
            }
            this.windowMask.show();
            _();
            this.masking = true
        },
        unmask: function(C) {
            var _ = $("body > .l-dialog:visible,body > .l-window:visible");
            for (var F = 0, B = _.length; F < B; F++) {
                var A = _.eq(F).attr("ligeruiid");
                if (C && C.id == A) continue;
                var D = $.ligerui.get(A);
                if (!D) continue;
                var E = D.get("modal");
                if (E) return
            }
            if (this.windowMask) this.windowMask.hide();
            this.masking = false
        },
        createTaskbar: function() {
            if (!this.taskbar) {
                this.taskbar = $("<div class=\"l-taskbar\"><div class=\"l-taskbar-tasks\"></div><div class=\"l-clear\"></div></div>").appendTo("body");
                if (this.top) this.taskbar.addClass("l-taskbar-top");
                this.taskbar.tasks = $(".l-taskbar-tasks:first", this.taskbar);
                this.tasks = {}
            }
            this.taskbar.show();
            this.taskbar.animate({
                bottom: 0
            });
            return this.taskbar
        },
        removeTaskbar: function() {
            var $ = this;
            $.taskbar.animate({
                bottom: -32
            }, function() {
                $.taskbar.remove();
                $.taskbar = null
            })
        },
        activeTask: function(A) {
            for (var _ in this.tasks) {
                var $ = this.tasks[_];
                if (_ == A.id) $.addClass("l-taskbar-task-active");
                else $.removeClass("l-taskbar-task-active")
            }
        },
        getTask: function(_) {
            var $ = this;
            if (!$.taskbar) return;
            if ($.tasks[_.id]) return $.tasks[_.id];
            return null
        },
        addTask: function(C) {
            var B = this;
            if (!B.taskbar) B.createTaskbar();
            if (B.tasks[C.id]) return B.tasks[C.id];
            var _ = C.get("title"),
                A = B.tasks[C.id] = $("<div class=\"l-taskbar-task\"><div class=\"l-taskbar-task-icon\"></div><div class=\"l-taskbar-task-content\">" + _ + "</div></div>");
            B.taskbar.tasks.append(A);
            B.activeTask(C);
            A.bind("click", function() {
                B.activeTask(C);
                if (C.actived) C.min();
                else C.active()
            }).hover(function() {
                $(this).addClass("l-taskbar-task-over")
            }, function() {
                $(this).removeClass("l-taskbar-task-over")
            });
            return A
        },
        hasTask: function() {
            for (var $ in this.tasks) if (this.tasks[$]) return true;
            return false
        },
        removeTask: function(_) {
            var $ = this;
            if (!$.taskbar) return;
            if ($.tasks[_.id]) {
                $.tasks[_.id].unbind();
                $.tasks[_.id].remove();
                delete $.tasks[_.id]
            }
            if (!$.hasTask()) $.removeTaskbar()
        },
        setFront: function(A) {
            var C = $.ligerui.find($.ligerui.core.Win);
            for (var B in C) {
                var _ = C[B];
                if (_ == A) {
                    $(_.element).css("z-index", "9200");
                    this.activeTask(_)
                } else $(_.element).css("z-index", "9100")
            }
        }
    };
    $.ligerui.core.Win = function(_, A) {
        $.ligerui.core.Win.base.constructor.call(this, _, A)
    };
    $.ligerui.core.Win.ligerExtend($.ligerui.core.UIComponent, {
        __getType: function() {
            return "$.ligerui.controls.Win"
        },
        mask: function() {
            if (this.options.modal) $.ligerui.win.mask(this)
        },
        unmask: function() {
            if (this.options.modal) $.ligerui.win.unmask(this)
        },
        min: function() {},
        max: function() {},
        active: function() {}
    });
    $.ligerui.draggable = {
        dragging: false
    };
    $.ligerui.resizable = {
        reszing: false
    };
    $.ligerui.toJSON = typeof JSON === "object" && JSON.stringify ? JSON.stringify : function(F) {
        var A = function($) {
                return $ < 10 ? "0" + $ : $
            },
            E = /[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
            C = function($) {
                E.lastIndex = 0;
                return E.test($) ? "\"" + $.replace(E, function(_) {
                    var $ = meta[_];
                    return typeof $ === "string" ? $ : "\\u" + ("0000" + _.charCodeAt(0).toString(16)).slice(-4)
                }) + "\"" : "\"" + $ + "\""
            };
        if (F === null) return "null";
        var H = typeof F;
        if (H === "undefined") return undefined;
        if (H === "string") return C(F);
        if (H === "number" || H === "boolean") return "" + F;
        if (H === "object") {
            if (typeof F.toJSON === "function") return $.ligerui.toJSON(F.toJSON());
            if (F.constructor === Date) return isFinite(this.valueOf()) ? this.getUTCFullYear() + "-" + A(this.getUTCMonth() + 1) + "-" + A(this.getUTCDate()) + "T" + A(this.getUTCHours()) + ":" + A(this.getUTCMinutes()) + ":" + A(this.getUTCSeconds()) + "Z" : null;
            var B = [];
            if (F.constructor === Array) {
                for (var J = 0, G = F.length; J < G; J++) B.push($.ligerui.toJSON(F[J]) || "null");
                return "[" + B.join(",") + "]"
            }
            var D, _;
            for (var I in F) {
                H = typeof I;
                if (H === "number") D = "\"" + I + "\"";
                else if (H === "string") D = C(I);
                else continue;
                H = typeof F[I];
                if (H === "function" || H === "undefined") continue;
                _ = $.ligerui.toJSON(F[I]);
                B.push(D + ":" + _)
            }
            return "{" + B.join(",") + "}"
        }
    }
})(jQuery);
(function($) {
    $.fn.ligerAccordion = function(_) {
        return $.ligerui.run.call(this, "ligerAccordion", arguments)
    };
    $.fn.ligerGetAccordionManager = function() {
        return $.ligerui.get(this)
    };
    $.ligerDefaults.Accordion = {
        height: null,
        speed: "normal",
        changeHeightOnResize: false,
        heightDiff: 0
    };
    $.ligerMethos.Accordion = {};
    $.ligerui.controls.Accordion = function(_, A) {
        $.ligerui.controls.Accordion.base.constructor.call(this, _, A)
    };
    $.ligerui.controls.Accordion.ligerExtend($.ligerui.core.UIComponent, {
        __getType: function() {
            return "Accordion"
        },
        __idPrev: function() {
            return "Accordion"
        },
        _extendMethods: function() {
            return $.ligerMethos.Accordion
        },
        _render: function() {
            var _ = this,
                B = this.options;
            _.accordion = $(_.element);
            if (!_.accordion.hasClass("l-accordion-panel")) _.accordion.addClass("l-accordion-panel");
            var A = 0;
            if ($("> div[lselected=true]", _.accordion).length > 0) A = $("> div", _.accordion).index($("> div[lselected=true]", _.accordion));
            $("> div", _.accordion).each(function(C, _) {
                var B = $("<div class=\"l-accordion-header\"><div class=\"l-accordion-toggle\"></div><div class=\"l-accordion-header-inner\"></div></div>");
                if (C == A) $(".l-accordion-toggle", B).addClass("l-accordion-toggle-open");
                if ($(_).attr("title")) {
                    $(".l-accordion-header-inner", B).html($(_).attr("title"));
                    $(_).attr("title", "")
                }
                $(_).before(B);
                if (!$(_).hasClass("l-accordion-content")) $(_).addClass("l-accordion-content")
            });
            $(".l-accordion-toggle", _.accordion).each(function() {
                if (!$(this).hasClass("l-accordion-toggle-open") && !$(this).hasClass("l-accordion-toggle-close")) $(this).addClass("l-accordion-toggle-close");
                if ($(this).hasClass("l-accordion-toggle-close")) $(this).parent().next(".l-accordion-content:visible").hide()
            });
            $(".l-accordion-header", _.accordion).hover(function() {
                $(this).addClass("l-accordion-header-over")
            }, function() {
                $(this).removeClass("l-accordion-header-over")
            });
            $(".l-accordion-toggle", _.accordion).hover(function() {
                if ($(this).hasClass("l-accordion-toggle-open")) $(this).addClass("l-accordion-toggle-open-over");
                else if ($(this).hasClass("l-accordion-toggle-close")) $(this).addClass("l-accordion-toggle-close-over")
            }, function() {
                if ($(this).hasClass("l-accordion-toggle-open")) $(this).removeClass("l-accordion-toggle-open-over");
                else if ($(this).hasClass("l-accordion-toggle-close")) $(this).removeClass("l-accordion-toggle-close-over")
            });
            $(">.l-accordion-header", _.accordion).click(function() {
                var _ = $(".l-accordion-toggle:first", this);
                if (_.hasClass("l-accordion-toggle-close")) {
                    _.removeClass("l-accordion-toggle-close").removeClass("l-accordion-toggle-close-over l-accordion-toggle-open-over");
                    _.addClass("l-accordion-toggle-open");
                    $(this).next(".l-accordion-content").show(B.speed).siblings(".l-accordion-content:visible").hide(B.speed);
                    $(this).siblings(".l-accordion-header").find(".l-accordion-toggle").removeClass("l-accordion-toggle-open").addClass("l-accordion-toggle-close")
                } else {
                    _.removeClass("l-accordion-toggle-open").removeClass("l-accordion-toggle-close-over l-accordion-toggle-open-over").addClass("l-accordion-toggle-close");
                    $(this).next(".l-accordion-content").hide(B.speed)
                }
            });
            _.headerHoldHeight = 0;
            $("> .l-accordion-header", _.accordion).each(function() {
                _.headerHoldHeight += $(this).height()
            });
            if (B.height && typeof(B.height) == "string" && B.height.indexOf("%") > 0) {
                _.onResize();
                if (B.changeHeightOnResize) $(window).resize(function() {
                    _.onResize()
                })
            } else if (B.height) {
                _.height = B.heightDiff + B.height;
                _.accordion.height(_.height);
                _.setHeight(B.height)
            } else _.header = _.accordion.height();
            _.set(B)
        },
        onResize: function() {
            var _ = this,
                A = this.options;
            if (!A.height || typeof(A.height) != "string" || A.height.indexOf("%") == -1) return false;
            if (_.accordion.parent()[0].tagName.toLowerCase() == "body") {
                var B = $(window).height();
                B -= parseInt(_.layout.parent().css("paddingTop"));
                B -= parseInt(_.layout.parent().css("paddingBottom"));
                _.height = A.heightDiff + B * parseFloat(_.height) * 0.01
            } else _.height = A.heightDiff + (_.accordion.parent().height() * parseFloat(A.height) * 0.01);
            _.accordion.height(_.height);
            _.setContentHeight(_.height - _.headerHoldHeight)
        },
        setHeight: function(A) {
            var _ = this,
                B = this.options;
            _.accordion.height(A);
            A -= _.headerHoldHeight;
            $("> .l-accordion-content", _.accordion).height(A)
        }
    })
})(jQuery);
(function($) {
    $.fn.ligerButton = function(_) {
        return $.ligerui.run.call(this, "ligerButton", arguments)
    };
    $.fn.ligerGetButtonManager = function() {
        return $.ligerui.run.call(this, "ligerGetButtonManager", arguments)
    };
    $.ligerDefaults.Button = {
        width: 100,
        text: "Button",
        disabled: false
    };
    $.ligerMethos.Button = {};
    $.ligerui.controls.Button = function(_, A) {
        $.ligerui.controls.Button.base.constructor.call(this, _, A)
    };
    $.ligerui.controls.Button.ligerExtend($.ligerui.controls.Input, {
        __getType: function() {
            return "Button"
        },
        __idPrev: function() {
            return "Button"
        },
        _extendMethods: function() {
            return $.ligerMethos.Button
        },
        _render: function() {
            var _ = this,
                A = this.options;
            _.button = $(_.element);
            _.button.addClass("l-btn");
            _.button.append("<div class=\"l-btn-l\"></div><div class=\"l-btn-r\"></div><span></span>");
            A.click && _.button.click(function() {
                if (!A.disabled) A.click()
            });
            _.set(A)
        },
        _setEnabled: function($) {
            if ($) this.button.removeClass("l-btn-disabled")
        },
        _setDisabled: function($) {
            if ($) {
                this.button.addClass("l-btn-disabled");
                this.options.disabled = true
            }
        },
        _setWidth: function($) {
            this.button.width($)
        },
        _setText: function(_) {
            $("span", this.button).html(_)
        },
        setValue: function($) {
            this.set("text", $)
        },
        getValue: function() {
            return this.options.text
        },
        setEnabled: function() {
            this.set("disabled", false)
        },
        setDisabled: function() {
            this.set("disabled", true)
        }
    })
})(jQuery);
(function($) {
    $.fn.ligerCheckBox = function(_) {
        return $.ligerui.run.call(this, "ligerCheckBox", arguments)
    };
    $.fn.ligerGetCheckBoxManager = function() {
        return $.ligerui.run.call(this, "ligerGetCheckBoxManager", arguments)
    };
    $.ligerDefaults.CheckBox = {
        disabled: false
    };
    $.ligerMethos.CheckBox = {};
    $.ligerui.controls.CheckBox = function(_, A) {
        $.ligerui.controls.CheckBox.base.constructor.call(this, _, A)
    };
    $.ligerui.controls.CheckBox.ligerExtend($.ligerui.controls.Input, {
        __getType: function() {
            return "CheckBox"
        },
        __idPrev: function() {
            return "CheckBox"
        },
        _extendMethods: function() {
            return $.ligerMethos.CheckBox
        },
        _render: function() {
            var _ = this,
                A = this.options;
            _.input = $(_.element);
            _.link = $("<a class=\"l-checkbox\"></a>");
            _.wrapper = _.input.addClass("l-hidden").wrap("<div class=\"l-checkbox-wrapper\"></div>").parent();
            _.wrapper.prepend(_.link);
            _.link.click(function() {
                if (_.input.attr("disabled")) return false;
                if (A.disabled) return false;
                if (_.trigger("beforeClick", [_.element]) == false) return false;
                if ($(this).hasClass("l-checkbox-checked")) _._setValue(false);
                else _._setValue(true);
                _.input.trigger("change")
            });
            _.wrapper.hover(function() {
                if (!A.disabled) $(this).addClass("l-over")
            }, function() {
                $(this).removeClass("l-over")
            });
            this.set(A);
            this.updateStyle()
        },
        _setCss: function($) {
            this.wrapper.css($)
        },
        _setValue: function(A) {
            var $ = this,
                _ = this.options;
            if (!A) {
                $.input[0].checked = false;
                $.link.removeClass("l-checkbox-checked")
            } else {
                $.input[0].checked = true;
                $.link.addClass("l-checkbox-checked")
            }
        },
        _setDisabled: function($) {
            if ($) {
                this.input.attr("disabled", true);
                this.wrapper.addClass("l-disabled")
            } else {
                this.input.attr("disabled", false);
                this.wrapper.removeClass("l-disabled")
            }
        },
        _getValue: function() {
            return this.element.checked
        },
        updateStyle: function() {
            if (this.input.attr("disabled")) {
                this.wrapper.addClass("l-disabled");
                this.options.disabled = true
            }
            if (this.input[0].checked) this.link.addClass("l-checkbox-checked");
            else this.link.removeClass("l-checkbox-checked")
        }
    })
})(jQuery);
(function($) {
    $.fn.ligerComboBox = function(_) {
        return $.ligerui.run.call(this, "ligerComboBox", arguments)
    };
    $.fn.ligerGetComboBoxManager = function() {
        return $.ligerui.run.call(this, "ligerGetComboBoxManager", arguments)
    };
    $.ligerDefaults.ComboBox = {
        resize: true,
        isMultiSelect: false,
        isShowCheckBox: false,
        columns: false,
        selectBoxWidth: false,
        selectBoxHeight: false,
        onBeforeSelect: false,
        onSelected: null,
        initValue: null,
        initText: null,
        valueField: "id",
        textField: "text",
        valueFieldID: null,
        slide: true,
        split: ";",
        data: null,
        tree: null,
        treeLeafOnly: true,
        grid: null,
        onStartResize: null,
        onEndResize: null,
        hideOnLoseFocus: true,
        url: null,
        onSuccess: null,
        onError: null,
        onBeforeOpen: null,
        render: null,
        absolute: true
    };
    $.ligerMethos.ComboBox = $.ligerMethos.ComboBox || {};
    $.ligerui.controls.ComboBox = function(_, A) {
        $.ligerui.controls.ComboBox.base.constructor.call(this, _, A)
    };
    $.ligerui.controls.ComboBox.ligerExtend($.ligerui.controls.Input, {
        __getType: function() {
            return "ComboBox"
        },
        _extendMethods: function() {
            return $.ligerMethos.ComboBox
        },
        _init: function() {
            $.ligerui.controls.ComboBox.base._init.call(this);
            var _ = this.options;
            if (_.columns) _.isShowCheckBox = true;
            if (_.isMultiSelect) _.isShowCheckBox = true
        },
        _render: function() {
            var _ = this,
                A = this.options;
            _.data = A.data;
            _.inputText = null;
            _.select = null;
            _.textFieldID = "";
            _.valueFieldID = "";
            _.valueField = null;
            if (this.element.tagName.toLowerCase() == "input") {
                this.element.readOnly = true;
                _.inputText = $(this.element);
                _.textFieldID = this.element.id
            } else if (this.element.tagName.toLowerCase() == "select") {
                $(this.element).hide();
                _.select = $(this.element);
                A.isMultiSelect = false;
                A.isShowCheckBox = false;
                _.textFieldID = this.element.id + "_txt";
                _.inputText = $("<input type=\"text\" readonly=\"true\"/>");
                _.inputText.attr("id", _.textFieldID).insertAfter($(this.element))
            } else return;
            if (_.inputText[0].name == undefined) _.inputText[0].name = _.textFieldID;
            _.valueField = null;
            if (A.valueFieldID) {
                _.valueField = $("#" + A.valueFieldID + ":input");
                if (_.valueField.length == 0) _.valueField = $("<input type=\"hidden\"/>");
                _.valueField[0].id = _.valueField[0].name = A.valueFieldID
            } else {
                _.valueField = $("<input type=\"hidden\"/>");
                _.valueField[0].id = _.valueField[0].name = _.textFieldID + "_val"
            }
            if (_.valueField[0].name == undefined) _.valueField[0].name = _.valueField[0].id;
            _.link = $("<div class=\"l-trigger\"><div class=\"l-trigger-icon\"></div></div>");
            _.selectBox = $("<div class=\"l-box-select\"><div class=\"l-box-select-inner\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"l-box-select-table\"></table></div></div>");
            _.selectBox.table = $("table:first", _.selectBox);
            _.wrapper = _.inputText.wrap("<div class=\"l-text l-text-combobox\"></div>").parent();
            _.wrapper.append("<div class=\"l-text-l\"></div><div class=\"l-text-r\"></div>");
            _.wrapper.append(_.link);
            _.textwrapper = _.wrapper.wrap("<div class=\"l-text-wrapper\"></div>").parent();
            if (A.absolute) _.selectBox.appendTo("body").addClass("l-box-select-absolute");
            else _.textwrapper.append(_.selectBox);
            _.textwrapper.append(_.valueField);
            _.inputText.addClass("l-text-field");
            if (A.isShowCheckBox && !_.select) $("table", _.selectBox).addClass("l-table-checkbox");
            else {
                A.isShowCheckBox = false;
                $("table", _.selectBox).addClass("l-table-nocheckbox")
            }
            _.link.hover(function() {
                if (A.disabled) return;
                this.className = "l-trigger-hover"
            }, function() {
                if (A.disabled) return;
                this.className = "l-trigger"
            }).mousedown(function() {
                if (A.disabled) return;
                this.className = "l-trigger-pressed"
            }).mouseup(function() {
                if (A.disabled) return;
                this.className = "l-trigger-hover"
            }).click(function() {
                if (A.disabled) return;
                if (_.trigger("beforeOpen") == false) return false;
                _._toggleSelectBox(_.selectBox.is(":visible"))
            });
            _.inputText.click(function() {
                if (A.disabled) return;
                if (_.trigger("beforeOpen") == false) return false;
                _._toggleSelectBox(_.selectBox.is(":visible"))
            }).blur(function() {
                if (A.disabled) return;
                _.wrapper.removeClass("l-text-focus")
            }).focus(function() {
                if (A.disabled) return;
                _.wrapper.addClass("l-text-focus")
            });
            _.wrapper.hover(function() {
                if (A.disabled) return;
                _.wrapper.addClass("l-text-over")
            }, function() {
                if (A.disabled) return;
                _.wrapper.removeClass("l-text-over")
            });
            _.resizing = false;
            _.selectBox.hover(null, function($) {
                if (A.hideOnLoseFocus && _.selectBox.is(":visible") && !_.boxToggling && !_.resizing) _._toggleSelectBox(true)
            });
            var B = $("tr", _.selectBox.table).length;
            if (!A.selectBoxHeight && B < 8) A.selectBoxHeight = B * 30;
            if (A.selectBoxHeight) _.selectBox.height(A.selectBoxHeight);
            _.bulidContent();
            _.set(A);
            if (A.selectBoxWidth) _.selectBox.width(A.selectBoxWidth);
            else _.selectBox.css("width", _.wrapper.css("width"))
        },
        destroy: function() {
            if (this.wrapper) this.wrapper.remove();
            if (this.selectBox) this.selectBox.remove();
            this.options = null;
            $.ligerui.remove(this)
        },
        _setDisabled: function($) {
            if ($) this.wrapper.addClass("l-text-disabled");
            else this.wrapper.removeClass("l-text-disabled")
        },
        _setLable: function(B) {
            var _ = this,
                A = this.options;
            if (B) {
                if (_.labelwrapper) _.labelwrapper.find(".l-text-label:first").html(B + ":&nbsp");
                else {
                    _.labelwrapper = _.textwrapper.wrap("<div class=\"l-labeltext\"></div>").parent();
                    _.labelwrapper.prepend("<div class=\"l-text-label\" style=\"float:left;display:inline;\">" + B + ":&nbsp</div>");
                    _.textwrapper.css("float", "left")
                }
                if (!A.labelWidth) A.labelWidth = $(".l-text-label", _.labelwrapper).outerWidth();
                else $(".l-text-label", _.labelwrapper).outerWidth(A.labelWidth);
                $(".l-text-label", _.labelwrapper).width(A.labelWidth);
                $(".l-text-label", _.labelwrapper).height(_.wrapper.height());
                _.labelwrapper.append("<br style=\"clear:both;\" />");
                if (A.labelAlign) $(".l-text-label", _.labelwrapper).css("text-align", A.labelAlign);
                _.textwrapper.css({
                    display: "inline"
                });
                _.labelwrapper.width(_.wrapper.outerWidth() + A.labelWidth + 2)
            }
        },
        _setWidth: function(_) {
            var $ = this;
            if (_ > 20) {
                $.wrapper.css({
                    width: _
                });
                $.inputText.css({
                    width: _ - 20
                });
                $.textwrapper.css({
                    width: _
                })
            }
        },
        _setHeight: function(_) {
            var $ = this;
            if (_ > 10) {
                $.wrapper.height(_);
                $.inputText.height(_ - 2);
                $.link.height(_ - 4);
                $.textwrapper.css({
                    width: _
                })
            }
        },
        _setResize: function(A) {
            if (A && $.fn.ligerResizable) {
                var _ = this;
                _.selectBox.ligerResizable({
                    handles: "se,s,e",
                    onStartResize: function() {
                        _.resizing = true;
                        _.trigger("startResize")
                    },
                    onEndResize: function() {
                        _.resizing = false;
                        if (_.trigger("endResize") == false) return false
                    }
                });
                _.selectBox.append("<div class='l-btn-nw-drop'></div>")
            }
        },
        findTextByValue: function(D) {
            var A = this,
                C = this.options;
            if (D == undefined) return "";
            var _ = "",
                B = function($) {
                    var _ = D.toString().split(C.split);
                    for (var A = 0; A < _.length; A++) if (_[A] == $) return true;
                    return false
                };
            $(A.data).each(function(E, A) {
                var $ = A[C.valueField],
                    D = A[C.textField];
                if (B($)) _ += D + C.split
            });
            if (_.length > 0) _ = _.substr(0, _.length - 1);
            return _
        },
        findValueByText: function(A) {
            var _ = this,
                D = this.options;
            if (!A && A == "") return "";
            var C = function($) {
                    var _ = A.toString().split(D.split);
                    for (var B = 0; B < _.length; B++) if (_[B] == $) return true;
                    return false
                },
                B = "";
            $(_.data).each(function(E, _) {
                var $ = _[D.valueField],
                    A = _[D.textField];
                if (C(A)) B += $ + D.split
            });
            if (B.length > 0) B = B.substr(0, B.length - 1);
            return B
        },
        removeItem: function() {},
        insertItem: function() {},
        addItem: function() {},
        _setValue: function(D) {
            var _ = this,
                C = this.options,
                A = _.findTextByValue(D);
            if (C.tree) _.selectValueByTree(D);
            else if (!C.isMultiSelect) {
                _._changeValue(D, A);
                $("tr[value=" + D + "] td", _.selectBox).addClass("l-selected");
                $("tr[value!=" + D + "] td", _.selectBox).removeClass("l-selected")
            } else {
                _._changeValue(D, A);
                var B = D.toString().split(C.split);
                $("table.l-table-checkbox :checkbox", _.selectBox).each(function() {
                    this.checked = false
                });
                for (var E = 0; E < B.length; E++) $("table.l-table-checkbox tr[value=" + B[E] + "] :checkbox", _.selectBox).each(function() {
                    this.checked = true
                })
            }
        },
        selectValue: function($) {
            this._setValue($)
        },
        bulidContent: function() {
            var _ = this,
                A = this.options;
            this.clearContent();
            if (_.select) _.setSelect();
            else if (_.data) _.setData(_.data);
            else if (A.tree) _.setTree(A.tree);
            else if (A.grid) _.setGrid(A.grid);
            else if (A.url) $.ajax({
                type: "post",
                url: A.url,
                cache: false,
                dataType: "json",
                success: function($) {
                    _.data = $;
                    _.setData(_.data);
                    _.trigger("success", [_.data])
                },
                error: function(A, $) {
                    _.trigger("error", [A, $])
                }
            })
        },
        clearContent: function() {
            var _ = this,
                A = this.options;
            $("table", _.selectBox).html("")
        },
        setSelect: function() {
            var _ = this,
                A = this.options;
            this.clearContent();
            $("option", _.select).each(function(D) {
                var A = $(this).val(),
                    C = $(this).html(),
                    B = $("<tr><td index='" + D + "' value='" + A + "'>" + C + "</td>");
                $("table.l-table-nocheckbox", _.selectBox).append(B);
                $("td", B).hover(function() {
                    $(this).addClass("l-over")
                }, function() {
                    $(this).removeClass("l-over")
                })
            });
            $("td:eq(" + _.select[0].selectedIndex + ")", _.selectBox).each(function() {
                if ($(this).hasClass("l-selected")) {
                    _.selectBox.hide();
                    return
                }
                $(".l-selected", _.selectBox).removeClass("l-selected");
                $(this).addClass("l-selected");
                if (_.select[0].selectedIndex != $(this).attr("index") && _.select[0].onchange) {
                    _.select[0].selectedIndex = $(this).attr("index");
                    _.select[0].onchange()
                }
                var D = parseInt($(this).attr("index"));
                _.select[0].selectedIndex = D;
                _.select.trigger("change");
                _.selectBox.hide();
                var C = $(this).attr("value"),
                    B = $(this).html();
                if (A.render) _.inputText.val(A.render(C, B));
                else _.inputText.val(B)
            });
            _._addClickEven()
        },
        setData: function(E) {
            var A = this,
                D = this.options;
            this.clearContent();
            if (!E || !E.length) return;
            if (A.data != E) A.data = E;
            if (D.columns) {
                A.selectBox.table.headrow = $("<tr class='l-table-headerow'><td width='18px'></td></tr>");
                A.selectBox.table.append(A.selectBox.table.headrow);
                A.selectBox.table.addClass("l-box-select-grid");
                for (var F = 0; F < D.columns.length; F++) {
                    var B = $("<td columnindex='" + F + "' columnname='" + D.columns[F].name + "'>" + D.columns[F].header + "</td>");
                    if (D.columns[F].width) B.width(D.columns[F].width);
                    A.selectBox.table.headrow.append(B)
                }
            }
            for (var H = 0; H < E.length; H++) {
                var _ = E[H][D.valueField],
                    G = E[H][D.textField];
                if (!D.columns) {
                    $("table.l-table-checkbox", A.selectBox).append("<tr value='" + _ + "'><td style='width:18px;'  index='" + H + "' value='" + _ + "' text='" + G + "' ><input type='checkbox' /></td><td index='" + H + "' value='" + _ + "' align='left'>" + G + "</td>");
                    $("table.l-table-nocheckbox", A.selectBox).append("<tr value='" + _ + "'><td index='" + H + "' value='" + _ + "' align='left'>" + G + "</td>")
                } else {
                    var C = $("<tr value='" + _ + "'><td style='width:18px;'  index='" + H + "' value='" + _ + "' text='" + G + "' ><input type='checkbox' /></td></tr>");
                    $("td", A.selectBox.table.headrow).each(function() {
                        var _ = $(this).attr("columnname");
                        if (_) {
                            var A = $("<td>" + E[H][_] + "</td>");
                            C.append(A)
                        }
                    });
                    A.selectBox.table.append(C)
                }
            }
            if (D.isShowCheckBox && $.fn.ligerCheckBox) $("table input:checkbox", A.selectBox).ligerCheckBox();
            $(".l-table-checkbox input:checkbox", A.selectBox).change(function() {
                if (this.checked && A.hasBind("beforeSelect")) {
                    var _ = null;
                    if ($(this).parent().get(0).tagName.toLowerCase() == "div") _ = $(this).parent().parent();
                    else _ = $(this).parent();
                    if (_ != null && A.trigger("beforeSelect", [_.attr("value"), _.attr("text")]) == false) {
                        A.selectBox.slideToggle("fast");
                        return false
                    }
                }
                if (!D.isMultiSelect) if (this.checked) {
                    $("input:checked", A.selectBox).not(this).each(function() {
                        this.checked = false;
                        $(".l-checkbox-checked", $(this).parent()).removeClass("l-checkbox-checked")
                    });
                    A.selectBox.slideToggle("fast")
                }
                A._checkboxUpdateValue()
            });
            $("table.l-table-nocheckbox td", A.selectBox).hover(function() {
                $(this).addClass("l-over")
            }, function() {
                $(this).removeClass("l-over")
            });
            A._addClickEven();
            A._dataInit()
        },
        setTree: function(A) {
            var _ = this,
                B = this.options;
            this.clearContent();
            _.selectBox.table.remove();
            if (A.checkbox != false) A.onCheck = function() {
                var C = _.treeManager.getChecked(),
                    D = [],
                    A = [];
                $(C).each(function(_, $) {
                    if (B.treeLeafOnly && $.data.children) return;
                    D.push($.data[B.valueField]);
                    A.push($.data[B.textField])
                });
                _._changeValue(D.join(B.split), A.join(B.split))
            };
            else {
                A.onSelect = function(A) {
                    if (B.treeLeafOnly && A.data.children) return;
                    var C = A.data[B.valueField],
                        $ = A.data[B.textField];
                    _._changeValue(C, $)
                };
                A.onCancelSelect = function($) {
                    _._changeValue("", "")
                }
            }
            A.onAfterAppend = function(A, C) {
                if (!_.treeManager) return;
                var $ = null;
                if (B.initValue) $ = B.initValue;
                else if (_.valueField.val() != "") $ = _.valueField.val();
                _.selectValueByTree($)
            };
            _.tree = $("<ul></ul>");
            $("div:first", _.selectBox).append(_.tree);
            _.tree.ligerTree(A);
            _.treeManager = _.tree.ligerGetTreeManager()
        },
        selectValueByTree: function(D) {
            var _ = this,
                C = this.options;
            if (D != null) {
                var A = "",
                    B = D.toString().split(C.split);
                $(B).each(function(D, $) {
                    _.treeManager.selectNode($.toString());
                    A += _.treeManager.getTextByID($);
                    if (D < B.length - 1) A += C.split
                });
                _._changeValue(D, A)
            }
        },
        setGrid: function(C) {
            var _ = this,
                B = this.options;
            this.clearContent();
            _.selectBox.table.remove();
            _.grid = $("div:first", _.selectBox);
            C.columnWidth = C.columnWidth || 120;
            C.width = "100%";
            C.height = "100%";
            C.heightDiff = -2;
            C.InWindow = false;
            _.gridManager = _.grid.ligerGrid(C);
            B.hideOnLoseFocus = false;
            if (C.checkbox != false) {
                var A = function() {
                        var C = _.gridManager.getCheckedRows(),
                            D = [],
                            A = [];
                        $(C).each(function(_, $) {
                            D.push($[B.valueField]);
                            A.push($[B.textField])
                        });
                        _._changeValue(D.join(B.split), A.join(B.split))
                    };
                _.gridManager.bind("CheckAllRow", A);
                _.gridManager.bind("CheckRow", A)
            } else {
                _.gridManager.bind("SelectRow", function(E, C, A) {
                    var D = E[B.valueField],
                        $ = E[B.textField];
                    _._changeValue(D, $)
                });
                _.gridManager.bind("UnSelectRow", function(B, A, $) {
                    _._changeValue("", "")
                })
            }
            _.bind("show", function() {
                if (_.gridManager) _.gridManager._updateFrozenWidth()
            });
            _.bind("endResize", function() {
                if (_.gridManager) {
                    _.gridManager._updateFrozenWidth();
                    _.gridManager.setHeight(_.selectBox.height() - 2)
                }
            })
        },
        _getValue: function() {
            return $(this.valueField).val()
        },
        getValue: function() {
            return this._getValue()
        },
        updateStyle: function() {
            var $ = this,
                _ = this.options;
            $._dataInit()
        },
        _dataInit: function() {
            var _ = this,
                B = this.options,
                C = null;
            if (B.initValue != null && B.initText != null) _._changeValue(B.initValue, B.initText);
            if (B.initValue != null) {
                C = B.initValue;
                if (B.tree) {
                    if (C) _.selectValueByTree(C)
                } else {
                    var A = _.findTextByValue(C);
                    _._changeValue(C, A)
                }
            } else if (B.initText != null) {
                C = _.findValueByText(B.initText);
                _._changeValue(C, B.initText)
            } else if (_.valueField.val() != "") {
                C = _.valueField.val();
                if (B.tree) {
                    if (C) _.selectValueByTree(C)
                } else {
                    A = _.findTextByValue(C);
                    _._changeValue(C, A)
                }
            }
            if (!B.isShowCheckBox && C != null) $("table tr", _.selectBox).find("td:first").each(function() {
                if (C == $(this).attr("value")) $(this).addClass("l-selected")
            });
            if (B.isShowCheckBox && C != null) $(":checkbox", _.selectBox).each(function() {
                var D = null,
                    _ = $(this);
                if (_.parent().get(0).tagName.toLowerCase() == "div") D = _.parent().parent();
                else D = _.parent();
                if (D == null) return;
                var A = C.toString().split(B.split);
                $(A).each(function(B, A) {
                    if (A == D.attr("value")) {
                        $(".l-checkbox", D).addClass("l-checkbox-checked");
                        _[0].checked = true
                    }
                })
            })
        },
        _changeValue: function(_, A) {
            var $ = this,
                B = this.options;
            $.valueField.val(_);
            if (B.render) $.inputText.val(B.render(_, A));
            else $.inputText.val(A);
            $.selectedValue = _;
            $.selectedText = A;
            $.inputText.trigger("change").focus();
            $.trigger("selected", [_, A])
        },
        _checkboxUpdateValue: function() {
            var _ = this,
                A = this.options,
                B = "",
                C = "";
            $("input:checked", _.selectBox).each(function() {
                var _ = null;
                if ($(this).parent().get(0).tagName.toLowerCase() == "div") _ = $(this).parent().parent();
                else _ = $(this).parent();
                if (!_) return;
                B += _.attr("value") + A.split;
                C += _.attr("text") + A.split
            });
            if (B.length > 0) B = B.substr(0, B.length - 1);
            if (C.length > 0) C = C.substr(0, C.length - 1);
            _._changeValue(B, C)
        },
        _addClickEven: function() {
            var _ = this,
                A = this.options;
            $(".l-table-nocheckbox td", _.selectBox).click(function() {
                var D = $(this).attr("value"),
                    C = parseInt($(this).attr("index")),
                    B = $(this).html();
                if (_.hasBind("beforeSelect") && _.trigger("beforeSelect", [D, B]) == false) {
                    if (A.slide) _.selectBox.slideToggle("fast");
                    else _.selectBox.hide();
                    return false
                }
                if ($(this).hasClass("l-selected")) {
                    if (A.slide) _.selectBox.slideToggle("fast");
                    else _.selectBox.hide();
                    return
                }
                $(".l-selected", _.selectBox).removeClass("l-selected");
                $(this).addClass("l-selected");
                if (_.select) if (_.select[0].selectedIndex != C) {
                    _.select[0].selectedIndex = C;
                    _.select.trigger("change")
                }
                if (A.slide) {
                    _.boxToggling = true;
                    _.selectBox.hide("fast", function() {
                        _.boxToggling = false
                    })
                } else _.selectBox.hide();
                _._changeValue(D, B)
            })
        },
        updateSelectBoxPosition: function() {
            var _ = this,
                C = this.options;
            if (C.absolute) _.selectBox.css({
                left: _.wrapper.offset().left,
                top: _.wrapper.offset().top + 1 + _.wrapper.outerHeight()
            });
            else {
                var B = _.wrapper.offset().top - $(window).scrollTop(),
                    A = _.selectBox.height() + textHeight + 4;
                if (B + A > $(window).height() && B > A) _.selectBox.css("marginTop", -1 * (_.selectBox.height() + textHeight + 5))
            }
        },
        _toggleSelectBox: function(D) {
            var A = this,
                B = this.options,
                _ = A.wrapper.height();
            A.boxToggling = true;
            if (D) {
                if (B.slide) A.selectBox.slideToggle("fast", function() {
                    A.boxToggling = false
                });
                else {
                    A.selectBox.hide();
                    A.boxToggling = false
                }
            } else {
                A.updateSelectBoxPosition();
                if (B.slide) A.selectBox.slideToggle("fast", function() {
                    A.boxToggling = false;
                    if (!B.isShowCheckBox && $("td.l-selected", A.selectBox).length > 0) {
                        var _ = ($("td.l-selected", A.selectBox).offset().top - A.selectBox.offset().top);
                        $(".l-box-select-inner", A.selectBox).animate({
                            scrollTop: _
                        })
                    }
                });
                else {
                    A.selectBox.show();
                    A.boxToggling = false;
                    if (!A.tree && !A.grid && !B.isShowCheckBox && $("td.l-selected", A.selectBox).length > 0) {
                        var C = ($("td.l-selected", A.selectBox).offset().top - A.selectBox.offset().top);
                        $(".l-box-select-inner", A.selectBox).animate({
                            scrollTop: C
                        })
                    }
                }
            }
            A.isShowed = A.selectBox.is(":visible");
            A.trigger("toggle", [D]);
            A.trigger(D ? "hide" : "show")
        }
    });
    $.ligerui.controls.ComboBox.prototype.setValue = $.ligerui.controls.ComboBox.prototype.selectValue;
    $.ligerui.controls.ComboBox.prototype.setInputValue = $.ligerui.controls.ComboBox.prototype._changeValue
})(jQuery);
(function($) {
    $.fn.ligerDateEditor = function() {
        return $.ligerui.run.call(this, "ligerDateEditor", arguments)
    };
    $.fn.ligerGetDateEditorManager = function() {
        return $.ligerui.run.call(this, "ligerGetDateEditorManager", arguments)
    };
    $.ligerDefaults.DateEditor = {
        format: "yyyy-MM-dd hh:mm",
        showTime: false,
        onChangeDate: false,
        absolute: true
    };
    $.ligerDefaults.DateEditorString = {
        dayMessage: ["\u65e5", "\u4e00", "\u4e8c", "\u4e09", "\u56db", "\u4e94", "\u516d"],
        monthMessage: ["\u4e00\u6708", "\u4e8c\u6708", "\u4e09\u6708", "\u56db\u6708", "\u4e94\u6708", "\u516d\u6708", "\u4e03\u6708", "\u516b\u6708", "\u4e5d\u6708", "\u5341\u6708", "\u5341\u4e00\u6708", "\u5341\u4e8c\u6708"],
        todayMessage: "\u4eca\u5929",
        closeMessage: "\u5173\u95ed"
    };
    $.ligerMethos.DateEditor = {};
    $.ligerui.controls.DateEditor = function(_, A) {
        $.ligerui.controls.DateEditor.base.constructor.call(this, _, A)
    };
    $.ligerui.controls.DateEditor.ligerExtend($.ligerui.controls.Input, {
        __getType: function() {
            return "DateEditor"
        },
        __idPrev: function() {
            return "DateEditor"
        },
        _extendMethods: function() {
            return $.ligerMethos.DateEditor
        },
        _render: function() {
            var _ = this,
                B = this.options;
            if (!B.showTime && B.format.indexOf(" hh:mm") > -1) B.format = B.format.replace(" hh:mm", "");
            if (this.element.tagName.toLowerCase() != "input" || this.element.type != "text") return;
            _.inputText = $(this.element);
            if (!_.inputText.hasClass("l-text-field")) _.inputText.addClass("l-text-field");
            _.link = $("<div class=\"l-trigger\"><div class=\"l-trigger-icon\"></div></div>");
            _.text = _.inputText.wrap("<div class=\"l-text l-text-date\"></div>").parent();
            _.text.append("<div class=\"l-text-l\"></div><div class=\"l-text-r\"></div>");
            _.text.append(_.link);
            _.textwrapper = _.text.wrap("<div class=\"l-text-wrapper\"></div>").parent();
            var C = "";
            C += "<div class='l-box-dateeditor' style='display:none'>";
            C += "    <div class='l-box-dateeditor-header'>";
            C += "        <div class='l-box-dateeditor-header-btn l-box-dateeditor-header-prevyear'><span></span></div>";
            C += "        <div class='l-box-dateeditor-header-btn l-box-dateeditor-header-prevmonth'><span></span></div>";
            C += "        <div class='l-box-dateeditor-header-text'><a class='l-box-dateeditor-header-month'></a> , <a  class='l-box-dateeditor-header-year'></a></div>";
            C += "        <div class='l-box-dateeditor-header-btn l-box-dateeditor-header-nextmonth'><span></span></div>";
            C += "        <div class='l-box-dateeditor-header-btn l-box-dateeditor-header-nextyear'><span></span></div>";
            C += "    </div>";
            C += "    <div class='l-box-dateeditor-body'>";
            C += "        <table cellpadding='0' cellspacing='0' border='0' class='l-box-dateeditor-calendar'>";
            C += "            <thead>";
            C += "                <tr><td align='center'></td><td align='center'></td><td align='center'></td><td align='center'></td><td align='center'></td><td align='center'></td><td align='center'></td></tr>";
            C += "            </thead>";
            C += "            <tbody>";
            C += "                <tr class='l-first'><td align='center'></td><td align='center'></td><td align='center'></td><td align='center'></td><td align='center'></td><td align='center'></td><td align='center'></td></tr><tr><td align='center'></td><td align='center'></td><td align='center'></td><td align='center'></td><td align='center'></td><td align='center'></td><td align='center'></td></tr><tr><td align='center'></td><td align='center'></td><td align='center'></td><td align='center'></td><td align='center'></td><td align='center'></td><td align='center'></td></tr><tr><td align='center'></td><td align='center'></td><td align='center'></td><td align='center'></td><td align='center'></td><td align='center'></td><td align='center'></td></tr><tr><td align='center'></td><td align='center'></td><td align='center'></td><td align='center'></td><td align='center'></td><td align='center'></td><td align='center'></td></tr><tr><td align='center'></td><td align='center'></td><td align='center'></td><td align='center'></td><td align='center'></td><td align='center'></td><td align='center'></td></tr>";
            C += "            </tbody>";
            C += "        </table>";
            C += "        <ul class='l-box-dateeditor-monthselector'><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li></ul>";
            C += "        <ul class='l-box-dateeditor-yearselector'><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li></ul>";
            C += "        <ul class='l-box-dateeditor-hourselector'><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li></ul>";
            C += "        <ul class='l-box-dateeditor-minuteselector'><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li><li></li></ul>";
            C += "    </div>";
            C += "    <div class='l-box-dateeditor-toolbar'>";
            C += "        <div class='l-box-dateeditor-time'></div>";
            C += "        <div class='l-button l-button-today'></div>";
            C += "        <div class='l-button l-button-close'></div>";
            C += "        <div class='l-clear'></div>";
            C += "    </div>";
            C += "</div>";
            _.dateeditor = $(C);
            if (B.absolute) _.dateeditor.appendTo("body").addClass("l-box-dateeditor-absolute");
            else _.textwrapper.append(_.dateeditor);
            _.header = $(".l-box-dateeditor-header", _.dateeditor);
            _.body = $(".l-box-dateeditor-body", _.dateeditor);
            _.toolbar = $(".l-box-dateeditor-toolbar", _.dateeditor);
            _.body.thead = $("thead", _.body);
            _.body.tbody = $("tbody", _.body);
            _.body.monthselector = $(".l-box-dateeditor-monthselector", _.body);
            _.body.yearselector = $(".l-box-dateeditor-yearselector", _.body);
            _.body.hourselector = $(".l-box-dateeditor-hourselector", _.body);
            _.body.minuteselector = $(".l-box-dateeditor-minuteselector", _.body);
            _.toolbar.time = $(".l-box-dateeditor-time", _.toolbar);
            _.toolbar.time.hour = $("<a></a>");
            _.toolbar.time.minute = $("<a></a>");
            _.buttons = {
                btnPrevYear: $(".l-box-dateeditor-header-prevyear", _.header),
                btnNextYear: $(".l-box-dateeditor-header-nextyear", _.header),
                btnPrevMonth: $(".l-box-dateeditor-header-prevmonth", _.header),
                btnNextMonth: $(".l-box-dateeditor-header-nextmonth", _.header),
                btnYear: $(".l-box-dateeditor-header-year", _.header),
                btnMonth: $(".l-box-dateeditor-header-month", _.header),
                btnToday: $(".l-button-today", _.toolbar),
                btnClose: $(".l-button-close", _.toolbar)
            };
            var A = new Date();
            _.now = {
                year: A.getFullYear(),
                month: A.getMonth() + 1,
                day: A.getDay(),
                date: A.getDate(),
                hour: A.getHours(),
                minute: A.getMinutes()
            };
            _.currentDate = {
                year: A.getFullYear(),
                month: A.getMonth() + 1,
                day: A.getDay(),
                date: A.getDate(),
                hour: A.getHours(),
                minute: A.getMinutes()
            };
            _.selectedDate = null;
            _.usedDate = null;
            $("td", _.body.thead).each(function(A, _) {
                $(_).html(B.dayMessage[A])
            });
            $("li", _.body.monthselector).each(function(A, _) {
                $(_).html(B.monthMessage[A])
            });
            _.buttons.btnToday.html(B.todayMessage);
            _.buttons.btnClose.html(B.closeMessage);
            if (B.showTime) {
                _.toolbar.time.show();
                _.toolbar.time.append(_.toolbar.time.hour).append(":").append(_.toolbar.time.minute);
                $("li", _.body.hourselector).each(function(B, A) {
                    var _ = B;
                    if (B < 10) _ = "0" + B.toString();
                    $(this).html(_)
                });
                $("li", _.body.minuteselector).each(function(B, A) {
                    var _ = B;
                    if (B < 10) _ = "0" + B.toString();
                    $(this).html(_)
                })
            }
            _.bulidContent();
            if (_.inputText.val() != "") _.onTextChange();
            _.dateeditor.hover(null, function($) {
                if (_.dateeditor.is(":visible") && !_.editorToggling) _.toggleDateEditor(true)
            });
            _.link.hover(function() {
                if (B.disabled) return;
                this.className = "l-trigger-hover"
            }, function() {
                if (B.disabled) return;
                this.className = "l-trigger"
            }).mousedown(function() {
                if (B.disabled) return;
                this.className = "l-trigger-pressed"
            }).mouseup(function() {
                if (B.disabled) return;
                this.className = "l-trigger-hover"
            }).click(function() {
                if (B.disabled) return;
                _.bulidContent();
                _.toggleDateEditor(_.dateeditor.is(":visible"))
            });
            if (B.disabled) {
                _.inputText.attr("readonly", "readonly");
                _.text.addClass("l-text-disabled")
            }
            if (B.initValue) _.inputText.val(B.initValue);
            _.buttons.btnClose.click(function() {
                _.toggleDateEditor(true)
            });
            $("td", _.body.tbody).hover(function() {
                if ($(this).hasClass("l-box-dateeditor-today")) return;
                $(this).addClass("l-box-dateeditor-over")
            }, function() {
                $(this).removeClass("l-box-dateeditor-over")
            }).click(function() {
                $(".l-box-dateeditor-selected", _.body.tbody).removeClass("l-box-dateeditor-selected");
                if (!$(this).hasClass("l-box-dateeditor-today")) $(this).addClass("l-box-dateeditor-selected");
                _.currentDate.date = parseInt($(this).html());
                _.currentDate.day = new Date(_.currentDate.year, _.currentDate.month - 1, 1).getDay();
                if ($(this).hasClass("l-box-dateeditor-out")) if ($("tr", _.body.tbody).index($(this).parent()) == 0) {
                    if (--_.currentDate.month == 0) {
                        _.currentDate.month = 12;
                        _.currentDate.year--
                    }
                } else if (++_.currentDate.month == 13) {
                    _.currentDate.month = 1;
                    _.currentDate.year++
                }
                _.selectedDate = {
                    year: _.currentDate.year,
                    month: _.currentDate.month,
                    date: _.currentDate.date
                };
                _.showDate();
                _.editorToggling = true;
                _.dateeditor.slideToggle("fast", function() {
                    _.editorToggling = false
                })
            });
            $(".l-box-dateeditor-header-btn", _.header).hover(function() {
                $(this).addClass("l-box-dateeditor-header-btn-over")
            }, function() {
                $(this).removeClass("l-box-dateeditor-header-btn-over")
            });
            _.buttons.btnYear.click(function() {
                if (!_.body.yearselector.is(":visible")) $("li", _.body.yearselector).each(function(C, B) {
                    var A = _.currentDate.year + (C - 4);
                    if (A == _.currentDate.year) $(this).addClass("l-selected");
                    else $(this).removeClass("l-selected");
                    $(this).html(A)
                });
                _.body.yearselector.slideToggle()
            });
            _.body.yearselector.hover(function() {}, function() {
                $(this).slideUp()
            });
            $("li", _.body.yearselector).click(function() {
                _.currentDate.year = parseInt($(this).html());
                _.body.yearselector.slideToggle();
                _.bulidContent()
            });
            _.buttons.btnMonth.click(function() {
                $("li", _.body.monthselector).each(function(B, A) {
                    if (_.currentDate.month == B + 1) $(this).addClass("l-selected");
                    else $(this).removeClass("l-selected")
                });
                _.body.monthselector.slideToggle()
            });
            _.body.monthselector.hover(function() {}, function() {
                $(this).slideUp("fast")
            });
            $("li", _.body.monthselector).click(function() {
                var A = $("li", _.body.monthselector).index(this);
                _.currentDate.month = A + 1;
                _.body.monthselector.slideToggle();
                _.bulidContent()
            });
            _.toolbar.time.hour.click(function() {
                $("li", _.body.hourselector).each(function(B, A) {
                    if (_.currentDate.hour == B) $(this).addClass("l-selected");
                    else $(this).removeClass("l-selected")
                });
                _.body.hourselector.slideToggle()
            });
            _.body.hourselector.hover(function() {}, function() {
                $(this).slideUp("fast")
            });
            $("li", _.body.hourselector).click(function() {
                var A = $("li", _.body.hourselector).index(this);
                _.currentDate.hour = A;
                _.body.hourselector.slideToggle();
                _.bulidContent()
            });
            _.toolbar.time.minute.click(function() {
                $("li", _.body.minuteselector).each(function(B, A) {
                    if (_.currentDate.minute == B) $(this).addClass("l-selected");
                    else $(this).removeClass("l-selected")
                });
                _.body.minuteselector.slideToggle("fast", function() {
                    var _ = $("li", this).index($("li.l-selected", this));
                    if (_ > 29) {
                        var A = ($("li.l-selected", this).offset().top - $(this).offset().top);
                        $(this).animate({
                            scrollTop: A
                        })
                    }
                })
            });
            _.body.minuteselector.hover(function() {}, function() {
                $(this).slideUp("fast")
            });
            $("li", _.body.minuteselector).click(function() {
                var A = $("li", _.body.minuteselector).index(this);
                _.currentDate.minute = A;
                _.body.minuteselector.slideToggle("fast");
                _.bulidContent()
            });
            _.buttons.btnPrevMonth.click(function() {
                if (--_.currentDate.month == 0) {
                    _.currentDate.month = 12;
                    _.currentDate.year--
                }
                _.bulidContent()
            });
            _.buttons.btnNextMonth.click(function() {
                if (++_.currentDate.month == 13) {
                    _.currentDate.month = 1;
                    _.currentDate.year++
                }
                _.bulidContent()
            });
            _.buttons.btnPrevYear.click(function() {
                _.currentDate.year--;
                _.bulidContent()
            });
            _.buttons.btnNextYear.click(function() {
                _.currentDate.year++;
                _.bulidContent()
            });
            _.buttons.btnToday.click(function() {
                _.currentDate = {
                    year: _.now.year,
                    month: _.now.month,
                    day: _.now.day,
                    date: _.now.date
                };
                _.selectedDate = {
                    year: _.now.year,
                    month: _.now.month,
                    day: _.now.day,
                    date: _.now.date
                };
                _.showDate();
                _.dateeditor.slideToggle("fast")
            });
            _.inputText.change(function() {
                _.onTextChange()
            }).blur(function() {
                _.text.removeClass("l-text-focus")
            }).focus(function() {
                _.text.addClass("l-text-focus")
            });
            _.text.hover(function() {
                _.text.addClass("l-text-over")
            }, function() {
                _.text.removeClass("l-text-over")
            });
            if (B.label) {
                _.labelwrapper = _.textwrapper.wrap("<div class=\"l-labeltext\"></div>").parent();
                _.labelwrapper.prepend("<div class=\"l-text-label\" style=\"float:left;display:inline;\">" + B.label + ":&nbsp</div>");
                _.textwrapper.css("float", "left");
                if (!B.labelWidth) B.labelWidth = $(".l-text-label", _.labelwrapper).outerWidth();
                else $(".l-text-label", _.labelwrapper).outerWidth(B.labelWidth);
                $(".l-text-label", _.labelwrapper).width(B.labelWidth);
                $(".l-text-label", _.labelwrapper).height(_.text.height());
                _.labelwrapper.append("<br style=\"clear:both;\" />");
                if (B.labelAlign) $(".l-text-label", _.labelwrapper).css("text-align", B.labelAlign);
                _.textwrapper.css({
                    display: "inline"
                });
                _.labelwrapper.width(_.text.outerWidth() + B.labelWidth + 2)
            }
            _.set(B)
        },
        destroy: function() {
            if (this.textwrapper) this.textwrapper.remove();
            if (this.dateeditor) this.dateeditor.remove();
            this.options = null;
            $.ligerui.remove(this)
        },
        bulidContent: function() {
            var _ = this,
                C = this.options,
                E = new Date(_.currentDate.year, _.currentDate.month - 1, 1).getDay(),
                B = _.currentDate.month,
                F = _.currentDate.year;
            if (++B == 13) {
                B = 1;
                F++
            }
            var D = new Date(F, B - 1, 0).getDate(),
                A = new Date(_.currentDate.year, _.currentDate.month - 1, 0).getDate();
            _.buttons.btnMonth.html(C.monthMessage[_.currentDate.month - 1]);
            _.buttons.btnYear.html(_.currentDate.year);
            _.toolbar.time.hour.html(_.currentDate.hour);
            _.toolbar.time.minute.html(_.currentDate.minute);
            if (_.toolbar.time.hour.html().length == 1) _.toolbar.time.hour.html("0" + _.toolbar.time.hour.html());
            if (_.toolbar.time.minute.html().length == 1) _.toolbar.time.minute.html("0" + _.toolbar.time.minute.html());
            $("td", this.body.tbody).each(function() {
                this.className = ""
            });
            $("tr", this.body.tbody).each(function(C, B) {
                $("td", B).each(function(H, G) {
                    var B = C * 7 + (H - E),
                        F = B + 1;
                    if (_.selectedDate && _.currentDate.year == _.selectedDate.year && _.currentDate.month == _.selectedDate.month && B + 1 == _.selectedDate.date) {
                        if (H == 0 || H == 6) $(G).addClass("l-box-dateeditor-holiday");
                        $(G).addClass("l-box-dateeditor-selected");
                        $(G).siblings().removeClass("l-box-dateeditor-selected")
                    } else if (_.currentDate.year == _.now.year && _.currentDate.month == _.now.month && B + 1 == _.now.date) {
                        if (H == 0 || H == 6) $(G).addClass("l-box-dateeditor-holiday");
                        $(G).addClass("l-box-dateeditor-today")
                    } else if (B < 0) {
                        F = A + F;
                        $(G).addClass("l-box-dateeditor-out").removeClass("l-box-dateeditor-selected")
                    } else if (B > D - 1) {
                        F = F - D;
                        $(G).addClass("l-box-dateeditor-out").removeClass("l-box-dateeditor-selected")
                    } else if (H == 0 || H == 6) $(G).addClass("l-box-dateeditor-holiday").removeClass("l-box-dateeditor-selected");
                    else G.className = "";
                    $(G).html(F)
                })
            })
        },
        updateSelectBoxPosition: function() {
            var _ = this,
                A = this.options;
            if (A.absolute) _.dateeditor.css({
                left: _.text.offset().left,
                top: _.text.offset().top + 1 + _.text.outerHeight()
            });
            else if (_.text.offset().top + 4 > _.dateeditor.height() && _.text.offset().top + _.dateeditor.height() + textHeight + 4 - $(window).scrollTop() > $(window).height()) {
                _.dateeditor.css("marginTop", -1 * (_.dateeditor.height() + textHeight + 5));
                _.showOnTop = true
            } else _.showOnTop = false
        },
        toggleDateEditor: function(B) {
            var _ = this,
                A = this.options,
                $ = _.text.height();
            _.editorToggling = true;
            if (B) _.dateeditor.hide("fast", function() {
                _.editorToggling = false
            });
            else {
                _.updateSelectBoxPosition();
                _.dateeditor.slideDown("fast", function() {
                    _.editorToggling = false
                })
            }
        },
        showDate: function() {
            var $ = this,
                _ = this.options;
            if (!this.selectedDate) return;
            var A = $.selectedDate.year + "/" + $.selectedDate.month + "/" + $.selectedDate.date;
            this.currentDate.hour = parseInt($.toolbar.time.hour.html(), 10);
            this.currentDate.minute = parseInt($.toolbar.time.minute.html(), 10);
            if (_.showTime) A += " " + this.currentDate.hour + ":" + this.currentDate.minute;
            this.inputText.val(A);
            this.inputText.trigger("change").focus()
        },
        isDateTime: function(C) {
            var $ = this,
                B = this.options,
                A = C.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/);
            if (A == null) return false;
            var _ = new Date(A[1], A[3] - 1, A[4]);
            if (_ == "NaN") return false;
            return (_.getFullYear() == A[1] && (_.getMonth() + 1) == A[3] && _.getDate() == A[4])
        },
        isLongDateTime: function(D) {
            var $ = this,
                C = this.options,
                B = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2})$/,
                A = D.match(B);
            if (A == null) return false;
            var _ = new Date(A[1], A[3] - 1, A[4], A[5], A[6]);
            if (_ == "NaN") return false;
            return (_.getFullYear() == A[1] && (_.getMonth() + 1) == A[3] && _.getDate() == A[4] && _.getHours() == A[5] && _.getMinutes() == A[6])
        },
        getFormatDate: function(C) {
            var $ = this,
                _ = this.options;
            if (C == "NaN") return null;
            var B = _.format,
                A = {
                    "M+": C.getMonth() + 1,
                    "d+": C.getDate(),
                    "h+": C.getHours(),
                    "m+": C.getMinutes(),
                    "s+": C.getSeconds(),
                    "q+": Math.floor((C.getMonth() + 3) / 3),
                    "S": C.getMilliseconds()
                };
            if (/(y+)/.test(B)) B = B.replace(RegExp.$1, (C.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var D in A) if (new RegExp("(" + D + ")").test(B)) B = B.replace(RegExp.$1, RegExp.$1.length == 1 ? A[D] : ("00" + A[D]).substr(("" + A[D]).length));
            return B
        },
        onTextChange: function() {
            var B = this,
                C = this.options,
                A = B.inputText.val();
            if (A == "") {
                B.selectedDate = null;
                return true
            }
            if (!C.showTime && !B.isDateTime(A)) {
                if (!B.usedDate) B.inputText.val("");
                else B.inputText.val(B.getFormatDate(B.usedDate))
            } else if (C.showTime && !B.isLongDateTime(A)) {
                if (!B.usedDate) B.inputText.val("");
                else B.inputText.val(B.getFormatDate(B.usedDate))
            } else {
                A = A.replace(/-/g, "/");
                var _ = B.getFormatDate(new Date(A));
                if (_ == null) if (!B.usedDate) B.inputText.val("");
                else B.inputText.val(B.getFormatDate(B.usedDate));
                B.usedDate = new Date(A);
                B.selectedDate = {
                    year: B.usedDate.getFullYear(),
                    month: B.usedDate.getMonth() + 1,
                    day: B.usedDate.getDay(),
                    date: B.usedDate.getDate(),
                    hour: B.usedDate.getHours(),
                    minute: B.usedDate.getMinutes()
                };
                B.currentDate = {
                    year: B.usedDate.getFullYear(),
                    month: B.usedDate.getMonth() + 1,
                    day: B.usedDate.getDay(),
                    date: B.usedDate.getDate(),
                    hour: B.usedDate.getHours(),
                    minute: B.usedDate.getMinutes()
                };
                B.inputText.val(_);
                B.trigger("changeDate", [_]);
                if ($(B.dateeditor).is(":visible")) B.bulidContent()
            }
        },
        _setHeight: function(_) {
            var $ = this;
            if (_ > 4) {
                $.text.css({
                    height: _
                });
                $.inputText.css({
                    height: _
                });
                $.textwrapper.css({
                    height: _
                })
            }
        },
        _setWidth: function(_) {
            var $ = this;
            if (_ > 20) {
                $.text.css({
                    width: _
                });
                $.inputText.css({
                    width: _ - 20
                });
                $.textwrapper.css({
                    width: _
                })
            }
        },
        _setValue: function(_) {
            var $ = this;
            if (!_) $.inputText.val("");
            if (typeof _ == "string") $.inputText.val(_);
            else if (typeof _ == "object") if (_ instanceof Date) {
                $.inputText.val($.getFormatDate(_));
                $.onTextChange()
            }
        },
        _getValue: function() {
            return this.usedDate
        },
        setEnabled: function() {
            var $ = this,
                _ = this.options;
            this.inputText.removeAttr("readonly");
            this.text.removeClass("l-text-disabled");
            _.disabled = false
        },
        setDisabled: function() {
            var $ = this,
                _ = this.options;
            this.inputText.attr("readonly", "readonly");
            this.text.addClass("l-text-disabled");
            _.disabled = true
        }
    })
})(jQuery);
(function($) {
    var _ = $.ligerui;
    $(".l-dialog-btn").live("mouseover", function() {
        $(this).addClass("l-dialog-btn-over")
    }).live("mouseout", function() {
        $(this).removeClass("l-dialog-btn-over")
    });
    $(".l-dialog-tc .l-dialog-close").live("mouseover", function() {
        $(this).addClass("l-dialog-close-over")
    }).live("mouseout", function() {
        $(this).removeClass("l-dialog-close-over")
    });
    $.ligerDialog = function() {
        return _.run.call(null, "ligerDialog", arguments, {
            isStatic: true
        })
    };
    $.ligerui.DialogImagePath = "../../lib/ligerUI/skins/Aqua/images/win/";

    function A(A) {
        for (var B in A) $("<img />").attr("src", _.DialogImagePath + A[B])
    }
    $.ligerDefaults.Dialog = {
        cls: null,
        id: null,
        buttons: null,
        isDrag: true,
        width: 280,
        height: null,
        content: "",
        target: null,
        url: null,
        load: false,
        onLoaded: null,
        type: "none",
        left: null,
        top: null,
        modal: true,
        name: null,
        isResize: false,
        allowClose: true,
        opener: null,
        timeParmName: null,
        closeWhenEnter: null,
        isHidden: true,
        show: true,
        title: "\u63d0\u793a",
        showMax: false,
        showToggle: false,
        showMin: false,
        slide: $.browser.msie ? false : true,
        fixedType: null,
        showType: null
    };
    $.ligerDefaults.DialogString = {
        titleMessage: "\u63d0\u793a",
        ok: "\u786e\u5b9a",
        yes: "\u662f",
        no: "\u5426",
        cancel: "\u53d6\u6d88",
        waittingMessage: "\u6b63\u5728\u7b49\u5f85\u4e2d,\u8bf7\u7a0d\u5019..."
    };
    $.ligerMethos.Dialog = $.ligerMethos.Dialog || {};
    _.controls.Dialog = function($) {
        _.controls.Dialog.base.constructor.call(this, null, $)
    };
    _.controls.Dialog.ligerExtend(_.core.Win, {
        __getType: function() {
            return "Dialog"
        },
        __idPrev: function() {
            return "Dialog"
        },
        _extendMethods: function() {
            return $.ligerMethos.Dialog
        },
        _render: function() {
            var A = this,
                C = this.options;
            A.set(C, true);
            var F = $("<div class=\"l-dialog\"><table class=\"l-dialog-table\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tbody><tr><td class=\"l-dialog-tl\"></td><td class=\"l-dialog-tc\"><div class=\"l-dialog-tc-inner\"><div class=\"l-dialog-icon\"></div><div class=\"l-dialog-title\"></div><div class=\"l-dialog-winbtns\"><div class=\"l-dialog-winbtn l-dialog-close\"></div></div></div></td><td class=\"l-dialog-tr\"></td></tr><tr><td class=\"l-dialog-cl\"></td><td class=\"l-dialog-cc\"><div class=\"l-dialog-body\"><div class=\"l-dialog-image\"></div> <div class=\"l-dialog-content\"></div><div class=\"l-dialog-buttons\"><div class=\"l-dialog-buttons-inner\"></div></td><td class=\"l-dialog-cr\"></td></tr><tr><td class=\"l-dialog-bl\"></td><td class=\"l-dialog-bc\"></td><td class=\"l-dialog-br\"></td></tr></tbody></table></div>");
            $("body").append(F);
            A.dialog = F;
            A.element = F[0];
            A.dialog.body = $(".l-dialog-body:first", A.dialog);
            A.dialog.header = $(".l-dialog-tc-inner:first", A.dialog);
            A.dialog.winbtns = $(".l-dialog-winbtns:first", A.dialog.header);
            A.dialog.buttons = $(".l-dialog-buttons:first", A.dialog);
            A.dialog.content = $(".l-dialog-content:first", A.dialog);
            A.set(C, false);
            if (C.allowClose == false) $(".l-dialog-close", A.dialog).remove();
            if (C.target || C.url || C.type == "none") {
                C.type = null;
                A.dialog.addClass("l-dialog-win")
            }
            if (C.cls) A.dialog.addClass(C.cls);
            if (C.id) A.dialog.attr("id", C.id);
            A.mask();
            if (C.isDrag) A._applyDrag();
            if (C.isResize) A._applyResize();
            if (C.type) A._setImage();
            else {
                $(".l-dialog-image", A.dialog).remove();
                A.dialog.content.addClass("l-dialog-content-noimage")
            }
            if (!C.show) {
                A.unmask();
                A.dialog.hide()
            }
            if (C.target) {
                A.dialog.content.prepend(C.target);
                $(C.target).show()
            } else if (C.url) {
                if (C.timeParmName) {
                    C.url += C.url.indexOf("?") == -1 ? "?" : "&";
                    C.url += C.timeParmName + "=" + new Date().getTime()
                }
                if (C.load) A.dialog.body.load(C.url, function() {
                    A._saveStatus();
                    A.trigger("loaded")
                });
                else {
                    A.jiframe = $("<iframe frameborder='0'></iframe>");
                    var E = C.name ? C.name : "ligerwindow" + new Date().getTime();
                    A.jiframe.attr("name", E);
                    A.jiframe.attr("id", E);
                    A.dialog.content.prepend(A.jiframe);
                    A.dialog.content.addClass("l-dialog-content-nopadding");
                    setTimeout(function() {
                        A.jiframe.attr("src", C.url);
                        A.frame = window.frames[A.jiframe.attr("name")]
                    }, 0)
                }
            }
            if (C.opener) A.dialog.opener = C.opener;
            if (C.buttons) $(C.buttons).each(function(C, B) {
                var _ = $("<div class=\"l-dialog-btn\"><div class=\"l-dialog-btn-l\"></div><div class=\"l-dialog-btn-r\"></div><div class=\"l-dialog-btn-inner\"></div></div>");
                $(".l-dialog-btn-inner", _).html(B.text);
                $(".l-dialog-buttons-inner", A.dialog.buttons).prepend(_);
                B.width && _.width(B.width);
                B.onclick && _.click(function() {
                    B.onclick(B, A, C)
                })
            });
            else A.dialog.buttons.remove();
            $(".l-dialog-buttons-inner", A.dialog.buttons).append("<div class='l-clear'></div>");
            $(".l-dialog-title", A.dialog).bind("selectstart", function() {
                return false
            });
            A.dialog.click(function() {
                _.win.setFront(A)
            });
            $(".l-dialog-tc .l-dialog-close", A.dialog).click(function() {
                if (C.isHidden) A.hide();
                else A.close()
            });
            if (!C.fixedType) {
                var D = 0,
                    G = 0,
                    B = C.width || A.dialog.width();
                if (C.slide == true) C.slide = "fast";
                if (C.left != null) D = C.left;
                else C.left = D = 0.5 * ($(window).width() - B);
                if (C.top != null) G = C.top;
                else C.top = G = 0.5 * ($(window).height() - A.dialog.height()) + $(window).scrollTop() - 10;
                if (D < 0) C.left = D = 0;
                if (G < 0) C.top = G = 0;
                A.dialog.css({
                    left: D,
                    top: G
                })
            }
            A.show();
            $("body").bind("keydown.dialog", function($) {
                var _ = $.which;
                if (_ == 13) A.enter();
                else if (_ == 27) A.esc()
            });
            A._updateBtnsWidth();
            A._saveStatus();
            A._onReisze()
        },
        _borderX: 12,
        _borderY: 32,
        doMax: function(F) {
            var A = this,
                D = this.options,
                C = $(window).width(),
                B = $(window).height(),
                E = 0,
                G = 0;
            if (_.win.taskbar) {
                B -= _.win.taskbar.outerHeight();
                if (_.win.top) G += _.win.taskbar.outerHeight()
            }
            if (F) {
                A.dialog.body.animate({
                    width: C - A._borderX
                }, D.slide);
                A.dialog.animate({
                    left: E,
                    top: G
                }, D.slide);
                A.dialog.content.animate({
                    height: B - A._borderY - A.dialog.buttons.outerHeight()
                }, D.slide, function() {
                    A._onReisze()
                })
            } else {
                A.set({
                    width: C,
                    height: B,
                    left: E,
                    top: G
                });
                A._onReisze()
            }
        },
        max: function() {
            var _ = this,
                A = this.options;
            if (_.winmax) {
                _.winmax.addClass("l-dialog-recover");
                _.doMax(A.slide);
                if (_.wintoggle) if (_.wintoggle.hasClass("l-dialog-extend")) _.wintoggle.addClass("l-dialog-toggle-disabled l-dialog-extend-disabled");
                else _.wintoggle.addClass("l-dialog-toggle-disabled l-dialog-collapse-disabled");
                if (_.resizable) _.resizable.set({
                    disabled: true
                });
                if (_.draggable) _.draggable.set({
                    disabled: true
                });
                _.maximum = true;
                $(window).bind("resize.dialogmax", function() {
                    _.doMax(false)
                })
            }
        },
        recover: function() {
            var _ = this,
                A = this.options;
            if (_.winmax) {
                _.winmax.removeClass("l-dialog-recover");
                if (A.slide) {
                    _.dialog.body.animate({
                        width: _._width - _._borderX
                    }, A.slide);
                    _.dialog.animate({
                        left: _._left,
                        top: _._top
                    }, A.slide);
                    _.dialog.content.animate({
                        height: _._height - _._borderY - _.dialog.buttons.outerHeight()
                    }, A.slide, function() {
                        _._onReisze()
                    })
                } else {
                    _.set({
                        width: _._width,
                        height: _._height,
                        left: _._left,
                        top: _._top
                    });
                    _._onReisze()
                }
                if (_.wintoggle) _.wintoggle.removeClass("l-dialog-toggle-disabled l-dialog-extend-disabled l-dialog-collapse-disabled");
                $(window).unbind("resize.dialogmax")
            }
            if (this.resizable) this.resizable.set({
                disabled: false
            });
            if (_.draggable) _.draggable.set({
                disabled: false
            });
            _.maximum = false
        },
        min: function() {
            var $ = this,
                B = this.options,
                A = _.win.getTask(this);
            if (B.slide) {
                $.dialog.body.animate({
                    width: 1
                }, B.slide);
                A.y = A.offset().top + A.height();
                A.x = A.offset().left + A.width() / 2;
                $.dialog.animate({
                    left: A.x,
                    top: A.y
                }, B.slide, function() {
                    $.dialog.hide()
                })
            } else $.dialog.hide();
            $.unmask();
            $.minimize = true;
            $.actived = false
        },
        active: function() {
            var A = this,
                D = this.options;
            if (A.minimize) {
                var C = A._width,
                    B = A._height,
                    E = A._left,
                    F = A._top;
                if (A.maximum) {
                    C = $(window).width();
                    B = $(window).height();
                    E = F = 0;
                    if (_.win.taskbar) {
                        B -= _.win.taskbar.outerHeight();
                        if (_.win.top) F += _.win.taskbar.outerHeight()
                    }
                }
                if (D.slide) {
                    A.dialog.body.animate({
                        width: C - A._borderX
                    }, D.slide);
                    A.dialog.animate({
                        left: E,
                        top: F
                    }, D.slide)
                } else A.set({
                    width: C,
                    height: B,
                    left: E,
                    top: F
                })
            }
            A.actived = true;
            A.minimize = false;
            _.win.setFront(A);
            A.show()
        },
        toggle: function() {
            var $ = this,
                _ = this.options;
            if (!$.wintoggle) return;
            if ($.wintoggle.hasClass("l-dialog-extend")) $.extend();
            else $.collapse()
        },
        collapse: function() {
            var $ = this,
                _ = this.options;
            if (!$.wintoggle) return;
            if (_.slide) $.dialog.content.animate({
                height: 1
            }, _.slide);
            else $.dialog.content.height(1);
            if (this.resizable) this.resizable.set({
                disabled: true
            })
        },
        extend: function() {
            var $ = this,
                A = this.options;
            if (!$.wintoggle) return;
            var _ = $._height - $._borderY - $.dialog.buttons.outerHeight();
            if (A.slide) $.dialog.content.animate({
                height: _
            }, A.slide);
            else $.dialog.content.height(_);
            if (this.resizable) this.resizable.set({
                disabled: false
            })
        },
        _updateBtnsWidth: function() {
            var _ = this,
                A = $(">div", _.dialog.winbtns).length;
            _.dialog.winbtns.width(22 * A)
        },
        _setLeft: function($) {
            if (!this.dialog) return;
            if ($ != null) this.dialog.css({
                left: $
            })
        },
        _setTop: function($) {
            if (!this.dialog) return;
            if ($ != null) this.dialog.css({
                top: $
            })
        },
        _setWidth: function($) {
            if (!this.dialog) return;
            if ($ >= this._borderX) this.dialog.body.width($ - this._borderX)
        },
        _setHeight: function(B) {
            var $ = this,
                A = this.options;
            if (!this.dialog) return;
            if (B >= this._borderY) {
                var _ = B - this._borderY - $.dialog.buttons.outerHeight();
                $.dialog.content.height(_)
            }
        },
        _setShowMax: function(B) {
            var _ = this,
                A = this.options;
            if (B) {
                if (!_.winmax) _.winmax = $("<div class=\"l-dialog-winbtn l-dialog-max\"></div>").appendTo(_.dialog.winbtns).hover(function() {
                    if ($(this).hasClass("l-dialog-recover")) $(this).addClass("l-dialog-recover-over");
                    else $(this).addClass("l-dialog-max-over")
                }, function() {
                    $(this).removeClass("l-dialog-max-over l-dialog-recover-over")
                }).click(function() {
                    if ($(this).hasClass("l-dialog-recover")) _.recover();
                    else _.max()
                })
            } else if (_.winmax) {
                _.winmax.remove();
                _.winmax = null
            }
            _._updateBtnsWidth()
        },
        _setShowMin: function(C) {
            var A = this,
                B = this.options;
            if (C) {
                if (!A.winmin) {
                    A.winmin = $("<div class=\"l-dialog-winbtn l-dialog-min\"></div>").appendTo(A.dialog.winbtns).hover(function() {
                        $(this).addClass("l-dialog-min-over")
                    }, function() {
                        $(this).removeClass("l-dialog-min-over")
                    }).click(function() {
                        A.min()
                    });
                    _.win.addTask(A)
                }
            } else if (A.winmin) {
                A.winmin.remove();
                A.winmin = null
            }
            A._updateBtnsWidth()
        },
        _setShowToggle: function(B) {
            var _ = this,
                A = this.options;
            if (B) {
                if (!_.wintoggle) _.wintoggle = $("<div class=\"l-dialog-winbtn l-dialog-collapse\"></div>").appendTo(_.dialog.winbtns).hover(function() {
                    if ($(this).hasClass("l-dialog-toggle-disabled")) return;
                    if ($(this).hasClass("l-dialog-extend")) $(this).addClass("l-dialog-extend-over");
                    else $(this).addClass("l-dialog-collapse-over")
                }, function() {
                    $(this).removeClass("l-dialog-extend-over l-dialog-collapse-over")
                }).click(function() {
                    if ($(this).hasClass("l-dialog-toggle-disabled")) return;
                    if (_.wintoggle.hasClass("l-dialog-extend")) {
                        if (_.trigger("extend") == false) return;
                        _.wintoggle.removeClass("l-dialog-extend");
                        _.extend();
                        _.trigger("extended")
                    } else {
                        if (_.trigger("collapse") == false) return;
                        _.wintoggle.addClass("l-dialog-extend");
                        _.collapse();
                        _.trigger("collapseed")
                    }
                })
            } else if (_.wintoggle) {
                _.wintoggle.remove();
                _.wintoggle = null
            }
        },
        enter: function() {
            var $ = this,
                _ = this.options,
                A;
            if (_.closeWhenEnter != undefined) A = _.closeWhenEnter;
            else if (_.type == "warn" || _.type == "error" || _.type == "success" || _.type == "question") A = true;
            if (A) $.close()
        },
        esc: function() {},
        _removeDialog: function() {
            var $ = this,
                _ = this.options;
            if (_.showType && _.fixedType) $.dialog.animate({
                bottom: -1 * _.height
            }, function() {
                $.dialog.remove()
            });
            else $.dialog.remove()
        },
        close: function() {
            var A = this,
                B = this.options;
            _.win.removeTask(this);
            A.unmask();
            A._removeDialog();
            $("body").unbind("keydown.dialog")
        },
        _getVisible: function() {
            return this.dialog.is(":visible")
        },
        _setUrl: function(A) {
            var $ = this,
                _ = this.options;
            _.url = A;
            if (_.load) $.dialog.body.html("").load(_.url, function() {
                $.trigger("loaded")
            });
            else if ($.jiframe) $.jiframe.attr("src", _.url)
        },
        _setContent: function($) {
            this.dialog.content.html($)
        },
        _setTitle: function(B) {
            var _ = this,
                A = this.options;
            if (B) $(".l-dialog-title", _.dialog).html(B)
        },
        _hideDialog: function() {
            var $ = this,
                _ = this.options;
            if (_.showType && _.fixedType) $.dialog.animate({
                bottom: -1 * _.height
            }, function() {
                $.dialog.hide()
            });
            else $.dialog.hide()
        },
        hidden: function() {
            var $ = this;
            _.win.removeTask($);
            $.dialog.hide();
            $.unmask()
        },
        show: function() {
            var _ = this,
                A = this.options;
            _.mask();
            if (A.fixedType) {
                if (A.showType) {
                    _.dialog.css({
                        bottom: -1 * A.height
                    }).addClass("l-dialog-fixed");
                    _.dialog.show().animate({
                        bottom: 0
                    })
                } else _.dialog.show().css({
                    bottom: 0
                })
            } else _.dialog.show();
            $.ligerui.win.setFront.ligerDefer($.ligerui.win, 100, [_])
        },
        setUrl: function($) {
            this._setUrl($)
        },
        _saveStatus: function() {
            var $ = this;
            $._width = $.dialog.body.width();
            $._height = $.dialog.body.height();
            var A = 0,
                _ = 0;
            if (!isNaN(parseInt($.dialog.css("top")))) A = parseInt($.dialog.css("top"));
            if (!isNaN(parseInt($.dialog.css("left")))) _ = parseInt($.dialog.css("left"));
            $._top = A;
            $._left = _
        },
        _applyDrag: function() {
            var A = this,
                B = this.options;
            if ($.fn.ligerDrag) A.draggable = A.dialog.ligerDrag({
                handler: ".l-dialog-title",
                animate: false,
                onStartDrag: function() {
                    _.win.setFront(A)
                },
                onStopDrag: function() {
                    if (B.target) {
                        var C = _.find($.ligerui.controls.DateEditor),
                            D = _.find($.ligerui.controls.ComboBox);
                        $($.merge(C, D)).each(function() {
                            if (this.updateSelectBoxPosition) this.updateSelectBoxPosition()
                        })
                    }
                    A._saveStatus()
                }
            })
        },
        _onReisze: function() {
            var _ = this,
                C = this.options;
            if (C.target) {
                var B = $(C.target).liger();
                if (!B) B = $(C.target).find(":first").liger();
                if (!B) return;
                var A = _.dialog.content.height(),
                    D = _.dialog.content.width();
                B.trigger("resize", [{
                    width: D,
                    height: A
                }])
            }
        },
        _applyResize: function() {
            var _ = this,
                A = this.options;
            if ($.fn.ligerResizable) _.resizable = _.dialog.ligerResizable({
                onStopResize: function(A, $) {
                    var C = 0,
                        B = 0;
                    if (!isNaN(parseInt(_.dialog.css("top")))) C = parseInt(_.dialog.css("top"));
                    if (!isNaN(parseInt(_.dialog.css("left")))) B = parseInt(_.dialog.css("left"));
                    if (A.diffLeft) _.set({
                        left: B + A.diffLeft
                    });
                    if (A.diffTop) _.set({
                        top: C + A.diffTop
                    });
                    if (A.newWidth) {
                        _.set({
                            width: A.newWidth
                        });
                        _.dialog.body.css({
                            width: A.newWidth - _._borderX
                        })
                    }
                    if (A.newHeight) _.set({
                        height: A.newHeight
                    });
                    _._onReisze();
                    _._saveStatus();
                    return false
                },
                animate: false
            })
        },
        _setImage: function() {
            var _ = this,
                A = this.options;
            if (A.type) if (A.type == "success" || A.type == "donne" || A.type == "ok") {
                $(".l-dialog-image", _.dialog).addClass("l-dialog-image-donne").show();
                _.dialog.content.css({
                    paddingLeft: 64,
                    paddingBottom: 30
                })
            } else if (A.type == "error") {
                $(".l-dialog-image", _.dialog).addClass("l-dialog-image-error").show();
                _.dialog.content.css({
                    paddingLeft: 64,
                    paddingBottom: 30
                })
            } else if (A.type == "warn") {
                $(".l-dialog-image", _.dialog).addClass("l-dialog-image-warn").show();
                _.dialog.content.css({
                    paddingLeft: 64,
                    paddingBottom: 30
                })
            } else if (A.type == "question") {
                $(".l-dialog-image", _.dialog).addClass("l-dialog-image-question").show();
                _.dialog.content.css({
                    paddingLeft: 64,
                    paddingBottom: 40
                })
            }
        }
    });
    _.controls.Dialog.prototype.hide = _.controls.Dialog.prototype.hidden;
    $.ligerDialog.open = function(_) {
        return $.ligerDialog(_)
    };
    $.ligerDialog.close = function() {
        var A = _.find(_.controls.Dialog.prototype.__getType());
        for (var B in A) {
            var $ = A[B];
            $.destroy.ligerDefer($, 5)
        }
        _.win.unmask()
    };
    $.ligerDialog.show = function(A) {
        var B = _.find(_.controls.Dialog.prototype.__getType());
        if (B.length) for (var C in B) {
            B[C].show();
            return
        }
        return $.ligerDialog(A)
    };
    $.ligerDialog.hide = function() {
        var A = _.find(_.controls.Dialog.prototype.__getType());
        for (var B in A) {
            var $ = A[B];
            $.hide()
        }
    };
    $.ligerDialog.tip = function(_) {
        _ = $.extend({
            showType: "slide",
            width: 240,
            modal: false,
            height: 100
        }, _ || {});
        $.extend(_, {
            fixedType: "se",
            type: "none",
            isDrag: false,
            isResize: false,
            showMax: false,
            showToggle: false,
            showMin: false
        });
        return $.ligerDialog.open(_)
    };
    $.ligerDialog.alert = function(_, A, D, B) {
        _ = _ || "";
        if (typeof(A) == "function") {
            B = A;
            D = null
        } else if (typeof(D) == "function") B = D;
        var C = function(_, A, $) {
                A.close();
                if (B) B(_, A, $)
            };
        p = {
            content: _,
            buttons: [{
                text: $.ligerDefaults.DialogString.ok,
                onclick: C
            }]
        };
        if (typeof(A) == "string" && A != "") p.title = A;
        if (typeof(D) == "string" && D != "") p.type = D;
        $.extend(p, {
            showMax: false,
            showToggle: false,
            showMin: false
        });
        return $.ligerDialog(p)
    };
    $.ligerDialog.confirm = function(_, A, B) {
        if (typeof(A) == "function") {
            B = A;
            type = null
        }
        var C = function($, _) {
                _.close();
                if (B) B($.type == "ok")
            };
        p = {
            type: "question",
            content: _,
            buttons: [{
                text: $.ligerDefaults.DialogString.yes,
                onclick: C,
                type: "ok"
            }, {
                text: $.ligerDefaults.DialogString.no,
                onclick: C,
                type: "no"
            }]
        };
        if (typeof(A) == "string" && A != "") p.title = A;
        $.extend(p, {
            showMax: false,
            showToggle: false,
            showMin: false
        });
        return $.ligerDialog(p)
    };
    $.ligerDialog.warning = function(_, A, B) {
        if (typeof(A) == "function") {
            B = A;
            type = null
        }
        var C = function($, _) {
                _.close();
                if (B) B($.type)
            };
        p = {
            type: "question",
            content: _,
            buttons: [{
                text: $.ligerDefaults.DialogString.yes,
                onclick: C,
                type: "yes"
            }, {
                text: $.ligerDefaults.DialogString.no,
                onclick: C,
                type: "no"
            }, {
                text: $.ligerDefaults.DialogString.cancel,
                onclick: C,
                type: "cancel"
            }]
        };
        if (typeof(A) == "string" && A != "") p.title = A;
        $.extend(p, {
            showMax: false,
            showToggle: false,
            showMin: false
        });
        return $.ligerDialog(p)
    };
    $.ligerDialog.waitting = function(_) {
        _ = _ || $.ligerDefaults.Dialog.waittingMessage;
        return $.ligerDialog.open({
            cls: "l-dialog-waittingdialog",
            type: "none",
            content: "<div style=\"padding:4px\">" + _ + "</div>",
            allowClose: false
        })
    };
    $.ligerDialog.closeWaitting = function() {
        var A = _.find(_.controls.Dialog);
        for (var B in A) {
            var $ = A[B];
            if ($.dialog.hasClass("l-dialog-waittingdialog")) $.close()
        }
    };
    $.ligerDialog.success = function(_, A, B) {
        return $.ligerDialog.alert(_, A, "success", B)
    };
    $.ligerDialog.error = function(_, A, B) {
        return $.ligerDialog.alert(_, A, "error", B)
    };
    $.ligerDialog.warn = function(_, A, B) {
        return $.ligerDialog.alert(_, A, "warn", B)
    };
    $.ligerDialog.question = function(_, A) {
        return $.ligerDialog.alert(_, A, "question")
    };
    $.ligerDialog.prompt = function(_, D, C, B) {
        var A = $("<input type=\"text\" class=\"l-dialog-inputtext\"/>");
        if (typeof(C) == "function") B = C;
        if (typeof(D) == "function") B = D;
        else if (typeof(D) == "boolean") C = D;
        if (typeof(C) == "boolean" && C) A = $("<textarea class=\"l-dialog-textarea\"></textarea>");
        if (typeof(D) == "string" || typeof(D) == "int") A.val(D);
        var E = function(_, C, $) {
                C.close();
                if (B) B(_.type == "yes", A.val())
            };
        p = {
            title: _,
            target: A,
            width: 320,
            buttons: [{
                text: $.ligerDefaults.DialogString.ok,
                onclick: E,
                type: "yes"
            }, {
                text: $.ligerDefaults.DialogString.cancel,
                onclick: E,
                type: "cancel"
            }]
        };
        return $.ligerDialog(p)
    }
})(jQuery);
(function($) {
    var _ = $.ligerui;
    $.fn.ligerDrag = function($) {
        return _.run.call(this, "ligerDrag", arguments, {
            idAttrName: "ligeruidragid",
            hasElement: false,
            propertyToElemnt: "target"
        })
    };
    $.fn.ligerGetDragManager = function() {
        return _.run.call(this, "ligerGetDragManager", arguments, {
            idAttrName: "ligeruidragid",
            hasElement: false,
            propertyToElemnt: "target"
        })
    };
    $.ligerDefaults.Drag = {
        onStartDrag: false,
        onDrag: false,
        onStopDrag: false,
        handler: null,
        proxy: true,
        revert: false,
        animate: true,
        onRevert: null,
        onEndRevert: null,
        receive: null,
        onDragEnter: null,
        onDragOver: null,
        onDragLeave: null,
        onDrop: null,
        disabled: false,
        proxyX: null,
        proxyY: null
    };
    _.controls.Drag = function($) {
        _.controls.Drag.base.constructor.call(this, null, $)
    };
    _.controls.Drag.ligerExtend(_.core.UIComponent, {
        __getType: function() {
            return "Drag"
        },
        __idPrev: function() {
            return "Drag"
        },
        _render: function() {
            var $ = this,
                _ = this.options;
            this.set(_);
            $.cursor = "move";
            $.handler.css("cursor", $.cursor);
            $.handler.bind("mousedown.drag", function(A) {
                if (_.disabled) return;
                if (A.button == 2) return;
                $._start.call($, A)
            }).bind("mousemove.drag", function() {
                if (_.disabled) return;
                $.handler.css("cursor", $.cursor)
            })
        },
        _rendered: function() {
            this.options.target.ligeruidragid = this.id
        },
        _start: function(B) {
            var A = this,
                C = this.options;
            if (A.reverting) return;
            if (C.disabled) return;
            A.current = {
                target: A.target,
                left: A.target.offset().left,
                top: A.target.offset().top,
                startX: B.pageX || B.screenX,
                startY: B.pageY || B.clientY
            };
            if (A.trigger("startDrag", [A.current, B]) == false) return false;
            A.cursor = "move";
            A._createProxy(C.proxy, B);
            if (C.proxy && !A.proxy) return false;
            (A.proxy || A.handler).css("cursor", A.cursor);
            $(document).bind("selectstart.drag", function() {
                return false
            });
            $(document).bind("mousemove.drag", function() {
                A._drag.apply(A, arguments)
            });
            _.draggable.dragging = true;
            $(document).bind("mouseup.drag", function() {
                _.draggable.dragging = false;
                A._stop.apply(A, arguments)
            })
        },
        _drag: function(A) {
            var _ = this,
                B = this.options;
            if (!_.current) return;
            var C = A.pageX || A.screenX,
                D = A.pageY || A.screenY;
            _.current.diffX = C - _.current.startX;
            _.current.diffY = D - _.current.startY;
            (_.proxy || _.handler).css("cursor", _.cursor);
            if (_.receive) _.receive.each(function(G, B) {
                var E = $(B),
                    F = E.offset();
                if (C > F.left && C < F.left + E.width() && D > F.top && D < F.top + E.height()) {
                    if (!_.receiveEntered[G]) {
                        _.receiveEntered[G] = true;
                        _.trigger("dragEnter", [B, _.proxy || _.target, A])
                    } else _.trigger("dragOver", [B, _.proxy || _.target, A])
                } else if (_.receiveEntered[G]) {
                    _.receiveEntered[G] = false;
                    _.trigger("dragLeave", [B, _.proxy || _.target, A])
                }
            });
            if (_.hasBind("drag")) {
                if (_.trigger("drag", [_.current, A]) != false) _._applyDrag();
                else _._removeProxy()
            } else _._applyDrag()
        },
        _stop: function(A) {
            var _ = this,
                B = this.options;
            $(document).unbind("mousemove.drag");
            $(document).unbind("mouseup.drag");
            $(document).unbind("selectstart.drag");
            if (_.receive) _.receive.each(function(B, $) {
                if (_.receiveEntered[B]) _.trigger("drop", [$, _.proxy || _.target, A])
            });
            if (_.proxy) if (B.revert) {
                if (_.hasBind("revert")) {
                    if (_.trigger("revert", [_.current, A]) != false) _._revert(A);
                    else _._removeProxy()
                } else _._revert(A)
            } else {
                _._applyDrag(_.target);
                _._removeProxy()
            }
            _.cursor = "move";
            _.trigger("stopDrag", [_.current, A]);
            _.current = null;
            _.handler.css("cursor", _.cursor)
        },
        _revert: function(_) {
            var $ = this;
            $.reverting = true;
            $.proxy.animate({
                left: $.current.left,
                top: $.current.top
            }, function() {
                $.reverting = false;
                $._removeProxy();
                $.trigger("endRevert", [$.current, _]);
                $.current = null
            })
        },
        _applyDrag: function(C) {
            var $ = this,
                _ = this.options;
            C = C || $.proxy || $.target;
            var A = {},
                D = false,
                B = C == $.target;
            if ($.current.diffX) {
                if (B || _.proxyX == null) A.left = $.current.left + $.current.diffX;
                else A.left = $.current.startX + _.proxyX + $.current.diffX;
                D = true
            }
            if ($.current.diffY) {
                if (B || _.proxyY == null) A.top = $.current.top + $.current.diffY;
                else A.top = $.current.startY + _.proxyY + $.current.diffY;
                D = true
            }
            if (C == $.target && $.proxy && _.animate) {
                $.reverting = true;
                C.animate(A, function() {
                    $.reverting = false
                })
            } else C.css(A)
        },
        _setReceive: function(_) {
            this.receiveEntered = {};
            if (!_) return;
            if (typeof _ == "string") this.receive = $(_);
            else this.receive = _
        },
        _setHandler: function(B) {
            var _ = this,
                A = this.options;
            if (!B) _.handler = $(A.target);
            else _.handler = (typeof B == "string" ? $(B, A.target) : B)
        },
        _setTarget: function(_) {
            this.target = $(_)
        },
        _setCursor: function($) {
            this.cursor = $;
            (this.proxy || this.handler).css("cursor", $)
        },
        _createProxy: function(C, A) {
            if (!C) return;
            var _ = this,
                B = this.options;
            if (typeof C == "function") _.proxy = C.call(this.options.target, _, A);
            else if (C == "clone") {
                _.proxy = _.target.clone().css("position", "absolute");
                _.proxy.appendTo("body")
            } else {
                _.proxy = $("<div class='l-draggable'></div>");
                _.proxy.width(_.target.width()).height(_.target.height());
                _.proxy.attr("dragid", _.id).appendTo("body")
            }
            _.proxy.css({
                left: B.proxyX == null ? _.current.left : _.current.startX + B.proxyX,
                top: B.proxyY == null ? _.current.top : _.current.startY + B.proxyY
            }).show()
        },
        _removeProxy: function() {
            var $ = this;
            if ($.proxy) {
                $.proxy.remove();
                $.proxy = null
            }
        }
    })
})(jQuery);
(function($) {
    $.fn.ligerEasyTab = function() {
        return $.ligerui.run.call(this, "ligerEasyTab", arguments)
    };
    $.fn.ligerGetEasyTabManager = function() {
        return $.ligerui.run.call(this, "ligerGetEasyTabManager", arguments)
    };
    $.ligerDefaults.EasyTab = {};
    $.ligerMethos.EasyTab = {};
    $.ligerui.controls.EasyTab = function(_, A) {
        $.ligerui.controls.EasyTab.base.constructor.call(this, _, A)
    };
    $.ligerui.controls.EasyTab.ligerExtend($.ligerui.core.UIComponent, {
        __getType: function() {
            return "EasyTab"
        },
        __idPrev: function() {
            return "EasyTab"
        },
        _extendMethods: function() {
            return $.ligerMethos.EasyTab
        },
        _render: function() {
            var _ = this,
                B = this.options;
            _.tabs = $(this.element);
            _.tabs.addClass("l-easytab");
            var A = 0;
            if ($("> div[lselected=true]", _.tabs).length > 0) A = $("> div", _.tabs).index($("> div[lselected=true]", _.tabs));
            _.tabs.ul = $("<ul class=\"l-easytab-header\"></ul>");
            $("> div", _.tabs).each(function(D, C) {
                var B = $("<li><span></span></li>");
                if (D == A) $("span", B).addClass("l-selected");
                if ($(C).attr("title")) $("span", B).html($(C).attr("title"));
                _.tabs.ul.append(B);
                if (!$(C).hasClass("l-easytab-panelbox")) $(C).addClass("l-easytab-panelbox")
            });
            _.tabs.ul.prependTo(_.tabs);
            $(".l-easytab-panelbox:eq(" + A + ")", _.tabs).show().siblings(".l-easytab-panelbox").hide();
            $("> ul:first span", _.tabs).click(function() {
                if ($(this).hasClass("l-selected")) return;
                var A = $("> ul:first span", _.tabs).index(this);
                $(this).addClass("l-selected").parent().siblings().find("span.l-selected").removeClass("l-selected");
                $(".l-easytab-panelbox:eq(" + A + ")", _.tabs).show().siblings(".l-easytab-panelbox").hide()
            }).not("l-selected").hover(function() {
                $(this).addClass("l-over")
            }, function() {
                $(this).removeClass("l-over")
            });
            _.set(B)
        }
    })
})(jQuery);
(function($) {
    $.fn.ligerFilter = function() {
        return $.ligerui.run.call(this, "ligerFilter", arguments)
    };
    $.fn.ligerGetFilterManager = function() {
        return $.ligerui.run.call(this, "ligerGetFilterManager", arguments)
    };
    $.ligerDefaults.Filter = {
        fields: [],
        operators: {},
        editors: {}
    };
    $.ligerDefaults.FilterString = {
        strings: {
            "and": "\u5e76\u4e14",
            "or": "\u6216\u8005",
            "equal": "\u76f8\u7b49",
            "notequal": "\u4e0d\u76f8\u7b49",
            "startwith": "\u4ee5..\u5f00\u59cb",
            "endwith": "\u4ee5..\u7ed3\u675f",
            "like": "\u76f8\u4f3c",
            "greater": "\u5927\u4e8e",
            "greaterorequal": "\u5927\u4e8e\u6216\u7b49\u4e8e",
            "less": "\u5c0f\u4e8e",
            "lessorequal": "\u5c0f\u4e8e\u6216\u7b49\u4e8e",
            "in": "\u5305\u62ec\u5728...",
            "notin": "\u4e0d\u5305\u62ec...",
            "addgroup": "\u589e\u52a0\u5206\u7ec4",
            "addrule": "\u589e\u52a0\u6761\u4ef6",
            "deletegroup": "\u5220\u9664\u5206\u7ec4"
        }
    };
    $.ligerDefaults.Filter.operators["string"] = $.ligerDefaults.Filter.operators["text"] = ["equal", "notequal", "startwith", "endwith", "like", "greater", "greaterorequal", "less", "lessorequal", "in", "notin"];
    $.ligerDefaults.Filter.operators["number"] = $.ligerDefaults.Filter.operators["int"] = $.ligerDefaults.Filter.operators["float"] = $.ligerDefaults.Filter.operators["date"] = ["equal", "notequal", "greater", "greaterorequal", "less", "lessorequal", "in", "notin"];
    $.ligerDefaults.Filter.editors["string"] = {
        create: function(B, _) {
            var A = $("<input type='text'/>");
            B.append(A);
            A.ligerTextBox(_.editor.options || {});
            return A
        },
        setValue: function($, _) {
            $.val(_)
        },
        getValue: function($) {
            return $.liger("option", "value")
        },
        destroy: function($) {
            $.liger("destroy")
        }
    };
    $.ligerDefaults.Filter.editors["date"] = {
        create: function(B, _) {
            var A = $("<input type='text'/>");
            B.append(A);
            A.ligerDateEditor(_.editor.options || {});
            return A
        },
        setValue: function($, _) {
            $.liger("option", "value", _)
        },
        getValue: function(_, $) {
            return _.liger("option", "value")
        },
        destroy: function($) {
            $.liger("destroy")
        }
    };
    $.ligerDefaults.Filter.editors["number"] = {
        create: function(B, _) {
            var A = $("<input type='text'/>");
            B.append(A);
            var C = {
                minValue: _.editor.minValue,
                maxValue: _.editor.maxValue
            };
            A.ligerSpinner($.extend(C, _.editor.options || {}));
            return A
        },
        setValue: function($, _) {
            $.val(_)
        },
        getValue: function(_, $) {
            var A = $.editor.type == "int";
            if (A) return parseInt(_.val(), 10);
            else return parseFloat(_.val())
        },
        destroy: function($) {
            $.liger("destroy")
        }
    };
    $.ligerDefaults.Filter.editors["combobox"] = {
        create: function(B, _) {
            var A = $("<input type='text'/>");
            B.append(A);
            var C = {
                data: _.data,
                slide: false,
                valueField: _.editor.valueField || _.editor.valueColumnName,
                textField: _.editor.textField || _.editor.displayColumnName
            };
            $.extend(C, _.editor.options || {});
            A.ligerComboBox(C);
            return A
        },
        setValue: function($, _) {
            $.liger("option", "value", _)
        },
        getValue: function($) {
            return $.liger("option", "value")
        },
        destroy: function($) {
            $.liger("destroy")
        }
    };
    $.ligerui.controls.Filter = function(_, A) {
        $.ligerui.controls.Filter.base.constructor.call(this, _, A)
    };
    $.ligerui.controls.Filter.ligerExtend($.ligerui.core.UIComponent, {
        __getType: function() {
            return "Filter"
        },
        __idPrev: function() {
            return "Filter"
        },
        _init: function() {
            $.ligerui.controls.Filter.base._init.call(this)
        },
        _render: function() {
            var _ = this,
                A = this.options;
            _.set(A);
            $("#" + _.id + " .addgroup").live("click", function() {
                var A = $(this).parent().parent().parent().parent();
                _.addGroup(A)
            });
            $("#" + _.id + " .deletegroup").live("click", function() {
                var A = $(this).parent().parent().parent().parent();
                _.deleteGroup(A)
            });
            $("#" + _.id + " .addrule").live("click", function() {
                var A = $(this).parent().parent().parent().parent();
                _.addRule(A)
            });
            $("#" + _.id + " .deleterole").live("click", function() {
                var A = $(this).parent().parent();
                _.deleteRule(A)
            })
        },
        _setFields: function(B) {
            var _ = this,
                A = this.options;
            if (_.group) _.group.remove();
            _.group = $(_._bulidGroupTableHtml()).appendTo(_.element)
        },
        editors: {},
        editorCounter: 0,
        addGroup: function(A) {
            var _ = this,
                C = this.options;
            A = $(A || _.group);
            var B = $(">tbody:first > tr:last", A),
                E = [];
            E.push("<tr class=\"l-filter-rowgroup\"><td class=\"l-filter-cellgroup\" colSpan=\"4\">");
            var D = !A.hasClass("l-filter-group-alt");
            E.push(_._bulidGroupTableHtml(D, true));
            E.push("</td></tr>");
            var F = $(E.join(""));
            B.before(F);
            return F.find("table:first")
        },
        deleteGroup: function(B) {
            var _ = this,
                A = this.options;
            $("td.l-filter-value", B).each(function() {
                var A = $(this).parent();
                $("select.fieldsel", A).unbind();
                _.removeEditor(A)
            });
            $(B).parent().parent().remove()
        },
        removeEditor: function(C) {
            var A = this,
                D = this.options,
                E = $(C).attr("editortype"),
                _ = $(C).attr("editorid"),
                B = A.editors[_];
            if (B) D.editors[E].destroy(B);
            $("td.l-filter-value:first", C).html("")
        },
        setData: function(D, A) {
            var _ = this,
                C = this.options;
            A = A || _.group;
            var B = $(">tbody:first > tr:last", A);
            A.find(">tbody:first > tr").not(B).remove();
            $("select:first", B).val(D.op);
            if (D.rules) $(D.rules).each(function() {
                var D = _.addRule(A);
                D.attr("fieldtype", this.type || "string");
                $("select.opsel", D).val(this.op);
                $("select.fieldsel", D).val(this.field).trigger("change");
                var E = D.attr("editorid");
                if (E && _.editors[E]) {
                    var B = _.getField(this.field);
                    C.editors[B.editor.type].setValue(_.editors[E], this.value, B)
                } else $(":text", D).val(this.value)
            });
            if (D.groups) $(D.groups).each(function() {
                var $ = _.addGroup(A);
                _.setData(this, $)
            })
        },
        addRule: function(A) {
            var _ = this,
                D = this.options;
            A = A || _.group;
            var C = $(">tbody:first > tr:last", A),
                B = $(_._bulidRuleRowHtml());
            C.before(B);
            if (D.fields.length) _.appendEditor(B, D.fields[0]);
            $("select.fieldsel", B).bind("change", function() {
                var D = $(this).parent().next().find("select:first"),
                    G = $(this).val(),
                    A = _.getField(G),
                    C = A.type || "string",
                    F = B.attr("fieldtype");
                if (C != F) {
                    D.html(_._bulidOpSelectOptionsHtml(C));
                    B.attr("fieldtype", C)
                }
                var H = null,
                    E = B.attr("editortype");
                if (_.enabledEditor(A)) H = A.editor.type;
                if (E) _.removeEditor(B);
                if (H) _.appendEditor(B, A);
                else {
                    B.removeAttr("editortype").removeAttr("editorid");
                    $("td.l-filter-value:first", B).html("<input type=\"text\" class=\"valtxt\" />")
                }
            });
            return B
        },
        deleteRule: function(_) {
            $("select.fieldsel", _).unbind();
            this.removeEditor(_);
            $(_).remove()
        },
        appendEditor: function(C, _) {
            var A = this,
                E = this.options;
            if (A.enabledEditor(_)) {
                var D = $("td.l-filter-value:first", C).html(""),
                    B = E.editors[_.editor.type];
                A.editors[++A.editorCounter] = B.create(D, _);
                C.attr("editortype", _.editor.type).attr("editorid", A.editorCounter)
            }
        },
        getData: function(C) {
            var _ = this,
                A = this.options;
            C = C || _.group;
            var B = {};
            $("> tbody > tr", C).each(function(K, J) {
                var H = $(J).hasClass("l-filter-rowlast"),
                    I = $(J).hasClass("l-filter-rowgroup");
                if (I) {
                    var G = $("> td:first > table:first", J);
                    if (G.length) {
                        if (!B.groups) B.groups = [];
                        B.groups.push(_.getData(G))
                    }
                } else if (H) B.op = $(".groupopsel:first", J).val();
                else {
                    var F = $("select.fieldsel:first", J).val(),
                        A = _.getField(F),
                        C = $(".opsel:first", J).val(),
                        D = _._getRuleValue(J, A),
                        E = $(J).attr("fieldtype") || "string";
                    if (!B.rules) B.rules = [];
                    B.rules.push({
                        field: F,
                        op: C,
                        value: D,
                        type: E
                    })
                }
            });
            return B
        },
        _getRuleValue: function(D, _) {
            var A = this,
                E = this.options,
                F = $(D).attr("editorid"),
                C = $(D).attr("editortype"),
                B = A.editors[F];
            if (B) return E.editors[C].getValue(B, _);
            return $(".valtxt:first", D).val()
        },
        enabledEditor: function($) {
            var _ = this,
                A = this.options;
            if (!$.editor || !$.editor.type) return false;
            return ($.editor.type in A.editors)
        },
        getField: function(C) {
            var _ = this,
                A = this.options;
            for (var D = 0, B = A.fields.length; D < B; D++) {
                var $ = A.fields[D];
                if ($.name == C) return $
            }
            return null
        },
        _bulidGroupTableHtml: function(A, C) {
            var $ = this,
                _ = this.options,
                B = [];
            B.push("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"l-filter-group");
            if (A) B.push(" l-filter-group-alt");
            B.push("\"><tbody>");
            B.push("<tr class=\"l-filter-rowlast\"><td class=\"l-filter-rowlastcell\" align=\"right\" colSpan=\"4\">");
            B.push("<select class=\"groupopsel\">");
            B.push("<option value=\"and\">" + _.strings["and"] + "</option>");
            B.push("<option value=\"or\">" + _.strings["or"] + "</option>");
            B.push("</select>");
            B.push("<input type=\"button\" value=\"" + _.strings["addgroup"] + "\" class=\"addgroup\">");
            B.push("<input type=\"button\" value=\"" + _.strings["addrule"] + "\" class=\"addrule\">");
            if (C) B.push("<input type=\"button\" value=\"" + _.strings["deletegroup"] + "\" class=\"deletegroup\">");
            B.push("</td></tr>");
            B.push("</tbody></table>");
            return B.join("")
        },
        _bulidRuleRowHtml: function(F) {
            var A = this,
                C = this.options;
            F = F || C.fields;
            var _ = [],
                B = F[0].type || "string";
            _.push("<tr fieldtype=\"" + B + "\"><td class=\"l-filter-column\">");
            _.push("<select class=\"fieldsel\">");
            for (var E = 0, D = F.length; E < D; E++) {
                var $ = F[E];
                _.push("<option value=\"" + $.name + "\"");
                if (E == 0) _.push(" selected ");
                _.push(">");
                _.push($.display);
                _.push("</option>")
            }
            _.push("</select>");
            _.push("</td>");
            _.push("<td class=\"l-filter-op\">");
            _.push("<select class=\"opsel\">");
            _.push(A._bulidOpSelectOptionsHtml(B));
            _.push("</select>");
            _.push("</td>");
            _.push("<td class=\"l-filter-value\">");
            _.push("<input type=\"text\" class=\"valtxt\" />");
            _.push("</td>");
            _.push("<td>");
            _.push("<div class=\"l-icon-cross deleterole\"></div>");
            _.push("</td>");
            _.push("</tr>");
            return _.join("")
        },
        _bulidOpSelectOptionsHtml: function(A) {
            var $ = this,
                C = this.options,
                F = C.operators[A],
                B = [];
            for (var E = 0, D = F.length; E < D; E++) {
                var _ = F[E];
                B[B.length] = "<option value=\"" + _ + "\">";
                B[B.length] = C.strings[_];
                B[B.length] = "</option>"
            }
            return B.join("")
        }
    })
})(jQuery);
(function($) {
    $.fn.ligerForm = function() {
        return $.ligerui.run.call(this, "ligerForm", arguments)
    };
    $.ligerDefaults = $.ligerDefaults || {};
    $.ligerDefaults.Form = {
        inputWidth: 180,
        labelWidth: 90,
        space: 40,
        rightToken: "\uff1a",
        labelAlign: "left",
        align: "left",
        fields: [],
        appendID: true,
        prefixID: "",
        toJSON: $.ligerui.toJSON
    };
    $.ligerDefaults.Form.editorBulider = function(C) {
        var _ = this,
            A = this.options,
            B = {};
        if (A.inputWidth) B.width = A.inputWidth;
        if (C.is("select")) C.ligerComboBox(B);
        else if (C.is(":text") || C.is(":password")) {
            var $ = C.attr("ltype");
            switch ($) {
            case "select":
            case "combobox":
                C.ligerComboBox(B);
                break;
            case "spinner":
                C.ligerSpinner(B);
                break;
            case "date":
                C.ligerDateEditor(B);
                break;
            case "float":
            case "number":
                B.number = true;
                C.ligerTextBox(B);
                break;
            case "int":
            case "digits":
                B.digits = true;
            default:
                C.ligerTextBox(B);
                break
            }
        } else if (C.is(":radio")) C.ligerRadio(B);
        else if (C.is(":checkbox")) C.ligerCheckBox(B);
        else if (C.is("textarea")) C.addClass("l-textarea")
    };
    $.ligerui.controls.Form = function(_, A) {
        $.ligerui.controls.Form.base.constructor.call(this, _, A)
    };
    $.ligerui.controls.Form.ligerExtend($.ligerui.core.UIComponent, {
        __getType: function() {
            return "Form"
        },
        __idPrev: function() {
            return "Form"
        },
        _init: function() {
            $.ligerui.controls.Form.base._init.call(this)
        },
        _render: function() {
            var _ = this,
                B = this.options,
                A = $(this.element);
            if (B.fields && B.fields.length) {
                if (!A.hasClass("l-form")) A.addClass("l-form");
                var D = [],
                    C = false;
                $(B.fields).each(function(A, $) {
                    var B = $.name || $.id;
                    if (!B) return;
                    if ($.type == "hidden") {
                        D.push("<input type=\"hidden\" id=\"" + B + "\" name=\"" + B + "\" />");
                        return
                    }
                    var E = $.renderToNewLine || $.newline;
                    if (E == null) E = true;
                    if ($.merge) E = false;
                    if ($.group) E = true;
                    if (E) {
                        if (C) {
                            D.push("</ul>");
                            C = false
                        }
                        if ($.group) {
                            D.push("<div class=\"l-group");
                            if ($.groupicon) D.push(" l-group-hasicon");
                            D.push("\">");
                            if ($.groupicon) D.push("<img src=\"" + $.groupicon + "\" />");
                            D.push("<span>" + $.group + "</span></div>")
                        }
                        D.push("<ul>");
                        C = true
                    }
                    D.push(_._buliderLabelContainer($));
                    D.push(_._buliderControlContainer($));
                    D.push(_._buliderSpaceContainer($))
                });
                if (C) {
                    D.push("</ul>");
                    C = false
                }
                A.append(D.join(""))
            }
            $("input,select,textarea", A).each(function() {
                B.editorBulider.call(_, $(this))
            })
        },
        _buliderLabelContainer: function($) {
            var _ = this,
                B = this.options,
                C = $.label || $.display,
                A = $.labelWidth || $.labelwidth || B.labelWidth,
                D = $.labelAlign || B.labelAlign;
            if (C) C += B.rightToken;
            var E = [];
            E.push("<li style=\"");
            if (A) E.push("width:" + A + "px;");
            if (D) E.push("text-align:" + D + ";");
            E.push("\">");
            if (C) E.push(C);
            E.push("</li>");
            return E.join("")
        },
        _buliderControlContainer: function($) {
            var _ = this,
                C = this.options,
                B = $.width || C.inputWidth,
                A = $.align || $.textAlign || $.textalign || C.align,
                D = [];
            D.push("<li style=\"");
            if (B) D.push("width:" + B + "px;");
            if (A) D.push("text-align:" + A + ";");
            D.push("\">");
            D.push(_._buliderControl($));
            D.push("</li>");
            return D.join("")
        },
        _buliderSpaceContainer: function($) {
            var _ = this,
                A = this.options,
                B = $.space || $.spaceWidth || A.space,
                C = [];
            C.push("<li style=\"");
            if (B) C.push("width:" + B + "px;");
            C.push("\">");
            C.push("</li>");
            return C.join("")
        },
        _buliderControl: function(_) {
            var A = this,
                D = this.options,
                C = _.width || D.inputWidth,
                B = _.name || _.id,
                G = [];
            if (_.comboboxName && _.type == "select") G.push("<input type=\"hidden\" id=\"" + D.prefixID + B + "\" name=\"" + B + "\" />");
            if (_.textarea || _.type == "textarea") G.push("<textarea ");
            else if (_.type == "checkbox") G.push("<input type=\"checkbox\" ");
            else if (_.type == "radio") G.push("<input type=\"radio\" ");
            else if (_.type == "password") G.push("<input type=\"password\" ");
            else G.push("<input type=\"text\" ");
            if (_.cssClass) G.push("class=\"" + _.cssClass + "\" ");
            if (_.type) G.push("ltype=\"" + _.type + "\" ");
            if (_.attr) for (var E in _.attr) G.push(E + "=\"" + _.attr[E] + "\" ");
            if (_.comboboxName && _.type == "select") {
                G.push("name=\"" + _.comboboxName + "\"");
                if (D.appendID) G.push(" id=\"" + D.prefixID + _.comboboxName + "\" ")
            } else {
                G.push("name=\"" + B + "\"");
                if (D.appendID) G.push(" id=\"" + B + "\" ")
            }
            var F = $.extend({
                width: C - 2
            }, _.options || {});
            G.push(" ligerui='" + D.toJSON(F) + "' ");
            if (_.validate) G.push(" validate='" + D.toJSON(_.validate) + "' ");
            G.push(" />");
            return G.join("")
        }
    })
})(jQuery);
(function($) {
    var l = $.ligerui;
    $.fn.ligerGrid = function(_) {
        return $.ligerui.run.call(this, "ligerGrid", arguments)
    };
    $.fn.ligerGetGridManager = function() {
        return $.ligerui.run.call(this, "ligerGetGridManager", arguments)
    };
    $.ligerDefaults.Grid = {
        title: null,
        width: "auto",
        height: "auto",
        columnWidth: null,
        resizable: true,
        url: false,
        usePager: true,
        page: 1,
        pageSize: 10,
        pageSizeOptions: [10, 20, 30, 40, 50],
        parms: [],
        columns: [],
        minColToggle: 1,
        dataType: "server",
        dataAction: "server",
        showTableToggleBtn: false,
        switchPageSizeApplyComboBox: false,
        allowAdjustColWidth: true,
        checkbox: false,
        allowHideColumn: true,
        enabledEdit: false,
        isScroll: true,
        onDragCol: null,
        onToggleCol: null,
        onChangeSort: null,
        onSuccess: null,
        onDblClickRow: null,
        onSelectRow: null,
        onUnSelectRow: null,
        onBeforeCheckRow: null,
        onCheckRow: null,
        onBeforeCheckAllRow: null,
        onCheckAllRow: null,
        onBeforeShowData: null,
        onAfterShowData: null,
        onError: null,
        onSubmit: null,
        dateFormat: "yyyy-MM-dd",
        InWindow: true,
        statusName: "__status",
        method: "post",
        async: true,
        fixedCellHeight: true,
        heightDiff: 0,
        cssClass: null,
        root: "Rows",
        record: "Total",
        pageParmName: "page",
        pagesizeParmName: "pagesize",
        sortnameParmName: "sortname",
        sortorderParmName: "sortorder",
        onReload: null,
        onToFirst: null,
        onToPrev: null,
        onToNext: null,
        onToLast: null,
        allowUnSelectRow: false,
        alternatingRow: true,
        mouseoverRowCssClass: "l-grid-row-over",
        enabledSort: true,
        rowAttrRender: null,
        groupColumnName: null,
        groupColumnDisplay: "\u5206\u7ec4",
        groupRender: null,
        totalRender: null,
        delayLoad: false,
        where: null,
        selectRowButtonOnly: false,
        onAfterAddRow: null,
        onBeforeEdit: null,
        onBeforeSubmitEdit: null,
        onAfterEdit: null,
        onLoading: null,
        onLoaded: null,
        onContextmenu: null,
        whenRClickToSelect: false,
        contentType: null,
        checkboxColWidth: 27,
        detailColWidth: 29,
        clickToEdit: true,
        detailToEdit: false,
        onEndEdit: null,
        minColumnWidth: 80,
        tree: null,
        isChecked: null,
        frozen: true,
        frozenDetail: false,
        frozenCheckbox: true,
        detailHeight: 260,
        rownumbers: false,
        frozenRownumbers: true,
        rownumbersColWidth: 26,
        colDraggable: false,
        rowDraggable: false,
        rowDraggingRender: null,
        autoCheckChildren: true,
        onRowDragDrop: null,
        rowHeight: 22,
        headerRowHeight: 23,
        toolbar: null,
        headerImg: null
    };
    $.ligerDefaults.GridString = {
        errorMessage: "\u53d1\u751f\u9519\u8bef",
        pageStatMessage: "\u663e\u793a\u4ece{from}\u5230{to}\uff0c\u603b {total} \u6761 \u3002\u6bcf\u9875\u663e\u793a\uff1a{pagesize}",
        pageTextMessage: "Page",
        loadingMessage: "\u52a0\u8f7d\u4e2d...",
        findTextMessage: "\u67e5\u627e",
        noRecordMessage: "\u6ca1\u6709\u7b26\u5408\u6761\u4ef6\u7684\u8bb0\u5f55\u5b58\u5728",
        isContinueByDataChanged: "\u6570\u636e\u5df2\u7ecf\u6539\u53d8,\u5982\u679c\u7ee7\u7eed\u5c06\u4e22\u5931\u6570\u636e,\u662f\u5426\u7ee7\u7eed?",
        cancelMessage: "\u53d6\u6d88",
        saveMessage: "\u4fdd\u5b58",
        applyMessage: "\u5e94\u7528",
        draggingMessage: "{count}\u884c"
    };
    $.ligerMethos.Grid = $.ligerMethos.Grid || {};
    $.ligerDefaults.Grid.sorters = $.ligerDefaults.Grid.sorters || {};
    $.ligerDefaults.Grid.formatters = $.ligerDefaults.Grid.formatters || {};
    $.ligerDefaults.Grid.editors = $.ligerDefaults.Grid.editors || {};
    $.ligerDefaults.Grid.sorters["date"] = function($, _) {
        return $ < _ ? -1 : $ > _ ? 1 : 0
    };
    $.ligerDefaults.Grid.sorters["int"] = function($, _) {
        return parseInt($) < parseInt(_) ? -1 : parseInt($) > parseInt(_) ? 1 : 0
    };
    $.ligerDefaults.Grid.sorters["float"] = function($, _) {
        return parseFloat($) < parseFloat(_) ? -1 : parseFloat($) > parseFloat(_) ? 1 : 0
    };
    $.ligerDefaults.Grid.sorters["string"] = function($, _) {
        return $.localeCompare(_)
    };
    $.ligerDefaults.Grid.formatters["date"] = function(value, column) {
        function getFormatDate(D, _) {
            var $ = this,
                A = this.options;
            if (isNaN(D)) return null;
            var C = _,
                B = {
                    "M+": D.getMonth() + 1,
                    "d+": D.getDate(),
                    "h+": D.getHours(),
                    "m+": D.getMinutes(),
                    "s+": D.getSeconds(),
                    "q+": Math.floor((D.getMonth() + 3) / 3),
                    "S": D.getMilliseconds()
                };
            if (/(y+)/.test(C)) C = C.replace(RegExp.$1, (D.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var E in B) if (new RegExp("(" + E + ")").test(C)) C = C.replace(RegExp.$1, RegExp.$1.length == 1 ? B[E] : ("00" + B[E]).substr(("" + B[E]).length));
            return C
        }
        if (!value) return "";
        if (typeof(value) == "string" && /^\/Date/.test(value)) {
            value = value.replace(/^\//, "new ").replace(/\/$/, "");
            eval("value = " + value)
        }
        if (value instanceof Date) {
            var format = column.format || this.options.dateFormat || "yyyy-MM-dd";
            return getFormatDate(value, format)
        } else return value.toString()
    };
    $.ligerDefaults.Grid.editors["date"] = {
        create: function(B, _) {
            var C = _.column,
                A = $("<input type='text'/>");
            B.append(A);
            var F = {},
                E = C.editor.p || C.editor.ext;
            if (E) {
                var D = typeof(E) == "function" ? E(_.record, _.rowindex, _.value, C) : E;
                $.extend(F, D)
            }
            A.ligerDateEditor(F);
            return A
        },
        getValue: function(_, $) {
            return _.liger("option", "value")
        },
        setValue: function(_, A, $) {
            _.liger("option", "value", A)
        },
        resize: function(_, B, A, $) {
            _.liger("option", "width", B);
            _.liger("option", "height", A)
        },
        destroy: function(_, $) {
            _.liger("destroy")
        }
    };
    $.ligerDefaults.Grid.editors["select"] = $.ligerDefaults.Grid.editors["combobox"] = {
        create: function(B, _) {
            var C = _.column,
                A = $("<input type='text'/>");
            B.append(A);
            var F = {
                data: C.editor.data,
                slide: false,
                valueField: C.editor.valueField || C.editor.valueColumnName,
                textField: C.editor.textField || C.editor.displayColumnName
            },
                E = C.editor.p || C.editor.ext;
            if (E) {
                var D = typeof(E) == "function" ? E(_.record, _.rowindex, _.value, C) : E;
                $.extend(F, D)
            }
            A.ligerComboBox(F);
            return A
        },
        getValue: function(_, $) {
            return _.liger("option", "value")
        },
        setValue: function(_, A, $) {
            _.liger("option", "value", A)
        },
        resize: function(_, B, A, $) {
            _.liger("option", "width", B);
            _.liger("option", "height", A)
        },
        destroy: function(_, $) {
            _.liger("destroy")
        }
    };
    $.ligerDefaults.Grid.editors["int"] = $.ligerDefaults.Grid.editors["float"] = $.ligerDefaults.Grid.editors["spinner"] = {
        create: function(B, _) {
            var C = _.column,
                A = $("<input type='text'/>");
            B.append(A);
            A.css({
                border: "#6E90BE"
            });
            var D = {
                type: C.editor.type == "float" ? "float" : "int"
            };
            if (C.editor.minValue != undefined) D.minValue = C.editor.minValue;
            if (C.editor.maxValue != undefined) D.maxValue = C.editor.maxValue;
            A.ligerSpinner(D);
            return A
        },
        getValue: function(_, $) {
            var A = $.column,
                B = A.editor.type == "int";
            if (B) return parseInt(_.val(), 10);
            else return parseFloat(_.val())
        },
        setValue: function(_, A, $) {
            _.val(A)
        },
        resize: function(_, B, A, $) {
            _.liger("option", "width", B);
            _.liger("option", "height", A)
        },
        destroy: function(_, $) {
            _.liger("destroy")
        }
    };
    $.ligerDefaults.Grid.editors["string"] = $.ligerDefaults.Grid.editors["text"] = {
        create: function(B, _) {
            var A = $("<input type='text' class='l-text-editing'/>");
            B.append(A);
            A.ligerTextBox();
            return A
        },
        getValue: function(_, $) {
            return _.val()
        },
        setValue: function(_, A, $) {
            _.val(A)
        },
        resize: function(_, B, A, $) {
            _.liger("option", "width", B);
            _.liger("option", "height", A)
        },
        destroy: function(_, $) {
            _.liger("destroy")
        }
    };
    $.ligerDefaults.Grid.editors["chk"] = $.ligerDefaults.Grid.editors["checkbox"] = {
        create: function(B, _) {
            var A = $("<input type='checkbox' />");
            B.append(A);
            A.ligerCheckBox();
            return A
        },
        getValue: function(_, $) {
            return _[0].checked ? 1 : 0
        },
        setValue: function(_, A, $) {
            _.val(A ? true : false)
        },
        resize: function(_, B, A, $) {
            _.liger("option", "width", B);
            _.liger("option", "height", A)
        },
        destroy: function(_, $) {
            _.liger("destroy")
        }
    };
    $.ligerui.controls.Grid = function(_, A) {
        $.ligerui.controls.Grid.base.constructor.call(this, _, A)
    };
    $.ligerui.controls.Grid.ligerExtend($.ligerui.core.UIComponent, {
        __getType: function() {
            return "$.ligerui.controls.Grid"
        },
        __idPrev: function() {
            return "grid"
        },
        _extendMethods: function() {
            return $.ligerMethos.Grid
        },
        _init: function() {
            $.ligerui.controls.Grid.base._init.call(this);
            var _ = this,
                A = this.options;
            A.dataType = A.url ? "server" : "local";
            if (A.dataType == "local") {
                A.data = A.data || [];
                A.dataAction = "local"
            }
            if (A.isScroll == false) A.height = "auto";
            if (!A.frozen) {
                A.frozenCheckbox = false;
                A.frozenDetail = false;
                A.frozenRownumbers = false
            }
            if (A.detailToEdit) {
                A.enabledEdit = true;
                A.clickToEdit = false;
                A.detail = {
                    height: "auto",
                    onShowDetail: function(B, C, D) {
                        $(C).addClass("l-grid-detailpanel-edit");
                        _.beginEdit(B, function(D, _) {
                            var B = $("<div class='l-editbox'></div>");
                            B.width(120).height(A.rowHeight + 1);
                            B.appendTo(C);
                            return B
                        });

                        function E() {
                            $(C).parent().parent().remove();
                            _.collapseDetail(B)
                        }
                        $("<div class='l-clear'></div>").appendTo(C);
                        $("<div class='l-button'>" + A.saveMessage + "</div>").appendTo(C).click(function() {
                            _.endEdit(B);
                            E()
                        });
                        $("<div class='l-button'>" + A.applyMessage + "</div>").appendTo(C).click(function() {
                            _.submitEdit(B)
                        });
                        $("<div class='l-button'>" + A.cancelMessage + "</div>").appendTo(C).click(function() {
                            _.cancelEdit(B);
                            E()
                        })
                    }
                }
            }
            if (A.tree) {
                A.tree.childrenName = A.tree.childrenName || "children";
                A.tree.isParent = A.tree.isParent ||
                function($) {
                    var _ = A.tree.childrenName in $;
                    return _
                };
                A.tree.isExtend = A.tree.isExtend ||
                function($) {
                    if ("isextend" in $ && $["isextend"] == false) return false;
                    return true
                }
            }
        },
        _render: function() {
            var _ = this,
                B = this.options;
            _.grid = $(_.element);
            _.grid.addClass("l-panel");
            var C = [];
            C.push("        <div class='l-panel-header'><span class='l-panel-header-text'></span></div>");
            C.push("                    <div class='l-grid-loading'></div>");
            C.push("        <div class='l-panel-topbar'></div>");
            C.push("        <div class='l-panel-bwarp'>");
            C.push("            <div class='l-panel-body'>");
            C.push("                <div class='l-grid'>");
            C.push("                    <div class='l-grid-dragging-line'></div>");
            C.push("                    <div class='l-grid-popup'><table cellpadding='0' cellspacing='0'><tbody></tbody></table></div>");
            C.push("                  <div class='l-grid1'>");
            C.push("                      <div class='l-grid-header l-grid-header1'>");
            C.push("                          <div class='l-grid-header-inner'><table class='l-grid-header-table' cellpadding='0' cellspacing='0'><tbody></tbody></table></div>");
            C.push("                      </div>");
            C.push("                      <div class='l-grid-body l-grid-body1'>");
            C.push("                      </div>");
            C.push("                  </div>");
            C.push("                  <div class='l-grid2'>");
            C.push("                      <div class='l-grid-header l-grid-header2'>");
            C.push("                          <div class='l-grid-header-inner'><table class='l-grid-header-table' cellpadding='0' cellspacing='0'><tbody></tbody></table></div>");
            C.push("                      </div>");
            C.push("                      <div class='l-grid-body l-grid-body2 l-scroll'>");
            C.push("                      </div>");
            C.push("                  </div>");
            C.push("                 </div>");
            C.push("              </div>");
            C.push("         </div>");
            C.push("         <div class='l-panel-bar'>");
            C.push("            <div class='l-panel-bbar-inner'>");
            C.push("                <div class='l-bar-group  l-bar-message'><span class='l-bar-text'></span></div>");
            C.push("            <div class='l-bar-group l-bar-selectpagesize'></div>");
            C.push("                <div class='l-bar-separator'></div>");
            C.push("                <div class='l-bar-group'>");
            C.push("                    <div class='l-bar-button l-bar-btnfirst'><span></span></div>");
            C.push("                    <div class='l-bar-button l-bar-btnprev'><span></span></div>");
            C.push("                </div>");
            C.push("                <div class='l-bar-separator'></div>");
            C.push("                <div class='l-bar-group'><span class='pcontrol'> <input type='text' size='4' value='1' style='width:20px' maxlength='3' /> / <span></span></span></div>");
            C.push("                <div class='l-bar-separator'></div>");
            C.push("                <div class='l-bar-group'>");
            C.push("                     <div class='l-bar-button l-bar-btnnext'><span></span></div>");
            C.push("                    <div class='l-bar-button l-bar-btnlast'><span></span></div>");
            C.push("                </div>");
            C.push("                <div class='l-bar-separator'></div>");
            C.push("                <div class='l-bar-group'>");
            C.push("                     <div class='l-bar-button l-bar-btnload'><span></span></div>");
            C.push("                </div>");
            C.push("                <div class='l-bar-separator'></div>");
            C.push("                <div class='l-clear'></div>");
            C.push("            </div>");
            C.push("         </div>");
            _.grid.html(C.join(""));
            _.header = $(".l-panel-header:first", _.grid);
            _.body = $(".l-panel-body:first", _.grid);
            _.toolbar = $(".l-panel-bar:first", _.grid);
            _.popup = $(".l-grid-popup:first", _.grid);
            _.gridloading = $(".l-grid-loading:first", _.grid);
            _.draggingline = $(".l-grid-dragging-line", _.grid);
            _.topbar = $(".l-panel-topbar:first", _.grid);
            _.gridview = $(".l-grid:first", _.grid);
            _.gridview.attr("id", _.id + "grid");
            _.gridview1 = $(".l-grid1:first", _.gridview);
            _.gridview2 = $(".l-grid2:first", _.gridview);
            _.gridheader = $(".l-grid-header:first", _.gridview2);
            _.gridbody = $(".l-grid-body:first", _.gridview2);
            _.f = {};
            _.f.gridheader = $(".l-grid-header:first", _.gridview1);
            _.f.gridbody = $(".l-grid-body:first", _.gridview1);
            _.currentData = null;
            _.changedCells = {};
            _.editors = {};
            _.editor = {
                editing: false
            };
            if (B.height == "auto") _.bind("SysGridHeightChanged", function() {
                if (_.enabledFrozen()) _.gridview.height(Math.max(_.gridview1.height(), _.gridview2.height()))
            });
            var A = $.extend({}, B);
            this._bulid();
            this._setColumns(B.columns);
            delete A["columns"];
            delete A["data"];
            delete A["url"];
            _.set(A);
            if (!B.delayLoad) if (B.url) _.set({
                url: B.url
            });
            else if (B.data) _.set({
                data: B.data
            })
        },
        _setFrozen: function($) {
            if ($) this.grid.addClass("l-frozen");
            else this.grid.removeClass("l-frozen")
        },
        _setCssClass: function($) {
            this.grid.addClass($)
        },
        _setLoadingMessage: function($) {
            this.gridloading.html($)
        },
        _setHeight: function(B) {
            var _ = this,
                A = this.options;
            _.unbind("SysGridHeightChanged");
            if (B == "auto") {
                _.bind("SysGridHeightChanged", function() {
                    if (_.enabledFrozen()) _.gridview.height(Math.max(_.gridview1.height(), _.gridview2.height()))
                });
                return
            }
            if (typeof B == "string" && B.indexOf("%") > 0) if (A.inWindow) B = $(window).height() * parseFloat(B) * 0.01;
            else B = _.grid.parent().height() * parseFloat(B) * 0.01;
            if (A.title) B -= 24;
            if (A.usePager) B -= 32;
            if (A.totalRender) B -= 25;
            if (A.toolbar) B -= _.topbar.outerHeight();
            var C = A.headerRowHeight * (_._columnMaxLevel - 1) + A.headerRowHeight - 1;
            B -= C;
            if (B > 0) {
                _.gridbody.height(B);
                if (B > 18) _.f.gridbody.height(B - 18);
                _.gridview.height(B + C)
            }
        },
        _updateFrozenWidth: function() {
            var $ = this,
                A = this.options;
            if ($.enabledFrozen()) {
                $.gridview1.width($.f.gridtablewidth);
                var _ = $.gridview.width() - $.f.gridtablewidth;
                $.gridview2.css({
                    left: $.f.gridtablewidth
                });
                if (_ > 0) $.gridview2.css({
                    width: _
                })
            }
        },
        _setWidth: function(A) {
            var $ = this,
                _ = this.options;
            if ($.enabledFrozen()) $._onResize()
        },
        _setUrl: function($) {
            this.options.url = $;
            if ($) {
                this.options.dataType = "server";
                this.loadData(true)
            } else this.options.dataType = "local"
        },
        _setData: function($) {
            this.loadData(this.options.data)
        },
        loadData: function(B) {
            var A = this,
                E = this.options;
            A.loading = true;
            var F = null,
                _ = true;
            if (typeof(B) == "function") {
                F = B;
                _ = false
            } else if (typeof(B) == "boolean") _ = B;
            else if (typeof(B) == "object" && B) {
                _ = false;
                E.dataType = "local";
                E.data = B
            }
            if (!E.newPage) E.newPage = 1;
            if (E.dataAction == "server") if (!E.sortOrder) E.sortOrder = "asc";
            var C = [];
            if (E.parms) if (E.parms.length) $(E.parms).each(function() {
                C.push({
                    name: this.name,
                    value: this.value
                })
            });
            else if (typeof E.parms == "object") for (var D in E.parms) C.push({
                name: D,
                value: E.parms[D]
            });
            if (E.dataAction == "server") {
                if (E.usePager) {
                    C.push({
                        name: E.pageParmName,
                        value: E.newPage
                    });
                    C.push({
                        name: E.pagesizeParmName,
                        value: E.pageSize
                    })
                }
                if (E.sortName) {
                    C.push({
                        name: E.sortnameParmName,
                        value: E.sortName
                    });
                    C.push({
                        name: E.sortorderParmName,
                        value: E.sortOrder
                    })
                }
            }
            $(".l-bar-btnload span", A.toolbar).addClass("l-disabled");
            if (E.dataType == "local") {
                A.filteredData = A.data = E.data;
                if (F) A.filteredData[E.root] = A._searchData(A.filteredData[E.root], F);
                if (E.usePager) A.currentData = A._getCurrentPageData(A.filteredData);
                else A.currentData = A.filteredData;
                A._showData()
            } else if (E.dataAction == "local" && !_) {
                if (A.data && A.data[E.root]) {
                    A.filteredData = A.data;
                    if (F) A.filteredData[E.root] = A._searchData(A.filteredData[E.root], F);
                    A.currentData = A._getCurrentPageData(A.filteredData);
                    A._showData()
                }
            } else A.loadServerData(C, F);
            A.loading = false
        },
        loadServerData: function(A, D) {
            var _ = this,
                B = this.options,
                C = {
                    type: B.method,
                    url: B.url,
                    data: A,
                    async: B.async,
                    dataType: "json",
                    beforeSend: function() {
                        if (_.hasBind("loading")) _.trigger("loading");
                        else _.toggleLoading(true)
                    },
                    success: function($) {
                        _.trigger("success", [$, _]);
                        if (!$ || !$[B.root] || !$[B.root].length) {
                            _.currentData = _.data = {};
                            _.currentData[B.root] = _.data[B.root] = [];
                            _.currentData[B.record] = _.data[B.record] = 0;
                            _._showData();
                            return
                        }
                        _.data = $;
                        if (B.dataAction == "server") _.currentData = _.data;
                        else {
                            _.filteredData = _.data;
                            if (D) _.filteredData[B.root] = _._searchData(_.filteredData[B.root], D);
                            if (B.usePager) _.currentData = _._getCurrentPageData(_.filteredData);
                            else _.currentData = _.filteredData
                        }
                        _._showData.ligerDefer(_, 10, [_.currentData])
                    },
                    complete: function() {
                        _.trigger("complete", [_]);
                        if (_.hasBind("loaded")) _.trigger("loaded", [_]);
                        else _.toggleLoading.ligerDefer(_, 10, [false])
                    },
                    error: function(D, A, C) {
                        _.currentData = _.data = {};
                        _.currentData[B.root] = _.data[B.root] = [];
                        _.currentData[B.record] = _.data[B.record] = 0;
                        _.toggleLoading.ligerDefer(_, 10, [false]);
                        $(".l-bar-btnload span", _.toolbar).removeClass("l-disabled");
                        _.trigger("error", [D, A, C])
                    }
                };
            if (B.contentType) C.contentType = B.contentType;
            $.ajax(C)
        },
        toggleLoading: function($) {
            this.gridloading[$ ? "show" : "hide"]()
        },
        _createEditor: function(_, C, $, D, A) {
            var B = _.create(C, $);
            if (_.setValue) _.setValue(B, $.value, $);
            if (_.resize) _.resize(B, D, A, $);
            return B
        },
        beginEdit: function(I, C) {
            var _ = this,
                K = this.options;
            if (!K.enabledEdit || K.clickToEdit) return;
            var M = _.getRow(I);
            if (M._editing) return;
            if (_.trigger("beginEdit", {
                record: M,
                rowindex: M["__index"]
            }) == false) return;
            _.editors[M["__id"]] = {};
            M._editing = true;
            _.reRender({
                rowdata: M
            });
            C = C ||
            function(C, B) {
                var D = _.getCellObj(C, B),
                    A = $(D).html("");
                _.setCellEditing(C, B, true);
                return A
            };
            for (var F = 0, E = _.columns.length; F < E; F++) {
                var L = _.columns[F];
                if (!L.name || !L.editor || !L.editor.type || !K.editors[L.editor.type]) continue;
                var G = K.editors[L.editor.type],
                    A = {
                        record: M,
                        value: M[L.name],
                        column: L,
                        rowindex: M["__index"],
                        grid: _
                    },
                    J = C(M, L),
                    D = J.width(),
                    H = J.height(),
                    B = _._createEditor(G, J, A, D, H);
                _.editors[M["__id"]][L["__id"]] = {
                    editor: G,
                    input: B,
                    editParm: A,
                    container: J
                }
            }
            _.trigger("afterBeginEdit", {
                record: M,
                rowindex: M["__index"]
            })
        },
        cancelEdit: function(_) {
            var $ = this;
            if (_ == undefined) {
                for (var D in $.editors) $.cancelEdit(D)
            } else {
                var C = $.getRow(_);
                if (!$.editors[C["__id"]]) return;
                if ($.trigger("cancelEdit", {
                    record: C,
                    rowindex: C["__index"]
                }) == false) return;
                for (var B in $.editors[C["__id"]]) {
                    var A = $.editors[C["__id"]][B];
                    if (A.editor.destroy) A.editor.destroy(A.input, A.editParm)
                }
                delete $.editors[C["__id"]];
                delete C["_editing"];
                $.reRender({
                    rowdata: C
                })
            }
        },
        addEditRow: function($) {
            this.submitEdit();
            $ = this.add($);
            this.beginEdit($)
        },
        submitEdit: function(A) {
            var _ = this,
                B = this.options;
            if (A == undefined) {
                for (var G in _.editors) _.submitEdit(G)
            } else {
                var F = _.getRow(A),
                    $ = {};
                if (!_.editors[F["__id"]]) return;
                for (var E in _.editors[F["__id"]]) {
                    var D = _.editors[F["__id"]][E],
                        C = D.editParm.column;
                    if (C.name) $[C.name] = D.editor.getValue(D.input, D.editParm)
                }
                if (_.trigger("beforeSubmitEdit", {
                    record: F,
                    rowindex: F["__index"],
                    newdata: $
                }) == false) return false;
                _.updateRow(F, $);
                _.trigger("afterSubmitEdit", {
                    record: F,
                    rowindex: F["__index"],
                    newdata: $
                })
            }
        },
        endEdit: function(_) {
            var $ = this,
                A = this.options;
            if ($.editor.editing) {
                var B = $.editor;
                $.trigger("sysEndEdit", [$.editor.editParm]);
                $.trigger("endEdit", [$.editor.editParm]);
                if (B.editor.destroy) B.editor.destroy(B.input, B.editParm);
                $.editor.container.remove();
                $.reRender({
                    rowdata: $.editor.editParm.record,
                    column: $.editor.editParm.column
                });
                $.trigger("afterEdit", [$.editor.editParm]);
                $.editor = {
                    editing: false
                }
            } else if (_ != undefined) {
                var D = $.getRow(_);
                if (!$.editors[D["__id"]]) return;
                if ($.submitEdit(_) == false) return false;
                for (var C in $.editors[D["__id"]]) {
                    B = $.editors[D["__id"]][C];
                    if (B.editor.destroy) B.editor.destroy(B.input, B.editParm)
                }
                delete $.editors[D["__id"]];
                delete D["_editing"];
                $.trigger("afterEdit", {
                    record: D,
                    rowindex: D["__index"]
                })
            } else for (var E in $.editors) $.endEdit(E)
        },
        setWidth: function($) {
            return this._setWidth($)
        },
        setHeight: function($) {
            return this._setHeight($)
        },
        enabledCheckbox: function() {
            return this.options.checkbox ? true : false
        },
        enabledFrozen: function() {
            var $ = this,
                A = this.options;
            if (!A.frozen) return false;
            var _ = $.columns || [];
            if ($.enabledDetail() && A.frozenDetail || $.enabledCheckbox() && A.frozenCheckbox || A.frozenRownumbers && A.rownumbers) return true;
            for (var C = 0, B = _.length; C < B; C++) if (_[C].frozen) return true;
            this._setFrozen(false);
            return false
        },
        enabledDetailEdit: function() {
            if (!this.enabledDetail()) return false;
            return this.options.detailToEdit ? true : false
        },
        enabledDetail: function() {
            if (this.options.detail && this.options.detail.onShowDetail) return true;
            return false
        },
        enabledGroup: function() {
            return this.options.groupColumnName ? true : false
        },
        deleteSelectedRow: function() {
            if (!this.selected) return;
            for (var _ in this.selected) {
                var $ = this.selected[_];
                if ($["__id"] in this.records) this._deleteData.ligerDefer(this, 10, [$])
            }
            this.reRender.ligerDefer(this, 20)
        },
        removeRange: function(A) {
            var _ = this,
                B = this.options;
            $.each(A, function() {
                _._removeData(this)
            });
            _.reRender()
        },
        remove: function(_) {
            var $ = this,
                A = this.options,
                B = $.getRow(_);
            $._removeData(_);
            $.reRender()
        },
        deleteRange: function(A) {
            var _ = this,
                B = this.options;
            $.each(A, function() {
                _._deleteData(this)
            });
            _.reRender()
        },
        deleteRow: function(_) {
            var $ = this,
                A = this.options,
                B = $.getRow(_);
            if (!B) return;
            $._deleteData(B);
            $.reRender();
            $.isDataChanged = true
        },
        _deleteData: function(_) {
            var $ = this,
                A = this.options,
                D = $.getRow(_);
            D[A.statusName] = "delete";
            if (A.tree) {
                var B = $.getChildren(D, true);
                if (B) for (var E = 0, C = B.length; E < C; E++) B[E][A.statusName] = "delete"
            }
            $.deletedRows = $.deletedRows || [];
            $.deletedRows.push(D);
            $._removeSelected(D)
        },
        updateCell: function(G, J, H) {
            var _ = this,
                I = this.options,
                K, A, L;
            if (typeof(G) == "string") {
                for (var F = 0, C = _.columns.length; F < C; F++) if (_.columns[F].name == G) _.updateCell(F, J, H);
                return
            }
            if (typeof(G) == "number") {
                K = _.columns[G];
                L = _.getRow(H);
                A = _.getCellObj(L, K)
            } else if (typeof(G) == "object" && G["__id"]) {
                K = G;
                L = _.getRow(H);
                A = _.getCellObj(L, K)
            } else {
                A = G;
                var B = A.id.split("|"),
                    D = B[B.length - 1];
                K = _._columns[D];
                var E = $(A).parent();
                L = L || _.getRow(E[0])
            }
            if (J != null && K.name) {
                L[K.name] = J;
                if (L[I.statusName] != "add") L[I.statusName] = "update";
                _.isDataChanged = true
            }
            _.reRender({
                rowdata: L,
                column: K
            })
        },
        addRows: function(C, E, B, A) {
            var _ = this,
                D = this.options;
            $(C).each(function() {
                _.addRow(this, E, B, A)
            })
        },
        _createRowid: function() {
            return "r" + (1000 + this.recordNumber)
        },
        _isRowId: function($) {
            return ($ in this.records)
        },
        _addNewRecord: function(C, A, D) {
            var $ = this,
                _ = this.options;
            $.recordNumber++;
            C["__id"] = $._createRowid();
            C["__previd"] = A;
            if (A && A != -1) {
                var F = $.records[A];
                if (F["__nextid"] && F["__nextid"] != -1) {
                    var E = $.records[F["__nextid"]];
                    if (E) E["__previd"] = C["__id"]
                }
                F["__nextid"] = C["__id"];
                C["__index"] = F["__index"] + 1
            } else C["__index"] = 0;
            if (_.tree) {
                if (D && D != -1) {
                    var B = $.records[D];
                    C["__pid"] = D;
                    C["__level"] = B["__level"] + 1
                } else {
                    C["__pid"] = -1;
                    C["__level"] = 1
                }
                C["__hasChildren"] = C[_.tree.childrenName] ? true : false
            }
            if (C[_.statusName] != "add") C[_.statusName] = "nochanged";
            $.rows[C["__index"]] = C;
            $.records[C["__id"]] = C;
            return C
        },
        _getRows: function(B) {
            var $ = this,
                A = this.options,
                _ = [];

            function C($) {
                if (!$ || !$.length) return;
                for (var E = 0, D = $.length; E < D; E++) {
                    var B = $[E];
                    _.push(B);
                    if (B[A.tree.childrenName]) C(B[A.tree.childrenName])
                }
            }
            C(B);
            return _
        },
        _updateGridData: function() {
            var $ = this,
                A = this.options;
            $.recordNumber = 0;
            $.rows = [];
            $.records = {};
            var _ = -1;

            function B(C, F) {
                if (!C || !C.length) return;
                for (var G = 0, E = C.length; G < E; G++) {
                    var D = C[G];
                    $.formatRecord(D);
                    if (D[A.statusName] == "delete") continue;
                    $._addNewRecord(D, _, F);
                    _ = D["__id"];
                    if (D["__hasChildren"]) B(D[A.tree.childrenName], D["__id"])
                }
            }
            B($.currentData[A.root], -1);
            return $.rows
        },
        _moveData: function(F, B, A) {
            var D = this,
                E = this.options,
                _ = D.getRow(F),
                I = D.getRow(B),
                C, G, H = D._getParentChildren(_);
            C = $.inArray(_, H);
            H.splice(C, 1);
            H = D._getParentChildren(I);
            G = $.inArray(I, H);
            H.splice(G + (A ? 1 : 0), 0, _)
        },
        move: function(A, _, $) {
            this._moveData(A, _, $);
            this.reRender()
        },
        moveRange: function(B, _, $) {
            for (var A in B) this._moveData(B[A], _, $);
            this.reRender()
        },
        up: function(C) {
            var _ = this,
                D = this.options,
                E = _.getRow(C),
                F = _._getParentChildren(E),
                B = $.inArray(E, F);
            if (B == -1 || B == 0) return;
            var A = _.getSelected();
            _.move(E, F[B - 1], false);
            _.select(A)
        },
        down: function(C) {
            var _ = this,
                D = this.options,
                E = _.getRow(C),
                F = _._getParentChildren(E),
                B = $.inArray(E, F);
            if (B == -1 || B == F.length - 1) return;
            var A = _.getSelected();
            _.move(E, F[B + 1], true);
            _.select(A)
        },
        addRow: function(F, E, A, _) {
            var $ = this,
                B = this.options;
            F = F || {};
            $._addData(F, _, E, A);
            $.reRender();
            F[B.statusName] = "add";
            if (B.tree) {
                var C = $.getChildren(F, true);
                if (C) for (var G = 0, D = C.length; G < D; G++) C[G][B.statusName] = "add"
            }
            $.isDataChanged = true;
            B.total = B.total ? (B.total + 1) : 1;
            B.pageCount = Math.ceil(B.total / B.pageSize);
            $._buildPager();
            $.trigger("SysGridHeightChanged");
            $.trigger("afterAddRow", [F]);
            return F
        },
        updateRow: function(A, B) {
            var _ = this,
                C = this.options,
                D = _.getRow(A);
            _.isDataChanged = true;
            $.extend(D, B || {});
            if (D[C.statusName] != "add") D[C.statusName] = "update";
            _.reRender.ligerDefer(_, 10, [{
                rowdata: D
            }]);
            return D
        },
        setCellEditing: function(G, E, A) {
            var _ = this,
                D = this.options,
                C = _.getCellObj(G, E),
                H = A ? "addClass" : "removeClass";
            $(C)[H]("l-grid-row-cell-editing");
            if (G["__id"] != 0) {
                var F = $(_.getRowObj(G["__id"])).prev();
                if (!F.length) return;
                var I = _.getRow(F[0]),
                    B = _.getCellObj(I, E);
                if (!B) return;
                $(B)[H]("l-grid-row-cell-editing-topcell")
            }
            if (E["__previd"] != -1 && E["__previd"] != null) {
                B = $(_.getCellObj(G, E)).prev();
                $(B)[H]("l-grid-row-cell-editing-leftcell")
            }
        },
        reRender: function(A) {
            var _ = this,
                D = this.options;
            A = A || {};
            var F = A.rowdata,
                E = A.column;
            if (E && (E.isdetail || E.ischeckbox)) return;
            if (F && F[D.statusName] == "delete") return;
            if (F && E) {
                var C = _.getCellObj(F, E);
                $(C).html(_._getCellHtml(F, E));
                if (!E.issystem) _.setCellEditing(F, E, false)
            } else if (F) $(_.columns).each(function() {
                _.reRender({
                    rowdata: F,
                    column: this
                })
            });
            else if (E) {
                for (var G in _.records) _.reRender({
                    rowdata: _.records[G],
                    column: E
                });
                for (var H = 0; H < _.totalNumber; H++) {
                    var B = document.getElementById(_.id + "|total" + H + "|" + E["__id"]);
                    $("div:first", B).html(_._getTotalCellContent(E, _.groups && _.groups[H] ? _.groups[H] : _.currentData[D.root]))
                }
            } else _._showData()
        },
        getData: function(B, A) {
            var _ = this,
                C = this.options,
                D = [];
            for (var F in _.records) {
                var E = $.extend(true, {}, _.records[F]);
                if (E[C.statusName] == B || B == undefined) D.push(_.formatRecord(E, A))
            }
            return D
        },
        formatRecord: function(_, $) {
            delete _["__id"];
            delete _["__previd"];
            delete _["__nextid"];
            delete _["__index"];
            if (this.options.tree) {
                delete _["__pid"];
                delete _["__level"];
                delete _["__hasChildren"]
            }
            if ($) delete _[this.options.statusName];
            return _
        },
        getUpdated: function() {
            return this.getData("update", true)
        },
        getDeleted: function() {
            return this.deletedRows
        },
        getAdded: function() {
            return this.getData("add", true)
        },
        getColumn: function($) {
            var _ = this,
                B = this.options;
            if (typeof $ == "string") {
                if (_._isColumnId($)) return _._columns[$];
                else return _.columns[parseInt($)]
            } else if (typeof($) == "number") return _.columns[$];
            else if (typeof $ == "object" && $.nodeType == 1) {
                var A = $.id.split("|"),
                    C = A[A.length - 1];
                return _._columns[C]
            }
            return $
        },
        getColumnType: function(_) {
            var $ = this,
                A = this.options;
            for (i = 0; i < $.columns.length; i++) if ($.columns[i].name == _) {
                if ($.columns[i].type) return $.columns[i].type;
                return "string"
            }
            return null
        },
        isTotalSummary: function() {
            var $ = this,
                _ = this.options;
            for (var A = 0; A < $.columns.length; A++) if ($.columns[A].totalSummary) return true;
            return false
        },
        getColumns: function(A) {
            var _ = this,
                C = this.options,
                D = [];
            for (var $ in _._columns) {
                var B = _._columns[$];
                if (A != undefined) {
                    if (B["__level"] == A) D.push(B)
                } else if (B["__leaf"]) D.push(B)
            }
            return D
        },
        changeSort: function(C, A) {
            var $ = this,
                B = this.options;
            if ($.loading) return true;
            if (B.dataAction == "local") {
                var _ = $.getColumnType(C);
                if (!$.sortedData) $.sortedData = $.filteredData;
                if (B.sortName == C) $.sortedData[B.root].reverse();
                else $.sortedData[B.root].sort(function(A, B) {
                    return $._compareData(A, B, C, _)
                });
                if (B.usePager) $.currentData = $._getCurrentPageData($.sortedData);
                else $.currentData = $.sortedData;
                $._showData()
            }
            B.sortName = C;
            B.sortOrder = A;
            if (B.dataAction == "server") $.loadData(B.where)
        },
        changePage: function(B) {
            var _ = this,
                A = this.options;
            if (_.loading) return true;
            if (A.dataAction != "local" && _.isDataChanged && !confirm(A.isContinueByDataChanged)) return false;
            A.pageCount = parseInt($(".pcontrol span", _.toolbar).html());
            switch (B) {
            case "first":
                if (A.page == 1) return;
                A.newPage = 1;
                break;
            case "prev":
                if (A.page == 1) return;
                if (A.page > 1) A.newPage = parseInt(A.page) - 1;
                break;
            case "next":
                if (A.page >= A.pageCount) return;
                A.newPage = parseInt(A.page) + 1;
                break;
            case "last":
                if (A.page >= A.pageCount) return;
                A.newPage = A.pageCount;
                break;
            case "input":
                var C = parseInt($(".pcontrol input", _.toolbar).val());
                if (isNaN(C)) C = 1;
                if (C < 1) C = 1;
                else if (C > A.pageCount) C = A.pageCount;
                $(".pcontrol input", _.toolbar).val(C);
                A.newPage = C;
                break
            }
            if (A.newPage == A.page) return false;
            if (A.newPage == 1) {
                $(".l-bar-btnfirst span", _.toolbar).addClass("l-disabled");
                $(".l-bar-btnprev span", _.toolbar).addClass("l-disabled")
            } else {
                $(".l-bar-btnfirst span", _.toolbar).removeClass("l-disabled");
                $(".l-bar-btnprev span", _.toolbar).removeClass("l-disabled")
            }
            if (A.newPage == A.pageCount) {
                $(".l-bar-btnlast span", _.toolbar).addClass("l-disabled");
                $(".l-bar-btnnext span", _.toolbar).addClass("l-disabled")
            } else {
                $(".l-bar-btnlast span", _.toolbar).removeClass("l-disabled");
                $(".l-bar-btnnext span", _.toolbar).removeClass("l-disabled")
            }
            _.trigger("changePage", [A.newPage]);
            if (A.dataAction == "server") _.loadData(A.where);
            else {
                _.currentData = _._getCurrentPageData(_.filteredData);
                _._showData()
            }
        },
        getSelectedRow: function() {
            for (var _ in this.selected) {
                var $ = this.selected[_];
                if ($["__id"] in this.records) return $
            }
            return null
        },
        getSelectedRows: function() {
            var $ = [];
            for (var A in this.selected) {
                var _ = this.selected[A];
                if (_["__id"] in this.records) $.push(_)
            }
            return $
        },
        getSelectedRowObj: function() {
            for (var _ in this.selected) {
                var $ = this.selected[_];
                if ($["__id"] in this.records) return this.getRowObj($)
            }
            return null
        },
        getSelectedRowObjs: function() {
            var $ = [];
            for (var A in this.selected) {
                var _ = this.selected[A];
                if (_["__id"] in this.records) $.push(this.getRowObj(_))
            }
            return $
        },
        getCellObj: function($, _) {
            var A = this.getRow($);
            _ = this.getColumn(_);
            return document.getElementById(this._getCellDomId(A, _))
        },
        getRowObj: function(_, B) {
            var $ = this,
                A = this.options;
            if (_ == null) return null;
            if (typeof(_) == "string") {
                if ($._isRowId(_)) return document.getElementById($.id + (B ? "|1|" : "|2|") + _);
                else return document.getElementById($.id + (B ? "|1|" : "|2|") + $.rows[parseInt(_)]["__id"])
            } else if (typeof(_) == "number") return document.getElementById($.id + (B ? "|1|" : "|2|") + $.rows[_]["__id"]);
            else if (typeof(_) == "object" && _["__id"]) return $.getRowObj(_["__id"], B);
            return _
        },
        getRow: function(_) {
            var $ = this,
                A = this.options;
            if (_ == null) return null;
            if (typeof(_) == "string") {
                if ($._isRowId(_)) return $.records[_];
                else return $.rows[parseInt(_)]
            } else if (typeof(_) == "number") return $.rows[parseInt(_)];
            else if (typeof(_) == "object" && _.nodeType == 1 && !_["__id"]) return $._getRowByDomId(_.id);
            return _
        },
        _setColumnVisible: function(C, A) {
            var _ = this,
                B = this.options;
            if (!A) {
                C._hide = false;
                document.getElementById(C["__domid"]).style.display = "";
                if (C["__pid"] != -1) {
                    var $ = _._columns[C["__pid"]];
                    if ($._hide) {
                        document.getElementById($["__domid"]).style.display = "";
                        this._setColumnVisible($, A)
                    }
                }
            } else {
                C._hide = true;
                document.getElementById(C["__domid"]).style.display = "none";
                if (C["__pid"] != -1) {
                    var D = true,
                        $ = this._columns[C["__pid"]];
                    for (var E = 0; $ && E < $.columns.length; E++) if (!$.columns[E]._hide) {
                        D = false;
                        break
                    }
                    if (D) {
                        $._hide = true;
                        document.getElementById($["__domid"]).style.display = "none";
                        this._setColumnVisible($, A)
                    }
                }
            }
        },
        toggleCol: function(_, E, A) {
            var B = this,
                I = this.options,
                J;
            if (typeof(_) == "number") J = B.columns[_];
            else if (typeof(_) == "object" && _["__id"]) J = _;
            else if (typeof(_) == "string") if (B._isColumnId(_)) J = B._columns[_];
            else {
                $(B.columns).each(function() {
                    if (this.name == _) B.toggleCol(this, E, A)
                });
                return
            }
            if (!J) return;
            var L = J["__leafindex"],
                G = document.getElementById(J["__domid"]);
            if (!G) return;
            G = $(G);
            var C = [];
            for (var H in B.rows) {
                var K = B.getCellObj(B.rows[H], J);
                if (K) C.push(K)
            }
            for (H = 0; H < B.totalNumber; H++) {
                var F = document.getElementById(B.id + "|total" + H + "|" + J["__id"]);
                if (F) C.push(F)
            }
            var D = J._width;
            if (E && J._hide) {
                if (J.frozen) B.f.gridtablewidth += (parseInt(D) + 1);
                else B.gridtablewidth += (parseInt(D) + 1);
                B._setColumnVisible(J, false);
                $(C).show()
            } else if (!E && !J._hide) {
                if (J.frozen) B.f.gridtablewidth -= (parseInt(D) + 1);
                else B.gridtablewidth -= (parseInt(D) + 1);
                B._setColumnVisible(J, true);
                $(C).hide()
            }
            if (J.frozen) {
                $("div:first", B.f.gridheader).width(B.f.gridtablewidth);
                $("div:first", B.f.gridbody).width(B.f.gridtablewidth)
            } else {
                $("div:first", B.gridheader).width(B.gridtablewidth + 40);
                $("div:first", B.gridbody).width(B.gridtablewidth)
            }
            B._updateFrozenWidth();
            if (!A) $(":checkbox[columnindex=" + L + "]", B.popup).each(function() {
                this.checked = E;
                if ($.fn.ligerCheckBox) {
                    var _ = $(this).ligerGetCheckBoxManager();
                    if (_) _.updateStyle()
                }
            })
        },
        setColumnWidth: function(_, G) {
            var A = this,
                I = this.options;
            if (!G) return;
            G = parseInt(G, 10);
            var J;
            if (typeof(_) == "number") J = A.columns[_];
            else if (typeof(_) == "object" && _["__id"]) J = _;
            else if (typeof(_) == "string") if (A._isColumnId(_)) J = A._columns[_];
            else {
                $(A.columns).each(function() {
                    if (this.name == _) A.setColumnWidth(this, G)
                });
                return
            }
            if (!J) return;
            var E = I.minColumnWidth;
            if (J.minWidth) E = J.minWidth;
            G = G < E ? E : G;
            var C = G - J._width;
            if (A.trigger("beforeChangeColumnWidth", [J, G]) == false) return;
            J._width = G;
            if (J.frozen) {
                A.f.gridtablewidth += C;
                $("div:first", A.f.gridheader).width(A.f.gridtablewidth);
                $("div:first", A.f.gridbody).width(A.f.gridtablewidth)
            } else {
                A.gridtablewidth += C;
                $("div:first", A.gridheader).width(A.gridtablewidth + 40);
                $("div:first", A.gridbody).width(A.gridtablewidth)
            }
            $(document.getElementById(J["__domid"])).css("width", G);
            var B = [];
            for (var L in A.records) {
                var K = A.getCellObj(A.records[L], J);
                if (K) B.push(K);
                if (!A.enabledDetailEdit() && A.editors[L] && A.editors[L][J["__id"]]) {
                    var F = A.editors[L][J["__id"]];
                    if (F.editor.resize) F.editor.resize(F.input, G, F.container.height(), F.editParm)
                }
            }
            for (var H = 0; H < A.totalNumber; H++) {
                var D = document.getElementById(A.id + "|total" + H + "|" + J["__id"]);
                if (D) B.push(D)
            }
            $(B).css("width", G).find("> div.l-grid-row-cell-inner:first").css("width", G - 8);
            A._updateFrozenWidth();
            A.trigger("afterChangeColumnWidth", [J, G])
        },
        changeHeaderText: function(A, F) {
            var _ = this,
                B = this.options,
                C;
            if (typeof(A) == "number") C = _.columns[A];
            else if (typeof(A) == "object" && A["__id"]) C = A;
            else if (typeof(A) == "string") if (_._isColumnId(A)) C = _._columns[A];
            else {
                $(_.columns).each(function() {
                    if (this.name == A) _.changeHeaderText(this, F)
                });
                return
            }
            if (!C) return;
            var E = C["__leafindex"],
                D = document.getElementById(C["__domid"]);
            $(".l-grid-hd-cell-text", D).html(F);
            if (B.allowHideColumn) $(":checkbox[columnindex=" + E + "]", _.popup).parent().next().html(F)
        },
        changeCol: function(E, _, G) {
            var A = this,
                K = this.options;
            if (!E || !_) return;
            var H = A.getColumn(E),
                J = A.getColumn(_);
            H.frozen = J.frozen;
            var B, D, L = H["__pid"] == -1 ? K.columns : A._columns[H["__pid"]].columns,
                F = J["__pid"] == -1 ? K.columns : A._columns[J["__pid"]].columns;
            B = $.inArray(H, L);
            D = $.inArray(J, F);
            var C = L == F,
                I = H["__level"] == J["__level"];
            F.splice(D + (G ? 1 : 0), 0, H);
            if (!C) L.splice(B, 1);
            else if (G) L.splice(B, 1);
            else L.splice(B + 1, 1);
            A._setColumns(K.columns);
            A.reRender()
        },
        collapseDetail: function(A) {
            var _ = this,
                C = this.options,
                E = _.getRow(A);
            if (!E) return;
            for (var G = 0, D = _.columns.length; G < D; G++) if (_.columns[G].isdetail) {
                var F = _.getRowObj(E),
                    B = _.getCellObj(E, _.columns[G]);
                $(F).next("tr.l-grid-detailpanel").hide();
                $(".l-grid-row-cell-detailbtn:first", B).removeClass("l-open");
                _.trigger("SysGridHeightChanged");
                return
            }
        },
        extendDetail: function(A) {
            var _ = this,
                C = this.options,
                E = _.getRow(A);
            if (!E) return;
            for (var G = 0, D = _.columns; G < D; G++) if (_.columns[G].isdetail) {
                var F = _.getRowObj(E),
                    B = _.getCellObj(E, _.columns[G]);
                $(F).next("tr.l-grid-detailpanel").show();
                $(".l-grid-row-cell-detailbtn:first", B).addClass("l-open");
                _.trigger("SysGridHeightChanged");
                return
            }
        },
        getParent: function(_) {
            var $ = this,
                A = this.options;
            if (!A.tree) return null;
            var B = $.getRow(_);
            if (!B) return null;
            if (B["__pid"] in $.records) return $.records[B["__pid"]];
            else return null
        },
        getChildren: function(A, E) {
            var $ = this,
                B = this.options;
            if (!B.tree) return null;
            var C = $.getRow(A);
            if (!C) return null;
            var _ = [];

            function D($) {
                if ($[B.tree.childrenName]) for (var F = 0, C = $[B.tree.childrenName].length; F < C; F++) {
                    var A = $[B.tree.childrenName][F];
                    if (A["__status"] == "delete") continue;
                    _.push(A);
                    if (E) D(A)
                }
            }
            D(C);
            return _
        },
        isLeaf: function(_) {
            var $ = this,
                A = this.options,
                B = $.getRow(_);
            if (!B) return;
            return B["__hasChildren"] ? false : true
        },
        hasChildren: function(_) {
            var $ = this,
                A = this.options,
                B = this.getRow(_);
            if (!B) return;
            return (B[A.tree.childrenName] && B[A.tree.childrenName].length) ? true : false
        },
        existRecord: function($) {
            for (var _ in this.records) if (this.records[_] == $) return true;
            return false
        },
        _removeSelected: function(F) {
            var _ = this,
                B = this.options;
            if (B.tree) {
                var C = _.getChildren(F, true);
                if (C) for (var G = 0, E = C.length; G < E; G++) {
                    var D = $.inArray(C[G], _.selected);
                    if (D != -1) _.selected.splice(D, 1)
                }
            }
            var A = $.inArray(F, _.selected);
            if (A != -1) _.selected.splice(A, 1)
        },
        _getParentChildren: function(_) {
            var $ = this,
                A = this.options,
                B = $.getRow(_),
                C;
            if (A.tree && $.existRecord(B) && B["__pid"] in $.records) C = $.records[B["__pid"]][A.tree.childrenName];
            else C = $.currentData[A.root];
            return C
        },
        _removeData: function(C) {
            var _ = this,
                B = this.options,
                D = _._getParentChildren(C),
                A = $.inArray(C, D);
            if (A != -1) D.splice(A, 1);
            _._removeSelected(C)
        },
        _addData: function(F, D, E, B) {
            var _ = this,
                C = this.options,
                G = _.currentData[C.root];
            if (E) {
                if (C.tree) if (D) G = D[C.tree.childrenName];
                else if (E["__pid"] in _.records) G = _.records[E["__pid"]][C.tree.childrenName];
                var A = $.inArray(E, G);
                G.splice(A == -1 ? -1 : A + (B ? 0 : 1), 0, F)
            } else {
                if (C.tree && D) G = D[C.tree.childrenName];
                G.push(F)
            }
        },
        _appendData: function(D, B, C, _) {
            var $ = this,
                A = this.options;
            D[A.statusName] = "update";
            $._removeData(D);
            $._addData(D, B, C, _)
        },
        appendRange: function(F, C, D, A) {
            var _ = this,
                B = this.options,
                E = false;
            $.each(F, function(B, $) {
                if ($["__id"] && _.existRecord($)) {
                    if (_.isLeaf(C)) _.upgrade(C);
                    _._appendData($, C, D, A);
                    E = true
                } else _.appendRow($, C, D, A)
            });
            if (E) _.reRender()
        },
        appendRow: function(E, C, D, A) {
            var _ = this,
                B = this.options;
            if ($.isArray(E)) {
                _.appendRange(E, C, D, A);
                return
            }
            if (E["__id"] && _.existRecord(E)) {
                _._appendData(E, C, D, A);
                _.reRender();
                return
            }
            if (C && _.isLeaf(C)) _.upgrade(C);
            _.addRow(E, D, A ? true : false, C)
        },
        upgrade: function(A) {
            var _ = this,
                B = this.options,
                C = _.getRow(A);
            if (!C || !B.tree) return;
            C[B.tree.childrenName] = C[B.tree.childrenName] || [];
            C["__hasChildren"] = true;
            var D = [_.getRowObj(C)];
            if (_.enabledFrozen()) D.push(_.getRowObj(C, true));
            $("> td > div > .l-grid-tree-space:last", D).addClass("l-grid-tree-link l-grid-tree-link-open")
        },
        demotion: function(A) {
            var _ = this,
                B = this.options,
                E = _.getRow(A);
            if (!E || !B.tree) return;
            var F = [_.getRowObj(E)];
            if (_.enabledFrozen()) F.push(_.getRowObj(E, true));
            $("> td > div > .l-grid-tree-space:last", F).removeClass("l-grid-tree-link l-grid-tree-link-open l-grid-tree-link-close");
            if (_.hasChildren(E)) {
                var C = _.getChildren(E);
                for (var G = 0, D = C.length; G < D; G++) _.deleteRow(C[G])
            }
            E["__hasChildren"] = false
        },
        collapse: function(B) {
            var _ = this,
                C = this.options,
                D = _.getRowObj(B),
                A = $(".l-grid-tree-link", D);
            if (A.hasClass("l-grid-tree-link-close")) return;
            _.toggle(B)
        },
        expand: function(B) {
            var _ = this,
                C = this.options,
                D = _.getRowObj(B),
                A = $(".l-grid-tree-link", D);
            if (A.hasClass("l-grid-tree-link-open")) return;
            _.toggle(B)
        },
        toggle: function(G) {
            if (!G) return;
            var _ = this,
                I = this.options,
                L = _.getRow(G),
                J = [_.getRowObj(L)];
            if (_.enabledFrozen()) J.push(_.getRowObj(L, true));
            var F = L["__level"],
                M, H = $(".l-grid-tree-link:first", J),
                K = true;
            _.collapsedRows = _.collapsedRows || [];
            if (H.hasClass("l-grid-tree-link-close")) {
                H.removeClass("l-grid-tree-link-close").addClass("l-grid-tree-link-open");
                M = $.inArray(L, _.collapsedRows);
                if (M != -1) _.collapsedRows.splice(M, 1)
            } else {
                K = false;
                H.addClass("l-grid-tree-link-close").removeClass("l-grid-tree-link-open");
                M = $.inArray(L, _.collapsedRows);
                if (M == -1) _.collapsedRows.push(L)
            }
            var B = _.getChildren(L, true);
            for (var E = 0, D = B.length; E < D; E++) {
                var A = B[E],
                    C = $([_.getRowObj(A["__id"])]);
                if (_.enabledFrozen()) C = C.add(_.getRowObj(A["__id"], true));
                if (K) {
                    $(".l-grid-tree-link", C).removeClass("l-grid-tree-link-close").addClass("l-grid-tree-link-open");
                    C.show()
                } else {
                    $(".l-grid-tree-link", C).removeClass("l-grid-tree-link-open").addClass("l-grid-tree-link-close");
                    C.hide()
                }
            }
        },
        _bulid: function() {
            var $ = this;
            $._clearGrid();
            $._initBuildHeader();
            $._initHeight();
            $._initFootbar();
            $._buildPager();
            $._setEvent()
        },
        _setColumns: function(_) {
            var $ = this;
            $._initColumns();
            $._initBuildGridHeader();
            $._initBuildPopup()
        },
        _initBuildHeader: function() {
            var _ = this,
                A = this.options;
            if (A.title) {
                $(".l-panel-header-text", _.header).html(A.title);
                if (A.headerImg) _.header.append("<img src='" + A.headerImg + "' />").addClass("l-panel-header-hasicon")
            } else _.header.hide();
            if (A.toolbar) {
                if ($.fn.ligerToolBar) _.toolbarManager = _.topbar.ligerToolBar(A.toolbar)
            } else _.topbar.remove()
        },
        _createColumnId: function($) {
            if ($.id != null) return $.id.toString();
            return "c" + (100 + this._columnCount)
        },
        _isColumnId: function($) {
            return ($ in this._columns)
        },
        _initColumns: function() {
            var A = this,
                I = this.options;
            A._columns = {};
            A._columnCount = 0;
            A._columnLeafCount = 0;
            A._columnMaxLevel = 1;
            if (!I.columns) return;

            function B($, _) {
                for (var A in _) if (_[A] in $) delete $[_[A]]
            }
            function K(E, C, G, D) {
                B(E, ["__id", "__pid", "__previd", "__nextid", "__domid", "__leaf", "__leafindex", "__level", "__colSpan", "__rowSpan"]);
                if (C > A._columnMaxLevel) A._columnMaxLevel = C;
                A._columnCount++;
                E["__id"] = A._createColumnId(E);
                E["__domid"] = A.id + "|hcell|" + E["__id"];
                A._columns[E["__id"]] = E;
                if (!E.columns || !E.columns.length) E["__leafindex"] = A._columnLeafCount++;
                E["__level"] = C;
                E["__pid"] = G;
                E["__previd"] = D;
                if (!E.columns || !E.columns.length) {
                    E["__leaf"] = true;
                    return 1
                }
                var $ = 0,
                    H = -1;
                for (var I = 0, F = E.columns.length; I < F; I++) {
                    var _ = E.columns[I];
                    $ += K(_, C + 1, E["__id"], H);
                    H = _["__id"]
                }
                E["__leafcount"] = $;
                return $
            }
            var D = -1;
            if (I.rownumbers) {
                var J = A.enabledGroup() ? false : I.frozen && I.frozenRownumbers,
                    C = {
                        isrownumber: true,
                        issystem: true,
                        width: I.rownumbersColWidth,
                        frozen: J
                    };
                K(C, 1, -1, D);
                D = C["__id"]
            }
            if (A.enabledDetail()) {
                var E = A.enabledGroup() ? false : I.frozen && I.frozenDetail,
                    C = {
                        isdetail: true,
                        issystem: true,
                        width: I.detailColWidth,
                        frozen: E
                    };
                K(C, 1, -1, D);
                D = C["__id"]
            }
            if (A.enabledCheckbox()) {
                var _ = A.enabledGroup() ? false : I.frozen && I.frozenCheckbox,
                    C = {
                        ischeckbox: true,
                        issystem: true,
                        width: I.detailColWidth,
                        frozen: _
                    };
                K(C, 1, -1, D);
                D = C["__id"]
            }
            for (var G = 0, F = I.columns.length; G < F; G++) {
                C = I.columns[G];
                K(C, 1, -1, D);
                D = C["__id"]
            }
            for (var H in A._columns) {
                C = A._columns[H];
                if (C["__leafcount"] > 1) C["__colSpan"] = C["__leafcount"];
                if (C["__leaf"] && C["__level"] != A._columnMaxLevel) C["__rowSpan"] = A._columnMaxLevel - C["__level"] + 1
            }
            A.columns = A.getColumns();
            $(A.columns).each(function(_, $) {
                $.columnname = $.name;
                $.columnindex = _;
                $.type = $.type || "string";
                $.islast = _ == A.columns.length - 1;
                $.isSort = $.isSort == false ? false : true;
                $.frozen = $.frozen ? true : false;
                $._width = A._getColumnWidth($);
                $._hide = $.hide ? true : false
            })
        },
        _getColumnWidth: function(B) {
            var $ = this,
                A = this.options;
            if (B._width) return B._width;
            var _;
            if (B.width) _ = B.width;
            else if (A.columnWidth) _ = A.columnWidth;
            if (!_) {
                var C = 4;
                if ($.enabledCheckbox()) C += A.checkboxColWidth;
                if ($.enabledDetail()) C += A.detailColWidth;
                _ = parseInt(($.grid.width() - C) / $.columns.length)
            }
            if (typeof(_) == "string" && _.indexOf("%") > 0) B._width = _ = parseInt(parseInt(_) * 0.01 * ($.grid.width() - $.columns.length));
            if (B.minWidth && _ < B.minWidth) _ = B.minWidth;
            if (B.maxWidth && _ > B.maxWidth) _ = B.maxWidth;
            B._width = _;
            return _
        },
        _createHeaderCell: function(B) {
            var _ = this,
                A = this.options,
                D = $("<td class='l-grid-hd-cell'><div class='l-grid-hd-cell-inner'><span class='l-grid-hd-cell-text'></span></div></td>");
            D.attr("id", B["__domid"]);
            if (!B["__leaf"]) D.addClass("l-grid-hd-cell-mul");
            if (B.columnindex == _.columns.length - 1) D.addClass("l-grid-hd-cell-last");
            if (B.isrownumber) {
                D.addClass("l-grid-hd-cell-rownumbers");
                D.html("<div class='l-grid-hd-cell-inner'></div>")
            }
            if (B.ischeckbox) {
                D.addClass("l-grid-hd-cell-checkbox");
                D.html("<div class='l-grid-hd-cell-inner'><div class='l-grid-hd-cell-text l-grid-hd-cell-btn-checkbox'></div></div>")
            }
            if (B.isdetail) {
                D.addClass("l-grid-hd-cell-detail");
                D.html("<div class='l-grid-hd-cell-inner'><div class='l-grid-hd-cell-text l-grid-hd-cell-btn-detail'></div></div>")
            }
            if (B.heightAlign) $(".l-grid-hd-cell-inner:first", D).css("textAlign", B.heightAlign);
            if (B["__colSpan"]) D.attr("colSpan", B["__colSpan"]);
            if (B["__rowSpan"]) {
                D.attr("rowSpan", B["__rowSpan"]);
                D.height(A.headerRowHeight * B["__rowSpan"])
            } else D.height(A.headerRowHeight);
            if (B["__leaf"]) {
                D.width(B["_width"]);
                D.attr("columnindex", B["__leafindex"])
            }
            if (B._hide) D.hide();
            if (B.name) D.attr({
                columnname: B.name
            });
            var C = "";
            if (B.display && B.display != "") C = B.display;
            else if (B.headerRender) C = B.headerRender(B);
            else C = "&nbsp;";
            $(".l-grid-hd-cell-text:first", D).html(C);
            if (!B.issystem && B["__leaf"] && B.resizable !== false && $.fn.ligerResizable) _.colResizable[B["__id"]] = D.ligerResizable({
                handles: "e",
                onStartResize: function(A, $) {
                    this.proxy.hide();
                    _.draggingline.css({
                        height: _.body.height(),
                        top: 0,
                        left: $.pageX - _.grid.offset().left + parseInt(_.body[0].scrollLeft)
                    }).show()
                },
                onResize: function(B, A) {
                    _.colresizing = true;
                    _.draggingline.css({
                        left: A.pageX - _.grid.offset().left + parseInt(_.body[0].scrollLeft)
                    });
                    $("body").add(D).css("cursor", "e-resize")
                },
                onStopResize: function(A) {
                    _.colresizing = false;
                    $("body").add(D).css("cursor", "default");
                    _.draggingline.hide();
                    _.setColumnWidth(B, B._width + A.diffX);
                    return false
                }
            });
            return D
        },
        _initBuildGridHeader: function() {
            var _ = this,
                D = this.options;
            _.gridtablewidth = 0;
            _.f.gridtablewidth = 0;
            if (_.colResizable) {
                for (var H in _.colResizable) _.colResizable[H].destroy();
                _.colResizable = null
            }
            _.colResizable = {};
            $("tbody:first", _.gridheader).html("");
            $("tbody:first", _.f.gridheader).html("");
            for (var A = 1; A <= _._columnMaxLevel; A++) {
                var E = _.getColumns(A),
                    C = A == _._columnMaxLevel,
                    B = $("<tr class='l-grid-hd-row'></tr>"),
                    F = $("<tr class='l-grid-hd-row'></tr>");
                if (!C) B.add(F).addClass("l-grid-hd-mul");
                $("tbody:first", _.gridheader).append(B);
                $("tbody:first", _.f.gridheader).append(F);
                $(E).each(function(C, A) {
                    (A.frozen ? F : B).append(_._createHeaderCell(A));
                    if (A["__leaf"]) {
                        var $ = A["_width"];
                        if (!A.frozen) _.gridtablewidth += (parseInt($) ? parseInt($) : 0) + 1;
                        else _.f.gridtablewidth += (parseInt($) ? parseInt($) : 0) + 1
                    }
                })
            }
            if (_._columnMaxLevel > 0) {
                var G = D.headerRowHeight * _._columnMaxLevel;
                _.gridheader.add(_.f.gridheader).height(G);
                if (D.rownumbers && D.frozenRownumbers) _.f.gridheader.find("td:first").height(G)
            }
            _._updateFrozenWidth();
            $("div:first", _.gridheader).width(_.gridtablewidth + 40)
        },
        _initBuildPopup: function() {
            var _ = this,
                A = this.options;
            $(":checkbox", _.popup).unbind();
            $("tbody tr", _.popup).remove();
            $(_.columns).each(function(D, B) {
                if (B.issystem) return;
                if (B.isAllowHide == false) return;
                var A = "checked=\"checked\"";
                if (B._hide) A = "";
                var C = B.display;
                $("tbody", _.popup).append("<tr><td class=\"l-column-left\"><input type=\"checkbox\" " + A + " class=\"l-checkbox\" columnindex=\"" + D + "\"/></td><td class=\"l-column-right\">" + C + "</td></tr>")
            });
            if ($.fn.ligerCheckBox) $("input:checkbox", _.popup).ligerCheckBox({
                onBeforeClick: function(B) {
                    if (!B.checked) return true;
                    if ($("input:checked", _.popup).length <= A.minColToggle) return false;
                    return true
                }
            });
            if (A.allowHideColumn) {
                $("tr", _.popup).hover(function() {
                    $(this).addClass("l-popup-row-over")
                }, function() {
                    $(this).removeClass("l-popup-row-over")
                });
                var B = function() {
                        if ($("input:checked", _.popup).length + 1 <= A.minColToggle) return false;
                        _.toggleCol(parseInt($(this).attr("columnindex")), this.checked, true)
                    };
                if ($.fn.ligerCheckBox) $(":checkbox", _.popup).bind("change", B);
                else $(":checkbox", _.popup).bind("click", B)
            }
        },
        _initHeight: function() {
            var $ = this,
                _ = this.options;
            if (_.height == "auto") {
                $.gridbody.height("auto");
                $.f.gridbody.height("auto")
            }
            if (_.width) $.grid.width(_.width);
            $._onResize.call($)
        },
        _initFootbar: function() {
            var _ = this,
                C = this.options;
            if (C.usePager) {
                var B = "",
                    A = -1;
                $(C.pageSizeOptions).each(function(D, $) {
                    var _ = "";
                    if (C.pageSize == $) A = D;
                    B += "<option value='" + $ + "' " + _ + " >" + $ + "</option>"
                });
                $(".l-bar-selectpagesize", _.toolbar).append("<select name='rp'>" + B + "</select>");
                if (A != -1) $(".l-bar-selectpagesize select", _.toolbar)[0].selectedIndex = A;
                if (C.switchPageSizeApplyComboBox && $.fn.ligerComboBox) $(".l-bar-selectpagesize select", _.toolbar).ligerComboBox({
                    onBeforeSelect: function() {
                        if (C.url && _.isDataChanged && !confirm(C.isContinueByDataChanged)) return false;
                        return true
                    },
                    width: 45
                })
            } else _.toolbar.hide()
        },
        _searchData: function(B, D) {
            var _ = this,
                A = this.options,
                $ = new Array();
            for (var C = 0; C < B.length; C++) if (D(B[C], C)) $[$.length] = B[C];
            return $
        },
        _clearGrid: function() {
            var _ = this,
                B = this.options;
            for (var C in _.rows) {
                var A = $(_.getRowObj(_.rows[C]));
                if (_.enabledFrozen()) A = A.add(_.getRowObj(_.rows[C], true));
                A.unbind()
            }
            _.gridbody.html("");
            _.f.gridbody.html("");
            _.recordNumber = 0;
            _.records = {};
            _.rows = [];
            _.selected = [];
            _.totalNumber = 0;
            _.editorcounter = 0
        },
        _fillGridBody: function(G, I) {
            var _ = this,
                F = this.options,
                H = ["<div class=\"l-grid-body-inner\"><table class=\"l-grid-body-table\" cellpadding=0 cellspacing=0><tbody>"];
            if (_.enabledGroup()) {
                var J = [],
                    A = [];
                _.groups = A;
                for (var C in G) {
                    var E = G[C],
                        B = E[F.groupColumnName],
                        D = $.inArray(B, J);
                    if (D == -1) {
                        J.push(B);
                        D = J.length - 1;
                        A.push([])
                    }
                    A[D].push(E)
                }
                $(A).each(function(B, $) {
                    if (A.length == 1) H.push("<tr class=\"l-grid-grouprow l-grid-grouprow-last l-grid-grouprow-first\"");
                    if (B == A.length - 1) H.push("<tr class=\"l-grid-grouprow l-grid-grouprow-last\"");
                    else if (B == 0) H.push("<tr class=\"l-grid-grouprow l-grid-grouprow-first\"");
                    else H.push("<tr class=\"l-grid-grouprow\"");
                    H.push(" groupindex\"=" + B + "\" >");
                    H.push("<td colSpan=\"" + _.columns.length + "\" class=\"l-grid-grouprow-cell\">");
                    H.push("<span class=\"l-grid-group-togglebtn\">&nbsp;&nbsp;&nbsp;&nbsp;</span>");
                    if (F.groupRender) H.push(F.groupRender(J[B], $, F.groupColumnDisplay));
                    else H.push(F.groupColumnDisplay + ":" + J[B]);
                    H.push("</td>");
                    H.push("</tr>");
                    H.push(_._getHtmlFromData($, I));
                    if (_.isTotalSummary()) H.push(_._getTotalSummaryHtml($, "l-grid-totalsummary-group", I))
                })
            } else H.push(_._getHtmlFromData(G, I));
            H.push("</tbody></table></div>");
            (I ? _.f.gridbody : _.gridbody).html(H.join(""));
            if (!_.enabledGroup()) _._bulidTotalSummary(I);
            $("> div:first", _.gridbody).width(_.gridtablewidth);
            _._onResize()
        },
        _showData: function() {
            var _ = this,
                B = this.options,
                C = _.currentData[B.root];
            if (B.usePager) {
                if (B.dataAction == "server" && _.data && _.data[B.record]) B.total = _.data[B.record];
                else if (_.filteredData && _.filteredData[B.root]) B.total = _.filteredData[B.root].length;
                else if (_.data && _.data[B.root]) B.total = _.data[B.root].length;
                else if (C) B.total = C.length;
                B.page = B.newPage;
                if (!B.total) B.total = 0;
                if (!B.page) B.page = 1;
                B.pageCount = Math.ceil(B.total / B.pageSize);
                if (!B.pageCount) B.pageCount = 1;
                _._buildPager()
            }
            $(".l-bar-btnloading:first", _.toolbar).removeClass("l-bar-btnloading");
            if (_.trigger("beforeShowData", [_.currentData]) == false) return;
            _._clearGrid();
            _.isDataChanged = false;
            if (!C) return;
            $(".l-bar-btnload:first span", _.toolbar).removeClass("l-disabled");
            _._updateGridData();
            if (_.enabledFrozen()) _._fillGridBody(_.rows, true);
            _._fillGridBody(_.rows, false);
            _.trigger("SysGridHeightChanged");
            if (B.totalRender) {
                $(".l-panel-bar-total", _.element).remove();
                $(".l-panel-bar", _.element).before("<div class=\"l-panel-bar-total\">" + B.totalRender(_.data, _.filteredData) + "</div>")
            }
            if (B.mouseoverRowCssClass) for (var D in _.rows) {
                var A = $(_.getRowObj(_.rows[D]));
                if (_.enabledFrozen()) A = A.add(_.getRowObj(_.rows[D], true));
                A.bind("mouseover.gridrow", function() {
                    _._onRowOver(this, true)
                }).bind("mouseout.gridrow", function() {
                    _._onRowOver(this, false)
                })
            }
            _.gridbody.trigger("scroll.grid");
            _.trigger("afterShowData", [_.currentData])
        },
        _getRowDomId: function(_, $) {
            return this.id + "|" + ($ ? "1" : "2") + "|" + _["__id"]
        },
        _getCellDomId: function(_, $) {
            return this._getRowDomId(_, $.frozen) + "|" + $["__id"]
        },
        _getHtmlFromData: function(D, F) {
            if (!D) return "";
            var _ = this,
                C = this.options,
                E = [];
            for (var A in D) {
                var B = D[A],
                    G = B["__id"];
                if (!B) continue;
                E.push("<tr");
                E.push(" id=\"" + _._getRowDomId(B, F) + "\"");
                E.push(" class=\"l-grid-row");
                if (!F && _.enabledCheckbox() && C.isChecked && C.isChecked(B)) {
                    _.select(B);
                    E.push(" l-selected")
                } else if (_.isSelected(B)) E.push(" l-selected");
                if (B["__index"] % 2 == 1 && C.alternatingRow) E.push(" l-grid-row-alt");
                E.push("\" ");
                if (C.rowAttrRender) E.push(C.rowAttrRender(B, G));
                if (C.tree && _.collapsedRows && _.collapsedRows.length) {
                    var H = function() {
                            var A = _.getParent(B);
                            while (A) {
                                if ($.inArray(A, _.collapsedRows) != -1) return true;
                                A = _.getParent(A)
                            }
                            return false
                        };
                    if (H()) E.push(" style=\"display:none;\" ")
                }
                E.push(">");
                $(_.columns).each(function(D, A) {
                    if (F != A.frozen) return;
                    E.push("<td");
                    E.push(" id=\"" + _._getCellDomId(B, this) + "\"");
                    if (this.isrownumber) {
                        E.push(" class=\"l-grid-row-cell l-grid-row-cell-rownumbers\" style=\"width:" + this.width + "px\"><div class=\"l-grid-row-cell-inner\"");
                        if (C.fixedCellHeight) E.push(" style = \"height:" + C.rowHeight + "px;\" ");
                        E.push(">" + (parseInt(B["__index"]) + 1) + "</div></td>");
                        return
                    }
                    if (this.ischeckbox) {
                        E.push(" class=\"l-grid-row-cell l-grid-row-cell-checkbox\" style=\"width:" + this.width + "px\"><div class=\"l-grid-row-cell-inner\"");
                        if (C.fixedCellHeight) E.push(" style = \"height:" + C.rowHeight + "px;\" ");
                        E.push("><span class=\"l-grid-row-cell-btn-checkbox\"></span></div></td>");
                        return
                    } else if (this.isdetail) {
                        E.push(" class=\"l-grid-row-cell l-grid-row-cell-detail\" style=\"width:" + this.width + "px\"><div class=\"l-grid-row-cell-inner\"");
                        if (C.fixedCellHeight) E.push(" style = \"height:" + C.rowHeight + "px;\" ");
                        E.push("><span class=\"l-grid-row-cell-detailbtn\"></span></div></td>");
                        return
                    }
                    var $ = this._width;
                    E.push(" class=\"l-grid-row-cell ");
                    if (_.changedCells[G + "_" + this["__id"]]) E.push("l-grid-row-cell-edited ");
                    if (this.islast) E.push("l-grid-row-cell-last ");
                    E.push("\"");
                    E.push(" style = \"");
                    E.push("width:" + $ + "px; ");
                    if (A._hide) E.push("display:none;");
                    E.push(" \">");
                    E.push(_._getCellHtml(B, A));
                    E.push("</td>")
                });
                E.push("</tr>")
            }
            return E.join("")
        },
        _getCellHtml: function(D, B) {
            var _ = this,
                A = this.options;
            if (B.isrownumber) return "<div class=\"l-grid-row-cell-inner\">" + (parseInt(D["__index"]) + 1) + "</div>";
            var C = [];
            C.push("<div class=\"l-grid-row-cell-inner\"");
            C.push(" style = \"width:" + parseInt(B._width - 8) + "px;");
            if (A.fixedCellHeight) C.push("height:" + A.rowHeight + "px;min-height:" + A.rowHeight + "px; ");
            if (B.align) C.push("text-align:" + B.align + ";");
            var $ = _._getCellContent(D, B);
            C.push("\">" + $ + "</div>");
            return C.join("")
        },
        _getCellContent: function(D, C) {
            if (!D || !C) return "";
            if (C.isrownumber) return parseInt(D["__index"]) + 1;
            var F = D["__id"],
                E = D["__index"],
                B = C.name ? D[C.name] : null,
                _ = this,
                A = this.options,
                $ = "";
            if (C.render) $ = C.render.call(_, D, E, B, C);
            else if (A.formatters[C.type]) $ = A.formatters[C.type].call(_, B, C);
            else if (B != null) $ = B.toString();
            if (A.tree && (A.tree.columnName != null && A.tree.columnName == C.name || A.tree.columnId != null && A.tree.columnId == C.id)) $ = _._getTreeCellHtml($, D);
            return $ || ""
        },
        _getTreeCellHtml: function(D, G) {
            var B = G["__level"],
                A = this,
                C = this.options,
                E = $.inArray(G, A.collapsedRows || []) == -1,
                F = C.tree.isParent(G),
                _ = "";
            B = parseInt(B) || 1;
            for (var H = 1; H < B; H++) _ += "<div class='l-grid-tree-space'></div>";
            if (E && F) _ += "<div class='l-grid-tree-space l-grid-tree-link l-grid-tree-link-open'></div>";
            else if (F) _ += "<div class='l-grid-tree-space l-grid-tree-link l-grid-tree-link-close'></div>";
            else _ += "<div class='l-grid-tree-space'></div>";
            _ += "<span class='l-grid-tree-content'>" + D + "</span>";
            return _
        },
        _applyEditor: function(Q) {
            var A = this,
                O = this.options,
                B = Q,
                D = B.id.split("|"),
                G = D[D.length - 1],
                P = A._columns[G],
                I = $(B).parent(),
                T = A.getRow(I[0]),
                U = T["__id"],
                H = T["__index"];
            if (!P || !P.editor) return;
            var E = P.name,
                S = P.columnindex;
            if (P.editor.type && O.editors[P.editor.type]) {
                var K = T[E],
                    _ = {
                        record: T,
                        value: K,
                        column: P,
                        rowindex: H
                    };
                if (A.trigger("beforeEdit", [_]) == false) return false;
                var L = O.editors[P.editor.type],
                    J = $(B),
                    R = $(B).offset();
                J.html("");
                A.setCellEditing(T, P, true);
                var F = $(B).width(),
                    M = $(B).height(),
                    N = $("<div class='l-grid-editor'></div>").appendTo("body");
                if ($.browser.mozilla) N.css({
                    left: R.left,
                    top: R.top
                }).show();
                else N.css({
                    left: R.left + 1,
                    top: R.top + 1
                }).show();
                var C = A._createEditor(L, N, _, F, M);
                A.editor = {
                    editing: true,
                    editor: L,
                    input: C,
                    editParm: _,
                    container: N
                };
                A.unbind("sysEndEdit");
                A.bind("sysEndEdit", function() {
                    var D = L.getValue(C, _);
                    if (D != K) {
                        $(B).addClass("l-grid-row-cell-edited");
                        A.changedCells[U + "_" + P["__id"]] = true;
                        if (P.editor.onChange) P.editor.onChange(B, D);
                        _.value = D;
                        if (A._checkEditAndUpdateCell(_)) if (P.editor.onChanged) P.editor.onChanged(B, D)
                    }
                })
            }
        },
        _checkEditAndUpdateCell: function(_) {
            var $ = this,
                A = this.options;
            if ($.trigger("beforeSubmitEdit", [_]) == false) return false;
            $.updateCell(_.column, _.value, _.record);
            if (_.column.render || $.enabledTotal()) $.reRender({
                column: _.column
            });
            $.reRender({
                rowdata: _.record
            });
            return true
        },
        _getCurrentPageData: function(_) {
            var $ = this,
                A = this.options,
                B = {};
            B[A.root] = [];
            if (!_ || !_[A.root] || !_[A.root].length) {
                B[A.record] = 0;
                return B
            }
            B[A.record] = _[A.root].length;
            if (!A.newPage) A.newPage = 1;
            for (i = (A.newPage - 1) * A.pageSize; i < _[A.root].length && i < A.newPage * A.pageSize; i++) B[A.root].push(_[A.root][i]);
            return B
        },
        _compareData: function(A, E, C, _) {
            var $ = this,
                B = this.options,
                D = A[C],
                F = E[C];
            if (D == null && F != null) return 1;
            else if (D == null && F == null) return 0;
            else if (D != null && F == null) return -1;
            if (B.sorters[_]) return B.sorters[_].call($, D, F);
            else return D < F ? -1 : D > F ? 1 : 0
        },
        _getTotalCellContent: function(K, D) {
            var _ = this,
                I = this.options,
                F = [];
            if (K.totalSummary) {
                var C = function($) {
                        for (var _ = 0; _ < L.length; _++) if (L[_].toLowerCase() == $.toLowerCase()) return true;
                        return false
                    },
                    E = 0,
                    B = 0,
                    M = 0,
                    A = parseFloat(D[0][K.name]),
                    $ = parseFloat(D[0][K.name]);
                for (var H = 0; H < D.length; H++) {
                    B += 1;
                    var J = parseFloat(D[H][K.name]);
                    if (!J) continue;
                    E += J;
                    if (J > A) A = J;
                    if (J < $) $ = J
                }
                M = E * 1 / D.length;
                if (K.totalSummary.render) {
                    var G = K.totalSummary.render({
                        sum: E,
                        count: B,
                        avg: M,
                        min: $,
                        max: A
                    }, K, _.data);
                    F.push(G)
                } else if (K.totalSummary.type) {
                    var L = K.totalSummary.type.split(",");
                    if (C("sum")) F.push("<div>Sum=" + E.toFixed(2) + "</div>");
                    if (C("count")) F.push("<div>Count=" + B + "</div>");
                    if (C("max")) F.push("<div>Max=" + A.toFixed(2) + "</div>");
                    if (C("min")) F.push("<div>Min=" + $.toFixed(2) + "</div>");
                    if (C("avg")) F.push("<div>Avg=" + M.toFixed(2) + "</div>")
                }
            }
            return F.join("")
        },
        _getTotalSummaryHtml: function(B, C, D) {
            var _ = this,
                A = this.options,
                E = [];
            if (C) E.push("<tr class=\"l-grid-totalsummary " + C + "\">");
            else E.push("<tr class=\"l-grid-totalsummary\">");
            $(_.columns).each(function(A, $) {
                if (this.frozen != D) return;
                if (this.isrownumber) {
                    E.push("<td class=\"l-grid-totalsummary-cell l-grid-totalsummary-cell-rownumbers\" style=\"width:" + this.width + "px\"><div>&nbsp;</div></td>");
                    return
                }
                if (this.ischeckbox) {
                    E.push("<td class=\"l-grid-totalsummary-cell l-grid-totalsummary-cell-checkbox\" style=\"width:" + this.width + "px\"><div>&nbsp;</div></td>");
                    return
                } else if (this.isdetail) {
                    E.push("<td class=\"l-grid-totalsummary-cell l-grid-totalsummary-cell-detail\" style=\"width:" + this.width + "px\"><div>&nbsp;</div></td>");
                    return
                }
                E.push("<td class=\"l-grid-totalsummary-cell");
                if (this.islast) E.push(" l-grid-totalsummary-cell-last");
                E.push("\" ");
                E.push("id=\"" + _.id + "|total" + _.totalNumber + "|" + $.__id + "\" ");
                E.push("width=\"" + this._width + "\" ");
                columnname = this.columnname;
                if (columnname) E.push("columnname=\"" + columnname + "\" ");
                E.push("columnindex=\"" + A + "\" ");
                E.push("><div class=\"l-grid-totalsummary-cell-inner\"");
                if ($.align) E.push(" style=\"text-Align:" + $.align + ";\"");
                E.push(">");
                E.push(_._getTotalCellContent($, B));
                E.push("</div></td>")
            });
            E.push("</tr>");
            if (!D) _.totalNumber++;
            return E.join("")
        },
        _bulidTotalSummary: function(C) {
            var _ = this,
                A = this.options;
            if (!_.isTotalSummary()) return false;
            if (!_.currentData || _.currentData[A.root].length == 0) return false;
            var B = $(_._getTotalSummaryHtml(_.currentData[A.root], null, C));
            $("tbody:first", C ? _.f.gridbody : _.gridbody).append(B)
        },
        _buildPager: function() {
            var _ = this,
                A = this.options;
            $(".pcontrol input", _.toolbar).val(A.page);
            if (!A.pageCount) A.pageCount = 1;
            $(".pcontrol span", _.toolbar).html(A.pageCount);
            var B = parseInt((A.page - 1) * A.pageSize) + 1,
                C = parseInt(B) + parseInt(A.pageSize) - 1;
            if (!A.total) A.total = 0;
            if (A.total < C) C = A.total;
            if (!A.total) B = C = 0;
            if (B < 0) B = 0;
            if (C < 0) C = 0;
            var D = A.pageStatMessage;
            D = D.replace(/{from}/, B);
            D = D.replace(/{to}/, C);
            D = D.replace(/{total}/, A.total);
            D = D.replace(/{pagesize}/, A.pageSize);
            $(".l-bar-text", _.toolbar).html(D);
            if (!A.total) $(".l-bar-btnfirst span,.l-bar-btnprev span,.l-bar-btnnext span,.l-bar-btnlast span", _.toolbar).addClass("l-disabled");
            if (A.page == 1) {
                $(".l-bar-btnfirst span", _.toolbar).addClass("l-disabled");
                $(".l-bar-btnprev span", _.toolbar).addClass("l-disabled")
            } else if (A.page > A.pageCount && A.pageCount > 0) {
                $(".l-bar-btnfirst span", _.toolbar).removeClass("l-disabled");
                $(".l-bar-btnprev span", _.toolbar).removeClass("l-disabled")
            }
            if (A.page == A.pageCount) {
                $(".l-bar-btnlast span", _.toolbar).addClass("l-disabled");
                $(".l-bar-btnnext span", _.toolbar).addClass("l-disabled")
            } else if (A.page < A.pageCount && A.pageCount > 0) {
                $(".l-bar-btnlast span", _.toolbar).removeClass("l-disabled");
                $(".l-bar-btnnext span", _.toolbar).removeClass("l-disabled")
            }
        },
        _getRowIdByDomId: function(_) {
            var $ = _.split("|"),
                A = $[2];
            return A
        },
        _getRowByDomId: function($) {
            return this.records[this._getRowIdByDomId($)]
        },
        _getSrcElementByEvent: function(B) {
            var A = this,
                F = (B.target || B.srcElement),
                D = $(F).parents().add(F),
                H = function(_) {
                    for (var B = 0, A = D.length; B < A; B++) if (typeof _ == "string") {
                        if ($(D[B]).hasClass(_)) return D[B]
                    } else if (typeof _ == "object") if (D[B] == _) return D[B];
                    return null
                };
            if (H("l-grid-editor")) return {
                editing: true,
                editor: H("l-grid-editor")
            };
            if (D.index(this.element) == -1) return {
                out: true
            };
            var _ = false;
            if (D.hasClass("l-grid-detailpanel") && A.detailrows) for (var I = 0, E = A.detailrows.length; I < E; I++) if (D.index(A.detailrows[I]) != -1) {
                _ = true;
                break
            }
            var C = {
                grid: H("l-panel"),
                indetail: _,
                frozen: H(A.gridview1[0]) ? true : false,
                header: H("l-panel-header"),
                gridheader: H("l-grid-header"),
                gridbody: H("l-grid-body"),
                total: H("l-panel-bar-total"),
                popup: H("l-grid-popup"),
                toolbar: H("l-panel-bar")
            };
            if (C.gridheader) {
                C.hrow = H("l-grid-hd-row");
                C.hcell = H("l-grid-hd-cell");
                C.hcelltext = H("l-grid-hd-cell-text");
                C.checkboxall = H("l-grid-hd-cell-checkbox");
                if (C.hcell) {
                    var G = C.hcell.id.split("|")[2];
                    C.column = A._columns[G]
                }
            }
            if (C.gridbody) {
                C.row = H("l-grid-row");
                C.cell = H("l-grid-row-cell");
                C.checkbox = H("l-grid-row-cell-btn-checkbox");
                C.groupbtn = H("l-grid-group-togglebtn");
                C.grouprow = H("l-grid-grouprow");
                C.detailbtn = H("l-grid-row-cell-detailbtn");
                C.detailrow = H("l-grid-detailpanel");
                C.totalrow = H("l-grid-totalsummary");
                C.totalcell = H("l-grid-totalsummary-cell");
                C.rownumberscell = $(C.cell).hasClass("l-grid-row-cell-rownumbers") ? C.cell : null;
                C.detailcell = $(C.cell).hasClass("l-grid-row-cell-detail") ? C.cell : null;
                C.checkboxcell = $(C.cell).hasClass("l-grid-row-cell-checkbox") ? C.cell : null;
                C.treelink = H("l-grid-tree-link");
                C.editor = H("l-grid-editor");
                if (C.row) C.data = this._getRowByDomId(C.row.id);
                if (C.cell) C.editing = $(C.cell).hasClass("l-grid-row-cell-editing");
                if (C.editor) C.editing = true;
                if (C.editing) C.out = false
            }
            if (C.toolbar) {
                C.first = H("l-bar-btnfirst");
                C.last = H("l-bar-btnlast");
                C.next = H("l-bar-btnnext");
                C.prev = H("l-bar-btnprev");
                C.load = H("l-bar-btnload");
                C.button = H("l-bar-button")
            }
            return C
        },
        _setEvent: function() {
            var _ = this,
                A = this.options;
            _.grid.bind("mousedown.grid", function($) {
                _._onMouseDown.call(_, $)
            });
            _.grid.bind("dblclick.grid", function($) {
                _._onDblClick.call(_, $)
            });
            _.grid.bind("contextmenu.grid", function($) {
                return _._onContextmenu.call(_, $)
            });
            $(document).bind("mouseup.grid", function($) {
                _._onMouseUp.call(_, $)
            });
            $(document).bind("click.grid", function($) {
                _._onClick.call(_, $)
            });
            $(window).bind("resize.grid", function($) {
                _._onResize.call(_)
            });
            $(document).bind("keydown.grid", function($) {
                if ($.ctrlKey) _.ctrlKey = true
            });
            $(document).bind("keyup.grid", function($) {
                delete _.ctrlKey
            });
            _.gridbody.bind("scroll.grid", function() {
                var A = _.gridbody.scrollLeft(),
                    $ = _.gridbody.scrollTop();
                if (A != null) _.gridheader[0].scrollLeft = A;
                if ($ != null) _.f.gridbody[0].scrollTop = $;
                _.endEdit();
                _.trigger("SysGridHeightChanged")
            });
            $("select", _.toolbar).change(function() {
                if (_.isDataChanged && !confirm(A.isContinueByDataChanged)) return false;
                A.newPage = 1;
                A.pageSize = this.value;
                _.loadData(A.where)
            });
            $("span.pcontrol :text", _.toolbar).blur(function($) {
                _.changePage("input")
            });
            $("div.l-bar-button", _.toolbar).hover(function() {
                $(this).addClass("l-bar-button-over")
            }, function() {
                $(this).removeClass("l-bar-button-over")
            });
            if ($.fn.ligerDrag && A.colDraggable) {
                _.colDroptip = $("<div class='l-drag-coldroptip' style='display:none'><div class='l-drop-move-up'></div><div class='l-drop-move-down'></div></div>").appendTo("body");
                _.gridheader.add(_.f.gridheader).ligerDrag({
                    revert: true,
                    animate: false,
                    proxyX: 0,
                    proxyY: 0,
                    proxy: function(E, B) {
                        var D = _._getSrcElementByEvent(B);
                        if (D.hcell && D.column) {
                            var A = $(".l-grid-hd-cell-text:first", D.hcell).html(),
                                C = $("<div class='l-drag-proxy' style='display:none'><div class='l-drop-icon l-drop-no'></div></div>").appendTo("body");
                            C.append(A);
                            return C
                        }
                    },
                    onRevert: function() {
                        return false
                    },
                    onRendered: function() {
                        this.set("cursor", "default");
                        _.children[this.id] = this
                    },
                    onStartDrag: function(C, A) {
                        if (A.button == 2) return false;
                        if (_.colresizing) return false;
                        this.set("cursor", "default");
                        var D = _._getSrcElementByEvent(A);
                        if (!D.hcell || !D.column || D.column.issystem || D.hcelltext) return false;
                        if ($(D.hcell).css("cursor").indexOf("resize") != -1) return false;
                        this.draggingColumn = D.column;
                        _.coldragging = true;
                        var B = _.grid.offset();
                        this.validRange = {
                            top: B.top,
                            bottom: B.top + _.gridheader.height(),
                            left: B.left - 10,
                            right: B.left + _.grid.width() + 10
                        }
                    },
                    onDrag: function(P, E) {
                        this.set("cursor", "default");
                        var Q = this.draggingColumn;
                        if (!Q) return false;
                        if (_.colresizing) return false;
                        if (_.colDropIn == null) _.colDropIn = -1;
                        var I = E.pageX,
                            J = E.pageY,
                            H = false,
                            O = _.grid.offset(),
                            C = this.validRange;
                        if (I < C.left || I > C.right || J > C.bottom || J < C.top) {
                            _.colDropIn = -1;
                            _.colDroptip.hide();
                            this.proxy.find(".l-drop-icon:first").removeClass("l-drop-yes").addClass("l-drop-no");
                            return
                        }
                        for (var L in _._columns) {
                            var D = _._columns[L];
                            if (Q == D) {
                                H = true;
                                continue
                            }
                            if (D.issystem) continue;
                            var M = D["__level"] == Q["__level"],
                                K = !M ? false : H ? true : false;
                            if (Q.frozen != D.frozen) K = D.frozen ? false : true;
                            if (_.colDropIn != -1 && _.colDropIn != L) continue;
                            var G = document.getElementById(D["__domid"]),
                                R = $(G).offset(),
                                F = {
                                    top: R.top,
                                    bottom: R.top + $(G).height(),
                                    left: R.left - 10,
                                    right: R.left + 10
                                };
                            if (K) {
                                var B = $(G).width();
                                F.left += B;
                                F.right += B
                            }
                            if (I > F.left && I < F.right && J > F.top && J < F.bottom) {
                                var N = A.headerRowHeight;
                                if (D["__rowSpan"]) N *= D["__rowSpan"];
                                _.colDroptip.css({
                                    left: F.left + 5,
                                    top: F.top - 9,
                                    height: N + 9 * 2
                                }).show();
                                _.colDropIn = L;
                                _.colDropDir = K ? "right" : "left";
                                this.proxy.find(".l-drop-icon:first").removeClass("l-drop-no").addClass("l-drop-yes");
                                break
                            } else if (_.colDropIn != -1) {
                                _.colDropIn = -1;
                                _.colDroptip.hide();
                                this.proxy.find(".l-drop-icon:first").removeClass("l-drop-yes").addClass("l-drop-no")
                            }
                        }
                    },
                    onStopDrag: function(B, $) {
                        var A = this.draggingColumn;
                        _.coldragging = false;
                        if (_.colDropIn != -1) {
                            _.changeCol.ligerDefer(_, 0, [A, _.colDropIn, _.colDropDir == "right"]);
                            _.colDropIn = -1
                        }
                        _.colDroptip.hide();
                        this.set("cursor", "default")
                    }
                })
            }
            if ($.fn.ligerDrag && A.rowDraggable) {
                _.rowDroptip = $("<div class='l-drag-rowdroptip' style='display:none'></div>").appendTo("body");
                _.gridbody.add(_.f.gridbody).ligerDrag({
                    revert: true,
                    animate: false,
                    proxyX: 0,
                    proxyY: 0,
                    proxy: function(F, C) {
                        var E = _._getSrcElementByEvent(C);
                        if (E.row) {
                            var B = A.draggingMessage.replace(/{count}/, F.draggingRows ? F.draggingRows.length : 1);
                            if (A.rowDraggingRender) B = A.rowDraggingRender(F.draggingRows, F, _);
                            var D = $("<div class='l-drag-proxy' style='display:none'><div class='l-drop-icon l-drop-no'></div>" + B + "</div>").appendTo("body");
                            return D
                        }
                    },
                    onRevert: function() {
                        return false
                    },
                    onRendered: function() {
                        this.set("cursor", "default");
                        _.children[this.id] = this
                    },
                    onStartDrag: function(C, $) {
                        if ($.button == 2) return false;
                        if (_.colresizing) return false;
                        if (!_.columns.length) return false;
                        this.set("cursor", "default");
                        var D = _._getSrcElementByEvent($);
                        if (!D.cell || !D.data || D.checkbox) return false;
                        var A = D.cell.id.split("|"),
                            B = _._columns[A[A.length - 1]];
                        if (D.rownumberscell || D.detailcell || D.checkboxcell || B == _.columns[0]) {
                            if (_.enabledCheckbox()) {
                                this.draggingRows = _.getSelecteds();
                                if (!this.draggingRows || !this.draggingRows.length) return false
                            } else this.draggingRows = [D.data];
                            this.draggingRow = D.data;
                            this.set("cursor", "move");
                            _.rowdragging = true;
                            this.validRange = {
                                top: _.gridbody.offset().top,
                                bottom: _.gridbody.offset().top + _.gridbody.height(),
                                left: _.grid.offset().left - 10,
                                right: _.grid.offset().left + _.grid.width() + 10
                            }
                        } else return false
                    },
                    onDrag: function(N, C) {
                        var P = this.draggingRow;
                        if (!P) return false;
                        var I = this.draggingRows ? this.draggingRows : [P];
                        if (_.colresizing) return false;
                        if (_.rowDropIn == null) _.rowDropIn = -1;
                        var F = C.pageX,
                            J = C.pageY,
                            E = false,
                            B = this.validRange;
                        if (F < B.left || F > B.right || J > B.bottom || J < B.top) {
                            _.rowDropIn = -1;
                            _.rowDroptip.hide();
                            this.proxy.find(".l-drop-icon:first").removeClass("l-drop-yes l-drop-add").addClass("l-drop-no");
                            return
                        }
                        for (var K in _.rows) {
                            var G = _.rows[K],
                                Q = G["__id"];
                            if (P == G) E = true;
                            if ($.inArray(G, I) != -1) continue;
                            var L = E ? true : false;
                            if (_.rowDropIn != -1 && _.rowDropIn != Q) continue;
                            var M = _.getRowObj(Q),
                                O = $(M).offset(),
                                D = {
                                    top: O.top - 4,
                                    bottom: O.top + $(M).height() + 4,
                                    left: _.grid.offset().left,
                                    right: _.grid.offset().left + _.grid.width()
                                };
                            if (F > D.left && F < D.right && J > D.top && J < D.bottom) {
                                var H = O.top;
                                if (L) H += $(M).height();
                                _.rowDroptip.css({
                                    left: D.left,
                                    top: H,
                                    width: D.right - D.left
                                }).show();
                                _.rowDropIn = Q;
                                _.rowDropDir = L ? "bottom" : "top";
                                if (A.tree && J > D.top + 5 && J < D.bottom - 5) {
                                    this.proxy.find(".l-drop-icon:first").removeClass("l-drop-no l-drop-yes").addClass("l-drop-add");
                                    _.rowDroptip.hide();
                                    _.rowDropInParent = true
                                } else {
                                    this.proxy.find(".l-drop-icon:first").removeClass("l-drop-no l-drop-add").addClass("l-drop-yes");
                                    _.rowDroptip.show();
                                    _.rowDropInParent = false
                                }
                                break
                            } else if (_.rowDropIn != -1) {
                                _.rowDropIn = -1;
                                _.rowDropInParent = false;
                                _.rowDroptip.hide();
                                this.proxy.find(".l-drop-icon:first").removeClass("l-drop-yes  l-drop-add").addClass("l-drop-no")
                            }
                        }
                    },
                    onStopDrag: function(D, C) {
                        var H = this.draggingRows;
                        _.rowdragging = false;
                        for (var G = 0; G < H.length; G++) {
                            var E = H[G].children;
                            if (E) H = $.grep(H, function(_, B) {
                                var A = $.inArray(_, E) == -1;
                                return A
                            })
                        }
                        if (_.rowDropIn != -1) {
                            if (A.tree) {
                                var F, B;
                                if (_.rowDropInParent) B = _.getRow(_.rowDropIn);
                                else {
                                    F = _.getRow(_.rowDropIn);
                                    B = _.getParent(F)
                                }
                                _.appendRange(H, B, F, _.rowDropDir != "bottom");
                                _.trigger("rowDragDrop", {
                                    rows: H,
                                    parent: B,
                                    near: F,
                                    after: _.rowDropDir == "bottom"
                                })
                            } else {
                                _.moveRange(H, _.rowDropIn, _.rowDropDir == "bottom");
                                _.trigger("rowDragDrop", {
                                    rows: H,
                                    parent: B,
                                    near: _.getRow(_.rowDropIn),
                                    after: _.rowDropDir == "bottom"
                                })
                            }
                            _.rowDropIn = -1
                        }
                        _.rowDroptip.hide();
                        this.set("cursor", "default")
                    }
                })
            }
        },
        _onRowOver: function(B, A) {
            if (l.draggable.dragging) return;
            var _ = this,
                C = this.options,
                D = _.getRow(B),
                E = A ? "addClass" : "removeClass";
            if (_.enabledFrozen()) $(_.getRowObj(D, true))[E](C.mouseoverRowCssClass);
            $(_.getRowObj(D, false))[E](C.mouseoverRowCssClass)
        },
        _onMouseUp: function(_) {
            var $ = this,
                A = this.options;
            if (l.draggable.dragging) {
                var B = $._getSrcElementByEvent(_);
                if (B.hcell && B.column) $.trigger("dragdrop", [{
                    type: "header",
                    column: B.column,
                    cell: B.hcell
                },
                _]);
                else if (B.row) $.trigger("dragdrop", [{
                    type: "row",
                    record: B.data,
                    row: B.row
                },
                _])
            }
        },
        _onMouseDown: function(_) {
            var $ = this,
                A = this.options
        },
        _onContextmenu: function(A) {
            var _ = this,
                B = this.options,
                D = _._getSrcElementByEvent(A);
            if (D.row) {
                if (B.whenRClickToSelect) _.select(D.data);
                if (_.hasBind("contextmenu")) return _.trigger("contextmenu", [{
                    data: D.data,
                    rowindex: D.data["__index"],
                    row: D.row
                },
                A])
            } else if (D.hcell) {
                if (!B.allowHideColumn) return true;
                var E = $(D.hcell).attr("columnindex");
                if (E == undefined) return true;
                var C = (A.pageX - _.body.offset().left + parseInt(_.body[0].scrollLeft));
                if (E == _.columns.length - 1) C -= 50;
                _.popup.css({
                    left: C,
                    top: _.gridheader.height() + 1
                });
                _.popup.toggle();
                return false
            }
        },
        _onDblClick: function(_) {
            var $ = this,
                A = this.options,
                B = $._getSrcElementByEvent(_);
            if (B.row) $.trigger("dblClickRow", [B.data, B.data["__id"], B.row])
        },
        _onClick: function(L) {
            var W = (L.target || L.srcElement),
                K = this,
                U = this.options,
                I = K._getSrcElementByEvent(L);
            if (I.out) {
                if (K.editor.editing && !$.ligerui.win.masking) K.endEdit();
                if (U.allowHideColumn) K.popup.hide();
                return
            }
            if (I.indetail || I.editing) return;
            if (K.editor.editing) K.endEdit();
            if (U.allowHideColumn) if (!I.popup) K.popup.hide();
            if (I.checkboxall) {
                var O = $(I.hrow),
                    B = O.hasClass("l-checked");
                if (K.trigger("beforeCheckAllRow", [!B, K.element]) == false) return false;
                if (B) O.removeClass("l-checked");
                else O.addClass("l-checked");
                K.selected = [];
                for (var X in K.records) if (B) K.unselect(K.records[X]);
                else K.select(K.records[X]);
                K.trigger("checkAllRow", [!B, K.element])
            } else if (I.hcelltext) {
                var Q = $(I.hcelltext).parent().parent();
                if (!U.enabledSort || !I.column) return;
                if (I.column.isSort == false) return;
                if (U.url && K.isDataChanged && !confirm(U.isContinueByDataChanged)) return;
                var _ = $(".l-grid-hd-cell-sort:first", Q),
                    H = I.column.name;
                if (!H) return;
                if (_.length > 0) {
                    if (_.hasClass("l-grid-hd-cell-sort-asc")) {
                        _.removeClass("l-grid-hd-cell-sort-asc").addClass("l-grid-hd-cell-sort-desc");
                        Q.removeClass("l-grid-hd-cell-asc").addClass("l-grid-hd-cell-desc");
                        K.changeSort(H, "desc")
                    } else if (_.hasClass("l-grid-hd-cell-sort-desc")) {
                        _.removeClass("l-grid-hd-cell-sort-desc").addClass("l-grid-hd-cell-sort-asc");
                        Q.removeClass("l-grid-hd-cell-desc").addClass("l-grid-hd-cell-asc");
                        K.changeSort(H, "asc")
                    }
                } else {
                    Q.removeClass("l-grid-hd-cell-desc").addClass("l-grid-hd-cell-asc");
                    $(I.hcelltext).after("<span class='l-grid-hd-cell-sort l-grid-hd-cell-sort-asc'>&nbsp;&nbsp;</span>");
                    K.changeSort(H, "asc")
                }
                $(".l-grid-hd-cell-sort", K.gridheader).add($(".l-grid-hd-cell-sort", K.f.gridheader)).not($(".l-grid-hd-cell-sort:first", Q)).remove()
            } else if (I.detailbtn && U.detail) {
                var T = I.data,
                    O = $([K.getRowObj(T, false)]);
                if (K.enabledFrozen()) O = O.add(K.getRowObj(T, true));
                X = T["__id"];
                if ($(I.detailbtn).hasClass("l-open")) {
                    if (U.detail.onCollapse) U.detail.onCollapse(T, $(".l-grid-detailpanel-inner:first", R)[0]);
                    O.next("tr.l-grid-detailpanel").hide();
                    $(I.detailbtn).removeClass("l-open")
                } else {
                    var R = O.next("tr.l-grid-detailpanel");
                    if (R.length > 0) {
                        R.show();
                        if (U.detail.onExtend) U.detail.onExtend(T, $(".l-grid-detailpanel-inner:first", R)[0]);
                        $(I.detailbtn).addClass("l-open");
                        K.trigger("SysGridHeightChanged");
                        return
                    }
                    $(I.detailbtn).addClass("l-open");
                    var S = 0;
                    for (var P = 0; P < K.columns.length; P++) if (K.columns[P].frozen) S++;
                    var G = $("<tr class='l-grid-detailpanel'><td><div class='l-grid-detailpanel-inner' style='display:none'></div></td></tr>"),
                        E = $("<tr class='l-grid-detailpanel'><td><div class='l-grid-detailpanel-inner' style='display:none'></div></td></tr>");
                    G.attr("id", K.id + "|detail|" + X);
                    K.detailrows = K.detailrows || [];
                    K.detailrows.push(G[0]);
                    K.detailrows.push(E[0]);
                    var A = $("div:first", G);
                    A.parent().attr("colSpan", K.columns.length - S);
                    O.eq(0).after(G);
                    if (S > 0) {
                        E.find("td:first").attr("colSpan", S);
                        O.eq(1).after(E)
                    }
                    if (U.detail.onShowDetail) {
                        U.detail.onShowDetail(T, A[0], function() {
                            K.trigger("SysGridHeightChanged")
                        });
                        $("div:first", E).add(A).show().height(U.detail.height || U.detailHeight)
                    } else if (U.detail.render) {
                        A.append(U.detail.render());
                        A.show()
                    }
                    K.trigger("SysGridHeightChanged")
                }
            } else if (I.groupbtn) {
                var F = $(I.grouprow),
                    V = true;
                if ($(I.groupbtn).hasClass("l-grid-group-togglebtn-close")) {
                    $(I.groupbtn).removeClass("l-grid-group-togglebtn-close");
                    if (F.hasClass("l-grid-grouprow-last")) $("td:first", F).width("auto")
                } else {
                    V = false;
                    $(I.groupbtn).addClass("l-grid-group-togglebtn-close");
                    if (F.hasClass("l-grid-grouprow-last")) $("td:first", F).width(K.gridtablewidth)
                }
                var N = F.next(".l-grid-row,.l-grid-totalsummary-group,.l-grid-detailpanel");
                while (true) {
                    if (N.length == 0) break;
                    if (V) {
                        N.show();
                        if (N.hasClass("l-grid-detailpanel") && !N.prev().find("td.l-grid-row-cell-detail:first span.l-grid-row-cell-detailbtn:first").hasClass("l-open")) N.hide()
                    } else N.hide();
                    N = N.next(".l-grid-row,.l-grid-totalsummary-group,.l-grid-detailpanel")
                }
                K.trigger("SysGridHeightChanged")
            } else if (I.treelink) K.toggle(I.data);
            else if (I.row && K.enabledCheckbox()) {
                var J = U.selectRowButtonOnly ? true : false;
                if (U.enabledEdit) J = true;
                if (I.checkbox || !J) {
                    O = $(I.row), B = O.hasClass("l-selected");
                    if (K.trigger("beforeCheckRow", [!B, I.data, I.data["__id"], I.row]) == false) return false;
                    var D = B ? "unselect" : "select";
                    K[D](I.data);
                    if (U.tree && U.autoCheckChildren) {
                        var C = K.getChildren(I.data, true);
                        for (var P = 0, M = C.length; P < M; P++) K[D](C[P])
                    }
                    K.trigger("checkRow", [!B, I.data, I.data["__id"], I.row])
                }
                if (!I.checkbox && I.cell && U.enabledEdit && U.clickToEdit) K._applyEditor(I.cell)
            } else if (I.row && !K.enabledCheckbox()) {
                if (I.cell && U.enabledEdit && U.clickToEdit) K._applyEditor(I.cell);
                if ($(I.row).hasClass("l-selected")) {
                    if (!U.allowUnSelectRow) {
                        $(I.row).addClass("l-selected-again");
                        return
                    }
                    K.unselect(I.data)
                } else K.select(I.data)
            } else if (I.toolbar) if (I.first) {
                if (K.trigger("toFirst", [K.element]) == false) return false;
                K.changePage("first")
            } else if (I.prev) {
                if (K.trigger("toPrev", [K.element]) == false) return false;
                K.changePage("prev")
            } else if (I.next) {
                if (K.trigger("toNext", [K.element]) == false) return false;
                K.changePage("next")
            } else if (I.last) {
                if (K.trigger("toLast", [K.element]) == false) return false;
                K.changePage("last")
            } else if (I.load) {
                if ($("span", I.load).hasClass("l-disabled")) return false;
                if (K.trigger("reload", [K.element]) == false) return false;
                if (U.url && K.isDataChanged && !confirm(U.isContinueByDataChanged)) return false;
                K.loadData(U.where)
            }
        },
        select: function(C) {
            var A = this,
                D = this.options,
                F = A.getRow(C),
                G = F["__id"],
                B = A.getRowObj(G),
                _ = A.getRowObj(G, true);
            if (!A.enabledCheckbox() && !A.ctrlKey) {
                for (var H in A.selected) {
                    var E = A.selected[H];
                    if (E["__id"] in A.records) {
                        $(A.getRowObj(E)).removeClass("l-selected l-selected-again");
                        if (A.enabledFrozen()) $(A.getRowObj(E, true)).removeClass("l-selected l-selected-again")
                    }
                }
                A.selected = []
            }
            if (B) $(B).addClass("l-selected");
            if (_) $(_).addClass("l-selected");
            A.selected[A.selected.length] = F;
            A.trigger("selectRow", [F, G, B])
        },
        unselect: function(C) {
            var A = this,
                D = this.options,
                E = A.getRow(C),
                F = E["__id"],
                B = A.getRowObj(F),
                _ = A.getRowObj(F, true);
            $(B).removeClass("l-selected l-selected-again");
            if (A.enabledFrozen()) $(_).removeClass("l-selected l-selected-again");
            A._removeSelected(E);
            A.trigger("unSelectRow", [E, F, B])
        },
        isSelected: function(_) {
            var $ = this,
                A = this.options,
                B = $.getRow(_);
            for (var C in $.selected) if ($.selected[C] == B) return true;
            return false
        },
        _onResize: function() {
            var _ = this,
                A = this.options;
            if (A.height && A.height != "auto") {
                var D = $(window).height(),
                    G = 0,
                    C = null;
                if (typeof(A.height) == "string" && A.height.indexOf("%") > 0) {
                    var B = _.grid.parent();
                    if (A.InWindow) {
                        C = D;
                        C -= parseInt($("body").css("paddingTop"));
                        C -= parseInt($("body").css("paddingBottom"))
                    } else C = B.height();
                    G = C * parseFloat(A.height) * 0.01;
                    if (A.InWindow || B[0].tagName.toLowerCase() == "body") G -= (_.grid.offset().top - parseInt($("body").css("paddingTop")))
                } else G = parseInt(A.height);
                G += A.heightDiff;
                _.windowHeight = D;
                _._setHeight(G)
            }
            if (_.enabledFrozen()) {
                var F = _.gridview1.width(),
                    E = _.gridview.width();
                _.gridview2.css({
                    width: E - F
                })
            }
            _.trigger("SysGridHeightChanged")
        }
    });
    $.ligerui.controls.Grid.prototype.enabledTotal = $.ligerui.controls.Grid.prototype.isTotalSummary;
    $.ligerui.controls.Grid.prototype.add = $.ligerui.controls.Grid.prototype.addRow;
    $.ligerui.controls.Grid.prototype.update = $.ligerui.controls.Grid.prototype.updateRow;
    $.ligerui.controls.Grid.prototype.append = $.ligerui.controls.Grid.prototype.appendRow;
    $.ligerui.controls.Grid.prototype.getSelected = $.ligerui.controls.Grid.prototype.getSelectedRow;
    $.ligerui.controls.Grid.prototype.getSelecteds = $.ligerui.controls.Grid.prototype.getSelectedRows;
    $.ligerui.controls.Grid.prototype.getCheckedRows = $.ligerui.controls.Grid.prototype.getSelectedRows;
    $.ligerui.controls.Grid.prototype.getCheckedRowObjs = $.ligerui.controls.Grid.prototype.getSelectedRowObjs;
    $.ligerui.controls.Grid.prototype.setOptions = $.ligerui.controls.Grid.prototype.set
})(jQuery);
(function($) {
    $.fn.ligerLayout = function(_) {
        return $.ligerui.run.call(this, "ligerLayout", arguments)
    };
    $.fn.ligerGetLayoutManager = function() {
        return $.ligerui.run.call(this, "ligerGetLayoutManager", arguments)
    };
    $.ligerDefaults.Layout = {
        topHeight: 50,
        bottomHeight: 50,
        leftWidth: 110,
        centerWidth: 300,
        rightWidth: 170,
        InWindow: true,
        heightDiff: 0,
        height: "100%",
        onHeightChanged: null,
        isLeftCollapse: false,
        isRightCollapse: false,
        allowLeftCollapse: true,
        allowRightCollapse: true,
        allowLeftResize: true,
        allowRightResize: true,
        allowTopResize: true,
        allowBottomResize: true,
        space: 3,
        onEndResize: null,
        minLeftWidth: 80,
        minRightWidth: 80
    };
    $.ligerMethos.Layout = {};
    $.ligerui.controls.Layout = function(_, A) {
        $.ligerui.controls.Layout.base.constructor.call(this, _, A)
    };
    $.ligerui.controls.Layout.ligerExtend($.ligerui.core.UIComponent, {
        __getType: function() {
            return "Layout"
        },
        __idPrev: function() {
            return "Layout"
        },
        _extendMethods: function() {
            return $.ligerMethos.Layout
        },
        _render: function() {
            var _ = this,
                B = this.options;
            _.layout = $(this.element);
            _.layout.addClass("l-layout");
            _.width = _.layout.width();
            if ($("> div[position=top]", _.layout).length > 0) {
                _.top = $("> div[position=top]", _.layout).wrap("<div class=\"l-layout-top\" style=\"top:0px;\"></div>").parent();
                _.top.content = $("> div[position=top]", _.top);
                if (!_.top.content.hasClass("l-layout-content")) _.top.content.addClass("l-layout-content");
                _.topHeight = B.topHeight;
                if (_.topHeight) _.top.height(_.topHeight)
            }
            if ($("> div[position=bottom]", _.layout).length > 0) {
                _.bottom = $("> div[position=bottom]", _.layout).wrap("<div class=\"l-layout-bottom\"></div>").parent();
                _.bottom.content = $("> div[position=bottom]", _.bottom);
                if (!_.bottom.content.hasClass("l-layout-content")) _.bottom.content.addClass("l-layout-content");
                _.bottomHeight = B.bottomHeight;
                if (_.bottomHeight) _.bottom.height(_.bottomHeight);
                var E = _.bottom.content.attr("title");
                if (E) {
                    _.bottom.header = $("<div class=\"l-layout-header\"></div>");
                    _.bottom.prepend(_.bottom.header);
                    _.bottom.header.html(E);
                    _.bottom.content.attr("title", "")
                }
            }
            if ($("> div[position=left]", _.layout).length > 0) {
                _.left = $("> div[position=left]", _.layout).wrap("<div class=\"l-layout-left\" style=\"left:0px;\"></div>").parent();
                _.left.header = $("<div class=\"l-layout-header\"><div class=\"l-layout-header-toggle\"></div><div class=\"l-layout-header-inner\"></div></div>");
                _.left.prepend(_.left.header);
                _.left.header.toggle = $(".l-layout-header-toggle", _.left.header);
                _.left.content = $("> div[position=left]", _.left);
                if (!_.left.content.hasClass("l-layout-content")) _.left.content.addClass("l-layout-content");
                if (!B.allowLeftCollapse) $(".l-layout-header-toggle", _.left.header).remove();
                var A = _.left.content.attr("title");
                if (A) {
                    _.left.content.attr("title", "");
                    $(".l-layout-header-inner", _.left.header).html(A)
                }
                _.leftWidth = B.leftWidth;
                if (_.leftWidth) _.left.width(_.leftWidth)
            }
            if ($("> div[position=center]", _.layout).length > 0) {
                _.center = $("> div[position=center]", _.layout).wrap("<div class=\"l-layout-center\" ></div>").parent();
                _.center.content = $("> div[position=center]", _.center);
                _.center.content.addClass("l-layout-content");
                var C = _.center.content.attr("title");
                if (C) {
                    _.center.content.attr("title", "");
                    _.center.header = $("<div class=\"l-layout-header\"></div>");
                    _.center.prepend(_.center.header);
                    _.center.header.html(C)
                }
                _.centerWidth = B.centerWidth;
                if (_.centerWidth) _.center.width(_.centerWidth)
            }
            if ($("> div[position=right]", _.layout).length > 0) {
                _.right = $("> div[position=right]", _.layout).wrap("<div class=\"l-layout-right\"></div>").parent();
                _.right.header = $("<div class=\"l-layout-header\"><div class=\"l-layout-header-toggle\"></div><div class=\"l-layout-header-inner\"></div></div>");
                _.right.prepend(_.right.header);
                _.right.header.toggle = $(".l-layout-header-toggle", _.right.header);
                if (!B.allowRightCollapse) $(".l-layout-header-toggle", _.right.header).remove();
                _.right.content = $("> div[position=right]", _.right);
                if (!_.right.content.hasClass("l-layout-content")) _.right.content.addClass("l-layout-content");
                var D = _.right.content.attr("title");
                if (D) {
                    _.right.content.attr("title", "");
                    $(".l-layout-header-inner", _.right.header).html(D)
                }
                _.rightWidth = B.rightWidth;
                if (_.rightWidth) _.right.width(_.rightWidth)
            }
            _.layout.lock = $("<div class='l-layout-lock'></div>");
            _.layout.append(_.layout.lock);
            _._addDropHandle();
            _.isLeftCollapse = B.isLeftCollapse;
            _.isRightCollapse = B.isRightCollapse;
            _.leftCollapse = $("<div class=\"l-layout-collapse-left\" style=\"display: none; \"><div class=\"l-layout-collapse-left-toggle\"></div></div>");
            _.rightCollapse = $("<div class=\"l-layout-collapse-right\" style=\"display: none; \"><div class=\"l-layout-collapse-right-toggle\"></div></div>");
            _.layout.append(_.leftCollapse).append(_.rightCollapse);
            _.leftCollapse.toggle = $("> .l-layout-collapse-left-toggle", _.leftCollapse);
            _.rightCollapse.toggle = $("> .l-layout-collapse-right-toggle", _.rightCollapse);
            _._setCollapse();
            _._bulid();
            $(window).resize(function() {
                _._onResize()
            });
            _.set(B)
        },
        setLeftCollapse: function(_) {
            var $ = this,
                A = this.options;
            if (!$.left) return false;
            $.isLeftCollapse = _;
            if ($.isLeftCollapse) {
                $.leftCollapse.show();
                $.leftDropHandle && $.leftDropHandle.hide();
                $.left.hide()
            } else {
                $.leftCollapse.hide();
                $.leftDropHandle && $.leftDropHandle.show();
                $.left.show()
            }
            $._onResize()
        },
        setRightCollapse: function(_) {
            var $ = this,
                A = this.options;
            if (!$.right) return false;
            $.isRightCollapse = _;
            $._onResize();
            if ($.isRightCollapse) {
                $.rightCollapse.show();
                $.rightDropHandle && $.rightDropHandle.hide();
                $.right.hide()
            } else {
                $.rightCollapse.hide();
                $.rightDropHandle && $.rightDropHandle.show();
                $.right.show()
            }
            $._onResize()
        },
        _bulid: function() {
            var _ = this,
                A = this.options;
            $("> .l-layout-left .l-layout-header,> .l-layout-right .l-layout-header", _.layout).hover(function() {
                $(this).addClass("l-layout-header-over")
            }, function() {
                $(this).removeClass("l-layout-header-over")
            });
            $(".l-layout-header-toggle", _.layout).hover(function() {
                $(this).addClass("l-layout-header-toggle-over")
            }, function() {
                $(this).removeClass("l-layout-header-toggle-over")
            });
            $(".l-layout-header-toggle", _.left).click(function() {
                _.setLeftCollapse(true)
            });
            $(".l-layout-header-toggle", _.right).click(function() {
                _.setRightCollapse(true)
            });
            _.middleTop = 0;
            if (_.top) {
                _.middleTop += _.top.height();
                _.middleTop += parseInt(_.top.css("borderTopWidth"));
                _.middleTop += parseInt(_.top.css("borderBottomWidth"));
                _.middleTop += A.space
            }
            if (_.left) {
                _.left.css({
                    top: _.middleTop
                });
                _.leftCollapse.css({
                    top: _.middleTop
                })
            }
            if (_.center) _.center.css({
                top: _.middleTop
            });
            if (_.right) {
                _.right.css({
                    top: _.middleTop
                });
                _.rightCollapse.css({
                    top: _.middleTop
                })
            }
            if (_.left) _.left.css({
                left: 0
            });
            _._onResize();
            _._onResize()
        },
        _setCollapse: function() {
            var _ = this,
                A = this.options;
            _.leftCollapse.hover(function() {
                $(this).addClass("l-layout-collapse-left-over")
            }, function() {
                $(this).removeClass("l-layout-collapse-left-over")
            });
            _.leftCollapse.toggle.hover(function() {
                $(this).addClass("l-layout-collapse-left-toggle-over")
            }, function() {
                $(this).removeClass("l-layout-collapse-left-toggle-over")
            });
            _.rightCollapse.hover(function() {
                $(this).addClass("l-layout-collapse-right-over")
            }, function() {
                $(this).removeClass("l-layout-collapse-right-over")
            });
            _.rightCollapse.toggle.hover(function() {
                $(this).addClass("l-layout-collapse-right-toggle-over")
            }, function() {
                $(this).removeClass("l-layout-collapse-right-toggle-over")
            });
            _.leftCollapse.toggle.click(function() {
                _.setLeftCollapse(false)
            });
            _.rightCollapse.toggle.click(function() {
                _.setRightCollapse(false)
            });
            if (_.left && _.isLeftCollapse) {
                _.leftCollapse.show();
                _.leftDropHandle && _.leftDropHandle.hide();
                _.left.hide()
            }
            if (_.right && _.isRightCollapse) {
                _.rightCollapse.show();
                _.rightDropHandle && _.rightDropHandle.hide();
                _.right.hide()
            }
        },
        _addDropHandle: function() {
            var _ = this,
                A = this.options;
            if (_.left && A.allowLeftResize) {
                _.leftDropHandle = $("<div class='l-layout-drophandle-left'></div>");
                _.layout.append(_.leftDropHandle);
                _.leftDropHandle && _.leftDropHandle.show();
                _.leftDropHandle.mousedown(function($) {
                    _._start("leftresize", $)
                })
            }
            if (_.right && A.allowRightResize) {
                _.rightDropHandle = $("<div class='l-layout-drophandle-right'></div>");
                _.layout.append(_.rightDropHandle);
                _.rightDropHandle && _.rightDropHandle.show();
                _.rightDropHandle.mousedown(function($) {
                    _._start("rightresize", $)
                })
            }
            if (_.top && A.allowTopResize) {
                _.topDropHandle = $("<div class='l-layout-drophandle-top'></div>");
                _.layout.append(_.topDropHandle);
                _.topDropHandle.show();
                _.topDropHandle.mousedown(function($) {
                    _._start("topresize", $)
                })
            }
            if (_.bottom && A.allowBottomResize) {
                _.bottomDropHandle = $("<div class='l-layout-drophandle-bottom'></div>");
                _.layout.append(_.bottomDropHandle);
                _.bottomDropHandle.show();
                _.bottomDropHandle.mousedown(function($) {
                    _._start("bottomresize", $)
                })
            }
            _.draggingxline = $("<div class='l-layout-dragging-xline'></div>");
            _.draggingyline = $("<div class='l-layout-dragging-yline'></div>");
            _.layout.append(_.draggingxline).append(_.draggingyline)
        },
        _setDropHandlePosition: function() {
            var $ = this,
                _ = this.options;
            if ($.leftDropHandle) $.leftDropHandle.css({
                left: $.left.width() + parseInt($.left.css("left")),
                height: $.middleHeight,
                top: $.middleTop
            });
            if ($.rightDropHandle) $.rightDropHandle.css({
                left: parseInt($.right.css("left")) - _.space,
                height: $.middleHeight,
                top: $.middleTop
            });
            if ($.topDropHandle) $.topDropHandle.css({
                top: $.top.height() + parseInt($.top.css("top")),
                width: $.top.width()
            });
            if ($.bottomDropHandle) $.bottomDropHandle.css({
                top: parseInt($.bottom.css("top")) - _.space,
                width: $.bottom.width()
            })
        },
        _onResize: function() {
            var _ = this,
                B = this.options,
                E = _.layout.height(),
                G = 0,
                D = $(window).height(),
                C = null;
            if (typeof(B.height) == "string" && B.height.indexOf("%") > 0) {
                var F = _.layout.parent();
                if (B.InWindow || F[0].tagName.toLowerCase() == "body") {
                    C = D;
                    C -= parseInt($("body").css("paddingTop"));
                    C -= parseInt($("body").css("paddingBottom"))
                } else C = F.height();
                G = C * parseFloat(B.height) * 0.01;
                if (B.InWindow || F[0].tagName.toLowerCase() == "body") G -= (_.layout.offset().top - parseInt($("body").css("paddingTop")))
            } else G = parseInt(B.height);
            G += B.heightDiff;
            _.layout.height(G);
            _.layoutHeight = _.layout.height();
            _.middleWidth = _.layout.width();
            _.middleHeight = _.layout.height();
            if (_.top) {
                _.middleHeight -= _.top.height();
                _.middleHeight -= parseInt(_.top.css("borderTopWidth"));
                _.middleHeight -= parseInt(_.top.css("borderBottomWidth"));
                _.middleHeight -= B.space
            }
            if (_.bottom) {
                _.middleHeight -= _.bottom.height();
                _.middleHeight -= parseInt(_.bottom.css("borderTopWidth"));
                _.middleHeight -= parseInt(_.bottom.css("borderBottomWidth"));
                _.middleHeight -= B.space
            }
            _.middleHeight -= 2;
            if (_.hasBind("heightChanged") && _.layoutHeight != E) _.trigger("heightChanged", [{
                layoutHeight: _.layoutHeight,
                diff: _.layoutHeight - E,
                middleHeight: _.middleHeight
            }]);
            if (_.center) {
                _.centerWidth = _.middleWidth;
                if (_.left) if (_.isLeftCollapse) {
                    _.centerWidth -= _.leftCollapse.width();
                    _.centerWidth -= parseInt(_.leftCollapse.css("borderLeftWidth"));
                    _.centerWidth -= parseInt(_.leftCollapse.css("borderRightWidth"));
                    _.centerWidth -= parseInt(_.leftCollapse.css("left"));
                    _.centerWidth -= B.space
                } else {
                    _.centerWidth -= _.leftWidth;
                    _.centerWidth -= parseInt(_.left.css("borderLeftWidth"));
                    _.centerWidth -= parseInt(_.left.css("borderRightWidth"));
                    _.centerWidth -= parseInt(_.left.css("left"));
                    _.centerWidth -= B.space
                }
                if (_.right) if (_.isRightCollapse) {
                    _.centerWidth -= _.rightCollapse.width();
                    _.centerWidth -= parseInt(_.rightCollapse.css("borderLeftWidth"));
                    _.centerWidth -= parseInt(_.rightCollapse.css("borderRightWidth"));
                    _.centerWidth -= parseInt(_.rightCollapse.css("right"));
                    _.centerWidth -= B.space
                } else {
                    _.centerWidth -= _.rightWidth;
                    _.centerWidth -= parseInt(_.right.css("borderLeftWidth"));
                    _.centerWidth -= parseInt(_.right.css("borderRightWidth"));
                    _.centerWidth -= B.space
                }
                _.centerLeft = 0;
                if (_.left) if (_.isLeftCollapse) {
                    _.centerLeft += _.leftCollapse.width();
                    _.centerLeft += parseInt(_.leftCollapse.css("borderLeftWidth"));
                    _.centerLeft += parseInt(_.leftCollapse.css("borderRightWidth"));
                    _.centerLeft += parseInt(_.leftCollapse.css("left"));
                    _.centerLeft += B.space
                } else {
                    _.centerLeft += _.left.width();
                    _.centerLeft += parseInt(_.left.css("borderLeftWidth"));
                    _.centerLeft += parseInt(_.left.css("borderRightWidth"));
                    _.centerLeft += B.space
                }
                _.center.css({
                    left: _.centerLeft
                });
                _.center.width(_.centerWidth);
                _.center.height(_.middleHeight);
                var A = _.middleHeight;
                if (_.center.header) A -= _.center.header.height();
                _.center.content.height(A)
            }
            if (_.left) {
                _.leftCollapse.height(_.middleHeight);
                _.left.height(_.middleHeight)
            }
            if (_.right) {
                _.rightCollapse.height(_.middleHeight);
                _.right.height(_.middleHeight);
                _.rightLeft = 0;
                if (_.left) if (_.isLeftCollapse) {
                    _.rightLeft += _.leftCollapse.width();
                    _.rightLeft += parseInt(_.leftCollapse.css("borderLeftWidth"));
                    _.rightLeft += parseInt(_.leftCollapse.css("borderRightWidth"));
                    _.rightLeft += B.space
                } else {
                    _.rightLeft += _.left.width();
                    _.rightLeft += parseInt(_.left.css("borderLeftWidth"));
                    _.rightLeft += parseInt(_.left.css("borderRightWidth"));
                    _.rightLeft += parseInt(_.left.css("left"));
                    _.rightLeft += B.space
                }
                if (_.center) {
                    _.rightLeft += _.center.width();
                    _.rightLeft += parseInt(_.center.css("borderLeftWidth"));
                    _.rightLeft += parseInt(_.center.css("borderRightWidth"));
                    _.rightLeft += B.space
                }
                _.right.css({
                    left: _.rightLeft
                })
            }
            if (_.bottom) {
                _.bottomTop = _.layoutHeight - _.bottom.height() - 2;
                _.bottom.css({
                    top: _.bottomTop
                })
            }
            _._setDropHandlePosition()
        },
        _start: function(C, A) {
            var _ = this,
                B = this.options;
            _.dragtype = C;
            if (C == "leftresize" || C == "rightresize") {
                _.xresize = {
                    startX: A.pageX
                };
                _.draggingyline.css({
                    left: A.pageX - _.layout.offset().left,
                    height: _.middleHeight,
                    top: _.middleTop
                }).show();
                $("body").css("cursor", "col-resize")
            } else if (C == "topresize" || C == "bottomresize") {
                _.yresize = {
                    startY: A.pageY
                };
                _.draggingxline.css({
                    top: A.pageY - _.layout.offset().top,
                    width: _.layout.width()
                }).show();
                $("body").css("cursor", "row-resize")
            } else return;
            _.layout.lock.width(_.layout.width());
            _.layout.lock.height(_.layout.height());
            _.layout.lock.show();
            if ($.browser.msie || $.browser.safari) $("body").bind("selectstart", function() {
                return false
            });
            $(document).bind("mouseup", function() {
                _._stop.apply(_, arguments)
            });
            $(document).bind("mousemove", function() {
                _._drag.apply(_, arguments)
            })
        },
        _drag: function(A) {
            var _ = this,
                B = this.options;
            if (_.xresize) {
                _.xresize.diff = A.pageX - _.xresize.startX;
                _.draggingyline.css({
                    left: A.pageX - _.layout.offset().left
                });
                $("body").css("cursor", "col-resize")
            } else if (_.yresize) {
                _.yresize.diff = A.pageY - _.yresize.startY;
                _.draggingxline.css({
                    top: A.pageY - _.layout.offset().top
                });
                $("body").css("cursor", "row-resize")
            }
        },
        _stop: function(A) {
            var _ = this,
                C = this.options,
                B;
            if (_.xresize && _.xresize.diff != undefined) {
                B = _.xresize.diff;
                if (_.dragtype == "leftresize") {
                    if (C.minLeftWidth) if (_.leftWidth + _.xresize.diff < C.minLeftWidth) return;
                    _.leftWidth += _.xresize.diff;
                    _.left.width(_.leftWidth);
                    if (_.center) _.center.width(_.center.width() - _.xresize.diff).css({
                        left: parseInt(_.center.css("left")) + _.xresize.diff
                    });
                    else if (_.right) _.right.width(_.left.width() - _.xresize.diff).css({
                        left: parseInt(_.right.css("left")) + _.xresize.diff
                    })
                } else if (_.dragtype == "rightresize") {
                    if (C.minRightWidth) if (_.rightWidth - _.xresize.diff < C.minRightWidth) return;
                    _.rightWidth -= _.xresize.diff;
                    _.right.width(_.rightWidth).css({
                        left: parseInt(_.right.css("left")) + _.xresize.diff
                    });
                    if (_.center) _.center.width(_.center.width() + _.xresize.diff);
                    else if (_.left) _.left.width(_.left.width() + _.xresize.diff)
                }
            } else if (_.yresize && _.yresize.diff != undefined) {
                B = _.yresize.diff;
                if (_.dragtype == "topresize") {
                    _.top.height(_.top.height() + _.yresize.diff);
                    _.middleTop += _.yresize.diff;
                    _.middleHeight -= _.yresize.diff;
                    if (_.left) {
                        _.left.css({
                            top: _.middleTop
                        }).height(_.middleHeight);
                        _.leftCollapse.css({
                            top: _.middleTop
                        }).height(_.middleHeight)
                    }
                    if (_.center) _.center.css({
                        top: _.middleTop
                    }).height(_.middleHeight);
                    if (_.right) {
                        _.right.css({
                            top: _.middleTop
                        }).height(_.middleHeight);
                        _.rightCollapse.css({
                            top: _.middleTop
                        }).height(_.middleHeight)
                    }
                } else if (_.dragtype == "bottomresize") {
                    _.bottom.height(_.bottom.height() - _.yresize.diff);
                    _.middleHeight += _.yresize.diff;
                    _.bottomTop += _.yresize.diff;
                    _.bottom.css({
                        top: _.bottomTop
                    });
                    if (_.left) {
                        _.left.height(_.middleHeight);
                        _.leftCollapse.height(_.middleHeight)
                    }
                    if (_.center) _.center.height(_.middleHeight);
                    if (_.right) {
                        _.right.height(_.middleHeight);
                        _.rightCollapse.height(_.middleHeight)
                    }
                }
            }
            _.trigger("endResize", [{
                direction: _.dragtype ? _.dragtype.replace(/resize/, "") : "",
                diff: B
            },
            A]);
            _._setDropHandlePosition();
            _.draggingxline.hide();
            _.draggingyline.hide();
            _.xresize = _.yresize = _.dragtype = false;
            _.layout.lock.hide();
            if ($.browser.msie || $.browser.safari) $("body").unbind("selectstart");
            $(document).unbind("mousemove", _._drag);
            $(document).unbind("mouseup", _._stop);
            $("body").css("cursor", "")
        }
    })
})(jQuery);
(function($) {
    $.ligerMenu = function(_) {
        return $.ligerui.run.call(null, "ligerMenu", arguments)
    };
    $.ligerDefaults.Menu = {
        width: 120,
        top: 0,
        left: 0,
        items: null,
        shadow: true
    };
    $.ligerMethos.Menu = {};
    $.ligerui.controls.Menu = function(_) {
        $.ligerui.controls.Menu.base.constructor.call(this, null, _)
    };
    $.ligerui.controls.Menu.ligerExtend($.ligerui.core.UIComponent, {
        __getType: function() {
            return "Menu"
        },
        __idPrev: function() {
            return "Menu"
        },
        _extendMethods: function() {
            return $.ligerMethos.Menu
        },
        _render: function() {
            var _ = this,
                A = this.options;
            _.menuItemCount = 0;
            _.menus = {};
            _.menu = _.createMenu();
            _.element = _.menu[0];
            _.menu.css({
                top: A.top,
                left: A.left,
                width: A.width
            });
            A.items && $(A.items).each(function(A, $) {
                _.addItem($)
            });
            $(document).bind("click.menu", function() {
                for (var A in _.menus) {
                    var $ = _.menus[A];
                    if (!$) return;
                    $.hide();
                    if ($.shadow) $.shadow.hide()
                }
            });
            _.set(A)
        },
        show: function(B, A) {
            var $ = this,
                _ = this.options;
            if (A == undefined) A = $.menu;
            if (B && B.left != undefined) A.css({
                left: B.left
            });
            if (B && B.top != undefined) A.css({
                top: B.top
            });
            A.show();
            $.updateShadow(A)
        },
        updateShadow: function(A) {
            var $ = this,
                _ = this.options;
            if (!_.shadow) return;
            A.shadow.css({
                left: A.css("left"),
                top: A.css("top"),
                width: A.outerWidth(),
                height: A.outerHeight()
            });
            if (A.is(":visible")) A.shadow.show();
            else A.shadow.hide()
        },
        hide: function(A) {
            var $ = this,
                _ = this.options;
            if (A == undefined) A = $.menu;
            $.hideAllSubMenu(A);
            A.hide();
            $.updateShadow(A)
        },
        toggle: function() {
            var $ = this,
                _ = this.options;
            $.menu.toggle();
            $.updateShadow($.menu)
        },
        removeItem: function(B) {
            var _ = this,
                A = this.options;
            $("> .l-menu-item[menuitemid=" + B + "]", _.menu.items).remove()
        },
        setEnabled: function(B) {
            var _ = this,
                A = this.options;
            $("> .l-menu-item[menuitemid=" + B + "]", _.menu.items).removeClass("l-menu-item-disable")
        },
        setDisabled: function(B) {
            var _ = this,
                A = this.options;
            $("> .l-menu-item[menuitemid=" + B + "]", _.menu.items).addClass("l-menu-item-disable")
        },
        isEnable: function(B) {
            var _ = this,
                A = this.options;
            return !$("> .l-menu-item[menuitemid=" + B + "]", _.menu.items).hasClass("l-menu-item-disable")
        },
        getItemCount: function() {
            var _ = this,
                A = this.options;
            return $("> .l-menu-item", _.menu.items).length
        },
        addItem: function(F, E) {
            var _ = this,
                D = this.options;
            if (!F) return;
            if (E == undefined) E = _.menu;
            if (F.line) {
                E.items.append("<div class=\"l-menu-item-line\"></div>");
                return
            }
            var A = $("<div class=\"l-menu-item\"><div class=\"l-menu-item-text\"></div> </div>"),
                B = $("> .l-menu-item", E.items).length;
            E.items.append(A);
            A.attr("ligeruimenutemid", ++_.menuItemCount);
            F.id && A.attr("menuitemid", F.id);
            F.text && $(">.l-menu-item-text:first", A).html(F.text);
            F.icon && A.prepend("<div class=\"l-menu-item-icon l-icon-" + F.icon + "\"></div>");
            if (F.disable || F.disabled) A.addClass("l-menu-item-disable");
            if (F.children) {
                A.append("<div class=\"l-menu-item-arrow\"></div>");
                var C = _.createMenu(A.attr("ligeruimenutemid"));
                _.menus[A.attr("ligeruimenutemid")] = C;
                C.width(D.width);
                C.hover(null, function() {
                    if (!C.showedSubMenu) _.hide(C)
                });
                $(F.children).each(function() {
                    _.addItem(this, C)
                })
            }
            F.click && A.click(function() {
                if ($(this).hasClass("l-menu-item-disable")) return;
                F.click(F, B)
            });
            F.dblclick && A.dblclick(function() {
                if ($(this).hasClass("l-menu-item-disable")) return;
                F.dblclick(F, B)
            });
            var G = $("> .l-menu-over:first", E);
            A.hover(function() {
                if ($(this).hasClass("l-menu-item-disable")) return;
                var B = $(this).offset().top,
                    C = B - E.offset().top;
                G.css({
                    top: C
                });
                _.hideAllSubMenu(E);
                if (F.children) {
                    var A = $(this).attr("ligeruimenutemid");
                    if (!A) return;
                    if (_.menus[A]) {
                        _.show({
                            top: B,
                            left: $(this).offset().left + $(this).width() - 5
                        }, _.menus[A]);
                        E.showedSubMenu = true
                    }
                }
            }, function() {
                if ($(this).hasClass("l-menu-item-disable")) return;
                var _ = $(this).attr("ligeruimenutemid");
                if (F.children) {
                    _ = $(this).attr("ligeruimenutemid");
                    if (!_) return
                }
            })
        },
        hideAllSubMenu: function(B) {
            var _ = this,
                A = this.options;
            if (B == undefined) B = _.menu;
            $("> .l-menu-item", B.items).each(function() {
                if ($("> .l-menu-item-arrow", this).length > 0) {
                    var A = $(this).attr("ligeruimenutemid");
                    if (!A) return;
                    _.menus[A] && _.hide(_.menus[A])
                }
            });
            B.showedSubMenu = false
        },
        createMenu: function(C) {
            var _ = this,
                B = this.options,
                A = $("<div class=\"l-menu\" style=\"display:none\"><div class=\"l-menu-yline\"></div><div class=\"l-menu-over\"><div class=\"l-menu-over-l\"></div> <div class=\"l-menu-over-r\"></div></div><div class=\"l-menu-inner\"></div></div>");
            C && A.attr("ligeruiparentmenuitemid", C);
            A.items = $("> .l-menu-inner:first", A);
            A.appendTo("body");
            if (B.shadow) {
                A.shadow = $("<div class=\"l-menu-shadow\"></div>").insertAfter(A);
                _.updateShadow(A)
            }
            A.hover(null, function() {
                if (!A.showedSubMenu) $("> .l-menu-over:first", A).css({
                    top: -24
                })
            });
            if (C) _.menus[C] = A;
            else _.menus[0] = A;
            return A
        }
    });
    $.ligerui.controls.Menu.prototype.setEnable = $.ligerui.controls.Menu.prototype.setEnabled;
    $.ligerui.controls.Menu.prototype.setDisable = $.ligerui.controls.Menu.prototype.setDisabled
})(jQuery);
(function($) {
    $.fn.ligerMenuBar = function(_) {
        return $.ligerui.run.call(this, "ligerMenuBar", arguments)
    };
    $.fn.ligerGetMenuBarManager = function() {
        return $.ligerui.run.call(this, "ligerGetMenuBarManager", arguments)
    };
    $.ligerDefaults.MenuBar = {};
    $.ligerMethos.MenuBar = {};
    $.ligerui.controls.MenuBar = function(_, A) {
        $.ligerui.controls.MenuBar.base.constructor.call(this, _, A)
    };
    $.ligerui.controls.MenuBar.ligerExtend($.ligerui.core.UIComponent, {
        __getType: function() {
            return "MenuBar"
        },
        __idPrev: function() {
            return "MenuBar"
        },
        _extendMethods: function() {
            return $.ligerMethos.MenuBar
        },
        _render: function() {
            var _ = this,
                A = this.options;
            _.menubar = $(this.element);
            if (!_.menubar.hasClass("l-menubar")) _.menubar.addClass("l-menubar");
            if (A && A.items) $(A.items).each(function(A, $) {
                _.addItem($)
            });
            $(document).click(function() {
                $(".l-panel-btn-selected", _.menubar).removeClass("l-panel-btn-selected")
            });
            _.set(A)
        },
        addItem: function(D) {
            var _ = this,
                C = this.options,
                A = $("<div class=\"l-menubar-item l-panel-btn\"><span></span><div class=\"l-panel-btn-l\"></div><div class=\"l-panel-btn-r\"></div><div class=\"l-menubar-item-down\"></div></div>");
            _.menubar.append(A);
            D.id && A.attr("menubarid", D.id);
            D.text && $("span:first", A).html(D.text);
            D.disable && A.addClass("l-menubar-item-disable");
            D.click && A.click(function() {
                D.click(D)
            });
            if (D.menu) {
                var B = $.ligerMenu(D.menu);
                A.hover(function() {
                    _.actionMenu && _.actionMenu.hide();
                    var A = $(this).offset().left,
                        C = $(this).offset().top + $(this).height();
                    B.show({
                        top: C,
                        left: A
                    });
                    _.actionMenu = B;
                    $(this).addClass("l-panel-btn-over l-panel-btn-selected").siblings(".l-menubar-item").removeClass("l-panel-btn-selected")
                }, function() {
                    $(this).removeClass("l-panel-btn-over")
                })
            } else {
                A.hover(function() {
                    $(this).addClass("l-panel-btn-over")
                }, function() {
                    $(this).removeClass("l-panel-btn-over")
                });
                $(".l-menubar-item-down", A).remove()
            }
        }
    })
})(jQuery);
(function($) {
    $.ligerMessageBox = function(_) {
        return $.ligerui.run.call(null, "ligerMessageBox", arguments, {
            isStatic: true
        })
    };
    $.ligerDefaults.MessageBox = {
        isDrag: true
    };
    $.ligerMethos.MessageBox = {};
    $.ligerui.controls.MessageBox = function(_) {
        $.ligerui.controls.MessageBox.base.constructor.call(this, null, _)
    };
    $.ligerui.controls.MessageBox.ligerExtend($.ligerui.core.UIComponent, {
        __getType: function() {
            return "MessageBox"
        },
        __idPrev: function() {
            return "MessageBox"
        },
        _extendMethods: function() {
            return $.ligerMethos.MessageBox
        },
        _render: function() {
            var B = this,
                E = this.options,
                C = "";
            C += "<div class=\"l-messagebox\">";
            C += "        <div class=\"l-messagebox-lt\"></div><div class=\"l-messagebox-rt\"></div>";
            C += "        <div class=\"l-messagebox-l\"></div><div class=\"l-messagebox-r\"></div> ";
            C += "        <div class=\"l-messagebox-image\"></div>";
            C += "        <div class=\"l-messagebox-title\">";
            C += "            <div class=\"l-messagebox-title-inner\"></div>";
            C += "            <div class=\"l-messagebox-close\"></div>";
            C += "        </div>";
            C += "        <div class=\"l-messagebox-content\">";
            C += "        </div>";
            C += "        <div class=\"l-messagebox-buttons\"><div class=\"l-messagebox-buttons-inner\">";
            C += "        </div></div>";
            C += "    </div>";
            B.messageBox = $(C);
            $("body").append(B.messageBox);
            B.messageBox.close = function() {
                B._removeWindowMask();
                B.messageBox.remove()
            };
            E.width && B.messageBox.width(E.width);
            E.title && $(".l-messagebox-title-inner", B.messageBox).html(E.title);
            E.content && $(".l-messagebox-content", B.messageBox).html(E.content);
            if (E.buttons) {
                $(E.buttons).each(function(C, A) {
                    var _ = $("<div class=\"l-messagebox-btn\"><div class=\"l-messagebox-btn-l\"></div><div class=\"l-messagebox-btn-r\"></div><div class=\"l-messagebox-btn-inner\"></div></div>");
                    $(".l-messagebox-btn-inner", _).html(A.text);
                    $(".l-messagebox-buttons-inner", B.messageBox).append(_);
                    A.width && _.width(A.width);
                    A.onclick && _.click(function() {
                        A.onclick(A, C, B.messageBox)
                    })
                });
                $(".l-messagebox-buttons-inner", B.messageBox).append("<div class='l-clear'></div>")
            }
            var A = B.messageBox.width(),
                _ = 0;
            $(".l-messagebox-buttons-inner .l-messagebox-btn", B.messageBox).each(function() {
                _ += $(this).width()
            });
            $(".l-messagebox-buttons-inner", B.messageBox).css({
                marginLeft: parseInt((A - _) * 0.5)
            });
            B._applyWindowMask();
            B._applyDrag();
            B._setImage();
            var F = 0,
                G = 0,
                D = E.width || B.messageBox.width();
            if (E.left != null) F = E.left;
            else E.left = F = 0.5 * ($(window).width() - D);
            if (E.top != null) G = E.top;
            else E.top = G = 0.5 * ($(window).height() - B.messageBox.height()) + $(window).scrollTop() - 10;
            if (F < 0) E.left = F = 0;
            if (G < 0) E.top = G = 0;
            B.messageBox.css({
                left: F,
                top: G
            });
            $(".l-messagebox-btn", B.messageBox).hover(function() {
                $(this).addClass("l-messagebox-btn-over")
            }, function() {
                $(this).removeClass("l-messagebox-btn-over")
            });
            $(".l-messagebox-close", B.messageBox).hover(function() {
                $(this).addClass("l-messagebox-close-over")
            }, function() {
                $(this).removeClass("l-messagebox-close-over")
            }).click(function() {
                B.messageBox.close()
            });
            B.set(E)
        },
        close: function() {
            var $ = this,
                _ = this.options;
            this.g._removeWindowMask();
            this.messageBox.remove()
        },
        _applyWindowMask: function() {
            var _ = this,
                A = this.options;
            $(".l-window-mask").remove();
            $("<div class='l-window-mask' style='display: block;'></div>").appendTo($("body"))
        },
        _removeWindowMask: function() {
            var _ = this,
                A = this.options;
            $(".l-window-mask").remove()
        },
        _applyDrag: function() {
            var _ = this,
                A = this.options;
            if (A.isDrag && $.fn.ligerDrag) _.messageBox.ligerDrag({
                handler: ".l-messagebox-title-inner",
                animate: false
            })
        },
        _setImage: function() {
            var _ = this,
                A = this.options;
            if (A.type) if (A.type == "success" || A.type == "donne") {
                $(".l-messagebox-image", _.messageBox).addClass("l-messagebox-image-donne").show();
                $(".l-messagebox-content", _.messageBox).css({
                    paddingLeft: 64,
                    paddingBottom: 30
                })
            } else if (A.type == "error") {
                $(".l-messagebox-image", _.messageBox).addClass("l-messagebox-image-error").show();
                $(".l-messagebox-content", _.messageBox).css({
                    paddingLeft: 64,
                    paddingBottom: 30
                })
            } else if (A.type == "warn") {
                $(".l-messagebox-image", _.messageBox).addClass("l-messagebox-image-warn").show();
                $(".l-messagebox-content", _.messageBox).css({
                    paddingLeft: 64,
                    paddingBottom: 30
                })
            } else if (A.type == "question") {
                $(".l-messagebox-image", _.messageBox).addClass("l-messagebox-image-question").show();
                $(".l-messagebox-content", _.messageBox).css({
                    paddingLeft: 64,
                    paddingBottom: 40
                })
            }
        }
    });
    $.ligerMessageBox.show = function(_) {
        return $.ligerMessageBox(_)
    };
    $.ligerMessageBox.alert = function(A, _, D, C) {
        A = A || "";
        _ = _ || A;
        var B = function(_, $, A) {
                A.close();
                if (C) C(_, $, A)
            };
        p = {
            title: A,
            content: _,
            buttons: [{
                text: "\u786e\u5b9a",
                onclick: B
            }]
        };
        if (D) p.type = D;
        return $.ligerMessageBox(p)
    };
    $.ligerMessageBox.confirm = function(A, _, B) {
        var C = function(_, $, A) {
                A.close();
                if (B) B($ == 0)
            };
        p = {
            type: "question",
            title: A,
            content: _,
            buttons: [{
                text: "\u662f",
                onclick: C
            }, {
                text: "\u5426",
                onclick: C
            }]
        };
        return $.ligerMessageBox(p)
    };
    $.ligerMessageBox.success = function(A, _, B) {
        return $.ligerMessageBox.alert(A, _, "success", B)
    };
    $.ligerMessageBox.error = function(A, _, B) {
        return $.ligerMessageBox.alert(A, _, "error", B)
    };
    $.ligerMessageBox.warn = function(A, _, B) {
        return $.ligerMessageBox.alert(A, _, "warn", B)
    };
    $.ligerMessageBox.question = function(A, _) {
        return $.ligerMessageBox.alert(A, _, "question")
    }
})(jQuery);
(function($) {
    $.fn.ligerRadio = function() {
        return $.ligerui.run.call(this, "ligerRadio", arguments)
    };
    $.fn.ligerGetRadioManager = function() {
        return $.ligerui.run.call(this, "ligerGetRadioManager", arguments)
    };
    $.ligerDefaults.Radio = {
        disabled: false
    };
    $.ligerMethos.Radio = {};
    $.ligerui.controls.Radio = function(_, A) {
        $.ligerui.controls.Radio.base.constructor.call(this, _, A)
    };
    $.ligerui.controls.Radio.ligerExtend($.ligerui.controls.Input, {
        __getType: function() {
            return "Radio"
        },
        __idPrev: function() {
            return "Radio"
        },
        _extendMethods: function() {
            return $.ligerMethos.Radio
        },
        _render: function() {
            var _ = this,
                A = this.options;
            _.input = $(this.element);
            _.link = $("<a href=\"javascript:void(0)\" class=\"l-radio\"></a>");
            _.wrapper = _.input.addClass("l-hidden").wrap("<div class=\"l-radio-wrapper\"></div>").parent();
            _.wrapper.prepend(_.link);
            _.input.change(function() {
                if (this.checked) _.link.addClass("l-radio-checked");
                else _.link.removeClass("l-radio-checked");
                return true
            });
            _.link.click(function() {
                _._doclick()
            });
            _.wrapper.hover(function() {
                if (!A.disabled) $(this).addClass("l-over")
            }, function() {
                $(this).removeClass("l-over")
            });
            this.element.checked && _.link.addClass("l-radio-checked");
            if (this.element.id) $("label[for=" + this.element.id + "]").click(function() {
                _._doclick()
            });
            _.set(A)
        },
        setValue: function(A) {
            var $ = this,
                _ = this.options;
            if (!A) {
                $.input[0].checked = false;
                $.link.removeClass("l-radio-checked")
            } else {
                $.input[0].checked = true;
                $.link.addClass("l-radio-checked")
            }
        },
        getValue: function() {
            return this.input[0].checked
        },
        setEnabled: function() {
            this.input.attr("disabled", false);
            this.wrapper.removeClass("l-disabled");
            this.options.disabled = false
        },
        setDisabled: function() {
            this.input.attr("disabled", true);
            this.wrapper.addClass("l-disabled");
            this.options.disabled = true
        },
        updateStyle: function() {
            if (this.input.attr("disabled")) {
                this.wrapper.addClass("l-disabled");
                this.options.disabled = true
            }
            if (this.input[0].checked) this.link.addClass("l-checkbox-checked");
            else this.link.removeClass("l-checkbox-checked")
        },
        _doclick: function() {
            var _ = this,
                B = this.options;
            if (_.input.attr("disabled")) return false;
            _.input.trigger("click").trigger("change");
            var A;
            if (_.input[0].form) A = _.input[0].form;
            else A = document;
            $("input:radio[name=" + _.input[0].name + "]", A).not(_.input).trigger("change");
            return false
        }
    })
})(jQuery);
(function($) {
    $.fn.ligerResizable = function(_) {
        return $.ligerui.run.call(this, "ligerResizable", arguments, {
            idAttrName: "ligeruiresizableid",
            hasElement: false,
            propertyToElemnt: "target"
        })
    };
    $.fn.ligerGetResizableManager = function() {
        return $.ligerui.run.call(this, "ligerGetResizableManager", arguments, {
            idAttrName: "ligeruiresizableid",
            hasElement: false,
            propertyToElemnt: "target"
        })
    };
    $.ligerDefaults.Resizable = {
        handles: "n, e, s, w, ne, se, sw, nw",
        maxWidth: 2000,
        maxHeight: 2000,
        minWidth: 20,
        minHeight: 20,
        scope: 3,
        animate: false,
        onStartResize: function($) {},
        onResize: function($) {},
        onStopResize: function($) {},
        onEndResize: null
    };
    $.ligerui.controls.Resizable = function(_) {
        $.ligerui.controls.Resizable.base.constructor.call(this, null, _)
    };
    $.ligerui.controls.Resizable.ligerExtend($.ligerui.core.UIComponent, {
        __getType: function() {
            return "Resizable"
        },
        __idPrev: function() {
            return "Resizable"
        },
        _render: function() {
            var _ = this,
                A = this.options;
            _.target = $(A.target);
            _.set(A);
            _.target.mousemove(function(B) {
                if (A.disabled) return;
                _.dir = _._getDir(B);
                if (_.dir) _.target.css("cursor", _.dir + "-resize");
                else if (_.target.css("cursor").indexOf("-resize") > 0) _.target.css("cursor", "default");
                if (A.target.ligeruidragid) {
                    var C = $.ligerui.get(A.target.ligeruidragid);
                    if (C && _.dir) C.set("disabled", true);
                    else if (C) C.set("disabled", false)
                }
            }).mousedown(function($) {
                if (A.disabled) return;
                if (_.dir) _._start($)
            })
        },
        _rendered: function() {
            this.options.target.ligeruiresizableid = this.id
        },
        _getDir: function(C) {
            var _ = this,
                F = this.options,
                D = "",
                I = _.target.offset(),
                E = _.target.width(),
                B = _.target.height(),
                A = F.scope,
                G = C.pageX || C.screenX,
                H = C.pageY || C.screenY;
            if (H >= I.top && H < I.top + A) D += "n";
            else if (H <= I.top + B && H > I.top + B - A) D += "s";
            if (G >= I.left && G < I.left + A) D += "w";
            else if (G <= I.left + E && G > I.left + E - A) D += "e";
            if (F.handles == "all" || D == "") return D;
            if ($.inArray(D, _.handles) != -1) return D;
            return ""
        },
        _setHandles: function($) {
            if (!$) return;
            this.handles = $.replace(/(\s*)/g, "").split(",")
        },
        _createProxy: function() {
            var _ = this;
            _.proxy = $("<div class=\"l-resizable\"></div>");
            _.proxy.width(_.target.width()).height(_.target.height());
            _.proxy.attr("resizableid", _.id).appendTo("body")
        },
        _removeProxy: function() {
            var $ = this;
            if ($.proxy) {
                $.proxy.remove();
                $.proxy = null
            }
        },
        _start: function(A) {
            var _ = this,
                B = this.options;
            _._createProxy();
            _.proxy.css({
                left: _.target.offset().left,
                top: _.target.offset().top,
                position: "absolute"
            });
            _.current = {
                dir: _.dir,
                left: _.target.offset().left,
                top: _.target.offset().top,
                startX: A.pageX || A.screenX,
                startY: A.pageY || A.clientY,
                width: _.target.width(),
                height: _.target.height()
            };
            $(document).bind("selectstart.resizable", function() {
                return false
            });
            $(document).bind("mouseup.resizable", function() {
                _._stop.apply(_, arguments)
            });
            $(document).bind("mousemove.resizable", function() {
                _._drag.apply(_, arguments)
            });
            _.proxy.show();
            _.trigger("startResize", [_.current, A])
        },
        changeBy: {
            t: ["n", "ne", "nw"],
            l: ["w", "sw", "nw"],
            w: ["w", "sw", "nw", "e", "ne", "se"],
            h: ["n", "ne", "nw", "s", "se", "sw"]
        },
        _drag: function(_) {
            var $ = this,
                A = this.options;
            if (!$.current) return;
            if (!$.proxy) return;
            $.proxy.css("cursor", $.current.dir == "" ? "default" : $.current.dir + "-resize");
            var B = _.pageX || _.screenX,
                C = _.pageY || _.screenY;
            $.current.diffX = B - $.current.startX;
            $.current.diffY = C - $.current.startY;
            $._applyResize($.proxy);
            $.trigger("resize", [$.current, _])
        },
        _stop: function(A) {
            var _ = this,
                B = this.options;
            if (_.hasBind("stopResize")) {
                if (_.trigger("stopResize", [_.current, A]) != false) _._applyResize()
            } else _._applyResize();
            _._removeProxy();
            _.trigger("endResize", [_.current, A]);
            $(document).unbind("selectstart.resizable");
            $(document).unbind("mousemove.resizable");
            $(document).unbind("mouseup.resizable")
        },
        _applyResize: function(D) {
            var _ = this,
                B = this.options,
                C = {
                    left: _.current.left,
                    top: _.current.top,
                    width: _.current.width,
                    height: _.current.height
                },
                A = false;
            if (!D) {
                D = _.target;
                A = true;
                if (!isNaN(parseInt(_.target.css("top")))) C.top = parseInt(_.target.css("top"));
                else C.top = 0;
                if (!isNaN(parseInt(_.target.css("left")))) C.left = parseInt(_.target.css("left"));
                else C.left = 0
            }
            if ($.inArray(_.current.dir, _.changeBy.l) > -1) {
                C.left += _.current.diffX;
                _.current.diffLeft = _.current.diffX
            } else if (A) delete C.left;
            if ($.inArray(_.current.dir, _.changeBy.t) > -1) {
                C.top += _.current.diffY;
                _.current.diffTop = _.current.diffY
            } else if (A) delete C.top;
            if ($.inArray(_.current.dir, _.changeBy.w) > -1) {
                C.width += (_.current.dir.indexOf("w") == -1 ? 1 : -1) * _.current.diffX;
                _.current.newWidth = C.width
            } else if (A) delete C.width;
            if ($.inArray(_.current.dir, _.changeBy.h) > -1) {
                C.height += (_.current.dir.indexOf("n") == -1 ? 1 : -1) * _.current.diffY;
                _.current.newHeight = C.height
            } else if (A) delete C.height;
            if (A && B.animate) D.animate(C);
            else D.css(C)
        }
    })
})(jQuery);
(function($) {
    $.fn.ligerSpinner = function() {
        return $.ligerui.run.call(this, "ligerSpinner", arguments)
    };
    $.fn.ligerGetSpinnerManager = function() {
        return $.ligerui.run.call(this, "ligerGetSpinnerManager", arguments)
    };
    $.ligerDefaults.Spinner = {
        type: "float",
        isNegative: true,
        decimalplace: 2,
        step: 0.1,
        interval: 50,
        onChangeValue: false,
        minValue: null,
        maxValue: null,
        disabled: false
    };
    $.ligerMethos.Spinner = {};
    $.ligerui.controls.Spinner = function(_, A) {
        $.ligerui.controls.Spinner.base.constructor.call(this, _, A)
    };
    $.ligerui.controls.Spinner.ligerExtend($.ligerui.controls.Input, {
        __getType: function() {
            return "Spinner"
        },
        __idPrev: function() {
            return "Spinner"
        },
        _extendMethods: function() {
            return $.ligerMethos.Spinner
        },
        _init: function() {
            $.ligerui.controls.Spinner.base._init.call(this);
            var _ = this.options;
            if (_.type == "float") {
                _.step = 0.1;
                _.interval = 50
            } else if (_.type == "int") {
                _.step = 1;
                _.interval = 100
            } else if (_.type == "time") {
                _.step = 1;
                _.interval = 100
            }
        },
        _render: function() {
            var _ = this,
                A = this.options;
            _.interval = null;
            _.inputText = null;
            _.value = null;
            _.textFieldID = "";
            if (this.element.tagName.toLowerCase() == "input" && this.element.type && this.element.type == "text") {
                _.inputText = $(this.element);
                if (this.element.id) _.textFieldID = this.element.id
            } else {
                _.inputText = $("<input type=\"text\"/>");
                _.inputText.appendTo($(this.element))
            }
            if (_.textFieldID == "" && A.textFieldID) _.textFieldID = A.textFieldID;
            _.link = $("<div class=\"l-trigger\"><div class=\"l-spinner-up\"><div class=\"l-spinner-icon\"></div></div><div class=\"l-spinner-split\"></div><div class=\"l-spinner-down\"><div class=\"l-spinner-icon\"></div></div></div>");
            _.wrapper = _.inputText.wrap("<div class=\"l-text\"></div>").parent();
            _.wrapper.append("<div class=\"l-text-l\"></div><div class=\"l-text-r\"></div>");
            _.wrapper.append(_.link).after(_.selectBox).after(_.valueField);
            _.link.up = $(".l-spinner-up", _.link);
            _.link.down = $(".l-spinner-down", _.link);
            _.inputText.addClass("l-text-field");
            if (A.disabled) _.wrapper.addClass("l-text-disabled");
            if (!_._isVerify(_.inputText.val())) {
                _.value = _._getDefaultValue();
                _.inputText.val(_.value)
            }
            _.link.up.hover(function() {
                if (!A.disabled) $(this).addClass("l-spinner-up-over")
            }, function() {
                clearInterval(_.interval);
                $(document).unbind("selectstart.spinner");
                $(this).removeClass("l-spinner-up-over")
            }).mousedown(function() {
                if (!A.disabled) {
                    _._uping.call(_);
                    _.interval = setInterval(function() {
                        _._uping.call(_)
                    }, A.interval);
                    $(document).bind("selectstart.spinner", function() {
                        return false
                    })
                }
            }).mouseup(function() {
                clearInterval(_.interval);
                _.inputText.trigger("change").focus();
                $(document).unbind("selectstart.spinner")
            });
            _.link.down.hover(function() {
                if (!A.disabled) $(this).addClass("l-spinner-down-over")
            }, function() {
                clearInterval(_.interval);
                $(document).unbind("selectstart.spinner");
                $(this).removeClass("l-spinner-down-over")
            }).mousedown(function() {
                if (!A.disabled) {
                    _.interval = setInterval(function() {
                        _._downing.call(_)
                    }, A.interval);
                    $(document).bind("selectstart.spinner", function() {
                        return false
                    })
                }
            }).mouseup(function() {
                clearInterval(_.interval);
                _.inputText.trigger("change").focus();
                $(document).unbind("selectstart.spinner")
            });
            _.inputText.change(function() {
                var $ = _.inputText.val();
                _.value = _._getVerifyValue($);
                _.trigger("changeValue", [_.value]);
                _.inputText.val(_.value)
            }).blur(function() {
                _.wrapper.removeClass("l-text-focus")
            }).focus(function() {
                _.wrapper.addClass("l-text-focus")
            });
            _.wrapper.hover(function() {
                if (!A.disabled) _.wrapper.addClass("l-text-over")
            }, function() {
                _.wrapper.removeClass("l-text-over")
            });
            _.set(A)
        },
        _setWidth: function(_) {
            var $ = this;
            if (_ > 20) {
                $.wrapper.css({
                    width: _
                });
                $.inputText.css({
                    width: _ - 20
                })
            }
        },
        _setHeight: function(_) {
            var $ = this;
            if (_ > 10) {
                $.wrapper.height(_);
                $.inputText.height(_ - 2);
                $.link.height(_ - 4)
            }
        },
        _setDisabled: function($) {
            if ($) this.wrapper.addClass("l-text-disabled");
            else this.wrapper.removeClass("l-text-disabled")
        },
        setValue: function($) {
            this.inputText.val($)
        },
        getValue: function() {
            return this.inputText.val()
        },
        _round: function(_, B) {
            var $ = this,
                C = this.options,
                A = 1;
            for (; B > 0; A *= 10, B--);
            for (; B < 0; A /= 10, B++);
            return Math.round(_ * A) / A
        },
        _isInt: function(_) {
            var $ = this,
                A = this.options,
                B = A.isNegative ? /^-?\d+$/ : /^\d+$/;
            if (!B.test(_)) return false;
            if (parseFloat(_) != _) return false;
            return true
        },
        _isFloat: function(_) {
            var $ = this,
                A = this.options,
                B = A.isNegative ? /^-?\d+(\.\d+)?$/ : /^\d+(\.\d+)?$/;
            if (!B.test(_)) return false;
            if (parseFloat(_) != _) return false;
            return true
        },
        _isTime: function(_) {
            var $ = this,
                B = this.options,
                A = _.match(/^(\d{1,2}):(\d{1,2})$/);
            if (A == null) return false;
            if (A[1] > 24 || A[2] > 60) return false;
            return true
        },
        _isVerify: function(_) {
            var $ = this,
                A = this.options;
            if (A.type == "float") {
                if (!$._isFloat(_)) return false;
                var B = parseFloat(_);
                if (A.minValue != undefined && A.minValue > B) return false;
                if (A.maxValue != undefined && A.maxValue < B) return false;
                return true
            } else if (A.type == "int") {
                if (!$._isInt(_)) return false;
                B = parseInt(_);
                if (A.minValue != undefined && A.minValue > B) return false;
                if (A.maxValue != undefined && A.maxValue < B) return false;
                return true
            } else if (A.type == "time") return $._isTime(_);
            return false
        },
        _getVerifyValue: function(B) {
            var _ = this,
                A = this.options,
                $ = null;
            if (A.type == "float") $ = _._round(B, A.decimalplace);
            else if (A.type == "int") $ = parseInt(B);
            else if (A.type == "time") $ = B;
            if (!_._isVerify($)) return _.value;
            else return $
        },
        _isOverValue: function(A) {
            var $ = this,
                _ = this.options;
            if (_.minValue != null && _.minValue > A) return true;
            if (_.maxValue != null && _.maxValue < A) return true;
            return false
        },
        _getDefaultValue: function() {
            var $ = this,
                _ = this.options;
            if (_.type == "float" || _.type == "int") return 0;
            else if (_.type == "time") return "00:00"
        },
        _addValue: function(_) {
            var $ = this,
                A = this.options,
                B = $.inputText.val();
            B = parseFloat(B) + _;
            if ($._isOverValue(B)) return;
            $.inputText.val(B);
            $.inputText.trigger("change")
        },
        _addTime: function($) {
            var _ = this,
                B = this.options,
                C = _.inputText.val(),
                A = C.match(/^(\d{1,2}):(\d{1,2})$/);
            newminute = parseInt(A[2]) + $;
            if (newminute < 10) newminute = "0" + newminute;
            C = A[1] + ":" + newminute;
            if (_._isOverValue(C)) return;
            _.inputText.val(C);
            _.inputText.trigger("change")
        },
        _uping: function() {
            var $ = this,
                _ = this.options;
            if (_.type == "float" || _.type == "int") $._addValue(_.step);
            else if (_.type == "time") $._addTime(_.step)
        },
        _downing: function() {
            var $ = this,
                _ = this.options;
            if (_.type == "float" || _.type == "int") $._addValue(-1 * _.step);
            else if (_.type == "time") $._addTime(-1 * _.step)
        },
        _isDateTime: function(C) {
            var $ = this,
                B = this.options,
                A = C.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/);
            if (A == null) return false;
            var _ = new Date(A[1], A[3] - 1, A[4]);
            if (_ == "NaN") return false;
            return (_.getFullYear() == A[1] && (_.getMonth() + 1) == A[3] && _.getDate() == A[4])
        },
        _isLongDateTime: function(D) {
            var $ = this,
                C = this.options,
                B = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2})$/,
                A = D.match(B);
            if (A == null) return false;
            var _ = new Date(A[1], A[3] - 1, A[4], A[5], A[6]);
            if (_ == "NaN") return false;
            return (_.getFullYear() == A[1] && (_.getMonth() + 1) == A[3] && _.getDate() == A[4] && _.getHours() == A[5] && _.getMinutes() == A[6])
        }
    })
})(jQuery);
(function($) {
    $.fn.ligerTab = function(_) {
        return $.ligerui.run.call(this, "ligerTab", arguments)
    };
    $.fn.ligerGetTabManager = function() {
        return $.ligerui.run.call(this, "ligerGetTabManager", arguments)
    };
    $.ligerDefaults.Tab = {
        height: null,
        heightDiff: 0,
        changeHeightOnResize: false,
        contextmenu: true,
        dblClickToClose: false,
        dragToMove: false,
        onBeforeOverrideTabItem: null,
        onAfterOverrideTabItem: null,
        onBeforeRemoveTabItem: null,
        onAfterRemoveTabItem: null,
        onBeforeAddTabItem: null,
        onAfterAddTabItem: null,
        onBeforeSelectTabItem: null,
        onAfterSelectTabItem: null
    };
    $.ligerDefaults.TabString = {
        closeMessage: "\u5173\u95ed\u5f53\u524d\u9875",
        closeOtherMessage: "\u5173\u95ed\u5176\u4ed6",
        closeAllMessage: "\u5173\u95ed\u6240\u6709",
        reloadMessage: "\u5237\u65b0"
    };
    $.ligerMethos.Tab = {};
    $.ligerui.controls.Tab = function(_, A) {
        $.ligerui.controls.Tab.base.constructor.call(this, _, A)
    };
    $.ligerui.controls.Tab.ligerExtend($.ligerui.core.UIComponent, {
        __getType: function() {
            return "Tab"
        },
        __idPrev: function() {
            return "Tab"
        },
        _extendMethods: function() {
            return $.ligerMethos.Tab
        },
        _render: function() {
            var _ = this,
                A = this.options;
            if (A.height) _.makeFullHeight = true;
            _.tab = $(this.element);
            _.tab.addClass("l-tab");
            if (A.contextmenu && $.ligerMenu) _.tab.menu = $.ligerMenu({
                width: 100,
                items: [{
                    text: A.closeMessage,
                    id: "close",
                    click: function() {
                        _._menuItemClick.apply(_, arguments)
                    }
                }, {
                    text: A.closeOtherMessage,
                    id: "closeother",
                    click: function() {
                        _._menuItemClick.apply(_, arguments)
                    }
                }, {
                    text: A.closeAllMessage,
                    id: "closeall",
                    click: function() {
                        _._menuItemClick.apply(_, arguments)
                    }
                }, {
                    text: A.reloadMessage,
                    id: "reload",
                    click: function() {
                        _._menuItemClick.apply(_, arguments)
                    }
                }]
            });
            _.tab.content = $("<div class=\"l-tab-content\"></div>");
            $("> div", _.tab).appendTo(_.tab.content);
            _.tab.content.appendTo(_.tab);
            _.tab.links = $("<div class=\"l-tab-links\"><ul style=\"left: 0px; \"></ul></div>");
            _.tab.links.prependTo(_.tab);
            _.tab.links.ul = $("ul", _.tab.links);
            var B = $("> div[lselected=true]", _.tab.content),
                C = B.length > 0;
            _.selectedTabId = B.attr("tabid");
            $("> div", _.tab.content).each(function(I, G) {
                var A = $("<li class=\"\"><a></a><div class=\"l-tab-links-item-left\"></div><div class=\"l-tab-links-item-right\"></div></li>"),
                    E = $(this);
                if (E.attr("title")) {
                    $("> a", A).html(E.attr("title"));
                    E.attr("title", "")
                }
                var H = E.attr("tabid");
                if (H == undefined) {
                    H = _.getNewTabid();
                    E.attr("tabid", H);
                    if (E.attr("lselected")) _.selectedTabId = H
                }
                A.attr("tabid", H);
                if (!C && I == 0) _.selectedTabId = H;
                var B = E.attr("showClose");
                if (B) A.append("<div class='l-tab-links-item-close'></div>");
                $("> ul", _.tab.links).append(A);
                if (!E.hasClass("l-tab-content-item")) E.addClass("l-tab-content-item");
                if (E.find("iframe").length > 0) {
                    var D = $("iframe:first", E);
                    if (D[0].readyState != "complete") {
                        if (E.find(".l-tab-loading:first").length == 0) E.prepend("<div class='l-tab-loading' style='display:block;'></div>");
                        var F = $(".l-tab-loading:first", E);
                        D.bind("load.tab", function() {
                            F.hide()
                        })
                    }
                }
            });
            _.selectTabItem(_.selectedTabId);
            if (A.height) if (typeof(A.height) == "string" && A.height.indexOf("%") > 0) {
                _.onResize();
                if (A.changeHeightOnResize) $(window).resize(function() {
                    _.onResize.call(_)
                })
            } else _.setHeight(A.height);
            if (_.makeFullHeight) _.setContentHeight();
            $("li", _.tab.links).each(function() {
                _._addTabItemEvent($(this))
            });
            _.tab.bind("dblclick.tab", function(B) {
                if (!A.dblClickToClose) return;
                _.dblclicking = true;
                var E = (B.target || B.srcElement),
                    C = E.tagName.toLowerCase();
                if (C == "a") {
                    var F = $(E).parent().attr("tabid"),
                        D = $(E).parent().find("div.l-tab-links-item-close").length ? true : false;
                    if (D) _.removeTabItem(F)
                }
                _.dblclicking = false
            });
            _.set(A)
        },
        _applyDrag: function(A) {
            var _ = this,
                C = this.options;
            _.droptip = _.droptip || $("<div class='l-tab-drag-droptip' style='display:none'><div class='l-drop-move-up'></div><div class='l-drop-move-down'></div></div>").appendTo("body");
            var B = $(A).ligerDrag({
                revert: true,
                animate: false,
                proxy: function() {
                    var A = $(this).find("a").html();
                    _.dragproxy = $("<div class='l-tab-drag-proxy' style='display:none'><div class='l-drop-icon l-drop-no'></div></div>").appendTo("body");
                    _.dragproxy.append(A);
                    return _.dragproxy
                },
                onRendered: function() {
                    this.set("cursor", "pointer")
                },
                onStartDrag: function(B, _) {
                    if (!$(A).hasClass("l-selected")) return false;
                    if (_.button == 2) return false;
                    var C = _.srcElement || _.target;
                    if ($(C).hasClass("l-tab-links-item-close")) return false
                },
                onDrag: function(C, B) {
                    if (_.dropIn == null) _.dropIn = -1;
                    var D = _.tab.links.ul.find(">li"),
                        A = D.index(C.target);
                    D.each(function(I, E) {
                        if (A == I) return;
                        var C = I > A;
                        if (_.dropIn != -1 && _.dropIn != I) return;
                        var G = $(this).offset(),
                            D = {
                                top: G.top,
                                bottom: G.top + $(this).height(),
                                left: G.left - 10,
                                right: G.left + 10
                            };
                        if (C) {
                            D.left += $(this).width();
                            D.right += $(this).width()
                        }
                        var F = B.pageX || B.screenX,
                            H = B.pageY || B.screenY;
                        if (F > D.left && F < D.right && H > D.top && H < D.bottom) {
                            _.droptip.css({
                                left: D.left + 5,
                                top: D.top - 9
                            }).show();
                            _.dropIn = I;
                            _.dragproxy.find(".l-drop-icon").removeClass("l-drop-no").addClass("l-drop-yes")
                        } else {
                            _.dropIn = -1;
                            _.droptip.hide();
                            _.dragproxy.find(".l-drop-icon").removeClass("l-drop-yes").addClass("l-drop-no")
                        }
                    })
                },
                onStopDrag: function(C, B) {
                    if (_.dropIn > -1) {
                        var A = _.tab.links.ul.find(">li:eq(" + _.dropIn + ")").attr("tabid"),
                            D = $(C.target).attr("tabid");
                        setTimeout(function() {
                            _.moveTabItem(D, A)
                        }, 0);
                        _.dropIn = -1;
                        _.dragproxy.remove()
                    }
                    _.droptip.hide();
                    this.set("cursor", "default")
                }
            });
            return B
        },
        _setDragToMove: function(B) {
            if (!$.fn.ligerDrag) return;
            var _ = this,
                A = this.options;
            if (B) {
                if (_.drags) return;
                _.drags = _.drags || [];
                _.tab.links.ul.find(">li").each(function() {
                    _.drags.push(_._applyDrag(this))
                })
            }
        },
        moveTabItem: function(A, C) {
            var _ = this,
                E = _.tab.links.ul.find(">li[tabid=" + A + "]"),
                $ = _.tab.links.ul.find(">li[tabid=" + C + "]"),
                B = _.tab.links.ul.find(">li").index(E),
                D = _.tab.links.ul.find(">li").index($);
            if (B < D) $.after(E);
            else $.before(E)
        },
        setTabButton: function() {
            var _ = this,
                A = this.options,
                C = 0;
            $("li", _.tab.links.ul).each(function() {
                C += $(this).width() + 2
            });
            var B = _.tab.width();
            if (C > B) {
                _.tab.links.append("<div class=\"l-tab-links-left\"></div><div class=\"l-tab-links-right\"></div>");
                _.setTabButtonEven();
                return true
            } else {
                _.tab.links.ul.animate({
                    left: 0
                });
                $(".l-tab-links-left,.l-tab-links-right", _.tab.links).remove();
                return false
            }
        },
        setTabButtonEven: function() {
            var _ = this,
                A = this.options;
            $(".l-tab-links-left", _.tab.links).hover(function() {
                $(this).addClass("l-tab-links-left-over")
            }, function() {
                $(this).removeClass("l-tab-links-left-over")
            }).click(function() {
                _.moveToPrevTabItem()
            });
            $(".l-tab-links-right", _.tab.links).hover(function() {
                $(this).addClass("l-tab-links-right-over")
            }, function() {
                $(this).removeClass("l-tab-links-right-over")
            }).click(function() {
                _.moveToNextTabItem()
            })
        },
        moveToPrevTabItem: function() {
            var _ = this,
                A = this.options,
                B = $(".l-tab-links-left", _.tab.links).width(),
                D = new Array();
            $("li", _.tab.links).each(function(C, _) {
                var A = -1 * B;
                if (C > 0) A = parseInt(D[C - 1]) + $(this).prev().width() + 2;
                D.push(A)
            });
            var C = -1 * parseInt(_.tab.links.ul.css("left"));
            for (var E = 0; E < D.length - 1; E++) if (D[E] < C && D[E + 1] >= C) {
                _.tab.links.ul.animate({
                    left: -1 * parseInt(D[E])
                });
                return
            }
        },
        moveToNextTabItem: function() {
            var _ = this,
                A = this.options,
                C = $(".l-tab-links-right", _.tab).width(),
                J = 0,
                H = $("li", _.tab.links.ul);
            H.each(function() {
                J += $(this).width() + 2
            });
            var B = _.tab.width(),
                G = new Array();
            for (var I = H.length - 1; I >= 0; I--) {
                var F = J - B + C + 2;
                if (I != H.length - 1) F = parseInt(G[H.length - 2 - I]) - $(H[I + 1]).width() - 2;
                G.push(F)
            }
            var E = -1 * parseInt(_.tab.links.ul.css("left"));
            for (var D = 1; D < G.length; D++) if (G[D] <= E && G[D - 1] > E) {
                _.tab.links.ul.animate({
                    left: -1 * parseInt(G[D - 1])
                });
                return
            }
        },
        getTabItemCount: function() {
            var _ = this,
                A = this.options;
            return $("li", _.tab.links.ul).length
        },
        getSelectedTabItemID: function() {
            var _ = this,
                A = this.options;
            return $("li.l-selected", _.tab.links.ul).attr("tabid")
        },
        removeSelectedTabItem: function() {
            var $ = this,
                _ = this.options;
            $.removeTabItem($.getSelectedTabItemID())
        },
        overrideSelectedTabItem: function(A) {
            var $ = this,
                _ = this.options;
            $.overrideTabItem($.getSelectedTabItemID(), A)
        },
        overrideTabItem: function(D, K) {
            var _ = this,
                I = this.options;
            if (_.trigger("beforeOverrideTabItem", [D]) == false) return false;
            var L = K.tabid;
            if (L == undefined) L = _.getNewTabid();
            var E = K.url,
                G = K.content,
                J = K.target,
                B = K.text,
                A = K.showClose,
                H = K.height;
            if (_.isTabItemExist(L)) return;
            var F = $("li[tabid=" + D + "]", _.tab.links.ul),
                C = $(".l-tab-content-item[tabid=" + D + "]", _.tab.content);
            if (!F || !C) return;
            F.attr("tabid", L);
            C.attr("tabid", L);
            if ($("iframe", C).length == 0 && E) C.html("<iframe frameborder='0'></iframe>");
            else if (G) C.html(G);
            $("iframe", C).attr("name", L);
            if (A == undefined) A = true;
            if (A == false) $(".l-tab-links-item-close", F).remove();
            else if ($(".l-tab-links-item-close", F).length == 0) F.append("<div class='l-tab-links-item-close'></div>");
            if (B == undefined) B = L;
            if (H) C.height(H);
            $("a", F).text(B);
            $("iframe", C).attr("src", E);
            _.trigger("afterOverrideTabItem", [D])
        },
        selectTabItem: function(B) {
            var _ = this,
                A = this.options;
            if (_.trigger("beforeSelectTabItem", [B]) == false) return false;
            _.selectedTabId = B;
            $("> .l-tab-content-item[tabid=" + B + "]", _.tab.content).show().siblings().hide();
            $("li[tabid=" + B + "]", _.tab.links.ul).addClass("l-selected").siblings().removeClass("l-selected");
            _.trigger("afterSelectTabItem", [B])
        },
        moveToLastTabItem: function() {
            var _ = this,
                A = this.options,
                D = 0;
            $("li", _.tab.links.ul).each(function() {
                D += $(this).width() + 2
            });
            var C = _.tab.width();
            if (D > C) {
                var B = $(".l-tab-links-right", _.tab.links).width();
                _.tab.links.ul.animate({
                    left: -1 * (D - C + B + 2)
                })
            }
        },
        isTabItemExist: function(B) {
            var _ = this,
                A = this.options;
            return $("li[tabid=" + B + "]", _.tab.links.ul).length > 0
        },
        addTabItem: function(L) {
            var _ = this,
                J = this.options;
            if (_.trigger("beforeAddTabItem", [M]) == false) return false;
            var M = L.tabid;
            if (M == undefined) M = _.getNewTabid();
            var E = L.url,
                G = L.content,
                C = L.text,
                A = L.showClose,
                I = L.height;
            if (_.isTabItemExist(M)) {
                _.selectTabItem(M);
                return
            }
            var F = $("<li><a></a><div class='l-tab-links-item-left'></div><div class='l-tab-links-item-right'></div><div class='l-tab-links-item-close'></div></li>"),
                D = $("<div class='l-tab-content-item'><div class='l-tab-loading' style='display:block;'></div><iframe frameborder='0'></iframe></div>"),
                K = $("div:first", D),
                H = $("iframe:first", D);
            if (_.makeFullHeight) {
                var B = _.tab.height() - _.tab.links.height();
                D.height(B)
            }
            F.attr("tabid", M);
            D.attr("tabid", M);
            if (E) H.attr("name", M).attr("id", M).attr("src", E).bind("load.tab", function() {
                K.hide();
                if (L.callback) L.callback()
            });
            else {
                H.remove();
                K.remove()
            }
            if (G) D.html(G);
            else if (L.target) D.append(L.target);
            if (A == undefined) A = true;
            if (A == false) $(".l-tab-links-item-close", F).remove();
            if (C == undefined) C = M;
            if (I) D.height(I);
            $("a", F).text(C);
            _.tab.links.ul.append(F);
            _.tab.content.append(D);
            _.selectTabItem(M);
            if (_.setTabButton()) _.moveToLastTabItem();
            _._addTabItemEvent(F);
            if (J.dragToMove && $.fn.ligerDrag) {
                _.drags = _.drags || [];
                F.each(function() {
                    _.drags.push(_._applyDrag(this))
                })
            }
            _.trigger("afterAddTabItem", [M])
        },
        _addTabItemEvent: function(B) {
            var _ = this,
                A = this.options;
            B.click(function() {
                var A = $(this).attr("tabid");
                _.selectTabItem(A)
            });
            _.tab.menu && _._addTabItemContextMenuEven(B);
            $(".l-tab-links-item-close", B).hover(function() {
                $(this).addClass("l-tab-links-item-close-over")
            }, function() {
                $(this).removeClass("l-tab-links-item-close-over")
            }).click(function() {
                var A = $(this).parent().attr("tabid");
                _.removeTabItem(A)
            })
        },
        removeTabItem: function(C) {
            var _ = this,
                A = this.options;
            if (_.trigger("beforeRemoveTabItem", [C]) == false) return false;
            var B = $("li[tabid=" + C + "]", _.tab.links.ul).hasClass("l-selected");
            if (B) {
                $(".l-tab-content-item[tabid=" + C + "]", _.tab.content).prev().show();
                $("li[tabid=" + C + "]", _.tab.links.ul).prev().addClass("l-selected").siblings().removeClass("l-selected")
            }
            $(".l-tab-content-item[tabid=" + C + "]", _.tab.content).remove();
            $("li[tabid=" + C + "]", _.tab.links.ul).remove();
            _.setTabButton();
            _.trigger("afterRemoveTabItem", [C])
        },
        addHeight: function(A) {
            var _ = this,
                B = this.options,
                $ = _.tab.height() + A;
            _.setHeight($)
        },
        setHeight: function(_) {
            var $ = this,
                A = this.options;
            $.tab.height(_);
            $.setContentHeight()
        },
        setContentHeight: function() {
            var _ = this,
                B = this.options,
                A = _.tab.height() - _.tab.links.height();
            _.tab.content.height(A);
            $("> .l-tab-content-item", _.tab.content).height(A)
        },
        getNewTabid: function() {
            var $ = this,
                _ = this.options;
            $.getnewidcount = $.getnewidcount || 0;
            return "tabitem" + (++$.getnewidcount)
        },
        getTabidList: function(D, _) {
            var B = this,
                C = this.options,
                A = [];
            $("> li", B.tab.links.ul).each(function() {
                if ($(this).attr("tabid") && $(this).attr("tabid") != D && (!_ || $(".l-tab-links-item-close", this).length > 0)) A.push($(this).attr("tabid"))
            });
            return A
        },
        removeOther: function(D, C) {
            var A = this,
                B = this.options,
                _ = A.getTabidList(D, true);
            $(_).each(function() {
                A.removeTabItem(this)
            })
        },
        reload: function(F) {
            var _ = this,
                C = this.options,
                B = $(".l-tab-content-item[tabid=" + F + "]"),
                D = $(".l-tab-loading:first", B),
                A = $("iframe:first", B),
                E = $(A).attr("src");
            D.show();
            A.attr("src", E).unbind("load.tab").bind("load.tab", function() {
                D.hide()
            })
        },
        removeAll: function(C) {
            var A = this,
                B = this.options,
                _ = A.getTabidList(null, true);
            $(_).each(function() {
                A.removeTabItem(this)
            })
        },
        onResize: function() {
            var _ = this,
                A = this.options;
            if (!A.height || typeof(A.height) != "string" || A.height.indexOf("%") == -1) return false;
            if (_.tab.parent()[0].tagName.toLowerCase() == "body") {
                var B = $(window).height();
                B -= parseInt(_.tab.parent().css("paddingTop"));
                B -= parseInt(_.tab.parent().css("paddingBottom"));
                _.height = A.heightDiff + B * parseFloat(_.height) * 0.01
            } else _.height = A.heightDiff + (_.tab.parent().height() * parseFloat(A.height) * 0.01);
            _.tab.height(_.height);
            _.setContentHeight()
        },
        _menuItemClick: function(A) {
            var $ = this,
                _ = this.options;
            if (!A.id || !$.actionTabid) return;
            switch (A.id) {
            case "close":
                $.removeTabItem($.actionTabid);
                $.actionTabid = null;
                break;
            case "closeother":
                $.removeOther($.actionTabid);
                break;
            case "closeall":
                $.removeAll();
                $.actionTabid = null;
                break;
            case "reload":
                $.selectTabItem($.actionTabid);
                $.reload($.actionTabid);
                break
            }
        },
        _addTabItemContextMenuEven: function(B) {
            var _ = this,
                A = this.options;
            B.bind("contextmenu", function(A) {
                if (!_.tab.menu) return;
                _.actionTabid = B.attr("tabid");
                _.tab.menu.show({
                    top: A.pageY,
                    left: A.pageX
                });
                if ($(".l-tab-links-item-close", this).length == 0) _.tab.menu.setDisabled("close");
                else _.tab.menu.setEnabled("close");
                return false
            })
        }
    })
})(jQuery);
(function($) {
    $.fn.ligerTextBox = function() {
        return $.ligerui.run.call(this, "ligerTextBox", arguments)
    };
    $.fn.ligerGetTextBoxManager = function() {
        return $.ligerui.run.call(this, "ligerGetTextBoxManager", arguments)
    };
    $.ligerDefaults.TextBox = {
        onChangeValue: null,
        width: null,
        disabled: false,
        value: null,
        nullText: null,
        digits: false,
        number: false
    };
    $.ligerui.controls.TextBox = function(_, A) {
        $.ligerui.controls.TextBox.base.constructor.call(this, _, A)
    };
    $.ligerui.controls.TextBox.ligerExtend($.ligerui.controls.Input, {
        __getType: function() {
            return "TextBox"
        },
        __idPrev: function() {
            return "TextBox"
        },
        _init: function() {
            $.ligerui.controls.TextBox.base._init.call(this);
            var _ = this,
                A = this.options;
            if (!A.width) A.width = $(_.element).width();
            if ($(this.element).attr("readonly")) A.disabled = true
        },
        _render: function() {
            var _ = this,
                A = this.options;
            _.inputText = $(this.element);
            _.wrapper = _.inputText.wrap("<div class=\"l-text\"></div>").parent();
            _.wrapper.append("<div class=\"l-text-l\"></div><div class=\"l-text-r\"></div>");
            if (!_.inputText.hasClass("l-text-field")) _.inputText.addClass("l-text-field");
            this._setEvent();
            _.set(A);
            _.checkValue()
        },
        _getValue: function() {
            return this.inputText.val()
        },
        _setNullText: function() {
            this.checkNotNull()
        },
        checkValue: function() {
            var _ = this,
                A = this.options,
                $ = _.inputText.val();
            if (A.number && !/^-?(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/.test($) || A.digits && !/^\d+$/.test($)) {
                _.inputText.val(_.value || 0);
                return
            }
            _.value = $
        },
        checkNotNull: function() {
            var $ = this,
                _ = this.options;
            if (_.nullText && !_.disabled) if (!$.inputText.val()) $.inputText.addClass("l-text-field-null").val(_.nullText)
        },
        _setEvent: function() {
            var _ = this,
                A = this.options;
            _.inputText.bind("blur.textBox", function() {
                _.trigger("blur");
                _.checkNotNull();
                _.checkValue();
                _.wrapper.removeClass("l-text-focus")
            }).bind("focus.textBox", function() {
                _.trigger("focus");
                if (A.nullText) if ($(this).hasClass("l-text-field-null")) $(this).removeClass("l-text-field-null").val("");
                _.wrapper.addClass("l-text-focus")
            }).change(function() {
                _.trigger("changeValue", [this.value])
            });
            _.wrapper.hover(function() {
                _.trigger("mouseOver");
                _.wrapper.addClass("l-text-over")
            }, function() {
                _.trigger("mouseOut");
                _.wrapper.removeClass("l-text-over")
            })
        },
        _setDisabled: function($) {
            if ($) {
                this.inputText.attr("readonly", "readonly");
                this.wrapper.addClass("l-text-disabled")
            } else {
                this.inputText.removeAttr("readonly");
                this.wrapper.removeClass("l-text-disabled")
            }
        },
        _setWidth: function($) {
            if ($ > 20) {
                this.wrapper.css({
                    width: $
                });
                this.inputText.css({
                    width: $ - 4
                })
            }
        },
        _setHeight: function($) {
            if ($ > 10) {
                this.wrapper.height($);
                this.inputText.height($ - 2)
            }
        },
        _setValue: function($) {
            if ($ != null) this.inputText.val($)
        },
        _setLabel: function(B) {
            var _ = this,
                A = this.options;
            if (!_.labelwrapper) {
                _.labelwrapper = _.wrapper.wrap("<div class=\"l-labeltext\"></div>").parent();
                var C = $("<div class=\"l-text-label\" style=\"float:left;\">" + B + ":&nbsp</div>");
                _.labelwrapper.prepend(C);
                _.wrapper.css("float", "left");
                if (!A.labelWidth) A.labelWidth = C.width();
                else _._setLabelWidth(A.labelWidth);
                C.height(_.wrapper.height());
                if (A.labelAlign) _._setLabelAlign(A.labelAlign);
                _.labelwrapper.append("<br style=\"clear:both;\" />");
                _.labelwrapper.width(A.labelWidth + A.width + 2)
            } else _.labelwrapper.find(".l-text-label").html(B + ":&nbsp")
        },
        _setLabelWidth: function(A) {
            var $ = this,
                _ = this.options;
            if (!$.labelwrapper) return;
            $.labelwrapper.find(".l-text-label").width(A)
        },
        _setLabelAlign: function(A) {
            var $ = this,
                _ = this.options;
            if (!$.labelwrapper) return;
            $.labelwrapper.find(".l-text-label").css("text-align", A)
        },
        updateStyle: function() {
            var $ = this,
                _ = this.options;
            if ($.inputText.attr("disabled") || $.inputText.attr("readonly")) {
                $.wrapper.addClass("l-text-disabled");
                $.options.disabled = true
            } else {
                $.wrapper.removeClass("l-text-disabled");
                $.options.disabled = false
            }
            if ($.inputText.hasClass("l-text-field-null") && $.inputText.val() != _.nullText) $.inputText.removeClass("l-text-field-null");
            $.checkValue()
        }
    })
})(jQuery);
(function($) {
    $.ligerTip = function(_) {
        return $.ligerui.run.call(null, "ligerTip", arguments)
    };
    $.fn.ligerTip = function(_) {
        this.each(function() {
            var A = $.extend({}, $.ligerDefaults.ElementTip, _ || {});
            A.target = A.target || this;
            if (A.auto || _ == undefined) {
                if (!A.content) {
                    A.content = this.title;
                    if (A.removeTitle) $(this).removeAttr("title")
                }
                A.content = A.content || this.title;
                $(this).bind("mouseover.tip", function() {
                    A.x = $(this).offset().left + $(this).width() + (A.distanceX || 0);
                    A.y = $(this).offset().top + (A.distanceY || 0);
                    $.ligerTip(A)
                }).bind("mouseout.tip", function() {
                    var _ = $.ligerui.managers[this.ligeruitipid];
                    if (_) _.remove()
                })
            } else {
                if (A.target.ligeruitipid) return;
                A.x = $(this).offset().left + $(this).width() + (A.distanceX || 0);
                A.y = $(this).offset().top + (A.distanceY || 0);
                A.x = A.x || 0;
                A.y = A.y || 0;
                $.ligerTip(A)
            }
        });
        return $.ligerui.get(this, "ligeruitipid")
    };
    $.fn.ligerHideTip = function(_) {
        return this.each(function() {
            var B = _ || {};
            if (B.isLabel == undefined) B.isLabel = this.tagName.toLowerCase() == "label" && $(this).attr("for") != null;
            var C = this;
            if (B.isLabel) {
                var D = $("#" + $(this).attr("for"));
                if (D.length == 0) return;
                C = D[0]
            }
            var A = $.ligerui.managers[C.ligeruitipid];
            if (A) A.remove()
        }).unbind("mouseover.tip").unbind("mouseout.tip")
    };
    $.fn.ligerGetTipManager = function() {
        return $.ligerui.get(this)
    };
    $.ligerDefaults = $.ligerDefaults || {};
    $.ligerDefaults.HideTip = {};
    $.ligerDefaults.Tip = {
        content: null,
        callback: null,
        width: 150,
        height: null,
        x: 0,
        y: 0,
        appendIdTo: null,
        target: null,
        auto: null,
        removeTitle: true
    };
    $.ligerDefaults.ElementTip = {
        distanceX: 1,
        distanceY: -3,
        auto: null,
        removeTitle: true
    };
    $.ligerMethos.Tip = {};
    $.ligerui.controls.Tip = function(_) {
        $.ligerui.controls.Tip.base.constructor.call(this, null, _)
    };
    $.ligerui.controls.Tip.ligerExtend($.ligerui.core.UIComponent, {
        __getType: function() {
            return "Tip"
        },
        __idPrev: function() {
            return "Tip"
        },
        _extendMethods: function() {
            return $.ligerMethos.Tip
        },
        _render: function() {
            var _ = this,
                A = this.options,
                B = $("<div class=\"l-verify-tip\"><div class=\"l-verify-tip-corner\"></div><div class=\"l-verify-tip-content\"></div></div>");
            _.tip = B;
            _.tip.attr("id", _.id);
            if (A.content) {
                $("> .l-verify-tip-content:first", B).html(A.content);
                B.appendTo("body")
            } else return;
            B.css({
                left: A.x,
                top: A.y
            }).show();
            A.width && $("> .l-verify-tip-content:first", B).width(A.width - 8);
            A.height && $("> .l-verify-tip-content:first", B).width(A.height);
            eee = A.appendIdTo;
            if (A.appendIdTo) A.appendIdTo.attr("ligerTipId", _.id);
            if (A.target) {
                $(A.target).attr("ligerTipId", _.id);
                A.target.ligeruitipid = _.id
            }
            A.callback && A.callback(B);
            _.set(A)
        },
        _setContent: function(_) {
            $("> .l-verify-tip-content:first", this.tip).html(_)
        },
        remove: function() {
            if (this.options.appendIdTo) this.options.appendIdTo.removeAttr("ligerTipId");
            if (this.options.target) {
                $(this.options.target).removeAttr("ligerTipId");
                this.options.target.ligeruitipid = null
            }
            this.tip.remove()
        }
    })
})(jQuery);
(function($) {
    $.fn.ligerToolBar = function(_) {
        return $.ligerui.run.call(this, "ligerToolBar", arguments)
    };
    $.fn.ligerGetToolBarManager = function() {
        return $.ligerui.run.call(this, "ligerGetToolBarManager", arguments)
    };
    $.ligerDefaults.ToolBar = {};
    $.ligerMethos.ToolBar = {};
    $.ligerui.controls.ToolBar = function(_, A) {
        $.ligerui.controls.ToolBar.base.constructor.call(this, _, A)
    };
    $.ligerui.controls.ToolBar.ligerExtend($.ligerui.core.UIComponent, {
        __getType: function() {
            return "ToolBar"
        },
        __idPrev: function() {
            return "ToolBar"
        },
        _extendMethods: function() {
            return $.ligerMethos.ToolBar
        },
        _render: function() {
            var _ = this,
                A = this.options;
            _.toolBar = $(this.element);
            _.toolBar.addClass("l-toolbar");
            _.set(A)
        },
        _setItems: function(A) {
            var _ = this;
            $(A).each(function(A, $) {
                _.addItem($)
            })
        },
        addItem: function(C) {
            var _ = this,
                B = this.options;
            if (C.line) {
                _.toolBar.append("<div class=\"l-bar-separator\"></div>");
                return
            }
            var A = $("<div class=\"l-toolbar-item l-panel-btn\"><span></span><div class=\"l-panel-btn-l\"></div><div class=\"l-panel-btn-r\"></div></div>");
            _.toolBar.append(A);
            C.id && A.attr("toolbarid", C.id);
            if (C.img) {
                A.append("<img src='" + C.img + "' />");
                A.addClass("l-toolbar-item-hasicon")
            } else if (C.icon) {
                A.append("<div class='l-icon l-icon-" + C.icon + "'></div>");
                A.addClass("l-toolbar-item-hasicon")
            }
            C.text && $("span:first", A).html(C.text);
            C.disable && A.addClass("l-toolbar-item-disable");
            C.click && A.click(function() {
                C.click(C)
            });
            A.hover(function() {
                $(this).addClass("l-panel-btn-over")
            }, function() {
                $(this).removeClass("l-panel-btn-over")
            })
        }
    })
})(jQuery);
(function($) {
    $.fn.ligerTree = function(_) {
        return $.ligerui.run.call(this, "ligerTree", arguments)
    };
    $.fn.ligerGetTreeManager = function() {
        return $.ligerui.run.call(this, "ligerGetTreeManager", arguments)
    };
    $.ligerDefaults.Tree = {
        url: null,
        data: null,
        checkbox: true,
        autoCheckboxEven: true,
        parentIcon: "folder",
        childIcon: "leaf",
        textFieldName: "text",
        attribute: ["id", "url"],
        treeLine: true,
        nodeWidth: 90,
        statusName: "__status",
        isLeaf: null,
        single: false,
        onBeforeExpand: function() {},
        onContextmenu: function() {},
        onExpand: function() {},
        onBeforeCollapse: function() {},
        onCollapse: function() {},
        onBeforeSelect: function() {},
        onSelect: function() {},
        onBeforeCancelSelect: function() {},
        onCancelselect: function() {},
        onCheck: function() {},
        onSuccess: function() {},
        onError: function() {},
        onClick: function() {},
        idFieldName: "id",
        parentIDFieldName: null,
        topParentIDValue: 0,
        onBeforeAppend: function() {},
        onAppend: function() {},
        onAfterAppend: function() {},
        slide: true,
        iconFieldName: "icon",
        nodeDraggable: false,
        nodeDraggingRender: null,
        btnClickToToggleOnly: true
    };
    $.ligerui.controls.Tree = function(_, A) {
        $.ligerui.controls.Tree.base.constructor.call(this, _, A)
    };
    $.ligerui.controls.Tree.ligerExtend($.ligerui.core.UIComponent, {
        _init: function() {
            $.ligerui.controls.Tree.base._init.call(this);
            var _ = this,
                A = this.options;
            if (A.single) A.autoCheckboxEven = false
        },
        _render: function() {
            var _ = this,
                A = this.options;
            _.set(A, true);
            _.tree = $(_.element);
            _.tree.addClass("l-tree");
            _.sysAttribute = ["isexpand", "ischecked", "href", "style"];
            _.loading = $("<div class='l-tree-loading'></div>");
            _.tree.after(_.loading);
            _.data = [];
            _.maxOutlineLevel = 1;
            _.treedataindex = 0;
            _._applyTree();
            _._setTreeEven();
            _.set(A, false)
        },
        _setTreeLine: function($) {
            if ($) this.tree.removeClass("l-tree-noline");
            else this.tree.addClass("l-tree-noline")
        },
        _setUrl: function($) {
            if ($) this.loadData(null, $)
        },
        _setData: function($) {
            if ($) this.append(null, $)
        },
        setData: function($) {
            this.set("data", $)
        },
        getData: function() {
            return this.data
        },
        hasChildren: function($) {
            if (this.options.isLeaf) return this.options.isLeaf($);
            return $.children ? true : false
        },
        getParent: function(A, D) {
            var _ = this;
            A = _.getNodeDom(A);
            var B = _.getParentTreeItem(A, D);
            if (!B) return null;
            var C = $(B).attr("treedataindex");
            return _._getDataNodeByTreeDataIndex(C)
        },
        getParentTreeItem: function(A, B) {
            var _ = this;
            A = _.getNodeDom(A);
            var C = $(A);
            if (C.parent().hasClass("l-tree")) return null;
            if (B == undefined) {
                if (C.parent().parent("li").length == 0) return null;
                return C.parent().parent("li")[0]
            }
            var D = parseInt(C.attr("outlinelevel")),
                E = C;
            for (var F = D - 1; F >= B; F--) E = E.parent().parent("li");
            return E[0]
        },
        getChecked: function() {
            var _ = this,
                B = this.options;
            if (!this.options.checkbox) return null;
            var A = [];
            $(".l-checkbox-checked", _.tree).parent().parent("li").each(function() {
                var B = parseInt($(this).attr("treedataindex"));
                A.push({
                    target: this,
                    data: _._getDataNodeByTreeDataIndex(_.data, B)
                })
            });
            return A
        },
        
        //返回所有 nodes
        getAllNodes: function() {
            var _ = this,  B = this.options;            
            var A = [];
            $(".l-box").parent().parent("li").each(function() {
                var S = parseInt($(this).attr("treedataindex"));
                A.push({
                    target: this,
                    data: _._getDataNodeByTreeDataIndex(_.data, S)
                })
            });
            return A
        },
        
        getSelected: function() {
            var _ = this,
                C = this.options,
                B = {};
            B.target = $(".l-selected", _.tree).parent("li")[0];
            if (B.target) {
                var A = parseInt($(B.target).attr("treedataindex"));
                B.data = _._getDataNodeByTreeDataIndex(_.data, A);
                return B
            }
            return null
        },
        upgrade: function(A) {
            var _ = this,
                B = this.options;
            $(".l-note", A).each(function() {
                $(this).removeClass("l-note").addClass("l-expandable-open")
            });
            $(".l-note-last", A).each(function() {
                $(this).removeClass("l-note-last").addClass("l-expandable-open")
            });
            $("." + _._getChildNodeClassName(), A).each(function() {
                $(this).removeClass(_._getChildNodeClassName()).addClass(_._getParentNodeClassName(true))
            })
        },
        demotion: function(B) {
            var _ = this,
                C = this.options;
            if (!B && B[0].tagName.toLowerCase() != "li") return;
            var A = $(B).hasClass("l-last");
            $(".l-expandable-open", B).each(function() {
                $(this).removeClass("l-expandable-open").addClass(A ? "l-note-last" : "l-note")
            });
            $(".l-expandable-close", B).each(function() {
                $(this).removeClass("l-expandable-close").addClass(A ? "l-note-last" : "l-note")
            });
            $("." + _._getParentNodeClassName(true), B).each(function() {
                $(this).removeClass(_._getParentNodeClassName(true)).addClass(_._getChildNodeClassName())
            })
        },
        collapseAll: function() {
            var _ = this,
                A = this.options;
            $(".l-expandable-open", _.tree).click()
        },
        expandAll: function() {
            var _ = this,
                A = this.options;
            $(".l-expandable-close", _.tree).click()
        },
        loadData: function(B, E, C) {
            var _ = this,
                D = this.options;
            _.loading.show();
            var A = C ? "post" : "get";
            C = C || [];
            $.ajax({
                type: A,
                url: E,
                data: C,
                dataType: "json",
                success: function($) {
                    if (!$) return;
                    _.loading.hide();
                    _.append(B, $);
                    _.trigger("success", [$])
                },
                error: function(C, $, B) {
                    try {
                        _.loading.hide();
                        _.trigger("error", [C, $, B])
                    } catch (A) {}
                }
            })
        },
        clear: function() {
            var _ = this,
                A = this.options;
            $("> li", _.tree).each(function() {
                _.remove(this)
            })
        },
        getNodeDom: function(B) {
            var _ = this,
                A = this.options;
            if (B == null) return B;
            if (typeof(B) == "string" || typeof(B) == "number") return $("li[treedataindex=" + B + "]", _.tree).get(0);
            else if (typeof(B) == "object" && "treedataindex" in B) return _.getNodeDom(B["treedataindex"]);
            return B
        },
        remove: function(C) {
            var _ = this,
                D = this.options;
            C = _.getNodeDom(C);
            var A = parseInt($(C).attr("treedataindex")),
                E = _._getDataNodeByTreeDataIndex(_.data, A);
            if (E) _._setTreeDataStatus([E], "delete");
            var B = _.getParentTreeItem(C);
            if (D.checkbox) _._setParentCheckboxStatus($(C));
            $(C).remove();
            _._updateStyle(B ? $("ul:first", B) : _.tree)
        },
        _updateStyle: function(A) {
            var _ = this,
                B = this.options,
                D = $(" > li", A),
                C = D.length;
            if (!C) return;
            D.each(function(B, A) {
                if (B == 0 && !$(this).hasClass("l-first")) $(this).addClass("l-first");
                if (B == C - 1 && !$(this).hasClass("l-last")) $(this).addClass("l-last");
                if (B == 0 && B == C - 1) $(this).addClass("l-onlychild");
                $("> div .l-note,> div .l-note-last", this).removeClass("l-note l-note-last").addClass(B == C - 1 ? "l-note-last" : "l-note");
                _._setTreeItem(this, {
                    isLast: B == C - 1
                })
            })
        },
        update: function(E, C) {
            var _ = this,
                B = this.options;
            E = _.getNodeDom(E);
            var A = parseInt($(E).attr("treedataindex"));
            nodedata = _._getDataNodeByTreeDataIndex(_.data, A);
            for (var D in C) {
                nodedata[D] = C[D];
                if (D == B.textFieldName) $("> .l-body > span", E).text(C[D])
            }
        },
        append: function(J, B, F, H) {
            var C = this,
                K = this.options;
            J = C.getNodeDom(J);
            if (C.trigger("beforeAppend", [J, B]) == false) return false;
            if (!B || !B.length) return false;
            if (K.idFieldName && K.parentIDFieldName) B = C.arrayToTree(B, K.idFieldName, K.parentIDFieldName);
            C._addTreeDataIndexToData(B);
            C._setTreeDataStatus(B, "add");
            if (F != null) F = C.getNodeDom(F);
            C.trigger("append", [J, B]);
            C._appendData(J, B);
            if (J == null) {
                var L = C._getTreeHTMLByData(B, 1, [], true);
                L[L.length - 1] = L[0] = "";
                if (F != null) {
                    $(F)[H ? "after" : "before"](L.join(""));
                    C._updateStyle(J ? $("ul:first", J) : C.tree)
                } else {
                    if ($("> li:last", C.tree).length > 0) C._setTreeItem($("> li:last", C.tree)[0], {
                        isLast: false
                    });
                    C.tree.append(L.join(""))
                }
                $(".l-body", C.tree).hover(function() {
                    $(this).addClass("l-over")
                }, function() {
                    $(this).removeClass("l-over")
                });
                C._upadteTreeWidth();
                C.trigger("afterAppend", [J, B]);
                return
            }
            var I = $(J),
                E = parseInt(I.attr("outlinelevel")),
                D = $("> ul", I).length > 0;
            if (!D) {
                I.append("<ul class='l-children'></ul>");
                C.upgrade(J)
            }
            var _ = [];
            for (var G = 1; G <= E - 1; G++) {
                var A = $(C.getParentTreeItem(J, G));
                _.push(A.hasClass("l-last"))
            }
            _.push(I.hasClass("l-last"));
            L = C._getTreeHTMLByData(B, E + 1, _, true);
            L[L.length - 1] = L[0] = "";
            if (F != null) {
                $(F)[H ? "after" : "before"](L.join(""));
                C._updateStyle(J ? $("ul:first", J) : C.tree)
            } else {
                if ($("> .l-children > li:last", I).length > 0) C._setTreeItem($("> .l-children > li:last", I)[0], {
                    isLast: false
                });
                $(">.l-children", J).append(L.join(""))
            }
            C._upadteTreeWidth();
            $(">.l-children .l-body", J).hover(function() {
                $(this).addClass("l-over")
            }, function() {
                $(this).removeClass("l-over")
            });
            C.trigger("afterAppend", [J, B])
        },
        cancelSelect: function(E) {
            var _ = this,
                D = this.options,
                F = _.getNodeDom(E),
                C = $(F),
                B = parseInt(C.attr("treedataindex")),
                G = _._getDataNodeByTreeDataIndex(_.data, B),
                A = $(">div:first", C);
            if (D.checkbox) $(".l-checkbox", A).removeClass("l-checkbox-checked").addClass("l-checkbox-unchecked");
            else A.removeClass("l-selected");
            _.trigger("cancelSelect", [{
                data: G,
                target: C[0]
            }])
        },
        selectNode: function(F) {
            var _ = this,
                D = this.options,
                G = null;
            if (typeof(F) == "function") G = F;
            else if (typeof(F) == "object") {
                var C = $(F),
                    B = parseInt(C.attr("treedataindex")),
                    E = _._getDataNodeByTreeDataIndex(_.data, B),
                    A = $(">div:first", C);
                if (D.checkbox) $(".l-checkbox", A).removeClass("l-checkbox-unchecked").addClass("l-checkbox-checked");
                else A.addClass("l-selected");
                _.trigger("select", [{
                    data: E,
                    target: C[0]
                }]);
                return
            } else G = function($) {
                if (!$[D.idFieldName]) return false;
                return $[D.idFieldName].toString() == F.toString()
            };
            $("li", _.tree).each(function() {
                var B = $(this),
                    A = parseInt(B.attr("treedataindex")),
                    C = _._getDataNodeByTreeDataIndex(_.data, A);
                if (G(C, A)) _.selectNode(this);
                else _.cancelSelect(this)
            })
        },
        getTextByID: function($) {
            var _ = this,
                A = this.options,
                B = _.getDataByID($);
            if (!B) return null;
            return B[A.textFieldName]
        },
        getDataByID: function(_) {
            var A = this,
                B = this.options,
                C = null;
            $("li", A.tree).each(function() {
                if (C) return;
                var E = $(this),
                    D = parseInt(E.attr("treedataindex")),
                    F = A._getDataNodeByTreeDataIndex(A.data, D);
                if (F[B.idFieldName].toString() == _.toString()) C = F
            });
            return C
        },
        arrayToTree: function(B, $, D) {
            if (!B || !B.length) return [];
            var A = [],
                G = {},
                F = B.length;
            for (var H = 0; H < F; H++) {
                var C = B[H];
                G[C[$]] = C
            }
            for (H = 0; H < F; H++) {
                var _ = B[H],
                    E = G[_[D]];
                if (!E) {
                    A.push(_);
                    continue
                }
                E.children = E.children || [];
                E.children.push(_)
            }
            return A
        },
        _getDataNodeByTreeDataIndex: function(C, _) {
            var $ = this,
                B = this.options;
            for (var D = 0; D < C.length; D++) {
                if (C[D].treedataindex == _) return C[D];
                if (C[D].children) {
                    var A = $._getDataNodeByTreeDataIndex(C[D].children, _);
                    if (A) return A
                }
            }
            return null
        },
        _setTreeDataStatus: function(C, A) {
            var _ = this,
                B = this.options;
            $(C).each(function() {
                this[B.statusName] = A;
                if (this.children) _._setTreeDataStatus(this.children, A)
            })
        },
        _addTreeDataIndexToData: function(B) {
            var _ = this,
                A = this.options;
            $(B).each(function() {
                if (this.treedataindex != undefined) return;
                this.treedataindex = _.treedataindex++;
                if (this.children) _._addTreeDataIndexToData(this.children)
            })
        },
        _addToNodes: function(B) {
            var _ = this,
                A = this.options;
            _.nodes = _.nodes || [];
            if ($.inArray(B, _.nodes) == -1) _.nodes.push(B);
            if (B.children) $(B.children).each(function(A, $) {
                _._addToNodes($)
            })
        },
        _appendData: function(B, D) {
            var _ = this,
                C = this.options,
                A = parseInt($(B).attr("treedataindex")),
                E = _._getDataNodeByTreeDataIndex(_.data, A);
            if (_.treedataindex == undefined) _.treedataindex = 0;
            if (E && E.children == undefined) E.children = [];
            $(D).each(function(A, $) {
                if (E) E.children[E.children.length] = $;
                else _.data[_.data.length] = $;
                _._addToNodes($)
            })
        },
        _setTreeItem: function(C, E) {
            var _ = this,
                D = this.options;
            if (!E) return;
            C = _.getNodeDom(C);
            var A = $(C),
                B = parseInt(A.attr("outlinelevel"));
            if (E.isLast != undefined) if (E.isLast == true) {
                A.removeClass("l-last").addClass("l-last");
                $("> div .l-note", A).removeClass("l-note").addClass("l-note-last");
                $(".l-children li", A).find(".l-box:eq(" + (B - 1) + ")").removeClass("l-line")
            } else if (E.isLast == false) {
                A.removeClass("l-last");
                $("> div .l-note-last", A).removeClass("l-note-last").addClass("l-note");
                $(".l-children li", A).find(".l-box:eq(" + (B - 1) + ")").removeClass("l-line").addClass("l-line")
            }
        },
        _upadteTreeWidth: function() {
            var $ = this,
                A = this.options,
                _ = $.maxOutlineLevel * 22;
            if (A.checkbox) _ += 22;
            if (A.parentIcon || A.childIcon) _ += 22;
            _ += A.nodeWidth;
            $.tree.width(_)
        },
        _getChildNodeClassName: function() {
            var $ = this,
                _ = this.options;
            return "l-tree-icon-" + _.childIcon
        },
        _getParentNodeClassName: function(B) {
            var $ = this,
                _ = this.options,
                A = "l-tree-icon-" + _.parentIcon;
            if (B) A += "-open";
            return A
        },
        _getTreeHTMLByData: function(C, B, _, K) {
            var A = this,
                N = this.options;
            if (A.maxOutlineLevel < B) A.maxOutlineLevel = B;
            _ = _ || [];
            B = B || 1;
            var L = [];
            if (!K) L.push("<ul class=\"l-children\" style=\"display:none\">");
            else L.push("<ul class='l-children'>");
            for (var I = 0; I < C.length; I++) {
                var E = I == 0,
                    H = I == C.length - 1,
                    J = true,
                    D = C[I];
                if (D.isexpand == false || D.isexpand == "false") J = false;
                L.push("<li ");
                if (D.treedataindex != undefined) L.push("treedataindex=\"" + D.treedataindex + "\" ");
                if (J) L.push("isexpand=" + D.isexpand + " ");
                L.push("outlinelevel=" + B + " ");
                for (var F = 0; F < A.sysAttribute.length; F++) if ($(this).attr(A.sysAttribute[F])) C[dataindex][A.sysAttribute[F]] = $(this).attr(A.sysAttribute[F]);
                for (F = 0; F < N.attribute.length; F++) if (D[N.attribute[F]]) L.push(N.attribute[F] + "=\"" + D[N.attribute[F]] + "\" ");
                L.push("class=\"");
                E && L.push("l-first ");
                H && L.push("l-last ");
                E && H && L.push("l-onlychild ");
                L.push("\"");
                L.push(">");
                L.push("<div class=\"l-body\">");
                for (var G = 0; G <= B - 2; G++) if (_[G]) L.push("<div class=\"l-box\"></div>");
                else L.push("<div class=\"l-box l-line\"></div>");
                if (A.hasChildren(D)) {
                    if (J) L.push("<div class=\"l-box l-expandable-open\"></div>");
                    else L.push("<div class=\"l-box l-expandable-close\"></div>");
                    
                    if (N.checkbox) 
                    	if(D.readWriteFlag==2){ //如果可读可写
                    		if (D.ischecked) L.push("<div class=\"l-box l-checkbox l-checkbox-checked\"></div>");
                    		else L.push("<div class=\"l-box l-checkbox l-checkbox-unchecked\"></div>");
                    	}
                    if (N.parentIcon) {
                        L.push("<div class=\"l-box l-tree-icon ");
                        L.push(A._getParentNodeClassName(N.parentIcon ? true : false) + " ");
                        if (N.iconFieldName && D[N.iconFieldName]) L.push("l-tree-icon-none");
                        L.push("\">");
                        if (N.iconFieldName && D[N.iconFieldName]) L.push("<img src=\"" + D[N.iconFieldName] + "\" />");
                        L.push("</div>")
                    }
                } else {
                    if (H) L.push("<div class=\"l-box l-note-last\"></div>");
                    else L.push("<div class=\"l-box l-note\"></div>");
                    
                    if (N.checkbox)
                    	if(D.readWriteFlag==2){ //如果可读可写
                    		if (D.ischecked) L.push("<div class=\"l-box l-checkbox l-checkbox-checked\"></div>");
                    		else L.push("<div class=\"l-box l-checkbox l-checkbox-unchecked\"></div>");
                    	}	
                    if (N.childIcon) {
                        L.push("<div class=\"l-box l-tree-icon ");
                        L.push(A._getChildNodeClassName() + " ");
                        if (N.iconFieldName && D[N.iconFieldName]) L.push("l-tree-icon-none");
                        L.push("\">");
                        if (N.iconFieldName && D[N.iconFieldName]) L.push("<img src=\"" + D[N.iconFieldName] + "\" />");
                        L.push("</div>")
                    }
                }
				if(D.readWriteFlag==2){ //如果可读可写 
					L.push("<span>" + D[N.textFieldName] + "</span></div>");
				}else { //否则字体变灰
                	L.push("<span style='color:grey'>" + D[N.textFieldName] + "</span></div>");
                }
                
                if (A.hasChildren(D)) {
                    var M = [];
                    for (G = 0; G < _.length; G++) M.push(_[G]);
                    M.push(H);
                    L.push(A._getTreeHTMLByData(D.children, B + 1, M, J).join(""))
                }
                L.push("</li>")
            }
            L.push("</ul>");
            return L
        },
        _getDataByTreeHTML: function(A) {
            var _ = this,
                B = this.options,
                C = [];
            $("> li", A).each(function(F, A) {
                var D = C.length;
                C[D] = {
                    treedataindex: _.treedataindex++
                };
                C[D][B.textFieldName] = $("> span,> a", this).html();
                for (var E = 0; E < _.sysAttribute.length; E++) if ($(this).attr(_.sysAttribute[E])) C[D][_.sysAttribute[E]] = $(this).attr(_.sysAttribute[E]);
                for (E = 0; E < B.attribute.length; E++) if ($(this).attr(B.attribute[E])) C[D][B.attribute[E]] = $(this).attr(B.attribute[E]);
                if ($("> ul", this).length > 0) C[D].children = _._getDataByTreeHTML($("> ul", this))
            });
            return C
        },
        _applyTree: function() {
            var _ = this,
                A = this.options;
            _.data = _._getDataByTreeHTML(_.tree);
            var B = _._getTreeHTMLByData(_.data, 1, [], true);
            B[B.length - 1] = B[0] = "";
            _.tree.html(B.join(""));
            _._upadteTreeWidth();
            $(".l-body", _.tree).hover(function() {
                $(this).addClass("l-over")
            }, function() {
                $(this).removeClass("l-over")
            })
        },
        _applyTreeEven: function(A) {
            var _ = this,
                B = this.options;
            $("> .l-body", A).hover(function() {
                $(this).addClass("l-over")
            }, function() {
                $(this).removeClass("l-over")
            })
        },
        _getSrcElementByEvent: function(B) {
            var _ = this,
                F = (B.target || B.srcElement),
                E = F.tagName.toLowerCase(),
                D = $(F).parents().add(F),
                G = function(_) {
                    for (var A = D.length - 1; A >= 0; A--) if ($(D[A]).hasClass(_)) return D[A];
                    return null
                };
            if (D.index(this.element) == -1) return {
                out: true
            };
            var C = {
                tree: G("l-tree"),
                node: G("l-body"),
                checkbox: G("l-checkbox"),
                icon: G("l-tree-icon"),
                text: E == "span"
            };
            if (C.node) {
                var A = parseInt($(C.node).parent().attr("treedataindex"));
                C.data = _._getDataNodeByTreeDataIndex(_.data, A)
            }
            return C
        },
        _setTreeEven: function() {
            var _ = this,
                A = this.options;
            if (_.hasBind("contextmenu")) _.tree.bind("contextmenu", function(B) {
                var D = (B.target || B.srcElement),
                    C = null;
                if (D.tagName.toLowerCase() == "a" || D.tagName.toLowerCase() == "span" || $(D).hasClass("l-box")) C = $(D).parent().parent();
                else if ($(D).hasClass("l-body")) C = $(D).parent();
                else if (D.tagName.toLowerCase() == "li") C = $(D);
                if (!C) return;
                var A = parseInt(C.attr("treedataindex")),
                    E = _._getDataNodeByTreeDataIndex(_.data, A);
                return _.trigger("contextmenu", [{
                    data: E,
                    target: C[0]
                },
                B])
            });
            _.tree.click(function(C) {
                var H = (C.target || C.srcElement),
                    D = null;
                if (H.tagName.toLowerCase() == "a" || H.tagName.toLowerCase() == "span" || $(H).hasClass("l-box")) D = $(H).parent().parent();
                else if ($(H).hasClass("l-body")) D = $(H).parent();
                else D = $(H);
                if (!D) return;
                var B = parseInt(D.attr("treedataindex")),
                    I = _._getDataNodeByTreeDataIndex(_.data, B),
                    F = $("div.l-body:first", D).find("div.l-expandable-open:first,div.l-expandable-close:first"),
                    E = $(H).hasClass("l-expandable-open") || $(H).hasClass("l-expandable-close");
                if (!$(H).hasClass("l-checkbox") && !E) if ($(">div:first", D).hasClass("l-selected")) {
                    if (_.trigger("beforeCancelSelect", [{
                        data: I,
                        target: D[0]
                    }]) == false) return false;
                    $(">div:first", D).removeClass("l-selected");
                    _.trigger("cancelSelect", [{
                        data: I,
                        target: D[0]
                    }])
                } else {
                    if (_.trigger("beforeSelect", [{
                        data: I,
                        target: D[0]
                    }]) == false) return false;
                    $(".l-body", _.tree).removeClass("l-selected");
                    $(">div:first", D).addClass("l-selected");
                    _.trigger("select", [{
                        data: I,
                        target: D[0]
                    }])
                }
                if ($(H).hasClass("l-checkbox")) {
                    if (A.autoCheckboxEven) {
                        if ($(H).hasClass("l-checkbox-unchecked")) {
                            $(H).removeClass("l-checkbox-unchecked").addClass("l-checkbox-checked");
                            $(".l-children .l-checkbox", D).removeClass("l-checkbox-incomplete l-checkbox-unchecked").addClass("l-checkbox-checked");
                            _.trigger("check", [{
                                data: I,
                                target: D[0]
                            },
                            true])
                        } else if ($(H).hasClass("l-checkbox-checked")) {
                            $(H).removeClass("l-checkbox-checked").addClass("l-checkbox-unchecked");
                            $(".l-children .l-checkbox", D).removeClass("l-checkbox-incomplete l-checkbox-checked").addClass("l-checkbox-unchecked");
                            _.trigger("check", [{
                                data: I,
                                target: D[0]
                            },
                            false])
                        } else if ($(H).hasClass("l-checkbox-incomplete")) {
                            $(H).removeClass("l-checkbox-incomplete").addClass("l-checkbox-checked");
                            $(".l-children .l-checkbox", D).removeClass("l-checkbox-incomplete l-checkbox-unchecked").addClass("l-checkbox-checked");
                            _.trigger("check", [{
                                data: I,
                                target: D[0]
                            },
                            true])
                        }
                        // 不掉用向上遍历方法  保证指向下操作 不向上操作
                        //_._setParentCheckboxStatus(D) 
                    } else if ($(H).hasClass("l-checkbox-unchecked")) {
                        $(H).removeClass("l-checkbox-unchecked").addClass("l-checkbox-checked");
                        if (A.single) $(".l-checkbox", _.tree).not(H).removeClass("l-checkbox-checked").addClass("l-checkbox-unchecked");
                        _.trigger("check", [{
                            data: I,
                            target: D[0]
                        },
                        true])
                    } else if ($(H).hasClass("l-checkbox-checked")) {
                        $(H).removeClass("l-checkbox-checked").addClass("l-checkbox-unchecked");
                        _.trigger("check", [{
                            data: I,
                            target: D[0]
                        },
                        false])
                    }
                } else if (F.hasClass("l-expandable-open") && (!A.btnClickToToggleOnly || E)) {
                    if (_.trigger("beforeCollapse", [{
                        data: I,
                        target: D[0]
                    }]) == false) return false;
                    F.removeClass("l-expandable-open").addClass("l-expandable-close");
                    if (A.slide) $("> .l-children", D).slideToggle("fast");
                    else $("> .l-children", D).toggle();
                    $("> div ." + _._getParentNodeClassName(true), D).removeClass(_._getParentNodeClassName(true)).addClass(_._getParentNodeClassName());
                    _.trigger("collapse", [{
                        data: I,
                        target: D[0]
                    }])
                } else if (F.hasClass("l-expandable-close") && (!A.btnClickToToggleOnly || E)) {
                    if (_.trigger("beforeExpand", [{
                        data: I,
                        target: D[0]
                    }]) == false) return false;
                    F.removeClass("l-expandable-close").addClass("l-expandable-open");
                    var G = function() {
                            _.trigger("expand", [{
                                data: I,
                                target: D[0]
                            }])
                        };
                    if (A.slide) $("> .l-children", D).slideToggle("fast", G);
                    else {
                        $("> .l-children", D).toggle();
                        G()
                    }
                    $("> div ." + _._getParentNodeClassName(), D).removeClass(_._getParentNodeClassName()).addClass(_._getParentNodeClassName(true))
                }
                _.trigger("click", [{
                    data: I,
                    target: D[0]
                }])
            });
            if ($.fn.ligerDrag && A.nodeDraggable) {
                _.nodeDroptip = $("<div class='l-drag-nodedroptip' style='display:none'></div>").appendTo("body");
                _.tree.ligerDrag({
                    revert: true,
                    animate: false,
                    proxyX: 20,
                    proxyY: 20,
                    proxy: function(H, D) {
                        var G = _._getSrcElementByEvent(D);
                        if (G.node) {
                            var B = "dragging";
                            if (A.nodeDraggingRender) B = A.nodeDraggingRender(H.draggingNodes, H, _);
                            else {
                                B = "";
                                var E = false;
                                for (var I in H.draggingNodes) {
                                    var C = H.draggingNodes[I];
                                    if (E) B += ",";
                                    B += C.text;
                                    E = true
                                }
                            }
                            var F = $("<div class='l-drag-proxy' style='display:none'><div class='l-drop-icon l-drop-no'></div>" + B + "</div>").appendTo("body");
                            return F
                        }
                    },
                    onRevert: function() {
                        return false
                    },
                    onRendered: function() {
                        this.set("cursor", "default");
                        _.children[this.id] = this
                    },
                    onStartDrag: function(B, $) {
                        if ($.button == 2) return false;
                        this.set("cursor", "default");
                        var C = _._getSrcElementByEvent($);
                        if (C.checkbox) return false;
                        if (A.checkbox) {
                            var D = _.getChecked();
                            this.draggingNodes = [];
                            for (var E in D) this.draggingNodes.push(D[E].data);
                            if (!this.draggingNodes || !this.draggingNodes.length) return false
                        } else this.draggingNodes = [C.data];
                        this.draggingNode = C.data;
                        this.set("cursor", "move");
                        _.nodedragging = true;
                        this.validRange = {
                            top: _.tree.offset().top,
                            bottom: _.tree.offset().top + _.tree.height(),
                            left: _.tree.offset().left,
                            right: _.tree.offset().left + _.tree.width()
                        }
                    },
                    onDrag: function(P, C) {
                        var I = this.draggingNode;
                        if (!I) return false;
                        var N = this.draggingNodes ? this.draggingNodes : [I];
                        if (_.nodeDropIn == null) _.nodeDropIn = -1;
                        var G = C.pageX,
                            J = C.pageY,
                            E = false,
                            A = this.validRange;
                        if (G < A.left || G > A.right || J > A.bottom || J < A.top) {
                            _.nodeDropIn = -1;
                            _.nodeDroptip.hide();
                            this.proxy.find(".l-drop-icon:first").removeClass("l-drop-yes l-drop-add").addClass("l-drop-no");
                            return
                        }
                        for (var K = 0, F = _.nodes.length; K < F; K++) {
                            var O = _.nodes[K],
                                B = O["treedataindex"];
                            if (I["treedataindex"] == B) E = true;
                            if ($.inArray(O, N) != -1) continue;
                            var M = E ? true : false;
                            if (_.nodeDropIn != -1 && _.nodeDropIn != B) continue;
                            var L = $("li[treedataindex=" + B + "] div:first", _.tree),
                                Q = L.offset(),
                                D = {
                                    top: Q.top,
                                    bottom: Q.top + L.height(),
                                    left: _.tree.offset().left,
                                    right: _.tree.offset().left + _.tree.width()
                                };
                            if (G > D.left && G < D.right && J > D.top && J < D.bottom) {
                                var H = Q.top;
                                if (M) H += L.height();
                                _.nodeDroptip.css({
                                    left: D.left,
                                    top: H,
                                    width: D.right - D.left
                                }).show();
                                _.nodeDropIn = B;
                                _.nodeDropDir = M ? "bottom" : "top";
                                if (J > D.top + 7 && J < D.bottom - 7) {
                                    this.proxy.find(".l-drop-icon:first").removeClass("l-drop-no l-drop-yes").addClass("l-drop-add");
                                    _.nodeDroptip.hide();
                                    _.nodeDropInParent = true
                                } else {
                                    this.proxy.find(".l-drop-icon:first").removeClass("l-drop-no l-drop-add").addClass("l-drop-yes");
                                    _.nodeDroptip.show();
                                    _.nodeDropInParent = false
                                }
                                break
                            } else if (_.nodeDropIn != -1) {
                                _.nodeDropIn = -1;
                                _.nodeDropInParent = false;
                                _.nodeDroptip.hide();
                                this.proxy.find(".l-drop-icon:first").removeClass("l-drop-yes  l-drop-add").addClass("l-drop-no")
                            }
                        }
                    },
                    onStopDrag: function(D, B) {
                        var C = this.draggingNodes;
                        _.nodedragging = false;
                        if (_.nodeDropIn != -1) {
                            for (var F = 0; F < C.length; F++) {
                                var E = C[F].children;
                                if (E) C = $.grep(C, function(_, B) {
                                    var A = $.inArray(_, E) == -1;
                                    return A
                                })
                            }
                            for (F in C) {
                                var A = C[F];
                                if (_.nodeDropInParent) {
                                    _.remove(A);
                                    _.append(_.nodeDropIn, [A])
                                } else {
                                    _.remove(A);
                                    _.append(_.getParent(_.nodeDropIn), [A], _.nodeDropIn, _.nodeDropDir == "bottom")
                                }
                            }
                            _.nodeDropIn = -1
                        }
                        _.nodeDroptip.hide();
                        this.set("cursor", "default")
                    }
                })
            }
        },
        _setParentCheckboxStatus: function(B) {
            var _ = this,
                C = this.options,
                A = $(".l-checkbox-unchecked", B.parent()).length == 0,
                D = $(".l-checkbox-checked", B.parent()).length == 0;
            if (A) B.parent().prev().find(".l-checkbox").removeClass("l-checkbox-unchecked l-checkbox-incomplete").addClass("l-checkbox-checked");
            else if (D) B.parent().prev().find("> .l-checkbox").removeClass("l-checkbox-checked l-checkbox-incomplete").addClass("l-checkbox-unchecked");
            else B.parent().prev().find("> .l-checkbox").removeClass("l-checkbox-unchecked l-checkbox-checked").addClass("l-checkbox-incomplete");
            if (B.parent().parent("li").length > 0) _._setParentCheckboxStatus(B.parent().parent("li"))
        }
    })
})(jQuery);
(function($) {
    var _ = $.ligerui;
    _.windowCount = 0;
    $.ligerWindow = function($) {
        return _.run.call(null, "ligerWindow", arguments, {
            isStatic: true
        })
    };
    $.ligerWindow.show = function(_) {
        return $.ligerWindow(_)
    };
    $.ligerDefaults.Window = {
        showClose: true,
        showMax: true,
        showToggle: true,
        showMin: true,
        title: "window",
        load: false,
        onLoaded: null,
        modal: false
    };
    $.ligerMethos.Window = {};
    _.controls.Window = function($) {
        _.controls.Window.base.constructor.call(this, null, $)
    };
    _.controls.Window.ligerExtend(_.core.Win, {
        __getType: function() {
            return "Window"
        },
        __idPrev: function() {
            return "Window"
        },
        _extendMethods: function() {
            return $.ligerMethos.Window
        },
        _render: function() {
            var B = this,
                D = this.options;
            B.window = $("<div class=\"l-window\"><div class=\"l-window-header\"><div class=\"l-window-header-buttons\"><div class=\"l-window-toggle\"></div><div class=\"l-window-max\"></div><div class=\"l-window-close\"></div><div class=\"l-clear\"></div></div><div class=\"l-window-header-inner\"></div></div><div class=\"l-window-content\"></div></div>");
            B.element = B.window[0];
            B.window.content = $(".l-window-content", B.window);
            B.window.header = $(".l-window-header", B.window);
            B.window.buttons = $(".l-window-header-buttons:first", B.window);
            if (D.url) {
                if (D.load) {
                    B.window.content.load(D.url, function() {
                        B.trigger("loaded")
                    });
                    B.window.content.addClass("l-window-content-scroll")
                } else {
                    var C = $("<iframe frameborder='0' src='" + D.url + "'></iframe>"),
                        F = "ligeruiwindow" + _.windowCount++;
                    if (D.name) F = D.name;
                    C.attr("name", F).attr("id", F);
                    D.framename = F;
                    C.appendTo(B.window.content);
                    B.iframe = C
                }
            } else if (D.content) {
                var A = $("<div>" + D.content + "</div>");
                A.appendTo(B.window.content)
            } else if (D.target) {
                B.window.content.append(D.target);
                D.target.show()
            }
            this.mask();
            B.active();
            $("body").append(B.window);
            B.set({
                width: D.width,
                height: D.height
            });
            var E = 0,
                G = 0;
            if (D.left != null) E = D.left;
            else D.left = E = 0.5 * ($(window).width() - B.window.width());
            if (D.top != null) G = D.top;
            else D.top = G = 0.5 * ($(window).height() - B.window.height()) + $(window).scrollTop() - 10;
            if (E < 0) D.left = E = 0;
            if (G < 0) D.top = G = 0;
            B.set(D);
            D.framename && $(">iframe", B.window.content).attr("name", D.framename);
            if (!D.showToggle) $(".l-window-toggle", B.window).remove();
            if (!D.showMax) $(".l-window-max", B.window).remove();
            if (!D.showClose) $(".l-window-close", B.window).remove();
            B._saveStatus();
            if ($.fn.ligerDrag) B.draggable = B.window.drag = B.window.ligerDrag({
                handler: ".l-window-header-inner",
                onStartDrag: function() {
                    B.active()
                },
                onStopDrag: function() {
                    B._saveStatus()
                },
                animate: false
            });
            if ($.fn.ligerResizable) {
                B.resizeable = B.window.resizable = B.window.ligerResizable({
                    onStartResize: function() {
                        B.active();
                        $(".l-window-max", B.window).removeClass("l-window-regain")
                    },
                    onStopResize: function(_, $) {
                        var C = 0,
                            A = 0;
                        if (!isNaN(parseInt(B.window.css("top")))) C = parseInt(B.window.css("top"));
                        if (!isNaN(parseInt(B.window.css("left")))) A = parseInt(B.window.css("left"));
                        if (_.diffTop) B.window.css({
                            top: C + _.diffTop
                        });
                        if (_.diffLeft) B.window.css({
                            left: A + _.diffLeft
                        });
                        if (_.newWidth) B.window.width(_.newWidth);
                        if (_.newHeight) B.window.content.height(_.newHeight - 28);
                        B._saveStatus();
                        return false
                    }
                });
                B.window.append("<div class='l-btn-nw-drop'></div>")
            }
            $(".l-window-toggle", B.window).click(function() {
                if ($(this).hasClass("l-window-toggle-close")) {
                    B.collapsed = false;
                    $(this).removeClass("l-window-toggle-close")
                } else {
                    B.collapsed = true;
                    $(this).addClass("l-window-toggle-close")
                }
                B.window.content.slideToggle()
            }).hover(function() {
                if (B.window.drag) B.window.drag.set("disabled", true)
            }, function() {
                if (B.window.drag) B.window.drag.set("disabled", false)
            });
            $(".l-window-close", B.window).click(function() {
                if (B.trigger("close") == false) return false;
                B.window.hide();
                _.win.removeTask(B)
            }).hover(function() {
                if (B.window.drag) B.window.drag.set("disabled", true)
            }, function() {
                if (B.window.drag) B.window.drag.set("disabled", false)
            });
            $(".l-window-max", B.window).click(function() {
                if ($(this).hasClass("l-window-regain")) {
                    if (B.trigger("regain") == false) return false;
                    B.window.width(B._width).css({
                        left: B._left,
                        top: B._top
                    });
                    B.window.content.height(B._height - 28);
                    $(this).removeClass("l-window-regain")
                } else {
                    if (B.trigger("max") == false) return false;
                    B.window.width($(window).width() - 2).css({
                        left: 0,
                        top: 0
                    });
                    B.window.content.height($(window).height() - 28).show();
                    $(this).addClass("l-window-regain")
                }
            })
        },
        _saveStatus: function() {
            var $ = this;
            $._width = $.window.width();
            $._height = $.window.height();
            var A = 0,
                _ = 0;
            if (!isNaN(parseInt($.window.css("top")))) A = parseInt($.window.css("top"));
            if (!isNaN(parseInt($.window.css("left")))) _ = parseInt($.window.css("left"));
            $._top = A;
            $._left = _
        },
        min: function() {
            this.window.hide();
            this.minimize = true;
            this.actived = false
        },
        _setShowMin: function(C) {
            var A = this,
                B = this.options;
            if (C) {
                if (!A.winmin) {
                    A.winmin = $("<div class=\"l-window-min\"></div>").prependTo(A.window.buttons).click(function() {
                        A.min()
                    });
                    _.win.addTask(A)
                }
            } else if (A.winmin) {
                A.winmin.remove();
                A.winmin = null
            }
        },
        _setLeft: function($) {
            if ($ != null) this.window.css({
                left: $
            })
        },
        _setTop: function($) {
            if ($ != null) this.window.css({
                top: $
            })
        },
        _setWidth: function($) {
            if ($ > 0) this.window.width($)
        },
        _setHeight: function($) {
            if ($ > 28) this.window.content.height($ - 28)
        },
        _setTitle: function(_) {
            if (_) $(".l-window-header-inner", this.window.header).html(_)
        },
        _setUrl: function(A) {
            var $ = this,
                _ = this.options;
            _.url = A;
            if (_.load) $.window.content.html("").load(_.url, function() {
                if ($.trigger("loaded") == false) return false
            });
            else if ($.jiframe) $.jiframe.attr("src", _.url)
        },
        hide: function() {
            var $ = this,
                _ = this.options;
            this.unmask();
            this.window.hide()
        },
        show: function() {
            var $ = this,
                _ = this.options;
            this.mask();
            this.window.show()
        },
        remove: function() {
            var $ = this,
                _ = this.options;
            this.unmask();
            this.window.remove()
        },
        active: function() {
            var A = this,
                D = this.options;
            if (A.minimize) {
                var C = A._width,
                    B = A._height,
                    E = A._left,
                    F = A._top;
                if (A.maximum) {
                    C = $(window).width();
                    B = $(window).height();
                    E = F = 0;
                    if (_.win.taskbar) {
                        B -= _.win.taskbar.outerHeight();
                        if (_.win.top) F += _.win.taskbar.outerHeight()
                    }
                }
                A.set({
                    width: C,
                    height: B,
                    left: E,
                    top: F
                })
            }
            A.actived = true;
            A.minimize = false;
            _.win.setFront(A);
            A.show();
            _.win.setFront(this)
        },
        setUrl: function($) {
            return _setUrl($)
        }
    })
})(jQuery)