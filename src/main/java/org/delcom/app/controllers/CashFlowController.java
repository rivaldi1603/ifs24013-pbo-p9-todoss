package org.delcom.app.controllers;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.delcom.app.configs.ApiResponse;
import org.delcom.app.entity.CashFlow;
import org.delcom.app.service.CashFlowService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cash-flows")
public class CashFlowController {

    private final CashFlowService cashFlowService;

    // Service di-inject melalui constructor
    public CashFlowController(CashFlowService cashFlowService) {
        this.cashFlowService = cashFlowService;
    }

    // 1. Membuat Cash Flow baru
    // Sesuai spec: POST /api/cash-flows
    @PostMapping
    public ApiResponse<Map<String, UUID>> createCashFlow(@RequestBody CashFlow cashFlow) {
        // Validasi input sederhana
        if (cashFlow.getType() == null || cashFlow.getType().isEmpty() ||
                cashFlow.getSource() == null || cashFlow.getSource().isEmpty() ||
                cashFlow.getLabel() == null || cashFlow.getLabel().isEmpty() ||
                cashFlow.getAmount() == null || cashFlow.getAmount() <= 0) {
            return new ApiResponse<>("fail", "Data tidak valid", null);
        }

        CashFlow newCashFlow = cashFlowService.createCashFlow(cashFlow);
        return new ApiResponse<>(
                "success",
                "Berhasil menambahkan data",
                Map.of("id", newCashFlow.getId()));
    }

    // 2. Mendapatkan semua Cash Flow (dengan/tanpa search)
    // Sesuai spec: GET /api/cash-flows
    @GetMapping
    public ApiResponse<Map<String, List<CashFlow>>> getAllCashFlows(
            @RequestParam(required = false) String search) {
        List<CashFlow> cashFlows = cashFlowService.getAllCashFlows(search);
        return new ApiResponse<>(
                "success",
                "Berhasil mengambil data",
                // Menggunakan key "cash_flows" sesuai spec Bagian D
                Map.of("cash_flows", cashFlows));
    }

    // 3. Mendapatkan Cash Flow by ID
    // Sesuai spec: GET /api/cash-flows/{id}
    @GetMapping("/{id}")
    public ApiResponse<Map<String, CashFlow>> getCashFlowById(@PathVariable UUID id) {
        CashFlow cashFlow = cashFlowService.getCashFlowById(id);
        if (cashFlow == null) {
            return new ApiResponse<>("fail", "Data todo tidak ditemukan", null);
        }
        return new ApiResponse<>(
                "success",
                "Berhasil mengambil data",
                // Menggunakan key "cash_flow" sesuai spec Bagian D
                Map.of("cash_flow", cashFlow));
    }

    // 4. Update Cash Flow by ID
    // Sesuai spec: PUT /api/cash-flows/{id}
    @PutMapping("/{id}")
    public ApiResponse<String> updateCashFlow(@PathVariable UUID id, @RequestBody CashFlow cashFlow) {
        // Validasi input
        if (cashFlow.getType() == null || cashFlow.getType().isEmpty() ||
                cashFlow.getSource() == null || cashFlow.getSource().isEmpty() ||
                cashFlow.getLabel() == null || cashFlow.getLabel().isEmpty() ||
                cashFlow.getAmount() == null || cashFlow.getAmount() <= 0) {
            return new ApiResponse<>("fail", "Data tidak valid", null);
        }

        CashFlow updatedCashFlow = cashFlowService.updateCashFlow(id, cashFlow);
        if (updatedCashFlow == null) {
            return new ApiResponse<>("fail", "Data todo tidak ditemukan", null);
        }
        return new ApiResponse<>("success", "Berhasil memperbarui data", null);
    }

    // 5. Hapus Cash Flow by ID
    // Sesuai spec: DELETE /api/cash-flows/{id}
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteCashFlow(@PathVariable UUID id) {
        boolean status = cashFlowService.deleteCashFlow(id);
        if (!status) {
            return new ApiResponse<>("fail", "Data todo tidak ditemukan", null);
        }
        return new ApiResponse<>("success", "Berhasil menghapus data", null);
    }

    // 6. Mendapatkan semua label unik
    // Sesuai spec: GET /api/cash-flows/labels
    @GetMapping("/labels")
    public ApiResponse<Map<String, List<String>>> getLabels() {
        List<String> labels = cashFlowService.getLabels();
        return new ApiResponse<>(
                "success",
                "Berhasil mengambil data",
                // Menggunakan key "labels" sesuai spec Bagian D
                Map.of("labels", labels));
    }
}