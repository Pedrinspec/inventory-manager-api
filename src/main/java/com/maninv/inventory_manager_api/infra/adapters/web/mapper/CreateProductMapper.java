package com.maninv.inventory_manager_api.infra.adapters.web.mapper;

import com.maninv.inventory_manager_api.application.dto.CreateProductCommand;
import com.maninv.inventory_manager_api.infra.adapters.web.dto.CreateProductRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CreateProductMapper {

    @Mapping(target = "initialStock", source = "initialStock")
    CreateProductCommand toCommand(CreateProductRequest request);

}
