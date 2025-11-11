package org.delcom.app.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.delcom.app.configs.ApiResponse;
import org.delcom.app.entity.CashFlow;
import org.delcom.app.service.CashFlowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CashFlowControllerTest {

    private CashFlowService serviceMock;
    private CashFlowController controller;
    private CashFlow dummyCashFlow;
    private UUID validId = UUID.randomUUID();
    private UUID invalidId = UUID.randomUUID();

    // Fungsi helper untuk membuat data valid
    private CashFlow createValidCashFlow() {
        return new CashFlow("Inflow", "Gaji", "gaji-bulanan", 5000000L, "Gaji");
    }

    @BeforeEach
    void setUp() {
        // --- Setup Mocks ---
        serviceMock = Mockito.mock(CashFlowService.class);
        controller = new CashFlowController(serviceMock);

        // Membuat data dummy
        dummyCashFlow = createValidCashFlow();
        dummyCashFlow.setId(validId);

        // --- Atur Perilaku Mock ---
        when(serviceMock.createCashFlow(any(CashFlow.class))).thenReturn(dummyCashFlow);
        when(serviceMock.getAllCashFlows(null)).thenReturn(List.of(dummyCashFlow));
        when(serviceMock.getCashFlowById(validId)).thenReturn(dummyCashFlow);
        when(serviceMock.getCashFlowById(invalidId)).thenReturn(null);
        // Perilaku update:
        // - Jika ID valid, kembalikan data
        // - Jika ID tidak valid, service akan mengembalikan null (disimulasikan di tes spesifik)
        when(serviceMock.updateCashFlow(any(UUID.class), any(CashFlow.class))).thenReturn(dummyCashFlow);
        when(serviceMock.deleteCashFlow(validId)).thenReturn(true);
        when(serviceMock.deleteCashFlow(invalidId)).thenReturn(false);
        when(serviceMock.getLabels()).thenReturn(List.of("gaji-bulanan"));
    }

    // --- TES SKENARIO SUKSES (HAPPY PATH) ---

    @Test
    @DisplayName("Create CashFlow: Skenario Sukses")
    void testCreateCashFlow_Valid() {
        ApiResponse<Map<String, UUID>> response = controller.createCashFlow(dummyCashFlow);
        assert (response.getStatus().equals("success"));
        assert (response.getData().get("id").equals(validId));
    }

    @Test
    @DisplayName("Get All CashFlows: Skenario Sukses")
    void testGetAllCashFlows() {
        ApiResponse<Map<String, List<CashFlow>>> response = controller.getAllCashFlows(null);
        assert (response.getStatus().equals("success"));
        assert (response.getData().get("cash_flows").size() == 1);
    }

    @Test
    @DisplayName("Get CashFlow by ID: Skenario Sukses (Ditemukan)")
    void testGetCashFlowById_Valid() {
        ApiResponse<Map<String, CashFlow>> response = controller.getCashFlowById(validId);
        assert (response.getStatus().equals("success"));
        assert (response.getData().get("cash_flow").getId().equals(validId));
    }

    @Test
    @DisplayName("Get CashFlow by ID: Skenario Gagal (Tidak Ditemukan)")
    void testGetCashFlowById_Invalid() {
        ApiResponse<Map<String, CashFlow>> response = controller.getCashFlowById(invalidId);
        assert (response.getStatus().equals("fail"));
    }

    @Test
    @DisplayName("Update CashFlow: Skenario Sukses")
    void testUpdateCashFlow_Valid() {
        ApiResponse<String> response = controller.updateCashFlow(validId, dummyCashFlow);
        assert (response.getStatus().equals("success"));
    }

    @Test
    @DisplayName("Update CashFlow: Skenario Gagal (ID Tidak Ditemukan)")
    void testUpdateCashFlow_NotFound() {
        // Atur mock service untuk mengembalikan null saat update gagal
        when(serviceMock.updateCashFlow(invalidId, dummyCashFlow)).thenReturn(null);
        ApiResponse<String> response = controller.updateCashFlow(invalidId, dummyCashFlow);
        assert (response.getStatus().equals("fail"));
    }

    @Test
    @DisplayName("Delete CashFlow: Skenario Sukses")
    void testDeleteCashFlow_Valid() {
        ApiResponse<String> response = controller.deleteCashFlow(validId);
        assert (response.getStatus().equals("success"));
    }

    @Test
    @DisplayName("Delete CashFlow: Skenario Gagal (Tidak Ditemukan)")
    void testDeleteCashFlow_Invalid() {
        ApiResponse<String> response = controller.deleteCashFlow(invalidId);
        assert (response.getStatus().equals("fail"));
    }

    @Test
    @DisplayName("Get Labels: Skenario Sukses")
    void testGetLabels() {
        ApiResponse<Map<String, List<String>>> response = controller.getLabels();
        assert (response.getStatus().equals("success"));
        assert (response.getData().get("labels").size() == 1);
    }

    // --- TES VALIDASI (BRANCH COVERAGE) ---

    @Test
    @DisplayName("Create & Update: Menguji semua cabang validasi data tidak valid")
    void testValidationBranches() {
        CashFlow data;

        // --- Tes Validasi untuk createCashFlow ---
        // Skenario 1: Tipe Null
        data = createValidCashFlow();
        data.setType(null);
        ApiResponse<Map<String, UUID>> resCreate = controller.createCashFlow(data);
        assert (resCreate.getStatus().equals("fail"));

        // Skenario 2: Tipe Kosong
        data = createValidCashFlow();
        data.setType("");
        resCreate = controller.createCashFlow(data);
        assert (resCreate.getStatus().equals("fail"));

        // Skenario 3: Source Null
        data = createValidCashFlow();
        data.setSource(null);
        resCreate = controller.createCashFlow(data);
        assert (resCreate.getStatus().equals("fail"));

        // Skenario 4: Source Kosong
        data = createValidCashFlow();
        data.setSource("");
        resCreate = controller.createCashFlow(data);
        assert (resCreate.getStatus().equals("fail"));

        // Skenario 5: Label Null
        data = createValidCashFlow();
        data.setLabel(null);
        resCreate = controller.createCashFlow(data);
        assert (resCreate.getStatus().equals("fail"));

        // Skenario 6: Label Kosong
        data = createValidCashFlow();
        data.setLabel("");
        resCreate = controller.createCashFlow(data);
        assert (resCreate.getStatus().equals("fail"));

        // Skenario 7: Amount Null
        data = createValidCashFlow();
        data.setAmount(null);
        resCreate = controller.createCashFlow(data);
        assert (resCreate.getStatus().equals("fail"));

        // Skenario 8: Amount <= 0
        data = createValidCashFlow();
        data.setAmount(0L);
        resCreate = controller.createCashFlow(data);
        assert (resCreate.getStatus().equals("fail"));

        // --- Tes Validasi untuk updateCashFlow ---
        // Skenario 1: Tipe Null
        data = createValidCashFlow();
        data.setType(null);
        ApiResponse<String> resUpdate = controller.updateCashFlow(validId, data);
        assert (resUpdate.getStatus().equals("fail"));

        // Skenario 2: Tipe Kosong
        data = createValidCashFlow();
        data.setType("");
        resUpdate = controller.updateCashFlow(validId, data);
        assert (resUpdate.getStatus().equals("fail"));

        // Skenario 3: Source Null
        data = createValidCashFlow();
        data.setSource(null);
        resUpdate = controller.updateCashFlow(validId, data);
        assert (resUpdate.getStatus().equals("fail"));

        // Skenario 4: Source Kosong
        data = createValidCashFlow();
        data.setSource("");
        resUpdate = controller.updateCashFlow(validId, data);
        assert (resUpdate.getStatus().equals("fail"));

        // Skenario 5: Label Null
        data = createValidCashFlow();
        data.setLabel(null);
        resUpdate = controller.updateCashFlow(validId, data);
        assert (resUpdate.getStatus().equals("fail"));

        // Skenario 6: Label Kosong
        data = createValidCashFlow();
        data.setLabel("");
        resUpdate = controller.updateCashFlow(validId, data);
        assert (resUpdate.getStatus().equals("fail"));

        // Skenario 7: Amount Null
        data = createValidCashFlow();
        data.setAmount(null);
        resUpdate = controller.updateCashFlow(validId, data);
        assert (resUpdate.getStatus().equals("fail"));

        // Skenario 8: Amount <= 0 (Skenario 4 Anda sebelumnya)
        data = createValidCashFlow();
        data.setAmount(0L);
        resUpdate = controller.updateCashFlow(validId, data);
        assert (resUpdate.getStatus().equals("fail"));
    }
}