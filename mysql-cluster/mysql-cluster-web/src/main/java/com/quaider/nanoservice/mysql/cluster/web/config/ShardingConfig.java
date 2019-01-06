package com.quaider.nanoservice.mysql.cluster.web.config;

import io.shardingsphere.core.yaml.sharding.YamlShardingConfiguration;
import io.shardingsphere.core.yaml.sharding.YamlShardingRuleConfiguration;
import io.shardingsphere.core.yaml.sharding.strategy.YamlInlineShardingStrategyConfiguration;
import io.shardingsphere.shardingjdbc.api.yaml.YamlShardingDataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@Configuration
public class ShardingConfig {

    @Bean
    public DataSource dataSource() throws IOException, SQLException {
        ClassPathResource resource = new ClassPathResource("sharding.yml");
        File file = resource.getFile();
        YamlShardingConfiguration a;
        YamlShardingRuleConfiguration r;
        YamlInlineShardingStrategyConfiguration c;
        DataSource dataSource = YamlShardingDataSourceFactory.createDataSource(file);

        return dataSource;
    }

}
