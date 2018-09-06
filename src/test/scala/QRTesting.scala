import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

/**
  * Performance test for the Patient searching
  * Please run these tests on gatling independent version, they can not be ran on intelliJ
  */
class QRTesting extends Simulation {
  val httpConf = http
    // .baseURL("https://fhirfacade.azurewebsites.net")
    .baseURL("http://localhost:8088")
    .inferHtmlResources(BlackList(""".*\.css""", """.*\.js""", """.*\.ico"""), WhiteList())
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3")
    .connectionHeader("keep-alive")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:33.0) Gecko/20100101 Firefox/33.0")


  val userNumber = 100
  val runTime = 1 minutes


  val searchCase1 = scenario("Test case 1")
    .pause(5,10)
    .exec(http("search ID, procedure ID, Patient name")
      .get("/questionnaireresponse?patient=doge&subject=dummy&identifier=4&parent=596"))

  val searchCase2 = scenario("test case 2")
    .pause(8,15)
    .exec(http("search Questionnaire, status, author & authored")
      .get("/questionnaireresponse?questionnaire=EQ-5D+3L+INDEX&status=completed&author=system&authored=2018-07-22"))

  val searchCase3 = scenario("test case 3")
    .pause(3)
    .exec(http("get all Questionnaire Responses")
      .get("/questionnaireresponse/all"))

  val searchCase4 = scenario("test case 4")
    .pause(3)
    .exec(http("search ID")
      .get("/questionnaireresponse?identifier=4"))

  val searchCase5 = scenario("test case 5")
    .pause(3)
    .exec(http("search patient family")
      .get("/questionnaireresponse?patient=doge"))


  val users1 = scenario("Users 1")
    .exec(searchCase1)

  val users2 = scenario("Users 2")
    .exec(searchCase2)

  val users3 = scenario("Users 3")
    .exec(searchCase3)

  val users4 = scenario("Users 4")
    .exec(searchCase4)

  val users5 = scenario("Users 5")
    .exec(searchCase5)


  setUp(
    users1.inject(
      rampUsers(userNumber) over (runTime)
    ).protocols(httpConf),
    users2.inject(
      rampUsers(userNumber) over (runTime)
    ).protocols(httpConf),
    users3.inject(
      rampUsers(userNumber) over (runTime)
    ).protocols(httpConf),
    users4.inject(
      rampUsers(userNumber) over (runTime)
    ).protocols(httpConf),
    users5.inject(
      rampUsers(userNumber) over (runTime)
    ).protocols(httpConf)
  )
}

