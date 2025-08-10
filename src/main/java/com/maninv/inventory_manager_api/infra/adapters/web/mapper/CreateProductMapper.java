package com.maninv.inventory_manager_api.infra.adapters.web.mapper;

import com.maninv.inventory_manager_api.application.dto.CreateProductCommand;
import com.maninv.inventory_manager_api.infra.adapters.web.dto.CreateProductRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreateProductMapper {

    CreateProductCommand toCommand(CreateProductRequest request);
}
