package com.example.claudedemo.fee.application;

import com.example.claudedemo.fee.domain.Fee;
import com.example.claudedemo.fee.domain.FeeNotFoundException;
import com.example.claudedemo.fee.domain.FeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FeeService {

    private final FeeRepository feeRepository;

    public List<FeeResponse> findAll() {
        return feeRepository.findAll().stream()
                .map(FeeResponse::from)
                .toList();
    }

    public FeeResponse findById(Long id) {
        Fee fee = feeRepository.findById(id)
                .orElseThrow(() -> new FeeNotFoundException(id));
        return FeeResponse.from(fee);
    }

    @Transactional
    public FeeResponse create(FeeRequest request) {
        Fee fee = Fee.create(
            request.getProductNumber(),
            request.getFeeValue(),
            request.getFeeUnit()
        );

        Fee saved = feeRepository.save(fee);
        return FeeResponse.from(saved);
    }

    @Transactional
    public FeeResponse update(Long id, FeeRequest request) {
        Fee fee = feeRepository.findById(id)
                .orElseThrow(() -> new FeeNotFoundException(id));

        fee.update(
            request.getProductNumber(),
            request.getFeeValue(),
            request.getFeeUnit()
        );

        Fee updated = feeRepository.save(fee);
        return FeeResponse.from(updated);
    }

    @Transactional
    public void delete(Long id) {
        if (!feeRepository.findById(id).isPresent()) {
            throw new FeeNotFoundException(id);
        }
        feeRepository.deleteById(id);
    }
}
