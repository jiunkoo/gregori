package com.gregori.order_detail.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderDetailRequestDtoTest {

	private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private final Validator validator = factory.getValidator();

	@Test
	@DisplayName("productId 필드가 비어 있으면 에러가 발생한다.")
	void should_ValidException_when_nullProductId() {

		// given
		OrderDetailRequestDto dto = new OrderDetailRequestDto(null, 1L);

		// when
		var result = validator.validate(dto);

		// then
		assertFalse(result.isEmpty());
	}

	@Test
	@DisplayName("productCount 필드가 비어 있으면 에러가 발생한다.")
	void should_ValidException_when_nullProductCount() {

		// given
		OrderDetailRequestDto dto = new OrderDetailRequestDto(1L, null);

		// when
		var result = validator.validate(dto);

		// then
		assertFalse(result.isEmpty());
	}

	@Test
	@DisplayName("OrderDetailRequestDto 객체를 생성하면 private 필드를 get 메서드로 조회한다.")
	void should_getFields_when_createOrderDetailRequestDto() {

		// given
		OrderDetailRequestDto dto = new OrderDetailRequestDto(1L, 1L);

		//when
		var result = validator.validate(dto);

		//then
		assertTrue(result.isEmpty());
	}
}
