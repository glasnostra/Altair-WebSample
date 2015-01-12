package org.altairtoolkit.sample.web.config

import org.altairtoolkit.sample.web.config.security.LogoutListener
import org.altairtoolkit.sample.web.db.domain.WebUser
import org.altairtoolkit.sample.web.db.service.SecurityService
import org.altairtoolkit.SpringBean
import org.altairtoolkit.auth.config.{AltairUserDetails, SecuritySupport}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

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
@EnableWebSecurity
class SecurityConfig extends SecuritySupport {

  @Autowired
  val securityService: SecurityService = SpringBean.DEFAULT

  @Bean
  def logoutListener = {
    new LogoutListener
  }


  override def configuration(http: HttpSecurity): Unit = {
    http
      .csrf().disable()
      .formLogin.usernameParameter("uname").passwordParameter("passwd")
      .loginProcessingUrl("/login_process")
      .loginPage("/login")
      .failureUrl("/login?error=t")
      .defaultSuccessUrl("/dashboard")
      //      .successHandler(successHandler)
      //      .failureHandler(failureHandler)
      .and
      .httpBasic
      .and
      .logout
      .logoutUrl("/logout")
      .logoutSuccessUrl("/")
      .invalidateHttpSession(true)
      //      .logoutSuccessHandler(logoutHandler)
      .and
      .authorizeRequests
      .antMatchers(
        "/",
        "/login",
        "/register",
        "/resources/**"
      ).permitAll
      //.antMatchers(HttpMethod.GET, "/auth/role").hasRole("role")
      .antMatchers(
        "/dashboard",
        "/dashboard/**",
        "/logout"
      ).fullyAuthenticated
      //.antMatchers("/booking/view").hasRole("BOOKING_VIEW")
      .anyRequest.fullyAuthenticated
  }

  override def userResolver(username: String): AltairUserDetails[WebUser] = {
    securityService.loadUserByUsername(username)
  }
}
