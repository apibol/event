package event.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Claudio E. de Oliveira on 28/02/16.
 */
@Data
@EqualsAndHashCode(of = {"id"})
public abstract class Game<T> implements GameWithResult<T> {

    protected String id;

    protected LocalDateTime time;

    protected LocalDateTime registeredAt;

}
