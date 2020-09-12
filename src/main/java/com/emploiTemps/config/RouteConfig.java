package com.emploiTemps.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by medilox on 1/24/17.
 */

@Controller
public class RouteConfig {


    @RequestMapping(value = "/{[path:[^\\.]*}", method = RequestMethod.GET)
    public String redirect() {
        return "forward:/";
    }

    @RequestMapping(value = "/{[path:[^\\.]*}/{[id:[\\d]+}")
    public String redirectWithParams(HttpServletRequest request) {
        return "forward:/";
    }

    @RequestMapping(value = "/{[path:[^\\.]*}/{[id:[\\d]+}/edit")
    public String redirectWithParams2(HttpServletRequest request) {
        return "forward:/";
    }

    @RequestMapping(value = "/{[path:[^\\.]*}/{[id:[\\d]+}/delete")
    public String redirectWithParams3(HttpServletRequest request) {
        return "forward:/";
    }

    @RequestMapping(value = "/{[path:[^\\.]*}/{[id:[\\d]+}/detail/edit")
    public String redirectWithParams4(HttpServletRequest request) {
        return "forward:/";
    }

    @RequestMapping(value = "/{[path:[^\\.]*}/{[id:[\\d]+}/{[path:[^\\.]*}")
    public String redirectWithParams5(HttpServletRequest request) {
        return "forward:/";
    }

    @RequestMapping(value = "/{[path:[^\\.]*}/{[id:[\\d]+}/{[path:[^\\.]*}/{[id:[\\d]+}")
    public String redirectWithParams6(HttpServletRequest request) {
        return "forward:/";
    }

    @RequestMapping(value = "/{[path:[^\\.]*}/{[id:[\\d]+}/{[path:[^\\.]*}/{[id:[\\d]+}/edit")
    public String redirectWithParams7(HttpServletRequest request) {
        return "forward:/";
    }

    @RequestMapping(value = "/{[path:[^\\.]*}/{[id:[\\d]+}/{[path:[^\\.]*}/{[id:[\\d]+}/detail/edit")
    public String redirectWithParams8(HttpServletRequest request) {
        return "forward:/";
    }

    @RequestMapping(value = "/{[path:[^\\.]*}/{[id:[\\d]+}/{[path:[^\\.]*}/{[id:[\\d]+}/delete")
    public String redirectWithParams9(HttpServletRequest request) {
        return "forward:/";
    }

    @RequestMapping(value = "/{[path:[^\\.]*}/{[id:[\\d]+}/{[path:[^\\.]*}/{[path:[^\\.]*}")
    public String redirectWithParams10(HttpServletRequest request) {
        return "forward:/";
    }

    @RequestMapping(value = "/{[path:[^\\.]*}/{[id:[\\d]+}/{[path:[^\\.]*}/{[path:[^\\.]*}/{[path:[^\\.]*}")
    public String redirectWithParams11(HttpServletRequest request) {
        return "forward:/";
    }

    @RequestMapping(value = "/reset/request", method = RequestMethod.GET)
    public String redirect12() {
        return "forward:/";
    }

    @RequestMapping(value = "/reset/finish", method = RequestMethod.GET)
    public String redirect13() {
        return "forward:/";
    }

    @RequestMapping(value = "/admin/{[path:[^\\.]*}", method = RequestMethod.GET)
    public String redirect14() {
        return "forward:/";
    }

}

