package org.altairtoolkit.sample.web.db.domain

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


import java.time.LocalDateTime

import org.altairtoolkit.json.Jsonable
import org.altairtoolkit.slick.Sortable
import org.altairtoolkit.util.LocalDateTimeUtil
import org.json4s.JsonAST.JObject
import org.json4s.JsonDSL._

import scala.slick.driver.PostgresDriver.simple._
import scala.slick.lifted
import scala.slick.model.ForeignKeyAction


case class Property(propertyKey: String, value: String) extends Jsonable {
  override def toJsonObject: JObject = {
    ("key" -> propertyKey) ~ ("value" -> value)
  }
}

class PropertyTable(_tableTag: Tag) extends Table[Property](_tableTag, "PROPERTY") with Sortable {
  val propertyKey: Column[String] = column[String]("PROPERTY_KEY", O.PrimaryKey, O.Length(255, varying = true))
  val value: Column[String] = column[String]("VALUE", O.Length(1255, varying = true))

  def * = (propertyKey, value) <>(Property.tupled, Property.unapply)

  override val fieldMap: Map[String, lifted.Column[_]] = Map("propertyKey" -> propertyKey, "value" -> value)
}

object Properties extends TableQuery(new PropertyTable(_))

case class AuthGroup(authGroupName: String, description: String, createdAt: LocalDateTime, updatedAt: LocalDateTime)

class AuthGroupTable(_tableTag: Tag) extends Table[AuthGroup](_tableTag, "AUTH_GROUP") with Sortable {
  val authGroupName: Column[String] = column[String]("AUTH_GROUP_NAME", O.PrimaryKey, O.Length(35, varying = true))
  val description: Column[String] = column[String]("DESCRIPTION", O.Length(355, varying = true))
  val createdAt: Column[Long] = column[Long]("CREATED_AT")
  val updatedAt: Column[Long] = column[Long]("UPDATED_AT")

  def * = (authGroupName, description, createdAt, updatedAt) <>(mapRow, unMapRow)

  def mapRow(tuple: (String, String, Long, Long)): AuthGroup = {
    val (name, description, createdAt, updatedAt) = tuple

    AuthGroup(name, description, LocalDateTimeUtil.fromMilliSeconds(createdAt), LocalDateTimeUtil.fromMilliSeconds(updatedAt))
  }

  def unMapRow(authGroup: AuthGroup) = {
    val tuple = (authGroup.authGroupName, authGroup.description, LocalDateTimeUtil.toMilliSeconds(authGroup.createdAt), LocalDateTimeUtil.toMilliSeconds(authGroup.updatedAt))
    Some(tuple)
  }

  override val fieldMap: Map[String, lifted.Column[_]] = Map("authGroupName" -> authGroupName, "description" -> description, "createdAt" -> createdAt, "updatedAt" -> updatedAt)
}


object AuthGroups extends TableQuery(new AuthGroupTable(_))


case class AuthRole(authRoleName: String, description: String, createdAt: LocalDateTime, updatedAt: LocalDateTime)

class AuthRoleTable(_tableTag: Tag) extends Table[AuthRole](_tableTag, "AUTH_ROLE") {
  val authRoleName: Column[String] = column[String]("AUTH_ROLE_NAME", O.PrimaryKey, O.Length(25, varying = true))
  val description: Column[String] = column[String]("DESCRIPTION", O.Length(355, varying = true))
  val createdAt: Column[Long] = column[Long]("CREATED_AT")
  val updatedAt: Column[Long] = column[Long]("UPDATED_AT")
  val descriptionIdx = index("DESCRIPTION_IDX", description)

  def * = (authRoleName, description, createdAt, updatedAt) <>(mapRow, unMapRow)

  def mapRow(tuple: (String, String, Long, Long)): AuthRole = {
    val (name, description, createdAt, updatedAt) = tuple

    AuthRole(name, description, LocalDateTimeUtil.fromMilliSeconds(createdAt), LocalDateTimeUtil.fromMilliSeconds(updatedAt))
  }

  def unMapRow(authRole: AuthRole) = {
    val tuple = (authRole.authRoleName, authRole.description, LocalDateTimeUtil.toMilliSeconds(authRole.createdAt), LocalDateTimeUtil.toMilliSeconds(authRole.updatedAt))
    Some(tuple)
  }

}

object AuthRoles extends TableQuery(new AuthRoleTable(_))

case class AuthGroupRole(authGroupRoleId: Option[Int], authRoleName: String, authGroupName: String, createdAt: LocalDateTime, updatedAt: LocalDateTime)

class AuthGroupRoleTable(_tableTag: Tag) extends Table[AuthGroupRole](_tableTag, "AUTH_GROUP_ROLE") {
  lazy val authGroupFk = foreignKey("group_group_role_fk", authGroupName, AuthGroups)(r => r.authGroupName, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
  lazy val authRoleFk = foreignKey("role_group_role_fk", authRoleName, AuthRoles)(r => r.authRoleName, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)

  val authGroupRoleId: Column[Int] = column[Int]("AUTH_GROUP_ROLE_ID", O.AutoInc, O.PrimaryKey)
  val authRoleName: Column[String] = column[String]("AUTH_ROLE_NAME", O.Length(25, varying = true))
  val authGroupName: Column[String] = column[String]("AUTH_GROUP_NAME", O.Length(35, varying = true))
  val createdAt: Column[Long] = column[Long]("CREATED_AT")
  val updatedAt: Column[Long] = column[Long]("UPDATED_AT")

  def * = (authGroupRoleId.?, authRoleName, authGroupName, createdAt, updatedAt) <>(mapRow, unMapRow)

  def mapRow(tuple: (Option[Int], String, String, Long, Long)): AuthGroupRole = {
    val (id, roleName, groupName, createdAt, updatedAt) = tuple

    AuthGroupRole(id, roleName, groupName, LocalDateTimeUtil.fromMilliSeconds(createdAt), LocalDateTimeUtil.fromMilliSeconds(updatedAt))
  }

  def unMapRow(authGroupRole: AuthGroupRole) = {
    val tuple = (authGroupRole.authGroupRoleId, authGroupRole.authRoleName, authGroupRole.authGroupName, LocalDateTimeUtil.toMilliSeconds(authGroupRole.createdAt), LocalDateTimeUtil.toMilliSeconds(authGroupRole.updatedAt))
    Some(tuple)
  }
}


object AuthGroupRoles extends TableQuery(new AuthGroupRoleTable(_))
