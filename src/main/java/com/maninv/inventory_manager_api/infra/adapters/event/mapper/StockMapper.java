package com.maninv.inventory_manager_api.infra.adapters.event.mapper;

import com.maninv.inventory_manager_api.application.dto.UpdateStockCommand;
import com.maninv.inventory_manager_api.infra.adapters.event.dto.StockEventDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockMapper {

    UpdateStockCommand toCommand(StockEventDTO event);
}
