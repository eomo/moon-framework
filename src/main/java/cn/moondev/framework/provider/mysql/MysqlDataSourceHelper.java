package cn.moondev.framework.provider.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据源工具类
 */
public class MysqlDataSourceHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(MysqlDataSourceHelper.class);

    private static final int MAX_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;

    public static HikariDataSource dataSource(String dataSourceUri,String user,String password) {
        LOGGER.info("MySQL DataSource provider: {}, uri = {}","hikaricp",dataSourceUri);
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(dataSourceUri);
        hikariConfig.setUsername(user);
        hikariConfig.setPassword(password);
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setMaximumPoolSize(MAX_POOL_SIZE);
        // 想要下面两个配置生效，需要设置为true
        hikariConfig.addDataSourceProperty("cachePrepStmts", false);
        // mysql缓存的prepared statements数量，建议：250 - 500
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", 250);
        // mysql驱动将缓存的sql语句的最大长度，mysql默认值为256，根据经验设置为2048
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        // 新版本mysql支持服务端预编译，设置为true以利于性能提升
        hikariConfig.addDataSourceProperty("useServerPrepStmts", true);
        hikariConfig.setConnectionInitSql("SELECT 1");
        return new HikariDataSource(hikariConfig);
    }
}
