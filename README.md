# Hack-Assembler


This is an Assembler for the Hack computer architeture described in the book: *The Elements of Computing Systems* ([book website](http://www.nand2tetris.org)). It will take \*.asm files and turn them into \*.hack files that can then be run on the CPU Emulator or actual Hack hardware.

---


## Hack Assembly Language

The Hack architecture only has two registers, `A` and `D`. Register `A` can be replaced with `M` when a location in memory needs to be referenced. That location will point to the memory location that is the value of `A`.

To give a value to the registers starts with giving a value to the `A` register first and then giving that value to the `D` register then giving that value to memory.

There are eight pre-mapped memory locations set aside for the assembler to use. These locations are: 

- `@SP` location 0
- `@LCL` location 1
- `@ARG` location 2
- `@THIS` location 3
- `@THAT` location 4
- `@R0-R15` locations 0-15 (Virtual Registers)
- `@SCREEN` screen memory location 16384
- `@KBD` keyboard memory location 24576

---

The Hack architecture only allows addition and subtraction when doing math.

The Hack architecture also only has three predefined literals, `0`, `1`, and `-1`, used like this: `A=-1`, `D=0`, `M=1`. This won't work however: `A=25`.

Jumps are done by setting the value of `A` to the line that you want to jump to in you code then using the jump condition to jump to it. Jumps can also be used with labels to jump a specific line in the code without having to know that line number.
<br />

| JUMPS |    CONDITIONS    |
| ----- | ---------------- |
|  JGT  | If value is > 0  |
|  JEQ  | If value is = 0  |
|  JGE  | If value is >= 0 |
|  JLT  | If value is < 0  |
|  JLE  | If value is <= 0 |
|  JNE  | If value is != 0 |
|  JMP  |      Jump        |

Memory location can also be represented by labels. These labeled memory locations start at location 16 and increase from there.

Comments are done by using `//`

<br />

### Examples

<br />
Setting values in the registers then to memory.

``` assembly
@10
D=A
M=D
```
<br/>
Putting the value of 200 into memory.

```assembly
@200
D=A
@10
M=D
```

<br />
Jumping to line

```assembly
@3
D=A
@10
D;JGT
@4
D=D-A
@10
D=A
D=D-1
M=D
```

<br />
Jumping to line with label

```assembly
  @3
  D=A
  @end   // Label set to value 9
  D;JGT
  @4
  D=D-A
  @10
  D=A
  D=D-1
(end)
  M=D
```

<br />
Accessing memory with labels

```assembly
  @3
  D=A
(loop)
  @counter   // counter stored in memory location 16
  M=D
  D=D-1
  @loop
  D;JLT
  D=0
```