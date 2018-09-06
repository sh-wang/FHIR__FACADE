import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

/**
  * Performance test for the Patient searching.
  * Please run these tests on gatling independent version, they can not be ran on intelliJ
  */
class PatientTesting extends Simulation {
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

  val testCase1 = scenario("Test case 1")
    .exec(http("search gender and NHS number")
      .get("/patient?identifier=9999999061&gender=male"))
    .pause(3)

  val testCase2 = scenario("Test case 2")
    .exec(http("search family name, given name and full name")
      .get("/patient?family=spence&given=Charissa&name=Charissa+Spence"))
    .pause(3)

  val testCase3 = scenario("Test case 3")
    .exec(http("search birthday and email")
      .get("/patient?birthdate=1989-07-28&email=test@test.com"))
    .pause(3)

  val testCase4 = scenario("Test case 4")
    .exec(http("get all patients")
      .get("/patient/all"))

  val testCase5 = scenario("Test case 5")
    .exec(http("search NHS number")
      .get("/patient?identifier=9999999062"))

  val testCase6 = scenario("Test case 6")
    .exec(http("search birthday")
      .get("/patient?birthdate=1954-10-17"))


  val users1 = scenario("Users 1")
    .exec(testCase1)

  val users2 = scenario("Users 2")
    .exec(testCase2)

  val users3 = scenario("Users 3")
    .exec(testCase3)

  val users4 = scenario("Users 4")
    .exec(testCase4)

  val users5 = scenario("Users 5")
    .exec(testCase5)

  val users6 = scenario("Users 6")
    .exec(testCase6)


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
    ).protocols(httpConf),
    users6.inject(
      rampUsers(userNumber) over (runTime)
    ).protocols(httpConf)
  )
}

