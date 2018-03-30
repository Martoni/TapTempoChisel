package taptempo

import chisel3.iotesters
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

class TapTempoUnitTester(t: TapTempo) extends PeekPokeTester(t) {
  private val tptmp = t

  for(i <- 1 to 100 by 1) {
    poke(tptmp.io.button, 1)
    step(1)
//    expect(tptmp.io.bpm, 0)
//    expect(tptmp.io.bpm, 1)
    poke(tptmp.io.button, 0)
    step(1)
  }
}


class TapTempoTester extends ChiselFlatSpec {
  behavior of "TapTempoTester"

  it should "launch a simple test" in {
    chisel3.iotesters.Driver(() => new TapTempo()) {
      c => new TapTempoUnitTester(c)
    } should be(true)
  }
}
