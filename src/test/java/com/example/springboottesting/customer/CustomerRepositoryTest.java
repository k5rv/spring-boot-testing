package com.example.springboottesting.customer;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest(properties = {"spring.jpa.properties.javax.persistence.validation.mode=none"})
class CustomerRepositoryTest {

  @Autowired private CustomerRepository underTest;

  @Test
  void itShouldSelectCustomerByPhoneNumber() {
    // Given
    UUID id = UUID.randomUUID();
    String phoneNumber = "000";
    Customer customer = new Customer(id, "Abel", phoneNumber);
    // When
    underTest.save(customer);
    // Then
    Optional<Customer> optionalCustomer = underTest.selectCustomerByPhoneNumber(phoneNumber);
    Assertions.assertThat(optionalCustomer)
        .isPresent()
        .hasValueSatisfying(c -> assertThat(c).usingRecursiveComparison().isEqualTo(customer));
  }

  @Test
  void itShouldSelectCustomerByPhoneNumberWhenNumberDoesNotExists() {
    // Given When
    Optional<Customer> optionalCustomer = underTest.selectCustomerByPhoneNumber("111");
    // Then
    Assertions.assertThat(optionalCustomer).isNotPresent();
  }

  @Test
  void itShouldNotSaveNewCustomerWhenNameIsNull() {
    // Given
    UUID id = UUID.randomUUID();
    Customer customer = new Customer(id, null, "000");

    // When Then
    assertThatThrownBy(() -> underTest.save(customer))
        .isInstanceOf(DataIntegrityViolationException.class)
        .hasMessage(
            "not-null property references a null or transient value : "
                + "com.example.springboottesting.customer.Customer.name");
  }

  @Test
  void itShouldNotSaveNewCustomerWhenPhoneNumberIsNull() {
    // Given
    UUID id = UUID.randomUUID();
    Customer customer = new Customer(id, "Alex", null);

    // When Then
    assertThatThrownBy(() -> underTest.save(customer))
        .isInstanceOf(DataIntegrityViolationException.class)
        .hasMessage(
            "not-null property references a null or transient value : "
                + "com.example.springboottesting.customer.Customer.phoneNumber");
  }
}
