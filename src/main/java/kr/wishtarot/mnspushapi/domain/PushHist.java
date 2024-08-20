package kr.wishtarot.mnspushapi.domain;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
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
}
