package findo.studio.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import findo.studio.data.entity.Studio;

@Repository
public interface StudioRepository extends JpaRepository<Studio, Integer> {
    boolean existsByName(String name);
    List<Studio> findByDeleted(boolean status);
}
