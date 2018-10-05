package com.app

object AppModel {

  case class AppResult(id:Int,l1:String, l2:String, l3:String, param_title:String, desc:String, option1:String,option2:String,option3:String,option4:String)
  case class AppPost(id:Int,l1:String, appparam:String, option:String,score:Int)
  case class AppScore(l1:String, score:Int)

}
