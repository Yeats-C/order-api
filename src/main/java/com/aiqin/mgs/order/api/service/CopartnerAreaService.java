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
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaListReq;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaRoleList;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaRoleVo;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaSave;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaStoreVo;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaUp;
import com.aiqin.mgs.order.api.domain.request.*;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.response.PartnerPayGateRep;

@SuppressWarnings("all")
public interface CopartnerAreaService {

	HttpResponse copartnerAreaList(@Valid CopartnerAreaListReq param);

	HttpResponse getCopartnerAreaDetail(@Valid String copartnerAreaId);

	HttpResponse getCopartnerAreaStore(CopartnerAreaStoreVo vo);

	HttpResponse getCopartnerAreaRole(CopartnerAreaRoleVo vo);

	HttpResponse<CopartnerAreaUp> copartnerAreaUp();

	HttpResponse getPersonList(@Valid String personTeam);

	HttpResponse getRoleList(@Valid List<CopartnerAreaRoleList> param);

	HttpResponse roledetail(@Valid String copartnerAreaId, @Valid String personId);

	HttpResponse saveCopartnerArea(@Valid CopartnerAreaSave param);
	
}
