program example;
var i1, i2, i3 :integer;
var c1, c2, c3 : char;

begin{comment}
(*Demonstrate integer operations*)
i1 := 4;
i2 := 6;
i3 := i2 + i1 * i2;
write('i2 + i1 * i2 = ');
writeln(i3);

(*Demonstrate char operations*)
c1 := 'a';
writeln(c1);
c2 := 'b';
writeln(c2);
read(c3);
writeln(c3);
write('(c1c2c3) = (');
write(c1);
write(c2);
write(c3);
writeln(')');

(*Demonstrate expression with parentheses*)
i2 := i2 + i1 * (i2 mod i1);
write ('i2 = ');
writeln(i2);

(*Demonstrate if statement*)
if i1 < i2 then i3 := i1 + 5 else i3 := i2 mod 4;
write('i3 = ');
writeln(i3);

(*Demonstrate while loop*)
while i3 >= i2 do i3 := i3 - 1;
write('i3 = ');
writeln(i3);

end.