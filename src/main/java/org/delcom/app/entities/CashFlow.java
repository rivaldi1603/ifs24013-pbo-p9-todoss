package org.delcom.app.entities;


import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cash_flows") // Sesuai dengan endpoint /cash-flows di PDF
public class CashFlow {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "source", nullable = false)
    private String source;

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Default constructor (diperlukan oleh JPA dan test case)
    public CashFlow() {
    }

    // Constructor untuk data (sesuai test case)
    public CashFlow(String type, String source, String label, Integer amount, String description) {
        this.type = type;
        this.source = source;
        this.label = label;
        this.amount = amount;
        this.description = description;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getSource() {
        return source;
    }

    public String getLabel() {
        return label;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Setters
    public void setId(UUID id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Lifecycle callbacks (sesuai test case)
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}