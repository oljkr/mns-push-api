package kr.wishtarot.mnspushapi.domain;

import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class TmpPushDevice {

    @NotNull
    @Size(max = 5)
    private String deviceType; // 디바이스 유형

    @NotNull
    @Size(max = 200)
    private String oldDeviceId; // 이전 디바이스 ID

    @NotNull
    @Size(max = 200)
    private String newDeviceId; // 새 디바이스 ID

    @NotNull
    @Size(max = 50)
    private String appCode; // 애플리케이션 코드
}
