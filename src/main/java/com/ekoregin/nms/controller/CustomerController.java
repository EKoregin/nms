package com.ekoregin.nms.controller;

import com.ekoregin.nms.dto.CustomerDto;
import com.ekoregin.nms.dto.TechParameterDto;
import com.ekoregin.nms.entity.*;
import com.ekoregin.nms.service.CustomerService;
import com.ekoregin.nms.service.DeviceService;
import com.ekoregin.nms.service.TechParamServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final DeviceService deviceService;
    private final TechParamServiceImpl techParamService;

    @RequestMapping(path = {"", "/search"})
    public String searchCustomers(Model model, String searchKeyword,
                                  @RequestParam("page") Optional<Integer> page,
                                  @RequestParam("size") Optional<Integer> size,
                                  @RequestParam("sortField") Optional<String> sortField) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(50);
        String currentSortField = sortField.orElse("id");
        model.addAttribute("countCustomers", customerService.count());
        var pageRequest = PageRequest.of(currentPage - 1, pageSize);
        Page<Customer> customerPage;
        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            log.info("Search keyword is: " + searchKeyword);
            customerPage = customerService.findByNamePaginated(searchKeyword, pageRequest);
            model.addAttribute("customerPage", customerPage);
            model.addAttribute("searchFlag", true);
        } else {
            log.info("Get all customers");
            customerPage = customerService.findPaginated(pageRequest, currentSortField);
            model.addAttribute("sortField", sortField);
            model.addAttribute("currentPage", currentPage);
            model.addAttribute("customerPage", customerPage);
        }

        int totalPages = customerPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "customers";
    }

    @GetMapping("/addForm")
    public String formCreateCustomer(Model model) {
        model.addAttribute("customerDto", new CustomerDto());
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
            List<Check> checks = foundCustomer.getDevices().stream()
                    .map(Device::getModel)
                    .flatMap(modelDevice -> modelDevice.getChecks().stream())
                    .filter(check -> check.getCheckScope().equals(CheckScope.CUSTOMER.name()))
                    .toList();
            model.addAttribute("checks", checks);
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
            model.addAttribute("customerId", customerId);
            model.addAttribute("allDevices", deviceService.findAll("id"));
        } else {
            log.info("Customer with ID: {} not found", customerId);
            throw new NoSuchElementException("Customer with ID: " + customerId + " not found");
        }
        return "addDeviceToCustomer";
    }

    @PostMapping("/addDeviceToCustomer")
    public String addDeviceToCustomer(@RequestParam("customerId") long customerId,
                                      @RequestParam("deviceId") long deviceId,
                                      @RequestParam("portNumber") long portNumber) {
        Customer customer = customerService.findById(customerId);
        Device device = deviceService.findById(deviceId);
        TypeTechParameter typePort = device.getModel().getTypePort();
        TechParameterDto paramPort = new TechParameterDto();
        paramPort.setCustomerId(customerId);
        paramPort.setTypeId(typePort.getId());
        paramPort.setValue(String.valueOf(portNumber));
        techParamService.create(paramPort);
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

        //Delete port from customer
        TypeTechParameter typePort = foundDevice.getModel().getTypePort();
        TechParameter techPortParam = foundCustomer.getParams()
                .stream()
                .filter(techParameter -> techParameter.getType().equals(typePort))
                .findFirst().orElse(null);
        log.info("Remove port {} from customer", techPortParam);
        foundCustomer.getParams().remove(techPortParam);
        foundCustomer.getDevices().remove(foundDevice);
        customerService.update(foundCustomer);
        return "redirect:/customers/editForm/" + customerId;
    }

}
