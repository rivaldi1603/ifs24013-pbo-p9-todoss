package org.delcom.app.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.UUID;

public class CashFlowTests {

    @Test
    @DisplayName("Membuat instance dari kelas CashFlow")
    void testMembuatInstanceCashFlow() throws Exception {
        UUID generatedId = UUID.randomUUID();

        // 1. Uji constructor
        {
            CashFlow cf = new CashFlow("Inflow", "Gaji", "gaji-bulanan", 5000000L, "Gaji bulan November");
            
            assert(cf.getType().equals("Inflow"));
            assert(cf.getSource().equals("Gaji"));
            assert(cf.getLabel().equals("gaji-bulanan"));
            assert(cf.getAmount() == 5000000L);
            assert(cf.getDescription().equals("Gaji bulan November"));
            assert(cf.getId() == null); // ID, createdAt, updatedAt diatur oleh JPA/callback
        }

        // 2. Uji default constructor dan setter
        {
            CashFlow cf = new CashFlow();
            cf.setId(generatedId);
            cf.setType("Outflow");
            cf.setSource("Tabungan");
            cf.setLabel("belanja-elektronik");
            cf.setAmount(1500000L);
            cf.setDescription("Beli monitor baru");

            // Panggil lifecycle callback secara manual untuk tes
            cf.onCreate();
            cf.onUpdate();

            assert(cf.getId().equals(generatedId));
            assert(cf.getType().equals("Outflow"));
            assert(cf.getSource().equals("Tabungan"));
            assert(cf.getLabel().equals("belanja-elektronik"));
            assert(cf.getAmount() == 1500000L);
            assert(cf.getDescription().equals("Beli monitor baru"));
            assert(cf.getCreatedAt() != null);
            assert(cf.getUpdatedAt() != null);
        }
    }
}
