package kr.wishtarot.mnspushapi.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.apache.ibatis.type.StringTypeHandler;

import javax.sql.DataSource;
@Configuration
@MapperScan("kr.wishtarot.mnspushapi.mapper")
public class MyBatisConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));

        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);

        // 타입 별칭 등록
        configuration.getTypeAliasRegistry().registerAlias("pushDeviceVO", kr.wishtarot.mnspushapi.domain.PushDevice.class);
        configuration.getTypeAliasRegistry().registerAlias("pushNotiRegVO", kr.wishtarot.mnspushapi.domain.PushNotiReg.class);
        configuration.getTypeAliasRegistry().registerAlias("pushInfoVO", kr.wishtarot.mnspushapi.domain.PushInfo.class);
        configuration.getTypeAliasRegistry().registerAlias("appInfoVO", kr.wishtarot.mnspushapi.domain.AppInfo.class);
        configuration.getTypeAliasRegistry().registerAlias("tmpPushDeviceVO", kr.wishtarot.mnspushapi.domain.TmpPushDevice.class);

        configuration.getTypeHandlerRegistry().register(String.class, StringTypeHandler.class);

        sessionFactory.setConfiguration(configuration);

        return sessionFactory.getObject();
    }
}