package org.example.repository;

import org.example.model.ParticipationReport;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class ParticipationReportRepository {
    private final Map<Long, ParticipationReport> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public ParticipationReport save(ParticipationReport report) {
        if (report.getId() == null) {
            report.setId(idGenerator.getAndIncrement());
        }
        storage.put(report.getId(), report);
        return report;
    }

    public Optional<ParticipationReport> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<ParticipationReport> findAll() {
        return new ArrayList<>(storage.values());
    }

    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }

    public List<ParticipationReport> findByResponseId(Long responseId) {
        return storage.values().stream()
                .filter(report -> responseId.equals(report.getResponseId()))
                .collect(Collectors.toList());
    }

    public boolean deleteById(Long id) {
        return storage.remove(id) != null;
    }
}
