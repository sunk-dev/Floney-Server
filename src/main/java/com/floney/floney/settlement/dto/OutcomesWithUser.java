package com.floney.floney.settlement.dto;

import com.floney.floney.settlement.dto.request.OutcomeRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class OutcomesWithUser {

    private final Map<String, Long> outcomesWithUser;

    private OutcomesWithUser(Set<String> userEmails, String leaderUserEmail) {
        this.outcomesWithUser = userEmails.stream().collect(Collectors.toMap(key -> key, value -> 0L));
        this.outcomesWithUser.put(leaderUserEmail, 0L);
    }

    public static OutcomesWithUser init(Set<String> userEmails, String leaderUserEmail) {
        return new OutcomesWithUser(userEmails, leaderUserEmail);
    }

    public void fillOutcomes(List<OutcomeRequest> outcomeRequests) {
        outcomeRequests.forEach(outcome -> outcomesWithUser.put(
                outcome.getUserEmail(), outcomesWithUser.get(outcome.getUserEmail()) + outcome.getOutcome()
        ));
    }

    public Map<String, Long> getOutcomesWithUser() {
        return Collections.unmodifiableMap(outcomesWithUser);
    }
}
