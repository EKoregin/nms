package com.ekoregin.nms.http.controller;

import com.ekoregin.nms.database.entity.*;
import com.ekoregin.nms.database.repository.CustomerDeviceRepo;
import com.ekoregin.nms.dto.CustomerDto;
import com.ekoregin.nms.mapper.CustomerCreateEditMapper;
import com.ekoregin.nms.service.*;
import com.ekoregin.nms.util.CustomerToDeviceForm;
import com.ekoregin.nms.util.Validation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final DeviceService deviceService;
    private final TypeTechParamService ttpService;
    private final CheckService checkService;
    private final TechParamService techParamService;
    private final CustomerDeviceRepo customerDeviceRepo;

    @ModelAttribute
    private void currentAuthUser(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("currentUser", userDetails.getUsername());
    }

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

    @PostMapping
    public String create(@ModelAttribute CustomerDto customerDto) {
        customerService.create(customerDto);
        return "redirect:/customers";
    }

    @GetMapping("/{id}")
    public String formEditCustomer(@PathVariable("id") Long id, Model model) {
        Customer foundCustomer = customerService.findById(id);
        if (foundCustomer != null) {
            CustomerDto customerDto = new CustomerDto(foundCustomer);
            List<Check> checks = foundCustomer.getDevices().stream()
                    .map(Device::getModel)
                    .flatMap(modelDevice -> modelDevice.getChecks().stream())
                    .filter(check -> check.getCheckScope().equals(CheckScope.CUSTOMER.name()))
                    .toList();
            model.addAttribute("checks", checks);
            model.addAttribute("customerDto", customerDto);
            model.addAttribute("connections", foundCustomer.getConnections());
        } else {
            log.info("Customer with ID: {} not found", id);
            throw new NoSuchElementException("Customer with ID: " + id + " not found");
        }
        return "editCustomer";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute CustomerDto customerDto) {
        return customerService.update(id, customerDto)
                .map(customer -> "redirect:/customers/" + customer.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        if(!customerService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/customers";
    }

    @GetMapping("/addDevice/{customerId}")
    public String formAddDeviceToCustomer(@PathVariable long customerId, Model model) {
        CustomerToDeviceForm customerToDeviceForm = new CustomerToDeviceForm();

        Customer foundCustomer = customerService.findById(customerId);
        if (foundCustomer != null) {
            customerToDeviceForm.setCustomer(new CustomerDto(foundCustomer));
            model.addAttribute("parametersForm", customerToDeviceForm);
            model.addAttribute("allDevices", deviceService.findAll("id"));
        } else {
            log.info("Customer with ID: {} not found", customerId);
            throw new NoSuchElementException("Customer with ID: " + customerId + " not found");
        }
        return "addDeviceToCustomer";
    }

    @PostMapping("/addDeviceToCustomer")
    public String addDeviceToCustomer(@ModelAttribute CustomerToDeviceForm parametersForm) {
        log.info("Parameters: {}", parametersForm.getParameters().toString());
        long customerId = parametersForm.getCustomer().getCustomerId();
        Map<Long, String> parameters = parametersForm.getParameters();
        Customer customer = customerService.findById(customerId);
        Device device = deviceService.findById(parametersForm.getDeviceId());

        for (Long typeId : parameters.keySet()) {
            TypeTechParameter type = ttpService.findById(typeId);
            TechParameter techParameter = new TechParameter();
            //find techParameter with type in Customer parameters
            for (TechParameter customerTechParam : customer.getParams()) {
                if (customerTechParam.getType().getName().equals(type.getName())) {
                    techParameter = customerTechParam;
                    break;
                }
            }
            Validation.verifyParameterForValidity(type, parameters.get(typeId));

            techParameter.setType(type);
            techParameter.setValue(parameters.get(typeId));
            techParameter.setCustomer(customer);
            techParamService.create(techParameter);

            device.getParameters().add(techParameter);
            customer.getParams().add(techParameter);
        }
        customer.getDevices().add(device);
        customerService.update(customer);

        //Exec check for connecting to device
        checkService.findByModelDeviceAndForConnecting(device.getModel())
                .ifPresentOrElse(check -> {
                            log.info("Run Check for connecting with ID: {} for Customer with ID: {}", check.getCheckId(), customer.getId());
                            checkService.executeForCustomer(check.getCheckId(), customer.getId());
                        },
                        () -> log.warn("Connecting creator for device: {} not assign", device.getName())
                );

        // Change isConnected to true in CustomerDevice
        CustomerDevice connection = customer.getConnections().stream()
                .filter(conn -> conn.getDevice().getId().equals(device.getId()))
                .findFirst().orElse(null);
        if (connection != null) {
            connection.setConnected(true);
            customerDeviceRepo.save(connection);
        }

        return "redirect:/customers/" + customerId;
    }

    @DeleteMapping("/delDeviceFromCustomer/{customerId}/device/{deviceId}")
    public String delDeviceFromCustomer(@PathVariable long customerId,
                                        @PathVariable long deviceId) {
        log.info("Delete device with ID {}, from customer ID: {}", deviceId, customerId);
        Customer foundCustomer = customerService.findById(customerId);
        Device foundDevice = deviceService.findById(deviceId);

        checkService.findByModelDeviceAndForDisconnecting(foundDevice.getModel())
                .ifPresentOrElse(check -> {
                            log.info("Run Check with ID: {} for disconnecting for Customer with ID: {}", check.getCheckId(), foundCustomer.getId());
                            checkService.executeForCustomer(check.getCheckId(), foundCustomer.getId());
                        },
                        () -> log.warn("Disconnecting creator for device: {} not assign", foundDevice.getName())
                );

        List<Long> deviceParameterIds = foundDevice.getParameters().stream().map(TechParameter::getParamId).toList();
        List<Long> customerParameterIds = foundCustomer.getParams().stream().map(TechParameter::getParamId).toList();
        List<Long> removeParameters = new ArrayList<>();
        for (Long paramId : customerParameterIds) {
            if (deviceParameterIds.contains(paramId))
                removeParameters.add(paramId);
        }

        for (Long paramId : removeParameters) {
            TechParameter removeParameter = foundDevice.getParameters().stream()
                    .filter(parameter -> parameter.getParamId().equals(paramId))
                    .findFirst().orElse(null);

            int devicesWithParameter = 0;
            if (removeParameter != null)
                devicesWithParameter = removeParameter.getDevices().size();

            foundDevice.getParameters().remove(removeParameter);

            if (devicesWithParameter == 1)
                foundCustomer.getParams().remove(removeParameter);
        }

        foundCustomer.getDevices().remove(foundDevice);

        customerService.update(foundCustomer);

        return "redirect:/customers/" + customerId;
    }
}
