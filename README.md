# Hack-Assembler

<br />

---

This is an Assembler for the Hack computer architeture described in the book: *The Elements of Computing Systems* ([book website](http://www.nand2tetris.org)). It will take *.asm files and turn them into *.hack files that can then be run on the CPU Emulator or actual Hack hardware.

<br />

---

## Hack Assembly Language

---

The Hack architecture only has two registers, '''A''' and '''D'''. Register '''A''' can replaced with '''M''' when a location in memory needs to be referenced. That location will point to the memory location that is the value of '''A'''.

Example
<br />
Giving memory location 10 the value of 200.

'''
@10
M=200
'''