package com.acg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * visit internal resource
 */

@Controller
public class JumpInternalResourceController {

    /**
     * 传入视图名称实现内部资源跳转
     * @param page 内部资源视图名称
     * @return 视图名称
     */
    @RequestMapping("/internalResource/{page}")
    public String jumpToInternalResource(@PathVariable("page") String page) {

        return page;
    }
}
