package event.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import domain.Participant;
import event.domain.exception.GameIsNotInEventRangeDate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Claudio E. de Oliveira on 13/03/16.
 */
public class EventTest {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

  private final Participant owner = Participant.builder().id("mary").nickname("mary").email("mary@mary").build();

  @Test
  public void testAddGameInEvent() throws JsonProcessingException {
    Period period = Period.newPeriod(LocalDateTime.now().format(FORMATTER), LocalDateTime.now().plusDays(4).format(FORMATTER));
    Event eventOne = Event.newEvent(UUID.randomUUID().toString(), "Event 1", period, false, owner, 2);
    final Battle newGame = Battle.createBattleWithoutResult("PlayerOne", "PlayerTwo", LocalDateTime.now().plusDays(2));
    final Event event = eventOne.addGame(newGame);
    assertTrue(event.getGames().size() == 1);
  }

  @Test(expected = GameIsNotInEventRangeDate.class)
  public void testAddGameOutOfRangeInEvent() throws JsonProcessingException {
    Period period = Period.newPeriod(LocalDateTime.now().format(FORMATTER), LocalDateTime.now().plusDays(4).format(FORMATTER));
    Event eventOne = Event.newEvent(UUID.randomUUID().toString(), "Event 1", period, false, owner, 2);
    final Battle newGame = Battle.createBattleWithoutResult("PlayerOne", "PlayerTwo", LocalDateTime.now().plusDays(8));
    eventOne.addGame(newGame);
  }

  @Test
  public void testGameOpenForPredictions() throws JsonProcessingException {
    Period period = Period.newPeriod(LocalDateTime.now().format(FORMATTER), LocalDateTime.now().plusDays(4).format(FORMATTER));
    Event eventOne = Event.newEvent(UUID.randomUUID().toString(), "Event 1", period, false, owner, 2);
    final Battle newGame = Battle.createBattleWithoutResult("PlayerOne", "PlayerTwo", LocalDateTime.now().plusDays(3));
    eventOne.addGame(newGame);
    assertTrue(eventOne.getGames().size() == 1);
    assertTrue(eventOne.isOpenForPredictions(newGame.getId()));
  }

  @Test
  public void testEventClosed() throws JsonProcessingException {
    Period period = Period.newPeriod(LocalDateTime.now().minusDays(2).format(FORMATTER), LocalDateTime.now().minusDays(1).format(FORMATTER));
    Event eventOne = Event.newEvent(UUID.randomUUID().toString(), "Event 1", period, false, owner, 2);
    assertFalse(eventOne.isOpen(LocalDateTime.now()));
  }

  @Test
  public void testEventOpened() throws JsonProcessingException {
    Period period = Period.newPeriod(LocalDateTime.now().format(FORMATTER), LocalDateTime.now().plusDays(4).format(FORMATTER));
    Event eventOne = Event.newEvent(UUID.randomUUID().toString(), "Event 1", period, false, owner, 2);
    assertTrue(eventOne.isOpen(LocalDateTime.now()));
  }

}
