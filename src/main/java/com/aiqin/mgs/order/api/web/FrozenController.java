/*****************************************************************

* 模块名称：挂单解挂后台-入口
* 开发人员: hzy
* 开发时间: 2018-11-05 

* ****************************************************************************/
package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.FrozenInfo;
import com.aiqin.mgs.order.api.service.FrozenService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/frozen")
@Api("接口-挂单解挂")
@SuppressWarnings("all")
public class FrozenController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FrozenController.class);
    
    @Resource
    private FrozenService frozenService;
    

    
    /**
     * 挂单入库
     * @param frozenInfo
     * @return
     */
    @PostMapping("")
    @ApiOperation(value = "将商品列表挂起")
    public HttpResponse addFrozenInfo(@Valid @RequestBody List<FrozenInfo> frozenInfolist) {
    	
        LOGGER.info("将商品列表挂起...");
        return frozenService.addFrozenInfo(frozenInfolist);
    
    }
    
    
    /**
     * 删除解卦信息
     * @param frozenId
     * @return
     */
    @DeleteMapping("/deletedetailbyfrozenId/{frozen_id}")
    @ApiOperation(value = "删除解卦信息")
    public HttpResponse deleteDetailByFrozenId(@Valid @PathVariable(name = "frozen_id",required = true) String frozenId) {
        
    	
    	LOGGER.info("删除挂单数据......");
        return frozenService.deleteByFrozenId(frozenId);
    }
    
    
    /**
     * 将挂单数据解卦
     * @param frozenId
     * @return
     */
    @GetMapping("/selectdetailbyfrozenId/{frozen_id}")
    @ApiOperation(value = "将挂单数据解卦")
    public HttpResponse selectDetailByFrozenId(@Valid @PathVariable(name = "frozen_id",required = true) String frozenId) {
        
    	LOGGER.info("解卦......");
    	
    	LOGGER.info("查询挂单明细......");
    	
    	HttpResponse rs = frozenService.selectDetailByFrozenId(frozenId);
    	
    	LOGGER.info("删除挂单数据......");
    	
    	frozenService.deleteByFrozenId(frozenId);
    	
        return rs;//返回挂单数据
    }

    
    /**
     * 查询挂单明细列表
     * @param frozenId
     * @return
     */
    @GetMapping("/selectsumbyfrozenId")
    @ApiOperation(value = "查询挂单明细...")
    public HttpResponse selectSumByFrozenId(@Valid @RequestParam(name = "create_by", required = true) String createBy,@RequestParam(name = "distributor_id", required = true) String distributorId) {
     
 	   LOGGER.info("查询挂单明细......");    	
       return frozenService.selectDetail(createBy,distributorId);
    }
    
    
    /**
     * 查询挂单数量
     * @param frozenId
     * @return
     */
    @GetMapping("/sfc")
    @ApiOperation(value = "查询挂单数量...")
    public HttpResponse sfc(@Valid @RequestParam(name = "create_by", required = true) String createBy,@RequestParam(name = "distributor_id", required = true) String distributorId) {
     
 	   LOGGER.info("查询挂单数量......");    	
       return frozenService.selectSumByParam(createBy,distributorId);
    }
    
    
}
