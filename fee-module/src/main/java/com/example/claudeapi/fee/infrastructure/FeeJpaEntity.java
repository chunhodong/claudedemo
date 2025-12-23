package com.example.claudeapi.fee.infrastructure;

import com.example.claudeapi.fee.domain.Fee;
import com.example.claudeapi.fee.domain.FeeUnit;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fees")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeeJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long productNumber;

    @Column(nullable = false)
    private Long feeValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeeUnit feeUnit;

    public FeeJpaEntity(Long id, Long productNumber, Long feeValue, FeeUnit feeUnit) {
        this.id = id;
        this.productNumber = productNumber;
        this.feeValue = feeValue;
        this.feeUnit = feeUnit;
    }

    public static FeeJpaEntity from(Fee fee) {
        return new FeeJpaEntity(
            fee.getId(),
            fee.getProductNumber(),
            fee.getFeeValue(),
            fee.getFeeUnit()
        );
    }

    public Fee toDomain() {
        return new Fee(id, productNumber, feeValue, feeUnit);
    }

    public void update(Long productNumber, Long feeValue, FeeUnit feeUnit) {
        this.productNumber = productNumber;
        this.feeValue = feeValue;
        this.feeUnit = feeUnit;
    }
}
