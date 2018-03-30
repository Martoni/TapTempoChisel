package taptempo

import chisel3._
import chisel3.util.Counter

class TapTempo(tclk_ns: Int = 10) extends Module {
  val io = IO(new Bundle {
    val bpm = Output(UInt(9.W))
    val button = Input(Bool())
  })
  val PULSE_NS = 120000

  val bpm_o = Reg(UInt(9.W))
  val (pulsecount, timepulse) = Counter(true.B, PULSE_NS/tclk_ns)

  io.bpm := pulsecount

//  io.bpm := bpm_o
  bpm_o := io.button
}
