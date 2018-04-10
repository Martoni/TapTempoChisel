package taptempo

import chisel3._
import chisel3.util.{Counter, PriorityEncoder}
import scala.language.reflectiveCalls  //avoid reflective call warnings
import scala.math.pow

// default clock 100Mhz -> T = 10ns
class TapTempo(tclk_ns: Int) extends Module {
  val io = IO(new Bundle {
//    val bpm = Output(UInt(8.W))
    val bpm = Output(UInt(270.W))
    val button = Input(Bool())
  })
  /* Constant parameters */
  val MINUTE_NS = 60*1000*1000*1000L
  val PULSE_NS = 1000*1000
  val TCLK_NS = tclk_ns

  /* usefull function */
  def risingedge(x: Bool) = x && !RegNext(x)

  val tp_count = RegInit(0.asUInt(16.W))
  val (pulsecount, timepulse) = Counter(true.B, PULSE_NS/tclk_ns)

  val countx = RegInit(Vec(Seq.fill(4)(0.asUInt(16.W))))
  val count_mux = RegInit(0.asUInt(2.W))
  val sum = Wire(UInt(19.W))

  /* div array calculation */
  val x = Seq.tabulate(pow(2,16).toInt-1)(n => ((MINUTE_NS/PULSE_NS)/(n+1)).U)
  val bpm_calc = RegInit(Vec(Seq.tabulate(270)(n => x(n+1))))
  val bpm_ineq = RegInit(Vec(Seq.fill(270)(0.asUInt(1.W))))

  val sum_by_4 = sum(18, 2)
  for(i <- 0 to 269) {
    bpm_ineq(i) := Mux(sum_by_4 < bpm_calc(i), 1.U, 0.U)
  }

  when(timepulse) {
    tp_count := tp_count + 1.U
  }
  when(risingedge(io.button)){
    countx(count_mux) := tp_count
    count_mux := Mux(count_mux === 3.U, 0.U, count_mux + 1.U)
    tp_count := 0.U
  }

  sum := countx(0) + countx(1) + countx(2) + countx(3)

  io.bpm := bpm_ineq.asUInt()
//  io.bpm := bpm_calc(269).asUInt()
}
