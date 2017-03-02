package com.singlepage.cms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.singlepage.cms.models.Customer;
import com.singlepage.cms.repositories.CustomerRepository;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
//@RestController
public class CustomerController {

    @Autowired
    CustomerRepository repository;

    @RequestMapping(value = "/customers/index", method = RequestMethod.GET)
    public String index(Map<String, Object> model) {
        model.put("customers",  repository.findAll());
        return "customer/index";
    }

    @RequestMapping(value = "/customer/{id}", method = RequestMethod.GET)
    public String show(@PathVariable long id, Map<String, Object> model){
        model.put("customer",  repository.findOne(id));
        return "customer/show";
    }

    @RequestMapping("/customers/save")
    public String process(Map<String, Object> model){
        repository.save(new Customer("Jack", "Smith"));
        repository.save(new Customer("Adam", "Johnson"));
        repository.save(new Customer("Kim", "Smith"));
        repository.save(new Customer("David", "Williams"));
        repository.save(new Customer("Peter", "Davis"));
        model.put("customers",  repository.findAll());
        return "customer/index";
    }
}
