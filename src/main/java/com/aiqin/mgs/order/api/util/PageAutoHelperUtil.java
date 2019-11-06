package com.aiqin.mgs.order.api.util;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.function.Supplier;

/**
 * 分页工具
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/6 16:23
 */
public class PageAutoHelperUtil {

    /**
     * 查询列表分页方法
     *
     * @param supplier 查询dao层方法一起写入
     * @param page     分页量
     * @return com.github.pagehelper.PageInfo
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/6 16:24
     */
    public static PageInfo generatePage(Supplier<List> supplier, PagesRequest page) {
        if (page == null) {
            page = new PagesRequest();
        }
        int pageNum = page.getBeginIndex();
        int pageSize = page.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List list = supplier.get();
        return new PageInfo(list);
    }

}
