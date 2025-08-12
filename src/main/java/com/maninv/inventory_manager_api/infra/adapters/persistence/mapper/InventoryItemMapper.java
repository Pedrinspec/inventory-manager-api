package com.maninv.inventory_manager_api.infra.adapters.persistence.mapper;

import com.maninv.inventory_manager_api.domain.InventoryItem;
import com.maninv.inventory_manager_api.infra.adapters.persistence.entity.InventoryItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface InventoryItemMapper {

    @Mapping(ignore = true, target = "id")
    InventoryItemEntity toEntity(InventoryItem domainObject);

    InventoryItem toDomain(InventoryItemEntity entity);

    List<InventoryItemEntity> toEntityList(List<InventoryItem> list);

    List<InventoryItem> toDomainList(List<InventoryItemEntity> list);
}
