import sbt._

object Libs {
  val `akka-http`           = "com.typesafe.akka"     %% "akka-http"                % "10.1.8"
  val `akka-http-play-json` = "de.heikoseeberger"     %% "akka-http-play-json"      % "1.25.2"
  val gatling               = "io.gatling"            % "gatling-test-framework"    % "3.1.2" % "it"
  val `gatling-charts`      = "io.gatling.highcharts" % "gatling-charts-highcharts" % "3.1.2" % "it"
}
