package org.example.repository;

import org.example.model.User;
import org.example.model.VolunteerResponse;
import org.example.model.VolunteerTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VolunteerResponseRepository extends JpaRepository<VolunteerResponse, Long> {
    List<VolunteerResponse> findByTask(VolunteerTask task);
    List<VolunteerResponse> findByVolunteer(User volunteer);
}
