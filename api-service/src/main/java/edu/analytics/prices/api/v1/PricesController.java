package edu.analytics.prices.api.v1;

import edu.analytics.common.BaseController;
import edu.analytics.prices.dto.PriceDto;
import edu.analytics.prices.mapper.PriceMapper;
import edu.analytics.prices.model.Price;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/v1/prices")
public class PricesController extends BaseController<Price, PriceDto> {

    public PricesController(MongoTemplate mongo, PriceMapper priceMapper) {
        super(mongo, Price.class, priceMapper);
    }
}
