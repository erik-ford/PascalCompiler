.data
c3: .word 0
c2: .word 0
i3: .word 0
c1: .word 0
i2: .word 0
i1: .word 0
string0: .asciiz "i2 + i1 * i2 = "
string1: .asciiz "(c1c2c3) = ("
string2: .asciiz "i2 = "
string4: .asciiz "i3 = "
string6: .asciiz "i3 = "
.text
li $t0, 4
sw $t0, i1
li $t0, 6
sw $t0, i2
lw $t0, i2
lw $t1, i1
mult $t1, $t0
mflo $t2
lw $t3, i2
add $t4, $t3, $t2
sw $t4, i3
li $v0, 4
la $a0, string0
syscall
li $v0, 1
lw $a0, i3
syscall
li $v0, 11
li $a0, '\n'
syscall
li $t0, 'a'
sw $t0, c1
li $v0, 11
lb $a0, c1
syscall
li $v0, 11
li $a0, '\n'
syscall
li $t0, 'b'
sw $t0, c2
li $v0, 11
lb $a0, c2
syscall
li $v0, 11
li $a0, '\n'
syscall
li $v0, 12
syscall
sb $v0, c3
li $v0, 11
li $a0, '\n'
syscall
li $v0, 11
lb $a0, c3
syscall
li $v0, 11
li $a0, '\n'
syscall
li $v0, 4
la $a0, string1
syscall
li $v0, 11
lb $a0, c1
syscall
li $v0, 11
lb $a0, c2
syscall
li $v0, 11
lb $a0, c3
syscall
li $v0, 11
li $a0, ')'
syscall
li $v0, 11
li $a0, '\n'
syscall
lw $t0, i1
lw $t1, i2
div $t1, $t0
mfhi $t2
lw $t3, i1
mult $t3, $t2
mflo $t4
lw $t5, i2
add $t6, $t5, $t4
sw $t6, i2
li $v0, 4
la $a0, string2
syscall
li $v0, 1
lw $a0, i2
syscall
li $v0, 11
li $a0, '\n'
syscall
IF3: lw $t1, i1
lw $t2, i2
bge $t1, $t2, FALSE3
TRUE3: li $t0, 5
lw $t1, i1
add $t2, $t1, $t0
sw $t2, i3
j END3
FALSE3: li $t0, 4
lw $t1, i2
div $t1, $t0
mfhi $t2
sw $t2, i3
END3:
li $v0, 4
la $a0, string4
syscall
li $v0, 1
lw $a0, i3
syscall
li $v0, 11
li $a0, '\n'
syscall
WHILE5: lw $t1, i3
lw $t2, i2
blt $t1, $t2, END5
DO5: li $t0, 1
lw $t1, i3
sub $t2, $t1, $t0
sw $t2, i3
j WHILE5
END5:
li $v0, 4
la $a0, string6
syscall
li $v0, 1
lw $a0, i3
syscall
li $v0, 11
li $a0, '\n'
syscall
li $v0, 10
syscall
