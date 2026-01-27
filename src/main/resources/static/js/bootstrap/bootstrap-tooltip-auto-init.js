/**
 * Bootstrap 3/4 Tooltip Auto Init
 * 自动初始化所有带 data-toggle="tooltip" 的元素，支持动态添加的元素
 * 依赖：jQuery + Bootstrap 3/4 Tooltip 组件
 * 作者：liufei8904
 * 版本：1.0.0
 */
(function($, window, document, undefined) {
    "use strict";

    // 检查依赖是否存在
    if (typeof $ === 'undefined') {
        console.error('[Tooltip Auto Init] 请先引入 jQuery（Bootstrap 3/4 必需依赖）');
        return;
    }
    if (typeof $.fn.tooltip === 'undefined') {
        console.error('[Tooltip Auto Init] 请先引入 Bootstrap 3/4 核心 JS 文件（需包含 Tooltip 组件）');
        return;
    }

    // 保存原始的 getCalculatedOffset 方法
    const originalGetCalculatedOffset = $.fn.tooltip.Constructor.prototype.getCalculatedOffset;

    // 重写 getCalculatedOffset 方法以支持表格单元格的智能定位
    $.fn.tooltip.Constructor.prototype.getCalculatedOffset = function(placement, pos, actualWidth, actualHeight) {
        const $element = this.$element;

        // 仅对表格单元格（td[role="gridcell"]）进行特殊处理
        if ($element.is('td[role="gridcell"]')) {
            // 获取单元格的文本对齐方式
            const textAlign = $element.css('text-align') || 'left';
            // 获取单元格内的文本内容宽度
            const textWidth = $element.text().trim().length * (parseInt($element.css('font-size')) || 14) * 0.6;
            // 获取单元格的内边距
            const paddingLeft = parseInt($element.css('padding-left')) || 0;
            const paddingRight = parseInt($element.css('padding-right')) || 0;

            // 计算内容的实际位置
            let contentLeft = pos.left;

            // 根据文本对齐方式调整内容左侧位置
            if (textAlign === 'right') {
                // 右对齐：内容从右侧内边距开始
                contentLeft = pos.left + pos.width - paddingRight - textWidth;
            } else if (textAlign === 'center') {
                // 居中对齐：内容居中
                contentLeft = pos.left + (pos.width / 2) - (textWidth / 2);
            } else {
                // 左对齐：内容从左侧内边距开始
                contentLeft = pos.left + paddingLeft;
            }

            // 根据placement返回调整后的位置
            return placement === 'bottom' ? {
                top: pos.top + pos.height,
                left: contentLeft + (textWidth / 2) - (actualWidth / 2)
            } : placement === 'top' ? {
                top: pos.top - actualHeight,
                left: contentLeft + (textWidth / 2) - (actualWidth / 2)
            } : placement === 'left' ? {
                top: pos.top + (pos.height / 2) - (actualHeight / 2),
                left: contentLeft - actualWidth
            } : /* placement == 'right' */ {
                top: pos.top + (pos.height / 2) - (actualHeight / 2),
                left: contentLeft + textWidth
            };
        }

        // 非表格单元格，使用原始方法
        return originalGetCalculatedOffset.call(this, placement, pos, actualWidth, actualHeight);
    };

    // 初始化 Tooltip 的核心方法（避免重复初始化）
    const initTooltips = function () {
        // 筛选所有未初始化的 Tooltip 元素
        $('[data-toggle="tooltip"], td[role="gridcell"][title]').each(function () {
            const $this = $(this);
            // 检查是否已通过 Bootstrap 初始化（避免重复绑定）
            if (!$this.data('bs.tooltip')) {
                // 获取元素上的自定义配置（优先级高于默认）
                const config = {
                    container: $this.data('container') || 'body',
                    html: $this.data('html') || true,
                    placement: $this.data('placement') || 'auto',
                    trigger: $this.data('trigger') || 'hover focus',
                    delay: $this.data('delay') || 300
                };

                // 初始化 Tooltip
                $this.tooltip(config);
            }
        });
    };

    // 1. 页面 DOM 加载完成后，初始化静态元素
    $(document).ready(function() {
        initTooltips();
    });

    // 2. 使用MutationObserver监听DOM变化，处理动态添加的元素
    if (window.MutationObserver) {
        const observer = new MutationObserver(function(mutations) {
            let shouldInit = false;

            mutations.forEach(function(mutation) {
                // 处理节点添加
                if (mutation.addedNodes.length > 0) {
                    shouldInit = true;
                }

                // 处理属性变化（特别是title属性）
                if (mutation.type === 'attributes' && mutation.attributeName === 'title') {
                    // 如果修改了title属性，确保这个元素是我们关心的类型
                    const $target = $(mutation.target);
                    if ($target.is('[data-toggle="tooltip"]') || $target.is('td[role="gridcell"]')) {
                        // 如果已存在tooltip实例，先销毁再重新初始化
                        if ($target.data('bs.tooltip')) {
                            $target.tooltip('destroy');
                        }
                        shouldInit = true;
                    }
                }

                // 处理字符数据变化（内容变化）
                if (mutation.type === 'characterData' || mutation.type === 'childList') {
                    const $target = $(mutation.target);
                    // 检查内容变化的元素是否是tooltip元素或其子元素
                    if ($target.is('[data-toggle="tooltip"]') ||
                        $target.is('td[role="gridcell"][title]') ||
                        $target.parents('[data-toggle="tooltip"], td[role="gridcell"][title]').length > 0) {
                        shouldInit = true;
                    }
                }
            });

            // 如果需要，延迟初始化以确保所有DOM操作完成
            if (shouldInit) {
                setTimeout(initTooltips, 0);
            }
        });

        // 配置观察选项，增加对attributes和characterData的监听
        const config = {
            childList: true,       // 监听子节点变化
            subtree: true,         // 监听所有后代节点
            attributes: true,      // 监听属性变化
            characterData: true,   // 监听字符数据变化
            attributeFilter: ['title', 'data-toggle']  // 只监听与tooltip相关的属性
        };

        // 开始观察
        observer.observe(document.body, config);
    } else {
        // 兼容旧浏览器
        $(document).on('DOMNodeInserted', function(e) {
            const $target = $(e.target);
            // 检查是否添加了需要初始化 Tooltip 的元素
            if ($target.is('[data-toggle="tooltip"]') ||
                $target.is('td[role="gridcell"][title]') ||
                $target.find('[data-toggle="tooltip"], td[role="gridcell"][title]').length > 0) {
                setTimeout(initTooltips, 0);
            }
        });

        // 监听属性变化（兼容旧浏览器）
        $(document).on('DOMAttrModified', function(e) {
            if (e.attrName === 'title' || e.attrName === 'data-toggle') {
                const $target = $(e.target);
                if ($target.is('[data-toggle="tooltip"]') || $target.is('td[role="gridcell"]')) {
                    setTimeout(initTooltips, 0);
                }
            }
        });
    }

    // 3. 暴露全局方法，允许手动触发初始化（可选）
    $.fn.initTooltips = initTooltips;
    window.initTooltips = initTooltips;

})(jQuery, window, document);