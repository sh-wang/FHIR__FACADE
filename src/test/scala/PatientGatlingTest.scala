import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

/**
  * Performance test for the Patient searching.
  */
class PatientGatlingTest extends Simulation {
    val httpConf = http
      // .baseURL("https://fhirfacade.azurewebsites.net")
      .baseURL("http://localhost:8088")
      .inferHtmlResources(BlackList(""".*\.css""", """.*\.js""", """.*\.ico"""), WhiteList())
      .acceptHeader("*/*")
      .acceptEncodingHeader("gzip, deflate")
      .acceptLanguageHeader("fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3")
      .connectionHeader("keep-alive")
      .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:33.0) Gecko/20100101 Firefox/33.0")


    val patientSearch = scenario("Test search patients")
      .exec(http("search gender").get("/patient?gender=male"))
      .pause(1)
      .exec(http("get all patients").get("/patient/all"))
      .pause(1)
      .exec(http("search NHS number").get("/patient?identifier=9999999061"))
      .pause(1)
      .exec(http("search family name").get("/patient?family=Christian"))
      .pause(1,2)
      .exec(http("search given name").)
      .pause(1,2)
      .exec(http("search full name").get("/patient?name=Charissa+Spence"))
      .pause(2,4)
      .exec(http("search birthday and email").get("/patient?birthdate=1947-01-27&email=test@test.com"))



    val users = scenario("Users")
      .exec(patientSearch)


    setUp(
        users.inject(
            nothingFor(3 seconds),
            rampUsers(100) over (10 minutes)
        ).protocols(httpConf)
    )
}
