package taptempo

import chisel3._

class TapTempo extends Module {
  val io = IO(new Bundle {
    val bpm = Output(UInt(9.W))
    val button = Input(Bool())
  })

  val bpm_o = Reg(UInt(9.W))

  io.bpm := bpm_o
  bpm_o := io.button
}
