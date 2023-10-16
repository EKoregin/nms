package com.ekoregin.nms.controller;

import com.ekoregin.nms.dto.CustomerDto;
import com.ekoregin.nms.entity.Customer;
import com.ekoregin.nms.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public String allCustomers(Model model) {
        model.addAttribute("customers", customerService.findAll());
        return "customers";
    }

    @GetMapping("/addForm")
    public String formCreateCustomer(Model model) {
        model.addAttribute("customerDto", new Customer());
        return "addCustomer";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute CustomerDto customerDto) {
        customerService.create(customerDto);
        return "redirect:/customers";
    }

    @GetMapping("/editForm/{customerId}")
    public String formEditCustomer(@PathVariable long customerId, Model model) {
        Customer foundCustomer = customerService.findById(customerId);
        if (foundCustomer != null) {
            CustomerDto customerDto = new CustomerDto(foundCustomer);
            model.addAttribute("customerDto", customerDto);
        } else {
            log.info("Customer with ID: {} not found", customerId);
            throw new NoSuchElementException("Customer with ID: " + customerId + " not found");
        }
        return "editCustomer";
    }

    @PutMapping("/update")
    public String update(@ModelAttribute CustomerDto customerDto) {
        customerService.update(customerDto);
        return "redirect:/customers";
    }

    @DeleteMapping("/{customerId}")
    public String delete(@PathVariable long customerId) {
        customerService.delete(customerId);
        return "redirect:/customers";
    }

}
