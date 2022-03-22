package me.devtools4.ts

import java.util.Properties

package object config {

  case class DbConfig(driver: String,
                      url: String,
                      user: String,
                      password: String,
                      logLevel: String)

  case class ServiceConfig(kafka: Map[String, String],
                           db: DbConfig)

  object Ops {
    implicit class MapExt(map: Map[String, String]) {
      def properties: Properties = {
        map.foldLeft(new Properties()) {
          (p, a) => {
            p.put(a._1, a._2)
            p
          }
        }
      }
    }
  }
}