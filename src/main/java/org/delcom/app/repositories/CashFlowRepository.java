package org.delcom.app.repositories;

import java.util.List;
import java.util.UUID;

import org.delcom.app.entities.CashFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CashFlowRepository extends JpaRepository<CashFlow, UUID> {

    /**
     * Mencari CashFlow berdasarkan keyword.
     * Query ini akan mencari di field type, source, label, dan description.
     * Sesuai dengan kebutuhan service test.
     */
    @Query("SELECT c FROM CashFlow c WHERE LOWER(c.type) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(c.source) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(c.label) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<CashFlow> findByKeyword(String keyword);

    /**
     * Mengambil semua label unik/berbeda.
     * Sesuai dengan kebutuhan service test untuk getCashFlowLabels().
     */
    @Query("SELECT DISTINCT c.label FROM CashFlow c")
    List<String> findDistinctLabels();
}