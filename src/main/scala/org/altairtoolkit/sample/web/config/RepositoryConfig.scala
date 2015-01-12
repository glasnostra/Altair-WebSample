package org.altairtoolkit.sample.web.config

import com.typesafe.scalalogging.Logger
import org.altairtoolkit.db.{DatabasePoolSupport, JdbcConfig}
import org.apache.tomcat.dbcp.dbcp.BasicDataSource
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.{Bean, ComponentScan, Configuration, PropertySource}
import org.springframework.core.env.Environment

import scala.slick.driver.PostgresDriver.backend._

/**
 * Deny Prasetyo,S.T
 * Java(Scala) Developer and Trainer
 * Software Engineer | Java Virtual Machine Junkie!
 * jasoet87@gmail.com
 * <p/>
 * http://github.com/jasoet
 * http://bitbucket.com/jasoet
 * [at] jasoet
 */

@Configuration
@PropertySource(value = Array("classpath:jdbc.properties"))
@ComponentScan(value = Array("org.altairtoolkit.sample.web.db.service"))
class RepositoryConfig() extends DatabasePoolSupport {
  val logger = Logger(LoggerFactory.getLogger(this.getClass))

  @Autowired
  var env: Environment = _

  @Bean
  def databasePool: DatabaseDef = {
    Database.forDataSource(dataSource())
  }


  override def databasePoolConfig(dbcp: BasicDataSource): Unit = {
    dbcp.setMaxActive(15)
    dbcp.setMaxWait(3000L)
  }

  override def jdbcConfig: JdbcConfig = JdbcConfig(env.getProperty("jdbc.driver"), env.getProperty("jdbc.url"), env.getProperty("jdbc.username"), env.getProperty("jdbc.password"))

}
