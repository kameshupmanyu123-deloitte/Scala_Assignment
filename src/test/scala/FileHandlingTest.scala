import FileHandling.getLongestDurationByCountry
import org.scalatest.funsuite.AnyFunSuite



class FileHandlingTest extends AnyFunSuite {

    test("get Longest duration by country") {

        val res = getLongestDurationByCountry("France")
        assert(res == "Les vampires");

    }

    test("get Highest budget Title by Year") {

        val res = FileHandling.getHighestBudgetTitleByYear(1914)
        assert(res == "Cabiria");

    }
}



