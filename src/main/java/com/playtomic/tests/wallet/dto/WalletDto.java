package com.playtomic.tests.wallet.dto;


import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WalletDto {

    private Long id;
    private BigDecimal currentBalance;
}
