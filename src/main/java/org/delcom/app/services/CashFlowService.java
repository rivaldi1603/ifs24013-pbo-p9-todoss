package org.delcom.app.services;

import java.util.List;
import java.util.UUID;

import org.delcom.app.entities.CashFlow;
import org.delcom.app.repositories.CashFlowRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CashFlowService {

    private final CashFlowRepository cashFlowRepository;

    // Constructor-based injection
    public CashFlowService(CashFlowRepository cashFlowRepository) {
        this.cashFlowRepository = cashFlowRepository;
    }

    @Transactional
    public CashFlow createCashFlow(String type, String source, String label, Integer amount, String description) {
        CashFlow cashFlow = new CashFlow(type, source, label, amount, description);
        return cashFlowRepository.save(cashFlow);
    }

    public List<CashFlow> getAllCashFlows(String search) {
        // Sesuai logika di test case
        if (search != null && !search.trim().isEmpty()) {
            return cashFlowRepository.findByKeyword(search);
        }
        return cashFlowRepository.findAll();
    }

    public CashFlow getCashFlowById(UUID id) {
        return cashFlowRepository.findById(id).orElse(null);
    }

    public List<String> getCashFlowLabels() {
        return cashFlowRepository.findDistinctLabels();
    }

    @Transactional
    public CashFlow updateCashFlow(UUID id, String type, String source, String label, Integer amount, String description) {
        CashFlow cashFlow = cashFlowRepository.findById(id).orElse(null);

        // Sesuai logika di test case, return null jika tidak ada
        if (cashFlow != null) {
            cashFlow.setType(type);
            cashFlow.setSource(source);
            cashFlow.setLabel(label);
            cashFlow.setAmount(amount);
            cashFlow.setDescription(description);
            return cashFlowRepository.save(cashFlow);
        }
        return null;
    }

    @Transactional
    public boolean deleteCashFlow(UUID id) {
        // Sesuai logika di test case
        if (cashFlowRepository.existsById(id)) {
            cashFlowRepository.deleteById(id);
            return true;
        }
        return false;
    }
}