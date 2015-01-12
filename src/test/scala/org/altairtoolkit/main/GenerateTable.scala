package org.altairtoolkit.main

import org.altairtoolkit.ApplicationContextProvider
import org.altairtoolkit.sample.web.config.RepositoryConfig
import org.altairtoolkit.sample.web.db.domain._

import scala.slick.jdbc.JdbcBackend.DatabaseDef
import scala.slick.driver.PostgresDriver.simple._

/**
 * Created by Deny Prasetyo,S.T
 * Java(Scala) Developer and Trainer
 * Software Engineer
 * jasoet87@gmail.com
 * <p/>
 * http://github.com/jasoet
 * http://bitbucket.com/jasoet
 * http://github.com/AltairLib
 * [at] jasoet
 */

object GenerateTable extends App {
  val context = ApplicationContextProvider(Array(classOf[RepositoryConfig]))
  val dbPool = context.getBean(classOf[DatabaseDef])

  dbPool.withTransaction(implicit session => {
    val ddl = AuthGroups.ddl ++ AuthGroupRoles.ddl ++ AuthRoles.ddl ++ Properties.ddl ++ WebUsers.ddl
    ddl.createStatements.foreach(s => {
      println(s)
    })

    ddl.create
  })


}
