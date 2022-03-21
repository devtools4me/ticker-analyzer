package me.devtools4.ts

package object config {

  case class DbConfig(driver: String,
                      url: String,
                      user: String,
                      password: String,
                      logLevel: String)
}