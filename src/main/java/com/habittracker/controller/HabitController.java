package com.habittracker.controller;

import com.habittracker.dto.HabitRequest;
import com.habittracker.dto.HabitResponse;
import com.habittracker.model.Habit;
import com.habittracker.model.User;
import com.habittracker.repository.UserRepository;
import com.habittracker.service.HabitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/habits")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class HabitController {
    private final HabitService habitService;
    private final UserRepository userRepository;

    private User getCurrentUser(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @GetMapping
    public ResponseEntity<List<HabitResponse>> getAllHabits(@AuthenticationPrincipal UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        List<HabitResponse> habits = habitService.getAllHabits(user.getId())
                .stream()
                .map(h -> new HabitResponse(h.getId(), h.getName(), h.getDescription()))
                .toList();
        return ResponseEntity.ok(habits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HabitResponse> getHabitById(@PathVariable Long id,
                                                      @AuthenticationPrincipal UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        Habit habit = habitService.getHabitById(id, user.getId());
        return ResponseEntity.ok(new HabitResponse(habit.getId(), habit.getName(), habit.getDescription()));
    }

    @PostMapping
    public ResponseEntity<HabitResponse> createHabit(@Valid @RequestBody HabitRequest request,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        Habit habit = habitService.createHabit(request.getName(), request.getDescription(), user);
        return ResponseEntity.ok(new HabitResponse(habit.getId(), habit.getName(), habit.getDescription()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HabitResponse> updateHabit(@PathVariable Long id,
                                                     @Valid @RequestBody HabitRequest request,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        Habit habit = habitService.updateHabit(id, request.getName(), request.getDescription(), user.getId());
        return ResponseEntity.ok(new HabitResponse(habit.getId(), habit.getName(), habit.getDescription()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHabit(@PathVariable Long id,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        habitService.deleteHabit(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<Void> completeHabit(@PathVariable Long id,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        habitService.completeHabit(id, user.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/streak")
    public ResponseEntity<Integer> getStreak(@PathVariable Long id,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        int streak = habitService.getStreak(id, user.getId());
        return ResponseEntity.ok(streak);
    }
}