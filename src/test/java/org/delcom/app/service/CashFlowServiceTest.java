package org.delcom.app.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.delcom.app.entity.CashFlow;
import org.delcom.app.repository.CashFlowRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CashFlowServiceTest {

    @Test
    @DisplayName("Pengujian untuk service CashFlow")
    void testCashFlowService() throws Exception {
        // --- Setup ---
        UUID validId = UUID.randomUUID();
        UUID invalidId = UUID.randomUUID();

        // Membuat data dummy
        CashFlow dummyCashFlow = new CashFlow("Inflow", "Gaji", "gaji-bulanan", 5000000L, "Gaji bulan November");
        dummyCashFlow.setId(validId);

        // 1. Buat mock untuk Repository
        CashFlowRepository repoMock = Mockito.mock(CashFlowRepository.class);

        // 2. Atur perilaku mock
        when(repoMock.save(any(CashFlow.class))).thenReturn(dummyCashFlow);
        when(repoMock.findAll()).thenReturn(List.of(dummyCashFlow));
        when(repoMock.findByKeyword("gaji")).thenReturn(List.of(dummyCashFlow));
        when(repoMock.findById(validId)).thenReturn(Optional.of(dummyCashFlow));
        when(repoMock.findById(invalidId)).thenReturn(Optional.empty());
        when(repoMock.existsById(validId)).thenReturn(true);
        when(repoMock.existsById(invalidId)).thenReturn(false);
        doNothing().when(repoMock).deleteById(any(UUID.class));
        when(repoMock.findDistinctLabels()).thenReturn(List.of("gaji-bulanan", "belanja"));

        // 3. Buat instance Service dengan mock repository
        CashFlowService cashFlowService = new CashFlowService(repoMock);
        assert (cashFlowService != null);

        // --- Execute & Assert ---

        // Uji createCashFlow
        {
            CashFlow created = cashFlowService.createCashFlow(dummyCashFlow);
            assert (created != null);
            assert (created.getId().equals(validId));
            assert (created.getLabel().equals("gaji-bulanan"));
        }

        // Uji getAllCashFlows (tanpa search)
        {
            List<CashFlow> all = cashFlowService.getAllCashFlows(null);
            assert (all.size() == 1);
        }

        // Uji getAllCashFlows (dengan search "gaji")
        {
            List<CashFlow> filtered = cashFlowService.getAllCashFlows("gaji");
            assert (filtered.size() == 1);
        }

        // --- TAMBAHAN TES UNTUK BRANCH COVERAGE ---
        // Uji getAllCashFlows (dengan search string kosong)
        {
            // Tes ini "memaksa" Java untuk mengevaluasi !search.trim().isEmpty()
            // dan mendapatkan hasil false, yang akan jatuh ke findAll().
            List<CashFlow> all = cashFlowService.getAllCashFlows("   ");
            assert (all.size() == 1); // Harus mengembalikan semua, sama seperti search null
        }
        // --- AKHIR TAMBAHAN TES ---

        // Uji getCashFlowById (ditemukan)
        {
            CashFlow found = cashFlowService.getCashFlowById(validId);
            assert (found != null);
            assert (found.getId().equals(validId));
        }

        // Uji getCashFlowById (tidak ditemukan)
        {
            CashFlow notFound = cashFlowService.getCashFlowById(invalidId);
            assert (notFound == null);
        }

        // Uji updateCashFlow (ditemukan)
        {
            CashFlow updateData = new CashFlow("Outflow", "Belanja", "belanja-bulanan", 1000000L, "Belanja bulanan");
            // Atur mock save agar mengembalikan data yang sudah diupdate
            when(repoMock.save(any(CashFlow.class))).thenReturn(updateData);
            
            CashFlow updated = cashFlowService.updateCashFlow(validId, updateData);
            assert (updated != null);
            assert (updated.getType().equals("Outflow"));
            assert (updated.getLabel().equals("belanja-bulanan"));
        }

        // Uji updateCashFlow (tidak ditemukan)
        {
            CashFlow updateData = new CashFlow("Outflow", "Belanja", "belanja-bulanan", 1000000L, "Belanja bulanan");
            CashFlow updated = cashFlowService.updateCashFlow(invalidId, updateData);
            assert (updated == null);
        }

        // Uji deleteCashFlow (ditemukan)
        {
            boolean deleted = cashFlowService.deleteCashFlow(validId);
            assert (deleted == true);
        }

        // Uji deleteCashFlow (tidak ditemukan)
        {
            boolean deleted = cashFlowService.deleteCashFlow(invalidId);
            assert (deleted == false);
        }

        // Uji getLabels
        {
            List<String> labels = cashFlowService.getLabels();
            assert (labels.size() == 2);
            assert (labels.contains("gaji-bulanan"));
        }
    }
}