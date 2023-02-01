package com.example.springboottesting.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

class CustomerRegistrationServiceTest {

  @Mock private CustomerRepository customerRepository;

  @Captor private ArgumentCaptor<Customer> customerArgumentCaptor;
  private CustomerRegistrationService underTest;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    underTest = new CustomerRegistrationService(customerRepository);
  }

  @Test
  void itShouldSaveNewCustomer() {
    // Given
    String phoneNumber = "00099";
    Customer customer = new Customer(UUID.randomUUID(), "Maryam", phoneNumber);
    CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);
    given(customerRepository.selectCustomerByPhoneNumber(phoneNumber)).willReturn(Optional.empty());
    // When
    underTest.registerNewCustomer(request);
    // Then
    then(customerRepository).should().save(customerArgumentCaptor.capture());
    Customer customerArgumentCaptorValue = customerArgumentCaptor.getValue();
    assertThat(customerArgumentCaptorValue).isEqualTo(customer);
  }

  @Test
  void itShouldNotSaveNewCustomerWhenCustomerExists() {
    // Given
    String phoneNumber = "00099";
    UUID id = UUID.randomUUID();
    Customer customer = new Customer(id, "Maryam", phoneNumber);
    CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);
    given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
        .willReturn(Optional.of(customer));
    // When
    underTest.registerNewCustomer(request);
    // Then
    then(customerRepository).should().selectCustomerByPhoneNumber(phoneNumber);
    then(customerRepository).shouldHaveNoMoreInteractions();
  }

  @Test
  void itShouldThrowWhenPhoneNumberIsTaken() {
    // Given
    String phoneNumber = "00099";
    UUID id = UUID.randomUUID();
    Customer customer = new Customer(id, "Maryam", phoneNumber);
    Customer customerTwo = new Customer(id, "John", phoneNumber);
    CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);
    given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
        .willReturn(Optional.of(customerTwo));
    // When Then
    assertThatThrownBy(() -> underTest.registerNewCustomer(request))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("phone number [" + phoneNumber + "] is taken");
    // And Then
    then(customerRepository).should(never()).save(any(Customer.class));
  }

  @Test
  void itShouldSaveNewCustomerWhenIdIsNull() {
    // Given
    String phoneNumber = "00099";
    Customer customer = new Customer(UUID.randomUUID(), "Maryam", phoneNumber);
    CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);
    given(customerRepository.selectCustomerByPhoneNumber(phoneNumber)).willReturn(Optional.empty());
    // When
    underTest.registerNewCustomer(request);
    // Then
    then(customerRepository).should().save(customerArgumentCaptor.capture());
    Customer customerArgumentCaptorValue = customerArgumentCaptor.getValue();
    assertThat(customerArgumentCaptorValue)
        .usingRecursiveComparison()
        .ignoringFields("id")
        .isEqualTo(customer);
    assertThat(customerArgumentCaptorValue.getId()).isNotNull();
  }
}
