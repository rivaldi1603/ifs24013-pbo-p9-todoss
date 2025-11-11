package org.delcom.app.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cash_flows")
public class CashFlow {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    // Tipe: "Inflow" atau "Outflow"
    @Column(name = "type", nullable = false)
    private String type;

    // Sumber: "Gaji", "Tabungan", "Freelance"
    @Column(name = "source", nullable = false)
    private String source;

    // Kategori: "gaji-bulanan", "alat-elektronik"
    @Column(name = "label", nullable = false)
    private String label;

    // Nominal
    @Column(name = "amount", nullable = false)
    private Long amount; // Menggunakan Long untuk mata uang lebih aman dari Double

    // Deskripsi tambahan
    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // -- Constructors --

    public CashFlow() {
    }

    public CashFlow(String type, String source, String label, Long amount, String description) {
        this.type = type;
        this.source = source;
        this.label = label;
        this.amount = amount;
        this.description = description;
    }

    // -- Lifecycle Callbacks (Otomatis mengisi createdAt/updatedAt) --

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // -- Getters and Setters --

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}