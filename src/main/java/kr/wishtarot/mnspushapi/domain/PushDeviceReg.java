package kr.wishtarot.mnspushapi.domain;

import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class PushDeviceReg {

    @NotNull
    @Size(max = 50)
    private String appCode;

    @NotNull
    @Size(max = 5)
    private String notiCode;

    @NotNull
    @Size(max = 2)
    private String deviceType;

    @NotNull
    @Size(max = 200)
    private String deviceId;

    // 여기에 유효성 검사를 추가할 수 있습니다.
}
