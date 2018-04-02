package taptempo

import chisel3._
import chisel3.iotesters
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}
import scala.language.reflectiveCalls

class TapTempoUnitTester(t: TapTempo) extends PeekPokeTester(t) {
  private val tptmp = t

  def pushbutton(button: Bool) {
   poke(button, 0)
   step(1)
   poke(button, 1)
   step(10)
   poke(button, 0)
  }

  val tclk = 10
  val tus = 1000/tclk
  val tms = 1000*tus
  val ts = 1000*tms

  //0
  pushbutton(tptmp.io.button)
  step(2*tus)
  //1
  pushbutton(tptmp.io.button)
  step(1*tus)
  //2
  pushbutton(tptmp.io.button)
  step(1*tus)
  //3
  pushbutton(tptmp.io.button)
  step(1*tus)
  //4
  pushbutton(tptmp.io.button)
  step(1*tus)
  //5
  pushbutton(tptmp.io.button)
  step(1*tus)
  //6
  pushbutton(tptmp.io.button)
  step(1*tus)

}


class TapTempoTester extends ChiselFlatSpec {
  behavior of "TapTempoTester"

  it should "launch a simple test" in {
    chisel3.iotesters.Driver(() => new TapTempo()) {
      c => new TapTempoUnitTester(c)
    } should be(true)
  }
}
