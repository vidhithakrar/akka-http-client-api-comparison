name := "akka-http-client-api-comparison"

version := "0.1"

scalaVersion := "2.12.8"

lazy val `financial-data-service` = project
  .settings(
    libraryDependencies ++= Seq(
      Libs.`akka-http`,
      Libs.`akka-http-play-json`)
  )

lazy val `financial-portfolio-service` = project
  .configs(IntegrationTest)
  .enablePlugins(GatlingPlugin)
  .settings(
    Defaults.itSettings,
    libraryDependencies ++= Seq(
      Libs.`akka-http`,
      Libs.`akka-http-play-json`,
      Libs.gatling,
      Libs.`gatling-charts`)
  )

lazy val aggregatedProjects = Seq(`financial-data-service`, `financial-portfolio-service`)
