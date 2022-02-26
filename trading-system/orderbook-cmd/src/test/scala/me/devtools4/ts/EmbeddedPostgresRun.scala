package me.devtools4.ts

import com.opentable.db.postgres.embedded.EmbeddedPostgres
import org.flywaydb.core.Flyway

object EmbeddedPostgresRun extends App {
  val db = EmbeddedPostgres.builder()
    .start()
  System.out.println(s"host=${db.getHost}, port=${db.getPort}")
  val ds = db.getPostgresDatabase()
  val fw = Flyway.configure().dataSource(ds).load()
  fw.migrate()
  Thread.sleep(100000)
  db.close();
}
