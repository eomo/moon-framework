package cn.moondev.framework.provider.mysql;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * MyBatis支持
 *
 * @Author CHEN川(luecsc @ sina.com)
 */
public abstract class MybatisConfigurationSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(MybatisConfigurationSupport.class);

    /**
     * jdbc datasource url
     */
    protected String dataSourceUri;

    /**
     * 数据库用户名
     */
    protected String user;

    /**
     * 数据库密码
     */
    protected String password;

    /**
     * mybatis配置文件路径
     */
    protected String configLocation;

    /**
     * mapper文件路径
     */
    protected String[] mapperLocations;

    /**
     * domain实体包路径
     */
    protected String typeAliasesPackage;

    /**
     * Package to scan handlers.
     */
    protected String typeHandlersPackage;

    /**
     * Execution mode：
     * SIMPLE: 这个类型不做特殊的事情，它只为每个语句创建一个PreparedStatement
     * REUSE: 这种类型将重复使用PreparedStatements
     * BATCH: 这个类型批量更新，且必要地区别开其中的select 语句，确保动作易于理解
     */
    protected ExecutorType executorType;


    @PostConstruct
    protected abstract void initConfiguration();

    @Bean
    public HikariDataSource dataSource() {
        // 数据库密码加密代码可以添加在此处
        return MysqlDataSourceHelper.dataSource(dataSourceUri, user, password);
    }

    @Bean
    @Autowired
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        if (StringUtils.hasLength(configLocation)) {
            factory.setConfigLocation(resloveConfigLocation(configLocation));
        }
        if (!ObjectUtils.isEmpty(mapperLocations)) {
            factory.setMapperLocations(resolveMapperLocations(mapperLocations));
        }
        if (StringUtils.hasLength(typeAliasesPackage)) {
            factory.setTypeAliasesPackage(typeAliasesPackage);
        }
        if (StringUtils.hasLength(typeHandlersPackage)) {
            factory.setTypeHandlersPackage(typeHandlersPackage);
        }
        return factory.getObject();
    }

//    @Bean
//    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }


    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        if (executorType != null) {
            return new SqlSessionTemplate(sqlSessionFactory, executorType);
        } else {
            return new SqlSessionTemplate(sqlSessionFactory);
        }
    }

    public Resource resloveConfigLocation(String configLocation) {
        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        return resourceResolver.getResource(configLocation);
    }

    public Resource[] resolveMapperLocations(String[] mapperLocations) {
        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        List<Resource> resources = new ArrayList<Resource>();
        for (String mapperLocation : mapperLocations) {
            try {
                Resource[] mappers = resourceResolver.getResources(mapperLocation);
                resources.addAll(Arrays.asList(mappers));
            } catch (IOException e) {
                LOGGER.warn("解析mapper路径出现IO异常,请检查配置路径是否正确,如未使用mapper.xml请忽略此警告");
            }
        }
        return resources.toArray(new Resource[resources.size()]);
    }
}
