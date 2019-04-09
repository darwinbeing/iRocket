`timescale 1ns/10ps

module tb_sifive_freedom();

   reg CLK100MHZ;
   reg ck_rst;
   wire uart_rxd_out;
   wire uart_txd_in;
   wire qspi_dq_0;
   wire qspi_dq_1;
   wire qspi_dq_2;
   wire qspi_dq_3;

   initial begin
      CLK100MHZ   <=0;
      ck_rst      <=0;
      #15000 ck_rst <=1;
   end

   always
     begin
        #5 CLK100MHZ <= ~CLK100MHZ;
     end


   E300ArtyDevKitFPGAChip dut
     (
      .CLK100MHZ(CLK100MHZ),
      .ck_rst(ck_rst),

      .led_0(),
      .led_1(),
      .led_2(),
      .led_3(),

      .led0_r(),
      .led0_g(),
      .led0_b(),
      .led1_r(),
      .led1_g(),
      .led1_b(),
      .led2_r(),
      .led2_g(),
      .led2_b(),

      .sw_0(),
      .sw_1(),
      .sw_2(),
      .sw_3(),

      .btn_0(),
      .btn_1(),
      .btn_2(),
      .btn_3(),

      .qspi_cs (),
      .qspi_sck(),
      .qspi_dq_0 (),
      .qspi_dq_1 (),
      .qspi_dq_2 (),
      .qspi_dq_3 (),

      .uart_rxd_out(uart_rxd_out),
      .uart_txd_in (uart_txd_in),

      .ja_0(),
      .ja_1(),

      .ck_io_0(),
      .ck_io_1(),
      .ck_io_2(),
      .ck_io_3(),
      .ck_io_4(),
      .ck_io_5(),
      .ck_io_6(),
      .ck_io_7(),
      .ck_io_8(),
      .ck_io_9(),
      .ck_io_10(),
      .ck_io_11(),
      .ck_io_12(),
      .ck_io_13(),
      .ck_io_14(),
      .ck_io_15(),
      .ck_io_16(),
      .ck_io_17(),
      .ck_io_18(),
      .ck_io_19(),

      .ck_miso(),
      .ck_mosi(),
      .ck_ss  (),
      .ck_sck (),

      .jd_0(), // TDO
      .jd_1(), // TRST_n
      .jd_2(), // TCK
      .jd_4(), // TDI
      .jd_5(), // TMS
      .jd_6() // SRST_n
      );

endmodule
