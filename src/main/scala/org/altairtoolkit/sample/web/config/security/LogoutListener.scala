package org.altairtoolkit.sample.web.config.security

import org.springframework.context.ApplicationListener
import org.springframework.security.core.session.SessionDestroyedEvent

import scala.collection.JavaConverters._

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

class LogoutListener extends ApplicationListener[SessionDestroyedEvent] {
  override def onApplicationEvent(e: SessionDestroyedEvent) = {
    val contexts = e.getSecurityContexts.asScala.toList
    contexts.foreach(c => {
      println(c)
    })
  }
}
