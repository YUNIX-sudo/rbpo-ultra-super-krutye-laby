package org.example.repository;

import org.example.model.User;
import org.example.model.VolunteerCampaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VolunteerCampaignRepository extends JpaRepository<VolunteerCampaign, Long> {
    List<VolunteerCampaign> findByCoordinator(User coordinator);
}
