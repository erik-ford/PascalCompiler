program example(input,output);
?
var x,y:integer;
function gcd(a,b:integer):integer;
begin{gcd}
if b=0then gcd:=a else gcd:=(b,a mod b)
end;{gcd}
begin{example}
read(x,y); (*comment*)
write(gcd(x,y))
end.