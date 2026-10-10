package org.example.repository;

import org.example.model.VolunteerTask;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class VolunteerTaskRepository {
    private final Map<Long, VolunteerTask> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public VolunteerTask save(VolunteerTask task) {
        if (task.getId() == null) {
            task.setId(idGenerator.getAndIncrement());
        }
        storage.put(task.getId(), task);
        return task;
    }

    public Optional<VolunteerTask> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<VolunteerTask> findAll() {
        return new ArrayList<>(storage.values());
    }

    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }

    public List<VolunteerTask> findByCampaignId(Long campaignId) {
        return storage.values().stream()
                .filter(t -> campaignId.equals(t.getCampaignId()))
                .collect(Collectors.toList());
    }

    public boolean deleteById(Long id) {
        return storage.remove(id) != null;
    }
}
