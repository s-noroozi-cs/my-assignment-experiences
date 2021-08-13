package com.payconiq.stock.api.controller.impl;

import com.payconiq.stock.api.controller.StockController;
import com.payconiq.stock.api.model.StockRequest;
import com.payconiq.stock.api.model.StockResponse;
import com.payconiq.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
@Transactional
public class StockControllerImpl implements StockController {

    @Autowired
    private StockService stockService;

    @Override
    public Page<StockResponse> getAllStocks(Pageable pageable) {
        return stockService.findAllStock(pageable);
    }

    @Override
    public StockResponse getSpecificStock(Long stockId) {
        return stockService.findById(stockId);
    }

    @Override
    public StockResponse updateSpecificStock(Long stockId, StockRequest stockRequest) {
        return stockService.update(stockId, stockRequest);
    }

    @Override
    public StockResponse creatNewStock(StockRequest stockRequest) {
        return stockService.create(stockRequest);
    }

    @Override
    public void deleteSpecificStock(Long stockId) {
        stockService.delete(stockId);
    }
}
