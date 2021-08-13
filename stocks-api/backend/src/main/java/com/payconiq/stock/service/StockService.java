package com.payconiq.stock.service;

import com.payconiq.stock.api.model.StockRequest;
import com.payconiq.stock.api.model.StockResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface StockService {
    Page<StockResponse> findAllStock(Pageable pageable);

    StockResponse findById(Long stockId);

    StockResponse update(Long stockId, StockRequest stockRequest);

    StockResponse create(StockRequest stockRequest);

    void delete(Long stockId);
}
