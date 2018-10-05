package com.app

import util._
import org.scalajs.dom._

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import org.scalajs.dom.ext.Ajax

import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import org.scalajs.dom
import dom.document
import org.scalajs.dom.html.{Div, FieldSet, Input}
import org.scalajs.dom.raw.{HTMLDivElement, HTMLElement}
//import org.scalajs.dom.html.Div
import upickle.default._
import upickle.default.{macroRW, ReadWriter => RW}
import scalatags.JsDom.all._
import AppModel._
import Protocol._

@JSExportTopLevel("APMApp")
object APMApp {

  val jsonstr=
    """
      |[
      |    {
      |        "id":1,
      |        "l3": "Ease of change",
      |        "l1": "Application Maturity",
      |        "desc": "What is the level of confidence in applying change?",
      |        "option1": "Very low",
      |        "option4": "high",
      |        "param_title": "Change Confidence",
      |        "option3": "medium",
      |        "l2": "Complexity",
      |        "option2": "low"
      |    },
      |    {
      |        "id":2,
      |        "l3": "Ease of change",
      |        "l1": "Application Maturity",
      |        "desc": "What is the level of effort (in PH) to implement change in application?\r\n?",
      |        "option1": ">100PH",
      |        "option4": "<20PH",
      |        "param_title": "Size of Change",
      |        "option3": ">20PH and <50PH",
      |        "l2": "Complexity",
      |        "option2": ">50PH and <100PH"
      |    },
      |    {
      |        "id":3,
      |        "l3": "Data Integrity Risk",
      |        "l1": "Application Risks",
      |        "desc": "How well does the application ensure data integrity",
      |        "option1": "a",
      |        "option4": "d",
      |        "param_title": "Data Integrity Risk",
      |        "option3": "c",
      |        "l2": "Risks",
      |        "option2": "b"
      |    },
      |
      |]
    """.stripMargin

  /*object AppResultImpli{
    implicit def rw: RW[AppResult] = macroRW
    implicit  def rw1:RW[AppPost] =macroRW
  }*/
  val ws = new dom.WebSocket("ws://127.0.0.1:9090/greeter")
  @JSExport
  def main(args: Array[String]): Unit = {
    val applist = read[List[AppResult]](jsonstr)

    updateProgress
//    println("app maturity - start")
//    Ajax.get("http://localhost:9090/progress/Application Maturity").map(_.responseText).map(s => println(s))
//    println("app maturity - end ")
    ws.onmessage = (x: MessageEvent) => println(x.data.toString)
    ws.onopen = (x: Event) => {}
    ws.onclose = (x: CloseEvent) => {}

  //  applist.map(x => println(x.l1))
    val mydiv = document.getElementById("mydiv")

    val d = div(
      ul(
        for(list <- applist) yield {
          li(
            p(s"${list.l1} > ${list.l2} > ${list.l3} > ${list.param_title}"),
            p(list.desc),
            createOption(list)
          )
        }
      )
    )
    mydiv.appendChild(d.render)

   // d.render

    /*val f=Ajax.get("http://localhost:9090/app/")
      //.map(r => println(JSON.parse(r.responseText)))
    //println(document.getElementById("mydiv"))
      f.onComplete{
      	case Success(xhr) => 
      	val json = js.JSON.parse(xhr.responseText)
      	//addPre(document.body, js.JSON.stringify(
        //js.JSON.parse(xhr.responseText),
       // space=4
     // ))

      	case Failure(e) => 	println(e.toString)
      }*/
    //showOnScreen(applist)

   }


  def createOption(rec:AppResult):  ConcreteHtmlTag[Div] = {
    lazy val options = div(
      input(tpe:="radio", name:="options", value:=s"${rec.option1}", id:=s"${rec.option1}_id", onclick:=handleoption(rec.id,rec.l1, rec.param_title, rec.option1,1)),
      label(`for`:=s"${rec.option1}_id", s"${rec.option1}"),
      input(tpe:="radio", name:="options", value:=s"${rec.option2}", id:=s"${rec.option2}_id",onclick:=handleoption(rec.id,rec.l1, rec.param_title, rec.option2,2)),
      label(`for`:=s"${rec.option2}_id", s"${rec.option2}"),
      input(tpe:="radio", name:="options", value:=s"${rec.option3}", id:=s"${rec.option3}_id",onclick:=handleoption(rec.id,rec.l1, rec.param_title, rec.option3,3)),
      label(`for`:=s"${rec.option3}_id", s"${rec.option3}"),
      input(tpe:="radio", name:="options", value:=s"${rec.option4}", id:=s"${rec.option4}_id",onclick:=handleoption(rec.id,rec.l1, rec.param_title, rec.option4,4)),
      label(`for`:=s"${rec.option4}_id", s"${rec.option4}")

    )
    options
  }
  //def handleoption(id:String,op:String)= (e:Event) => println(s"${id} - ${op}"/*e.currentTarget.asInstanceOf[Input].value*/)
  /*def handleoption(id:String,op:String) = (e:Event) => {
    println("test")
  }*/
  def handleoption(id:Int, l1:String, appparam:String, selectedoption:String,score:Int) =(e:Event) => {

    //ws.send("hello")
    println(s"${id.toString} - ${selectedoption}")
    /*val f=Ajax.get("http://localhost:9090/app/")
    //.map(r => println(JSON.parse(r.responseText)))
    //println(document.getElementById("mydiv"))
    f.onComplete{
      case Success(xhr) =>
        import AppResultImpli._
        val json = read[List[AppResult]](xhr.responseText)
        println(json)

      case Failure(e) => 	println(e.toString)
    }*/
    val apppost = AppPost(id,l1,appparam,selectedoption,score)
//    println(write(apppost))
    val headersMap:Map[String,String]  = Map("Content-Type" -> "application/json")
     Ajax.post("http://localhost:9090/", data = write(apppost), headers = headersMap).onComplete{
       case Success(xhr) => xhr.responseText
       case Failure(e) => println(e.toString)
     }
    //e.stopPropagation()
    //e.preventDefault()
  }
  def addPre(targetNode: dom.Node, text: String): Unit = {
    val parNode = document.createElement("pre")
    val textNode = document.createTextNode(text)
    parNode.appendChild(textNode)
    targetNode.appendChild(parNode)
  }

  def updateProgress : Unit ={

     Ajax.get("http://localhost:9090/progress").onComplete {
       case Success(xhr) => {
         val scores = read[List[AppScore]](xhr.responseText)
         scores.foreach { s =>
           s.l1 match {
             case "Application Maturity" => populateProgressDiv(getDiv("AppMaturityBar"), s)
             case "Application Risks" => populateProgressDiv(getDiv("AppRiskBar"), s)
           }

         }
       }
       case Failure(e) => println((e.toString))
     }
  }

  def populateProgressDiv(div:HTMLDivElement, s:AppScore): Unit ={
    div.style.width =s"${s.score}%"
    div.innerHTML = s"${s.score}%"
  }

  val getDiv : String => HTMLDivElement = s => document.getElementById(s).asInstanceOf[HTMLDivElement]


//
//  def showOnScreen(applist:List[AppResult]): Unit = {
//   //target.appendChild(
//     div(
//       ul(
//         for(list <- applist) yield
//           li(
//             p(list.desc)
//           )
//       )
//     ).render
//   //)
//  }
}