package com.fcv.citas.user.application.port.in;

import com.fcv.citas.user.domain.ActivePlan;

import java.util.List;

public interface GetActivePlansUseCase {
    List<ActivePlan> getActivePlans();
}
