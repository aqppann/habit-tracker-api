package com.habittracker.service;

import com.habittracker.model.Completion;
import com.habittracker.model.Habit;
import com.habittracker.model.User;
import com.habittracker.repository.CompletionRepository;
import com.habittracker.repository.HabitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitService {
    private final HabitRepository habitRepository;
    private final CompletionRepository completionRepository;

    public Habit createHabit(String name, String description, User user) {
        Habit habit = Habit.builder()
                .name(name)
                .description(description)
                .user(user)
                .build();
        return habitRepository.save(habit);
    }

    public List<Habit> getAllHabits(Long userId) {
        return habitRepository.findAllByUserId(userId);
    }

    public Habit getHabitById(Long habitId, Long userId) {
        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new RuntimeException("Habit not found"));

        if (!habit.getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }
        return habit;
    }

    public Habit updateHabit(Long habitId, String name, String description, Long userId) {
        Habit habit = getHabitById(habitId, userId);
        habit.setName(name);
        habit.setDescription(description);
        return habitRepository.save(habit);
    }

    public void deleteHabit(Long habitId, Long userId) {
        Habit habit = getHabitById(habitId, userId);
        habitRepository.delete(habit);
    }

    public Completion completeHabit(Long habitId, Long userId) {
        Habit habit = getHabitById(habitId, userId);

        if (completionRepository.existsByHabitIdAndDate(habitId, LocalDate.now())) {
            throw new RuntimeException("Habit already completed today");
        }

        Completion completion = Completion.builder()
                .habit(habit)
                .date(LocalDate.now())
                .build();
        return completionRepository.save(completion);
    }

    public int getStreak(Long habitId, Long userId) {
        getHabitById(habitId, userId);

        List<Completion> completions = completionRepository.findAllByHabitId(habitId);

        if (completions.isEmpty()) return 0;

        List<LocalDate> dates = completions.stream()
                .map(Completion::getDate)
                .sorted()
                .toList();

        int streak = 0;
        LocalDate expected = LocalDate.now();

        for (int i = dates.size() - 1; i >= 0; i--) {
            if (dates.get(i).equals(expected)) {
                streak++;
                expected = expected.minusDays(1);
            } else {
                break;
            }
        }
        return streak;
    }
}