package com.aiqin.mgs.order.api.service.test;

import com.aiqin.ground.util.json.JsonUtil;
import com.aiqin.mgs.order.api.OrderApiBootApplication;
import com.aiqin.mgs.order.api.domain.request.orderList.OrderReqVo;
import com.aiqin.mgs.order.api.service.OrderListService;
import com.aiqin.mgs.order.api.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * OrderServiceTest
 *
 * @author zhangtao
 * @createTime 2019-03-15
 * @description
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = OrderApiBootApplication.class)
public class OrderServiceTest {


    @Resource
    private OrderListService orderService;

    @Test
    public void testSave() {
        String json = "{\n" +
                "\t\"original\": \"9b354ef90804414c8ccc3e844042c461\",\n" +
                "\t\"order_type\": 1,\n" +
                "\t\"store_id\": \"ABEC8D65036E5A45DBABCBA413FA56AEA2\",\n" +
                "\t\"store_code\": \"1147621\",\n" +
                "\t\"store_name\": \"河北承德围场县李树利1店\",\n" +
                "\t\"store_type\": \"加盟\",\n" +
                "\t\"total_orders\": 5000,\n" +
                "\t\"actual_amount_paid\": 5000,\n" +
                "\t\"activity_amount\": 0,\n" +
                "\t\"preferential_quota\": 2000,\n" +
                "\t\"logistics_remission_ratio\": 0,\n" +
                "\t\"place_order_code\": \"111111\",\n" +
                "\t\"receiving_address\": \"河北省承德市围场县巴黎春天婚纱摄影  \",\n" +
                "\t\"consignee\": \"杨俊\",\n" +
                "\t\"consignee_phone\": \"1888888888\",\n" +
                "\t\"company_code\": \"01\",\n" +
                "\t\"company_name\": \"爱亲\",\n" +
                "\t\"province_code\": \"130000\",\n" +
                "\t\"province_name\": \"河北省\",\n" +
                "\t\"city_code\": \"130800\",\n" +
                "\t\"city_name\": \"承德市\",\n" +
                "\t\"order_original\": 0,\n" +
                "\t\"product_num\": 9,\n" +
                "\t\"create_by\": \"111\",\n" +
                "\t\"application_gifts\": 0,\n" +
                "\t\"products\": [{\n" +
                "\t\t\"sku_code\": \"102421\",\n" +
                "\t\t\"sku_name\": \"日本花王纸尿裤S82片\",\n" +
                "\t\t\"product_number\": 2,\n" +
                "\t\t\"activity_code\": \"20190300037\",\n" +
                "\t\t\"activity_name\": \"3.8女神节\",\n" +
                "\t\t\"gift\": 0,\n" +
                "\t\t\"specifications\": \"S82\",\n" +
                "\t\t\"unit\": \"包\",\n" +
                "\t\t\"color_code\": \"黄\",\n" +
                "\t\t\"color_name\": \"黄\",\n" +
                "\t\t\"model_number\": \"M\",\n" +
                "\t\t\"product_price\": 2000,\n" +
                "\t\t\"original_product_price\": 1000,\n" +
                "\t\t\"discount_money\": 0,\n" +
                "\t\t\"picture_url\": \"http://lyj-upload-image.oss-cn-qingdao.aliyuncs.com/dev/a795991f-e435-47b1-acd1-a1a779909c59.jpeg?Expires=1867457988&OSSAccessKeyId=LTAIPWfI0ApMnQPo&Signature=t2sarXtrQD59dwOaItX3AzStO1M%3D\",\n" +
                "\t\t\"amount\": 2000,\n" +
                "\t\t\"preferential_allocation\": 0,\n" +
                "\t\t\"use_discount_amount\": 0,\n" +
                "\t\t\"product_type\": 1,\n" +
                "\t\t\"logo\": \"http://lyj-upload-image.oss-cn-qingdao.aliyuncs.com/dev/a795991f-e435-47b1-acd1-a1a779909c59.jpeg?Expires=1867457988&OSSAccessKeyId=LTAIPWfI0ApMnQPo&Signature=t2sarXtrQD59dwOaItX3AzStO1M%3D\",\n" +
                "\t\t\"image\": \"http://lyj-upload-image.oss-cn-qingdao.aliyuncs.com/dev/a795991f-e435-47b1-acd1-a1a779909c59.jpeg?Expires=1867457988&OSSAccessKeyId=LTAIPWfI0ApMnQPo&Signature=t2sarXtrQD59dwOaItX3AzStO1M%3D\"\n" +
                "\t}, {\n" +
                "\t\t\"sku_code\": \"102421\",\n" +
                "\t\t\"sku_name\": \"日本花王纸尿裤S82片\",\n" +
                "\t\t\"product_number\": 3,\n" +
                "\t\t\"gift\": 0,\n" +
                "\t\t\"specifications\": \"S82\",\n" +
                "\t\t\"unit\": \"包\",\n" +
                "\t\t\"color_code\": \"黄\",\n" +
                "\t\t\"color_name\": \"黄\",\n" +
                "\t\t\"model_number\": \"M\",\n" +
                "\t\t\"product_price\": 3000,\n" +
                "\t\t\"original_product_price\": 1000,\n" +
                "\t\t\"picture_url\": \"http://lyj-upload-image.oss-cn-qingdao.aliyuncs.com/dev/a795991f-e435-47b1-acd1-a1a779909c59.jpeg?Expires=1867457988&OSSAccessKeyId=LTAIPWfI0ApMnQPo&Signature=t2sarXtrQD59dwOaItX3AzStO1M%3D\",\n" +
                "\t\t\"amount\": 3000,\n" +
                "\t\t\"use_discount_amount\": 0,\n" +
                "\t\t\"product_type\": 0,\n" +
                "\t\t\"logo\": \"http://lyj-upload-image.oss-cn-qingdao.aliyuncs.com/dev/a795991f-e435-47b1-acd1-a1a779909c59.jpeg?Expires=1867457988&OSSAccessKeyId=LTAIPWfI0ApMnQPo&Signature=t2sarXtrQD59dwOaItX3AzStO1M%3D\",\n" +
                "\t\t\"image\": \"http://lyj-upload-image.oss-cn-qingdao.aliyuncs.com/dev/a795991f-e435-47b1-acd1-a1a779909c59.jpeg?Expires=1867457988&OSSAccessKeyId=LTAIPWfI0ApMnQPo&Signature=t2sarXtrQD59dwOaItX3AzStO1M%3D\"\n" +
                "\t}, {\n" +
                "\t\t\"sku_code\": \"0000111\",\n" +
                "\t\t\"sku_name\": \"惠氏金装爱儿复无乳糖配方奶粉1段800g/听\",\n" +
                "\t\t\"product_number\": 1,\n" +
                "\t\t\"gift\": 1,\n" +
                "\t\t\"original_product_sku\": \"102421\",\n" +
                "\t\t\"specifications\": \"800g\",\n" +
                "\t\t\"unit\": \"听\",\n" +
                "\t\t\"color_code\": \"3\",\n" +
                "\t\t\"color_name\": \"黄\",\n" +
                "\t\t\"model_number\": \"2段\",\n" +
                "\t\t\"product_price\": 0,\n" +
                "\t\t\"original_product_price\": 1000,\n" +
                "\t\t\"picture_url\": \"http://lyj-upload-image.oss-cn-qingdao.aliyuncs.com/dev/a795991f-e435-47b1-acd1-a1a779909c59.jpeg?Expires=1867457988&OSSAccessKeyId=LTAIPWfI0ApMnQPo&Signature=t2sarXtrQD59dwOaItX3AzStO1M%3D\",\n" +
                "\t\t\"use_discount_amount\": 0,\n" +
                "\t\t\"product_type\": 1\n" +
                "\t}, {\n" +
                "\t\t\"sku_code\": \"0000110\",\n" +
                "\t\t\"sku_name\": \"惠氏金装爱儿复无乳糖配方奶粉1段400g/听\",\n" +
                "\t\t\"product_number\": 1,\n" +
                "\t\t\"gift\": 1,\n" +
                "\t\t\"original_product_sku\": \"102421\",\n" +
                "\t\t\"specifications\": \"400g\",\n" +
                "\t\t\"unit\": \"听\",\n" +
                "\t\t\"color_code\": \"3\",\n" +
                "\t\t\"color_name\": \"黄\",\n" +
                "\t\t\"model_number\": \"2段\",\n" +
                "\t\t\"product_price\": 0,\n" +
                "\t\t\"original_product_price\": 1000,\n" +
                "\t\t\"picture_url\": \"http://lyj-upload-image.oss-cn-qingdao.aliyuncs.com/dev/13710e80-a50d-4e9d-8484-79388e31d4ad.jpeg?Expires=1867458035&OSSAccessKeyId=LTAIPWfI0ApMnQPo&Signature=sKAUvJW0W8uFIcKOQQLOGk09cgk%3D\",\n" +
                "\t\t\"use_discount_amount\": 0,\n" +
                "\t\t\"product_type\": 1\n" +
                "\t}, {\n" +
                "\t\t\"sku_code\": \"0000109\",\n" +
                "\t\t\"sku_name\": \"惠氏金装爱儿复无乳糖配方奶粉1段800g/听\",\n" +
                "\t\t\"product_number\": 1,\n" +
                "\t\t\"gift\": 1,\n" +
                "\t\t\"original_product_sku\": \"102421\",\n" +
                "\t\t\"specifications\": \"800g\",\n" +
                "\t\t\"unit\": \"听\",\n" +
                "\t\t\"color_code\": \"3\",\n" +
                "\t\t\"color_name\": \"黄\",\n" +
                "\t\t\"model_number\": \"1段\",\n" +
                "\t\t\"product_price\": 0,\n" +
                "\t\t\"original_product_price\": 1000,\n" +
                "\t\t\"picture_url\": \"http://lyj-upload-image.oss-cn-qingdao.aliyuncs.com/dev/a795991f-e435-47b1-acd1-a1a779909c59.jpeg?Expires=1867457988&OSSAccessKeyId=LTAIPWfI0ApMnQPo&Signature=t2sarXtrQD59dwOaItX3AzStO1M%3D\",\n" +
                "\t\t\"supply_company_code\": \"10000025\",\n" +
                "\t\t\"supply_company_name\": \"测试供货单位20190307002\",\n" +
                "\t\t\"use_discount_amount\": 0,\n" +
                "\t\t\"product_type\": 1\n" +
                "\t}, {\n" +
                "\t\t\"sku_code\": \"0000108\",\n" +
                "\t\t\"sku_name\": \"惠氏金装爱儿复无乳糖配方奶粉1段400g/听\",\n" +
                "\t\t\"product_number\": 1,\n" +
                "\t\t\"gift\": 1,\n" +
                "\t\t\"original_product_sku\": \"102421\",\n" +
                "\t\t\"specifications\": \"400g\",\n" +
                "\t\t\"unit\": \"听\",\n" +
                "\t\t\"color_code\": \"3\",\n" +
                "\t\t\"color_name\": \"黄\",\n" +
                "\t\t\"model_number\": \"1段\",\n" +
                "\t\t\"product_price\": 0,\n" +
                "\t\t\"original_product_price\": 1000,\n" +
                "\t\t\"picture_url\": \"http://lyj-upload-image.oss-cn-qingdao.aliyuncs.com/dev/b84aa328-d648-48c8-a2bd-fbfa2adcab4f.jpeg?Expires=1867457961&OSSAccessKeyId=LTAIPWfI0ApMnQPo&Signature=RycnhSGaFEDV%2F1wi8w17oS5Jlxw%3D\",\n" +
                "\t\t\"use_discount_amount\": 0,\n" +
                "\t\t\"product_type\": 1\n" +
                "\t}]\n" +
                "}";
        OrderReqVo orderReqVo = JsonUtil.fromJson(json, OrderReqVo.class);
        orderService.save(orderReqVo);
    }
}
