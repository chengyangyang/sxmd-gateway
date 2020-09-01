package com.sxmd.content.baseroute.controller;


import com.sxmd.base.BaseController;
import com.sxmd.base.JsonResult;
import com.sxmd.config.CustomRouteRepository;
import com.sxmd.constant.ConstantWeb;
import com.sxmd.content.baseroute.model.RouteDefinitionSimpleModel;
import com.sxmd.content.baseroute.service.BaseRouteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 路由规则 控制器
 *
 * @author sxmd
 * @date Version 1.0
 */
@RestController
@Api(value = "路由规则(网关手动配置)", tags = "路由规则(网关手动配置)")
@RequestMapping("base-route")
public class BaseRouteController extends BaseController {

    @Autowired
    private BaseRouteService service;
    @Autowired
    private CustomRouteRepository customRouteRepository;


    /**
     * Description:   路由规则-新增简化版
     *
     * @param model:
     * @author sxmd
     * @date
     */
    @ApiOperation(value = "新增(简化路由规则)", notes = "新增(简化路由规则)")
    @PostMapping("/simple")
    public JsonResult<String> insert(@RequestBody @Valid RouteDefinitionSimpleModel model) {
        service.simpleInsert(model);
        return success(ConstantWeb.SAVE_MSEEAGE);
    }

    /**
     * Description:   路由规则-所有数据
     *
     * @param :
     * @author sxmd
     * @date
     */
    @ApiOperation(value = "所有数据", notes = "获得所有路由规则数据")
    @GetMapping(value = "/all")
    public JsonResult<List<RouteDefinition>> all() {
        List<RouteDefinition> result = new ArrayList<>();
        Flux<RouteDefinition> routeDefinitions = customRouteRepository.getRouteDefinitions();
        routeDefinitions.subscribe(x -> result.add(x));
        return success(result);
    }


    /**
     * Description:   路由规则-新增
     *
     * @param model:
     * @author sxmd
     * @date
     */
    @ApiOperation(value = "新增", notes = "新增路由规则")
    @PostMapping
    public JsonResult<String> insert(@RequestBody @Valid RouteDefinition model) {
        service.insertBaseRoute(model);
        return success(ConstantWeb.SAVE_MSEEAGE);
    }


    /**
     * Description:   路由规则-修改
     *
     * @param model:
     * @author sxmd
     * @date
     */
    @ApiOperation(value = "修改", notes = "根据id更新路由规则")
    @PutMapping
    public JsonResult<String> update(@RequestBody @Valid RouteDefinition model) {
        service.updateBaseRoute(model);
        return success(ConstantWeb.UPDATE_MSEEAGE);
    }


    /**
     * Description:   路由规则-删除
     *
     * @param id:
     * @author sxmd
     * @date
     */
    @ApiOperation(value = "路由id", notes = "根据路由id删除路由规则")
    @ApiImplicitParam(name = "id", value = "路由 id", required = true, dataType = "String", paramType = "path")
    @DeleteMapping(value = "/{id}")
    public JsonResult<String> delete(@PathVariable("id") String id) {
        service.deleteBaseRoute(id);
        return success(ConstantWeb.DELETE_MSEEAGE);
    }

}
