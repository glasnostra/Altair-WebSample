package org.altairtoolkit.sample.web.db.service

import com.typesafe.scalalogging.Logger
import org.altairtoolkit.sample.web.db.domain.{Properties, Property}
import org.altairtoolkit.json.Page
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.altairtoolkit.slick.sort.Sort
import scala.util.{Failure, Success, Try}
import scala.slick.driver.PostgresDriver.simple._
import Database.dynamicSession
import scala.slick.jdbc.JdbcBackend.DatabaseDef


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


@Service
class PropertyService @Autowired()(databasePool: DatabaseDef) {
  val logger = Logger(LoggerFactory.getLogger(this.getClass))

  def +=(p: Property): Try[Property] = {
    databasePool withDynTransaction {
      try {
        Properties += p
        Success(p)
      } catch {
        case e: Throwable =>
          logger.error("Exception when Insert Property " + e)
          Failure(e)
      }
    }
  }

  def ++=(p: Seq[Property]): Try[Seq[Property]] = {
    databasePool withDynTransaction {
      try {
        Properties ++= p
        Success(p)
      } catch {
        case e: Throwable =>
          logger.error("Exception when Insert All Property " + e)
          Failure(e)
      }
    }
  }

  def findByKey(k: String): Try[Property] = {
    databasePool withDynTransaction {
      try {
        Success(Properties.filter(_.propertyKey === k).first)
      } catch {
        case e: Throwable =>
          logger.error("Exception when findByKey " + e)
          Failure(e)
      }
    }
  }

  def deleteByKey(k: String): Try[Int] = {
    databasePool withDynTransaction {
      try {
        Success(Properties.filter(_.propertyKey === k).delete)
      } catch {
        case e: Throwable =>
          logger.error("Exception when deleteByKey " + e)
          Failure(e)
      }
    }
  }

  def findAll(page: Int = 0, size: Int = 20, sort: List[Sort] = Nil): Try[Page[Property]] = {
    databasePool withDynTransaction {
      try {
        val result = page match {
          case p if p < 0 => Properties.sortBy(_.sort(sort)).list
          case p => Properties.sortBy(_.sort(sort)).drop(page * size).take(size).list
        }

        val total = Properties.length.run

        logger.info("Execute query [" + Properties.selectStatement + "]")
        Success(Page(result, total, page, size))
      } catch {
        case e: Throwable =>
          logger.error("Exception when FindAll " + e)
          Failure(e)
      }
    }
  }
}