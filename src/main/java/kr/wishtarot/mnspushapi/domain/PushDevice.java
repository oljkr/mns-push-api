package kr.wishtarot.mnspushapi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@Alias("pushDeviceVO")
@NoArgsConstructor
@AllArgsConstructor
public class PushDevice {

    private Long pdNo; // pd_no 컬럼에 해당하는 필드 추가

    @NotNull
    @Size(max = 2)
    private String deviceType;

    @NotNull
    @Size(max = 200)
    private String deviceId;

    @Size(max = 100)
    private String custId;

    private LocalDateTime regDt; // reg_dt 컬럼에 해당하는 필드 추가
}