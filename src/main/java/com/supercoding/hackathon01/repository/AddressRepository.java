package com.supercoding.hackathon01.repository;

import com.supercoding.hackathon01.entity.Address;
import com.supercoding.hackathon01.entity.Home;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<Address> findByHome(Home home);

}
