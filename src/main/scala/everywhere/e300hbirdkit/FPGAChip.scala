// See LICENSE for license details.
package sifive.freedom.everywhere.e300hbirdkit

import Chisel._
import chisel3.core.{attach}
import chisel3.experimental.{withClockAndReset}

import freechips.rocketchip.config._
import freechips.rocketchip.diplomacy.{LazyModule}
import freechips.rocketchip.util.ResetCatchAndSync

import sifive.blocks.devices.gpio._
import sifive.blocks.devices.spi._

import sifive.fpgashells.shell.xilinx.hbirdshell._
import sifive.fpgashells.ip.xilinx.{IBUFG, IOBUF, PULLUP, PowerOnResetFPGAOnly}

import sifive.blocks.devices.pinctrl.{BasePin}
import sifive.blocks.devices.mockaon._
//-------------------------------------------------------------------------
// PinGen
//-------------------------------------------------------------------------

object PinGen {
  def apply(): BasePin = {
    new BasePin()
  }
}

//-------------------------------------------------------------------------
// E300HBirdKitFPGAChip
//-------------------------------------------------------------------------

class E300HBirdKitFPGAChip(implicit override val p: Parameters)
    extends HBirdShell
    with HasDDR3 {

  //-----------------------------------------------------------------------
  // DUT
  //-----------------------------------------------------------------------

  // Connect the clock to the 50 Mhz output from the PLL
  dut_clock := clk50
  withClockAndReset(dut_clock, dut_reset) {
    val dut = Module(LazyModule(new E300HBirdKitSystem).module)

    //---------------------------------------------------------------------
    // Connect peripherals
    //---------------------------------------------------------------------

    connectSPIFlash (dut)
    connectDebugJTAG(dut)
    connectUART     (dut)
    connectMIG      (dut)

    //---------------------------------------------------------------------
    // GPIO
    //---------------------------------------------------------------------

    val gpioParams = p(PeripheryGPIOKey)
    val gpio_pins = Wire(new GPIOPins(() => PinGen(), gpioParams(0)))

    GPIOPinsFromPort(gpio_pins, dut.gpio(0))

    gpio_pins.pins.foreach { _.i.ival := Bool(false) }
    // gpio_pins.pins.zipWithIndex.foreach {
    //   case(pin, idx) => led(idx) := pin.o.oval
    // }

    // // tie to zero
    // for( idx <- 7 to 4 ) { led(idx) := false.B }

    // Use the LEDs for some more useful debugging things
    IOBUF(led_0, ck_rst)
    IOBUF(led_1, SRST_n)
  }
}
