import scala.io.Source
import com.typesafe.config.{Config, ConfigFactory}

import java.io.{FileNotFoundException, FileReader}
import java.util;

object FileHandling extends App {

// 1. Titles directed by given director in the given year range e.g.
   // generate titles report for director D.W. Griffith and year range
   

   def getTitleForGivenDirector(director: String, s: Int, l: Int) = {
      // Loading the path of File from config file
      val applicationConf: Config = ConfigFactory.load("application.conf")
      val f = applicationConf.getString("app.file");


     // Reading the File Line by Line

      try {

         val df = Source.fromFile(f).getLines().toList
         df.foreach(line => {
            var str = line.split(",");
            var year = 0;
            try {
               year = str(3).toInt;

               if (director.equals(str(9))) {
                  if (year > s && year < l)
                     println(str(1));

               };
            }
            catch {
               case x: NumberFormatException => {}
            }


         })
      }
      catch {
         case x: FileNotFoundException =>{println("Invalid Path or File Not Found")}
      }

   }

   //2. Generate report of English titles which have user reviews more than given user
   // review filter and sort the report with user reviews by descending

   def getTitleHavingMoreReview(review : Int): List[String]= {
      val applicationConf: Config = ConfigFactory.load("application.conf")
      val f = applicationConf.getString("app.file");
      var res = List("")

      try {
         val df = Source.fromFile(f).getLines().toList
         val col = df.head.split(",")
         //println(col.length)
         val colIndex = col.indexOf("reviews_from_users")

         // Reading the Line and Splitting Based on Comma but ignoring them in Double quotes

         val data = df.tail.map(line => {
            val index = line.split(",(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)")
            //println(index.length);
            if (colIndex < index.length)
               line.split(",(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)")(colIndex)
         })
         val list: List[Int] = data.flatMap {
            case i: String => i.toIntOption
            case _ => None
         }

         //data.foreach(println);
         var c = 0;

         val ReverseOrderList = list.sorted(Ordering.Int.reverse)
         ReverseOrderList.foreach(el => {
            c = c + 1;
            if (el > review) {
               val str = df(c).split(",").toList
               res = res :+ str(1);
            }
         })
      }
      catch {
         case x: FileNotFoundException =>{println("Invalid Path or File Not Found")}
      }

     res
   }

   //3. Generate highest budget titles for the given year and country filters

   def getHighestBudgetTitleByYear(year : Int): String= {
      val applicationConf: Config = ConfigFactory.load("application.conf")
      val f = applicationConf.getString("app.file");
      var res=""

      //loading 10000 line from csv file to df
      try {
         val df = Source.fromFile(f).getLines().toList
         val col = df.head.split(",")

         val colIndex = col.indexOf("year")
         val BudIndex = col.indexOf("budget")
         var budget = 0;
         //println(BudIndex)
         df.foreach(line => {
            var str = line.split(",(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)").toList
            try {
               if (str(colIndex).toInt == year) {
                  val num = str(BudIndex).replace(",", "").replace("$", "").replace("\"", "")
                  if (num.length > 0) {
                     if (num.toInt > budget)
                        budget = num.toInt
                  }
               }

            }
            catch {
               case x: NumberFormatException => {}
            }
         })
         df.foreach(line => {
            var str = line.split(",(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)").toList

            try {
               val num = str(BudIndex).replace(",", "").replace("$", "").replace("\"", "")
               if (num.toInt == budget && str(3).toInt == year)
                  res = str(1)

            }
            catch {
               case x: NumberFormatException => {}
               case _: IndexOutOfBoundsException => {}
            }
         })
      }
      catch {
         case x: FileNotFoundException =>{println("Invalid Path or File Not Found")}
      }
      res
   }

  // 4. Generate report of longest duration title for the given country filter

   def getLongestDurationByCountry(country: String):String={
      val applicationConf: Config = ConfigFactory.load("application.conf")
      val f = applicationConf.getString("app.file");
      var res = ""

      //loading 10000 line from csv file to df
      try {
         val df = Source.fromFile(f).getLines().toList

         try {
            val col = df.head.split(",")

            val DIndex = col.indexOf("duration")
            val CIndex = col.indexOf("country")
            var duration = 0;
            //println(CIndex)
            df.foreach(line => {
               var str = line.split(",(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)").toList
               try {
                  if (str(CIndex) == country) {
                     val num = str(DIndex).replace(",", "").replace("$", "").replace("\"", "")
                     if (num.length > 0) {
                        if (num.toInt > duration)
                           duration = num.toInt
                     }
                  }

               }
               catch {
                  case x: NumberFormatException => {}
               }
            })

            //println(duration)
            df.foreach(line => {
               var str = line.split(",(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)").toList

               try {
                  val num = str(DIndex).replace(",", "").replace("$", "").replace("\"", "")
                  if (num.toInt == duration && str(CIndex) == country)
                     res = str(1);

               }
               catch {
                  case x: NumberFormatException => {}
                  case _: IndexOutOfBoundsException => {}
               }
            })
         }
         catch {
            case x: NullPointerException => {}
         }
      }
      catch {
         case x: FileNotFoundException =>{println("Invalid Path or File Not Found")}
      }
       res;
   }

   getTitleForGivenDirector("D.W. Griffith", 1913, 1915);

   getTitleHavingMoreReview(7);


   getHighestBudgetTitleByYear(1914)

   getLongestDurationByCountry("France")

}