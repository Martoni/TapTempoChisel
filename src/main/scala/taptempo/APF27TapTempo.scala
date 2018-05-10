package taptempoplatform

import chisel3._
import chisel3.util.{ShiftRegister, Cat}
import scala.language.reflectiveCalls  //avoid reflective call warnings
import taptempo.{TapTempo}

class APF27TapTempo extends Module {
  val io = IO(new Bundle {
    val data = Output(UInt(16.W))
    val button = Input(Bool())
  })

  val button_s = ShiftRegister(io.button, 2)
  val button_deb = RegInit(false.B)

  /* Debounce */
  val clk_freq_khz = 100000
  val debounce_per_ms = 20
  val MAX_COUNT = (clk_freq_khz * debounce_per_ms) + 1
  val debcounter = RegInit(MAX_COUNT.U)

  def risingedge(x: Bool) = x && !RegNext(x)
  def fallingedge(x: Bool) = !x && RegNext(x)

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
  io.data := Cat("b0000000".U, tap_tempo.io.bpm)
  tap_tempo.io.button := button_deb
}

object APF27TapTempoDriver extends App {
  chisel3.Driver.execute(args, () => new APF27TapTempo)
}
