package event.domain.exception;

import lombok.Getter;

/**
 * Game not found exception
 *
 * @author Claudio E. de Oliveira on 27/07/16.
 */
public class GameNotFound extends RuntimeException {

  @Getter
  private final String gameId;

  public GameNotFound(String gameId) {
    this.gameId = gameId;
  }

}
