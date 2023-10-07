package com.supercoding.hackathon01.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "home_id", nullable = false)
    private Home home;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @Lob
    @Column(name = "seller_file_url")
    private String sellerFileUrl;

    @Lob
    @Column(name = "buyer_file_url")
    private String buyerFileUrl;

    public static Transaction of(Home home, User user) {
        return Transaction.builder()
                .seller(home.getUser())
                .buyer(user)
                .home(home)
                .status(new Status(1L, "거래신청"))
                .sellerFileUrl(null)
                .buyerFileUrl(null)
                .build();
    }

}