import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

/**
  * Performance test for the Patient searching.
  * Please run these tests on gatling independent version, they can not be ran on intelliJ
  */
class QPTesting extends Simulation {
  val httpConf = http
    // .baseURL("https://fhirfacade.azurewebsites.net")
    .baseURL("http://localhost:8088")
    .inferHtmlResources(BlackList(""".*\.css""", """.*\.js""", """.*\.ico"""), WhiteList())
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3")
    .connectionHeader("keep-alive")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:33.0) Gecko/20100101 Firefox/33.0")


  val userNumber = 300
  val runTime = 1 minutes


  val QuestionnaireSearch = scenario("Test search Questionnaire")
    .exec(http("search questionnaire ID").get("/questionnaire?id=1"))

  val ProcedureSearch = scenario("Test search procedure")
    .exec(http("search procedure ID").get("/procedure?id=2"))


  val users1 = scenario("Users 1")
    .exec(QuestionnaireSearch)

  val users2 = scenario("Users 2")
    .exec(ProcedureSearch)


  setUp(
    users1.inject(
      rampUsers(userNumber) over (runTime)
    ).protocols(httpConf),
    users2.inject(
      rampUsers(userNumber) over (runTime)
    ).protocols(httpConf)
  )
}

