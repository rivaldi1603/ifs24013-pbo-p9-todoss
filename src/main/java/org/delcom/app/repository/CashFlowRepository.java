package org.delcom.app.repository;

import java.util.List;
import java.util.UUID;
import org.delcom.app.entity.CashFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CashFlowRepository extends JpaRepository<CashFlow, UUID> {

    /**
     * Mencari CashFlow berdasarkan keyword.
     * Keyword akan dicocokkan (case-insensitive) dengan source, label, atau
     * description.
     */
    @Query("SELECT c FROM CashFlow c WHERE LOWER(c.source) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.label) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<CashFlow> findByKeyword(String keyword);

    /**
     * Mengambil semua label unik dari tabel cash_flows.
     * Digunakan untuk endpoint /labels.
     */
    @Query("SELECT DISTINCT c.label FROM CashFlow c ORDER BY c.label ASC")
    List<String> findDistinctLabels();
}