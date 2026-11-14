package org.example.repository;

import org.example.model.VolunteerCampaign;
import org.example.model.VolunteerTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VolunteerTaskRepository extends JpaRepository<VolunteerTask, Long> {
    List<VolunteerTask> findByCampaign(VolunteerCampaign campaign);
}
