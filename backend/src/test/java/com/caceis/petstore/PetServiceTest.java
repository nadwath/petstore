package com.caceis.petstore;
import com.caceis.petstore.common.PetStatus;
import com.caceis.petstore.domain.Pet;
import com.caceis.petstore.repo.PetRepo;
import com.caceis.petstore.service.impl.PetServiceImpl;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.mockito.Mockito.*;
public class PetServiceTest {
  @Test void listUsesRepo(){
    PetRepo repo = mock(PetRepo.class);
    when(repo.findAll()).thenReturn(List.of(Pet.builder().name("Scobby doo").status(PetStatus.AVAILABLE).build()));
    PetServiceImpl svc = new PetServiceImpl(repo);
    assert svc.list().size()==1;
  }
}
