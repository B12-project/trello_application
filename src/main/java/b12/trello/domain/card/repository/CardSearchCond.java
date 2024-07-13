package b12.trello.domain.card.repository;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CardSearchCond {
    public static final String COND_NULL = "null";
    public static final String COND_WORKER_ID = "workerId";
    public static final String COND_WORKER_EMAIL = "email";

    private Long columnId;
    private Long workerId;
    private String workerEmail;
}
