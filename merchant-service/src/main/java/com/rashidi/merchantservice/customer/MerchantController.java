package com.rashidi.merchantservice.customer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/merchants")
public class MerchantController {

  private final MerchantService merchantService;

  public MerchantController(MerchantService merchantService) {
    this.merchantService = merchantService;
  }

  @GetMapping
  public Collection<Merchant> getMerchants() {
    return merchantService.findAll();
  }

  @GetMapping(params = "local")
  public List<Merchant> getCustomersByCurrency() {
    return merchantService.findLocalMerchants();
  }

}
