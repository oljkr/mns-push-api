package kr.wishtarot.mnspushapi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@Alias("pushNotiRegVO")
@NoArgsConstructor
@AllArgsConstructor
public class PushNotiReg {

    private Long pnrNo; // pnr_no 컬럼에 해당하는 필드

    @NotNull
    private Long pdNo; // pd_no 컬럼에 해당하는 필드

    @NotNull
    private Long pushInfoNo; // push_info_no 컬럼에 해당하는 필드

    private LocalDateTime regDt; // reg_dt 컬럼에 해당하는 필드
}
