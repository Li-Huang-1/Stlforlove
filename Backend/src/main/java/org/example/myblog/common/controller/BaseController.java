package org.example.myblog.common.controller;

import org.example.myblog.common.domain.AjaxResult;
import org.example.myblog.common.domain.TableDataInfo;

/*
    针对控制层优化的响应类
 */
public class BaseController {

    /**
     * 列表数据响应
     * @param rows 列表数据
     * @param total 总条数
     * @return
     */
    protected TableDataInfo getDataInfo(Object rows,Long total){

        TableDataInfo dataInfo = new TableDataInfo();
        dataInfo.setCode(200);
        dataInfo.setMsg("查询成功");
        dataInfo.setRows(rows);
        dataInfo.setTotal(total);

        return dataInfo;
    }

    /**
     * 通过影响行数界定操作结果
     * @param row
     * @return
     */
    protected AjaxResult toAjax(int row){
        return row > 0 ? AjaxResult.success() : AjaxResult.error();
    }

}
