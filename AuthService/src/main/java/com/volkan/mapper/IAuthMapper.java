package com.volkan.mapper;

import com.volkan.dto.request.RegisterRequestDto;
import com.volkan.dto.response.FindAllResponseDto;
import com.volkan.repository.entity.Auth;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface IAuthMapper {
    IAuthMapper INSTANCE = Mappers.getMapper(IAuthMapper.class);
    Auth toAuth(final RegisterRequestDto dto);
    FindAllResponseDto toFindAllResponseDto(final Auth auth);

}
