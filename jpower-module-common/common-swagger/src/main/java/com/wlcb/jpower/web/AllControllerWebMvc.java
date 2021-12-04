package com.wlcb.jpower.web;

import io.swagger.models.Swagger;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponents;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.service.Documentation;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.PropertySourcedMapping;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.json.JsonSerializer;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static org.springframework.util.StringUtils.isEmpty;
import static springfox.documentation.swagger.common.HostNameProvider.componentsFrom;

/**
 * @author ding
 * @description
 * @date 2021-03-31 15:38
 */
@Controller
@EnableSwagger2WebMvc
@ConditionalOnClass(name = "javax.servlet.http.HttpServletRequest")
@ApiIgnore
@AllArgsConstructor
public class AllControllerWebMvc {

    private static final String HAL_MEDIA_TYPE = "application/hal+json";

    private final DocumentationCache documentationCache;
    private final ServiceModelToSwagger2Mapper mapper;
    private final JsonSerializer jsonSerializer;


    @RequestMapping(
            value = "/all/restful",
            method = RequestMethod.GET,
            produces = { APPLICATION_JSON_VALUE, HAL_MEDIA_TYPE })
    @PropertySourcedMapping(
            value = "${springfox.documentation.swagger.v2.path}",
            propertyKey = "springfox.documentation.swagger.v2.path")
    @ResponseBody
    public ResponseEntity<Json> getDocumentation(
            HttpServletRequest servletRequest) {

        Documentation documentation = documentationCache.documentationByGroup(Docket.DEFAULT_GROUP_NAME);
        Swagger swagger = mapper.mapDocumentation(documentation);
        UriComponents uriComponents = componentsFrom(servletRequest, swagger.getBasePath());
        swagger.basePath(isEmpty(uriComponents.getPath()) ? "/" : uriComponents.getPath());
        if (isEmpty(swagger.getHost())) {
            swagger.host(hostName(uriComponents));
        }
        return new ResponseEntity<>(jsonSerializer.toJson(swagger), HttpStatus.OK);
    }

    private String hostName(UriComponents uriComponents) {
        String host = uriComponents.getHost();
        int port = uriComponents.getPort();
        if (port > -1) {
            return String.format("%s:%d", host, port);
        }
        return host;
    }
}
