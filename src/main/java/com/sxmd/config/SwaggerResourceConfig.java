package com.sxmd.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.handler.AsyncPredicate;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author cy
 * @date 2019年12月02日 10:34
 * Version 1.0
 */
@Slf4j
@Component
@Primary
public class SwaggerResourceConfig implements SwaggerResourcesProvider {

    @Value("${spring.application.name}")
    private String applicationName;

    public static final String API_URI = "/v2/api-docs";
    private final RouteLocator routeLocator;
    private final GatewayProperties gatewayProperties;
    /**
     * 从注册中心注册的都会带这个,本次使用的是nacos
     */
    public static final String REGISTRY_CENTER_SUB_PRIX = "ReactiveCompositeDiscoveryClient_";

    public SwaggerResourceConfig(RouteLocator routeLocator, GatewayProperties gatewayProperties) {
        this.routeLocator = routeLocator;
        this.gatewayProperties = gatewayProperties;
    }

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        List<Route> routes = new ArrayList<>();
        routeLocator.getRoutes().subscribe(route -> routes.add(route));
        // 从路由中加载  并去除注册中心加载的网关
        routes.stream().filter(x -> !(REGISTRY_CENTER_SUB_PRIX + applicationName).equals(x.getId())).forEach(x -> {
            // 获得名称
            String routeId = x.getId().replace(REGISTRY_CENTER_SUB_PRIX, "");
            AsyncPredicate<ServerWebExchange> predicate = x.getPredicate();
            String predicateStr = predicate.toString();
            // 查找对应的paths
            String url = "/" + routeId + API_URI;
            if (StringUtils.isNotBlank(predicateStr) && predicateStr.contains("Paths")) {
                String content = predicateStr.substring(predicateStr.indexOf("Paths"));
                // 获取path中的值
                String path = content.substring(content.indexOf('[') + 1, content.indexOf(']'));
                if (path.endsWith("/**")) {
                    url = path.substring(0, path.length() - 3) + API_URI;
                }
            }
            resources.add(swaggerResource(routeId, url));
        });
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location) {
        log.info("name:{},location:{}", name, location);
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }

}
