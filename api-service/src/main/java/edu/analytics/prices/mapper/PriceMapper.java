package edu.analytics.prices.mapper;


import edu.analytics.common.EntityDtoMapper;
import edu.analytics.prices.dto.PriceDto;
import edu.analytics.prices.model.Price;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PriceMapper extends EntityDtoMapper<Price,PriceDto> {

    PriceDto toDto(Price price);

    List<PriceDto> toDtoList(List<Price> prices);

}
