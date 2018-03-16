package taptempo

import chisel3._

class TapTempo extends Module {
  val io = IO(new Bundle {
    val button = Input(Bool())
    val bpm = Output(UInt(16.W))
  })
}
