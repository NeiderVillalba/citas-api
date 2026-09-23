package com.fcv.citas.user.application;

import com.fcv.citas.user.application.port.in.GetActivePlansUseCase;
import com.fcv.citas.user.application.port.out.ActivePlanQueryPort;
import com.fcv.citas.user.domain.ActivePlan;

import java.util.List;

public class GetActivePlansService implements GetActivePlansUseCase {
    private final ActivePlanQueryPort activePlanQueryPort;

    public GetActivePlansService(ActivePlanQueryPort activePlanQueryPort) {
        this.activePlanQueryPort = activePlanQueryPort;
    }

    @Override
    public List<ActivePlan> getActivePlans() {
        return activePlanQueryPort.findActivePlans();
    }
}
