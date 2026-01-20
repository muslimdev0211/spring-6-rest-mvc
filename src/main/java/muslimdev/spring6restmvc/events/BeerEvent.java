package muslimdev.spring6restmvc.events;

import muslimdev.spring6restmvc.entities.Beer;
import org.springframework.security.core.Authentication;

public interface BeerEvent {

    Beer getBeer();

    Authentication getAuthentication();

}
