// See LICENSE for license details.
package sifive.freedom.everywhere.e300hbirdkit

import Chisel._
import chisel3.core.{attach}
import chisel3.experimental.{withClockAndReset}

import freechips.rocketchip.config._
import freechips.rocketchip.diplomacy.{LazyModule}

import sifive.blocks.devices.gpio._
import sifive.blocks.devices.spi._

import sifive.fpgashells.shell.xilinx.hbirdshell.{HBirdShell}
import sifive.fpgashells.ip.xilinx.{IBUFG, IOBUF, PULLUP, PowerOnResetFPGAOnly}

//-------------------------------------------------------------------------
// E300HBirdKitFPGAChip
//-------------------------------------------------------------------------

class E300HBirdKitFPGAChip(implicit override val p: Parameters) extends HBirdShell {

  //-----------------------------------------------------------------------
  // DUT
  //-----------------------------------------------------------------------

  withClockAndReset(clock_65MHz, ck_rst) {
    val dut = Module(new E300HBirdKitPlatform)

    //---------------------------------------------------------------------
    // SPI flash IOBUFs
    //---------------------------------------------------------------------

    IOBUF(qspi_sck, dut.io.pins.qspi.sck)
    IOBUF(qspi_cs,  dut.io.pins.qspi.cs(0))

    IOBUF(qspi_dq(0), dut.io.pins.qspi.dq(0))
    IOBUF(qspi_dq(1), dut.io.pins.qspi.dq(1))
    IOBUF(qspi_dq(2), dut.io.pins.qspi.dq(2))
    IOBUF(qspi_dq(3), dut.io.pins.qspi.dq(3))

    //---------------------------------------------------------------------
    // JTAG IOBUFs
    //---------------------------------------------------------------------

    dut.io.pins.jtag.TCK.i.ival := IBUFG(IOBUF(mcu_TCK).asClock).asUInt

    IOBUF(mcu_TMS, dut.io.pins.jtag.TMS)
    PULLUP(mcu_TMS)

    IOBUF(mcu_TDI, dut.io.pins.jtag.TDI)
    PULLUP(mcu_TDI)

    IOBUF(mcu_TDO, dut.io.pins.jtag.TDO)

    // mimic putting a pullup on this line (part of reset vote)
    SRST_n := IOBUF(jd_6)
    PULLUP(jd_6)

    // jtag reset
    val jtag_power_on_reset = PowerOnResetFPGAOnly(clock_65MHz)
    dut.io.jtag_reset := jtag_power_on_reset

    // debug reset
    dut_ndreset := dut.io.ndreset

    IOBUF(uart_txd_in, dut.io.pins.gpio.pins(16))
    IOBUF(uart_rxd_out, dut.io.pins.gpio.pins(17))

    IOBUF(gpio(0), dut.io.pins.gpio.pins(0))
    IOBUF(gpio(1), dut.io.pins.gpio.pins(1))
    IOBUF(gpio(2), dut.io.pins.gpio.pins(2))
    IOBUF(gpio(3), dut.io.pins.gpio.pins(3))
    IOBUF(gpio(4), dut.io.pins.gpio.pins(4))
    IOBUF(gpio(5), dut.io.pins.gpio.pins(5))

    dut.io.pins.gpio.pins(6).i.ival  := 0.U
    dut.io.pins.gpio.pins(7).i.ival  := 0.U
    dut.io.pins.gpio.pins(8).i.ival  := 0.U

    IOBUF(gpio(9), dut.io.pins.gpio.pins(9))
    IOBUF(gpio(10), dut.io.pins.gpio.pins(10))
    IOBUF(gpio(11), dut.io.pins.gpio.pins(11))
    IOBUF(gpio(12), dut.io.pins.gpio.pins(12))
    IOBUF(gpio(13), dut.io.pins.gpio.pins(13))
    IOBUF(gpio(14), dut.io.pins.gpio.pins(14))
    IOBUF(gpio(15), dut.io.pins.gpio.pins(15))

    IOBUF(gpio(18), dut.io.pins.gpio.pins(18))
    IOBUF(gpio(19), dut.io.pins.gpio.pins(19))
    IOBUF(gpio(20), dut.io.pins.gpio.pins(20))
    IOBUF(gpio(21), dut.io.pins.gpio.pins(21))
    IOBUF(gpio(22), dut.io.pins.gpio.pins(22))
    IOBUF(gpio(23), dut.io.pins.gpio.pins(23))

    val iobuf_mcu_wakeup = Module(new IOBUF())
    iobuf_mcu_wakeup.io.I := ~dut.io.pins.aon.pmu.dwakeup_n.o.oval
    iobuf_mcu_wakeup.io.T := ~dut.io.pins.aon.pmu.dwakeup_n.o.oe
    attach(mcu_wakeup, iobuf_mcu_wakeup.io.IO)
    dut.io.pins.aon.pmu.dwakeup_n.i.ival := ~iobuf_mcu_wakeup.io.O & dut.io.pins.aon.pmu.dwakeup_n.o.ie

    IOBUF(gpio(24), dut.io.pins.gpio.pins(24)) // UART1 RX
    IOBUF(gpio(25), dut.io.pins.gpio.pins(25)) // UART1 TX

    IOBUF(gpio(26), dut.io.pins.gpio.pins(26))
    IOBUF(gpio(27), dut.io.pins.gpio.pins(27))
    IOBUF(gpio(28), dut.io.pins.gpio.pins(28))
    IOBUF(gpio(29), dut.io.pins.gpio.pins(29))
    IOBUF(gpio(30), dut.io.pins.gpio.pins(30))
    IOBUF(gpio(31), dut.io.pins.gpio.pins(31))

    // Use the LEDs for some more useful debugging things
    IOBUF(led_0, ck_rst)
    IOBUF(led_1, SRST_n)
    IOBUF(led_2, dut.io.pins.aon.pmu.dwakeup_n.i.ival)

    // IOBUF(led_0, dut.io.pins.gpio.pins(0))
    // IOBUF(led_1, dut.io.pins.gpio.pins(1))
    // IOBUF(led_2, dut.io.pins.gpio.pins(2))

    //---------------------------------------------------------------------
    // Unconnected inputs
    //---------------------------------------------------------------------

    dut.io.pins.aon.erst_n.i.ival       := ~reset_periph
    dut.io.pins.aon.lfextclk.i.ival     := CLK32768KHZ.asUInt
    dut.io.pins.aon.pmu.vddpaden.i.ival := 1.U
  }
}
