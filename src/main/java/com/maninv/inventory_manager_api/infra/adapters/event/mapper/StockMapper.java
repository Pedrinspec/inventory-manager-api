package com.maninv.inventory_manager_api.infra.adapters.event.mapper;

import com.maninv.inventory_manager_api.application.dto.UpdateStockCommand;
import com.maninv.inventory_manager_api.infra.adapters.event.dto.StockEventDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StockMapper {

    @Mapping(target = "storeId", source = "payload.storeId")
    @Mapping(target = "productId", source = "payload.productId")
    @Mapping(target = "quantityChange", source = "payload.quantityChange")
    UpdateStockCommand toCommand(StockEventDTO event);
}
