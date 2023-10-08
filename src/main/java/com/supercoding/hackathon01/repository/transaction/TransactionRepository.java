package com.supercoding.hackathon01.repository.transaction;

import com.supercoding.hackathon01.dto.my_page.response.HomeListResponse;
import com.supercoding.hackathon01.dto.my_page.response.PurchaseListResponse;
import com.supercoding.hackathon01.dto.my_page.response.SellingListResponse;
import com.supercoding.hackathon01.dto.my_page.response.StatusCountResponse;
import com.supercoding.hackathon01.entity.Transaction;
import com.supercoding.hackathon01.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends TransactionCustomRepository, JpaRepository<Transaction, Long> {

    @Query("SELECT new com.supercoding.hackathon01.dto.my_page.response.SellingListResponse " +
            "(t.id, t.buyer.nickname, t.status.name, t.home.id, t.home.name, t.buyerFileUrl, a.address, p.url, t.home.deposit) " +
            "FROM Transaction t " +
            "JOIN t.seller " +
            "ON t.seller = :seller " +
            "JOIN t.home " +
            "JOIN t.status " +
            "JOIN Address a " +
            "ON a.home = t.home " +
            "JOIN Picture p " +
            "ON p.home = t.home " +
            "AND p.isThumbnail = true")
    Page<SellingListResponse> findBySellingList(@Param("seller") User user, Pageable pageable);

    @Query("SELECT new com.supercoding.hackathon01.dto.my_page.response.PurchaseListResponse " +
            "(t.id, t.seller.nickname, t.status.name, t.home.id, t.home.name, t.sellerFileUrl, a.address, p.url, t.home.deposit, t.accountNumber) " +
            "FROM Transaction t " +
            "JOIN t.buyer " +
            "ON t.buyer = :buyer " +
            "JOIN t.home " +
            "JOIN t.status " +
            "JOIN Address a " +
            "ON a.home = t.home " +
            "JOIN Picture p " +
            "ON p.home = t.home " +
            "AND p.isThumbnail = true")
    Page<PurchaseListResponse> findByPurchaseList(@Param("buyer") User user, Pageable pageable);

    @Query("SELECT new com.supercoding.hackathon01.dto.my_page.response.HomeListResponse " +
            "(h.id, h.name, h.category.name, h.transactionType, a.address, p.url, h.createdAt) " +
            "FROM Home h " +
            "JOIN h.user " +
            "ON h.user = :user " +
            "JOIN h.category " +
            "JOIN Address a " +
            "ON a.home = h " +
            "JOIN Picture p " +
            "ON p.home = h " +
            "AND p.isThumbnail = true")
    Page<HomeListResponse> findByMyHomeList(@Param("user") User user, Pageable pageable);

    @Query("SELECT new com.supercoding.hackathon01.dto.my_page.response.StatusCountResponse " +
            "(s.name, COUNT(t)) " +
            "FROM Status s " +
            "LEFT JOIN Transaction t " +
            "ON t.status = s " +
            "AND t.seller = :user " +
            "LEFT JOIN User u " +
            "ON t.seller = u " +
            "GROUP BY s")
    List<StatusCountResponse> countTransactionsByStatusForSeller(@Param("user") User user);

    @Query("SELECT new com.supercoding.hackathon01.dto.my_page.response.StatusCountResponse " +
            "(s.name, COUNT(t)) " +
            "FROM Status s " +
            "LEFT JOIN Transaction t " +
            "ON t.status = s " +
            "AND t.buyer = :user " +
            "LEFT JOIN User u " +
            "ON t.buyer = u " +
            "GROUP BY s")
    List<StatusCountResponse> countTransactionsByStatusForBuyer(@Param("user") User user);


}
