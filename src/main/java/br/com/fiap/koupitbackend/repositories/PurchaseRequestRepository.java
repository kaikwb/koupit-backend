package br.com.fiap.koupitbackend.repositories;

import br.com.fiap.koupitbackend.models.PurchaseRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, Long> {

}
