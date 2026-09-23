package com.fcv.citas.user.adapter.in.rest;

import com.fcv.citas.user.application.port.in.GetActivePlansUseCase;
import com.fcv.citas.user.domain.ActivePlan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlanController {
    private final GetActivePlansUseCase getActivePlansUseCase;

    public PlanController(GetActivePlansUseCase getActivePlansUseCase) {
        this.getActivePlansUseCase = getActivePlansUseCase;
    }

    @GetMapping("/api/v1/plans/active")
    public List<ActivePlan> getActivePlans() {
        return getActivePlansUseCase.getActivePlans();
    }
}
