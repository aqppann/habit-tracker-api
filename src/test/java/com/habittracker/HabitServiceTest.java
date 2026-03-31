package com.habittracker;

import com.habittracker.model.Completion;
import com.habittracker.model.Habit;
import com.habittracker.model.User;
import com.habittracker.repository.CompletionRepository;
import com.habittracker.repository.HabitRepository;
import com.habittracker.service.HabitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HabitServiceTest {

    @Mock
    private HabitRepository habitRepository;

    @Mock
    private CompletionRepository completionRepository;

    @InjectMocks
    private HabitService habitService;

    private User user;
    private Habit habit;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email("test@gmail.com")
                .password("password")
                .build();

        habit = Habit.builder()
                .id(1L)
                .name("Читання")
                .description("30 хвилин на день")
                .user(user)
                .build();
    }

    @Test
    void shouldCreateHabit() {
        when(habitRepository.save(any(Habit.class))).thenReturn(habit);

        Habit result = habitService.createHabit("Читання", "30 хвилин на день", user);

        assertNotNull(result);
        assertEquals("Читання", result.getName());
        verify(habitRepository, times(1)).save(any(Habit.class));
    }

    @Test
    void shouldReturnAllHabits() {
        when(habitRepository.findAllByUserId(1L)).thenReturn(List.of(habit));

        List<Habit> result = habitService.getAllHabits(1L);

        assertEquals(1, result.size());
        assertEquals("Читання", result.get(0).getName());
    }

    @Test
    void shouldThrowWhenHabitNotFound() {
        when(habitRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> habitService.getHabitById(99L, 1L));
    }

    @Test
    void shouldThrowWhenAccessDenied() {
        when(habitRepository.findById(1L)).thenReturn(Optional.of(habit));

        assertThrows(RuntimeException.class, () -> habitService.getHabitById(1L, 99L));
    }

    @Test
    void shouldCompleteHabit() {
        when(habitRepository.findById(1L)).thenReturn(Optional.of(habit));
        when(completionRepository.existsByHabitIdAndDate(1L, LocalDate.now())).thenReturn(false);
        when(completionRepository.save(any(Completion.class))).thenReturn(
                Completion.builder().habit(habit).date(LocalDate.now()).build()
        );

        Completion result = habitService.completeHabit(1L, 1L);

        assertNotNull(result);
        assertEquals(LocalDate.now(), result.getDate());
    }

    @Test
    void shouldThrowWhenHabitAlreadyCompletedToday() {
        when(habitRepository.findById(1L)).thenReturn(Optional.of(habit));
        when(completionRepository.existsByHabitIdAndDate(1L, LocalDate.now())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> habitService.completeHabit(1L, 1L));
    }

    @Test
    void shouldCalculateStreak() {
        when(habitRepository.findById(1L)).thenReturn(Optional.of(habit));
        when(completionRepository.findAllByHabitId(1L)).thenReturn(List.of(
                Completion.builder().habit(habit).date(LocalDate.now().minusDays(2)).build(),
                Completion.builder().habit(habit).date(LocalDate.now().minusDays(1)).build(),
                Completion.builder().habit(habit).date(LocalDate.now()).build()
        ));

        int streak = habitService.getStreak(1L, 1L);

        assertEquals(3, streak);
    }

    @Test
    void shouldReturnZeroStreakWhenNoCompletions() {
        when(habitRepository.findById(1L)).thenReturn(Optional.of(habit));
        when(completionRepository.findAllByHabitId(1L)).thenReturn(List.of());

        int streak = habitService.getStreak(1L, 1L);

        assertEquals(0, streak);
    }
}