package com.habittracker.repository;

import com.habittracker.model.Completion;
import com.habittracker.model.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompletionRepository extends JpaRepository<Completion, Long> {
    List<Completion> findAllByHabitId(Long habitId);
    Optional<Completion> findByHabitIdAndDate(Long habitId, LocalDate date);
    boolean existsByHabitIdAndDate(Long habitId, LocalDate date);
}
