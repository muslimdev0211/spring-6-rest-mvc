package muslimdev.spring6restmvc.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import muslimdev.spring6restmvc.entities.Beer;
import org.springframework.security.core.Authentication;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class BeerDeletedEvent implements BeerEvent {
    Beer beer;
    Authentication authentication;
}
