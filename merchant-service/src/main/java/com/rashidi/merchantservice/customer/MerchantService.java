package com.rashidi.merchantservice.customer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Service
@RefreshScope
public class MerchantService {

  private static final Map<Long, Merchant> cache = new HashMap<Long, Merchant>() {{
    put(1L, new Merchant(1L, "Betsson group", "support@bestsson.com", "Sweden"));
    put(2L, new Merchant(2L, "Zalando group", "support@zalando.com", "Germany"));
  }};

  private final String defaultCountry;

  public MerchantService(@Value("${default-country}") String defaultCountry) {
    this.defaultCountry = defaultCountry;
  }

  Collection<Merchant> findAll() {
    return cache.values();
  }

  List<Merchant> findLocalMerchants() {
    return findAll().stream().filter(it -> it.getCountry().equals(defaultCountry)).collect(toList());
  }

}
