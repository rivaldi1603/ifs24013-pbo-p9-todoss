package org.delcom.app.service;

import java.util.List;
import java.util.UUID;
import org.delcom.app.entity.CashFlow;
import org.delcom.app.repository.CashFlowRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CashFlowService {

    private final CashFlowRepository cashFlowRepository;

    public CashFlowService(CashFlowRepository cashFlowRepository) {
        this.cashFlowRepository = cashFlowRepository;
    }

    @Transactional
    public CashFlow createCashFlow(CashFlow cashFlowData) {
        // Objek CashFlow baru dibuat untuk memastikan hanya data yang relevan
        // yang disimpan
        CashFlow newCashFlow = new CashFlow(
                cashFlowData.getType(),
                cashFlowData.getSource(),
                cashFlowData.getLabel(),
                cashFlowData.getAmount(),
                cashFlowData.getDescription());
        return cashFlowRepository.save(newCashFlow);
    }

    public List<CashFlow> getAllCashFlows(String search) {
        if (search != null && !search.trim().isEmpty()) {
            return cashFlowRepository.findByKeyword(search);
        }
        return cashFlowRepository.findAll();
    }

    public CashFlow getCashFlowById(UUID id) {
        return cashFlowRepository.findById(id).orElse(null);
    }

    @Transactional
    public CashFlow updateCashFlow(UUID id, CashFlow updateData) {
        // Cari data yang ada
        CashFlow existingCashFlow = cashFlowRepository.findById(id).orElse(null);

        // Jika data ditemukan, perbarui field-nya
        if (existingCashFlow != null) {
            existingCashFlow.setType(updateData.getType());
            existingCashFlow.setSource(updateData.getSource());
            existingCashFlow.setLabel(updateData.getLabel());
            existingCashFlow.setAmount(updateData.getAmount());
            existingCashFlow.setDescription(updateData.getDescription());
            return cashFlowRepository.save(existingCashFlow);
        }
        // Jika tidak ditemukan, kembalikan null
        return null;
    }

    @Transactional
    public boolean deleteCashFlow(UUID id) {
        if (cashFlowRepository.existsById(id)) {
            cashFlowRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<String> getLabels() {
        return cashFlowRepository.findDistinctLabels();
    }
}