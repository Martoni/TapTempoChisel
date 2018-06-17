package taptempo

import chisel3._
import chisel3.experimental.Analog
import chisel3.experimental._
import chisel3.util.{ShiftRegister, Cat}
import chisel3.util.{HasBlackBoxInline, HasBlackBoxResource}
import scala.language.reflectiveCalls  //avoid reflective call warnings

class Apf27Bus extends BlackBox with HasBlackBoxInline {
  val io = IO(new Bundle {
    val dataout = Output(UInt(16.W))
    val dataio = Analog(16.W)
    val datain = Input(UInt(16.W))
    val oe = Input(Bool())
  })

  setInline("Apf27Bus.v",
    s"""
    |module Apf27Bus(
    |     output [15:0] dataout,
    |     inout [15:0] dataio,
    |     input [15:0] datain,
    |     input oe);
    |
    |   assign dataio = (oe == 'b0) ? datain : 'bzzzzzzzzzzzzzzzz;
    |   assign dataout = dataio;
    |endmodule
    """.stripMargin)
}

class APF27TapTempo extends Module {
  val io = IO(new Bundle {
//    val data = Output(UInt(16.W))
    val data = Analog(16.W)
    val oe = Input(Bool())
    val button = Input(Bool())
  })

  val dataout = Wire(UInt(16.W))

  val button_s = ShiftRegister(io.button, 2)
  val button_deb = RegInit(false.B)

  /* Debounce */
  val clk_freq_khz = 100000
  val debounce_per_ms = 20
  val MAX_COUNT = (clk_freq_khz * debounce_per_ms) + 1
  val debcounter = RegInit(MAX_COUNT.U)

  def risingedge(x: Bool) = x && !RegNext(x)
  def fallingedge(x: Bool) = !x && RegNext(x)

  val iobuf = Module(new Apf27Bus)
  io.data <> iobuf.io.dataio
  iobuf.io.datain := dataout
  iobuf.io.oe <> io.oe

  when(debcounter =/= MAX_COUNT.U) {
    debcounter := debcounter + 1.U
  }.otherwise {
    when(risingedge(button_s) || fallingedge(button_s)){
      debcounter := 0.U
      button_deb := button_s
    }
  }

  // TapTempo connection
  val tap_tempo = Module(new TapTempo(10, 270))
  dataout := Cat("b0000000".U, tap_tempo.io.bpm)
  tap_tempo.io.button := button_deb
}

object APF27TapTempoDriver extends App {
  chisel3.Driver.execute(args, () => new APF27TapTempo)
}
