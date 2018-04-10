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


  val tclk = tptmp.TCLK_NS //ns

  printf("tclk %d\n", tclk)

  val tms = 10*tclk
  val ts = 1000*tms

  for(i <- 0 to 6) {
    peek(tptmp.io.button)
    pushbutton(tptmp.io.button)
    step(1*tms)
  }

  printf("End of Test %d\n", ts)

}
