package life.freebao.devops.brokereye.web.error;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.io.Serial;

public class InvalidIdNumberException extends AbstractThrowableProblem {
    @Serial
    private static final long serialVersionUID = 1L;
    public InvalidIdNumberException() {
        super(ErrorConstant.INVALID_ID_NUMBER_TYPE, "Incorrect ID number", Status.BAD_REQUEST);
    }
}
