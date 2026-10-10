package org.example.repository;

import org.example.model.VolunteerCampaign;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class VolunteerCampaignRepository {
    private final Map<Long, VolunteerCampaign> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public VolunteerCampaign save(VolunteerCampaign campaign) {
        if (campaign.getId() == null) {
            campaign.setId(idGenerator.getAndIncrement());
        }
        storage.put(campaign.getId(), campaign);
        return campaign;
    }

    public Optional<VolunteerCampaign> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<VolunteerCampaign> findAll() {
        return new ArrayList<>(storage.values());
    }

    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }

    public List<VolunteerCampaign> findByCoordinatorId(Long coordinatorId) {
        return storage.values().stream()
                .filter(c -> coordinatorId.equals(c.getCoordinatorId()))
                .collect(Collectors.toList());
    }

    public boolean deleteById(Long id) {
        return storage.remove(id) != null;
    }
}
