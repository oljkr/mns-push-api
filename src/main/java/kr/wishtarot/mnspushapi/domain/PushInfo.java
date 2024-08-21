package kr.wishtarot.mnspushapi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@Alias("pushInfoVO")
@NoArgsConstructor
@AllArgsConstructor
public class PushInfo {

    private Long pushInfoNo; // push_info_no 컬럼에 해당하는 필드

    @NotNull
    private Long pushAppNo; // push_app_no 컬럼에 해당하는 필드

    @NotNull
    @Size(max = 5)
    private String notiCode; // noti_code 컬럼에 해당하는 필드

    @Size(max = 50)
    private String notiNm; // noti_nm 컬럼에 해당하는 필드
}
