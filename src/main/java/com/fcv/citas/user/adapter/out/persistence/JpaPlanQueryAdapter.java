package com.fcv.citas.user.adapter.out.persistence;

import com.fcv.citas.user.adapter.out.persistence.repository.EpsPlanJpaRepository;
import com.fcv.citas.user.application.port.out.ActivePlanQueryPort;
import com.fcv.citas.user.domain.ActivePlan;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class JpaPlanQueryAdapter implements ActivePlanQueryPort {
    private final EpsPlanJpaRepository plans;

    public JpaPlanQueryAdapter(EpsPlanJpaRepository plans) {
        this.plans = plans;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivePlan> findActivePlans() {
        return plans.findActivePlans().stream()
                .map(plan -> new ActivePlan(plan.getId(), plan.getName(), plan.getEps().getName()))
                .toList();
    }
}
