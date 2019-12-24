package com.aiqin.mgs.order.api.domain.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("文件上传返回实体")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileOssResponse {
    @ApiModelProperty("文件原始名称")
    private String originalFilename;

    @ApiModelProperty("文件oss存储uuid")
    private String uuid;

    @ApiModelProperty("地址")
    private String url;

  /*  @ApiModelProperty("文件类型")
    private String type;*/
}
