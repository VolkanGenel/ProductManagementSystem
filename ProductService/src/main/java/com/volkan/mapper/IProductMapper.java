package com.volkan.mapper;

import com.volkan.dto.request.SaveProductRequestDto;
import com.volkan.dto.response.UpdateProductResponseDto;
import com.volkan.repository.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface IProductMapper {
    IProductMapper INSTANCE = Mappers.getMapper(IProductMapper.class);
    Product toProduct(final SaveProductRequestDto dto);
    UpdateProductResponseDto toUpdateProductResponseDto (final Product product);
}