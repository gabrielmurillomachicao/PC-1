package com.ejemplo.pc1.Repository;

import com.ejemplo.pc1.model.route;
import com.ejemplo.pc1.model.trip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface tripRepository extends JpaRepository<trip,Long> {
}
