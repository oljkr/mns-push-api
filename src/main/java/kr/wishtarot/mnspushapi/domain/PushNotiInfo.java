package kr.wishtarot.mnspushapi.domain;

import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;

@Data
@Builder
public class PushNotiInfo {

    @NotNull
    @Size(max = 50)
    private String appCode;

    @NotNull
    @Size(max = 5)
    private String notiCode;

    @NotNull
    @Size(max = 50)
    private String notiNm;

    // 주석으로 처리된 필드는 필요 시 추가
    // @Size(max = 255)
    // private String notiDesc;

    // @Pattern(regexp = "Y|N")
    // private String isDefault;

    @Pattern(regexp = "Y|N")
    private String isSetting; // 해당 사용자가 셋팅이 되어있는지 정보 Y or N
}
