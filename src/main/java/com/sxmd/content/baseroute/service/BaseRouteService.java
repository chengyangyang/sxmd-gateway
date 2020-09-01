package com.sxmd.content.baseroute.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxmd.content.baseroute.entity.BaseRouteEntity;
import com.sxmd.content.baseroute.model.RouteDefinitionSimpleModel;
import org.springframework.cloud.gateway.route.RouteDefinition;

import java.util.List;

/**
 * Description: 路由规则接口
 *
 * @author sxmd
 * @date Version 1.0
 */
public interface BaseRouteService extends IService<BaseRouteEntity> {

    /**
     * Description:  路由规则-新增
     *
     * @param route:
     * @return
     * @author sxmd
     * @date
     */
    Long insertBaseRoute(RouteDefinition route);

    /**
     * Description:  路由规则-更新
     *
     * @param model:
     * @author sxmd
     * @date
     */
    void updateBaseRoute(RouteDefinition model);

    /**
     * Description:   路由规则-删除
     *
     * @param routeId: 路由id
     * @author sxmd
     * @date
     */
    void deleteBaseRoute(String routeId);

    /**
     * Description:   查找所有的路由
     *
     * @param :
     * @return java.util.List<com.sxmd.content.baseroute.entity.BaseRouteEntity>
     * @author cy
     * @date 2020/8/13 18:52
     */
    List<BaseRouteEntity> findAll();

    /**
     * Description:   简化版的路由新增
     *
     * @param model:
     * @return void
     * @author cy
     * @date 2020/9/1 18:51
     */
    void simpleInsert(RouteDefinitionSimpleModel model);

}
