package kr.wishtarot.mnspushapi.domain;

import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@Builder
public class PushHist {

    private long histSeq;

    @NotNull
    @Size(max = 50)
    private String custId;

    @NotNull
    @Size(max = 2)
    private String deviceType;

    @NotNull
    @Size(max = 200)
    private String deviceId;

    @NotNull
    @Size(max = 4000)
    private String message;

    @NotNull
    private LocalDateTime sendDt; // LocalDateTime 사용으로 변경

    @Size(max = 50)
    private String appCode;  // 선택적 필드이므로 @NotNull 제거

    @NotNull
    @Size(max = 5)
    private String notiCode;

    @Size(max = 1)
    @Pattern(regexp = "Y|N")
    private String receiveSuccesYn; // 선택적 필드이므로 @NotNull 제거

    private LocalDateTime qryStartDt; // LocalDateTime 사용으로 변경 (선택적 필드이므로 @NotNull 제거)
}
