package com.sxmd.content.baseroute.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxmd.config.RouteInitConfig;
import com.sxmd.content.baseroute.entity.BaseRouteEntity;
import com.sxmd.content.baseroute.mapper.BaseRouteMapper;
import com.sxmd.content.baseroute.model.RouteDefinitionSimpleModel;
import com.sxmd.util.IdWorkerUil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Description: 路由规则 实现类
 *
 * @author sxmd
 * @date Version 1.0
 */
@Service
public class BaseRouteServiceImpl extends ServiceImpl<BaseRouteMapper, BaseRouteEntity> implements BaseRouteService {

    @Autowired
    private RouteInitConfig routeInitConfig;

    /**
     * Description:   新增
     *
     * @param route:
     * @author sxmd
     * @date
     */
    @Override
    @Transactional
    public Long insertBaseRoute(RouteDefinition route) {
        routeInitConfig.add(route);
        BaseRouteEntity entity = new BaseRouteEntity();
        entity.setId(IdWorkerUil.generateId());
        entity.setRouteId(route.getId());
        entity.setOrder(route.getOrder());
        entity.setCreateTime(LocalDateTime.now());
        entity.setDelete(false);
        entity.setUri(route.getUri().toString());
        entity.setPredicates(JSON.toJSONString(route.getPredicates()));
        entity.setFilters(JSON.toJSONString(route.getFilters()));
        baseMapper.insert(entity);
        return entity.getId();
    }

    /**
     * Description: 更新
     *
     * @param model:
     * @author sxmd
     * @date
     */
    @Override
    public void updateBaseRoute(RouteDefinition model) {
        // 调用删除，再新增
        this.deleteBaseRoute(model.getId());
        this.insertBaseRoute(model);
    }

    /**
     * Description: 删除
     *
     * @param routeId: 路由id
     * @author sxmd
     * @date
     */
    @Override
    public void deleteBaseRoute(String routeId) {
        // 先删除路由表
        routeInitConfig.delete(routeId);
        baseMapper.delete(new QueryWrapper<BaseRouteEntity>().eq("route_id", routeId));
    }

    @Override
    public List<BaseRouteEntity> findAll() {
        return baseMapper.selectList(new QueryWrapper<BaseRouteEntity>().eq("is_delete", false));
    }

    @Override
    public void simpleInsert(RouteDefinitionSimpleModel model) {
        // 数据转换
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setId(model.getRouteId());
        routeDefinition.setUri(URI.create("lb://" + model.getServerId()));
        List<PredicateDefinition> list = new ArrayList<>();
        // path 条件
        PredicateDefinition predicatePath = new PredicateDefinition();
        predicatePath.setName("Path");
        predicatePath.setArgs(new LinkedHashMap<String, String>() {
            private static final long serialVersionUID = 6919255455158000113L;

            {
                put("patterns", model.getUrl());
            }
        });
        list.add(predicatePath);

        // weight 条件构造
        if (StringUtils.isNotBlank(model.getGroup()) && model.getWeight() != null) {
            PredicateDefinition predicateWeight = new PredicateDefinition();
            predicateWeight.setName("Weight");
            predicateWeight.setArgs(new LinkedHashMap<String, String>() {
                private static final long serialVersionUID = 6919255455158000113L;

                {
                    put("group", model.getGroup());
                    put("weight", model.getWeight() + "");
                }
            });
            list.add(predicateWeight);
        }
        FilterDefinition filterDefinition = new FilterDefinition();
        filterDefinition.setName("StripPrefix");
        filterDefinition.setArgs(new LinkedHashMap<String, String>() {
            private static final long serialVersionUID = 6919255455158000113L;

            {
                put("parts", "1");
            }
        });
        routeDefinition.setFilters(Collections.singletonList(filterDefinition));
        routeDefinition.setPredicates(list);
        this.insertBaseRoute(routeDefinition);
    }
}
