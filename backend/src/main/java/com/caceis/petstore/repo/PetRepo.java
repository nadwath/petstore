package com.caceis.petstore.repo;

import com.caceis.petstore.common.PetStatus;
import com.caceis.petstore.domain.Pet;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PetRepo extends JpaRepository<Pet, Long> {
    List<Pet> findByStatus(String status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Pet p WHERE p.id = :petId")
    Optional<Pet> findByIdWithLock(@Param("petId") Long petId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Pet p WHERE p.id = :petId AND p.status = :status")
    Optional<Pet> findByIdAndStatusWithLock(@Param("petId") Long petId, @Param("status") PetStatus status);
}
