package com.fcv.citas.user.application.port.out;

import com.fcv.citas.user.domain.ActivePlan;

import java.util.List;

public interface ActivePlanQueryPort {
    List<ActivePlan> findActivePlans();
}
