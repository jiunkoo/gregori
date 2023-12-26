package com.gregori.product.service;

import java.util.List;

import com.gregori.common.exception.NotFoundException;
import com.gregori.product.domain.Sorter;
import com.gregori.product.dto.ProductCreateDto;
import com.gregori.product.dto.ProductResponseDto;
import com.gregori.product.dto.ProductUpdateDto;

public interface ProductService {

	Long saveProduct(ProductCreateDto productCreateDto);
	void updateProduct(ProductUpdateDto productUpdateDto) throws NotFoundException;
	void deleteProduct(Long productId) throws NotFoundException;
	ProductResponseDto getProduct(Long productId) throws NotFoundException;
	List<ProductResponseDto> getProducts(String keyword, Long categoryId, Long sellerId, int page, Sorter sorter);
}
