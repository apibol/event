package event.domain.service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import domain.Participant;
import event.domain.exception.UserNotFound;
import lombok.extern.log4j.Log4j2;

/**
 * @author Claudio E. de Oliveira on 06/03/16.
 */
@Service
@RefreshScope
@Log4j2
public class UserInfoService {

  @Autowired
  private RestTemplate restTemplate;

  @Value("${services.user.baseUrl}")
  private String baseUrl;

  private final Cache<String, Participant> cache =
      CacheBuilder.newBuilder().maximumSize(100).expireAfterWrite(24L, TimeUnit.HOURS).build();

  @HystrixCommand(fallbackMethod = "getCachedUser")
  public Participant getUserInfo(String userId) {
    ResponseEntity<Participant> response = this.restTemplate.getForEntity(this.baseUrl + userId, Participant.class);
    Participant body = response.getBody();
    cache.put(body.getId(), body);
    return body;
  }

  /**
   * Retrieve user from cache
   * 
   * @param userId
   * @return
   */
  public Participant getCachedUser(String userId) {
    Participant cachedUser = cache.getIfPresent(userId);
    if (Objects.isNull(cachedUser)) {
      throw new UserNotFound(userId);
    }
    return cachedUser;
  }

}
