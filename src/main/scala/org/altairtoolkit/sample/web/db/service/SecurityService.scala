package org.altairtoolkit.sample.web.db.service

import org.altairtoolkit.sample.web.db.domain.WebUser
import org.altairtoolkit.SpringBean
import org.altairtoolkit.auth.config.AltairUserDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.{UserDetails, UserDetailsService, UsernameNotFoundException}
import org.springframework.stereotype.Service

import scala.util.{Failure, Success}

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
class SecurityService {

  @Autowired
  private val userService: AuthUserService = SpringBean.DEFAULT

  def loadUserByUsername(username: String): AltairUserDetails[WebUser] = {
    userService.findByUsername(username) match {
      case Success((user, roles)) => AltairUserDetails(user, roles)
      case Failure(x) => throw new UsernameNotFoundException(x.getMessage, x)
    }
  }
}

