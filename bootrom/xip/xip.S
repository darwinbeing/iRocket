// See LICENSE for license details.
// Execute in place
// Jump directly to XIP_TARGET_ADDR

#define GPIO_BASE 0x10012000
#define UART_BASE 0x10013000

#define GPIO_OUTPUT_EN  (0x08)
#define GPIO_OUTPUT_VAL (0x0C)
#define UART_REG_TXFIFO         0x00
#define UART_REG_TXCTRL         0x08
#define UART_REG_DIV            0x18
#define UART_TXEN               0x1
#define UART_RXEN               0x1



  .section .text.init
  .option norvc
  .globl _start
_start:

  li t0, 1
  li t1, GPIO_BASE + GPIO_OUTPUT_EN
  sw t0, 0(t1)
  li t1, GPIO_BASE + GPIO_OUTPUT_VAL
  sw t0, 0(t1)

  li t2, 138		// 16,000,000 / 115200 - 1
  li t3, UART_BASE + UART_REG_DIV
  li t2, UART_TXEN
  li t3, UART_BASE + UART_REG_TXCTRL
  sh t2, 0(t3)

  li t2, 0x41		// t2 = 'A'

  li t3, UART_BASE + UART_REG_TXFIFO

uart_tx:
  sb t2, 0(t3)
  j uart_tx

  csrr a0, mhartid

  la a1, dtb
  li t0, XIP_TARGET_ADDR
  jr t0

  .section .rodata
dtb:
  .incbin DEVICE_TREE
