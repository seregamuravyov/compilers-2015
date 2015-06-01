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



struc secondClass
.index RESD 1
.fc RESB firstClass.size
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
push 0
pop eax
mov ecx, [motherfucker]
mov [ecx + firstClass.flag], eax
push 2976
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
mov ecx, [ebp - 4]
mov edx, [eax  + firstClass.num]
mov [ecx  + firstClass.num], edx
mov ecx, [ebp - 4]
mov edx, [eax  + firstClass.flag]
mov [ecx  + firstClass.flag], edx
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
jb  L79
mov eax, 0
jmp L80
L79:
mov eax, 1
L80:
push eax
pop eax
cmp eax, 1
jne L73
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
L73:
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
call recurse
push eax
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
je  L217
mov eax, 0
jmp L218
L217:
mov eax, 1
L218:
push eax
pop ebx
pop edx
cmp edx, 1
jne L205
cmp ebx, 1
jne L205
mov eax, 1
jmp L206
L205:
mov eax, 0
L206:
push eax
pop eax
mov [ebp - 8], eax
push dword [ebp - 8]
pop eax
cmp eax, 1
jne L229
mov ecx, [ebp - 4]
push dword [ecx + firstClass.flag]
push int_format
call printf
add esp, 8
jmp L230
L229:
mov ecx, [ebp - 4]
push dword [ecx + firstClass.num]
push int_format
call printf
add esp, 8
L230:
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
sub esp, 32
pusha
call glob_assign
popa
pusha
push 8
call malloc
add esp, 4
mov [ebp - 4], eax
popa
call getBean
push eax
pop eax
mov ecx, [ebp - 4]
mov edx, [eax  + firstClass.num]
mov [ecx  + firstClass.num], edx
mov ecx, [ebp - 4]
mov edx, [eax  + firstClass.flag]
mov [ecx  + firstClass.flag], edx
pusha
push 12
call malloc
add esp, 4
mov [ebp - 8], eax
popa
call getBean
push eax
pop eax
mov ecx, [ebp - 8]
mov edx, [eax  + firstClass.num]
mov [ecx + secondClass.fc + firstClass.num], edx
mov ecx, [ebp - 8]
mov edx, [eax  + firstClass.flag]
mov [ecx + secondClass.fc + firstClass.flag], edx
push 10
pop eax
mov ecx, [ebp - 8]
mov [ecx + secondClass.index], eax
push tmp311
push str_format
call printf
add esp, 8
push 6
pop eax
mov [ebp - 12], eax
pusha
push 8
call malloc
add esp, 4
mov [ebp - 16], eax
popa
push 0
pop eax
mov ecx, [ebp - 16]
mov [ecx + point.y], eax
push 0
pop eax
mov ecx, [ebp - 16]
mov [ecx + point.x], eax
pusha
push 8
call malloc
add esp, 4
mov [ebp - 20], eax
popa
push 4
pop eax
mov ecx, [ebp - 20]
mov [ecx + point.y], eax
push 3
pop eax
mov ecx, [ebp - 20]
mov [ecx + point.x], eax
pusha
push 16
call malloc
add esp, 4
mov [ebp - 28], eax
popa
push dword [ebp - 16]
pop eax
mov ecx, [ebp - 28]
mov edx, [eax  + point.x]
mov [ecx + vector.b + point.x], edx
mov ecx, [ebp - 28]
mov edx, [eax  + point.y]
mov [ecx + vector.b + point.y], edx
push dword [ebp - 20]
pop eax
mov ecx, [ebp - 28]
mov edx, [eax  + point.x]
mov [ecx + vector.a + point.x], edx
mov ecx, [ebp - 28]
mov edx, [eax  + point.y]
mov [ecx + vector.a + point.y], edx
pusha
push 8
call malloc
add esp, 4
mov [ebp - 24], eax
popa
mov ecx, [ebp - 28]
push ecx
pop eax
mov ecx, [ebp - 24]
mov edx, [eax  + vector.a + point.x]
mov [ecx  + point.x], edx
mov ecx, [ebp - 24]
mov edx, [eax  + vector.a + point.y]
mov [ecx  + point.y], edx
push dword [ebp - 24]
pop eax
mov ecx, [ebp - 28]
mov edx, [eax  + point.x]
mov [ecx + vector.b + point.x], edx
mov ecx, [ebp - 28]
mov edx, [eax  + point.y]
mov [ecx + vector.b + point.y], edx
push 0
pop eax
pusha
push dword [ebp - 8]
call free
add esp, 4
popa
pusha
push dword [ebp - 4]
call free
add esp, 4
popa
pusha
push dword [ebp - 28]
call free
add esp, 4
popa
pusha
push dword [ebp - 16]
call free
add esp, 4
popa
pusha
push dword [ebp - 20]
call free
add esp, 4
popa
pusha
push dword [ebp - 24]
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
ja  L425
mov eax, 0
jmp L426
L425:
mov eax, 1
L426:
push eax
pop eax
cmp eax, 1
jne L419
L420:
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
je  L441
mov eax, 0
jmp L442
L441:
mov eax, 1
L442:
push eax
pop eax
cmp eax, 1
jne L433
push dword [ebp - 4]
push dword [ebp + 12]
pop ebx
pop eax
imul ebx
push eax
pop eax
mov [ebp - 4], eax
L433:
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
ja  L481
mov eax, 0
jmp L482
L481:
mov eax, 1
L482:
push eax
pop eax
cmp eax, 1
je L420
L419:
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
tmp1: dd "hello, ", 0
tmp2: dd "goddamn ", 0
tmp3: dd "motherfuckers!", 0
tmp311: dd "hello, goddamn motherfuckers!", 0

section .bss
motherfucker: resb firstClass.size
