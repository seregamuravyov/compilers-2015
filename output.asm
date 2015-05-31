extern printf
extern scanf

extern strcmp
extern strcat
extern strcpy


extern malloc
extern memcpy
extern free


struc map
.index RESD 1
.value RESD 1
.size:
endstruc



struc vecmap
.x1 RESB map.size
.x2 RESB map.size
.size:
endstruc



struc firstClass
.num RESD 1
.flag RESD 1
.size:
endstruc



struc point
.x RESD 1
.y RESD 1
.size:
endstruc



struc vector
.a RESB point.size
.b RESB point.size
.size:
endstruc



struc dvector
.v1 RESB vector.size
.v2 RESB vector.size
.size:
endstruc



section .text
global main

glob_assign:
push ebp
mov ebp, esp
pusha
push 8
call malloc
add esp, 4
mov [motherfucker], eax
popa
push 1
pop eax
mov ecx, [motherfucker]
mov [ecx + firstClass.flag], eax
push 1488
pop eax
mov ecx, [motherfucker]
mov [ecx + firstClass.num], eax
mov esp, ebp
pop ebp
ret


doNothing:
push ebp
mov ebp, esp
sub esp, 8
mov ecx, [ebp + 8]
push dword [ecx + firstClass.num]
push int_format
call printf
add esp, 8
pusha
push 8
call malloc
add esp, 4
mov [ebp - 4], eax
popa
push dword [ebp + 8]
pop eax
push dword [ebp + 8]
pop eax
pusha
push 8
push eax
push dword [ebp - 4]
call memcpy
add esp, 12
popa
mov ecx, [ebp - 4]
push 1488
pop eax
mov ecx, [ebp - 4]
mov [ecx + firstClass.num], eax
mov ecx, [ebp + 8]
push dword [ecx + firstClass.num]
push int_format
call printf
add esp, 8
mov ecx, [ebp - 4]
push dword [ecx + firstClass.num]
push int_format
call printf
add esp, 8
pusha
push dword [ebp - 4]
call free
add esp, 4
popa
mov esp, ebp
pop ebp
ret 4



recurse:
push ebp
mov ebp, esp
sub esp, 4
push dword [ebp + 8]
push 20
pop ebx
pop edx
cmp edx, ebx
jb  L67
mov eax, 0
jmp L68
L67:
mov eax, 1
L68:
push eax
pop eax
cmp eax, 1
jne L61
push dword [ebp + 8]
push 1
pop ebx
pop edx
add edx, ebx
mov eax, edx
push eax
call recurse
push eax
pop eax
mov [ebp + 8], eax
L61:
push dword [ebp + 8]
pop eax
mov esp, ebp
pop ebp
ret 4



getSqDistance:
push ebp
mov ebp, esp
sub esp, 8
mov ecx, [ebp + 8]
push dword [ecx + vector.a + point.x]
mov ecx, [ebp + 8]
push dword [ecx + vector.b + point.x]
pop ebx
pop edx
sub edx, ebx
mov eax, edx
push eax
mov ecx, [ebp + 8]
push dword [ecx + vector.a + point.x]
mov ecx, [ebp + 8]
push dword [ecx + vector.b + point.x]
pop ebx
pop edx
sub edx, ebx
mov eax, edx
push eax
pop ebx
pop eax
imul ebx
push eax
mov ecx, [ebp + 8]
push dword [ecx + vector.a + point.y]
mov ecx, [ebp + 8]
push dword [ecx + vector.b + point.y]
pop ebx
pop edx
sub edx, ebx
mov eax, edx
push eax
mov ecx, [ebp + 8]
push dword [ecx + vector.a + point.y]
mov ecx, [ebp + 8]
push dword [ecx + vector.b + point.y]
pop ebx
pop edx
sub edx, ebx
mov eax, edx
push eax
pop ebx
pop eax
imul ebx
push eax
pop ebx
pop edx
add edx, ebx
mov eax, edx
push eax
pop eax
mov [ebp - 4], eax
push dword [ebp - 4]
pop eax
mov esp, ebp
pop ebp
ret 4



