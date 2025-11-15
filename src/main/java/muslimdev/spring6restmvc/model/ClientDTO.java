package muslimdev.spring6restmvc.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ClientDTO {
    private UUID id;
    private String clientName;
    private String version;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
