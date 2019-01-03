package com.rashidi.merchantservice.customer;

public final class Merchant {

  private final long id;
  private final String name;
  private final String email;
  private final String country;

  public Merchant(long id, String name, String email, String country) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.country = country;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getCountry() {
    return country;
  }

}
