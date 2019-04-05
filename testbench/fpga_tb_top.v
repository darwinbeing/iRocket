`timescale 1ns/10ps

module tb_E300HBirdKitFPGAChip();

   reg CLK100MHZ;
   // wire CLK32768KHZ;
   reg CLK32768KHZ;
   reg  fpga_rst;
   reg  mcu_rst;
   wire led_0;
   wire led_1;
   wire led_2;
   wire mcu_wakeup;
   wire qspi_cs;
   wire qspi_sck;
   wire [3:0] qspi_dq;
   wire       uart_rxd_out;
   wire       uart_txd_in;
   wire       mcu_TDO;
   wire       mcu_TCK;
   wire       mcu_TDI;
   wire       mcu_TMS;

   wire [31:0] gpio;
   wire        jd_6;

   assign mcu_wakeup = 1;
   assign jd_6 = 1;

   initial begin
      CLK100MHZ <= 0;
      CLK32768KHZ <= 0;
      fpga_rst <= 0;
      mcu_rst <= 0;
      #15000 fpga_rst <= 1;
      mcu_rst <= 1;
   end

   always
     begin
        #5 CLK100MHZ <= ~CLK100MHZ;
     end

   always
     begin
        // #15259 CLK32768KHZ <= ~CLK32768KHZ;
        #20 CLK32768KHZ <= ~CLK32768KHZ;
     end

   wire clk_8388;
   wire mmcm_locked;
   wire resetn = fpga_rst & mcu_rst;

   // mmcm u_mmcm
   //   (
   //    .resetn(resetn),
   //    .clk_in1(CLK100MHZ),
   //    .clk_out1(clk_8388),
   //    .locked(mmcm_locked)
   //    );

   // clkdivider u_clkdivider
   //   (
   //    .clk(clk_8388),
   //    .reset(~resetn),
   //    .clk_out(CLK32768KHZ)
   //    );

   E300HBirdKitFPGAChip u_E300HBirdKitFPGAChip
     (
      .CLK100MHZ(CLK100MHZ),
      .CLK32768KHZ(CLK32768KHZ),

      .fpga_rst(fpga_rst),
      .mcu_rst(mcu_rst),
      .led_0(led_0),
      .led_1(led_1),
      .led_2(led_2),
      .mcu_wakeup(mcu_wakeup),

      .qspi_cs(qspi_cs),
      .qspi_sck(qspi_sck),
      .qspi_dq_0(qspi_dq[0]),
      .qspi_dq_1(qspi_dq[1]),
      .qspi_dq_2(qspi_dq[2]),
      .qspi_dq_3(qspi_dq[3]),

      .uart_rxd_out(uart_rxd_out),
      .uart_txd_in(uart_txd_in),

      .mcu_TDO(mcu_TDO),
      .mcu_TCK(mcu_TCK),
      .mcu_TDI(mcu_TDI),
      .mcu_TMS(mcu_TMS),

      .gpio_0(gpio[0]),
      .gpio_1(gpio[1]),
      .gpio_2(gpio[2]),
      .gpio_3(gpio[3]),
      .gpio_4(gpio[4]),
      .gpio_5(gpio[5]),
      .gpio_6(gpio[6]),
      .gpio_7(gpio[7]),
      .gpio_8(gpio[8]),
      .gpio_9(gpio[9]),
      .gpio_10(gpio[10]),
      .gpio_11(gpio[11]),
      .gpio_12(gpio[12]),
      .gpio_13(gpio[13]),
      .gpio_14(gpio[14]),
      .gpio_15(gpio[15]),
      .gpio_16(gpio[16]),
      .gpio_17(gpio[17]),
      .gpio_18(gpio[18]),
      .gpio_19(gpio[19]),
      .gpio_20(gpio[20]),
      .gpio_21(gpio[21]),
      .gpio_22(gpio[22]),
      .gpio_23(gpio[23]),
      .gpio_24(gpio[24]),
      .gpio_25(gpio[25]),
      .gpio_26(gpio[26]),
      .gpio_27(gpio[27]),
      .gpio_28(gpio[28]),
      .gpio_29(gpio[29]),
      .gpio_30(gpio[30]),
      .gpio_31(gpio[31]),
      .jd_6(jd_6)
      );

   W25Q32JVxxIM u_w25q32jvm(
                            .CSn(qspi_cs),
                            .CLK(qspi_sck),
                            .DIO(qspi_dq[0]),
                            .DO(qspi_dq[1]),
                            .WPn(qspi_dq[2]),
                            .HOLDn(qspi_dq[3]),
                            .RESETn(resetn));

endmodule
