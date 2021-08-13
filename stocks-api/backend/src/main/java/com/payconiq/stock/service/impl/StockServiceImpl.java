package com.payconiq.stock.service.impl;

import com.payconiq.stock.api.model.StockRequest;
import com.payconiq.stock.api.model.StockResponse;
import com.payconiq.stock.entity.StockEntity;
import com.payconiq.stock.exception.PersistenceException;
import com.payconiq.stock.exception.StockDataIntegrityException;
import com.payconiq.stock.exception.StockNotFoundException;
import com.payconiq.stock.repository.StockRepository;
import com.payconiq.stock.service.StockService;
import io.vavr.control.Try;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.function.Supplier;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ModelMapper modelMapper;

    private Supplier<StockNotFoundException> makeNotFoundException(Long stockId) {
        return () -> new StockNotFoundException("There is not exist any stock with id: " + stockId);
    }

    private void handlePersistenceException(Throwable ex) {
        if (ex.getClass().isAssignableFrom(DataIntegrityViolationException.class)) {
            throw new StockDataIntegrityException(ex.getMessage(), ex);
        } else {
            throw new PersistenceException(ex.getMessage(), ex);
        }
    }

    @Override
    public Page<StockResponse> findAllStock(Pageable pageable) {
        return stockRepository.findAll(pageable)
                .map(entity -> modelMapper.map(entity, StockResponse.class));
    }

    @Override
    public StockResponse findById(Long stockId) {
        return stockRepository
                .findById(stockId)
                .map(entity -> modelMapper.map(entity, StockResponse.class))
                .orElseThrow(makeNotFoundException(stockId));
    }

    @Override
    public StockResponse update(Long stockId, StockRequest stockRequest) {
        StockEntity entity = stockRepository
                .findById(stockId)
                .orElseThrow(makeNotFoundException(stockId));

        modelMapper.map(stockRequest, entity);
        entity.setLastUpdate(LocalDateTime.now());
        return modelMapper.map(
                Try.of(() -> stockRepository.saveAndFlush(entity))
                        .onFailure(this::handlePersistenceException)
                        .get()
                , StockResponse.class);
    }

    @Override
    public StockResponse create(StockRequest stockRequest) {
        StockEntity entity = modelMapper.map(stockRequest, StockEntity.class);
        entity.setCreateTime(LocalDateTime.now());
        Try.of(() -> stockRepository.saveAndFlush(entity)).onFailure(this::handlePersistenceException);
        return modelMapper.map(entity, StockResponse.class);
    }

    @Override
    public void delete(Long stockId) {
        //I think checking before deletion have performance issue
        //However some api design preferred to prevent deletion of not exist resource
        stockRepository.findById(stockId).orElseThrow(makeNotFoundException(stockId));

        stockRepository.deleteById(stockId);
    }
}
