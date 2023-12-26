package com.gregori.order.service;

import java.util.List;

import com.gregori.common.exception.NotFoundException;
import com.gregori.order.dto.OrderRequestDto;
import com.gregori.order.dto.OrderResponseDto;

public interface OrderService {

	Long saveOrder(OrderRequestDto orderRequestDto) throws NotFoundException;
	void cancelOrder(Long memberId, Long orderId) throws NotFoundException;
	void cancelOrderDetail(Long memberId, Long orderDetailId) throws NotFoundException;
	OrderResponseDto getOrder(Long orderId) throws NotFoundException;
	List<OrderResponseDto> getOrders(Long memberId, int page);
}
