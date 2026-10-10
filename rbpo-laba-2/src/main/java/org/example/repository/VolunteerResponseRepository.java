package org.example.repository;

import org.example.model.VolunteerResponse;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class VolunteerResponseRepository {
    private final Map<Long, VolunteerResponse> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public VolunteerResponse save(VolunteerResponse response) {
        if (response.getId() == null) {
            response.setId(idGenerator.getAndIncrement());
        }
        storage.put(response.getId(), response);
        return response;
    }

    public Optional<VolunteerResponse> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<VolunteerResponse> findAll() {
        return new ArrayList<>(storage.values());
    }

    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }

    public List<VolunteerResponse> findByTaskId(Long taskId) {
        return storage.values().stream()
                .filter(r -> taskId.equals(r.getTaskId()))
                .collect(Collectors.toList());
    }

    public List<VolunteerResponse> findByVolunteerId(Long volunteerId) {
        return storage.values().stream()
                .filter(r -> volunteerId.equals(r.getVolunteerId()))
                .collect(Collectors.toList());
    }

    public boolean deleteById(Long id) {
        return storage.remove(id) != null;
    }
}
