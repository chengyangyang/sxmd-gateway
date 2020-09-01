package com.sxmd.content.baseroute.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 *
 * @author cy
 * @date 2020年09月01日 14:10
 * Version 1.0
 */
@ApiModel("简化的路由新增model")
@Data
public class RouteDefinitionSimpleModel {


    @ApiModelProperty(value = "服务id,对应注册中心名称", required = true)
    private String serverId;

    @ApiModelProperty(value = "路由id", required = true)
    private String routeId;

    @ApiModelProperty(value = "path url 匹配", required = true)
    private String url;

    @ApiModelProperty(value = "组")
    private String group;

    @ApiModelProperty(value = "权重(是根据组计算的)")
    private Integer weight;


}
