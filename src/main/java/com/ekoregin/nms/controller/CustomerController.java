package com.ekoregin.nms.controller;

import com.ekoregin.nms.dto.CustomerDto;
import com.ekoregin.nms.entity.Customer;
import com.ekoregin.nms.entity.Device;
import com.ekoregin.nms.service.CustomerService;
import com.ekoregin.nms.service.DeviceService;
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
    private final DeviceService deviceService;

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

    @GetMapping("/addDevice/{customerId}")
    public String formAddDeviceToCustomer(@PathVariable long customerId, Model model) {
        Customer foundCustomer = customerService.findById(customerId);
        if (foundCustomer != null) {
            CustomerDto customerDto = new CustomerDto(foundCustomer);
            model.addAttribute("customerId", customerId);
            model.addAttribute("allDevices", deviceService.findAll());
        } else {
            log.info("Customer with ID: {} not found", customerId);
            throw new NoSuchElementException("Customer with ID: " + customerId + " not found");
        }
        return "addDeviceToCustomer";
    }

    @PostMapping("/addDeviceToCustomer")
    public String addDeviceToCustomer(@RequestParam("customerId") long customerId,
                                      @RequestParam("deviceId") long deviceId) {
        Customer customer = customerService.findById(customerId);
        Device device = deviceService.findById(deviceId);
        customer.getDevices().add(device);
        customerService.update(customer);
        return "redirect:/customers/editForm/" + customerId;
    }

    @DeleteMapping("/delDeviceFromCustomer/{customerId}/device/{deviceId}")
    public String delDeviceFromCustomer(@PathVariable long customerId,
                                        @PathVariable long deviceId) {
        log.info("Delete device with ID {}, from customer ID: {}", deviceId, customerId);
        Customer foundCustomer = customerService.findById(customerId);
        Device foundDevice = deviceService.findById(deviceId);
        foundCustomer.getDevices().remove(foundDevice);
        customerService.update(foundCustomer);
        return "redirect:/customers/editForm/" + customerId;
    }

}
