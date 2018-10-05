package com.app

import upickle.default.{macroRW, ReadWriter => RW}
import AppModel._

object Protocol {
  implicit def rw: RW[AppResult] = macroRW
  implicit  def rw1:RW[AppPost]  = macroRW
  implicit  def rw2:RW[AppScore]  = macroRW
}


