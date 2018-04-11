package taptempo

import chisel3.Bool
import chisel3.iotesters
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}
import scala.language.reflectiveCalls

class TapTempoUnitTester(t: TapTempo) extends PeekPokeTester(t) {
  val tptmp = t

  printf("Begin of test\n")

  def pushbutton(button: Bool) {
   poke(button, 0)
   step(2)
   print("Push \n")
   poke(button, 1)
   step(2)
   poke(button, 0)
  }


  val tclk = tptmp.TCLK_NS //ns

  printf("tclk step time -> %d ns\n", tclk)

  val tms = 10
  val ts = 1000*tms

  for(i <- 0 to 6) {
    pushbutton(tptmp.io.button)
    step(ts)
  }
  expect(tptmp.io.bpm, 60)

  for(i <- 0 to 10) {
    pushbutton(tptmp.io.button)
    step(100*tms)
  }
  expect(tptmp.io.bpm, 270)

  for(i <- 0 to 10) {
    pushbutton(tptmp.io.button)
    step(500*tms)
  }
  expect(tptmp.io.bpm, 120)

  printf("End of Test %d\n", ts)

}
