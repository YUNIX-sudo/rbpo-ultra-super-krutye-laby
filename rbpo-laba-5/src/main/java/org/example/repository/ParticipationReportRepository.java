package org.example.repository;

import org.example.model.ParticipationReport;
import org.example.model.VolunteerResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipationReportRepository extends JpaRepository<ParticipationReport, Long> {
    Optional<ParticipationReport> findByResponse(VolunteerResponse response);
}
