package com.supercoding.hackathon01.entity;

import com.supercoding.hackathon01.dto.home.request.RegisterHomeRequest;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "home")
public class Home {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Size(max = 45)
    @NotNull
    @Column(name = "transaction_type", nullable = false, length = 45)
    private String transactionType;

    @NotNull
    @Column(name = "deposit", nullable = false)
    private Long deposit;

    @Column(name = "price")
    private Long price;

    @NotNull
    @Column(name = "square_feet", nullable = false)
    private Integer squareFeet;

    @Column(name = "floor")
    private Integer floor;

    @Column(name = "maintenance_fee")
    private Long maintenanceFee;

    @Column(name = "is_parking")
    private Boolean isParking;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", insertable = false)
    private Instant updatedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @OneToMany(mappedBy = "home", cascade = CascadeType.ALL)
    private List<Picture> pictures = new ArrayList<>();

    @Size(max = 45)
    @NotNull
    @Column(name = "name", nullable = false, length = 45)
    private String name;

    public static Home of(RegisterHomeRequest homeRequest, User user, Category category) {
        return Home.builder()
                .category(category)
                .isDeleted(false)
                .user(user)
                .transactionType(homeRequest.getTransactionType())
                .name(homeRequest.getName())
                .deposit(homeRequest.getDeposit())
                .price(homeRequest.getPrice())
                .squareFeet(homeRequest.getSquareFeet())
                .floor(homeRequest.getFloor())
                .maintenanceFee(homeRequest.getMaintenanceFee())
                .isParking(homeRequest.getIsParking())
                .build();
    }

}