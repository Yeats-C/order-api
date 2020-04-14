package com.aiqin.mgs.order.api.web.gift;


import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.po.gift.GiftQuotasUseDetail;
import com.aiqin.mgs.order.api.domain.po.gift.GiftQuotasUseDetailPageRequest;
import com.aiqin.mgs.order.api.service.gift.GiftQuotasUseDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/giftQuotasUseDetail")
@Api(tags = "兑换赠品积分账户使用明细相关接口")
public class GiftQuotasUseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GiftQuotasUseController.class);
    @Resource
    private GiftQuotasUseDetailService giftQuotasUseDetailService;

    /**
     * 通过门店id查询兑换赠品积分账户使用明细
     * @param store_id
     * @return
     */
    @GetMapping("/getListByStoreId")
    @ApiOperation(value = "通过门店id查询兑换赠品积分账户使用明细")
    public HttpResponse<List<GiftQuotasUseDetail>> getListByStoreId(String store_id){
        return giftQuotasUseDetailService.getListByStoreId(store_id);
    }

    /**
     * 添加兑换赠品积分账户使用明细
     *
     * @param
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加兑换赠品积分账户使用明细")
    public HttpResponse add(@RequestBody GiftQuotasUseDetail giftQuotasUseDetail) {
        return giftQuotasUseDetailService.add(giftQuotasUseDetail);
    }

    /**
     * 通过门店id查询兑换赠品积分账户使用明细（爱掌柜使用，分页）
     * @param giftQuotasUseDetail
     * @return
     */
    @PostMapping("/getPageByStoreId")
    @ApiOperation(value = "通过门店id查询兑换赠品积分账户使用明细（爱掌柜使用，分页）")
    public HttpResponse<PageResData<GiftQuotasUseDetail>> getPageByStoreId(@RequestBody GiftQuotasUseDetailPageRequest giftQuotasUseDetailPageRequest){
        return giftQuotasUseDetailService.getPageByStoreId(giftQuotasUseDetailPageRequest);
    }

}
