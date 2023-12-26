package com.gregori.order.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gregori.order.dto.OrderRequestDto;
import com.gregori.order.dto.OrderResponseDto;
import com.gregori.order.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static java.lang.Long.parseLong;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<Void> createOrder(@RequestBody @Valid OrderRequestDto dto) {

		Long orderId = orderService.saveOrder(dto);

		return ResponseEntity.created(URI.create("/order/" + orderId)).build();
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long orderId) {

		OrderResponseDto response = orderService.getOrder(orderId);

		return ResponseEntity.ok().body(response);
	}

	@GetMapping
	public ResponseEntity<List<OrderResponseDto>> getOrders(@RequestParam(defaultValue = "1") int page) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		long memberId = parseLong(authentication.getName());

		List<OrderResponseDto> response = orderService.getOrders(memberId, page);

		return ResponseEntity.ok().body(response);
	}
}
