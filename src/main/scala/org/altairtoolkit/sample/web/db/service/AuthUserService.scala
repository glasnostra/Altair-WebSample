package org.altairtoolkit.sample.web.db.service

import java.time.LocalDateTime

import com.typesafe.scalalogging.Logger
import org.altairtoolkit.sample.web.db.domain.{AuthGroupRoles, WebUsers, WebUser}
import org.altairtoolkit.json.Page
import org.altairtoolkit.slick.sort.Sort
import org.altairtoolkit.util.LocalDateTimeUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import net.logstash.logback.marker.LogstashMarker
import net.logstash.logback.marker.Markers._
import scala.slick.driver.PostgresDriver.simple._
import Database.dynamicSession
import scala.slick.jdbc.JdbcBackend.DatabaseDef
import scala.util.{Failure, Success, Try}

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
class AuthUserService @Autowired()(databasePool: DatabaseDef) {
  val logger = Logger(LoggerFactory.getLogger(this.getClass))

  def add(u: WebUser): Try[WebUser] = {
    val logMaker = append("WebUser", u.toString)
    databasePool withDynTransaction {
      try {
        WebUsers += u
        Success(u)
      } catch {
        case e: Throwable =>
          logger.error(logMaker.and[LogstashMarker](append("Status", "Failure")), "Exception when add " + e)
          Failure(e)
      }
    }
  }

  def addUser(u: WebUser): Try[WebUser] = {
    val logMaker = append("WebUser", u.toString)
    databasePool withDynTransaction {
      try {
        WebUsers += u

        val user = WebUsers.filter(_.email === u.email).first
        Success(user)
      } catch {
        case e: Throwable =>
          logger.error(logMaker.and[LogstashMarker](append("Status", "Failure")), "Exception when addUser " + e)
          Failure(e)
      }
    }
  }

  def findAll(page: Int = 0, size: Int = 10, sorts: List[Sort]): Try[Page[WebUser]] = {
    val logMaker = append("Page", page)
      .and[LogstashMarker](append("Size", size))
      .and[LogstashMarker](append("List[Sorts]", sorts.toString()))
    databasePool withDynTransaction {
      try {
        val result = page match {
          case x if x <= 0 => WebUsers.sortBy(_.sort(sorts)).list
          case _ => WebUsers.sortBy(_.sort(sorts)).drop((page * size) - size).take(size).list
        }
        val total = WebUsers.length.run
        Success(Page(result, total, page, size))
      } catch {
        case e: Throwable =>
          logger.error(logMaker.and[LogstashMarker](append("Status", "Failure")), "Exception when findAll " + e)
          Failure(e)
      }
    }
  }

  def findUserAll(): Try[Seq[WebUser]] = {
    databasePool withDynTransaction {
      try {
        val result = WebUsers.list
        Success(result)
      } catch {
        case e: Throwable =>
          logger.error(append("Status", "Failure"), "Exception when findUserAll " + e)
          Failure(e)
      }
    }
  }

  def findByKey(k: Int): Try[WebUser] = {
    val logMaker = append("UserId", k)
    databasePool withDynTransaction {
      try {
        val result = WebUsers.filter(_.userId === k).first
        Success(result)
      } catch {
        case e: Throwable =>
          logger.error(logMaker.and[LogstashMarker](append("Status", "Failure")), "Exception when findByKey " + e)
          Failure(e)
      }
    }
  }

  def findByUsername(username: String): Try[(WebUser, Seq[String])] = {
    val logMaker = append("Username", username)
    databasePool withDynTransaction {
      try {
        val result = WebUsers.filter(_.username === username).first
        val groupRoles = AuthGroupRoles.filter(_.authGroupName === result.authGroupName).list
        Success(result, groupRoles.map(x => x.authRoleName))
      } catch {
        case e: Throwable =>
          logger.error(logMaker.and[LogstashMarker](append("Status", "Failure")), "Exception when findByUsername " + e)
          dynamicSession.rollback()
          Failure(e)
      }
    }
  }

