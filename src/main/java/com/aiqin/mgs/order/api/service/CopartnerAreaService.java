/*****************************************************************

* 模块名称：经营区域设置-接口层
* 开发人员: huangzy
* 开发时间: 2020-02-13 

* ****************************************************************************/
package com.aiqin.mgs.order.api.service;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.copartnerArea.*;
import com.aiqin.mgs.order.api.domain.request.*;
import com.aiqin.mgs.order.api.domain.request.returnorder.AreaReq;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.response.PartnerPayGateRep;

@SuppressWarnings("all")
public interface CopartnerAreaService {

	HttpResponse copartnerAreaList(@Valid CopartnerAreaVo param);

	HttpResponse getCopartnerAreaDetail(@Valid String copartnerAreaId);

	HttpResponse getCopartnerAreaStore(String copartnerAreaId);

	HttpResponse getCopartnerAreaRole(String copartnerAreaId);

	HttpResponse<CopartnerAreaUp> copartnerAreaUp();

	HttpResponse getPersonList(@Valid String personTeam);

	HttpResponse getRoleList(@Valid List<CopartnerAreaRoleList> param);

	HttpResponse roledetail(@Valid String copartnerAreaId, @Valid String personId);

	HttpResponse saveCopartnerArea(@Valid CopartnerAreaSave param);

	HttpResponse deleteMainById(String copartnerAreaId);

	HttpResponse selectStoreByPerson(String personId,String resourceCode);

	HttpResponse areaTypeInfo(Integer areaType);

	HttpResponse childrenInfo(String parentId);

	HttpResponse<List<NewStoreTreeResponse>> getStoresByAreaCode(AreaReq areaReq);

	HttpResponse<List<NewStoreTreeResponse>> getStoresByCodeOrName(String parm);

	CopartnerAreaUp qryInfo(String storeCode);
	
	List<String> qryAreaByStores(List<String> storeIds);

	HttpResponse<CopartnerAreaUp> qryCopartnerAreaList();

	HttpResponse saveAreaStore(CopartnerAreaStoreVo param);

	HttpResponse<CopartnerAreaUp> qryCopartnerAreaListBypersonId(String personId);
	//新建区域-下辖公司-新增二级公司-查询二级公司
    HttpResponse selcetCompany(String copartnerAreaCompany);
    //区域合伙人-查询下辖公司
	HttpResponse getCopartnerAreaCompany(String copartnerAreaId);
	//删除下辖公司
	HttpResponse delectTwoCompany(List<String> copartnerAreaIds);
	//门店id查询区域信息
	HttpResponse selectCopartnerAreaByStoreId(String storeId);
}
