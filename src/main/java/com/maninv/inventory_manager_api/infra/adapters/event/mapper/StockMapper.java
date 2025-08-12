package com.maninv.inventory_manager_api.infra.adapters.event.mapper;

import com.maninv.inventory_manager_api.application.dto.UpdateStockCommand;
import com.maninv.inventory_manager_api.infra.adapters.event.dto.StockEventDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StockMapper {

    @Mapping(target = "storeId", source = "stock.storeId")
    @Mapping(target = "productId", source = "stock.productId")
    @Mapping(target = "quantityChange", source = "stock.quantityChange")
    UpdateStockCommand toCommand(StockEventDTO event);
}
