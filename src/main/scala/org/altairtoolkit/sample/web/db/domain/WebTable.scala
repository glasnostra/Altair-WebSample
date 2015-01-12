package org.altairtoolkit.sample.web.db.domain

import java.time.LocalDateTime

import org.altairtoolkit.auth.config.UserTrait
import org.altairtoolkit.json.Jsonable
import org.altairtoolkit.slick.Sortable
import org.altairtoolkit.util.LocalDateTimeUtil
import org.json4s.JsonAST.JObject
import org.json4s.JsonDSL._

import scala.slick.driver.PostgresDriver.simple._
import scala.slick.lifted
import scala.slick.model.ForeignKeyAction

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

case class WebUser(userId: Option[Int], override val username: String, override val password: String, override val enabled: Boolean, activation_code: String, override val name: String, override val email: String, phone: Option[String], photo: Option[String], address: Option[String], city: Option[String], postalCode: Option[String], dateOfBirth: Option[Long], override val authGroupName: String, remember_token: Option[String], createdAt: LocalDateTime, updatedAt: LocalDateTime) extends UserTrait with Jsonable {
  override def toJsonObject: JObject = {
    ("username" -> username) ~ ("enabled" -> enabled) ~ ("activation_code" -> activation_code) ~ ("name" -> name) ~ ("email" -> email) ~ ("phone" -> phone) ~ ("photo" -> photo) ~ ("groupName" -> authGroupName) ~ ("remember_token" -> remember_token)
  }
}

class UserTable(_tableTag: Tag) extends Table[WebUser](_tableTag, "USER") with Sortable {
  lazy val authGroupFk = foreignKey("group_user_fk", authGroupName, AuthGroups)(r => r.authGroupName, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Restrict)
  val userId: Column[Int] = column[Int]("USER_ID", O.AutoInc, O.PrimaryKey)
  val username: Column[String] = column[String]("USERNAME", O.Length(35, varying = true))
  val password: Column[String] = column[String]("PASSWORD", O.Length(555, varying = true))
  val enabled: Column[Boolean] = column[Boolean]("ENABLED")
  val activation_code: Column[String] = column[String]("ACTIVATION_CODE", O.Length(555, varying = true))
  val name: Column[String] = column[String]("NAME", O.Length(255, varying = true))
  val email: Column[String] = column[String]("EMAIL", O.Length(55, varying = true))
  val phone: Column[String] = column[String]("PHONE", O.Length(16, varying = true), O.Nullable)
  val photo: Column[String] = column[String]("PHOTO", O.DBType("text"), O.Nullable)
  val address: Column[String] = column[String]("ADDRESS", O.Length(555, varying = true), O.Nullable)
  val city: Column[String] = column[String]("CITY", O.Length(55, varying = true), O.Nullable)
  val postalCode: Column[String] = column[String]("POSTAL_CODE", O.Length(35, varying = true), O.Nullable)
  val dateOfBirth: Column[Long] = column[Long]("DATE_OF_BIRTH", O.Nullable)
  val authGroupName: Column[String] = column[String]("AUTH_GROUP_NAME", O.Length(35, varying = true))
  val remember_token: Column[String] = column[String]("REMEMBER_TOKEN", O.Length(555, varying = true), O.Nullable)
  val createdAt: Column[Long] = column[Long]("CREATED_AT")
  val updateAt: Column[Long] = column[Long]("UPDATE_AT")
  val emailIdx = index("EMAIL_UNIQUE", email, unique = true)
  val usernameIdx = index("USERNAME_UNIQUE", username, unique = true)

  def * = (userId.?, username, password, enabled, activation_code, name, email, phone.?, photo.?, address.?, city.?, postalCode.?, dateOfBirth.?, authGroupName, remember_token.?, createdAt, updateAt) <>(mapRow, unMapRow)

  def mapRow(tuple: (Option[Int], String, String, Boolean, String, String, String, Option[String], Option[String], Option[String], Option[String], Option[String], Option[Long], String, Option[String], Long, Long)): WebUser = {
    val (id, username, password, enabled, activation_code, name, email, phone, photo, address, city, postalCode, dateOfBirth, groupName, remember_token, createdAt, updatedAt) = tuple

    WebUser(id, username, password, enabled, activation_code, name, email, phone, photo, address, city, postalCode, dateOfBirth, groupName, remember_token, LocalDateTimeUtil.fromMilliSeconds(createdAt), LocalDateTimeUtil.fromMilliSeconds(updatedAt))
  }

  def unMapRow(user: WebUser) = {
    val tuple = (user.userId, user.username, user.password, user.enabled, user.activation_code, user.name, user.email, user.phone, user.photo, user.address, user.city, user.postalCode, user.dateOfBirth, user.authGroupName, user.remember_token, LocalDateTimeUtil.toMilliSeconds(user.createdAt), LocalDateTimeUtil.toMilliSeconds(user.updatedAt))
    Some(tuple)
  }

  override val fieldMap: Map[String, lifted.Column[_]] = Map("userId" -> userId, "username" -> username, "password" -> password, "enabled" -> enabled, "activation_code" -> activation_code, "name" -> name, "email" -> email, "phone" -> phone, "photo" -> photo, "address" -> address, "city" -> city, "postalCode" -> postalCode, "dateOfBirth" -> dateOfBirth, "authGroupName" -> authGroupName, "remember_token" -> remember_token, "createdAt" -> createdAt, "updateAt" -> updateAt)
}


object WebUsers extends TableQuery(new UserTable(_)) {
}