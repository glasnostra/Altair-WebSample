package org.altairtoolkit.sample.web.config

import java.util.Locale

import org.altairtoolkit.locale.config.LocaleSupport
import org.altairtoolkit.scalatra.config.ScalatraSupport
import org.springframework.context.annotation._


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
@ComponentScan(value = Array("org.altairtoolkit.sample.web.servlet"))
class WebScalatraConfig extends ScalatraSupport with LocaleSupport {
  override def defaultLocale: Locale = {
    new Locale("in", "id")
  }

  override def supportedLocales: Array[Locale] = {
    Array(Locale.ENGLISH, new Locale("in", "id"))
  }

  override def baseNames: Array[String] = {
    Array("i18n/messages")
  }
}

