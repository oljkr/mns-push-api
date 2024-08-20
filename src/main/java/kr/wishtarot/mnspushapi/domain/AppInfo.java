package kr.wishtarot.mnspushapi.domain;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class AppInfo {

    private Long pushAppNo; // push_app_no 컬럼에 해당하는 필드

    @NotNull
    @Size(max = 50)
    private String appCode; // app_code 컬럼에 해당하는 필드

    @Size(max = 50)
    private String appNm; // app_nm 컬럼에 해당하는 필드
}
