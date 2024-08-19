package kr.wishtarot.mnspushapi.domain;

import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class PushDevice {

    @NotNull
    @Size(max = 2)
    private String deviceType;

    @NotNull
    @Size(max = 200)
    private String deviceId;

    @Size(max = 50)
    private String custId;

    @NotNull
    @Size(max = 50)
    private String appCode;

}
