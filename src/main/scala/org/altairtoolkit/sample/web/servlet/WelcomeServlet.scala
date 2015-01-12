package org.altairtoolkit.sample.web.servlet

import com.typesafe.scalalogging.Logger
import org.altairtoolkit.sample.web.twirl._
import org.altairtoolkit.sample.web.db.service.AuthUserService
import org.altairtoolkit.SpringBean
import org.altairtoolkit.annotation.scalatra.ScalatraMapping
import org.scalatra.ScalatraServlet
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

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

@Component
@ScalatraMapping(value = "/*", welcome = true)
class WelcomeServlet extends ScalatraServlet {
  val logger = Logger(LoggerFactory.getLogger(this.getClass))

  @Autowired
  private val authUserService: AuthUserService = SpringBean.DEFAULT


  get("/") {
    html.welcome()
  }

  get("/login") {
    html.login()
  }


}
