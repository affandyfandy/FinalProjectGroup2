package findo.studio.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import findo.studio.data.entity.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer> {

}
