program example; 
(*var arr : array [1 .. 10] of integer;*)
var x,y:integer;
var a, b, c : char;
var z: real;
begin{comment}
x:=1;
y := 2;
a := 'a';
b:= 'b';
c := '4';
z := 1.234;
(*x := arr[2];
arr[x] := 2;*)
x := (y + x) * x;
if x < y then z := z + x else z := z + y;
while x >= y do x := x - 1;
read(x);
write(x);
read(a);
write(a);
read(z);
write(z);
writeln('hello world');
write('h');
end.