package muslimdev.spring6restmvc.repositories;

import muslimdev.spring6restmvc.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
}