  def delete(k: Int): Try[Int] = {
    val logMaker = append("UserId", k)
    databasePool withDynTransaction {
      try {
        val result = WebUsers.filter(_.userId === k).delete
        Success(result)
      } catch {
        case e: Throwable =>
          logger.error(logMaker.and[LogstashMarker](append("Status", "Failure")), "Exception when delete " + e)
          Failure(e)
      }
    }
  }

  def changePassword(id: Int, k: String): Try[Int] = {
    val logMaker = append("UserId", id).and[LogstashMarker](append("Password", k))
    val query = for {
      u <- WebUsers if u.userId === id
    } yield (u.password, u.updateAt)
    databasePool withDynTransaction {
      try {
        val result = query.update((k, LocalDateTimeUtil.toMilliSeconds(LocalDateTime.now())))
        Success(result)
      } catch {
        case e: Throwable =>
          logger.error(logMaker.and[LogstashMarker](append("Status", "Failure")), "Exception when changePassword " + e)
          Failure(e)
      }
    }
  }

  def changePassByToken(k: String, p: String): Try[Int] = {
    val logMaker = append("RememberToken", k).and[LogstashMarker](append("Password", p))
    val query = for {
      u <- WebUsers if u.remember_token === k
    } yield (u.password, u.remember_token.?, u.updateAt)
    databasePool withDynTransaction {
      try {
        val result = query.update(p, None, LocalDateTimeUtil.toMilliSeconds(LocalDateTime.now()))
        Success(result)
      } catch {
        case e: Throwable =>
          logger.error(logMaker.and[LogstashMarker](append("Status", "Failure")), "Exception when changePassByToken " + e)
          Failure(e)
      }
    }
  }

  def activate(code: String, k: Int): Try[Int] = {
    val logMaker = append("ActivationCode", code).and[LogstashMarker](append("UserId", k))
    val query = for {
      u <- WebUsers if u.userId === k && u.activation_code === code
    } yield (u.enabled, u.updateAt)
    databasePool withDynTransaction {
      try {
        val result = query.update(true, LocalDateTimeUtil.toMilliSeconds(LocalDateTime.now()))
        Success(result)
      } catch {
        case e: Throwable =>
          logger.error(logMaker.and[LogstashMarker](append("Status", "Failure")), "Exception when activate " + e)
          Failure(e)
      }
    }
  }

  def createToken(code: String, e: String): Try[WebUser] = {
    val logMaker = append("RememberToken", code).and[LogstashMarker](append("Email", e))
    val query = WebUsers.filter(_.email === e).map { u =>
      (u.remember_token, u.updateAt)
    }
    databasePool withDynTransaction {
      try {
        val update = query.update(code, LocalDateTimeUtil.toMilliSeconds(LocalDateTime.now()))
        val result = WebUsers.filter(_.email === e).first
        Success(result)
      } catch {
        case e: Throwable =>
          logger.error(logMaker.and[LogstashMarker](append("Status", "Failure")), "Exception when createToken " + e)
          Failure(e)
      }
    }
  }

  def findByEmail(k: String): Try[Int] = {
    val logMaker = append("Email", k)
    databasePool withDynTransaction {
      try {
        val result = WebUsers.filter(_.email === k).length.run

        Success(result)
      } catch {
        case e: Throwable =>
          logger.error(logMaker.and[LogstashMarker](append("Status", "Failure")), "Exception when findByEmail " + e)
          Failure(e)
      }
    }
  }

  def findUser(k: String): Try[WebUser] = {
    val logMaker = append("Username", k)
    databasePool withDynTransaction {
      try {
        val result = WebUsers.filter(_.username === k).first

        Success(result)
      } catch {
        case e: Throwable =>
          logger.error(logMaker.and[LogstashMarker](append("Status", "Failure")), "Exception when findUser " + e)
          Failure(e)
      }
    }
  }
}