getBean:
push ebp
mov ebp, esp
sub esp, 12
pusha
push 8
call malloc
add esp, 4
mov [ebp - 4], eax
popa
push 0
pop eax
mov ecx, [ebp - 4]
mov [ecx + firstClass.flag], eax
push 6
pop eax
mov ecx, [ebp - 4]
mov [ecx + firstClass.num], eax
mov ecx, [ebp - 4]
push dword [ecx + firstClass.flag]
push dword [globIntVar]
push 0
pop ebx
pop edx
cmp edx, ebx
je  L189
mov eax, 0
jmp L190
L189:
mov eax, 1
L190:
push eax
pop ebx
pop edx
cmp edx, 1
jne L177
cmp ebx, 1
jne L177
mov eax, 1
jmp L178
L177:
mov eax, 0
L178:
push eax
pop eax
mov [ebp - 8], eax
push dword [ebp - 8]
pop eax
cmp eax, 1
jne L201
mov ecx, [ebp - 4]
push dword [ecx + firstClass.flag]
push int_format
call printf
add esp, 8
jmp L202
L201:
mov ecx, [ebp - 4]
push dword [ecx + firstClass.num]
push int_format
call printf
add esp, 8
L202:
push dword [ebp - 4]
push 8
call malloc
add esp, 4
pop ebx
push 8
push ebx
push eax
call memcpy
add esp, 12
push eax
pop eax
pusha
push dword [ebp - 4]
call free
add esp, 4
popa
mov esp, ebp
pop ebp
ret



strread:
push ebp
mov ebp, esp
sub esp, 8
pusha
push 256
call malloc
add esp, 4
mov [ebp - 4], eax
push dword [ebp - 4]
push str_format
call scanf
add esp, 8
popa
push dword [ebp - 4]
push 256
call malloc
add esp, 4
pop ebx
push ebx
push eax
call strcpy
add esp, 8
push eax
pop eax
pusha
push dword [ebp - 4]
call free
add esp, 4
popa
mov esp, ebp
pop ebp
ret



main:
push ebp
mov ebp, esp
sub esp, 20
pusha
call glob_assign
popa
mov ecx, [motherfucker]
push dword [ecx + firstClass.num]
push int_format
call printf
add esp, 8
push tmp1
push str_format
call printf
add esp, 8
mov ecx, [motherfucker]
push dword [ecx + firstClass.flag]
push int_format
call printf
add esp, 8
push tmp2
push str_format
call printf
add esp, 8
push 2
pop eax
mov ecx, [motherfucker]
mov [ecx + firstClass.num], eax
push 0
pop eax
mov ecx, [motherfucker]
mov [ecx + firstClass.flag], eax
mov ecx, [motherfucker]
push dword [ecx + firstClass.num]
push int_format
call printf
add esp, 8
push tmp3
push str_format
call printf
add esp, 8
mov ecx, [motherfucker]
push dword [ecx + firstClass.flag]
push int_format
call printf
add esp, 8
push tmp4
push str_format
call printf
add esp, 8
pusha
push 8
call malloc
add esp, 4
mov [ebp - 4], eax
popa
push 0
pop eax
mov ecx, [ebp - 4]
mov [ecx + point.y], eax
push 0
pop eax
mov ecx, [ebp - 4]
mov [ecx + point.x], eax
pusha
push 8
call malloc
add esp, 4
mov [ebp - 8], eax
popa
push 4
pop eax
mov ecx, [ebp - 8]
mov [ecx + point.y], eax
push 3
pop eax
mov ecx, [ebp - 8]
mov [ecx + point.x], eax
pusha
push 16
call malloc
add esp, 4
mov [ebp - 16], eax
popa
push dword [ebp - 4]
pop eax
mov ecx, [ebp - 16]
mov edx, [eax  + point.x]
mov [ecx + vector.b + point.x], edx
mov ecx, [ebp - 16]
mov edx, [eax  + point.y]
mov [ecx + vector.b + point.y], edx
push dword [ebp - 8]
pop eax
mov ecx, [ebp - 16]
mov edx, [eax  + point.x]
mov [ecx + vector.a + point.x], edx
mov ecx, [ebp - 16]
mov edx, [eax  + point.y]
mov [ecx + vector.a + point.y], edx
pusha
push 8
call malloc
add esp, 4
mov [ebp - 12], eax
popa
mov ecx, [ebp - 16]
push dword [ecx + vector.a]
pop eax
mov ecx, [ebp - 16]
push ecx
pop eax
mov ecx, [ebp - 12]
mov edx, [eax  + vector.a + point.x]
mov [ecx  + point.x], edx
mov ecx, [ebp - 12]
mov edx, [eax  + vector.a + point.y]
mov [ecx  + point.y], edx
mov ecx, [ebp - 12]
push dword [ebp - 12]
pop eax
mov ecx, [ebp - 16]
mov edx, [eax  + point.x]
mov [ecx + vector.b + point.x], edx
mov ecx, [ebp - 16]
mov edx, [eax  + point.y]
mov [ecx + vector.b + point.y], edx
push tmp5
push str_format
call printf
add esp, 8
mov ecx, [ebp - 16]
push dword [ecx + vector.a + point.x]
push int_format
call printf
add esp, 8
push tmp6
push str_format
call printf
add esp, 8
mov ecx, [ebp - 16]
push dword [ecx + vector.a + point.y]
push int_format
call printf
add esp, 8
push tmp7
push str_format
call printf
add esp, 8
mov ecx, [ebp - 16]
push dword [ecx + vector.b + point.x]
push int_format
call printf
add esp, 8
push tmp8
push str_format
call printf
add esp, 8
mov ecx, [ebp - 16]
push dword [ecx + vector.b + point.y]
push int_format
call printf
add esp, 8
push tmp9
push str_format
call printf
add esp, 8
push 0
pop eax
pusha
push dword [ebp - 16]
call free
add esp, 4
popa
pusha
push dword [ebp - 4]
call free
add esp, 4
popa
pusha
push dword [ebp - 8]
call free
add esp, 4
popa
pusha
push dword [ebp - 12]
call free
add esp, 4
popa
mov esp, ebp
pop ebp
ret



