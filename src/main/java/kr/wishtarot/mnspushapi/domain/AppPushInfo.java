package kr.wishtarot.mnspushapi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Data
@Builder
@Alias("AppPushInfo")
@NoArgsConstructor
@AllArgsConstructor
public class AppPushInfo {
    private Long pushAppNo;
    private String appCode;
    private String appNm;
    private Long pushInfoNo;
    private String notiCode;
    private String notiNm;
}
