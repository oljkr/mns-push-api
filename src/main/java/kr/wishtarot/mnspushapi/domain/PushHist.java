package kr.wishtarot.mnspushapi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushHist {

    private Long histNo; // hist_no 컬럼에 해당하는 필드

    @NotNull
    private Long pnrNo; // pnr_no 컬럼에 해당하는 필드

    @Size(max = 200)
    private String title; // title 컬럼에 해당하는 필드

    @Size(max = 4000)
    private String message; // message 컬럼에 해당하는 필드

    private LocalDateTime sendDt; // send_dt 컬럼에 해당하는 필드

    @Size(max = 1)
    @Pattern(regexp = "[YyNn]")
    private String sendSuccessYn; // send_success_yn 컬럼에 해당하는 필드

    @Size(max = 200)
    private String sendError; // send_error 컬럼에 해당하는 필드

    // Associated objects (연관된 테이블의 데이터)
    private PushNotiReg pushNotiReg;
    private PushDevice pushDevice;
    private PushInfo pushInfo;
    private AppInfo appInfo;

    // 검색 조건 필드 (옵션)
    @JsonIgnore
    private String deviceType;

    @JsonIgnore
    private String deviceId;

    @JsonIgnore
    private String custId;

    @JsonIgnore
    private String appCode;

    @JsonIgnore
    private String notiCode;
}
