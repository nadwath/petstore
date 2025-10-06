package com.caceis.petstore.repo;
import com.caceis.petstore.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface PetRepo extends JpaRepository<Pet,Long> {
  List<Pet> findByStatus(String status);
}
