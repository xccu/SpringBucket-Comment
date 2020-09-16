package com.example.demo.controller;

import com.example.demo.controller.request.NewOrderRequest;
import com.example.demo.model.Coffee;
import com.example.demo.model.CoffeeOrder;
import com.example.demo.service.CoffeeOrderService;
import com.example.demo.service.CoffeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/order")
@Slf4j
public class CoffeeOrderController {
    @Autowired
    private CoffeeOrderService orderService;
    @Autowired
    private CoffeeService coffeeService;

    @GetMapping("/{id}")
    @ResponseBody
    public CoffeeOrder getOrder(@PathVariable("id") Long id) {
        return orderService.get(id);
    }

    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public CoffeeOrder create(@RequestBody NewOrderRequest newOrder) {
        log.info("Receive new Order {}", newOrder);
        Coffee[] coffeeList = coffeeService.getCoffeeByName(newOrder.getItems())
                .toArray(new Coffee[] {});
        return orderService.createOrder(newOrder.getCustomer(), coffeeList);
    }

    /**
     * @ModelAttribute注解，返回值放在请求的modelMap中
     * @return
     */
    @ModelAttribute
    public List<Coffee> coffeeList() {
        return coffeeService.getAllCoffee();
    }

    @GetMapping(path = "/")
    public ModelAndView showCreateForm() {
        //浏览器呈现视图
        return new ModelAndView("create-order-form");
    }

    /**
     * 不使用@ModelAttribute注解，直接在当前方法返回modelMap
     * @param model
     * @return
     */
    /*@GetMapping(path = "/")
    public ModelAndView showCreateFormMode(Model model) {
        model.addAttribute("coffeeList",coffeeService.getAllCoffee());
        //浏览器呈现视图
        return new ModelAndView("create-order-form");
    }*/

    /**
     * APPLICATION_FORM_URLENCODED_VALUE 接收提交的表单类型
     * @param newOrder 对接收的数据进行校验
     * @param result
     * @param map
     * @return
     */
    @PostMapping(path = "/", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createOrder(@Valid NewOrderRequest newOrder,
                              BindingResult result, ModelMap map) {

        //验证出错,不做重定向,返回message报错信息
        if (result.hasErrors()) {
            log.warn("Binding Result: {}", result);
            //传递给create-order-form页面的信息
            map.addAttribute("message", result.toString());
            return "create-order-form";
        }

        log.info("Receive new Order {}", newOrder);
        Coffee[] coffeeList = coffeeService.getCoffeeByName(newOrder.getItems())
                .toArray(new Coffee[] {});
        CoffeeOrder order = orderService.createOrder(newOrder.getCustomer(), coffeeList);

        //status:302 重定向到:getOrder(@PathVariable("id") Long id)
        return "redirect:/order/" + order.getId();
    }
}