power:
push ebp
mov ebp, esp
sub esp, 8
push 1
pop eax
mov [ebp - 4], eax
push dword [ebp + 8]
push 0
pop ebx
pop edx
cmp edx, ebx
ja  L493
mov eax, 0
jmp L494
L493:
mov eax, 1
L494:
push eax
pop eax
cmp eax, 1
jne L487
L488:
push dword [ebp + 8]
push 2
pop ebx
pop eax
mov edx, 0
idiv ebx
mov eax, edx
push eax
push 1
pop ebx
pop edx
cmp edx, ebx
je  L509
mov eax, 0
jmp L510
L509:
mov eax, 1
L510:
push eax
pop eax
cmp eax, 1
jne L501
push dword [ebp - 4]
push dword [ebp + 12]
pop ebx
pop eax
imul ebx
push eax
pop eax
mov [ebp - 4], eax
L501:
push dword [ebp + 12]
push dword [ebp + 12]
pop ebx
pop eax
imul ebx
push eax
pop eax
mov [ebp + 12], eax
push dword [ebp + 8]
push 2
pop ebx
pop eax
mov edx, 0
idiv ebx
push eax
pop eax
mov [ebp + 8], eax
push dword [ebp + 8]
push 0
pop ebx
pop edx
cmp edx, ebx
ja  L549
mov eax, 0
jmp L550
L549:
mov eax, 1
L550:
push eax
pop eax
cmp eax, 1
je L488
L487:
push dword [ebp - 4]
pop eax
mov esp, ebp
pop ebp
ret 8



section .data
int_format dd "%d", 10, 0
str_format dd "%s", 10, 0
globIntVar: dd 0
globStringVarWithInit: dd "It is global string", 0
globBoolVar: dd 0
tmp1: dd " ", 0
tmp2: dd " ", 0
tmp3: dd " ", 0
tmp4: dd " ", 0
tmp5: dd "v.a.x = ", 0
tmp6: dd "v.a.y = ", 0
tmp7: dd "v.b.x = ", 0
tmp8: dd "v.b.y = ", 0
tmp9: dd "     ", 0

section .bss
motherfucker: resb firstClass.size
