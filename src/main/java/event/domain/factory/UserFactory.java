package event.domain.factory;

import domain.Participant;
import domain.SystemUser;

/**
 * User factory
 *
 * @author Claudio E. de Oliveira on on 28/04/16.
 */
public class UserFactory {

  /**
     * Convert User from system user
     *
     * @param systemUser
     * @return
     */
    public static Participant fromSystemUser(SystemUser systemUser){
        return Participant.builder().id(systemUser.getId()).nickname(systemUser.getNickname()).email(systemUser.getEmail()).build();
    }

}
