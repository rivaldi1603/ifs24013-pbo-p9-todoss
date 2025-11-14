package org.delcom.app.controllers;


import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.delcom.app.configs.ApiResponse;
import org.delcom.app.entities.CashFlow;
import org.delcom.app.services.CashFlowService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cash-flows") // Sesuai dengan app.rest di PDF
public class CashFlowController {

    private final CashFlowService cashFlowService;

    public CashFlowController(CashFlowService cashFlowService) {
        this.cashFlowService = cashFlowService;
    }

    @PostMapping
    public ApiResponse<Map<String, UUID>> createCashFlow(@RequestBody CashFlow cashFlow) {
        // Validasi sesuai test case
        if (cashFlow.getType() == null || cashFlow.getType().trim().isEmpty() ||
            cashFlow.getSource() == null || cashFlow.getSource().trim().isEmpty() ||
            cashFlow.getLabel() == null || cashFlow.getLabel().trim().isEmpty() ||
            cashFlow.getAmount() == null || cashFlow.getAmount() <= 0 || // Test case memeriksa '0' dan 'null'
            cashFlow.getDescription() == null || cashFlow.getDescription().trim().isEmpty()) {
            return new ApiResponse<>("fail", "Data tidak valid", null);
        }

        CashFlow newCashFlow = cashFlowService.createCashFlow(
                cashFlow.getType(),
                cashFlow.getSource(),
                cashFlow.getLabel(),
                cashFlow.getAmount(),
                cashFlow.getDescription()
        );

        // Response message dari PDF
        return new ApiResponse<>("success", "Berhasil menambahkan data", Map.of("id", newCashFlow.getId()));
    }

    @GetMapping
    public ApiResponse<Map<String, List<CashFlow>>> getAllCashFlows(@RequestParam(required = false) String search) {
        List<CashFlow> cashFlows = cashFlowService.getAllCashFlows(search);
        // Response message dan key "cash_flows" dari PDF
        return new ApiResponse<>("success", "Berhasil mengambil data", Map.of("cash_flows", cashFlows));
    }

    @GetMapping("/{id}")
    public ApiResponse<Map<String, CashFlow>> getCashFlowById(@PathVariable UUID id) {
        CashFlow cashFlow = cashFlowService.getCashFlowById(id);

        if (cashFlow == null) {
            return new ApiResponse<>("fail", "Data cash flow tidak ditemukan", null); // Message disesuaikan
        }
        // Response message dan key "cash_flow" dari PDF
        return new ApiResponse<>("success", "Berhasil mengambil data", Map.of("cash_flow", cashFlow));
    }

    @GetMapping("/labels") // Sesuai app.rest di PDF
    public ApiResponse<Map<String, List<String>>> getCashFlowLabels() {
        List<String> labels = cashFlowService.getCashFlowLabels();
        // Response message dan key "labels" dari PDF
        return new ApiResponse<>("success", "Berhasil mengambil data", Map.of("labels", labels));
    }

    @PutMapping("/{id}")
    public ApiResponse<CashFlow> updateCashFlow(@PathVariable UUID id, @RequestBody CashFlow cashFlow) {
        // Validasi sesuai test case
        if (cashFlow.getType() == null || cashFlow.getType().trim().isEmpty() ||
            cashFlow.getSource() == null || cashFlow.getSource().trim().isEmpty() ||
            cashFlow.getLabel() == null || cashFlow.getLabel().trim().isEmpty() ||
            cashFlow.getAmount() == null || cashFlow.getAmount() <= 0 ||
            cashFlow.getDescription() == null || cashFlow.getDescription().trim().isEmpty()) {
            return new ApiResponse<>("fail", "Data tidak valid", null);
        }

        CashFlow updatedCashFlow = cashFlowService.updateCashFlow(
                id,
                cashFlow.getType(),
                cashFlow.getSource(),
                cashFlow.getLabel(),
                cashFlow.getAmount(),
                cashFlow.getDescription()
        );

        if (updatedCashFlow == null) {
            return new ApiResponse<>("fail", "Data cash flow tidak ditemukan", null); // Message disesuaikan
        }

        // Response message dari PDF (data: null)
        return new ApiResponse<>("success", "Berhasil memperbarui data", null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteCashFlow(@PathVariable UUID id) {
        boolean deleted = cashFlowService.deleteCashFlow(id);

        if (!deleted) {
            return new ApiResponse<>("fail", "Data cash flow tidak ditemukan", null); // Message disesuaikan
        }
        // Response message dari PDF
        return new ApiResponse<>("success", "Berhasil menghapus data", null);
    }
}
