package com.example.smartticket.repository;
import com.example.smartticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findAllByUserId(Long userId);

    Optional<Ticket> findByIdAndUserId(Long id, Long userId);

}
